package jp.noriokun4649.noriotter2.overlay;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.media.Image;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.activity.MainActivity;
import jp.noriokun4649.noriotter2.twitter.TwitterConnect;
import twitter4j.AsyncTwitter;

import static android.widget.Toast.makeText;
import static jp.noriokun4649.noriotter2.overlay.ScreenConectActivity.mImageReader;
import static jp.noriokun4649.noriotter2.overlay.ScreenConectActivity.mHeight;
import static jp.noriokun4649.noriotter2.overlay.ScreenConectActivity.mWidth;


/**
 * Created by noriokun4649 on 2017/03/09.
 */

public class OverrideView extends Service {
    private final int base = 140;
    private View player_view;
    private CheckBox checkBox;
    private View view;
    //画面に全面表示させるビュー
    private WindowManager wm;
    private int now;
    private TextView textView3;
    private TwitterConnect twitterIn;
    DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            twitterIn.login();
        }
    };
    private boolean longnow = false;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        final LayoutInflater inflater = LayoutInflater.from(this);
        final SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final SharedPreferences.Editor editor = preference.edit();
        //画面に常に表示するビューのレイアウトの設定
        int layoutParams;
        if (Build.VERSION.SDK_INT >= 26){
            layoutParams= WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else {
            layoutParams= WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        final WindowManager.LayoutParams params
                = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                preference.getInt("x", 0), preference.getInt("y", 0),
                layoutParams,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        final View.OnTouchListener taxt = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!longnow) {
                    return false;
                }
                int rawx = (int) motionEvent.getRawX();
                int rawy = (int) motionEvent.getRawY();// - 120;
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    int centerX = rawx - (size().x / 2);
                    int centerY = rawy - (size().y / 2);
                    params.x = centerX;
                    params.y = centerY;
                    editor.putInt("y", centerY).apply();
                    editor.putInt("x", centerX).apply();
                    wm.updateViewLayout(player_view, params);
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    player_view.setBackgroundColor(preference.getInt("main_color", Color.argb(60, 0, 0, 0)));
                    setTextColor(preference);
                    longnow = false;
                }
                return true;
            }
        };

        View.OnLongClickListener lons = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longnow = true;
                player_view.setBackgroundColor(preference.getInt("back_color", Color.argb(60, 255, 0, 0)));
                setTextColor(preference);
                player_view.setOnTouchListener(taxt);
                return false;
            }
        };
        wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        player_view = inflater.inflate(R.layout.input, null);
        player_view.setBackgroundColor(preference.getInt("main_color", Color.argb(60, 0, 0, 0)));
        player_view.setOnLongClickListener(lons);
        setTextColor(preference);
        twitterIn = new TwitterConnect(player_view.getContext());
        //レイヤーに重ねたいビュー
        twitterIn.login();
        final CheckBox shot = (CheckBox) player_view.findViewById(R.id.checkBox);
        shot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (mImageReader == null) {
                        startActivity(new Intent(OverrideView.this, ScreenConectActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }
            }
        });
        textView3 = (TextView) player_view.findViewById(R.id.textView4);
        final EditText editText3 = (EditText) player_view.findViewById(R.id.editText);
        editText3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBox.setChecked(true);
            }
        });
        editText3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longnow = true;
                player_view.setBackgroundColor(preference.getInt("back_color", Color.argb(60, 255, 0, 0)));
                editText3.setOnTouchListener(taxt);
                return false;
            }
        });
        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                now = base - editText3.getText().toString().length();
                textView3.setText("残り文字数:" + now);
            }
        });
        editText3.setImeOptions(EditorInfo.IME_ACTION_SEND);
        editText3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    Toast ts = makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
                    boolean isshot = false;
                    try {
                        ts.setGravity(Gravity.TOP, 0, 0);
                        boolean loginFlag = sharedPreferences.getBoolean("flag", false);
                        if (loginFlag) {
                            String fat = editText3.getText().toString();

                            if (now < 0) {
                                ts.setText("文字数を超えています");
                                ts.show();
                            } else {
                                if (shot.isChecked()) {
                                    isshot = true;
                                    //twitterIn.twitPhoto(fat, getBitmapFiles());
                                    TweetTask testTask = new TweetTask(OverrideView.this, twitterIn, fat);
                                    // executeを呼んでAsyncTaskを実行する、パラメータは１番目
                                    testTask.execute(0);
                                } else {
                                    if (!fat.equals("")) {
                                        twitterIn.getmTwitter().updateStatus(fat);
                                    } else {
                                        ts.setText("内容がありません");
                                        ts.show();
                                    }
                                }
                                //キーボード閉じる
                                InputMethodManager inputMethodManager = (InputMethodManager) player_view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(player_view.getWindowToken(), 0);
                                editText3.setText("");
                                checkBox.setChecked(false);
                                shot.setChecked(false);
                                boolean setete = preference.getBoolean("autoresize2", false);
                                if (preference.getBoolean("autoresize", false) || (setete && isshot)) {
                                    params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                                    wm.updateViewLayout(player_view, params);
                                    LinearLayout linearLayout = (LinearLayout) player_view.findViewById(R.id.laus);
                                    linearLayout.removeAllViews();
                                    view = inflater.inflate(R.layout.input2, linearLayout);
                                    final ImageView close = (ImageView) view.findViewById(R.id.imageView4);
                                    close.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            stopService(new Intent(OverrideView.this, OverrideView.class));
                                        }
                                    });
                                    close.setOnLongClickListener(new View.OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View v) {
                                            longnow = true;
                                            player_view.setBackgroundColor(preference.getInt("back_color", Color.argb(60, 255, 0, 0)));
                                            close.setOnTouchListener(taxt);
                                            return false;
                                        }
                                    });
                                    final ImageView goapp = (ImageView) view.findViewById(R.id.imageView6);
                                    goapp.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            editor.putBoolean("autostart", false).apply();
                                            startActivity(new Intent(OverrideView.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                        }
                                    });
                                    goapp.setOnLongClickListener(new View.OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View v) {
                                            longnow = true;
                                            player_view.setBackgroundColor(preference.getInt("back_color", Color.argb(60, 255, 0, 0)));
                                            goapp.setOnTouchListener(taxt);
                                            return false;
                                        }
                                    });
                                    final ImageView big = (ImageView) view.findViewById(R.id.imageView5);
                                    big.setOnLongClickListener(new View.OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View v) {
                                            longnow = true;
                                            player_view.setBackgroundColor(preference.getInt("back_color", Color.argb(60, 255, 0, 0)));
                                            big.setOnTouchListener(taxt);
                                            return false;
                                        }
                                    });
                                    big.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.laus2);
                                            linearLayout.removeAllViews();
                                            wm.removeView(player_view);
                                            //inflater.inflate(R.layout.input,linearLayout);
                                            //onStartCommand(intent, flags, startId);
                                            onCreate();
                                        }
                                    });

                                }
                            }
                        } else {
                            ts.setText("アプリケーションに戻り\nOAuth認証をしてください");
                            ts.show();
                        }
                    } catch (NullPointerException e) {
                        Log.e("Noriotter", e.toString());
                        ts.setText("アプリケーションに戻り\nスクショを許可してください");
                        ts.show();
                    } catch (UnsupportedOperationException e) {
                        ts.setText("お使いの端末がサポートしていない入力カラーフォーマット形式です\n\nアプリケーションに戻り変更してみてください");
                        ts.show();
                    }
                    return true;
                }
                return false;
            }
        });
        ImageButton button2 = (ImageButton) player_view.findViewById(R.id.button);
        button2.setImageResource(R.drawable.ic_send_white_24dp);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View viewd) {
                Toast ts = makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
                boolean isshot = false;
                try {
                    ts.setGravity(Gravity.TOP, 0, 0);
                    boolean loginFlag = sharedPreferences.getBoolean("flag", false);
                    if (loginFlag) {
                        String fat = editText3.getText().toString();
                        if (now < 0) {
                            ts.setText("文字数を超えています");
                            ts.show();
                        } else {
                            if (shot.isChecked()) {
                                isshot = true;
                                //twitterIn.twitPhoto(fat, getBitmapFiles());
                                TweetTask testTask = new TweetTask(OverrideView.this, twitterIn, fat);
                                // executeを呼んでAsyncTaskを実行する、パラメータは１番目
                                testTask.execute(0);
                            } else {
                                if (!fat.equals("")) {
                                    twitterIn.getmTwitter().updateStatus(fat);
                                } else {
                                    ts.setText("内容がありません");
                                    ts.show();
                                }
                            }
                            //キーボード閉じる
                            InputMethodManager inputMethodManager = (InputMethodManager) player_view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(player_view.getWindowToken(), 0);
                            editText3.setText("");
                            checkBox.setChecked(false);
                            shot.setChecked(false);

                            boolean setete = preference.getBoolean("autoresize2", false);
                            if (preference.getBoolean("autoresize", false) || (setete && isshot)) {
                                params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                                wm.updateViewLayout(player_view, params);
                                LinearLayout linearLayout = (LinearLayout) player_view.findViewById(R.id.laus);
                                linearLayout.removeAllViews();
                                view = inflater.inflate(R.layout.input2, linearLayout);
                                final ImageView close = (ImageView) view.findViewById(R.id.imageView4);
                                close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        stopService(new Intent(OverrideView.this, OverrideView.class));
                                    }
                                });
                                close.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        longnow = true;
                                        player_view.setBackgroundColor(preference.getInt("back_color", Color.argb(60, 255, 0, 0)));
                                        close.setOnTouchListener(taxt);
                                        return false;
                                    }
                                });
                                final ImageView goapp = (ImageView) view.findViewById(R.id.imageView6);
                                goapp.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        editor.putBoolean("autostart", false).apply();
                                        startActivity(new Intent(OverrideView.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    }
                                });
                                goapp.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        longnow = true;
                                        player_view.setBackgroundColor(preference.getInt("back_color", Color.argb(60, 255, 0, 0)));
                                        goapp.setOnTouchListener(taxt);
                                        return false;
                                    }
                                });
                                final ImageView big = (ImageView) view.findViewById(R.id.imageView5);
                                big.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {
                                        longnow = true;
                                        player_view.setBackgroundColor(preference.getInt("back_color", Color.argb(60, 255, 0, 0)));
                                        big.setOnTouchListener(taxt);
                                        return false;
                                    }
                                });
                                big.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.laus2);
                                        linearLayout.removeAllViews();
                                        wm.removeView(player_view);
                                        //inflater.inflate(R.layout.input,linearLayout);
                                        //onStartCommand(intent, flags, startId);
                                        onCreate();
                                    }
                                });
                            }
                        }
                    } else {
                        ts.setText("アプリケーションに戻り\nOAuth認証をしてください");
                        ts.show();
                    }
                } catch (NullPointerException e) {
                    Log.e("Noriotter", e.toString());
                    ts.setText("アプリケーションに戻り\nスクショを許可してください");
                    ts.show();
                } catch (UnsupportedOperationException e) {
                    ts.setText("お使いの端末がサポートしていない入力カラーフォーマット形式です\n\nアプリケーションに戻り変更してみてください");
                    ts.show();
                }
            }
        });
        checkBox = (CheckBox) player_view.findViewById(R.id.check);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                params.flags = isChecked ? WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL : WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                wm.updateViewLayout(player_view, params);
            }
        });
        checkBox.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                longnow = true;
                player_view.setBackgroundColor(preference.getInt("back_color", Color.argb(60, 255, 0, 0)));
                checkBox.setOnTouchListener(taxt);
                return false;
            }
        });
        ImageView goapp = (ImageView) player_view.findViewById(R.id.imageView7);
        goapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OverrideView.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
        ImageView delete = (ImageView) player_view.findViewById(R.id.imageView9);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                Long id = sharedPreferences.getLong("id", 0);
                AsyncTwitter a = twitterIn.getmTwitter();
                if (id != 0) {
                    a.destroyStatus(id);
                    editor1.remove("id").apply();
                } else {
                    Toast ts = Toast.makeText(getApplicationContext(), "このアプリから送信された直前のツイートが見つからなかったため削除できませんでした", Toast.LENGTH_SHORT);
                    ts.setGravity(Gravity.TOP, 0, 0);
                    ts.show();
                }
            }
        });
        //checkBox.setChecked(params.flags == WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE ? false : true);
        ImageView imageView = (ImageView) player_view.findViewById(R.id.imageView2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(OverrideView.this, OverrideView.class));
            }
        });
        ImageView imageView2 = (ImageView) player_view.findViewById(R.id.imageView3);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                wm.updateViewLayout(player_view, params);
                LinearLayout linearLayout = (LinearLayout) player_view.findViewById(R.id.laus);
                linearLayout.removeAllViews();
                view = inflater.inflate(R.layout.input2, linearLayout);
                final ImageView close = (ImageView) view.findViewById(R.id.imageView4);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        stopService(new Intent(OverrideView.this, OverrideView.class));
                    }
                });
                close.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        longnow = true;
                        player_view.setBackgroundColor(preference.getInt("back_color", Color.argb(60, 255, 0, 0)));
                        close.setOnTouchListener(taxt);
                        return false;
                    }
                });
                final ImageView goapp = (ImageView) view.findViewById(R.id.imageView6);
                goapp.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(OverrideView.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                });
                goapp.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        longnow = true;
                        player_view.setBackgroundColor(preference.getInt("back_color", Color.argb(60, 255, 0, 0)));
                        goapp.setOnTouchListener(taxt);
                        return false;
                    }
                });
                final ImageView big = (ImageView) view.findViewById(R.id.imageView5);
                big.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        longnow = true;
                        player_view.setBackgroundColor(preference.getInt("back_color", Color.argb(60, 255, 0, 0)));
                        big.setOnTouchListener(taxt);
                        return false;
                    }
                });
                big.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.laus2);
                        linearLayout.removeAllViews();
                        wm.removeView(player_view);
                        //inflater.inflate(R.layout.input,linearLayout);
                        //onStartCommand(intent, flags, startId);
                        onCreate();
                    }
                });
            }
        });
        ImageView imageView10 = (ImageView) player_view.findViewById(R.id.imageView10);
        imageView10.setImageResource(R.drawable.ic_content_paste_white_24dp);
        imageView10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (clipboard != null) {
                    // クリップボードにデータがある場合
                    if (clipboard.hasPrimaryClip()) {
                        // クリップボードのデータがテキストの場合
                        if (clipboard.getPrimaryClipDescription().hasMimeType(
                                ClipDescription.MIMETYPE_TEXT_PLAIN) || clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML)) {
                            // コピーしたアイテムを取得
                            ClipData.Item item = clipboard.getPrimaryClip()
                                    .getItemAt(0);
                            // ペーストする文字列をクリップボードから取得
                            String pasteData = item.getText().toString();
                            if (pasteData != null) {
                                editText3.setText(pasteData);
                            }
                        }
                    }
                }
            }
        });
        wm.addView(player_view, params);
        //レイヤーにビューを重ねる。
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressLint("NewApi")
    private Point size() {
        Display display = wm.getDefaultDisplay();
        Point p = new Point(0, 0);
        display.getSize(p);
        return p;
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
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(OverrideView.this);
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
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/picture/");
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
                Toast ts = makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
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
            Toast toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.setText("Andorid5.0以降でのみサポートされてます");
            toast.show();
        }
        return null;
    }
    private void setTextColor(SharedPreferences preference){
        EditText editText = (EditText) player_view.findViewById(R.id.editText);
        TextView textView = (TextView) player_view.findViewById(R.id.textView4);
        CheckBox cc = (CheckBox) player_view.findViewById(R.id.check);
        CheckBox ccb = (CheckBox) player_view.findViewById(R.id.checkBox);
        if (cc != null){
            cc.setTextColor(preference.getInt("text_color", Color.argb(255, 255, 255, 255)));
            ccb.setTextColor(preference.getInt("text_color", Color.argb(255, 255, 255, 255)));
            textView.setTextColor(preference.getInt("text_color", Color.argb(255, 255, 255, 255)));
            editText.setTextColor(preference.getInt("text_color", Color.argb(255, 255, 255, 255)));
            editText.setHintTextColor(preference.getInt("text_color", Color.argb(255, 255, 255, 255)));
        }
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            wm.removeView(player_view);
        } catch (IllegalArgumentException e) {
            makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        //ビューをレイヤーから削除する
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
