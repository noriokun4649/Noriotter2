package jp.noriokun4649.noriotter2.twitter;

import android.os.Handler;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.list.TweetListItemAdapter;
import twitter4j.AsyncTwitter;
import twitter4j.Paging;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterListener;
import twitter4j.TwitterMethod;

public class GetUserTimeLine {
    /**
     * ハンドラー.
     * このインスタンスを通して、じゃないとアプリの画面等を操作できません.
     */
    private final Handler mHandler = new Handler();

    /**
     * 非同期処理のTwitterインスタンス.
     */
    private final AsyncTwitter asyncTwitter;
    /**
     * アクティビティの情報.
     */
    private final FragmentActivity context;

    private boolean mode = false;


    public GetUserTimeLine(final FragmentActivity contexts, final AsyncTwitter asyncTwitterd, final TweetListItemAdapter adapter, final ICallBack callBack) {
        this.asyncTwitter = asyncTwitterd;
        this.context = contexts;
        TwitterListener twitterListener = new TwitterAdapter() {
            @Override
            public void gotUserTimeline(final ResponseList<Status> statuses) {
                mHandler.post(() -> new GetStatus(statuses, asyncTwitter, adapter, context, callBack, mode));
            }

            @Override
            public void onException(final TwitterException te, final TwitterMethod method) {
                super.onException(te, method);
                mHandler.post(() -> {
                    if (te.getErrorCode() == 88) {
                        Toast.makeText(context, R.string.api_limit, Toast.LENGTH_LONG).show();
                    }
                    te.printStackTrace();
                    callBack.callback();
                });
            }
        };
        asyncTwitter.addListener(twitterListener);
    }

    public void getUserTimeLine(final long id) {
        mode = false;
        Paging paging = new Paging();
        paging.setCount(200);
        asyncTwitter.getUserTimeline(id, paging);
    }

    public void getFutureUserTimeLine(final long id, final long sinceId) {
        mode = false;
        Paging paging = new Paging();
        paging.setCount(200);
        paging.setSinceId(sinceId);
        asyncTwitter.getUserTimeline(id, paging);
    }

    public void getOldUserTimeLine(final long id, final long maxId) {
        mode = true;
        Paging paging = new Paging();
        paging.setCount(200);
        paging.setMaxId(maxId);
        asyncTwitter.getUserTimeline(id, paging);
    }


}
