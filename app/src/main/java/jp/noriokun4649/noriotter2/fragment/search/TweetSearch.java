package jp.noriokun4649.noriotter2.fragment.search;

import jp.noriokun4649.noriotter2.fragment.TimeLineBase;
import jp.noriokun4649.noriotter2.twitter.GetSearch;
import twitter4j.Query;

public class TweetSearch extends TimeLineBase {

    private GetSearch getSearch;
    private Query query;

    @Override
    public void getFastLoad() {
        String tag = getArguments().getString("tag");
        getTweetListItemAdapter().clear();
        getTweetListItemAdapter().notifyDataSetChanged();
        getSwipeRefreshLayout().setRefreshing(false);
        getSwipeRefreshLayout().setEnabled(false);
        getSearch = new GetSearch(getActivity(), getAsyncTwitter(), getTweetListItemAdapter(), this);
        query = new Query();
        if (tag != null) {
            getSearch(tag);
        }
    }

    @Override
    public void getFutureLoad(final long index) {
        getSwipeRefreshLayout().setRefreshing(false);
    }

    @Override
    public void getOldLoad(final long index) {
        query.setMaxId(index);
        getSearch.getSearch(query, true);
        getSwipeRefreshLayout().setRefreshing(false);
    }

    public void getSearch(final String s) {
        query.setCount(20);
        query.query(s);
        getSearch.getSearch(query, false);
    }
}
