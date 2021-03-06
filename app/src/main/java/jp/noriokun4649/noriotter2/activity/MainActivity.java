package jp.noriokun4649.noriotter2.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.LayoutInflaterCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import org.jetbrains.annotations.NotNull;

import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.fragment.RealTimeLine;
import jp.noriokun4649.noriotter2.fragment.TimeLine;
import jp.noriokun4649.noriotter2.glide.MyGlideApp;
import jp.noriokun4649.noriotter2.twitter.StatusCallBack;
import jp.noriokun4649.noriotter2.twitter.TimeLineTwetterAdapter;
import jp.noriokun4649.noriotter2.twitter.TwitterConnect;
import twitter4j.AsyncTwitter;
import twitter4j.Status;
import twitter4j.User;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, StatusCallBack {
    /**
     * ハンドラー.
     * このインスタンスを通して、じゃないとアプリの画面等を操作できません.
     */
    private final Handler mHandler = new Handler();
    /**
     * Twitterのインスタンス.
     */
    private final TwitterConnect twitterConnect = new TwitterConnect(this);

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        twitterConnect.login();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean flag = sharedPreferences.getBoolean("flag", false);
        if (!flag) {
            startActivity(new Intent(MainActivity.this, HelloActivity.class));
        } else {
            AsyncTwitter asyncTwitter = twitterConnect.getmTwitter();
            asyncTwitter.addListener(new TimeLineTwetterAdapter(this, asyncTwitter));
            asyncTwitter.getAccountSettings();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setNaviIcon(navigationView);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    /**
     * 左側から出てくるナビゲーションの設定をするメソッド.
     *
     * @param navi ナビゲーション
     */
    private void setNaviIcon(final NavigationView navi) {
        IconicsDrawable gmdAccountCircle = new IconicsDrawable(this, GoogleMaterial.Icon.gmd_account_circle);
        IconicsDrawable gmdList = new IconicsDrawable(this, GoogleMaterial.Icon.gmd_list);
        IconicsDrawable gmdSearch = new IconicsDrawable(this, GoogleMaterial.Icon.gmd_search);
        IconicsDrawable gmdSend = new IconicsDrawable(this, GoogleMaterial.Icon.gmd_send);
        IconicsDrawable gmdCode = new IconicsDrawable(this, GoogleMaterial.Icon.gmd_code);
        IconicsDrawable gmdLogout = new IconicsDrawable(this, GoogleMaterial.Icon.gmd_exit_to_app);
        Menu menu = navi.getMenu();
        MenuItem profile = menu.findItem(R.id.nav_profile);
        MenuItem list = menu.findItem(R.id.nav_list);
        MenuItem search = menu.findItem(R.id.nav_search);
        MenuItem freeTweet = menu.findItem(R.id.nav_free_tweet);
        MenuItem code = menu.findItem(R.id.nav_oss);
        MenuItem logout = menu.findItem(R.id.nav_logout);
        profile.setIcon(gmdAccountCircle);
        list.setIcon(gmdList);
        search.setIcon(gmdSearch);
        freeTweet.setIcon(gmdSend);
        code.setIcon(gmdCode);
        logout.setIcon(gmdLogout);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();

        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_list) {
            startActivity(new Intent(MainActivity.this, ListListActivity.class));
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, UserPageActivity.class);
            intent.putExtra("userid", PreferenceManager.getDefaultSharedPreferences(this).getString("scren_name", ""));
            startActivity(intent);
        } else if (id == R.id.nav_search) {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        } else if (id == R.id.nav_free_tweet) {
            startActivity(new Intent(MainActivity.this, DokodemoTweetActivity.class));
        } else if (id == R.id.nav_logout) {
            twitterConnect.logout();
            finish();
            startActivity(new Intent(MainActivity.this, HelloActivity.class));
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void callbackStatus(final Status status) {

    }

    @Override
    public void callbackFollow(final long[] follow, final User user) {
        mHandler.post(() -> {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            sharedPreferences.edit().putString("scren_name", user.getScreenName()).apply();
            ImageView image = findViewById(R.id.imageView);
            TextView screenName = findViewById(R.id.screen_name);
            TextView userName = findViewById(R.id.user_name);
            TextView followCount = findViewById(R.id.follow_count);
            TextView followerCount = findViewById(R.id.follower_count);

            MyGlideApp.with(getApplicationContext()).load(user.get400x400ProfileImageURLHttps()).circleCrop().into(image);
            screenName.setText(String.format("@%s", user.getScreenName()));
            userName.setText(user.getName());
            followCount.setText(getString(R.string.follow_count, user.getFriendsCount()));
            followerCount.setText(getString(R.string.follower_count, user.getFollowersCount()));
            followCount.setOnClickListener((v -> startActivity(new Intent(MainActivity.this, GetUserFollowActivity.class))));
            followerCount.setOnClickListener((v -> startActivity(new Intent(MainActivity.this, GetUserFollowerActivity.class))));
        });
    }

    public static class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(final FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public @NotNull Fragment getItem(final int position) {
            if (position == 1) {
                return new RealTimeLine();
                    /*
                case 2:
                    return new Notice();
                case 3:
                    return new DirectMessage();
                     */
            }
            return new TimeLine();
//return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
