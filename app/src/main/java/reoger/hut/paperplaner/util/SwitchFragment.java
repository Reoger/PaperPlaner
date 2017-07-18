package reoger.hut.paperplaner.util;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import reoger.hut.paperplaner.R;
import reoger.hut.paperplaner.fragment.DouBanFragment;
import reoger.hut.paperplaner.fragment.GankFragmenrt;
import reoger.hut.paperplaner.fragment.GuokeFragment;
import reoger.hut.paperplaner.fragment.ZhihuFragment;



/**
 * Created by 24540 on 2017/5/5.
 * 切换Fragment的实现方法
 * 实现的方法有待提高~ 暂时先这么用着。
 */

public class SwitchFragment {

   public  enum FRAGMENT_TYPE{
        HOME,GUOKE,DOUBAN,GANK;
    }

    private static GuokeFragment guokeFragment;
    private static ZhihuFragment homeFragment;
    private static DouBanFragment douBanFragment;
    private static GankFragmenrt gankFragmenrt;

    private  FragmentManager manager;

    public static  boolean isDouBanOnClick = false;

    public SwitchFragment(FragmentManager manager) {
        this.manager = manager;
        FragmentTransaction transaction = manager.beginTransaction();
        if(homeFragment == null){
            homeFragment = new ZhihuFragment();
            transaction.add(R.id.content,homeFragment);
        }
        hiderFragment(transaction);
        transaction.show(homeFragment);
        transaction.commit();
    }

    // choice current fragment
    public void  choiceFragment(FRAGMENT_TYPE type){
        FragmentTransaction transaction = manager.beginTransaction();
        switch (type){
            case HOME:
                if(homeFragment == null){
                    homeFragment = new ZhihuFragment();
                    transaction.add(R.id.content,homeFragment);
                }
                hiderFragment(transaction);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.show(homeFragment);
                transaction.commit();
                isDouBanOnClick = false;
                break;
            case DOUBAN:
                if(douBanFragment == null){
                    douBanFragment = new DouBanFragment();
                    transaction.add(R.id.content,douBanFragment);
                }
                hiderFragment(transaction);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.show(douBanFragment);
                transaction.commit();
                isDouBanOnClick = true;
                break;
            case GUOKE:
                if(guokeFragment == null){
                    guokeFragment = new GuokeFragment();
                    transaction.add(R.id.content,guokeFragment);
                }
                hiderFragment(transaction);

                transaction.show(guokeFragment);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
                isDouBanOnClick = false;
                break;
            case GANK:
                if(gankFragmenrt == null){
                    gankFragmenrt = new GankFragmenrt();
                    transaction.add(R.id.content,gankFragmenrt);
                }
                hiderFragment(transaction);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.show(gankFragmenrt);
                transaction.commit();
                isDouBanOnClick = false;
                break;
        }
    }


    //hide all of Fragment
    private void hiderFragment(FragmentTransaction transaction){
        if(homeFragment != null){
            transaction.hide(homeFragment);
        }
        if(guokeFragment != null){
            transaction.hide(guokeFragment);
        }
        if(douBanFragment != null){
            transaction.hide(douBanFragment);
        }
        if(gankFragmenrt != null){
            transaction.hide(gankFragmenrt);
        }
    }

}
