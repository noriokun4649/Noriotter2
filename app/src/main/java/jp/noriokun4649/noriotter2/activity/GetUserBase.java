/*
 * Copyright (c) 2018 noriokun4649.
 */

package jp.noriokun4649.noriotter2.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.LayoutInflaterCompat;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import java.util.ArrayList;

import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.dialogfragment.DialogsListener;
import jp.noriokun4649.noriotter2.list.UserList;
import jp.noriokun4649.noriotter2.list.UserListItemAdapter;
import jp.noriokun4649.noriotter2.twitter.TwitterConnect;
import twitter4j.AsyncTwitter;

/**
 * インポートアクティビティのベースです.
 * 様々なインポート方法ののちに表示されるユーザー結果を表示するさいに継承します。
 */
abstract class GetUserBase extends AppCompatActivity
        implements DialogsListener {
    private long userId;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list_layout);
        getWindow().setSharedElementsUseOverlay(true);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        userId = getIntent().getLongExtra("user_id", 0L);
        LinearLayout linearLayout = findViewById(R.id.progress);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getTitles());
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_arrow_back).sizeDp(16).color(Color.WHITE));
        toolbar.setNavigationOnClickListener(v -> finish());
        TextView textView = findViewById(R.id.textView4);
        textView.setText(getLoadingText());
        final ListView listView = findViewById(R.id.follow_import_list);
        final ArrayList<UserList> circles = new ArrayList<>();
        UserListItemAdapter adapter = new UserListItemAdapter(this, circles);
        TwitterConnect twitterConnect = new TwitterConnect(this);
        twitterConnect.login();
        final AsyncTwitter asyncTwitter = twitterConnect.getmTwitter();
        getListData(asyncTwitter, adapter);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, ids) -> {
            UserList list = circles.get(position);
            Intent intent = new Intent(GetUserBase.this, UserPageActivity.class);
            intent.putExtra("userid", list.getUserScreenName());
            startActivity(intent);
        });
        listView.setEmptyView(linearLayout);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * リストViewのアダプタにサークル情報を入れる処理をするメソッド.
     *
     * @param asyncTwitter Twitterの非同期処理Twitterから情報を得る際にのみ使用する
     * @param adapters     リストViewのアダプタ
     */
    abstract void getListData(AsyncTwitter asyncTwitter, UserListItemAdapter adapters);

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

    /**
     * Gets userId .
     *
     * @return value of userId
     */
    public long getUserId() {
        return userId;
    }
}
