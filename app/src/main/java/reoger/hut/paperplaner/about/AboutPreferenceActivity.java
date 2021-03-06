package reoger.hut.paperplaner.about;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import reoger.hut.paperplaner.R;

/**
 * Created by 24540 on 2017/7/6.
 *
 */

public class AboutPreferenceActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initViews();

        AboutPreferenceFragment fragment = new AboutPreferenceFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.about_container,fragment)
                .commit();

        new AboutPresenter(AboutPreferenceActivity.this, fragment);

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
