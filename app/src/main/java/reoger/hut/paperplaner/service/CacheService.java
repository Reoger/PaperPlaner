package reoger.hut.paperplaner.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import org.greenrobot.greendao.query.Query;

import java.io.IOException;

import hut.reoger.gen.DaoSession;
import hut.reoger.gen.DbDoubanDao;
import hut.reoger.gen.DbGuokrDao;
import hut.reoger.gen.DbZhihuDao;
import okhttp3.ResponseBody;
import reoger.hut.paperplaner.app.App;
import reoger.hut.paperplaner.bean.DbDouban;
import reoger.hut.paperplaner.bean.DbGuokr;
import reoger.hut.paperplaner.bean.DbZhihu;
import reoger.hut.paperplaner.bean.DouBanDetail;
import reoger.hut.paperplaner.bean.ZhihuDetail;
import reoger.hut.paperplaner.httpService.Api;
import reoger.hut.paperplaner.httpService.HttpMethodForDouBan;
import reoger.hut.paperplaner.httpService.HttpMethods;
import reoger.hut.paperplaner.httpService.HttpMethodsB;
import reoger.hut.paperplaner.util.log;
import rx.Subscriber;

/**
 * Created by 24540 on 2017/5/11.
 *
 */

public class CacheService extends Service {

    public static final int TYPE_ZHIHU = 0x00;
    public static final int TYPE_GUOKR = 0x01;
    public static final int TYPE_DOUBAN = 0x02;

    private DbZhihuDao dbZhihuDao;
    private DbGuokrDao dbGuokrDao;
    private DbDoubanDao dbDoubanDao;
    private DaoSession daoSession = App.getInstance().getDaoSession();
    private Query<DbZhihu> ZhiHuQuery;

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter();
        filter.addAction("com.marktony.zhihudaily.LOCAL_BROADCAST");
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
        manager.registerReceiver(new LocalReceiver(),filter);
        dbZhihuDao = daoSession.getDbZhihuDao();
        dbGuokrDao = daoSession.getDbGuokrDao();
        dbDoubanDao = daoSession.getDbDoubanDao();

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private void startZhihuCache(int id) {
        ZhiHuQuery = dbZhihuDao.queryBuilder().where(DbZhihuDao.Properties.Id.eq(id)).build();
        final DbZhihu  dbZhihu= ZhiHuQuery.list().get(0);

        Subscriber<ZhihuDetail> subs = new Subscriber<ZhihuDetail>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            log.d("this has gone wrong!"+e);
            }

            @Override
            public void onNext(ZhihuDetail zhihuDetail) {
                dbZhihu.setZhihu_content(zhihuDetail.getBody());
                dbZhihuDao.update(dbZhihu);
            }
        };
        HttpMethods.getInstance(Api.ZHIHU_NEWS).getZhihuDetail(subs,id);

    }



    private void startGuokrCache(int id) {

        final DbGuokr guokr =  dbGuokrDao.queryBuilder().where(DbGuokrDao.Properties.Guokr_id.eq(id)).build().list().get(0);
     Subscriber<ResponseBody> sub = new Subscriber<ResponseBody>() {
         @Override
         public void onCompleted() {
             log.d("缓存完成");
         }

         @Override
         public void onError(Throwable e) {
            log.e("guokr数据缓存失败");
         }

         @Override
         public void onNext(ResponseBody responseBody) {
             try {
                 guokr.setGuokr_content(responseBody.string());
                 dbGuokrDao.update(guokr);
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }
     };


        HttpMethodsB.getInstance().getGuokrDetail(sub,id);

    }

    private void startDoubanCache(int id) {
        final DbDouban douBan = dbDoubanDao.queryBuilder().where(DbDoubanDao.Properties.Douban_id.eq(id)).build().list().get(0);
            Subscriber<DouBanDetail> subscriber2 = new Subscriber<DouBanDetail>() {
                @Override
                public void onCompleted() {
                    log.e("douBan 缓存完成");
                }

                @Override
                public void onError(Throwable e) {
                    log.e("douBan 缓存失败"+e.toString());
                }

                @Override
                public void onNext(DouBanDetail douBanDetail) {
                   douBan.setDouban_content(douBanDetail.getContent());
                    dbDoubanDao.update(douBan);
                }
            };
            HttpMethodForDouBan.getInstance().getDouBanNewsDetali(subscriber2, id);

    }



    class LocalReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int id = intent.getIntExtra("id", 0);
            switch (intent.getIntExtra("type", -1)) {
                case TYPE_ZHIHU:
                    startZhihuCache(id);
                    break;
                case TYPE_GUOKR:
                    startGuokrCache(id);
                    break;
                case TYPE_DOUBAN:
                    startDoubanCache(id);
                    break;
                default:
                case -1:
                    break;
            }
        }

    }
}
