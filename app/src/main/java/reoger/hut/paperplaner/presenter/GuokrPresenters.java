package reoger.hut.paperplaner.presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.gson.Gson;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hut.reoger.gen.DaoSession;
import hut.reoger.gen.DbGuokrDao;
import reoger.hut.paperplaner.activity.DetailActivity;
import reoger.hut.paperplaner.app.App;
import reoger.hut.paperplaner.bean.BeanType;
import reoger.hut.paperplaner.bean.DbGuokr;
import reoger.hut.paperplaner.bean.GuokrHandpickNews;
import reoger.hut.paperplaner.httpService.HttpMethodsA;
import reoger.hut.paperplaner.interfaces.IGuoKrContract;
import reoger.hut.paperplaner.service.CacheService;
import reoger.hut.paperplaner.util.NetworkState;
import reoger.hut.paperplaner.util.log;
import rx.Subscriber;

/**
 * Created by 24540 on 2017/5/7.
 *
 */

public class GuokrPresenters implements IGuoKrContract.Presenter {
    private Context context;
    private IGuoKrContract.View view;
    private DbGuokrDao dbGuokrDao;
    private DaoSession daoSession = App.getInstance().getDaoSession();
    private Gson gson = new Gson();

    private List<GuokrHandpickNews.result> list = new ArrayList<>();

    public GuokrPresenters(Context context, IGuoKrContract.View view) {
        this.context = context;
        this.view = view;
        view.setPresenter(this);
        dbGuokrDao = daoSession.getDbGuokrDao();
    }



    @Override
    public void start() {
        loadPosts();
    }

    @Override
    public void loadPosts() {

        if(NetworkState.networkConnected(context)){
            Subscriber<GuokrHandpickNews> subscriber = new Subscriber<GuokrHandpickNews>() {
                @Override
                public void onCompleted() {
                    view.stopLoading();
                }

                @Override
                public void onError(Throwable e) {
                    view.stopLoading();
                    log.d("这里发生了什么"+e.getMessage());
                    //view.showError();
                }

                @Override
                public void onNext(GuokrHandpickNews result) {
                    list.addAll(result.getResult());
                    view.showResults(list);

                    InsertDbGuokr(result);
                }



                @Override
                public void onStart() {
                    view.showLoading();
                }
            };
            HttpMethodsA.getInstance().getGuoKrNews(subscriber);



        }else{
            log.e("没有网络连接");
            Query<DbGuokr> query = dbGuokrDao.queryBuilder().orderAsc(DbGuokrDao.Properties.Guokr_time).build();
            list.clear();
            for (DbGuokr dbGuokr : query.list()) {
                list.add(gson.fromJson(dbGuokr.getGuolkr_news(),GuokrHandpickNews.result.class));
            }
            log.e("从数据库中读取+条信息");
            view.stopLoading();
            view.showResults(list);
        }


    }

    @Override
    public void refresh() {
        loadPosts();
    }

    @Override
    public void startReading(int position) {
        GuokrHandpickNews.result item = list.get(position);
        context.startActivity(new Intent(context, DetailActivity.class)
                .putExtra("type", BeanType.TYPE_GUOKR)
                .putExtra("id", item.getId())
                .putExtra("coverUrl", item.getHeadline_img())
                .putExtra("title", item.getTitle())
        );
    }

    @Override
    public void feelLucky() {
        if (list.isEmpty()) {
            view.showError();
            return;
        }
        startReading(new Random().nextInt(list.size()));
    }


    private void InsertDbGuokr(GuokrHandpickNews result) {
        for (GuokrHandpickNews.result beanResult : result.getResult()) {
            Query<DbGuokr> dbGuokrQuery = dbGuokrDao.queryBuilder().where(DbGuokrDao.Properties.Guokr_id.eq(beanResult.getId())).build();
            if(dbGuokrQuery.list().size() ==0){
                DbGuokr guokr = new DbGuokr();
                guokr.setGuokr_id(beanResult.getId());
                guokr.setGuolkr_news(gson.toJson(beanResult));
                guokr.setGuokr_time((float) beanResult.getDate_picked());
                guokr.setGuokr_content("");
                dbGuokrDao.insert(guokr);

                Intent intent = new Intent("com.marktony.zhihudaily.LOCAL_BROADCAST");
                intent.putExtra("type", CacheService.TYPE_GUOKR);
                intent.putExtra("id", beanResult.getId());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }else{
                log.e("添加失败，数据重复！");
            }
        }
    }

}
