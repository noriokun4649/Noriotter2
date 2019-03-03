package jp.noriokun4649.noriotter2.twitter;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.noriokun4649.noriotter2.list.TweetList;
import jp.noriokun4649.noriotter2.list.TweetListItemAdapter;
import twitter4j.AsyncTwitter;
import twitter4j.GeoLocation;
import twitter4j.MediaEntity;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.URLEntity;

public class GetStatus {

    private ICallBack iCallBack;
    private long[] follow;
    private boolean addMode = false;

    public GetStatus(final long[] follow) {
        this.follow = follow;
        //getStatus(status, asyncTwitterd, adapter, context, mode);
    }

    public GetStatus(final ResponseList<Status> statuses, final AsyncTwitter asyncTwitterd,
                     final TweetListItemAdapter adapter, final Activity context, final ICallBack callBack,
                     final boolean addMode) {
        this.addMode = addMode;
        int i;
        if (!addMode) {
            Collections.reverse(statuses);
            i=0;
        }else {
            i
                    =1;
        }
        for (; i < statuses.size(); i++) {
            getStatus(statuses.get(i), asyncTwitterd, adapter, context, true);
        }
        iCallBack = callBack;
        finish();
    }

    private void finish() {
        if (iCallBack != null) {
            iCallBack.callback();
        }
    }

    public void getStatus(final Status status, final AsyncTwitter asyncTwitterd, final TweetListItemAdapter adapter,
                          final Context context, final boolean mode) {
        Double lat = null;
        Double lng = null;
        String[] urls = null;
        String[] medias = null;
        final boolean isRetwet = status.isRetweet();
        String retwetOwner = status.getUser().getScreenName();
        long retwetOwnerId = status.getUser().getId();
        Status statusMain;
        if (isRetwet) {
            statusMain = status.getRetweetedStatus();
        } else {
            statusMain = status;
        }

        // . 位置情報が含まれていれば取得する
        final StringBuilder result = new StringBuilder();
        GeoLocation location = statusMain.getGeoLocation();
        if (location != null) {
            double dlat = location.getLatitude();
            double dlng = location.getLongitude();
            lat = dlat;
            lng = dlng;
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(dlat, dlng, 1);
            } catch (IOException e) {
                e.getMessage();
            }
            for (Address address : addresses) {
                int idx = address.getMaxAddressLineIndex();
                // 1番目のレコードは国名のため省略
                for (int i = 1; i <= idx; i++) {
                    result.append(address.getAddressLine(i));
                }
            }
        }

        final long tweetid = statusMain.getId(); // . ツイートID
        String text = statusMain.getText(); // . ツイート本文
        long userid = statusMain.getUser().getId(); // . ユーザーID
        final String screanname = statusMain.getUser().getScreenName(); // . ユーザー表示名
        final Date created = statusMain.getCreatedAt(); // . ツイート日時
        final String name = statusMain.getUser().getName();
        final String source = statusMain.getSource();
        final String imageURL = statusMain.getUser().getOriginalProfileImageURLHttps();
        final DateFormat dateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final boolean isLock = statusMain.getUser().isProtected();
        final boolean isOfficial = statusMain.getUser().isVerified();
        final int rtcount = statusMain.getRetweetCount();
        final int favcount = statusMain.getFavoriteCount();
        final boolean meRt = statusMain.isRetweeted();
        final boolean meFav = statusMain.isFavorited();
        String quitText = "";
        String quitName = "";
        String quitScreanName = "";
        String[] quitMedias = null;
        String[] quitUrls = null;
        boolean isQuit = false;
        String movieThumbnail = null;
        String quitMovieThumbnail = null;

        if (statusMain.getQuotedStatus() != null) {
            isQuit = true;
            text = text.replace(statusMain.getQuotedStatusPermalink().getURL(), "");
            quitText = statusMain.getQuotedStatus().getText();
            quitName = statusMain.getQuotedStatus().getUser().getName();
            quitScreanName = statusMain.getQuotedStatus().getUser().getScreenName();

            MediaEntity[] quitMediEntiy = statusMain.getQuotedStatus().getMediaEntities();
            if (quitMediEntiy != null && quitMediEntiy.length > 0) {
                Object[] objects = getMedias(quitMediEntiy, quitText);
                quitMedias = (String[]) objects[0];
                quitText = (String) objects[1];
                quitMovieThumbnail = (String) objects[2];
            }

            URLEntity[] quitUrlEntities = statusMain.getQuotedStatus().getURLEntities();
            if (quitUrlEntities != null && quitUrlEntities.length > 0) {
                Object[] objects = getURLs(quitUrlEntities, quitText);
                quitUrls = (String[]) objects[0];
                quitText = (String) objects[1];
            }
        }

