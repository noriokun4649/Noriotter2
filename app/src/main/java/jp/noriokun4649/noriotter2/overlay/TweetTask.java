package jp.noriokun4649.noriotter2.overlay;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import jp.noriokun4649.noriotter2.twitter.TwitterConnect;
import twitter4j.StatusUpdate;

import static android.widget.Toast.makeText;
import static jp.noriokun4649.noriotter2.overlay.ScreenConectActivity.mImageReader;
import static jp.noriokun4649.noriotter2.overlay.ScreenConectActivity.mHeight;
import static jp.noriokun4649.noriotter2.overlay.ScreenConectActivity.mWidth;

/**
 * Created by noriokun4649 on 2017/05/12.
 */

public class TweetTask extends AsyncTask<Integer, Integer, Integer> {
    private Context mainActivity;
    private TwitterConnect twitterIn;
    private String fat;

    // コンストラクター
    public TweetTask(Context activity, TwitterConnect twitterIn, String fat) {
        mainActivity = activity;
        this.twitterIn = twitterIn;
        this.fat = fat;
    }


    // 非同期処理
    @Override
    protected Integer doInBackground(Integer... params) {

        // 5秒数える処理
        try {
            //　1sec sleep
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        twitterIn.getmTwitter().updateStatus(new StatusUpdate(fat).media(getBitmapFiles()));
        Log.d("debug", "" + params[0]);
        //params[0]++;
        // 途中経過を返す
        //publishProgress(params[0]);

        return params[0];
    }

    // 途中経過をメインスレッドに返す
    @Override
    protected void onProgressUpdate(Integer... progress) {
        //progressDialog_.incrementProgressBy(progress[0]);
        //mainActivity.setTextView(progress[0]);
    }

    // 非同期処理が終了後、結果をメインスレッドに返す
    @Override
    protected void onPostExecute(Integer result) {
        //mainActivity.setTextView(result);
    }

    public File getBitmapFiles() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Image image = mImageReader.acquireLatestImage();
            Image.Plane[] planes = image.getPlanes();
            ByteBuffer buffer = planes[0].getBuffer();

            int pixelStride = planes[0].getPixelStride();
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * mWidth;
            // バッファからBitmapを生成
            Bitmap.Config config = Bitmap.Config.ARGB_8888;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mainActivity);
            int outformat = sharedPreferences.getInt("outformat", 0);
            switch (outformat) {
                case 1:
                    config = Bitmap.Config.ARGB_4444;
                    break;
                case 2:
                    config = Bitmap.Config.RGB_565;
                    break;
                case 3:
                    config = Bitmap.Config.ALPHA_8;
                    break;
                default:
                    config = Bitmap.Config.ARGB_8888;
                    break;
            }
            Log.d("outFormat", " int:" + outformat);
            Bitmap bitmap = Bitmap.createBitmap(
                    mWidth + rowPadding / pixelStride, mHeight,
                    config);
            bitmap.copyPixelsFromBuffer(buffer);
            image.close();
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/images/");
            if (!file.exists()) {
                file.mkdir();
            }
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String AttachName = file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
            try {
                FileOutputStream out = new FileOutputStream(AttachName);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (IOException e) {
                Log.e("PAPAPA", e.toString());
                Toast ts = makeText(mainActivity, "", Toast.LENGTH_SHORT);
                ts.setText("写真、メディア、ファイルへのアクセスを許可してください");
                ts.show();
            }
            return new File(AttachName);
            // save index
        /*
        ContentValues values = new ContentValues();
        ContentResolver contentResolver = getContentResolver();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put("_data", AttachName);
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        noriokun©📶MSSアプリ開発中

        最近JavaでMinecraftのModとか作ってたりJavaでのAndroidアプリ開発にもはまってたりする基本コピペな、似非なんちゃって自称プログラマー。 PCは自作PC、プログラマーさんたちと近づきになれたらと思います。
        アニメはCharlotteとか好き友利ラブ。MSS開発中

        5pel5pys44Gr5L2P44KT44Gn44GE44

        noriokun4649.ddo.jp
        */
        } else {
            Toast toast = Toast.makeText(mainActivity, "", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.setText("Andorid5.0以降でのみサポートされてます");
            toast.show();
        }
        return null;
    }

}