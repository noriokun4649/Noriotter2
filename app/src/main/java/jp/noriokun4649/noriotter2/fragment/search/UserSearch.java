package jp.noriokun4649.noriotter2.fragment.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.activity.UserPageActivity;
import jp.noriokun4649.noriotter2.list.UserList;
import jp.noriokun4649.noriotter2.list.UserListItemAdapter;
import jp.noriokun4649.noriotter2.twitter.GetUserSearch;
import jp.noriokun4649.noriotter2.twitter.TwitterConnect;
import twitter4j.AsyncTwitter;

public class UserSearch extends Fragment {

    private AsyncTwitter asyncTwitter;
    private UserListItemAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.user_list_layout, null);
        LinearLayout linearLayout = view.findViewById(R.id.progress);
        TextView textView = view.findViewById(R.id.textView4);
        textView.setText(R.string.getting_user_now);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        final ListView listView = view.findViewById(R.id.follow_import_list);
        final ArrayList<UserList> circles = new ArrayList<>();
        adapter = new UserListItemAdapter(getContext(), circles);
        TwitterConnect twitterConnect = new TwitterConnect(getContext());
        twitterConnect.login();
        asyncTwitter = twitterConnect.getmTwitter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, viewd, position, ids) -> {
            UserList list = circles.get(position);
            Intent intent = new Intent(getContext(), UserPageActivity.class);
            intent.putExtra("userid", list.getUserScreenName());
            startActivity(intent);
        });
        listView.setEmptyView(linearLayout);
        return view;
    }

    public void getSearch(final String s) {
        adapter.clear();
        adapter.notifyDataSetChanged();
        GetUserSearch getSearch = new GetUserSearch(getActivity(), asyncTwitter, adapter);
        getSearch.getUserSearch(s, 0);

    }
}
