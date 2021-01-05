package jp.noriokun4649.noriotter2.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
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

public class ListListActivity extends AppCompatActivity {
    /**
     * ハンドラー.
     * このインスタンスを通して、じゃないとアプリの画面等を操作できません.
     */
    private static Handler mHandler = new Handler();
    /**
     * リストViewのアダプタです.
     */
    private ListListItemAdapter adapter;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_list_layout);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        IconicsDrawable gmdBack = new IconicsDrawable(this,
                GoogleMaterial.Icon.gmd_arrow_back).sizeDp(30).paddingDp(6).color(Color.WHITE).roundedCornersDp(15);
        Toolbar toolbar = findViewById(R.id.toolbar4);
        toolbar.setTitle("りぃすとぉ");
        toolbar.setNavigationIcon(gmdBack);
        toolbar.setNavigationOnClickListener(v -> finish());
        ListView listView = findViewById(R.id.list);
        final ArrayList<ListList> listList = new ArrayList<>();
        adapter = new ListListItemAdapter(this, listList);
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
                                    list.getUser().get400x400ProfileImageURLHttps(),
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
                    }
                });
                super.onException(te, method);
            }
        });
        asyncTwitter.getAccountSettings();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                Intent intent = new Intent(ListListActivity.this, ListDetailActivity.class);
                intent.putExtra("list_id", adapter.getItem(position).getId());
                intent.putExtra("list_name", adapter.getItem(position).getName());
                //intent.putExtra("count", adapter.getItem(position).getMemberCount());
                startActivity(intent);

            }
        });
        //listView.setEmptyView(linearLayout);
    }
}
