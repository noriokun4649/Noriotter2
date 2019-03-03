package jp.noriokun4649.noriotter2.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.activity.MediaActivity;
import jp.noriokun4649.noriotter2.list.TweetList;
import jp.noriokun4649.noriotter2.list.TweetListItemAdapter;
import jp.noriokun4649.noriotter2.twitter.GetStatus;
import jp.noriokun4649.noriotter2.twitter.ICallBack;
import jp.noriokun4649.noriotter2.twitter.StatusCallBack;
import jp.noriokun4649.noriotter2.twitter.TimeLineTwetterAdapter;
import jp.noriokun4649.noriotter2.twitter.TwitterConnect;
import twitter4j.AsyncTwitter;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserStreamAdapter;
import twitter4j.conf.Configuration;

public class RealTimeLine extends Fragment implements StatusCallBack, ICallBack {
    /**
     * ハンドラー.
     * このインスタンスを通して、じゃないとアプリの画面等を操作できません.
     */
    private Handler handler = new Handler();
    private long[] follow;
    private TwitterStream twitterStream;
    private AsyncTwitter asyncTwitter;
    private TweetListItemAdapter tweetListItemAdapter;
    private ArrayList<TweetList> arrayList;
    private View topView;
    private TweetList tweetList;
    private GetStatus getStatus;

    private UserStreamAdapter userStreamAdapter = new UserStreamAdapter() {
        @Override
        public void onException(final Exception ex) {
            handler.post(() -> {
                twitterStream.shutdown();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("Error ( *´艸｀)");
                alertDialog.setMessage(ex.getMessage());
                alertDialog.show();
                ex.printStackTrace();
            });
        }

        @Override
        public void onStallWarning(final StallWarning warning) {
            super.onStallWarning(warning);
        }

        @Override
        public void onStatus(final Status status) {
            handler.post(() -> {
                getStatus.getStatus(status, asyncTwitter, tweetListItemAdapter, getActivity(), false);
                //new GetStatus(status,asyncTwitter,  tweetListItemAdapter ,getActivity(),RealTimeLine.this,follow);
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.tl_layout, null);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ListView listView = view.findViewById(R.id.time_line);
        arrayList = new ArrayList<>();
        tweetListItemAdapter = new TweetListItemAdapter(getContext(), arrayList);
        listView.setAdapter(tweetListItemAdapter);

        final EditText edtext = view.findViewById(R.id.text);
        BootstrapButton imageButton = view.findViewById(R.id.image_button);
        BootstrapButton tweetButton = view.findViewById(R.id.tweet_button);

        final TwitterConnect twitterConnect = new TwitterConnect(inflater.getContext());
        twitterConnect.login();
        asyncTwitter = twitterConnect.getmTwitter();
        asyncTwitter.addListener(new TimeLineTwetterAdapter(this, asyncTwitter));


        Configuration configuration = twitterConnect.getConfiguration();
        if (configuration != null) {
            twitterStream = new TwitterStreamFactory(configuration).getInstance();
            twitterStream.addListener(userStreamAdapter);
        }
        FloatingActionButton actionButton = view.findViewById(R.id.floatingActionButton);
        IconicsDrawable gmdAuto = new IconicsDrawable(getContext(), GoogleMaterial.Icon.gmd_autorenew);
        actionButton.setImageDrawable(gmdAuto);
        actionButton.setVisibility(View.VISIBLE);
        actionButton.setOnClickListener(v -> {
            asyncTwitter.getFriendsIDs(-1);
            Toast.makeText(getContext(), "Stream接続開始", Toast.LENGTH_LONG).show();
        });

        imageButton.setOnClickListener(v -> twitterStream.shutdown());
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
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            RealTimeLine.this.tweetList = arrayList.get(arrayList.size() - 1 - position);
            switch (view1.getId()) {
                case R.id.image1:
                    openMediaActivity(view1, 0, "image0", tweetList.getMedias());
                    break;
                case R.id.image2:
                    openMediaActivity(view1, 1, "image1", tweetList.getMedias());
                    break;
                case R.id.image3:
                    openMediaActivity(view1, 2, "image2", tweetList.getMedias());
                    break;
                case R.id.image4:
                    openMediaActivity(view1, 3, "image3", tweetList.getMedias());
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
                default:
                    if ((view1.getId() == R.id.textView34) || (view1.getId() == R.id.textView35)) {
                        RealTimeLine.this.topView = view1;
                        asyncTwitter.showStatus(tweetList.getTweetid());
                    }
                    break;
            }
        });

        return view;
    }

    @Override
    public void callbackStatus(final Status status) {
        handler.post(() -> {
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
                        ab.setItems(items, (dialog, which) -> {
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
                        });
                        ab.show();
                    } else {
                        //asyncTwitter
                        asyncTwitter.destroyStatus(status.getId());
                        tweetList.setMeRt(false);
                        rtText.setTextColor(Color.BLACK);
                        tweetList.setRtcount(tweetList.getRtcount() - 1);
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

    private void openMediaActivity(final View view, final int index, final String name, final String[] medias) {
        Intent intent = new Intent(getActivity(), MediaActivity.class);
        intent.putExtra("urls", medias);
        view.setTransitionName(name);
        intent.putExtra("index", index);
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, view.getTransitionName());
        startActivity(intent, compat.toBundle());
    }

    @Override
    public void callbackFollow(final long[] followd, final User user) {
        this.follow = followd;
        getStatus = new GetStatus(follow);
        AsyncHttpRequest task = new AsyncHttpRequest();
        task.execute();
    }

    @Override
    public void callback() {

    }

    class AsyncHttpRequest extends AsyncTask<Uri.Builder, Void, String> {

        private AppCompatActivity mainActivity;

        AsyncHttpRequest() {
        }

        @Override
        protected String doInBackground(final Uri.Builder... builder) {
            FilterQuery filterQuery = new FilterQuery(0, follow);
            filterQuery.filterLevel("low");
            twitterStream.filter(filterQuery);
            return "";
        }

        @Override
        protected void onPostExecute(final String result) {
        }
    }
}
