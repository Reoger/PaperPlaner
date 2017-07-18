package reoger.hut.paperplaner.interfaces;

import java.util.List;

import reoger.hut.paperplaner.bean.GankXiatuijian;

/**
 * Created by 24540 on 2017/5/10.
 */

public interface IGankContract {

    interface View extends BaseView<Presenter> {

        void showError();

        void showResults(List<GankXiatuijian.ResultsBean> list);

        void showLoading();

        void stopLoading();

    }

    interface Presenter extends BasePresenter{

        void loadPosts(int count);

        void refresh();

        void startReading(int position);

        void feelLucky();

    }
}
