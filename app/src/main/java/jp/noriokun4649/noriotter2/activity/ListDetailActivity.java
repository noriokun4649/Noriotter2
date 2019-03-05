package jp.noriokun4649.noriotter2.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

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
import jp.noriokun4649.noriotter2.fragment.list.TweetList;
import jp.noriokun4649.noriotter2.fragment.list.UsersList;

public class ListDetailActivity extends AppCompatActivity {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private long listId;
    private int count;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_detail_layout);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        listId = getIntent().getLongExtra("list_id", 0);
        count = getIntent().getIntExtra("list_count", 0);
        IconicsDrawable gmdBack = new IconicsDrawable(this,
                GoogleMaterial.Icon.gmd_arrow_back).sizeDp(30).paddingDp(6).color(Color.WHITE).backgroundColor(Color.argb(140, 0, 0, 0)).roundedCornersDp(15);
        Toolbar toolbar = findViewById(R.id.toolbar4);
        toolbar.setTitle(getIntent().getStringExtra("list_name"));
        toolbar.setNavigationIcon(gmdBack);
        toolbar.setNavigationOnClickListener(v -> finish());
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(final FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(final int position) {
            Bundle bundle = new Bundle();
            bundle.putLong("list_id", listId);
            bundle.putInt("list_count", count);
            switch (position) {
                case 1:
                    UsersList usersList = new UsersList();
                    usersList.setArguments(bundle);
                    return usersList;
                default:
                    TweetList tweetList = new TweetList();
                    tweetList.setArguments(bundle);
                    return tweetList;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
