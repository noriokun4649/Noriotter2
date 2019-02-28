package jp.noriokun4649.noriotter2.twitter;

import twitter4j.Status;
import twitter4j.User;

public interface StatusCallBack {
    void callbackStatus(Status status);

    void callbackFollow(long[] follow, User user);
}
