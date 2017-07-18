package reoger.hut.paperplaner.presenter;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.text.Html;
import android.webkit.WebView;

import com.google.gson.Gson;

import org.greenrobot.greendao.query.Query;

import java.io.IOException;
import java.util.List;

import hut.reoger.gen.DaoSession;
import hut.reoger.gen.DbDoubanDao;
import hut.reoger.gen.DbGuokrDao;
import hut.reoger.gen.DbZhihuDao;
import okhttp3.ResponseBody;
import reoger.hut.paperplaner.R;
import reoger.hut.paperplaner.app.App;
import reoger.hut.paperplaner.bean.BeanType;
import reoger.hut.paperplaner.bean.DbDouban;
import reoger.hut.paperplaner.bean.DbGuokr;
import reoger.hut.paperplaner.bean.DbZhihu;
import reoger.hut.paperplaner.bean.DouBanDetail;
import reoger.hut.paperplaner.bean.DouBanNews;
import reoger.hut.paperplaner.bean.ZhihuDetail;
import reoger.hut.paperplaner.customtabs.CustomFallback;
import reoger.hut.paperplaner.customtabs.CustomTabActivityHelper;
import reoger.hut.paperplaner.httpService.Api;
import reoger.hut.paperplaner.httpService.HttpMethodForDouBan;
import reoger.hut.paperplaner.httpService.HttpMethods;
import reoger.hut.paperplaner.httpService.HttpMethodsB;
import reoger.hut.paperplaner.interfaces.DetailContract;
import reoger.hut.paperplaner.util.NetworkState;
import reoger.hut.paperplaner.util.log;
import rx.Subscriber;

import static android.content.Context.CLIPBOARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by 24540 on 2017/5/6.
 * 详情页面的加载类
 */

public class DetailPresenter implements DetailContract.Presenter {

    private ZhihuDetail zhihuDailyStory;

    private Context context;
    private DetailContract.View view;

    private BeanType type;
    private int id;
    private String title;
    private String coverUrl;

    private SharedPreferences sp;

    private String guokrStory;

    private DouBanDetail doubanMomentStory;

    private DaoSession daoSession = App.getInstance().getDaoSession();

    private DbZhihuDao dbZhihuDao;
    private DbGuokrDao dbGuokrDao;
    private DbDoubanDao dbDoubanDao;

    private Gson gson = new Gson();

    public DetailPresenter(Context context, DetailContract.View view) {
        this.context = context;
        this.view = view;
        this.view.setPresenter(this);
        sp = context.getSharedPreferences("user_settings", MODE_PRIVATE);
        dbZhihuDao = daoSession.getDbZhihuDao();
        dbGuokrDao = daoSession.getDbGuokrDao();
        dbDoubanDao = daoSession.getDbDoubanDao();
    }


    public void setType(BeanType type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    @Override
    public void start() {

    }


    @Override
    public void openInBrowser() {
        if (checkNull()) {
            view.showLoadingError();
            return;
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            switch (type) {
                case TYPE_ZHIHU:
                    intent.setData(Uri.parse(zhihuDailyStory.getShare_url()));
                    break;
                case TYPE_GUOKR:
                    intent.setData(Uri.parse(Api.GUOKR_ARTICLE_LINK_V1 + id));
                    break;
                case TYPE_DOUBAN:
                    intent.setData(Uri.parse(doubanMomentStory.getShort_url()));
                    break;
            }
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            view.showBrowserNotFoundError();
        }
    }

    @Override
    public void shareAsText() {
        if (checkNull()) {
            view.showSharingError();
            return;
        }

        try {
            Intent shareIntent = new Intent().setAction(Intent.ACTION_SEND).setType("text/plain");
            String shareText = "" + title + " ";

            switch (type) {
                case TYPE_ZHIHU:
                    shareText += zhihuDailyStory.getShare_url();
                    break;
                case TYPE_GUOKR:
                    shareText += Api.GUOKR_ARTICLE_LINK_V1 + id;
                    break;
                case TYPE_DOUBAN:
                    shareText += doubanMomentStory.getShort_url();
                    break;
                case TYPE_GANK:
                    break;
            }

            shareText = shareText + "\t\t\t" + context.getString(R.string.share_extra);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            context.startActivity(Intent.createChooser(shareIntent, context.getString(R.string.share_to)));
        } catch (android.content.ActivityNotFoundException ex) {
            view.showLoadingError();
        }
    }

    @Override
    public void openUrl(WebView webView, String url) {
        if (sp.getBoolean("in_app_browser", true)) {
            CustomTabsIntent.Builder customTabsIntent = new CustomTabsIntent.Builder()
                    .setToolbarColor(context.getResources().getColor(R.color.colorAccent))
                    .setShowTitle(true);
            CustomTabActivityHelper.openCustomTab(
                    (Activity) context,
                    customTabsIntent.build(),
                    Uri.parse(url),
                    new CustomFallback() {
                        @Override
                        public void openUri(Activity activity, Uri uri) {
                            super.openUri(activity, uri);
                        }
                    }
            );
        } else {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
            } catch (android.content.ActivityNotFoundException ex) {
                view.showBrowserNotFoundError();
            }

        }
    }

