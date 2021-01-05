package jp.noriokun4649.noriotter2.overlay;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import jp.noriokun4649.noriotter2.R;

/**
 * Created by noriokun4649 on 2017/05/08.
 */

public class ScreenConectActivity extends AppCompatActivity {
    private static final String TAG = "ScreenCapture";
    private static final int REQUEST_CODE_SCREEN_CAPTURE = 1;
    private static final int REQUEST_CODE_PLEASE_GRANT_PERMISSION = 3;
    public static ImageReader mImageReader; // スクリーンショット用
    public static int mWidth;
    public static int mHeight;
    private MediaProjectionManager mMediaProjectionManager;
    private VirtualDisplay mVirtualDisplay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conection);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityCompat.requestPermissions(ScreenConectActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PLEASE_GRANT_PERMISSION);
            mMediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            Intent permissionIntent = mMediaProjectionManager.createScreenCaptureIntent();
            startActivityForResult(permissionIntent, REQUEST_CODE_SCREEN_CAPTURE);
        } else {
            Toast.makeText(getApplicationContext(), "Andorid5.0以降でのみサポートされてます", Toast.LENGTH_SHORT).show();
            finish();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_CODE_SCREEN_CAPTURE) {
                if (resultCode != RESULT_OK) {
                    //パーミッションなし
                    Toast.makeText(this, "スクリーンショット取得には許可が必要です", Toast.LENGTH_LONG).show();
                    return;
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Toast.makeText(this, "現在のAndroid10以上ではスクリーンショットの取得に対応していません", Toast.LENGTH_LONG).show();
                    return;
                }
                // MediaProjectionの取得
                MediaProjection mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                mWidth = metrics.widthPixels;
                mHeight = metrics.heightPixels;
                int density = metrics.densityDpi;

                Log.d(TAG, "setup VirtualDisplay");
                SharedPreferences informat = PreferenceManager.getDefaultSharedPreferences(ScreenConectActivity.this);
                Log.d("inFormat", " int:" + informat.getInt("informat", PixelFormat.RGBA_8888));
                mImageReader = ImageReader.newInstance(mWidth, mHeight, informat.getInt("informat", PixelFormat.RGBA_8888), 2);
                mVirtualDisplay = mMediaProjection.createVirtualDisplay("Capturing Display",
                        mWidth, mHeight, density,
                        DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                        mImageReader.getSurface(), null, null);
                finish();
            } else if (requestCode == REQUEST_CODE_PLEASE_GRANT_PERMISSION) {

            }
        }
    }
}
