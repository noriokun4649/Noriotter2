package jp.noriokun4649.noriotter2.twitter;

import android.os.Handler;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.list.UserList;
import jp.noriokun4649.noriotter2.list.UserListItemAdapter;
import twitter4j.AsyncTwitter;
import twitter4j.ResponseList;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterListener;
import twitter4j.TwitterMethod;
import twitter4j.User;

public class GetUserSearch {
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

    public GetUserSearch(final FragmentActivity contexts, final AsyncTwitter asyncTwitterd, final UserListItemAdapter adapter) {
        this.asyncTwitter = asyncTwitterd;
        this.context = contexts;
        TwitterListener twitterListener = new TwitterAdapter() {

            @Override
            public void searchedUser(final ResponseList<User> users) {
                super.searchedUser(users);
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
                    }
                    adapter.notifyDataSetChanged();
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
                });
            }
        };
        asyncTwitter.addListener(twitterListener);
    }

    public void getUserSearch(final String text, final int index) {
        asyncTwitter.searchUsers(text, index);
    }
}