    @Override
    public void copyText() {
        if (checkNull()) {
            view.showCopyTextError();
            return;
        }

        ClipboardManager manager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = null;
        switch (type) {
            case TYPE_ZHIHU:
                clipData = ClipData.newPlainText("text", Html.fromHtml(title + "\n" + zhihuDailyStory.getBody()).toString());
                break;
            case TYPE_GUOKR:
                clipData = ClipData.newPlainText("text", Html.fromHtml(guokrStory).toString());
                break;
            case TYPE_DOUBAN:
                clipData = ClipData.newPlainText("text", Html.fromHtml(title + "\n" + doubanMomentStory.getContent()).toString());
                break;
            case TYPE_GANK:

                break;
        }
        manager.setPrimaryClip(clipData);
        view.showTextCopied();
    }

    @Override
    public void copyLink() {
        if (checkNull()) {
            view.showCopyTextError();
            return;
        }

        ClipboardManager manager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = null;
        switch (type) {
            case TYPE_ZHIHU:
                clipData = ClipData.newPlainText("text", Html.fromHtml(zhihuDailyStory.getShare_url()).toString());
                break;
            case TYPE_GUOKR:
                clipData = ClipData.newPlainText("text", Html.fromHtml(Api.GUOKR_ARTICLE_LINK_V1 + id).toString());
                break;
            case TYPE_DOUBAN:
                clipData = ClipData.newPlainText("text", Html.fromHtml(doubanMomentStory.getOriginal_url()).toString());
                break;
            case TYPE_GANK:

                break;
        }
        manager.setPrimaryClip(clipData);
        view.showTextCopied();
    }

    @Override
    public void addToOrDeleteFromBookmarks() {
        switch (type) {
            case TYPE_ZHIHU:
                Query<DbZhihu> query = dbZhihuDao.queryBuilder().where(DbZhihuDao.Properties.Id.eq(id)).build();
                DbZhihu dbZhihu = query.list().get(0);
                dbZhihu.setIsBookMark(!dbZhihu.getIsBookMark());
                dbZhihuDao.update(dbZhihu);
                break;
            case TYPE_GUOKR:
                DbGuokr dbGuokr = dbGuokrDao.queryBuilder().where( DbGuokrDao.Properties.Guokr_id.eq(id)).build().list().get(0);
                dbGuokr.setIsBookMark(!dbGuokr.getIsBookMark());
                dbGuokrDao.update(dbGuokr);
                break;
            case TYPE_DOUBAN:
                DbDouban dbDouban = dbDoubanDao.queryBuilder().where(DbDoubanDao.Properties.Douban_id.eq(id)).build().list().get(0);
                dbDouban.setIsBookMark(!dbDouban.getIsBookMark());
                dbDoubanDao.update(dbDouban);
                break;
            case TYPE_GANK:

                break;
            default:
                break;
        }


    }

