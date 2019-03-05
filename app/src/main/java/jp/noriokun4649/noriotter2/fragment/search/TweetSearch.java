package jp.noriokun4649.noriotter2.fragment.search;

import jp.noriokun4649.noriotter2.fragment.TimeLineBase;
import jp.noriokun4649.noriotter2.twitter.GetSearch;
import twitter4j.Query;

public class TweetSearch extends TimeLineBase {
    @Override
    public void getFastLoad() {
        String tag = getArguments().getString("tag");
        if (tag != null) {
            getSearch(tag);
        }
        getTweetListItemAdapter().clear();
        getTweetListItemAdapter().notifyDataSetChanged();
        getSwipeRefreshLayout().setRefreshing(false);
    }

    @Override
    public void getFutureLoad(final long index) {
        getSwipeRefreshLayout().setRefreshing(false);
    }

    @Override
    public void getOldLoad(final long index) {
        getSwipeRefreshLayout().setRefreshing(false);
    }

    public void getSearch(final String s) {
        Query query = new Query();
        query.setCount(20);
        query.query(s);
        GetSearch getSearch = new GetSearch(getActivity(), getAsyncTwitter(), getTweetListItemAdapter(), this);
        getSearch.getSearch(query);
    }
}
