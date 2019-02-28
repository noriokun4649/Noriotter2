/*
 * Copyright (c) 2018 noriokun4649.
 */

package jp.noriokun4649.noriotter2;


import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * アプリが実行された際に1回のみ処理される初期化処理.
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().name("noriokunrealm.realm").build();
    }
}
