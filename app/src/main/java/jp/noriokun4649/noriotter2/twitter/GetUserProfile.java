package jp.noriokun4649.noriotter2.twitter;

import android.app.Activity;

import twitter4j.AsyncTwitter;

public class GetUserProfile {

    private final AsyncTwitter asyncTwitter;

    public GetUserProfile(final Activity contexts, final AsyncTwitter asyncTwitterd, final StatusCallBack callBack) {
        this.asyncTwitter = asyncTwitterd;
        asyncTwitterd.addListener(new TimeLineTwetterAdapter(callBack, asyncTwitterd));

    }

    public void getUserProfile(final String id) {
        asyncTwitter.showUser(id);
    }

}