        // . ツイート本文に画像／動画URLが含まれていれば取り出す
        MediaEntity[] mediaEntitiesd = statusMain.getMediaEntities();
        if (mediaEntitiesd != null && mediaEntitiesd.length > 0) {
            Object[] objects = getMedias(mediaEntitiesd, text);
            medias = (String[]) objects[0];
            text = (String) objects[1];
            movieThumbnail = (String) objects[2];
        }

        // . RTツイート本文にリンクURLが含まれていれば取り出す
        URLEntity[] urlEntities = statusMain.getURLEntities();
        if (urlEntities != null && urlEntities.length > 0) {
            Object[] objects = getURLs(urlEntities, text);
            urls = (String[]) objects[0];
            text = (String) objects[1];
        }
        boolean isfollow = isRetwet ? ArrayUtils.contains(follow, retwetOwnerId) : ArrayUtils.contains(follow, userid);

        if ((isfollow && (status.getInReplyToUserId() == -1L || ArrayUtils.contains(follow, status.getInReplyToUserId()))) || mode) {

            // . 取り出した情報を表示する（以下では id, username, text のみ）
            TweetList useredata = new TweetList();
            useredata.setLock(isLock);
            useredata.setOfficial(isOfficial);
            useredata.setTweetid(tweetid);
            useredata.setScreanname(screanname);
            useredata.setName(name);
            useredata.setLocation(result.toString());  // userLoc;
            useredata.setTwiite(text);
            useredata.setSource(source);
            useredata.setImageURL(imageURL);
            useredata.setTimestamp(dateTimeFormat.format(created));
            useredata.setMedias(medias);
            useredata.setUrls(urls);
            useredata.setRetwet(isRetwet);
            useredata.setRetwetOwner(retwetOwner);
            useredata.setFavocount(favcount);
            useredata.setRtcount(rtcount);
            useredata.setQuit(isQuit);
            useredata.setQuitText(quitText);
            useredata.setQuitName(quitName);
            useredata.setQuitScreenName(quitScreanName);
            useredata.setQuitMedias(quitMedias);
            useredata.setQuitUrls(quitUrls);
            useredata.setMeRt(meRt);
            useredata.setMeFav(meFav);
            useredata.setMovieThumbnail(movieThumbnail);
            useredata.setQuitMovieThumbnail(quitMovieThumbnail);
            if(!addMode) {
                adapter.add(useredata);
            }else {
                adapter.insert(useredata,0);
            }
            adapter.notifyDataSetChanged();
        }
    }

    private Object[] getURLs(final URLEntity[] uentitys, final String texts) {
        String text = texts;
        List list = new ArrayList();
        for (int i = 0; i < uentitys.length; i++) {
            URLEntity uentity = uentitys[i];
            String expandedURL = uentity.getExpandedURL();
            text = texts.replace(uentity.getURL(), expandedURL);
            list.add(expandedURL);
        }
        return new Object[]{(String[]) list.toArray(new String[0]), text};
    }

    private Object[] getMedias(final MediaEntity[] mediaEntities, final String texts) {
        String text = texts;
        String movieThumbnail = "";
        List list = new ArrayList();
        for (int i = 0; i < mediaEntities.length; i++) {
            MediaEntity mentity = mediaEntities[i];
            String expandedURL;
            if (mentity.getType().equals("photo")) {
                expandedURL = mentity.getMediaURLHttps();
            } else {
                expandedURL = mentity.getVideoVariants()[0].getUrl();
                movieThumbnail = mentity.getMediaURLHttps();
            }
            list.add(expandedURL);
            text = texts.replace(mentity.getURL(), "");
        }
        return new Object[]{(String[]) list.toArray(new String[0]), text, movieThumbnail};
    }
}
