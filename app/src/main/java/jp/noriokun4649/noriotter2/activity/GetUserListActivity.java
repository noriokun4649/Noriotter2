/*
 * Copyright (c) 2018 noriokun4649.
 */

package jp.noriokun4649.noriotter2.activity;

import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.twitter.GetList;
import twitter4j.AsyncTwitter;

/**
 * リストのユーザーからインポートする際のアクティビティです.
 */
public class GetUserListActivity extends GetUserBase {

    @Override
    void getListData(final AsyncTwitter asyncTwitter, /* final CircleListItemAdapter adapters,*/ final int counts, final long id) {
        GetList getList = new GetList(this, asyncTwitter, id, counts/*, adapters*/);
        getList.getList();
    }

    @Override
    int getTitles() {
        return 0;
    }

    @Override
    int getLoadingText() {
        return R.string.getting_list_user_now;
    }


    @Override
    public void onItemClick(final int which, final String tag) {

    }
}