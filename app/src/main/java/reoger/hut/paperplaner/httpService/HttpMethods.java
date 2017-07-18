package reoger.hut.paperplaner.httpService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import reoger.hut.paperplaner.bean.ZhiHuLatest;
import reoger.hut.paperplaner.bean.ZhihuDetail;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 24540 on 2017/5/6.
 * 封装的网络请求：
 */

public class HttpMethods {

    private static final int DEFAULT_TIMEOUT = 5;
    private static String URl = Api.ZHIHU_NEWS;

    private Retrofit retrofit;
    private ApiService httpService;



    private HttpMethods(){
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);


        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(URl)
                .build();

        httpService = retrofit.create(ApiService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance(String url){
        URl = url;
        return SingletonHolder.INSTANCE;
    }


    /**
     * 获取知乎最新的数据
     * @param subscriber
     */
    public void getZhiHuLatest(Subscriber<ZhiHuLatest> subscriber){
        httpService.getZhiHuLatest()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 根据时间获取知乎的数据
     * @param subscriber
     * @param time
     */
    public void getZHihuByTime(Subscriber<ZhiHuLatest> subscriber,String time){
        httpService.getZhihuByTime(time)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 获取支付的详细界面
     * @param subscriber
     * @param id
     */
    public void getZhihuDetail(Subscriber<ZhihuDetail> subscriber,int id){
        httpService.getZhihuDetail(id)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

//    public void getGuoKrNews(Subscriber<GuokrHandpickNews.result> subscriber){
//        httpService.getGuokrNews()
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
//    }
}
