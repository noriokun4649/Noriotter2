package jp.noriokun4649.noriotter2.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.LayoutInflaterCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.fragment.userprofile.UserLike;
import jp.noriokun4649.noriotter2.fragment.userprofile.UserTweet;
import jp.noriokun4649.noriotter2.glide.MyGlideApp;
import jp.noriokun4649.noriotter2.twitter.GetUserProfile;
import jp.noriokun4649.noriotter2.twitter.StatusCallBack;
import jp.noriokun4649.noriotter2.twitter.TwitterConnect;
import twitter4j.AsyncTwitter;
import twitter4j.Status;
import twitter4j.User;

public class UserPageActivity extends AppCompatActivity implements StatusCallBack {

    private Handler had = new Handler();
    /**
     * Twitterのインスタンス.
     */
    private TwitterConnect twitterConnect = new TwitterConnect(this);
    private Toolbar toolbar;
    private long userId;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_layout);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        String id = getIntent().getStringExtra("userid");
        twitterConnect.login();
        AsyncTwitter asyncTwitter = twitterConnect.getmTwitter();
        GetUserProfile userProfile = new GetUserProfile(this, asyncTwitter, this);
        userProfile.getUserProfile(id);
        IconicsDrawable gmdBack = new IconicsDrawable(this,
                GoogleMaterial.Icon.gmd_arrow_back).sizeDp(30).paddingDp(6).color(Color.WHITE).backgroundColor(Color.argb(140, 0, 0, 0)).roundedCornersDp(15);
        toolbar = findViewById(R.id.toolbar2);
        toolbar.setNavigationIcon(gmdBack);
        toolbar.setNavigationOnClickListener(v -> finishAfterTransition());
    }

    @Override
    public void callbackStatus(final Status status) {

    }

    @Override
    public void callbackFollow(final long[] follow, final User user) {
        had.post(() -> {
            userId = user.getId();
            ImageView imageIcon = findViewById(R.id.image_icon);
            ImageView imageheder = findViewById(R.id.imageheder);
            TextView textName = findViewById(R.id.textName);
            TextView textScreenName = findViewById(R.id.textScreenName);
            TextView textInfo = findViewById(R.id.textInfo);
            TextView textGeo = findViewById(R.id.textGeo);
            TextView textUrl = findViewById(R.id.textUrl);
            TextView textFollow = findViewById(R.id.textFollow);
            TextView textFollower = findViewById(R.id.textFollower);
            TextView textStartDay = findViewById(R.id.textStartDay);
            MyGlideApp.with(this).load(user.get400x400ProfileImageURLHttps()).circleCrop().into(imageIcon);
            Glide.with(this).load(user.getProfileBanner1500x500URL()).into(imageheder);
            textName.setText(user.getName());
            textScreenName.setText("@" + user.getScreenName());
            textInfo.setText(user.getDescription());
            if (user.getDescription().equals("")) {
                textInfo.setVisibility(View.GONE);
            }
            if (!user.getLocation().equals("")) {
                textGeo.setVisibility(View.VISIBLE);
                textGeo.setText(user.getLocation());
            }
            if (!user.getURLEntity().getExpandedURL().equals("")) {
                textUrl.setVisibility(View.VISIBLE);
                textUrl.setText(user.getURLEntity().getExpandedURL());
            }
            textFollow.setText(getString(R.string.follow_count, user.getFriendsCount()));
            textFollower.setText(getString(R.string.follow_count, user.getFollowersCount()));
            textFollow.setOnClickListener((v -> {
                Intent intent = new Intent(UserPageActivity.this, GetUserFollowActivity.class);
                intent.putExtra("user_id", user.getId());
                startActivity(intent);
            }));
            textFollower.setOnClickListener((v -> {
                Intent intent = new Intent(UserPageActivity.this, GetUserFollowerActivity.class);
                intent.putExtra("user_id", user.getId());
                startActivity(intent);
            }));
            textStartDay.setText(new SimpleDateFormat("yyyy年MM月dd日").format(user.getCreatedAt()));
            //toolbar.setTitle(user.getName()+user.getStatusesCount()+"ツイート");

            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
            mViewPager = findViewById(R.id.viewpager);
            mViewPager.setAdapter(mSectionsPagerAdapter);
            TabLayout tabLayout = findViewById(R.id.tabs);
            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        });
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(final FragmentManager fm) {
            super(fm);
        }


        @NotNull
        @Override
        public Fragment getItem(final int position) {
            Bundle bundle = new Bundle();
            bundle.putLong("user_id", userId);
            switch (position) {
                case 1:
                    UserLike userLike = new UserLike();
                    userLike.setArguments(bundle);
                    return userLike;
                default:
                    UserTweet userTweet = new UserTweet();
                    userTweet.setArguments(bundle);
                    return userTweet;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
