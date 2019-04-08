package jp.noriokun4649.noriotter2.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.overlay.OverrideView;
import jp.noriokun4649.noriotter2.overlay.ScreenConectActivity;
import jp.noriokun4649.noriotter2.twitter.TwitterConnect;

public class DokodemoTweetActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PLEASE_GRANT_PERMISSION = 3;
    public TwitterConnect twitterIn = new TwitterConnect(this);

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSharedElementsUseOverlay(true);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean autostart = sharedPreferences.getBoolean("autostart", false);
        if (autostart) {
            startOverlay(true);
        }
        int permissionCheck = ContextCompat.checkSelfPermission(DokodemoTweetActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setTitle("パーミッションについて");
            ab.setMessage("Noriotterではスクリーンショットを取得時に一時的に画像を本体に保存するために写真、メディア、ファイルへのアクセス許可が必要になります。" +
                    "\n\nまた、スクリーンショット取得時にはキャプチャーの許可を促す画面が表示されますのでスクリーンショットを使用する場合は「今すぐ開始」を押してください。");
            ab.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(DokodemoTweetActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PLEASE_GRANT_PERMISSION);
                }
            });
            ab.show();
        }

        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.dokodemo_layout);
        twitterIn.login();
        IconicsDrawable gmdBack = new IconicsDrawable(this,
                GoogleMaterial.Icon.gmd_arrow_back).sizeDp(30).paddingDp(6).color(Color.WHITE).backgroundColor(Color.argb(140, 0, 0, 0)).roundedCornersDp(15);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.free_tweet);
        toolbar.setNavigationIcon(gmdBack);
        toolbar.setNavigationOnClickListener(v -> finish());
        Button button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startOverlay(false);
            }
        });
        Button button8 = (Button) findViewById(R.id.button8);
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(DokodemoTweetActivity.this, OverrideView.class));
            }
        });
        Button button5 = (Button) findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DokodemoTweetActivity.this, SettingActivity.class);
                startActivity(i);

            }
        });

        Button button6 = (Button) findViewById(R.id.button6);

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DokodemoTweetActivity.this, ScreenConectActivity.class));
            }
        });
        Button button7 = (Button) findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(DokodemoTweetActivity.this);
                final SharedPreferences.Editor editor = sharedPreferences.edit();
                CharSequence[] a = {"スクリーンショット入力", "スクリーンショット出力"};
                AlertDialog.Builder ab = new AlertDialog.Builder(DokodemoTweetActivity.this);
                ab.setTitle("カラーフォーマット選択");
                ab.setItems(a, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                CharSequence[] abin_item = {"RGBA_8888", "RGB_888", "RGBX_8888", "RGB_565"};
                                AlertDialog.Builder abin = new AlertDialog.Builder(DokodemoTweetActivity.this);
                                abin.setTitle("入力カラーフォーマット選択");
                                abin.setItems(abin_item, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which) {
                                            case 0:
                                                editor.putInt("informat", PixelFormat.RGBA_8888).apply();
                                                break;
                                            case 1:
                                                editor.putInt("informat", PixelFormat.RGBX_8888).apply();
                                                break;
                                            case 2:
                                                editor.putInt("informat", PixelFormat.RGB_888).apply();
                                                break;
                                            case 3:
                                                editor.putInt("informat", PixelFormat.RGB_565).apply();
                                                break;
                                        }
                                        Toast.makeText(getApplicationContext(), "入力カラーフォーマット変更後は再度スクショを許可ボタンを押してください", Toast.LENGTH_SHORT).show();
                                        Log.d("inFormat", " int:" + sharedPreferences.getInt("informat", PixelFormat.RGBA_8888));
                                    }
                                });
                                abin.show();
                                break;
                            case 1:
                                CharSequence[] about_item = {"ARGB_8888", "ARGB_4444", "RGB_565", "ALPHA_8"};
                                AlertDialog.Builder about = new AlertDialog.Builder(DokodemoTweetActivity.this);
                                about.setTitle("出力カラーフォーマット選択");
                                about.setItems(about_item, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        editor.putInt("outformat", which).apply();
                                    }
                                });
                                about.show();
                                break;
                        }
                    }
                });
                ab.show();
            }
        });
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        Switch sw = (Switch) findViewById(R.id.switch1);

        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("autoresize", isChecked).apply();
            }
        });
        sw.setChecked(sharedPreferences.getBoolean("autoresize", false));

        Switch sw2 = (Switch) findViewById(R.id.switch2);

        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("autostart", isChecked).apply();
            }
        });
        sw2.setChecked(sharedPreferences.getBoolean("autostart", false));

        Switch sw3 = (Switch) findViewById(R.id.switch3);
        sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("autoresize2", isChecked).apply();
            }
        });
        sw3.setChecked(sharedPreferences.getBoolean("autoresize2", false));

    }


    public void startOverlay(boolean ifclose) {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> listServiceInfo = am.getRunningServices(Integer.MAX_VALUE);
        boolean found = false;
        for (ActivityManager.RunningServiceInfo curr : listServiceInfo) {
            // クラス名を比較
            if (curr.service.getClassName().equals(OverrideView.class.getName())) {
                // 実行中のサービスと一致
                found = true;
                break;
            }
        }
        if (!found) {
            checkOverlayPermission();
            if (ifclose) {
                finish();
            }
        }
    }

    public void checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 5463);
            } else {
                startService(new Intent(DokodemoTweetActivity.this, OverrideView.class));
            }
        } else {
            startService(new Intent(DokodemoTweetActivity.this, OverrideView.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        }

        if (Build.VERSION.SDK_INT >= 23) {
            if (requestCode == 5463) {
                if (Settings.canDrawOverlays(this)) {
                    startService(new Intent(DokodemoTweetActivity.this, OverrideView.class));
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
