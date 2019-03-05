package jp.noriokun4649.noriotter2.twitter;

import android.os.Handler;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.list.TweetListItemAdapter;
import twitter4j.AsyncTwitter;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterListener;
import twitter4j.TwitterMethod;

public class GetSearch {
    /**
     * ハンドラー.
     * このインスタンスを通して、じゃないとアプリの画面等を操作できません.
     */
    private Handler mHandler = new Handler();

    /**
     * 非同期処理のTwitterインスタンス.
     */
    private AsyncTwitter asyncTwitter;
    /**
     * Twitterの取得時のリスナー.
     */
    private TwitterListener twitterListener;
    /**
     * アクティビティの情報.
     */
    private FragmentActivity context;

    private boolean mode = false;


    public GetSearch(final FragmentActivity contexts, final AsyncTwitter asyncTwitterd, final TweetListItemAdapter adapter, final ICallBack callBack) {
        this.asyncTwitter = asyncTwitterd;
        this.context = contexts;
        twitterListener = new TwitterAdapter() {
            @Override
            public void searched(final QueryResult queryResult) {
                super.searched(queryResult);
                mHandler.post(() -> {
                    GetStatus status = new GetStatus(queryResult.getTweets(), asyncTwitter, adapter, context, callBack, mode);
                });

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

    public void getSearch(final Query query) {
        asyncTwitter.search(query);
    }

}