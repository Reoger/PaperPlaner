package reoger.hut.paperplaner.bookmarks;

import java.util.ArrayList;

import reoger.hut.paperplaner.bean.BeanType;
import reoger.hut.paperplaner.bean.DouBanNews;
import reoger.hut.paperplaner.bean.GuokrHandpickNews;
import reoger.hut.paperplaner.bean.ZhiHuLatest;
import reoger.hut.paperplaner.interfaces.BasePresenter;
import reoger.hut.paperplaner.interfaces.BaseView;

/**
 * Created by 24540 on 2017/7/7.
 */

public interface BookmarksContract {
    interface View extends BaseView<Presenter> {

        void showResults(ArrayList<ZhiHuLatest.StoriesBean> zhihuList,
                         ArrayList<GuokrHandpickNews.result> guokrList,
                         ArrayList<DouBanNews.PostsBean> doubanList,
                         ArrayList<Integer> types);

        void notifyDataChanged();

        void showLoading();

        void stopLoading();

    }

    interface Presenter extends BasePresenter {

        void loadResults(boolean refresh);

        void startReading(BeanType type, int position);

        void checkForFreshData();

        void feelLucky();

    }
}
