package reoger.hut.paperplaner.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;

import reoger.hut.paperplaner.R;
import reoger.hut.paperplaner.adapter.DouBanNewsAdapter;
import reoger.hut.paperplaner.bean.DouBanNews;
import reoger.hut.paperplaner.interfaces.IDouBanContract;
import reoger.hut.paperplaner.interfaces.OnRecyclerViewOnClickListener;
import reoger.hut.paperplaner.presenter.DouBanPresenter;

/**
 * Created by 24540 on 2017/5/5.
 */

public class DouBanFragment extends Fragment implements IDouBanContract.View{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton fab;

    private DouBanNewsAdapter adapter;
    private IDouBanContract.Presenter presenter;

    private int mYear = Calendar.getInstance().get(Calendar.YEAR);
    private int mMonth = Calendar.getInstance().get(Calendar.MONTH);
    private int mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    public DouBanFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_list,container,false);
        initViews(view);

        presenter = new DouBanPresenter(this,getContext());
        presenter.start();

        addListenerToFab();
        return view;
    }

    private void addListenerToFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickDialog();
            }
        });
    }


    @Override
    public void setPresenter(IDouBanContract.Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
      //  fab.setRippleColor(getResources().getColor(R.color.colorPrimaryDark));

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        //设置下拉刷新的按钮的颜色
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);

        fab.hide();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadPosts(Calendar.getInstance().getTimeInMillis(), true);
            }
        });

       recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
           boolean isSlidingToLast = false;

           @Override
           public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
               LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
               // 当不滚动时
               if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                   // 获取最后一个完全显示的itemposition
                   int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                   int totalItemCount = manager.getItemCount();

                   // 判断是否滚动到底部并且是向下滑动
                   if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                       Calendar c = Calendar.getInstance();
                       c.set(mYear, mMonth, --mDay);
                       presenter.loadMore(c.getTimeInMillis());
                   }
               }
               super.onScrollStateChanged(recyclerView, newState);
           }

           @Override
           public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
               super.onScrolled(recyclerView, dx, dy);
               isSlidingToLast = dy > 0;

               // 隐藏或者显示fab
               if(dy > 0) {
                   fab.hide();
               } else {
                   fab.show();
               }
           }
       });
    }

    @Override
    public void startLoading() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void stopLoading() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoadingError() {
        Snackbar.make(fab, R.string.loaded_failed,Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.refresh();
                    }
                })
                .show();
    }

    @Override
    public void showResults(List<DouBanNews.PostsBean> list) {
        if (adapter == null) {
            adapter = new DouBanNewsAdapter(getContext(), list);
            adapter.setItemClickListener(new OnRecyclerViewOnClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    presenter.startReading(position);
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    public void showPickDialog() {

        Calendar now = Calendar.getInstance();
        now.set(mYear, mMonth, mDay);
        DatePickerDialog dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                Calendar temp = Calendar.getInstance();
                temp.clear();
                temp.set(year, monthOfYear, dayOfMonth);
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;
                presenter.loadPosts(temp.getTimeInMillis(), true);
            }
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

        dialog.setMaxDate(Calendar.getInstance());
        Calendar minDate = Calendar.getInstance();
        minDate.set(2014, 5, 12);
        dialog.setMinDate(minDate);
        // set the dialog not vibrate when date change, default value is true
        dialog.vibrate(false);

        dialog.show(getActivity().getFragmentManager(), "DatePickerDialog");

    }

}
