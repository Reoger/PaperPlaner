package reoger.hut.paperplaner.interfaces;

import android.view.View;

/**
 * Created by 24540 on 2017/5/6.
 */

public interface BaseView<T> {
    /**
     * set the presenter of mvp
     * @param presenter
     */
    void setPresenter(T presenter);

    /**
     * init the views of fragment
     * @param view
     */
    void initViews(View view);
}
