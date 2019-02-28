/*
 * Copyright (c) 2018 noriokun4649.
 */

package jp.noriokun4649.noriotter2.dialogfragment;


import androidx.annotation.Nullable;

/**
 * フラグメントダイアログを通して行われたあらゆる操作を仲介します.
 */
public interface DialogsListener {
    /**
     * クリックされた際のアクション.
     *
     * @param which クリックされたアイテムのポジション
     * @param tag   ダイアログのタグです
     */
    void onItemClick(int which, String tag);

    /**
     * クリックしたさいに呼ばれるアクション.
     *
     * @param dialogId   ダイアログのID
     * @param position   ポジション
     * @param returnMemo memoに関するダイアログの場合は編集後のメモ内容
     * @param tag        ダイアログのタグです
     * @param items      アイテムす
     */
    void onOKClick(int dialogId, int position, @Nullable String returnMemo, String tag, String[] items);


}
