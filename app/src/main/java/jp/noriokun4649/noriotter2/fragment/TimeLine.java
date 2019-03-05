package jp.noriokun4649.noriotter2.fragment;

import jp.noriokun4649.noriotter2.twitter.GetTimeLine;

public class TimeLine extends TimeLineBase {
    private GetTimeLine getTimeLine;

    @Override
    public void getFastLoad() {
        getTimeLine = new GetTimeLine(getActivity(), getAsyncTwitter(), getTweetListItemAdapter(), this);
        getTimeLine.getTimeLine();
    }

    @Override
    public void getFutureLoad(final long index) {
        getTimeLine.getFutureTimeLine(index);
    }

    @Override
    public void getOldLoad(final long index) {
        getTimeLine.getOldTimeLine(index);
    }
}
