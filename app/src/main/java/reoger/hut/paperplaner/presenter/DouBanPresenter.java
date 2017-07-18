package reoger.hut.paperplaner.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import hut.reoger.gen.DaoSession;
import hut.reoger.gen.DbDoubanDao;
import reoger.hut.paperplaner.activity.DetailActivity;
import reoger.hut.paperplaner.app.App;
import reoger.hut.paperplaner.bean.BeanType;
import reoger.hut.paperplaner.bean.DbDouban;
import reoger.hut.paperplaner.bean.DouBanNews;
import reoger.hut.paperplaner.httpService.HttpMethodForDouBan;
import reoger.hut.paperplaner.interfaces.IDouBanContract;
import reoger.hut.paperplaner.service.CacheService;
import reoger.hut.paperplaner.util.DateFormatter;
import reoger.hut.paperplaner.util.NetworkState;
import reoger.hut.paperplaner.util.log;
import rx.Subscriber;

/**
 * Created by 24540 on 2017/5/8.
 */

public class DouBanPresenter implements IDouBanContract.Presenter {

    private IDouBanContract.View view;
    private Context context;
    private List<DouBanNews.PostsBean> list = new ArrayList<>();
    private Gson gson = new Gson();

    private DbDoubanDao dbDoubanDao;
    private DaoSession daoSession = App.getInstance().getDaoSession();

    public DouBanPresenter(IDouBanContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.view.setPresenter(this);
        dbDoubanDao = daoSession.getDbDoubanDao();
    }


    @Override
    public void start() {
        refresh();
    }

    @Override
    public void startReading(int position) {
        DouBanNews.PostsBean item = list.get(position);
        Intent intent = new Intent(context, DetailActivity.class);

        intent.putExtra("type", BeanType.TYPE_DOUBAN);
        intent.putExtra("id", item.getId());
        intent.putExtra("title", item.getTitle());
        if (item.getThumbs().size() == 0) {
            intent.putExtra("coverUrl", "");
        } else {
            intent.putExtra("coverUrl", item.getThumbs().get(0).getMedium().getUrl());
        }
        context.startActivity(intent);
    }

    @Override
    public void loadPosts(final long date, final boolean clearing) {
        if (NetworkState.networkConnected(context)) {
            if (clearing)
                view.startLoading();
            Subscriber<DouBanNews> subscriber = new Subscriber<DouBanNews>() {
                @Override
                public void onCompleted() {
                    log.e(list.size() + "-----");
                    view.stopLoading();
                    view.showResults(list);
                }

                @Override
                public void onError(Throwable e) {
                    log.e("出错拉" + e.toString());
                    view.stopLoading();
                    view.showLoadingError();
                }

                @Override
                public void onNext(DouBanNews postsBean) {
                    if (clearing)
                        list.clear();
                    list.addAll(postsBean.getPosts());

                    for (DouBanNews.PostsBean bean : postsBean.getPosts()) {
                        DbDouban dbDouban = new DbDouban((long) bean.getId(), gson.toJson(bean), date, "",false);
                        dbDoubanDao.insertOrReplace(dbDouban);
                        Intent intent = new Intent("com.marktony.zhihudaily.LOCAL_BROADCAST");
                        intent.putExtra("type", CacheService.TYPE_DOUBAN);
                        intent.putExtra("id", bean.getId());
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    }
                }

                @Override
                public void onStart() {
                    view.startLoading();
                }
            };
            HttpMethodForDouBan.getInstance().getDouBanNews(subscriber, new DateFormatter().DoubanDateFormat(date));


        } else {
            list.clear();
            Query<DbDouban> query = dbDoubanDao.queryBuilder().orderAsc(DbDoubanDao.Properties.Douban_id).build();
            for (DbDouban dbDouban : query.list()) {
                list.add(gson.fromJson(dbDouban.getDouban_news(), DouBanNews.PostsBean.class));
            }
            view.stopLoading();
            view.showResults(list);
        }
    }

    @Override
    public void refresh() {
        loadPosts(Calendar.getInstance().getTimeInMillis(), true);
    }

    @Override
    public void loadMore(long date) {
        loadPosts(date, false);
    }

    @Override
    public void feelLucky() {
        if (list.isEmpty()) {
            view.showLoadingError();
            return;
        }
        startReading(new Random().nextInt(list.size()));
    }
}
