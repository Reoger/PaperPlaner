package reoger.hut.paperplaner.setting;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import reoger.hut.paperplaner.R;

/**
 *
 * Created by 24540 on 2017/7/6.
 */

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


            initViews();
            Fragment fragment = SettingsPreferenceFragment.newInstance();


        new SettingsPresenter(SettingsActivity.this, (SettingsContract.View) fragment);

        getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings_container, fragment)
                    .commit();

    }

    private void initViews() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


}
