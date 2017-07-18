package reoger.hut.paperplaner.httpService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import reoger.hut.paperplaner.bean.GuokrHandpickNews;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 24540 on 2017/5/7.
 */

public class HttpMethodsA {

    private static final int DEFAULT_TIMEOUT = 5;
    private static String URl = " http://apis.guokr.com/handpick/";

    private Retrofit retrofit;
    private ApiService httpService;

    private HttpMethodsA(){
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
        private static final HttpMethodsA INSTANCE = new HttpMethodsA();
    }

    //获取单例
    public static HttpMethodsA getInstance(){
        return SingletonHolder.INSTANCE;
    }


    public void getGuoKrNews(Subscriber<GuokrHandpickNews> subscriber){
        httpService.getGuokrNews()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
