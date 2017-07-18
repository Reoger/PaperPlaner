package reoger.hut.paperplaner.interfaces;

import java.util.List;

import reoger.hut.paperplaner.bean.GuokrHandpickNews;

/**
 * Created by 24540 on 2017/5/7.
 */

public interface IGuoKrContract {
    interface View extends BaseView<Presenter> {

        void showError();

        void showResults(List<GuokrHandpickNews.result> list);

        void showLoading();

        void stopLoading();

    }

    interface Presenter extends BasePresenter{

        void loadPosts();

        void refresh();

        void startReading(int position);

        void feelLucky();

    }
}
