/*
 * Copyright (c) 2018 noriokun4649.
 */

package jp.noriokun4649.noriotter2.list;

/**
 * リストの一覧情報のひな型.
 */
public class ListList {
    /**
     * リストの名前です.
     */
    private String name;
    /**
     * メンバーの数です.
     */
    private int memberCount;
    /**
     * リストの内部IDです.
     */
    private Long id;
    /**
     * リストのオーナーです.
     */
    private String owner;
    /**
     * オーナーのアイコンURLです.
     */
    private String imageUrl;
    /**
     * リストの説明です.
     */
    private String description;
    /**
     * リストが公開か、非公開かです.
     */
    private boolean isPublic;

    /**
     * リストのリストのコンストラクタ.
     *
     * @param name        　リストの名前です
     * @param memberCount 　メンバーの数です
     * @param id          　リストの内部IDです
     * @param owner       　リストのオーナーです
     * @param imageUrl    　オーナーのアイコンURLです
     * @param description 　リストの説明です
     * @param isPublic    　リストが公開か、非公開かです
     */
    public ListList(final String name, final int memberCount, final Long id, final String owner, final String imageUrl, final String description, final boolean isPublic) {
        this.name = name;
        this.memberCount = memberCount;
        this.id = id;
        this.owner = owner;
        this.imageUrl = imageUrl;
        this.description = description;
        this.isPublic = isPublic;
    }

    /**
     * リストの名前を設定します.
     *
     * @return リストの名前
     */
    public String getName() {
        return name;
    }

    /**
     * リストの名前を取得します.
     *
     * @param name リストの名前
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * メンバーの数を取得します.
     *
     * @return メンバーの数
     */
    public int getMemberCount() {
        return memberCount;
    }

    /**
     * メンバーの数を設定します.
     *
     * @param memberCount メンバーの数
     */
    public void setMemberCount(final int memberCount) {
        this.memberCount = memberCount;
    }

    /**
     * リストのIDを取得します.
     *
     * @return リストのID
     */
    public Long getId() {
        return id;
    }

    /**
     * リストのIDを設定します.
     *
     * @param id リストのID
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * リストのオーナーを取得します.
     *
     * @return リストのオーナー
     */
    public String getOwner() {
        return owner;
    }

    /**
     * リストのオーナーを設定します.
     *
     * @param owner リストのオーナー
     */
    public void setOwner(final String owner) {
        this.owner = owner;
    }

    /**
     * オーナーのアイコンURL取得します.
     *
     * @return アイコンURL
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * オーナーのアイコンURLを設定します.
     *
     * @param imageUrl アイコンURL
     */
    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * リストの説明を取得します.
     *
     * @return リストの説明
     */
    public String getDescription() {
        return description;
    }

    /**
     * リストの説明を設定します.
     *
     * @param description リストの説明
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * 公開リストかどうかを取得します.
     *
     * @return 公開かどうか
     */
    public boolean isPublic() {
        return isPublic;
    }

    /**
     * 公開リストかどうかを設定します.
     *
     * @param aPublic 公開かどうか
     */
    public void setPublic(final boolean aPublic) {
        isPublic = aPublic;
    }
}
