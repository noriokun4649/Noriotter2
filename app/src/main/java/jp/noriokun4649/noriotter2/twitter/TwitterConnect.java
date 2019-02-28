/*
 * Copyright (c) 2018 noriokun4649.
 */

package jp.noriokun4649.noriotter2.twitter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.widget.Toast;

import com.kazakago.cryptore.CipherAlgorithm;
import com.kazakago.cryptore.Cryptore;
import com.kazakago.cryptore.DecryptResult;
import com.kazakago.cryptore.EncryptResult;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.NoSuchPaddingException;

import androidx.appcompat.app.AppCompatActivity;
import jp.noriokun4649.noriotter2.R;
import jp.noriokun4649.noriotter2.dialogfragment.FragmentCancelOKAlertDialog;
import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterListener;
import twitter4j.TwitterMethod;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by noriokun4649 on 2017/03/10.
 */

public class TwitterConnect implements Serializable {
    /**
     * api キー.
     */
    private final String apiKey;
    /**
     * api シークレット.
     */
    private final String apiSecret;
    /**
     * ハンドラー.
     * このインスタンスを通して、じゃないとアプリの画面等を操作できません.
     */
    private Handler mHandler = new Handler();
    /**
     * コンテキスト.
     * キャストしてサポートライブラリ使用可能なアクティビティ.
     */
    private Context context;
    /**
     * リクエストとーくん.
     */
    private RequestToken mReqToken;
    /**
     * Twitterのリスナー.
     */
    private final TwitterListener mListener = new TwitterAdapter() {
        @Override
        public void verifiedCredentials(final User user) {
            super.verifiedCredentials(user);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    sharedPreferences.edit().putString("scren_name", user.getScreenName()).apply();
                }
            });
        }

        @Override
        public void gotOAuthRequestToken(final RequestToken token) {
            mReqToken = token;
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mReqToken.getAuthenticationURL()));
            context.startActivity(intent);
            //Toast.makeText(appCompatActivity.getApplicationContext(), "token:" + token.getAuthorizationURL(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void gotOAuthAccessToken(final AccessToken token) {
            try {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).edit();
                editor.putString("tokens", encrypt(token.getToken())).apply();
                editor.putString("tokens_secret", encrypt(token.getTokenSecret())).apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onException(final TwitterException te, final TwitterMethod method) { //super.onException(te, method);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, te.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
            //Log.TimeLine("A", "a:" + te.getErrorMessage());
        }
    };
    /**
     * 非同期処理のTwitterのコンストラクタ.
     */
    private AsyncTwitter mTwitter;

    /**
     * アクティビティから呼ばれた場合のコンストラクタ.
     *
     * @param context context
     */
    public TwitterConnect(final Context context) {
        this.context = context;
        GetPrivateDeta privateDeta = new GetPrivateDeta();
        apiKey = privateDeta.getSkebeWord();
        apiSecret = privateDeta.getEroWord();
    }

    /*
    /**
     * アクティビティから呼ばれた場合のコンストラクタ.
     *
     * @param activity アクティビティ
     */
    /*
    public TwitterConnect(final Activity activity) {
        appCompatActivity = (AppCompatActivity) activity;
    }
    */

    /**
     * TwitterとOAuth認証を行う際のメソッド.
     *
     * @param appCompatActivity appCompatActivityde
     */
    public void signIn(final AppCompatActivity appCompatActivity) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean flag = sharedPreferences.getBoolean("flag", false);
        if (!flag) {
            Toast.makeText(appCompatActivity.getApplicationContext(), R.string.twitter_sinin, Toast.LENGTH_LONG).show();
            mTwitter.addListener(mListener);
            mTwitter.getOAuthRequestTokenAsync("noriottercallback://callback");
        } else {
            FragmentCancelOKAlertDialog fragmentCancelOKAlertDialog = new FragmentCancelOKAlertDialog();
            Bundle bundle = new Bundle();
            bundle.putInt("title", R.string.this_connect_now);
            bundle.putString("massage", appCompatActivity.getString(R.string.this_connect_now_mass));
            bundle.putInt("buttonName", R.string.dis_connect);
            fragmentCancelOKAlertDialog.setArguments(bundle);
            fragmentCancelOKAlertDialog.show(appCompatActivity.getSupportFragmentManager(), "disconnect");
        }
    }

    /**
     * loginメソッド.
     */
    public void login() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String tokens2 = sharedPreferences.getString("tokens", null);
        String tokensSecrets = sharedPreferences.getString("tokens_secret", null);
        mTwitter = new AsyncTwitterFactory().getInstance();
        mTwitter.setOAuthConsumer(apiKey, apiSecret);
        if (sharedPreferences.getBoolean("flag", false)) {
            if ((tokens2 != null) && (tokensSecrets != null)) {
                try {
                    tokens2 = decrypt(tokens2);
                    tokensSecrets = decrypt(tokensSecrets);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mTwitter.setOAuthAccessToken(new AccessToken(tokens2, tokensSecrets));
                mTwitter.verifyCredentials();
                sharedPreferences.edit().putBoolean("flag", true).apply();
            }
        }
    }

    /**
     * Twitterとの連携を解除するメソッド.
     * このメソッドで連携を解除し、各トークンの情報を削除する。
     */
    public void logout() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("tokens").apply();
        editor.remove("tokens_secret").apply();
        editor.remove("scren_name").apply();
        editor.remove("flag").apply();
        getmTwitter().setOAuthAccessToken(null);
        //Toast.makeText(appCompatActivity, "ログアウト適用のためアプリ再起動します", Toast.LENGTH_SHORT).show();
    }

    /**
     * TwitterとOAuth認証をした際にブラウザ等からのコールバックを受け取るメソッド.
     *
     * @param verifier コールバック時に受け取った認証鍵?
     */
    public void getOAuthAsync(final String verifier) {
        mTwitter.getOAuthAccessTokenAsync(mReqToken, verifier);
    }

    /**
     * APIKEYを取得するメソッド.
     *
     * @return APIKEY
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * APISECRETを取得するメソッド.
     *
     * @return getApiSECRET
     */
    public String getApiSecret() {
        return apiSecret;
    }

    /**
     * 非同期処理を行う際に必要になるインスタンスを返すメソッド.
     *
     * @return 非同期処理のTwitter
     */
    public AsyncTwitter getmTwitter() {
        return mTwitter;
    }

    /**
     * 使われてないメソッドだおぉーーん.
     *
     * @return しらん
     */
    public Configuration getConfiguration() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String tokens = sharedPreferences.getString("tokens", null);
        String tokensSecrets = sharedPreferences.getString("tokens_secret", null);
        if (sharedPreferences.getBoolean("flag", false)) {
            if ((tokens != null) && (tokensSecrets != null)) {
                try {
                    tokens = decrypt(tokens);
                    tokensSecrets = decrypt(tokensSecrets);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return new ConfigurationBuilder().setOAuthConsumerKey(apiKey)
                        .setOAuthConsumerSecret(apiSecret)
                        .setOAuthAccessToken(tokens)
                        .setOAuthAccessTokenSecret(tokensSecrets)
                        .build();
            }
        }
        return null;
    }

    /**
     * AndroidKeyStoreを使用した暗号化プロセスの初期化処理です.
     *
     * @return 暗号化・複合化を行う際に利用するビルダーを返します。
     * @throws CertificateException               CertificateException
     * @throws NoSuchPaddingException             NoSuchPaddingException
     * @throws NoSuchAlgorithmException           NoSuchAlgorithmException
     * @throws KeyStoreException                  KeyStoreException
     * @throws NoSuchProviderException            NoSuchProviderException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws IOException                        IOException
     */
    private Cryptore getCryptore() throws CertificateException, NoSuchPaddingException, NoSuchAlgorithmException,
            KeyStoreException, NoSuchProviderException, InvalidAlgorithmParameterException, IOException {
        Cryptore.Builder builder = new Cryptore.Builder("CIPHER_RSA", CipherAlgorithm.RSA);
        builder.setContext(context);
        return builder.build();
    }

    /**
     * 実際に暗号化を処理するメソッドです.
     *
     * @param plainStr 暗号化対象の文字列
     * @return 暗号化後の文字列
     * @throws CertificateException               CertificateException
     * @throws NoSuchAlgorithmException           NoSuchAlgorithmException
     * @throws NoSuchPaddingException             NoSuchPaddingException
     * @throws KeyStoreException                  KeyStoreException
     * @throws NoSuchProviderException            NoSuchProviderException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws IOException                        IOException
     * @throws UnrecoverableEntryException        UnrecoverableEntryException
     * @throws InvalidKeyException                InvalidKeyException
     */
    private String encrypt(final String plainStr) throws CertificateException, NoSuchAlgorithmException, NoSuchPaddingException,
            KeyStoreException, NoSuchProviderException, InvalidAlgorithmParameterException, IOException, UnrecoverableEntryException, InvalidKeyException {
        byte[] plainByte = plainStr.getBytes();
        EncryptResult result = getCryptore().encrypt(plainByte);
        return Base64.encodeToString(result.getBytes(), Base64.DEFAULT);
    }

    /**
     * 実際に複合化を処理するメソッドです.
     *
     * @param encryptedStr 複合化対象の文字列
     * @return 複合後の文字列
     * @throws CertificateException               CertificateException
     * @throws NoSuchAlgorithmException           NoSuchAlgorithmException
     * @throws NoSuchPaddingException             NoSuchPaddingException
     * @throws KeyStoreException                  KeyStoreException
     * @throws NoSuchProviderException            NoSuchProviderException
     * @throws InvalidAlgorithmParameterException InvalidAlgorithmParameterException
     * @throws IOException                        IOException
     * @throws UnrecoverableKeyException          UnrecoverableEntryException
     * @throws InvalidKeyException                InvalidKeyException
     */
    private String decrypt(final String encryptedStr) throws CertificateException, NoSuchAlgorithmException, NoSuchPaddingException, KeyStoreException,
            NoSuchProviderException, InvalidAlgorithmParameterException, IOException, UnrecoverableKeyException, InvalidKeyException {
        byte[] encryptedByte = Base64.decode(encryptedStr, Base64.DEFAULT);
        DecryptResult result = getCryptore().decrypt(encryptedByte, null);
        return new String(result.getBytes());
    }
}
