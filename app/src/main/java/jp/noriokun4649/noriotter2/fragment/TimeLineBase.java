package jp.noriokun4649.noriotter2.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import twitter4j.User;

abstract public class TimeLineBase extends Fragment implements ICallBack, StatusCallBack {
    protected TweetListItemAdapter tweetListItemAdapter;
    protected TwitterConnect twitterConnect;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected AsyncTwitter asyncTwitter;
    protected FloatingActionButton actionButton;
    private Handler mHandler = new Handler();
    private ArrayList<TweetList> arrayList;
    private View topView;
    private TweetList tweetList;
    private ListView listView;
    private boolean flag = false;

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
        final EditText edtext = view.findViewById(R.id.text);
        BootstrapButton imageButton = view.findViewById(R.id.image_button);
        BootstrapButton tweetButton = view.findViewById(R.id.tweet_button);
        //GetTimeLine getTimeLine = new GetTimeLine(getActivity(), asyncTwitter, tweetListItemAdapter, this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (arrayList.size() > 0) {
                    //getTimeLine.getFutureTimeLine(arrayList.get(arrayList.size() - 1).getTweetid());
                    getFutureLoad(arrayList.get(arrayList.size() - 1).getTweetid());
                }
            }
        });
        swipeRefreshLayout.setRefreshing(true);
        actionButton = view.findViewById(R.id.floatingActionButton);
        IconicsDrawable gmdUp = new IconicsDrawable(getContext(), GoogleMaterial.Icon.gmd_arrow_upward);
        actionButton.setImageDrawable(gmdUp);
        actionButton.setVisibility(View.VISIBLE);
        actionButton.setOnClickListener(v -> {
            listView.smoothScrollToPosition(0);
        });

        //getTimeLine.getTimeLine();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean loginFlag = sharedPreferences.getBoolean("flag", false);
        if (loginFlag) {
            getFastLoad();
        }
        tweetButton.setOnClickListener(v -> {
            String text = edtext.getText().toString();
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            //if (media == null) {
            asyncTwitter.updateStatus(text);
                /*
            } else {
                asyncTwitter.updateStatus(new StatusUpdate(string).media(media));
                media = null;
            }*/
            edtext.setText("");
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(final AbsListView view, final int scrollState) {
                if (listView.getLastVisiblePosition() == (tweetListItemAdapter.getCount() - 1) && !flag) {
                    swipeRefreshLayout.setRefreshing(true);
                    //getTimeLine.getOldTimeLine(arrayList.get(0).getTweetid());
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
                            /*
                            LayoutInflater factory = LayoutInflater.from(getContext());
                            final View inputView = factory.inflate(R.layout.reply, null);
                            final AlertDialog.Builder as = new AlertDialog.Builder(getContext());
                            final AlertDialog alertDialog = as.create();
                            alertDialog.setView(inputView);
                            final EditText et = (EditText) inputView.findViewById(R.id.editText7);
                            Button a = (Button) inputView.findViewById(R.id.button12);
                            a.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String input = et.getText().toString();
                                    asyncTwitter.updateStatus(new StatusUpdate("@" + useredata.name + " " + input).inReplyToStatusId(useredata.tweetid));
                                    alertDialog.dismiss();
                                }
                            });
                            alertDialog.show();
                            */
                        break;
                    case R.id.textView36:
                        break;
                    /* case R.id.videoView:
                        Intent intent = new Intent(getActivity(), MediaActivity.class);
                        intent.putExtra("urls", tweetList.getMedias());
                        intent.putExtra("index", 0);
                        startActivity(intent);
                        break; */
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
                        String[] items = {"りついーと", "いんようついーと"};
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
                                    case 1:/*
                                            LayoutInflater factorys = LayoutInflater.from(getContext());
                                            final View inputViews = factorys.inflate(R.layout.reply, null);
                                            final AlertDialog.Builder ass = new AlertDialog.Builder(getContext());
                                            final AlertDialog alertDialogs = ass.create();
                                            alertDialogs.setView(inputViews);
                                            final EditText ets = (EditText) inputViews.findViewById(R.id.editText7);
                                            Button ast = (Button) inputViews.findViewById(R.id.button12);
                                            ast.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String input = ets.getText().toString();
                                                    asyncTwitter.updateStatus(input + " https://twitter.com/" + useredata.name + "/status/" + useredata.tweetid);
                                                    alertDialogs.dismiss();
                                                }
                                            });
                                            alertDialogs.show();
                                            */
                                        break;
                                    default:
                                }
                                tweetListItemAdapter.notifyDataSetChanged();
                            }
                        });
                        ab.show();
                    } else {
                        //asyncTwitter
                        asyncTwitter.unretweetStatus(status.getId());
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
    public void callbackFollow(final long[] follow, final User user) {

    }

    public abstract void getFastLoad();

    public abstract void getFutureLoad(long index);

    public abstract void getOldLoad(long index);
}
