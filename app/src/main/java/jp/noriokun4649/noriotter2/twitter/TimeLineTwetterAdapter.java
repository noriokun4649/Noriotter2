package jp.noriokun4649.noriotter2.twitter;

import org.apache.commons.lang3.ArrayUtils;

import twitter4j.AccountSettings;
import twitter4j.AsyncTwitter;
import twitter4j.IDs;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.User;

public class TimeLineTwetterAdapter extends TwitterAdapter {

    private StatusCallBack callBack;
    private final AsyncTwitter asyncTwitter;
    private long[] follow;

    public TimeLineTwetterAdapter(final StatusCallBack callBack, final AsyncTwitter asyncTwitter) {
        this.asyncTwitter = asyncTwitter;
        if (callBack != null) {
            this.callBack = callBack;
        }
    }

    @Override
    public void gotShowStatus(final Status status) {
        super.gotShowStatus(status);
        callBack.callbackStatus(status);
    }

    @Override
    public void gotFriendsIDs(final IDs ids) {
        super.gotFriendsIDs(ids);
        follow = ids.getIDs();
        asyncTwitter.getAccountSettings();
    }

    @Override
    public void gotAccountSettings(final AccountSettings settings) {
        super.gotAccountSettings(settings);
        asyncTwitter.showUser(settings.getScreenName());
    }

    @Override
    public void gotUserDetail(final User user) {
        super.gotUserDetail(user);
        follow = ArrayUtils.add(follow, user.getId());
        callBack.callbackFollow(follow, user);
    }

}
