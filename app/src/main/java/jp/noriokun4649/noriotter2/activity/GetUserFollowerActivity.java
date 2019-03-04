package jp.noriokun4649.noriotter2.activity;

import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.list.UserListItemAdapter;
import jp.noriokun4649.noriotter2.twitter.GetFollower;
import twitter4j.AsyncTwitter;

public class GetUserFollowerActivity extends GetUserBase {

    @Override
    void getListData(final AsyncTwitter asyncTwitter, final UserListItemAdapter adapter) {
        GetFollower getFollower = new GetFollower(this, asyncTwitter, adapter);
        if (userId == 0L) {
            getFollower.getFollower();
        } else {
            getFollower.getFollower(userId);
        }
    }

    @Override
    int getTitles() {
        return R.string.follower;
    }

    @Override
    int getLoadingText() {
        return R.string.getting_follower_user_now;
    }

    @Override
    public void onItemClick(final int which, final String tag) {

    }
}
