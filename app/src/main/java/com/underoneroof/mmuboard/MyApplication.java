package com.underoneroof.mmuboard;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.underoneroof.mmuboard.Model.Post;
import com.underoneroof.mmuboard.Model.Subject;
import com.underoneroof.mmuboard.Model.SubjectUser;
import com.underoneroof.mmuboard.Model.Topic;
import com.underoneroof.mmuboard.Model.User;

/**
 * Created by Hii on 10/09/2015.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Subject.class);
        ParseObject.registerSubclass(Topic.class);
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(SubjectUser.class);

        Parse.enableLocalDatastore(getApplicationContext());

        Parse.initialize(this, "mpIbcCkosC9Hk7dZFigPaNT0zRjZr838Ap5orEOv", "lWRiLS1bOagSInQ5PpsXopuybetLRs0oWR3tCU7T");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }
}
