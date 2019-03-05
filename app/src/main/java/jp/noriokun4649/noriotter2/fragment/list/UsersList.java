package jp.noriokun4649.noriotter2.fragment.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.activity.UserPageActivity;
import jp.noriokun4649.noriotter2.list.UserList;
import jp.noriokun4649.noriotter2.list.UserListItemAdapter;
import jp.noriokun4649.noriotter2.twitter.GetList;
import jp.noriokun4649.noriotter2.twitter.TwitterConnect;
import twitter4j.AsyncTwitter;

public class UsersList extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.user_list_layout, null);
        long listId = getArguments().getLong("list_id");
        int count = getArguments().getInt("list_count");
        LinearLayout linearLayout = view.findViewById(R.id.progress);
        TextView textView = view.findViewById(R.id.textView4);
        textView.setText(R.string.getting_list_user_now);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        final ListView listView = view.findViewById(R.id.follow_import_list);
        final ArrayList<UserList> circles = new ArrayList<>();
        UserListItemAdapter adapter = new UserListItemAdapter(getContext(), circles);
        TwitterConnect twitterConnect = new TwitterConnect(getContext());
        twitterConnect.login();
        final AsyncTwitter asyncTwitter = twitterConnect.getmTwitter();
        GetList getList = new GetList(getActivity(), asyncTwitter, adapter, listId, count, view);
        getList.getList();
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
}
