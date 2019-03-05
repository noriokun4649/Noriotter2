/*
 * Copyright (c) 2018 noriokun4649.
 */

package jp.noriokun4649.noriotter2.twitter;

import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.list.UserList;
import jp.noriokun4649.noriotter2.list.UserListItemAdapter;
import twitter4j.AsyncTwitter;
import twitter4j.IDs;
import twitter4j.ResponseList;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterListener;
import twitter4j.TwitterMethod;
import twitter4j.User;

/**
 * Twitterからフォローしているユーザーを取得するクラスです.
 */
public class GetFollow {
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
    private AppCompatActivity context;

    /**
     * コンストラクタ.
     * Twitterでフォローを取得する際のListenerの設定と、各Viewに対しての初期化処理などを行ってる
     *
     * @param contexts     アプリケーションコンテキスト
     * @param asyncTwitter 非同期処理のTwitterインスタンス
     * @param adapter      　　　リストViewのAdapter
     */
    public GetFollow(final AppCompatActivity contexts, final AsyncTwitter asyncTwitter, final UserListItemAdapter adapter) {
        this.asyncTwitter = asyncTwitter;
        this.context = contexts;
        twitterListener = new TwitterAdapter() {

            //フォロー中ユーザーの内部IDを1回につき5000件取得
            @Override
            public void gotFriendsIDs(final IDs ids) {
                mHandler.post(() -> {
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
                        asyncTwitter.getFriendsIDs(cursor);
                    }
                });
            }

            @Override
            public void lookedupUsers(final ResponseList<User> users) {
                mHandler.post(() -> {
                    for (User user : users) {
                        UserList userList = new UserList();
                        userList.setUserIconUrl(user.get400x400ProfileImageURLHttps());
                        userList.setUserName(user.getName());
                        userList.setUserScreenName("@" + user.getScreenName());
                        userList.setUserId(user.getId());
                        userList.setUserInfo(user.getDescription());
                        userList.setUserLock(user.isProtected());
                        userList.setUserOffical(user.isVerified());
                        adapter.add(userList);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onException(final TwitterException te, final TwitterMethod method) {
                mHandler.post(() -> {
                    Toast.makeText(context, R.string.api_limit, Toast.LENGTH_LONG).show();
                });
                super.onException(te, method);
            }
        };
    }

    /**
     * フォローの取得をする.
     */
    public void getFollow() {
        asyncTwitter.addListener(twitterListener);
        asyncTwitter.getFriendsIDs(-1L);
    }

    /**
     * UserIDのフォロー取得.
     *
     * @param userid ユーザーID
     */
    public void getFollow(final long userid) {
        asyncTwitter.addListener(twitterListener);
        asyncTwitter.getFriendsIDs(userid, -1L);
    }
}