    @Override
    public boolean queryIfIsBookmarked() {

        if (id == 0 || type == null) {
            view.showLoadingError();
            return false;
        }

        switch (type) {
            case TYPE_ZHIHU:
               DbZhihu dbZhihu = dbZhihuDao.queryBuilder().where(DbZhihuDao.Properties.Id.eq(id)).build().list().get(0);
                return dbZhihu.getIsBookMark();
            case TYPE_GUOKR:
                DbGuokr dbGuokr = dbGuokrDao.queryBuilder().where( DbGuokrDao.Properties.Guokr_id.eq(id)).build().list().get(0);
                return dbGuokr.getIsBookMark();
            case TYPE_DOUBAN:
                DbDouban dbDouban = dbDoubanDao.queryBuilder().where(DbDoubanDao.Properties.Douban_id.eq(id)).build().list().get(0);
                return dbDouban.getIsBookMark();
            case TYPE_GANK:
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public void requestData() {
        if (id == 0 || type == null) {
            view.showLoadingError();
            return;
        }
        view.showLoading();
        view.setTitle(title);
        view.showCover(coverUrl);
        view.setImageMode(sp.getBoolean("no_picture_mode", false));


        switch (type) {
            case TYPE_ZHIHU:
                getDataForZhiHu();
                break;
            case TYPE_GUOKR:
                getDataForGuoKr();
                break;
            case TYPE_DOUBAN:
                getDateForDouBan();
                break;
            case TYPE_GANK:
                getDataForGank();
                break;
        }
    }

    public void getDataForGank() {
        if (NetworkState.networkConnected(context)) {
            view.showResultWithoutBody(coverUrl);
            view.stopLoading();
        } else {
            view.stopLoading();

        }
    }

    private void getDateForDouBan() {
        if (NetworkState.networkConnected(context)) {
            Subscriber<DouBanDetail> subscriber2 = new Subscriber<DouBanDetail>() {
                @Override
                public void onCompleted() {
                    view.stopLoading();
                }

                @Override
                public void onError(Throwable e) {
                    view.stopLoading();
                    view.showLoadingError();
                }

                @Override
                public void onNext(DouBanDetail douBanDetail) {
                    doubanMomentStory = douBanDetail;
                    view.showResult(convertDoubanContent(douBanDetail));
                }
            };
            HttpMethodForDouBan.getInstance().getDouBanNewsDetali(subscriber2, id);
        } else {
            DbDouban dbDouban = dbDoubanDao.queryBuilder().where(DbDoubanDao.Properties.Douban_id.eq(id)).build().list().get(0);
            if (dbDouban != null && !"".equals(dbDouban)) {
                view.showResult(dbDouban.getDouban_content());
                view.stopLoading();
            } else {
                log.e("暂时没有任何缓存");
            }
        }
    }

    private void getDataForGuoKr() {
        if (NetworkState.networkConnected(context)) {
            Subscriber<ResponseBody> subscriber1 = new Subscriber<ResponseBody>() {
                @Override
                public void onCompleted() {
                    view.stopLoading();
                }

                @Override
                public void onError(Throwable e) {
                    view.stopLoading();
                    view.showLoadingError();
                }

                @Override
                public void onNext(ResponseBody responseBody) {
                    try {
                        convertGuokrContent(responseBody.string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    view.showResult(guokrStory);
                }
            };
            HttpMethodsB.getInstance().getGuokrDetail(subscriber1, id);
        } else {
            DbGuokr dbGuokr = dbGuokrDao.queryBuilder().where(DbGuokrDao.Properties.Guokr_id.eq(id)).build().list().get(0);
            if (dbGuokr != null) {
                convertGuokrContent(dbGuokr.getGuokr_content());
                view.showResult(guokrStory);
            } else {
                log.e("暂时没有缓存");
            }
        }


    }

    private void getDataForZhiHu() {
        if (NetworkState.networkConnected(context)) {
            Subscriber<ZhihuDetail> subscriber = new Subscriber<ZhihuDetail>() {
                @Override
                public void onCompleted() {
                    view.stopLoading();
                }

                @Override
                public void onError(Throwable e) {
                    view.stopLoading();
                    view.showLoadingError();
                }

                @Override
                public void onNext(ZhihuDetail zhihuDetail) {
                    zhihuDailyStory = zhihuDetail;
                    if (zhihuDetail.getBody() == null) {
                        view.showResultWithoutBody(zhihuDetail.getShare_url());
                    } else {
                        view.showResult(convertZhihuContent(zhihuDetail.getBody()));
                    }
                }
            };
            HttpMethods.getInstance(Api.ZHIHU_NEWS).getZhihuDetail(subscriber, id);
        } else {
            Query<DbZhihu> query = dbZhihuDao.queryBuilder().where(DbZhihuDao.Properties.Id.eq(id)).build();
            final DbZhihu dbZhihu = query.list().get(0);
            view.showResult(convertZhihuContent(dbZhihu.getZhihu_content()));
            view.stopLoading();
        }

    }

    private String convertZhihuContent(String preResult) {

        preResult = preResult.replace("<div class=\"img-place-holder\">", "");
        preResult = preResult.replace("<div class=\"headline\">", "");

        // 在api中，css的地址是以一个数组的形式给出，这里需要设置
        // in fact,in api,css addresses are given as an array
        // api中还有js的部分，这里不再解析js
        // javascript is included,but here I don't use it
        // 不再选择加载网络css，而是加载本地assets文件夹中的css
        // use the css file from local assets folder,not from network
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/zhihu_daily.css\" type=\"text/css\">";


        // 根据主题的不同确定不同的加载内容
        // load content judging by different theme
        String theme = "<body className=\"\" onload=\"onLoaded()\">";
        if ((context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                == Configuration.UI_MODE_NIGHT_YES) {
            theme = "<body className=\"\" onload=\"onLoaded()\" class=\"night\">";
        }

        return new StringBuilder()
                .append("<!DOCTYPE html>\n")
                .append("<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\">\n")
                .append("<head>\n")
                .append("\t<meta charset=\"utf-8\" />")
                .append(css)
                .append("\n</head>\n")
                .append(theme)
                .append(preResult)
                .append("</body></html>").toString();
    }

    private void convertGuokrContent(String content) {
        // 简单粗暴的去掉下载的div部分
        this.guokrStory = content.replace("<div class=\"down\" id=\"down-footer\">\n" +
                "        <img src=\"http://static.guokr.com/apps/handpick/images/c324536d.jingxuan-logo.png\" class=\"jingxuan-img\">\n" +
                "        <p class=\"jingxuan-txt\">\n" +
                "            <span class=\"jingxuan-title\">果壳精选</span>\n" +
                "            <span class=\"jingxuan-label\">早晚给你好看</span>\n" +
                "        </p>\n" +
                "        <a href=\"\" class=\"app-down\" id=\"app-down-footer\">下载</a>\n" +
                "    </div>\n" +
                "\n" +
                "    <div class=\"down-pc\" id=\"down-pc\">\n" +
                "        <img src=\"http://static.guokr.com/apps/handpick/images/c324536d.jingxuan-logo.png\" class=\"jingxuan-img\">\n" +
                "        <p class=\"jingxuan-txt\">\n" +
                "            <span class=\"jingxuan-title\">果壳精选</span>\n" +
                "            <span class=\"jingxuan-label\">早晚给你好看</span>\n" +
                "        </p>\n" +
                "        <a href=\"http://www.guokr.com/mobile/\" class=\"app-down\">下载</a>\n" +
                "    </div>", "");

        // 替换css文件为本地文件
        guokrStory = guokrStory.replace("<link rel=\"stylesheet\" href=\"http://static.guokr.com/apps/handpick/styles/d48b771f.article.css\" />",
                "<link rel=\"stylesheet\" href=\"file:///android_asset/guokr.article.css\" />");

        // 替换js文件为本地文件
        guokrStory = guokrStory.replace("<script src=\"http://static.guokr.com/apps/handpick/scripts/9c661fc7.base.js\"></script>",
                "<script src=\"file:///android_asset/guokr.base.js\"></script>");
        if ((context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                == Configuration.UI_MODE_NIGHT_YES) {
            guokrStory = guokrStory.replace("<div class=\"article\" id=\"contentMain\">",
                    "<div class=\"article \" id=\"contentMain\" style=\"background-color:#212b30; color:#878787\">");
        }
    }

    private String convertDoubanContent(DouBanDetail doubanMomentStory) {

        if (doubanMomentStory.getContent() == null) {
            return null;
        }
        String css;
        if ((context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                == Configuration.UI_MODE_NIGHT_YES) {
            css = "<link rel=\"stylesheet\" href=\"file:///android_asset/douban_dark.css\" type=\"text/css\">";
        } else {
            css = "<link rel=\"stylesheet\" href=\"file:///android_asset/douban_light.css\" type=\"text/css\">";
        }
        String content = doubanMomentStory.getContent();


        List<DouBanNews.PostsBean.ThumbsBean> imageList = doubanMomentStory.getPhotos();
        for (int i = 0; i < imageList.size(); i++) {
            String old = "<img id=\"" + imageList.get(i).getTag_name() + "\" />";
            String newStr = "<img id=\"" + imageList.get(i).getTag_name() + "\" "
                    + "src=\"" + imageList.get(i).getMedium().getUrl() + "\"/>";
            content = content.replace(old, newStr);
        }
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>\n");
        builder.append("<html lang=\"ZH-CN\" xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        builder.append("<head>\n<meta charset=\"utf-8\" />\n");
        builder.append(css);
        builder.append("\n</head>\n<body>\n");
        builder.append("<div class=\"container bs-docs-container\">\n");
        builder.append("<div class=\"post-container\">\n");
        builder.append(content);
        builder.append("</div>\n</div>\n</body>\n</html>");

        return builder.toString();
    }


    private boolean checkNull() {
        return (type == BeanType.TYPE_ZHIHU && zhihuDailyStory == null)
                || (type == BeanType.TYPE_GUOKR && guokrStory == null)
                || (type == BeanType.TYPE_DOUBAN && doubanMomentStory == null);
    }


}
