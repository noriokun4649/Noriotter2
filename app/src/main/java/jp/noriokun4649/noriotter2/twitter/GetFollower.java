package jp.noriokun4649.noriotter2.twitter;

import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import jp.noriokun4649.noriotter2.R;
import twitter4j.AsyncTwitter;
import twitter4j.IDs;
import twitter4j.ResponseList;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterListener;
import twitter4j.TwitterMethod;
import twitter4j.User;

public class GetFollower {
    /**
     * ハンドラー.
     * このインスタンスを通して、じゃないとアプリの画面等を操作できません.
     */
    private Handler mHandler = new Handler();
    /**
     * 　取得進捗をひょじするびゅー.
     */
    private LinearLayout linearLayout;
    /**
     * 進捗表示するびゅーのテキスト.
     */
    private TextView textView;
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
    private AppCompatActivity context;
    /**
     * スナックバー.
     */
    private Snackbar snackbar;


    /**
     * コンストラクタ.
     * Twitterでフォローを取得する際のListenerの設定と、各Viewに対しての初期化処理などを行ってる
     *
     * @param contexts     アプリケーションコンテキスト
     * @param asyncTwitter 非同期処理のTwitterインスタンス
     */
    public GetFollower(final AppCompatActivity contexts, final AsyncTwitter asyncTwitter) {
        this.asyncTwitter = asyncTwitter;
        this.context = contexts;
        final CoordinatorLayout layout = context.findViewById(R.id.coord);
        snackbar = Snackbar.make(layout, R.string.api_limit,
                Snackbar.LENGTH_LONG).setAction(R.string.close, new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                context.finish();
            }
        });
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        twitterListener = new TwitterAdapter() {

            //フォロー中ユーザーの内部IDを1回につき5000件取得
            @Override
            public void gotFollowersIDs(final IDs ids) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        long[] idsd = ids.getIDs().clone();
                        int point = 0;
                        for (double a = 0; a <= Math.ceil(idsd.length / 100); a++) {
                            long[] longs = new long[100];
                            for (int as = 0; as < 100; as++) {
                                longs[as] = idsd[point];
                                point++;
                                if (idsd.length <= point) {
                                    break;
                                }
                            }
                            //100件ごとに内部IDを元にユーザデータを取得するようにする。
                            asyncTwitter.lookupUsers(longs);
                        }
                        if (ids.hasNext()) {
                            long cursor = ids.getNextCursor();
                            asyncTwitter.getFollowersIDs(cursor);
                        }
                    }
                });
            }

            @Override
            public void lookedupUsers(final ResponseList<User> users) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<String[]> arrayList = new ArrayList<>();
                        for (User stat : users) {
                            String[] af = {stat.getName(), "@" + stat.getScreenName(),
                                    stat.get400x400ProfileImageURLHttps()};
                            arrayList.add(af);
                        }
                        // ここでフォロワーユーザーの情報すべてとれる。
                        /*
                        GetCircleSpaceInfo circleSpaceInfo = new GetCircleSpaceInfo();
                        textView.setText(R.string.processing);
                        circleSpaceInfo.getData(sharedPreferences.getBoolean("setting0",
                                true), arrayList, adapter, linearLayout);
                        */
                        linearLayout.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onException(final TwitterException te, final TwitterMethod method) {
                mHandler.post(() -> {
                    linearLayout.setVisibility(View.GONE);
                    snackbar.show();
                });
                super.onException(te, method);
            }
        };
    }

    /**
     * フォロワーの取得をする.
     */
    public void getFollower() {
        asyncTwitter.addListener(twitterListener);
        asyncTwitter.getFollowersIDs(-1L);
    }

    public void getFollower(final long userid) {
        asyncTwitter.addListener(twitterListener);
        asyncTwitter.getFollowersIDs(userid, -1L);
    }

}


