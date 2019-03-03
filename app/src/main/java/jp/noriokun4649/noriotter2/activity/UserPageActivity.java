package jp.noriokun4649.noriotter2.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.text.SimpleDateFormat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.glide.MyGlideApp;
import jp.noriokun4649.noriotter2.twitter.GetUserProfile;
import jp.noriokun4649.noriotter2.twitter.StatusCallBack;
import jp.noriokun4649.noriotter2.twitter.TwitterConnect;
import twitter4j.AsyncTwitter;
import twitter4j.Status;
import twitter4j.User;

public class UserPageActivity extends AppCompatActivity implements StatusCallBack {

    /**
     * Twitterのインスタンス.
     */
    private TwitterConnect twitterConnect = new TwitterConnect(this);

    Handler had = new Handler();
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_layout);
        String id =  getIntent().getStringExtra("userid");
        twitterConnect.login();
        AsyncTwitter asyncTwitter = twitterConnect.getmTwitter();
        GetUserProfile userProfile = new GetUserProfile(this,asyncTwitter,this);
        userProfile.getUserProfile("@"+id);
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
            ImageView imageIcon = findViewById(R.id.imageIcon);
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
            textScreenName.setText("@"+user.getScreenName());
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
            textFollow.setText(String.valueOf(user.getFriendsCount())+"フォロー");
            textFollower.setText(String.valueOf(user.getFollowersCount())+"フォロワー");
            textStartDay.setText(new SimpleDateFormat("yyyy年MM月dd日").format(user.getCreatedAt()));
            //toolbar.setTitle(user.getName()+user.getStatusesCount()+"ツイート");
        });
    }
}
