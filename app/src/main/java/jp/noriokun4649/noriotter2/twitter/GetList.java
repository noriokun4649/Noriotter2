/*
 * Copyright (c) 2018 noriokun4649.
 */

package jp.noriokun4649.noriotter2.twitter;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.list.UserList;
import jp.noriokun4649.noriotter2.list.UserListItemAdapter;
import twitter4j.AsyncTwitter;
import twitter4j.PagableResponseList;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterListener;
import twitter4j.TwitterMethod;
import twitter4j.User;

/**
 * Twitterからリストを取得するクラスです.
 */
public class GetList {
    /**
     * ハンドラー.
     * このインスタンスを通して、じゃないとアプリの画面等を操作できません.
     */
    private Handler mHandler = new Handler();
    /**
     * リストのID.
     */
    private Long listID;
    /**
     * 非同期処理のTwitterインスタンス.
     */
    private AsyncTwitter asyncTwitter;
    /**
     * Twitterの取得時のリスナー.
     */
    private TwitterListener twitterListener;
    /**
     * 取得完了したメンバーの数.
     * 動的
     */
    private int count;

    /**
     * コンストラクタ.
     * Twitterでフォローを取得する際のListenerの設定と、各Viewに対しての初期化処理などを行ってる
     *
     * @param context      アクティビティのコンテキスト
     * @param asyncTwitter 非同期処理のTwitterインスタンス
     * @param adapter      リストViewのアダプタ
     * @param listID       取得するListのID
     * @param memberCount  そのListに何人のメンバーがいるかを入れる
     * @param view         びゅー
     */
    public GetList(final Activity context, final AsyncTwitter asyncTwitter, final UserListItemAdapter adapter, final Long listID,
                   final int memberCount, final View view) {
        this.asyncTwitter = asyncTwitter;
        this.listID = listID;
        TextView textView = view.findViewById(R.id.textView4);
        LinearLayout linearLayout = view.findViewById(R.id.progress);
        twitterListener = new TwitterAdapter() {
            @Override
            public void gotUserListMembers(final PagableResponseList<User> users) {
                mHandler.post(() -> {
                    for (User user : users) {
                        count++;
                        UserList userList = new UserList();
                        userList.setUserIconUrl(user.get400x400ProfileImageURLHttps());
                        userList.setUserName(user.getName());
                        userList.setUserScreenName("@" + user.getScreenName());
                        userList.setUserId(user.getId());
                        userList.setUserInfo(user.getDescription());
                        userList.setUserLock(user.isProtected());
                        userList.setUserOffical(user.isVerified());
                        adapter.add(userList);
                        textView.setText(
                                context.getString(R.string.getting_list_user_more_now,
                                        memberCount, count));
                    }

                    if (users.hasNext()) {
                        long cursor = users.getNextCursor();
                        asyncTwitter.getUserListMembers(listID, cursor);
                    } else {
                        textView.setText(R.string.processing);
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onException(final TwitterException te, final TwitterMethod method) {
                mHandler.post(() -> {
                    linearLayout.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.api_limit, Toast.LENGTH_LONG).show();
                });
                super.onException(te, method);
            }
        };
    }

    /**
     * List取得.
     */
    public void getList() {
        asyncTwitter.addListener(twitterListener);
        asyncTwitter.getUserListMembers(listID, -1L);
    }
}
