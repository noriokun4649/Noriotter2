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

public class GetTimeLine {
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

    //private boolean mode = false;


    public GetTimeLine(final FragmentActivity contexts, final AsyncTwitter asyncTwitterd, final TweetListItemAdapter adapter, final ICallBack callBack) {
        this.asyncTwitter = asyncTwitterd;
        this.context = contexts;
        twitterListener = new TwitterAdapter() {
            @Override
            public void gotHomeTimeline(final ResponseList<Status> statuses) {
                mHandler.post(() -> {
                    /*
                    if (mode){
                        Collections.reverse(statuses);
                    }
                    */
                    GetStatus status = new GetStatus(statuses, asyncTwitter, adapter, context, callBack);
                });
            }

            @Override
            public void onException(final TwitterException te, final TwitterMethod method) {
                super.onException(te, method);
                mHandler.post(() -> {
                    Toast.makeText(context, R.string.api_limit, Toast.LENGTH_LONG).show();
                    te.printStackTrace();
                    callBack.callback();
                });
            }
        };
        asyncTwitter.addListener(twitterListener);
    }

    public void getTimeLine() {
        Paging paging = new Paging();
        paging.setCount(200);
        asyncTwitter.getHomeTimeline(paging);
    }

    public void getFutureTimeLine(final long sinceId) {
        Paging paging = new Paging();
        paging.setCount(200);
        paging.setSinceId(sinceId);
        asyncTwitter.getHomeTimeline(paging);
    }

    public void getOldTimeLine(final long maxId) {
        //mode = true;
        Paging paging = new Paging();
        paging.setCount(200);
        paging.setMaxId(maxId);
        asyncTwitter.getHomeTimeline(paging);
    }


}