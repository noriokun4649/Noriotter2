package jp.noriokun4649.noriotter2.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.WindowManager;

import androidx.core.content.ContextCompat;
import jp.noriokun4649.noriotter2.R;

/**
 * Created by noriokun4649 on 2017/03/30.
 */

public class SettingActivity extends PreferenceActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSharedElementsUseOverlay(true);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        addPreferencesFromResource(R.xml.settings);
    }
}