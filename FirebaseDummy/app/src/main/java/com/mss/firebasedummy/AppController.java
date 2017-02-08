package com.mss.firebasedummy;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


/**
 * Created by deepakgupta on 20/12/16.
 */

public class AppController extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    public static FirebaseAuth mAuth;

    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
    }
}
