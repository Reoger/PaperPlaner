package reoger.hut.paperplaner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import reoger.hut.paperplaner.R;
import reoger.hut.paperplaner.bean.BeanType;
import reoger.hut.paperplaner.fragment.DetailFragment;
import reoger.hut.paperplaner.presenter.DetailPresenter;

/**
 * Created by 24540 on 2017/5/6.
 * 中转站activity，用于显示详情页的Fragment
 */

public class DetailActivity extends AppCompatActivity {
    private DetailFragment fragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frame);

        if(savedInstanceState != null){
            fragment = (DetailFragment) getSupportFragmentManager().getFragment(savedInstanceState,"detailFragment");
        }else{
            fragment = new DetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container,fragment)
                    .commit();
        }

        Intent intent = getIntent();
        DetailPresenter presenter = new DetailPresenter(DetailActivity.this, fragment);

        presenter.setType((BeanType) intent.getSerializableExtra("type"));
        presenter.setId(intent.getIntExtra("id", 1));
        presenter.setTitle(intent.getStringExtra("title"));
        presenter.setCoverUrl(intent.getStringExtra("coverUrl"));


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "detailFragment", fragment);
        }
    }
}
