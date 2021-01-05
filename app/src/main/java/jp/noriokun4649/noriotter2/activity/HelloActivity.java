package jp.noriokun4649.noriotter2.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;

import java.io.File;
import java.io.IOException;

import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.twitter.TwitterConnect;

public class HelloActivity extends AppCompatActivity {
    private BootstrapButton button;
    /**
     * Twitterのインスタンス.
     */
    private final TwitterConnect twitterConnect = new TwitterConnect(this);

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hellow_layout);
        getWindow().setSharedElementsUseOverlay(true);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            twitterConnect.login();
            twitterConnect.signIn(HelloActivity.this);
        });

        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Pictures/Noriotter2/");
        if (!file.exists() && !file.mkdir()) {
            Toast.makeText(this, R.string.cancel, Toast.LENGTH_LONG).show();
        }
        try {
            if (!file.createNewFile())
                Toast.makeText(this, R.string.cancel, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onNewIntent(final Intent intent) {
        super.onNewIntent(intent);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        final Uri uri = intent.getData();
        BootstrapText.Builder text = new BootstrapText.Builder(this);
        TextView textView = findViewById(R.id.textView2);
        if (uri != null) {
            final String verifier = uri.getQueryParameter("oauth_verifier");
            if (verifier != null) {
                twitterConnect.getOAuthAsync(verifier);
                editor.putBoolean("flag", true).apply();
                text.addText(getString(R.string.start_noriotter));
                textView.setText(getString(R.string.twitter_connect_ok));
                button.setBootstrapText(text.build());
                button.setOnClickListener(v -> {
                    finish();
                    startActivity(new Intent(HelloActivity.this, MainActivity.class));
                });
            } else {
                textView.setText(getString(R.string.twitter_connect_error));
                editor.putBoolean("flag", false).apply();
            }
        } else {
            textView.setText(getString(R.string.twitter_connect_error));
            editor.putBoolean("flag", false).apply();
        }
    }
}
