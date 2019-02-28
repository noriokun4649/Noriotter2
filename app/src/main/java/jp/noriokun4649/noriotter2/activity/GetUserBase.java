/*
 * Copyright (c) 2018 noriokun4649.
 */

package jp.noriokun4649.noriotter2.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.LayoutInflaterCompat;
import io.realm.Realm;
import io.realm.RealmAsyncTask;
import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.dialogfragment.DialogsListener;
import jp.noriokun4649.noriotter2.twitter.TwitterConnect;
import twitter4j.AsyncTwitter;

/**
 * インポートアクティビティのベースです.
 * 様々なインポート方法ののちに表示されるユーザー結果を表示するさいに継承します。
 */
abstract class GetUserBase extends AppCompatActivity
        implements DialogsListener {

    /**
     * ダイアログのタイトルリストです.
     */
    private String[] a;

    /**
     * サークルリストのリストViewのアダプタでしゅ.
     */
    //private CircleListItemAdapter adapter;

    /**
     * Realmデータベースのインスタンス.
     */
    private Realm realm;

    /**
     * Realmの非同期処理タスク.
     */
    private RealmAsyncTask realmAsyncTask;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.import_layout);
        Intent intent = getIntent();
        int counts = intent.getIntExtra("count", 0);
        long id = intent.getLongExtra("listId", 0);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setTitle(getTitles());
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_arrow_back).sizeDp(16).color(Color.WHITE));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                finish();
            }
        });
        realm = Realm.getDefaultInstance();
        TextView textView = findViewById(R.id.textView4);
        textView.setText(getLoadingText());
        final ListView listView = findViewById(R.id.follow_import_list);
        //final ArrayList<Circle> circles = new ArrayList<>();
        //adapter = new CircleListItemAdapter(this, circles);
        TwitterConnect twitterConnect = new TwitterConnect(this);
        twitterConnect.login();
        final AsyncTwitter asyncTwitter = twitterConnect.getmTwitter();
        getListData(asyncTwitter, /*adapter,*/ counts, id);
        //listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


// @param adapters     リストViewのアダプタ

    /**
     * リストViewのアダプタにサークル情報を入れる処理をするメソッド.
     *
     * @param asyncTwitter Twitterの非同期処理Twitterから情報を得る際にのみ使用する
     * @param counts       TwitterのListから取得時のみ使用するカウント
     * @param id           TwitterのListから取得時のみ使用するListのID
     */
    abstract void getListData(AsyncTwitter asyncTwitter, /* CircleListItemAdapter adapters,*/ int counts, long id);

    /**
     * ToolBarのタイトルを設定する(例：R.string.follow_import).
     *
     * @return R.string.xxxxx
     */
    abstract int getTitles();

    /**
     * ロード中のメッセージ内容を指定する(例：R.string.follow_import).
     *
     * @return R.string.xxxxxx
     */
    abstract int getLoadingText();

    @Override
    public void onOKClick(final int dialogId, final int position, @Nullable final String returnMemo, final String tag, final String[] items) {
    }
}
