package jp.noriokun4649.noriotter2.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import jp.noriokun4649.noriotter2.R;

public class Media extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.media_fr_layout, null);
        int index = getArguments().getInt("image_index");
        String url = getArguments().getString("image_url");
        ImageView imageView2 = view.findViewById(R.id.imageView2);
        VideoView videoView2 = view.findViewById(R.id.videoView2);
        ProgressBar progressBar = view.findViewById(R.id.progressBar2);
        if (url.contains(".mp4") || url.contains(".m3u8")) {
            imageView2.setVisibility(View.GONE);
            videoView2.setVideoURI(Uri.parse(url));
            videoView2.setOnPreparedListener(mp -> {
                videoView2.start();
                progressBar.setVisibility(View.GONE);
            });
            videoView2.setMediaController(new MediaController(getContext()));
        } else {
            progressBar.setVisibility(View.GONE);
            videoView2.setVisibility(View.GONE);
            imageView2.setTransitionName("image" + index);
            Glide.with(this).setDefaultRequestOptions(new RequestOptions().dontTransform()).load(url).into(imageView2);
        }
        return view;

    }
}
