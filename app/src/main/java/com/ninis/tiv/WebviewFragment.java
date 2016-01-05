package com.ninis.tiv;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

/**
 * Created by gypark on 15. 8. 4..
 */
public class WebviewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private final String LOAD_URL = "http://m.blog.naver.com/tiv85";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private WebView mWebView;
    private CircleProgressBar mCircleProgressBar;

    public interface OnPageFinishedCallback {
        void onPageFinished(WebView view, String url);
    }
    private OnPageFinishedCallback mOnPageFinishedCallback = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_webview, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.color1, R.color.color2, R.color.color3, R.color.color4);
        mCircleProgressBar = (CircleProgressBar) root.findViewById(R.id.progressBar_Custom);

        mWebView = (WebView) root.findViewById(R.id.wv_main);
        initWebView();

        return root;
    }

    public void setOnPageFinishedCallback(OnPageFinishedCallback callback) {
        mOnPageFinishedCallback = callback;
    }

    private void initWebView() {
        if (mWebView == null)
            return;

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClientClass());

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if( mCircleProgressBar != null ) {
                    mCircleProgressBar.setProgress(newProgress);
                }
            }
        });

        mWebView.loadUrl(LOAD_URL);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            mCircleProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if( mOnPageFinishedCallback != null ) {
                mOnPageFinishedCallback.onPageFinished(view, url);
            }

            mCircleProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

        }
    }

    public void loadHomeUrl() {
        if (mWebView == null)
            return;

        mWebView.clearHistory();
        mWebView.loadUrl(LOAD_URL);
    }

    public void goBackPage() {
        if( mWebView.canGoBack() )
            mWebView.goBack();
    }

    public void goForwardPage() {
        if( mWebView.canGoForward() )
            mWebView.goForward();
    }

    public void refreshPage() {
        mWebView.reload();
    }

    @Override
    public void onRefresh() {
        refreshPage();

        mSwipeRefreshLayout.setRefreshing(false);
    }

    public boolean canGoBackPage() {
        return mWebView.canGoBack();
    }
}
