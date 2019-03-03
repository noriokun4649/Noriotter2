package jp.noriokun4649.noriotter2.list;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.constraintlayout.widget.ConstraintLayout;
import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.twitter.TwitterConnect;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by noriokun4649 on 2017/07/01.
 */

public class TweetListItemAdapter extends ArrayAdapter<TweetList> {
    private final LayoutInflater inflater;
    private List<TweetList> arrayList;
    private Context context;
    private TwitterConnect twitterIn;

    public TweetListItemAdapter(final Context context, final List<TweetList> objects) {
        super(context, 0, objects);
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        arrayList = objects;
        this.context = context;
    }

    @Override
    public View getView(final int position, final View convertView, final ViewGroup parent) {
        final View view = inflater.inflate(R.layout.tl_list_layout, null, false);
        View.OnClickListener clickListener = (View v) -> ((ListView) parent).performItemClick(v, position, v.getId());
        TextView textView6 = view.findViewById(R.id.textView6);
        TextView textView = view.findViewById(R.id.textView);
        TextView textView7 = view.findViewById(R.id.textView7);
        TextView textView8 = view.findViewById(R.id.textView8);
        TextView textView10 = view.findViewById(R.id.textView10);
        TextView textView14 = view.findViewById(R.id.textView14);
        TextView textView32 = view.findViewById(R.id.textView32);
        TextView textView33 = view.findViewById(R.id.textView33);
        TextView textView34 = view.findViewById(R.id.textView34);
        TextView textView35 = view.findViewById(R.id.textView35);
        TextView textView36 = view.findViewById(R.id.textView36);
        SimpleDraweeView image17 = view.findViewById(R.id.image1);
        SimpleDraweeView image18 = view.findViewById(R.id.image2);
        SimpleDraweeView image19 = view.findViewById(R.id.image3);
        SimpleDraweeView image20 = view.findViewById(R.id.image4);
        ImageView isLock = view.findViewById(R.id.imageView13);
        ImageView isOfficial = view.findViewById(R.id.imageView14);
        //VideoView video = view.findViewById(R.id.videoView);
        ConstraintLayout constraintLayout = view.findViewById(R.id.image_layout);
        final SimpleDraweeView imageView8 = view.findViewById(R.id.imageView8);
        final TweetList useredata = arrayList.get(getCount() - 1 - position);
        if (useredata.getLocation().equals("")) {
            textView14.setVisibility(View.GONE);
        } else {
            textView14.setVisibility(View.VISIBLE);
        }
        if (useredata.getUrls() != null) {
            String[] urls = useredata.getUrls();
            switch (urls.length) {
                case 4:
                    Log.d("URL", urls[3]);
                case 3:
                    Log.d("URL", urls[2]);
                case 2:
                    Log.d("URL", urls[1]);
                case 1:
                    Log.d("URL", urls[0]);
                default:
            }
        }
        if (useredata.getMedias() != null) {
            mediaTweet(clickListener, useredata.getMedias(), constraintLayout, useredata.getMovieThumbnail(), /* video,*/ view, image17, image18, image19, image20);
        }
        if (useredata.isRetwet()) {
            textView32.setVisibility(View.VISIBLE);
            textView32.setText("{cmd-twitter-retweet}  " + useredata.getRetwetOwner() + "さんがリツイートしました");
            textView32.setOnClickListener(clickListener);
        }
        if (useredata.isQuit()) {
            LinearLayout quitLine = view.findViewById(R.id.quit_line);
            quitLine.setVisibility(View.VISIBLE);
            quitLine.setOnClickListener(clickListener);
            TextView quitText = view.findViewById(R.id.quit_text);
            TextView quitName = view.findViewById(R.id.quit_name);
            TextView quitScreanname = view.findViewById(R.id.quit_screanname);
            quitText.setText(useredata.getQuitText());
            quitName.setText(useredata.getQuitName());
            quitScreanname.setText("@" + useredata.getQuitScreenName());
            if (useredata.getQuitMedias() != null) {
                ConstraintLayout quitImageLayout = view.findViewById(R.id.quit_image_layout);
                quitImageLayout.setVisibility(View.VISIBLE);
                //VideoView quitVideo = view.findViewById(R.id.quit_videoView);
                SimpleDraweeView quitImage1 = view.findViewById(R.id.quit_image1);
                SimpleDraweeView quitImage2 = view.findViewById(R.id.quit_image2);
                SimpleDraweeView quitImage3 = view.findViewById(R.id.quit_image3);
                SimpleDraweeView quitImage4 = view.findViewById(R.id.quit_image4);
                mediaTweet(clickListener, useredata.getQuitMedias(), quitImageLayout, useredata.getQuitMovieThumbnail(), /*quitVideo,*/ view, quitImage1, quitImage2, quitImage3, quitImage4);
            }
        }
        textView33.setText("{cmd-message}");
        textView33.setOnClickListener(clickListener);
        textView34.setText("{cmd-twitter-retweet}" + useredata.getRtcount());
        textView34.setOnClickListener(clickListener);
        textView34.setTextColor(useredata.isMeRt() ? Color.GREEN : Color.BLACK);
        textView35.setText("{gmd-favorite}" + useredata.getFavocount());
        textView35.setOnClickListener(clickListener);
        textView35.setTextColor(useredata.isMeFav() ? Color.RED : Color.BLACK);
        textView36.setText("{gmd-share}");
        textView36.setOnClickListener(clickListener);
        textView.setText("@" + useredata.getScreanname());
        textView6.setText(useredata.getName());
        textView7.setText(useredata.getTwiite());
        textView8.setText(Html.fromHtml(useredata.getSource()));
        textView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String in = useredata.getSource();
                final Pattern urlPattern = Pattern.compile("(http://|https://){1}[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+", Pattern.CASE_INSENSITIVE);
                final Matcher matcher = urlPattern.matcher(useredata.getSource());
                while (matcher.find()) {
                    System.out.println(matcher.group());
                    Uri uri = Uri.parse(matcher.group());
                    Intent i = new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(i);
                }
            }
        });
        if (useredata.isLock()) {
            isLock.setVisibility(View.VISIBLE);
        } else {
            isLock.setVisibility(View.INVISIBLE);
        }
        if (useredata.isOfficial()) {
            isOfficial.setVisibility(View.VISIBLE);
        } else {
            isOfficial.setVisibility(View.INVISIBLE);
        }
        textView10.setText(useredata.getTimestamp());
        textView14.setText(useredata.getLocation());
        imageView8.setOnClickListener(clickListener);
        imageView8.setImageURI(useredata.getImageURL());
        //MyGlideApp.with(view).load(uri).diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop().into(imageView8);

        return view;
    }

    private void mediaTweet(final View.OnClickListener clickListener, final String[] media,
                            final ConstraintLayout constraintLayout,
                            final String movieThumbnail/*final VideoView video*/, final View mainView, final SimpleDraweeView image1,
                            final SimpleDraweeView image2, final SimpleDraweeView image3, final SimpleDraweeView image4) {
        if (media[0].contains(".mp4") || media[0].contains(".m3u8")) {
            IconicsDrawable gmdPlay = new IconicsDrawable(getContext(), GoogleMaterial.Icon.gmd_play_circle_filled).paddingDp(40);
            constraintLayout.setVisibility(View.VISIBLE);
            image1.setVisibility(View.VISIBLE);
            image1.setOnClickListener(clickListener);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                image1.setForeground(gmdPlay);
            }
            image1.setImageURI(movieThumbnail);
            //Glide.with(mainView).load(movieThumbnail).diskCacheStrategy(DiskCacheStrategy.ALL).into(image1);
            //video.setVisibility(View.VISIBLE);
            //video.setVideoURI(Uri.parse(media[0]));
            //video.setOnClickListener(clickListener);
        } else {
            constraintLayout.setVisibility(View.VISIBLE);
            //video.setVisibility(View.GONE);
            switch (media.length) {
                case 4:
                    image4.setVisibility(View.VISIBLE);
                    image4.setOnClickListener(clickListener);
                    image4.setImageURI(media[3]);
                    //Glide.with(mainView).load(media[3]).diskCacheStrategy(DiskCacheStrategy.ALL).into(image4);
                case 3:
                    image3.setVisibility(View.VISIBLE);
                    image3.setOnClickListener(clickListener);
                    image3.setImageURI(media[2]);
                    //Glide.with(mainView).load(media[2]).diskCacheStrategy(DiskCacheStrategy.ALL).into(image3);
                case 2:
                    image2.setVisibility(View.VISIBLE);
                    image2.setOnClickListener(clickListener);
                    image2.setImageURI(media[1]);
                    //Glide.with(mainView).load(media[1]).diskCacheStrategy(DiskCacheStrategy.ALL).into(image2);
                case 1:
                    image1.setVisibility(View.VISIBLE);
                    image1.setOnClickListener(clickListener);
                    image1.setImageURI(media[0]);
                    //Glide.with(mainView).load(media[0]).diskCacheStrategy(DiskCacheStrategy.ALL).into(image1);
                    break;
                default:
                    constraintLayout.setVisibility(View.GONE);
            }
        }
    }

}
