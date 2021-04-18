package com.codepath.expensetracker;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        // Need to make sure to register this class with Parse before we call Parse.initialize
        //ParseObject.registerSubclass(Post.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("di8dtAM0GAFE0B3y69IM8QcWvZbbzlGobdfpYwdX")
                .clientKey("qZFRyNzSZbLBT6X4iCWhPuQOKlOOF6xyqc1lcDO8")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}