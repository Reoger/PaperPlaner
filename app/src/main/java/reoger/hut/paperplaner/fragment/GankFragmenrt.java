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

import java.util.List;

import reoger.hut.paperplaner.R;
import reoger.hut.paperplaner.adapter.GankTuijianAdapter;
import reoger.hut.paperplaner.bean.GankXiatuijian;
import reoger.hut.paperplaner.interfaces.IGankContract;
import reoger.hut.paperplaner.interfaces.OnRecyclerViewOnClickListener;
import reoger.hut.paperplaner.presenter.GankPresenter;

/**
 * Created by 24540 on 2017/5/10.
 *
 */

public class GankFragmenrt extends Fragment implements IGankContract.View{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private FloatingActionButton fab;

    private GankTuijianAdapter adapter;
    private IGankContract.Presenter presenter;

    private static int BASE_COUNT = 20;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_list,container,false);

        initViews(view);

        presenter = new GankPresenter(this,getContext());
        presenter.start();
        return view;
    }

    @Override
    public void setPresenter(IGankContract.Presenter presenter) {

    }

    @Override
    public void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.hide();

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        //设置下拉刷新的按钮的颜色

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadPosts(BASE_COUNT);
                BASE_COUNT+=20;
                if(BASE_COUNT>99)
                    BASE_COUNT = 20;
            }
        });


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
    public void showResults(List<GankXiatuijian.ResultsBean> list) {
        if(adapter == null){
            adapter = new GankTuijianAdapter(getContext(),list);
          adapter.setItemClickListener(new OnRecyclerViewOnClickListener() {
              @Override
              public void OnItemClick(View v, int position) {
                  presenter.startReading(position);
              }
          });
            recyclerView.setAdapter(adapter);
        }else{
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showLoading() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void stopLoading() {
        refreshLayout.setRefreshing(false);
    }
}
