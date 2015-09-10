package com.underoneroof.mmuboard;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by Hii on 10/09/2015.
 */
public class MyApplication extends com.orm.SugarApp {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "mpIbcCkosC9Hk7dZFigPaNT0zRjZr838Ap5orEOv", "lWRiLS1bOagSInQ5PpsXopuybetLRs0oWR3tCU7T");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
