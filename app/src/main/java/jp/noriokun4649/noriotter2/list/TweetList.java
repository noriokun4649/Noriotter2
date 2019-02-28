package jp.noriokun4649.noriotter2.list;

/**
 * Created by noriokun4649 on 2017/07/01.
 */

public class TweetList {
    private boolean isLock;
    private boolean isOfficial;
    private String name;
    private String screanname;
    private String twiite;
    private String location;
    private String timestamp;
    private long tweetid;
    private String source;
    private String imageURL;
    private String[] urls;
    private String[] medias;
    private boolean isRetwet;
    private String retwetOwner;
    private int rtcount;
    private int favocount;
    private boolean isQuit;
    private String quitText;
    private String quitName;
    private String quitScreenName;
    private String[] quitMedias;
    private String[] quitUrls;
    private boolean meRt;
    private boolean meFav;
    private String movieThumbnail;
    private String quitMovieThumbnail;

    /**
     * Gets quitMovieThumbnail .
     *
     * @return value of quitMovieThumbnail
     */
    public String getQuitMovieThumbnail() {
        return quitMovieThumbnail;
    }

    /**
     * Sets quitMovieThumbnail .
     *
     * @param quitMovieThumbnail set in quitMovieThumbnail
     */
    public void setQuitMovieThumbnail(final String quitMovieThumbnail) {
        this.quitMovieThumbnail = quitMovieThumbnail;
    }

    /**
     * Gets movieThumbnail .
     *
     * @return value of movieThumbnail
     */
    public String getMovieThumbnail() {
        return movieThumbnail;
    }

    /**
     * Sets movieThumbnail .
     *
     * @param movieThumbnail set in movieThumbnail
     */
    public void setMovieThumbnail(final String movieThumbnail) {
        this.movieThumbnail = movieThumbnail;
    }

    /**
     * Gets isLock .
     *
     * @return value of isLock
     */
    public boolean isLock() {
        return isLock;
    }

    /**
     * Sets isLock .
     *
     * @param lock set in lock
     */
    public void setLock(final boolean lock) {
        isLock = lock;
    }

    /**
     * Gets isOfficial .
     *
     * @return value of isOfficial
     */
    public boolean isOfficial() {
        return isOfficial;
    }

    /**
     * Sets isOfficial .
     *
     * @param official set in official
     */
    public void setOfficial(final boolean official) {
        isOfficial = official;
    }

    /**
     * Gets name .
     *
     * @return value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name .
     *
     * @param name set in name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets screanname .
     *
     * @return value of screanname
     */
    public String getScreanname() {
        return screanname;
    }

    /**
     * Sets screanname .
     *
     * @param screanname set in screanname
     */
    public void setScreanname(final String screanname) {
        this.screanname = screanname;
    }

    /**
     * Gets twiite .
     *
     * @return value of twiite
     */
    public String getTwiite() {
        return twiite;
    }

    /**
     * Sets twiite .
     *
     * @param twiite set in twiite
     */
    public void setTwiite(final String twiite) {
        this.twiite = twiite;
    }

    /**
     * Gets location .
     *
     * @return value of location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets location .
     *
     * @param location set in location
     */
    public void setLocation(final String location) {
        this.location = location;
    }

