package jp.noriokun4649.noriotter2.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.twitter.TwitterConnect;

public class HelloActivity extends AppCompatActivity {
    private BootstrapButton button;
    /**
     * Twitterのインスタンス.
     */
    private TwitterConnect twitterConnect = new TwitterConnect(this);

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hellow_layout);
        button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            twitterConnect.login();
            twitterConnect.signIn(HelloActivity.this);
        });
    }

    @Override
    protected void onNewIntent(final Intent intent) {
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
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        finish();
                        startActivity(new Intent(HelloActivity.this, MainActivity.class));
                    }
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
