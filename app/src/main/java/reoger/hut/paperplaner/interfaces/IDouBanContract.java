package reoger.hut.paperplaner.interfaces;

import java.util.List;

import reoger.hut.paperplaner.bean.DouBanNews;

/**
 * Created by 24540 on 2017/5/8.
 */

public interface IDouBanContract {
    interface View extends BaseView<Presenter> {

        void startLoading();

        void stopLoading();

        void showLoadingError();

        void showResults(List<DouBanNews.PostsBean> list);

    }

    interface Presenter extends BasePresenter {

        void startReading(int position);

        void loadPosts(long date, boolean clearing);

        void refresh();

        void loadMore(long date);

        void feelLucky();

    }
}
