package jp.noriokun4649.noriotter2.fragment.userprofile;

import jp.noriokun4649.noriotter2.fragment.TimeLineBase;
import jp.noriokun4649.noriotter2.twitter.GetUserLike;

public class UserLike extends TimeLineBase {
    private GetUserLike getFavorit;
    private long id;

    @Override
    public void getFastLoad() {
        id = getArguments().getLong("user_id");
        getFavorit = new GetUserLike(getActivity(), getAsyncTwitter(), getTweetListItemAdapter(), this);
        getFavorit.getFavorites(id);
    }


    @Override
    public void getFutureLoad(final long index) {
        getFavorit.getFutureFavorites(id, index);
    }

    @Override
    public void getOldLoad(final long index) {
        getFavorit.getOldFavorites(id, index);
    }
}