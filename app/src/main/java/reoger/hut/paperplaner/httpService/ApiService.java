package reoger.hut.paperplaner.httpService;

import okhttp3.ResponseBody;
import reoger.hut.paperplaner.bean.DouBanDetail;
import reoger.hut.paperplaner.bean.DouBanNews;
import reoger.hut.paperplaner.bean.GuokrHandpickNews;
import reoger.hut.paperplaner.bean.GankXiatuijian;
import reoger.hut.paperplaner.bean.ZhiHuLatest;
import reoger.hut.paperplaner.bean.ZhihuDetail;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by 24540 on 2017/5/5.
 * 用于发送网络请求
 */

public interface ApiService {

    //获取知乎最新的消息
    @GET("latest")
    Observable<ZhiHuLatest> getZhiHuLatest();

    //获取知乎指定时间段的的消息
    @GET("before/{time}")
    Observable<ZhiHuLatest> getZhihuByTime(@Path("time")String time);

    @GET("{id}")
    Observable<ZhihuDetail> getZhihuDetail(@Path("id")int id);

    //获取果壳精选
    @GET("article.json?retrieve_type=by_since&category=all&limit=25&ad=1")
    Observable<GuokrHandpickNews> getGuokrNews();

    @GET("{id}")
    Observable<ResponseBody> getGuokrDetail(@Path("id")int id);

    @GET("stream/date/{date}")
    Observable<DouBanNews> getDoubanNews(@Path("date")String date);

    @GET("post/{id}")
    Observable<DouBanDetail> getDouBanNewsDetail(@Path("id")int id);

    @GET("Android/{count}")
    Observable<GankXiatuijian> getGanxiaTuijian(@Path("count") int count);
}
