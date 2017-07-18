package reoger.hut.paperplaner.interfaces;

import java.util.List;

import reoger.hut.paperplaner.bean.ZhiHuLatest;

/**
 * Created by 24540 on 2017/5/6.
 * MVP实现---接口定义
 */

public interface IZhihuContract {

    interface View extends BaseView<Presenter> {

        void showError();

        void showLoading();

        void stopLoading();

        void showResults(List<ZhiHuLatest.StoriesBean> list);

    }

    interface Presenter extends BasePresenter {

        void loadPosts(long date, boolean clearing);

        void refresh();

        void loadMore(long date);

        void startReading(int position);

        void feelLucky();

    }

}
