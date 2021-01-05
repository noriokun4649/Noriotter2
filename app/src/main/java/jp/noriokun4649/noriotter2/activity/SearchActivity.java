package jp.noriokun4649.noriotter2.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.LayoutInflaterCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import org.jetbrains.annotations.NotNull;

import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.fragment.search.TweetSearch;
import jp.noriokun4649.noriotter2.fragment.search.UserSearch;

public class SearchActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String tag = "";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);
        getWindow().setSharedElementsUseOverlay(true);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        setContentView(R.layout.search_layout);
        String tags = getIntent().getStringExtra("tag");
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        IconicsDrawable gmdBack = new IconicsDrawable(this,
                GoogleMaterial.Icon.gmd_arrow_back).sizeDp(30).paddingDp(6).color(Color.WHITE).backgroundColor(Color.argb(140, 0, 0, 0)).roundedCornersDp(15);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.search);
        toolbar.setNavigationIcon(gmdBack);
        toolbar.setNavigationOnClickListener(v -> finish());
        toolbar.inflateMenu(R.menu.search);
        SearchView mSearchView = (SearchView) toolbar.getMenu().findItem(R.id.menu_search).getActionView();
        mSearchView.setQueryHint("けんさくわーど");
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String s) {
                Fragment fragment0 = mSectionsPagerAdapter.findFragmentByPosition(mViewPager, 0);
                Fragment fragment1 = mSectionsPagerAdapter.findFragmentByPosition(mViewPager, 1);
                if (fragment0 instanceof TweetSearch) {
                    Log.d("aa", s);
                    ((TweetSearch) fragment0).getSearch(s);
                }
                if (fragment1 instanceof UserSearch) {
                    ((UserSearch) fragment1).getSearch(s);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                return false;
            }
        });
        if (tags != null) {
            tag = tags;
            mSearchView.setIconified(false);
            mSearchView.setQuery(tag, false);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(final FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public @NotNull Fragment getItem(final int position) {
            if (position == 1) {
                return new UserSearch();
            }
            Bundle bundle = new Bundle();
            bundle.putString("tag", tag);
            TweetSearch tweetSearch = new TweetSearch();
            tweetSearch.setArguments(bundle);
            return tweetSearch;
        }

        @Override
        public int getCount() {
            return 2;
        }

        public Fragment findFragmentByPosition(final ViewPager viewPager,
                                               final int position) {
            return (Fragment) instantiateItem(viewPager, position);
        }
    }
}

