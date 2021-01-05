package jp.noriokun4649.noriotter2.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.activity.MediaActivity;
import jp.noriokun4649.noriotter2.activity.UserPageActivity;
import jp.noriokun4649.noriotter2.list.TweetList;
import jp.noriokun4649.noriotter2.list.TweetListItemAdapter;
import jp.noriokun4649.noriotter2.twitter.ICallBack;
import jp.noriokun4649.noriotter2.twitter.StatusCallBack;
import jp.noriokun4649.noriotter2.twitter.TimeLineTwetterAdapter;
import jp.noriokun4649.noriotter2.twitter.TwitterConnect;
import twitter4j.AsyncTwitter;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.User;

public abstract class TimeLineBase extends Fragment implements ICallBack, StatusCallBack {
    /**
     * インテントコールバック識別用のコード.
     */
    private static final int REQUEST_CODE = 1005;
    private TweetListItemAdapter tweetListItemAdapter;
    private TwitterConnect twitterConnect;
    private SwipeRefreshLayout swipeRefreshLayout;
    private AsyncTwitter asyncTwitter;
    private FloatingActionButton actionButton;
    private Handler mHandler = new Handler();
    private ArrayList<TweetList> arrayList;
    private View topView;
    private TweetList tweetList;
    private ListView listView;
    private boolean flag = false;
    private ConstraintLayout constraintLayout;
    private LinearLayout quit;
    private TextView quitName;
    private TextView quitScreenname;
    private TextView quitText;
    private TextView textMode;
    private BootstrapButton tweetButton;
    private EditText edtext;
    private ArrayList<File> fileArrayList = new ArrayList<>();
    private InputMethodManager inputMethodManager;
    private View.OnClickListener onClickListener = v -> {
        String text = edtext.getText().toString();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        if (fileArrayList.size() == 0 || fileArrayList == null) {
            asyncTwitter.updateStatus(text);
        } else {
            //twitterConnect.loginSing(text, fileArrayList);
            fileArrayList.clear();
        }
        edtext.setText("");
        inputMethodManager.hideSoftInputFromWindow(constraintLayout.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tl_layout, null);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        twitterConnect = new TwitterConnect(inflater.getContext());
        twitterConnect.login();
        listView = view.findViewById(R.id.time_line);
        arrayList = new ArrayList<>();
        tweetListItemAdapter = new TweetListItemAdapter(getContext(), arrayList);
        listView.setAdapter(tweetListItemAdapter);
        asyncTwitter = twitterConnect.getmTwitter();
        asyncTwitter.addListener(new TimeLineTwetterAdapter(this, asyncTwitter));
        edtext = view.findViewById(R.id.text);
        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        BootstrapButton buttonClose = view.findViewById(R.id.button_close);
        BootstrapButton imageButton = view.findViewById(R.id.image_button);
        tweetButton = view.findViewById(R.id.tweet_button);
        constraintLayout = view.findViewById(R.id.constraintLayout);
        quit = view.findViewById(R.id.quit_line);
        quitName = view.findViewById(R.id.quit_name);
        quitScreenname = view.findViewById(R.id.quit_screanname);
        quitText = view.findViewById(R.id.quit_text);
        textMode = view.findViewById(R.id.textMode);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (arrayList.size() > 0) {
                getFutureLoad(arrayList.get(arrayList.size() - 1).getTweetid());
            }
        });
        buttonClose.setOnClickListener(v -> {
            constraintLayout.setVisibility(View.GONE);
            quit.setVisibility(View.GONE);
            tweetButton.setOnClickListener(onClickListener);
        });
        imageButton.setOnClickListener(v -> {
            String[] mimeType = new String[]{"image/*"};
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType);
            startActivityForResult(Intent.createChooser(intent, null), REQUEST_CODE);
        });
        swipeRefreshLayout.setRefreshing(true);
        actionButton = view.findViewById(R.id.floatingActionButton);
        IconicsDrawable gmdUp = new IconicsDrawable(getContext(), GoogleMaterial.Icon.gmd_arrow_upward);
        actionButton.setImageDrawable(gmdUp);
        actionButton.setVisibility(View.VISIBLE);
        actionButton.setOnClickListener(v -> {
            listView.smoothScrollToPosition(0);
        });
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean loginFlag = sharedPreferences.getBoolean("flag", false);
        if (loginFlag) {
            getFastLoad();
        }
        tweetButton.setOnClickListener(onClickListener);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final AbsListView view, final int scrollState) {
                if (listView.getLastVisiblePosition() == (tweetListItemAdapter.getCount() - 1) && !flag) {
                    swipeRefreshLayout.setRefreshing(true);
                    getOldLoad(arrayList.get(0).getTweetid());
                    flag = true;
                }
            }

            @Override
            public void onScroll(final AbsListView view, final int firstVisibleItem, final int visibleItemCount, final int totalItemCount) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                TimeLineBase.this.tweetList = arrayList.get(arrayList.size() - 1 - position);
                int getId = view.getId();
                switch (getId) {
                    case R.id.image1:
                        openMediaActivity(view, 0, "image0", tweetList.getMedias());
                        break;
                    case R.id.quit_image1:
                        openMediaActivity(view, 0, "image0", tweetList.getQuitMedias());
                        break;
                    case R.id.image2:
                        openMediaActivity(view, 1, "image1", tweetList.getMedias());
                        break;
                    case R.id.quit_image2:
                        openMediaActivity(view, 1, "image1", tweetList.getQuitMedias());
                        break;
                    case R.id.image3:
                        openMediaActivity(view, 2, "image2", tweetList.getMedias());
                        break;
                    case R.id.quit_image3:
                        openMediaActivity(view, 2, "image2", tweetList.getQuitMedias());
                        break;
                    case R.id.image4:
                        openMediaActivity(view, 3, "image3", tweetList.getMedias());
                        break;
                    case R.id.quit_image4:
                        openMediaActivity(view, 3, "image3", tweetList.getQuitMedias());
                        break;
                    case R.id.textView33:
                        textMode.setText(R.string.reply);
                        constraintLayout.setVisibility(View.VISIBLE);
                        quit.setVisibility(View.VISIBLE);
                        quitName.setText(tweetList.getName());
                        quitScreenname.setText(tweetList.getScreanname());
                        quitText.setText(tweetList.getTwiite());
                        tweetButton.setOnClickListener(v -> {
                            Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            asyncTwitter.updateStatus(new StatusUpdate(tweetList.getScreanname() + " " + edtext.getText()).inReplyToStatusId(tweetList.getTweetid()));
                            edtext.setText("");
                            constraintLayout.setVisibility(View.GONE);
                            quit.setVisibility(View.GONE);
                            tweetButton.setOnClickListener(onClickListener);
                        });
                        break;
                    case R.id.textView36:
                        break;
                    case R.id.imageView8:
                        Intent intent = new Intent(getActivity(), UserPageActivity.class);
                        intent.putExtra("userid", tweetList.getScreanname());
                        startActivity(intent);
                    default:
                        if ((view.getId() == R.id.textView34) || (view.getId() == R.id.textView35)) {
                            TimeLineBase.this.topView = view;
                            asyncTwitter.showStatus(tweetList.getTweetid());
                        }
                        break;
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Toast.makeText(getContext(), String.valueOf(fileArrayList.size()), Toast.LENGTH_LONG).show();
    }

    private void openMediaActivity(final View view, final int index, final String name, final String[] medias) {
        Intent intent = new Intent(getActivity(), MediaActivity.class);
        intent.putExtra("urls", medias);
        view.setTransitionName(name);
        intent.putExtra("index", index);
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, view.getTransitionName());
        startActivity(intent, compat.toBundle());
    }

    @Override
    public void callback() {
        flag = false;
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void callbackStatus(final Status status) {
        mHandler.post(() -> {
            boolean isRetweeted = status.isRetweeted();
            boolean isFavorited = status.isFavorited();
            switch (topView.getId()) {
                case R.id.textView34:
                    TextView rtText = (TextView) topView;
                    if (!isRetweeted) {
                        AlertDialog.Builder ab = new AlertDialog.Builder(topView.getContext());
                        ab.setTitle(status.getText());
                        String[] items = {getString(R.string.retweet), getString(R.string.quit_tweet)};
                        ab.setPositiveButton("閉じる", null);
                        ab.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, final int which) {
                                switch (which) {
                                    case 0:
                                        asyncTwitter.retweetStatus(status.getId());
                                        rtText.setTextColor(Color.GREEN);
                                        tweetList.setMeRt(true);
                                        tweetList.setRtcount(tweetList.getRtcount() + 1);
                                        break;
                                    case 1:
                                        textMode.setText(R.string.quit_tweet);
                                        constraintLayout.setVisibility(View.VISIBLE);
                                        quit.setVisibility(View.VISIBLE);
                                        quitName.setText(tweetList.getName());
                                        quitScreenname.setText(tweetList.getScreanname());
                                        quitText.setText(tweetList.getTwiite());
                                        tweetButton.setOnClickListener(v -> {
                                            Objects.requireNonNull(getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                            asyncTwitter.updateStatus(edtext.getText() + "\nhttps://twitter.com/"
                                                    + tweetList.getScreanname().replace("@", "") + "/status/" + tweetList.getTweetid());
                                            edtext.setText("");
                                            constraintLayout.setVisibility(View.GONE);
                                            quit.setVisibility(View.GONE);
                                            tweetButton.setOnClickListener(onClickListener);
                                        });
                                        break;
                                    default:
                                }
                                tweetListItemAdapter.notifyDataSetChanged();
                            }
                        });
                        ab.show();
                    } else {
                        //asyncTwitter.unretweetStatus(status.getId());
                        tweetList.setMeRt(false);
                        rtText.setTextColor(Color.BLACK);
                        tweetList.setRtcount(tweetList.getRtcount() - 1);
                        tweetListItemAdapter.notifyDataSetChanged();
                    }
                    break;
                case R.id.textView35:
                    TextView favText = (TextView) topView;
                    if (!isFavorited) {
                        asyncTwitter.createFavorite(status.getId());
                        favText.setTextColor(Color.RED);
                        tweetList.setMeFav(true);
                        tweetList.setFavocount(tweetList.getFavocount() + 1);
                    } else {
                        asyncTwitter.destroyFavorite(status.getId());
                        favText.setTextColor(Color.BLACK);
                        tweetList.setMeFav(false);
                        tweetList.setFavocount(tweetList.getFavocount() - 1);
                    }
                    tweetListItemAdapter.notifyDataSetChanged();
                    break;
                default:
            }
        });
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode,
                                 final Intent resultData) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK && resultData != null && resultData.getData() != null) {
            Uri uri = resultData.getData();
            try (Cursor cursor = Objects.requireNonNull(getContext()).getContentResolver().query(uri, null,
                    null, null, null, null);
                 InputStream inputStream = getContext().getContentResolver().openInputStream(uri)) {
                if (cursor != null) {
                    cursor.moveToFirst();
                    final String displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    if (isImage(displayName)) {
                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Pictures/Noriotter2", displayName);
                        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                            assert inputStream != null;
                            IOUtils.copy(inputStream, fileOutputStream);
                        } catch (AssertionError error) {
                            error.printStackTrace();
                        } catch (IOException error) {
                            error.printStackTrace();
                        }
                        fileArrayList.add(file);

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isImage(final String fileName) {
        return fileName.matches(".*\\.(jpeg|jpg|png|gif)+$");
    }

    @Override
    public void callbackFollow(final long[] follow, final User user) {

    }

    public abstract void getFastLoad();

    public abstract void getFutureLoad(long index);

    public abstract void getOldLoad(long index);

    /**
     * Gets tweetListItemAdapter .
     *
     * @return value of tweetListItemAdapter
     */
    protected TweetListItemAdapter getTweetListItemAdapter() {
        return tweetListItemAdapter;
    }

    /**
     * Gets asyncTwitter .
     *
     * @return value of asyncTwitter
     */
    public AsyncTwitter getAsyncTwitter() {
        return asyncTwitter;
    }

    /**
     * Gets twitterConnect .
     *
     * @return value of twitterConnect
     */
    public TwitterConnect getTwitterConnect() {
        return twitterConnect;
    }

    /**
     * Gets swipeRefreshLayout .
     *
     * @return value of swipeRefreshLayout
     */
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    /**
     * Gets actionButton .
     *
     * @return value of actionButton
     */
    public FloatingActionButton getActionButton() {
        return actionButton;
    }
}
