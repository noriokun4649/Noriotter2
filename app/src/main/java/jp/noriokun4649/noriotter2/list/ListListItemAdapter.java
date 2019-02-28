/*
 * Copyright (c) 2018 noriokun4649.
 */

package jp.noriokun4649.noriotter2.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.glide.MyGlideApp;

/**
 * リストのリストアイテムのアダプタです.
 * これでは、主にリストからインポート時のリストViewで使用されている
 */
public class ListListItemAdapter extends ArrayAdapter<ListList> {
    /**
     * レイアウト.
     */
    private final LayoutInflater inflater;

    /**
     * コンストラクタ.
     *
     * @param context コンテキスト
     * @param objects リストViewで使用する情報のひな型
     */
    public ListListItemAdapter(@NonNull final Context context, @NonNull final List<ListList> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressLint("StringFormatInvalid")
    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.list_list_item, parent, false);
        }
        TextView textListName = view.findViewById(R.id.text_listname);
        TextView textMember = view.findViewById(R.id.text_menber);
        TextView textOwner = view.findViewById(R.id.text_owner);
        TextView textDescription = view.findViewById(R.id.text_description);
        ImageView imageIcon = view.findViewById(R.id.image_icon);
        ImageView imagePrivate = view.findViewById(R.id.image_private);
        ListList listList = getItem(position);
        textListName.setText(listList.getName());
        textMember.setText(getContext().getString(R.string.menber, listList.getMemberCount()));
        textOwner.setText(getContext().getString(R.string.owner, listList.getOwner()));
        textDescription.setText(listList.getDescription());
        //image_private.setImageDrawable(new IconicsDrawable(view.getContext(), CommunityMaterial.Icon.cmd_lock));
        imagePrivate.setVisibility(listList.isPublic() ? View.GONE : View.VISIBLE);
        MyGlideApp.with(view).load(listList.getImageUrl()).circleCrop().into(imageIcon);
        return view;

    }
}
