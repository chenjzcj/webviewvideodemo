package com.sun.videodemo;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;
    private FrameLayout mVideoContainer;
    private WebChromeClient.CustomViewCallback mCallBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView) findViewById(R.id.webView);
        mVideoContainer = (FrameLayout) findViewById(R.id.videoContainer);

        initWebView();
    }

    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new CustomWebViewChromeClient());
        //mWebView.setWebViewClient(new CustomWebClient());

        mWebView.loadUrl("http://play.ykreadvip.com//player_m3u8.php?url=https://cdn-4.haku99.com/hls/2019/03/01/IE3eyJLE/playlist.m3u8");
    }


    private class CustomWebViewChromeClient extends WebChromeClient {

        @Override
        public void onHideCustomView() {
            if (mCallBack != null) {
                mCallBack.onCustomViewHidden();
            }
            mWebView.setVisibility(View.VISIBLE);
            mVideoContainer.removeAllViews();
            mVideoContainer.setVisibility(View.GONE);
            fullScreen();
            //清除全屏
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            mWebView.setVisibility(View.GONE);
            mVideoContainer.setVisibility(View.VISIBLE);
            mVideoContainer.addView(view);
            mCallBack = callback;
            fullScreen();
            //设置全屏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void fullScreen() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    private class CustomWebClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            String js = TagUtils.getJs(url);
            view.loadUrl(js);
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
