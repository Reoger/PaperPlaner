package reoger.hut.paperplaner.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;

import reoger.hut.paperplaner.R;
import reoger.hut.paperplaner.adapter.ZhihuNewsAdapter;
import reoger.hut.paperplaner.bean.ZhiHuLatest;
import reoger.hut.paperplaner.interfaces.IZhihuContract;
import reoger.hut.paperplaner.interfaces.OnRecyclerViewOnClickListener;
import reoger.hut.paperplaner.presenter.ZhihuPresenters;


/**
 * Created by 24540 on 2017/5/5.
 * 首页：用于显示知乎日报的
 */

public class ZhihuFragment extends Fragment implements IZhihuContract.View{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private ZhihuNewsAdapter adapter;

    private FloatingActionButton fab;

    private IZhihuContract.Presenter presenter;

    private int mYear = Calendar.getInstance().get(Calendar.YEAR);
    private int mMonth = Calendar.getInstance().get(Calendar.MONTH);
    private int mDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_list,container,false);

        initViews(view);

        presenter = new ZhihuPresenters(this,getContext());
        presenter.start();

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });


        // 直接将豆瓣精选的fab点击事件放在知乎的部分
        // 因为fab是属于activity的view
        // 按通常的做法，在每个fragment中去设置监听时间会导致先前设置的listener失效
        // 尝试将监听放置到main pager adapter中，这样做会引起fragment中recycler view和fab的监听冲突
        // fab并不能获取到点击事件
        // 根据tab layout的位置选择显示不同的dialog
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                if(SwitchFragment.isDouBanOnClick){
//                    log.e("点了的是豆瓣");
//                }else{
//                    log.e("点击的是知乎日报");
//                }


                Calendar now = Calendar.getInstance();
                    now.set(mYear, mMonth, mDay);
                    DatePickerDialog dialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;
                            Calendar temp = Calendar.getInstance();
                            temp.clear();
                            temp.set(year, monthOfYear, dayOfMonth);
                            presenter.loadPosts(temp.getTimeInMillis(), true);
                        }
                    }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

                    dialog.setMaxDate(Calendar.getInstance());
                    Calendar minDate = Calendar.getInstance();
                    // 2013.5.20是知乎日报api首次上线
                    minDate.set(2013, 5, 20);
                    dialog.setMinDate(minDate);
                    dialog.vibrate(false);

                    dialog.show(getActivity().getFragmentManager(), "DatePickerDialog");

            }
        });

        return view;
    }




    public void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            boolean isSlidingToLast = false;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    // 当不滚动时
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        // 获取最后一个完全显示的item position
                        int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                        int totalItemCount = manager.getItemCount();

                        // 判断是否滚动到底部并且是向下滑动
                        if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                            Calendar c = Calendar.getInstance();
                            c.set(mYear, mMonth, --mDay);
                            Log.d("TAG","测试时间"+mDay);
                            presenter.loadMore(c.getTimeInMillis());
                        }
                    }

                    super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
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
    public void setPresenter(IZhihuContract.Presenter presenter) {
        if(presenter != null){
        this.presenter = presenter;
        }
    }

    @Override
    public void showError() {
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
    public void showLoading() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void stopLoading() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void showResults(List<ZhiHuLatest.StoriesBean> list) {
        if(adapter == null ){
            adapter  = new ZhihuNewsAdapter(getContext(),list);
            adapter.setItemClickListenter(new OnRecyclerViewOnClickListener() {
                @Override
                public void OnItemClick(View v, int position) {
                    presenter.startReading(position);
                }
            });
            recyclerView.setAdapter(adapter);
        }else{
            adapter.addData(list);
        }
    }
}
