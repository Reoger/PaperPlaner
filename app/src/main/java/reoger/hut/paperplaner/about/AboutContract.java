package reoger.hut.paperplaner.about;

import reoger.hut.paperplaner.interfaces.BasePresenter;
import reoger.hut.paperplaner.interfaces.BaseView;

/**
 * Created by 24540 on 2017/7/6.
 */

public interface AboutContract {
    interface View extends BaseView<Presenter> {

        void showRateError();

        void showFeedbackError();

        void showBrowserNotFoundError();

    }

    interface Presenter extends BasePresenter {

        void rate();

        void openLicense();

        void followOnGithub();

        void followOnZhihu();

        void feedback();

        void donate();

        void showEasterEgg();

    }

}
