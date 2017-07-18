package reoger.hut.paperplaner.setting;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import reoger.hut.paperplaner.R;
import reoger.hut.paperplaner.util.log;

/**
 * Created by 24540 on 2017/7/6.
 *
 */

public class SettingsPreferenceFragment extends PreferenceFragmentCompat implements SettingsContract.View{

    private SettingsContract.Presenter presenter;
    private Toolbar toolbar;

    private Preference timePreference;

    public SettingsPreferenceFragment() {

    }

    public static SettingsPreferenceFragment newInstance() {
        log.e(" 创建Fragment 成功");
        return new SettingsPreferenceFragment();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        log.e("加载布局前");
        addPreferencesFromResource(R.xml.settings_preference_fragment);
        log.e("加载布局后");
        initViews(getView());

        findPreference("no_picture_mode").setOnPreferenceClickListener(new android.support.v7.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {
                presenter.setNoPictureMode(preference);
                return false;
            }
        });

        findPreference("in_app_browser").setOnPreferenceClickListener(new android.support.v7.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(android.support.v7.preference.Preference preference) {
                presenter.setInAppBrowser(preference);
                return false;
            }
        });

        findPreference("clear_glide_cache").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                presenter.cleanGlideCache();
                return false;
            }
        });


        timePreference = findPreference("time_of_saving_articles");

        timePreference.setSummary(presenter.getTimeSummary());

        timePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                presenter.setTimeOfSavingArticles(preference, newValue);
                timePreference.setSummary(presenter.getTimeSummary());
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void showCleanGlideCacheDone() {
        Snackbar.make(toolbar, R.string.clear_image_cache_successfully, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(SettingsContract.Presenter presenter) {
        if (presenter != null){
            this.presenter = presenter;
        }
    }

    @Override
    public void initViews(View view) {
        log.e("初始化界面");
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
    }
}
