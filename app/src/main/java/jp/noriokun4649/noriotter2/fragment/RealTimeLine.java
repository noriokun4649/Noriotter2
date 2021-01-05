package jp.noriokun4649.noriotter2.fragment;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import jp.noriokun4649.noriotter2.twitter.GetStatus;
import jp.noriokun4649.noriotter2.twitter.ICallBack;
import jp.noriokun4649.noriotter2.twitter.StatusCallBack;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.UserStreamAdapter;
import twitter4j.conf.Configuration;

public class RealTimeLine extends TimeLineBase implements StatusCallBack, ICallBack {
    /**
     * ハンドラー.
     * このインスタンスを通して、じゃないとアプリの画面等を操作できません.
     */
    private final Handler handler = new Handler();
    private long[] follow;
    private TwitterStream twitterStream;
    private GetStatus getStatus;

    private final UserStreamAdapter userStreamAdapter = new UserStreamAdapter() {
        @Override
        public void onException(final Exception ex) {
            handler.post(() -> {
                twitterStream.shutdown();
                IconicsDrawable gmdAuto = new IconicsDrawable(getContext(), GoogleMaterial.Icon.gmd_autorenew);
                getActionButton().setImageDrawable(gmdAuto);
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
                getStatus.getStatus(status, getAsyncTwitter(), getTweetListItemAdapter(), getActivity(), false);
                //new GetStatus(status,asyncTwitter,  tweetListItemAdapter ,getActivity(),RealTimeLine.this,follow);
            });
        }
    };

    @Override
    public void callbackFollow(final long[] followd, final User user) {
        this.follow = followd;
        getStatus = new GetStatus(follow);
        AsyncHttpRequest task = new AsyncHttpRequest();
        task.execute();
    }

    @Override
    public void getFastLoad() {
        getSwipeRefreshLayout().setRefreshing(false);
        Configuration configuration = getTwitterConnect().getConfiguration();
        if (configuration != null) {
            twitterStream = new TwitterStreamFactory(configuration).getInstance();
            twitterStream.addListener(userStreamAdapter);
        }
        IconicsDrawable gmdAuto = new IconicsDrawable(getContext(), GoogleMaterial.Icon.gmd_autorenew);
        getActionButton().setImageDrawable(gmdAuto);
        getActionButton().setVisibility(View.VISIBLE);
        getActionButton().setOnClickListener(v -> {
            getAsyncTwitter().getFriendsIDs(-1);
            Toast.makeText(getContext(), "Stream接続開始", Toast.LENGTH_LONG).show();
            IconicsDrawable gmdClose = new IconicsDrawable(getContext(), GoogleMaterial.Icon.gmd_close);
            getActionButton().setImageDrawable(gmdClose);
        });
    }

    @Override
    public void getFutureLoad(final long index) {

    }

    @Override
    public void getOldLoad(final long index) {

    }

    class AsyncHttpRequest extends AsyncTask<Uri.Builder, Void, String> {
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
