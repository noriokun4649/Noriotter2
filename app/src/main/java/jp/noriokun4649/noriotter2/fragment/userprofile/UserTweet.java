package jp.noriokun4649.noriotter2.fragment.userprofile;

import jp.noriokun4649.noriotter2.fragment.TimeLineBase;
import jp.noriokun4649.noriotter2.twitter.GetUserTimeLine;

public class UserTweet extends TimeLineBase {
    private GetUserTimeLine getTimeLine;
    private long id;

    @Override
    public void getFastLoad() {
        id = getArguments().getLong("user_id");
        getTimeLine = new GetUserTimeLine(getActivity(), asyncTwitter, tweetListItemAdapter, this);
        getTimeLine.getUserTimeLine(id);
    }


    @Override
    public void getFutureLoad(final long index) {
        getTimeLine.getFutureUserTimeLine(id, index);
    }

    @Override
    public void getOldLoad(final long index) {
        getTimeLine.getOldUserTimeLine(id, index);
    }
}
