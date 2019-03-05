package jp.noriokun4649.noriotter2.fragment;

public class Notice extends TimeLineBase {
    @Override
    public void getFastLoad() {
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
}
