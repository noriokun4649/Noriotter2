package jp.noriokun4649.noriotter2.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import jp.noriokun4649.noriotter2.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class UserListItemAdapter extends ArrayAdapter<UserList> {
    private final LayoutInflater inflater;
    private List<UserList> arrayList;
    private Context context;

    public UserListItemAdapter(final Context context, final List<UserList> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        arrayList = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.user_list_item, parent, false);
        }
        UserList userList = getItem(position);
        SimpleDraweeView simpleDraweeView = view.findViewById(R.id.imageIcon);
        ImageView imageLock = view.findViewById(R.id.imageLock);
        ImageView imageOffical = view.findViewById(R.id.imageOfficical);
        TextView textInfo = view.findViewById(R.id.text_info);
        TextView textName = view.findViewById(R.id.text_name);
        TextView textScreenName = view.findViewById(R.id.text_screenname);
        simpleDraweeView.setImageURI(userList.getUserIconUrl());
        textInfo.setText(userList.getUserInfo());
        textName.setText(userList.getUserName());
        textScreenName.setText(userList.getUserScreenName());
        if (!userList.isUserLock()) {
            imageLock.setVisibility(View.GONE);
        }
        if (!userList.isUserOffical()) {
            imageOffical.setVisibility(View.GONE);
        }

        return view;
    }
}
