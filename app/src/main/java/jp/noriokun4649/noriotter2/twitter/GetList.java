/*
 * Copyright (c) 2018 noriokun4649.
 */

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
     * アクティビティの情報.
     */
    private AppCompatActivity context;
    /**
     * 取得完了したメンバーの数.
     * 動的
     */
    private int count;
    /**
     * 　取得進捗をひょじするびゅー.
     */
    private LinearLayout linearLayout;
    /**
     * 進捗表示するびゅーのテキスト.
     */
    private TextView textView;
    /**
     * リストのメンバーの数.
     */
    private int memberCount;
    /**
     * スナックバー.
     */
    private Snackbar snackbar;

    /**
     * コンストラクタ.
     * Twitterでフォローを取得する際のListenerの設定と、各Viewに対しての初期化処理などを行ってる
     *
     * @param contexts     アクティビティのコンテキスト
     * @param asyncTwitter 非同期処理のTwitterインスタンス
     * @param listID       取得するListのID
     * @param memberCount  そのListに何人のメンバーがいるかを入れる
     */
    public GetList(final AppCompatActivity contexts, final AsyncTwitter asyncTwitter, final Long listID,
                   final int memberCount) {
        this.asyncTwitter = asyncTwitter;
        this.listID = listID;
        this.context = contexts;
        this.memberCount = memberCount;
        textView = context.findViewById(R.id.textView4);
        linearLayout = context.findViewById(R.id.progress);
        final ArrayList<String[]> arrayList = new ArrayList<>();
        final CoordinatorLayout layout = context.findViewById(R.id.coord);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        snackbar = Snackbar.make(layout, R.string.api_limit,
                Snackbar.LENGTH_LONG).setAction(R.string.close, new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                context.finish();
            }
        });
        twitterListener = new TwitterAdapter() {
            @Override
            public void gotUserListMembers(final PagableResponseList<User> users) {
                mHandler.post(() -> {
                    for (User user : users) {
                        count++;
                        String[] af = {user.getName(), "@" + user.getScreenName(),
                                user.get400x400ProfileImageURLHttps()};
                        arrayList.add(af);
                        textView.setText(
                                context.getString(R.string.getting_list_user_more_now,
                                        memberCount, count));
                    }

                    if (users.hasNext()) {
                        long cursor = users.getNextCursor();
                        asyncTwitter.getUserListMembers(listID, cursor);
                    } else {
                        textView.setText(R.string.processing);
                        /*
                        GetCircleSpaceInfo circleSpaceInfo = new GetCircleSpaceInfo();
                        circleSpaceInfo.getData(sharedPreferences.getBoolean("setting1",
                                false), arrayList, adapter, linearLayout);
                                */
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
     * List取得.
     */
    public void getList() {
        asyncTwitter.addListener(twitterListener);
        asyncTwitter.getUserListMembers(listID, -1L);
    }
}