    /**
     * Gets timestamp .
     *
     * @return value of timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Sets timestamp .
     *
     * @param timestamp set in timestamp
     */
    public void setTimestamp(final String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Gets tweetid .
     *
     * @return value of tweetid
     */
    public long getTweetid() {
        return tweetid;
    }

    /**
     * Sets tweetid .
     *
     * @param tweetid set in tweetid
     */
    public void setTweetid(final long tweetid) {
        this.tweetid = tweetid;
    }

    /**
     * Gets source .
     *
     * @return value of source
     */
    public String getSource() {
        return source;
    }

    /**
     * Sets source .
     *
     * @param source set in source
     */
    public void setSource(final String source) {
        this.source = source;
    }

    /**
     * Gets imageURL .
     *
     * @return value of imageURL
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * Sets imageURL .
     *
     * @param imageURL set in imageURL
     */
    public void setImageURL(final String imageURL) {
        this.imageURL = imageURL;
    }

    /**
     * Gets urls .
     *
     * @return value of urls
     */
    public String[] getUrls() {
        return urls;
    }

    /**
     * Sets urls .
     *
     * @param urls set in urls
     */
    public void setUrls(final String[] urls) {
        this.urls = urls;
    }

    /**
     * Gets medias .
     *
     * @return value of medias
     */
    public String[] getMedias() {
        return medias;
    }

    /**
     * Sets medias .
     *
     * @param medias set in medias
     */
    public void setMedias(final String[] medias) {
        this.medias = medias;
    }

    /**
     * Gets isRetwet .
     *
     * @return value of isRetwet
     */
    public boolean isRetwet() {
        return isRetwet;
    }

    /**
     * Sets isRetwet .
     *
     * @param retwet set in retwet
     */
    public void setRetwet(final boolean retwet) {
        isRetwet = retwet;
    }

    /**
     * Gets retwetOwner .
     *
     * @return value of retwetOwner
     */
    public String getRetwetOwner() {
        return retwetOwner;
    }

    /**
     * Sets retwetOwner .
     *
     * @param retwetOwner set in retwetOwner
     */
    public void setRetwetOwner(final String retwetOwner) {
        this.retwetOwner = retwetOwner;
    }

    /**
     * Gets rtcount .
     *
     * @return value of rtcount
     */
    public int getRtcount() {
        return rtcount;
    }

    /**
     * Sets rtcount .
     *
     * @param rtcount set in rtcount
     */
    public void setRtcount(final int rtcount) {
        this.rtcount = rtcount;
    }

    /**
     * Gets favocount .
     *
     * @return value of favocount
     */
    public int getFavocount() {
        return favocount;
    }

    /**
     * Sets favocount .
     *
     * @param favocount set in favocount
     */
    public void setFavocount(final int favocount) {
        this.favocount = favocount;
    }

    /**
     * Gets isQuit .
     *
     * @return value of isQuit
     */
    public boolean isQuit() {
        return isQuit;
    }

    /**
     * Sets isQuit .
     *
     * @param quit set in quit
     */
    public void setQuit(final boolean quit) {
        isQuit = quit;
    }

    /**
     * Gets quitText .
     *
     * @return value of quitText
     */
    public String getQuitText() {
        return quitText;
    }

    /**
     * Sets quitText .
     *
     * @param quitText set in quitText
     */
    public void setQuitText(final String quitText) {
        this.quitText = quitText;
    }

    /**
     * Gets quitName .
     *
     * @return value of quitName
     */
    public String getQuitName() {
        return quitName;
    }

    /**
     * Sets quitName .
     *
     * @param quitName set in quitName
     */
    public void setQuitName(final String quitName) {
        this.quitName = quitName;
    }

    /**
     * Gets quitScreenName .
     *
     * @return value of quitScreenName
     */
    public String getQuitScreenName() {
        return quitScreenName;
    }

    /**
     * Sets quitScreenName .
     *
     * @param quitScreenName set in quitScreenName
     */
    public void setQuitScreenName(final String quitScreenName) {
        this.quitScreenName = quitScreenName;
    }

    /**
     * Gets quitMedias .
     *
     * @return value of quitMedias
     */
    public String[] getQuitMedias() {
        return quitMedias;
    }

    /**
     * Sets quitMedias .
     *
     * @param quitMedias set in quitMedias
     */
    public void setQuitMedias(final String[] quitMedias) {
        this.quitMedias = quitMedias;
    }

    /**
     * Gets quitUrls .
     *
     * @return value of quitUrls
     */
    public String[] getQuitUrls() {
        return quitUrls;
    }

    /**
     * Sets quitUrls .
     *
     * @param quitUrls set in quitUrls
     */
    public void setQuitUrls(final String[] quitUrls) {
        this.quitUrls = quitUrls;
    }

    /**
     * Gets meRt .
     *
     * @return value of meRt
     */
    public boolean isMeRt() {
        return meRt;
    }

    /**
     * Sets meRt .
     *
     * @param meRt set in meRt
     */
    public void setMeRt(final boolean meRt) {
        this.meRt = meRt;
    }

    /**
     * Gets meFav .
     *
     * @return value of meFav
     */
    public boolean isMeFav() {
        return meFav;
    }

    /**
     * Sets meFav .
     *
     * @param meFav set in meFav
     */
    public void setMeFav(final boolean meFav) {
        this.meFav = meFav;
    }
}
