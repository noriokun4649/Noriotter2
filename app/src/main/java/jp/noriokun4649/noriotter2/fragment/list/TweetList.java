package jp.noriokun4649.noriotter2.fragment.list;

import jp.noriokun4649.noriotter2.fragment.TimeLineBase;
import jp.noriokun4649.noriotter2.twitter.GetListTweetTL;

public class TweetList extends TimeLineBase {
    private GetListTweetTL getListTweetTL;
    private long listId;

    @Override
    public void getFastLoad() {
        listId = getArguments().getLong("list_id");
        getListTweetTL = new GetListTweetTL(getActivity(), asyncTwitter, tweetListItemAdapter, this);
        getListTweetTL.getListStatuses(listId);
    }

    @Override
    public void getFutureLoad(final long index) {
        getListTweetTL.getFutureListStatuses(listId, index);
    }

    @Override
    public void getOldLoad(final long index) {
        getListTweetTL.getOldListStatuses(listId, index);

    }
}
