
package reoger.hut.paperplaner.innerbrowser;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import reoger.hut.paperplaner.R;


public class InnerBrowserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, InnerBrowserFragment.getInstance()).commit();

    }

}
