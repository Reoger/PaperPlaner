package reoger.hut.paperplaner.setting;

import android.support.v7.preference.Preference;

import reoger.hut.paperplaner.interfaces.BasePresenter;
import reoger.hut.paperplaner.interfaces.BaseView;

/**
 * Created by 24540 on 2017/7/6.
 *
 */

public class SettingsContract {
    interface View extends BaseView<Presenter> {

        void showCleanGlideCacheDone();

    }

    interface Presenter extends BasePresenter {

        void setNoPictureMode(Preference preference);

        void setInAppBrowser(Preference preference);

        void cleanGlideCache();

        void setTimeOfSavingArticles(Preference preference, Object newValue);

        String getTimeSummary();

    }

}
