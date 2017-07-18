package reoger.hut.paperplaner.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;

import org.greenrobot.greendao.query.Query;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import hut.reoger.gen.DaoSession;
import hut.reoger.gen.DbZhihuDao;
import reoger.hut.paperplaner.activity.DetailActivity;
import reoger.hut.paperplaner.app.App;
import reoger.hut.paperplaner.bean.BeanType;
import reoger.hut.paperplaner.bean.DbZhihu;
import reoger.hut.paperplaner.bean.ZhiHuLatest;
import reoger.hut.paperplaner.httpService.Api;
import reoger.hut.paperplaner.httpService.HttpMethods;
import reoger.hut.paperplaner.interfaces.IZhihuContract;
import reoger.hut.paperplaner.service.CacheService;
import reoger.hut.paperplaner.util.DateFormatter;
import reoger.hut.paperplaner.util.NetworkState;
import reoger.hut.paperplaner.util.log;
import rx.Subscriber;

/**
 * Created by 24540 on 2017/5/6.
 * 知乎日报的实现类
 */

public class ZhihuPresenters implements IZhihuContract.Presenter {

    private IZhihuContract.View view;
    private Context context;

    private List<ZhiHuLatest.StoriesBean> list = new ArrayList<>();

    private DaoSession daoSession = App.getInstance().getDaoSession();
    private DbZhihuDao dbZhihuDao;

    private Gson gson = new Gson();
    private Date dateFrom = null;
    private DateFormat format = new SimpleDateFormat("yyyyMMdd");

    private DateFormatter formatter = new DateFormatter();

    public ZhihuPresenters(IZhihuContract.View view, Context context) {
        this.view = view;
        this.context = context;
        this.view.setPresenter(this);
        dbZhihuDao = daoSession.getDbZhihuDao();
    }

    @Override
    public void start() {
        loadPosts(Calendar.getInstance().getTimeInMillis(),true);
    }

    @Override
    public void loadPosts(final long date, final boolean clearing) {
        if(clearing)
            view.showLoading();

        if(NetworkState.networkConnected(context)){
            Subscriber<ZhiHuLatest> subscriber = new Subscriber<ZhiHuLatest>() {
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
                public void onNext(ZhiHuLatest zhiHuLatest) {
                    if(clearing){
                        list.clear();
                    }

                    list.addAll(zhiHuLatest.getStories());
                    view.showResults(list);

                    for (ZhiHuLatest.StoriesBean storiesBean : zhiHuLatest.getStories()) {
                        Query<DbZhihu> build = dbZhihuDao.queryBuilder().where(DbZhihuDao.Properties.Id.eq(storiesBean.getId())).build();
                        if(build.list().size() == 0){
                            InsertDbZhihu(zhiHuLatest, storiesBean);
                        }else{
                            //update if necessary
                            log.d("数据重复，不在添加");
                        }
                    }
                }
            };
            HttpMethods.getInstance(Api.ZHIHU_NEWS).getZHihuByTime(subscriber,formatter.ZhihuDailyDateFormat(date));
        }else{
            log.e("没有网络连接");
            if (clearing) {
            Query<DbZhihu> query = dbZhihuDao.queryBuilder().orderAsc(DbZhihuDao.Properties.Date).build();
            for (DbZhihu dbZhihu : query.list()) {
                list.add(gson.fromJson(dbZhihu.getZhihu_news(),ZhiHuLatest.StoriesBean.class));
            }
            log.d("从数据库中读取的"+list.size()+"条数据");
            view.stopLoading();
            view.showResults(list);

            }else{
                view.showError();
            }

        }

    }

    private void InsertDbZhihu(ZhiHuLatest zhiHuLatest, ZhiHuLatest.StoriesBean storiesBean) {
        try {
            dateFrom = format.parse(zhiHuLatest.getDate());
            DbZhihu dbZhihu = new DbZhihu(storiesBean.getId(),gson.toJson(storiesBean),"",dateFrom,false);
            dbZhihuDao.insert(dbZhihu);
            Intent intent = new Intent("com.marktony.zhihudaily.LOCAL_BROADCAST");
            intent.putExtra("type", CacheService.TYPE_ZHIHU);
            intent.putExtra("id", storiesBean.getId());
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            log.d("成功发soon个清流");
        } catch (ParseException e) {
            e.printStackTrace();
            view.showError();
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
    public void startReading(int position) {
        context.startActivity(new Intent(context, DetailActivity.class)
                .putExtra("type", BeanType.TYPE_ZHIHU)
                .putExtra("id", list.get(position).getId())
                .putExtra("title", list.get(position).getTitle())
                .putExtra("coverUrl", list.get(position).getImages().get(0)));

    }

    @Override
    public void feelLucky() {

    }
}
