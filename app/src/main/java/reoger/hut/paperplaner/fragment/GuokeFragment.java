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
import reoger.hut.paperplaner.adapter.GuoKrNewsAdapter;
import reoger.hut.paperplaner.bean.GuokrHandpickNews;
import reoger.hut.paperplaner.interfaces.IGuoKrContract;
import reoger.hut.paperplaner.interfaces.OnRecyclerViewOnClickListener;
import reoger.hut.paperplaner.presenter.GuokrPresenters;

/**
 * Created by 24540 on 2017/5/5.
 */

public class GuokeFragment extends Fragment implements IGuoKrContract.View{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private GuoKrNewsAdapter adapter;

    private FloatingActionButton fab;

    private IGuoKrContract.Presenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_list,container,false);
        initViews(view);

        presenter = new GuokrPresenters(getContext(),this);
        presenter.start();
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refresh();
            }
        });
        return view;
    }



    @Override
    public void setPresenter(IGuoKrContract.Presenter presenter) {
        if (presenter != null){
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.hide();
    }

    @Override
    public void showError() {
        Snackbar.make(refreshLayout, R.string.loaded_failed,Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.refresh();
                    }
                })
                .show();
    }

    @Override
    public void showResults(List<GuokrHandpickNews.result> list) {
        if (adapter == null) {
            adapter = new GuoKrNewsAdapter(getContext(), list);
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

    @Override
    public void showLoading() {
        refreshLayout.setRefreshing(true);
    }

    @Override
    public void stopLoading() {
        refreshLayout.setRefreshing(false);
    }
}
