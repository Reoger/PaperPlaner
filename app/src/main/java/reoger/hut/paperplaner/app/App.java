package reoger.hut.paperplaner.app;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import hut.reoger.gen.DaoMaster;
import hut.reoger.gen.DaoSession;
import reoger.hut.paperplaner.util.log;


/**
 * Created by 24540 on 2017/5/6.
 *
 */

public class App extends Application {

    private static App myApp;

    public  static App getInstance() {
        if(myApp == null){
            log.e("wa  myApp is null!!!");
            return null;
        }
        return myApp;
    }


    private DaoSession daoSession;

    @Override
    public void onCreate() {
        myApp = this;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "paperPlaner");
        Database db =  helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        super.onCreate();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
