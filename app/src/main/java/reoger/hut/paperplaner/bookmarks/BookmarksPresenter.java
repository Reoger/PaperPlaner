package reoger.hut.paperplaner.bookmarks;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.Random;

import hut.reoger.gen.DaoSession;
import hut.reoger.gen.DbDoubanDao;
import hut.reoger.gen.DbGuokrDao;
import hut.reoger.gen.DbZhihuDao;
import reoger.hut.paperplaner.activity.DetailActivity;
import reoger.hut.paperplaner.app.App;
import reoger.hut.paperplaner.bean.BeanType;
import reoger.hut.paperplaner.bean.DbDouban;
import reoger.hut.paperplaner.bean.DbGuokr;
import reoger.hut.paperplaner.bean.DbZhihu;
import reoger.hut.paperplaner.bean.DouBanNews;
import reoger.hut.paperplaner.bean.GuokrHandpickNews;
import reoger.hut.paperplaner.bean.ZhiHuLatest;

import static reoger.hut.paperplaner.bookmarks.BookmarksAdapter.TYPE_DOUBAN_NORMAL;
import static reoger.hut.paperplaner.bookmarks.BookmarksAdapter.TYPE_DOUBAN_WITH_HEADER;
import static reoger.hut.paperplaner.bookmarks.BookmarksAdapter.TYPE_GUOKR_NORMAL;
import static reoger.hut.paperplaner.bookmarks.BookmarksAdapter.TYPE_GUOKR_WITH_HEADER;
import static reoger.hut.paperplaner.bookmarks.BookmarksAdapter.TYPE_ZHIHU_NORMAL;
import static reoger.hut.paperplaner.bookmarks.BookmarksAdapter.TYPE_ZHIHU_WITH_HEADER;

/**
 * Created by 24540 on 2017/7/7.
 *
 */

public class BookmarksPresenter implements BookmarksContract.Presenter {
    private BookmarksContract.View view;
    private Context context;
    private Gson gson;

    private ArrayList<DouBanNews.PostsBean> doubanList;
    private ArrayList<GuokrHandpickNews.result> guokrList;
    private ArrayList<ZhiHuLatest.StoriesBean> zhihuList;

    private ArrayList<Integer> types;

    private DaoSession daoSession = App.getInstance().getDaoSession();

    private DbZhihuDao dbZhihuDao;
    private DbGuokrDao dbGuokrDao;
    private DbDoubanDao dbDoubanDao;


    public BookmarksPresenter(Context context, BookmarksContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        gson = new Gson();

        dbDoubanDao = daoSession.getDbDoubanDao();
        dbGuokrDao  = daoSession.getDbGuokrDao();
        dbZhihuDao  = daoSession.getDbZhihuDao();

        zhihuList = new ArrayList<>();
        guokrList = new ArrayList<>();
        doubanList = new ArrayList<>();

        types = new ArrayList<>();

    }

    @Override
    public void start() {

    }

    @Override
    public void loadResults(boolean refresh) {

        if (!refresh) {
            view.showLoading();
        } else {
            zhihuList.clear();
            guokrList.clear();
            doubanList.clear();
            types.clear();
        }

        checkForFreshData();

        view.showResults(zhihuList, guokrList, doubanList, types);

        view.stopLoading();

    }

    @Override
    public void startReading(BeanType type, int position) {
        Intent intent = new Intent(context, DetailActivity.class);
        switch (type) {
            case TYPE_ZHIHU:
                ZhiHuLatest.StoriesBean q = zhihuList.get(position - 1);
                intent.putExtra("type", BeanType.TYPE_ZHIHU);
                intent.putExtra("id",q.getId());
                intent.putExtra("title", q.getTitle());
                intent.putExtra("coverUrl", q.getImages().get(0));
                break;

            case TYPE_GUOKR:
                GuokrHandpickNews.result r = guokrList.get(position - zhihuList.size() - 2);
                intent.putExtra("type", BeanType.TYPE_GUOKR);
                intent.putExtra("id", r.getId());
                intent.putExtra("title", r.getTitle());
                intent.putExtra("coverUrl", r.getHeadline_img());
                break;
            case TYPE_DOUBAN:
               DouBanNews.PostsBean p = doubanList.get(position - zhihuList.size() - guokrList.size() - 3);
                intent.putExtra("type", BeanType.TYPE_DOUBAN);
                intent.putExtra("id", p.getId());
                intent.putExtra("title", p.getTitle());
                if (p.getThumbs().size() == 0){
                    intent.putExtra("coverUrl", "");
                } else {
                    intent.putExtra("image", p.getThumbs().get(0).getMedium().getUrl());
                }
                break;
            default:
                break;
        }
        context.startActivity(intent);
    }

    @Override
    public void checkForFreshData() {

        // every first one of the 3 lists is with header
        // add them in advance

        types.add(TYPE_ZHIHU_WITH_HEADER);
        Query<DbZhihu> query = dbZhihuDao.queryBuilder().where(DbZhihuDao.Properties.IsBookMark.eq(1)).build();
        for (DbZhihu dbZhihu : query.list()) {
            try {
                ZhiHuLatest.StoriesBean bean = gson.fromJson(dbZhihu.getZhihu_news(),ZhiHuLatest.StoriesBean.class);
                zhihuList.add(bean);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
            types.add(TYPE_ZHIHU_NORMAL);
        }


        types.add(TYPE_GUOKR_WITH_HEADER);
        Query<DbGuokr> queryGuokr = dbGuokrDao.queryBuilder().where(DbGuokrDao.Properties.IsBookMark.eq(1)).build();
        for (DbGuokr dbGuokr : queryGuokr.list()) {
            GuokrHandpickNews.result result = gson.fromJson(dbGuokr.getGuolkr_news(),GuokrHandpickNews.result.class);
            guokrList.add(result);
            types.add(TYPE_GUOKR_NORMAL);
        }

        types.add(TYPE_DOUBAN_WITH_HEADER);
        Query<DbDouban> queryDouban = dbDoubanDao.queryBuilder().where(DbDoubanDao.Properties.IsBookMark.eq(1)).build();
        for (DbDouban dbDouban : queryDouban.list()) {
            DouBanNews.PostsBean postsBean = gson.fromJson(dbDouban.getDouban_news(), DouBanNews.PostsBean.class);
            doubanList.add(postsBean);
            types.add(TYPE_DOUBAN_NORMAL);
        }

    }

    @Override
    public void feelLucky() {
        Random random = new Random();
        int p = random.nextInt(types.size());
        while (true) {
            if (types.get(p) == TYPE_ZHIHU_NORMAL) {
                startReading(BeanType.TYPE_ZHIHU, p);
                break;
            } else if (types.get(p) == TYPE_GUOKR_NORMAL) {
                startReading(BeanType.TYPE_GUOKR, p);
                break;
            } else if (types.get(p) == TYPE_DOUBAN_NORMAL) {
                startReading(BeanType.TYPE_DOUBAN, p);
                break;
            } else {
                p = random.nextInt(types.size());
            }
        }
    }
}
