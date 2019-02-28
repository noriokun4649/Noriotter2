/*
 * Copyright (c) 2018 noriokun4649.
 */

package jp.noriokun4649.noriotter2.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.snackbar.Snackbar;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.LayoutInflaterCompat;
import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.list.ListList;
import jp.noriokun4649.noriotter2.list.ListListItemAdapter;
import jp.noriokun4649.noriotter2.twitter.TwitterConnect;
import twitter4j.AccountSettings;
import twitter4j.AsyncTwitter;
import twitter4j.ResponseList;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterMethod;
import twitter4j.UserList;

/**
 * リストからインポートする際のアクティビティです.
 * こっちはリストの一覧が出る
 */
public class GetListListActivity extends AppCompatActivity {
    /**
     * ハンドラー.
     * このインスタンスを通して、じゃないとアプリの画面等を操作できません.
     */
    private Handler mHandler = new Handler();
    /**
     * リストViewのアダプタです.
     */
    private ListListItemAdapter adapter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_list_import_layout);
        final LinearLayout linearLayout = findViewById(R.id.progress);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setTitle(R.string.list_import);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(new IconicsDrawable(this,
                GoogleMaterial.Icon.gmd_arrow_back).sizeDp(16).color(Color.WHITE));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                finish();
            }
        });
        final ArrayList<ListList> listList = new ArrayList<>();
        adapter = new ListListItemAdapter(this, listList);
        final ListView listView = findViewById(R.id.list_import_list);
        listView.setAdapter(adapter);
        TwitterConnect twitterConnect = new TwitterConnect(this);
        twitterConnect.login();
        final AsyncTwitter asyncTwitter = twitterConnect.getmTwitter();
        asyncTwitter.addListener(new TwitterAdapter() {
            @Override
            public void gotAccountSettings(final AccountSettings settings) {
                asyncTwitter.getUserLists(settings.getScreenName());
            }

            @Override
            public void gotUserLists(final ResponseList<UserList> userLists) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        for (UserList list : userLists) {
                            ListList listList1 = new ListList(list.getName(), list.getMemberCount(),
                                    list.getId(), list.getUser().getName(),
                                    list.getUser().get400x400ProfileImageURL(),
                                    list.getDescription(), list.isPublic());
                            adapter.add(listList1);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }

            @Override
            public void onException(final TwitterException te, final TwitterMethod method) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        final ListView layout = findViewById(R.id.list_import_list);
                        Snackbar snackbar = Snackbar.make(layout, R.string.api_limit, Snackbar.LENGTH_LONG).setAction(R.string.close, new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                finish();
                            }
                        });
                        snackbar.show();
                    }
                });
                super.onException(te, method);
            }
        });
        asyncTwitter.getAccountSettings();
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(GetListListActivity.this, GetUserListActivity.class);
            intent.putExtra("listId", adapter.getItem(position).getId());
            intent.putExtra("count", adapter.getItem(position).getMemberCount());
            startActivity(intent);

        });
        listView.setEmptyView(linearLayout);
    }
}
