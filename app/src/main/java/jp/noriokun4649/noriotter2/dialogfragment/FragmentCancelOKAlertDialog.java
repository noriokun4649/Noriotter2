/*
 * Copyright (c) 2018 noriokun4649.
 */

package jp.noriokun4649.noriotter2.dialogfragment;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import jp.noriokun4649.noriotter2.R;

/**
 * フラグメントダイアログです.
 * このフラグメントダイアログは、OK、キャンセルといったダイアログを表示する際に使用します。
 * 必要になるbundleパラメータは、
 * タイトル
 * メッセージ内容
 * ボタンの表示名
 * の3項目です。
 * <p>
 * これらのパラメータはすべてStringのリソースIDでしていされ、タイトルに限っては0の際には非表示になります
 */
public class FragmentCancelOKAlertDialog extends DialogFragment {
    /**
     * ダイアログからの操作を受け取るインターフェースです.
     */
    private DialogsListener mListener;
    /**
     * タイトルのリソースIDです.
     */
    private int title;
    /**
     * メッセージ内容です.
     */
    private String massage;
    /**
     * ボタンの表示名のリソースIDです.
     */
    private int buttonName;


    @Override
    public void onAttach(final @NotNull Context context) {
        super.onAttach(context);
        if (context instanceof DialogsListener) {
            mListener = (DialogsListener) context;
            Bundle bundle = getArguments();
            title = bundle.getInt("title");
            massage = bundle.getString("massage");
            buttonName = bundle.getInt("buttonName");
        } else {
            throw new RuntimeException("Don't cast to DialogsListener. Activity implements DialogsListener??");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        androidx.appcompat.app.AlertDialog.Builder a = new AlertDialog.Builder(getContext());
        a.setMessage(massage);
        if (title != 0) {
            a.setTitle(title);
        }
        a.setPositiveButton(buttonName, (dialog, which) -> mListener.onItemClick(which, getTag()));
        a.setNegativeButton(R.string.cancel, (dialog, which) -> mListener.onItemClick(which, getTag()));
        return a.create();
    }
}
