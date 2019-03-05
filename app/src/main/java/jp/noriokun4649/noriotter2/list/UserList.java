package jp.noriokun4649.noriotter2.list;

public class UserList {
    private long userId;
    private String userIconUrl;
    private String userName;
    private String userScreenName;
    private String userInfo;
    private boolean userLock;
    private boolean userOffical;

    /**
     * Gets userInfo .
     *
     * @return value of userInfo
     */
    public String getUserInfo() {
        return userInfo;
    }

    /**
     * Sets userInfo .
     *
     * @param userInfo set in userInfo
     */
    public void setUserInfo(final String userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * Gets userId .
     *
     * @return value of userId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Sets userId .
     *
     * @param userId set in userId
     */
    public void setUserId(final long userId) {
        this.userId = userId;
    }

    /**
     * Gets userIconUrl .
     *
     * @return value of userIconUrl
     */
    public String getUserIconUrl() {
        return userIconUrl;
    }

    /**
     * Sets userIconUrl .
     *
     * @param userIconUrl set in userIconUrl
     */
    public void setUserIconUrl(final String userIconUrl) {
        this.userIconUrl = userIconUrl;
    }

    /**
     * Gets userName .
     *
     * @return value of userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets userName .
     *
     * @param userName set in userName
     */
    public void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * Gets userScreenName .
     *
     * @return value of userScreenName
     */
    public String getUserScreenName() {
        return userScreenName;
    }

    /**
     * Sets userScreenName .
     *
     * @param userScreenName set in userScreenName
     */
    public void setUserScreenName(final String userScreenName) {
        this.userScreenName = userScreenName;
    }

    /**
     * Gets userLock .
     *
     * @return value of userLock
     */
    public boolean isUserLock() {
        return userLock;
    }

    /**
     * Sets userLock .
     *
     * @param userLock set in userLock
     */
    public void setUserLock(final boolean userLock) {
        this.userLock = userLock;
    }

    /**
     * Gets userOffical .
     *
     * @return value of userOffical
     */
    public boolean isUserOffical() {
        return userOffical;
    }

    /**
     * Sets userOffical .
     *
     * @param userOffical set in userOffical
     */
    public void setUserOffical(final boolean userOffical) {
        this.userOffical = userOffical;
    }
}
