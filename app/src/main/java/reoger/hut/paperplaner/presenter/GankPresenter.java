package reoger.hut.paperplaner.presenter;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import reoger.hut.paperplaner.activity.DetailActivity;
import reoger.hut.paperplaner.bean.BeanType;
import reoger.hut.paperplaner.bean.GankXiatuijian;
import reoger.hut.paperplaner.httpService.HttpMethodsForXiatuijan;
import reoger.hut.paperplaner.interfaces.IGankContract;
import rx.Subscriber;

/**
 * Created by 24540 on 2017/5/10.
 */

public class GankPresenter implements IGankContract.Presenter {
    private List<GankXiatuijian.ResultsBean> list = new ArrayList<>();

    private boolean isClear = false;

    private IGankContract.View view;
    private Context context;

    public GankPresenter(IGankContract.View view, Context context) {
        this.view = view;
        this.context = context;
    }


    @Override
    public void start() {
        loadPosts(20);
    }

    @Override
    public void loadPosts(int count) {

        Subscriber<GankXiatuijian> subscriber = new Subscriber<GankXiatuijian>() {
            @Override
            public void onCompleted() {
                view.stopLoading();
            }

            @Override
            public void onError(Throwable e) {
                view.stopLoading();
                view.showError();
            }

            @Override
            public void onNext(GankXiatuijian gankXiatuijian) {
                if(isClear)
                    list.clear();
                list.addAll(gankXiatuijian.getResults());
                view.showResults(list);
            }

            @Override
            public void onStart() {
                view.showLoading();
            }
        };
        HttpMethodsForXiatuijan.getInstance().getXiaTuijan(subscriber,count);
    }

    @Override
    public void refresh() {
        loadPosts(20);
    }

    @Override
    public void startReading(int position) {
        GankXiatuijian.ResultsBean item = list.get(position);
        context.startActivity(new Intent(context, DetailActivity.class)
                .putExtra("type", BeanType.TYPE_GANK)
                .putExtra("id", 1)
                .putExtra("coverUrl", item.getUrl())
                .putExtra("title", item.getDesc()));
    }

    @Override
    public void feelLucky() {

    }
}
