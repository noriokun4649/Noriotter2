/*
 * Copyright (c) 2018 noriokun4649.
 */

package jp.noriokun4649.noriotter2.glide;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * アイコンの処理.
 * URLでインターネットから自動で取得する、その際に自動で丸くリサイズされる
 */
@GlideModule(glideName = "MyGlideApp")
public class MyGlideModule extends AppGlideModule {
}
