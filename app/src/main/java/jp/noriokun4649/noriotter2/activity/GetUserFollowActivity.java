/*
 * Copyright (c) 2018 noriokun4649.
 */

package jp.noriokun4649.noriotter2.activity;

import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.list.UserListItemAdapter;
import jp.noriokun4649.noriotter2.twitter.GetFollow;
import twitter4j.AsyncTwitter;

/**
 * フォローからインポートする際のアクティビティです.
 */
public class GetUserFollowActivity extends GetUserBase {
    @Override
    void getListData(final AsyncTwitter asyncTwitter, final UserListItemAdapter adapter) {
        GetFollow getFollow = new GetFollow(this, asyncTwitter, adapter);
        if (getUserId() == 0L) {
            getFollow.getFollow();
        } else {
            getFollow.getFollow(getUserId());
        }
    }

    @Override
    int getTitles() {
        return R.string.follow;
    }

    @Override
    int getLoadingText() {
        return R.string.getting_follow_user_now;
    }

    @Override
    public void onItemClick(final int which, final String tag) {

    }
}
