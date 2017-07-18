package reoger.hut.paperplaner.interfaces;

/**
 * Created by 24540 on 2017/5/10.
 */

public interface IAboutContract {
    interface View extends BaseView<Presenter>{

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
