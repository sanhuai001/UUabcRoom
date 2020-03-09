package com.uuabc.classroomlib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.DownloadListener;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.CacheMemoryUtils;
import com.blankj.utilcode.util.CleanUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ren.yale.android.cachewebviewlib.CacheWebView;
import ren.yale.android.cachewebviewlib.WebViewCache;

/**
 * 加载网页控件
 * Created by wb on 2017/9/26.
 */
@SuppressLint("CheckResult")
public class ProgressWebView extends CacheWebView {
    public ViewDownloadListener mViewDownloadListener;
    private boolean canAnimate;
    private LoadUrlListener loadUrlListener;
    private LoadErrorListener loadErrorListener;
    private String mUrl;

    private boolean forbidScroll;

    public void setforbidScroll(boolean forbidScroll) {
        this.forbidScroll = forbidScroll;
    }

    public void setCanAnimate(boolean canAnimate) {
        this.canAnimate = canAnimate;
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public ProgressWebView(Context context) {
        super(getFixedContext(context));
        initWebView();
    }

    public ProgressWebView(final Context context, AttributeSet attrs) {
        super(getFixedContext(context), attrs);
        initWebView();
    }

    public ProgressWebView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(getFixedContext(context), attrs, defStyleAttr);
        initWebView();
    }

    public static Context getFixedContext(Context context) {
        return context.createConfigurationContext(new Configuration());
    }

    private void initWebView() {
        setCacheStrategy(WebViewCache.CacheStrategy.NO_CACHE);
        setWebChromeClient(new WebChromeClient());
        setWebViewClient(new WebViewClient());
        setDownloadListener(new WebViewDownLoadListener());
        String ua = getSettings().getUserAgentString();
        getSettings().setUserAgentString(ua + "; uuabcAndroid");
        getSettings().setSavePassword(false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return !canAnimate || super.onTouchEvent(event);
    }

    public interface ViewDownloadListener {
        void onViewDownloadStart(String url, String contentDisposition);
    }

    private class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    }

    private class WebViewClient extends android.webkit.WebViewClient {
        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
            return super.shouldInterceptRequest(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (loadUrlListener != null) {
                loadUrlListener.onUrlLoad(url);
            }
            loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!view.getSettings().getLoadsImagesAutomatically()) {
                view.getSettings().setLoadsImagesAutomatically(true);
            }
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            if (loadErrorListener != null && request != null && errorResponse != null && request.getUrl() != null) {
                int statusCode = errorResponse.getStatusCode();
                String requestUrl = String.valueOf(request.getUrl());
                if (statusCode != 200 && TextUtils.equals(requestUrl, mUrl)) {
                    loadErrorListener.onLoadError();
                }
            }
        }
    }

    private class WebViewDownLoadListener implements DownloadListener {
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
            if (mViewDownloadListener != null) {
                mViewDownloadListener.onViewDownloadStart(url, contentDisposition);
            }
        }
    }

    public void setLoadUrlListener(LoadUrlListener loadUrlListener) {
        this.loadUrlListener = loadUrlListener;
    }

    public void setLoadErrorListener(LoadErrorListener listener) {
        this.loadErrorListener = listener;
    }

    public void removeLoadErrorListener() {
        this.loadErrorListener = null;
    }

    public interface LoadUrlListener {
        void onUrlLoad(String url);
    }

    public interface LoadErrorListener {
        void onLoadError();
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return !forbidScroll && super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
    }

    /**
     * 使WebView不可滚动
     */
    @Override
    public void scrollTo(int x, int y) {
        if (forbidScroll) {
            super.scrollTo(0, 0);
        }
    }

    public void destoryWebView() {
        Observable.just("destoryWebView")
                .subscribeOn(Schedulers.io())
                .doOnNext(s -> {
                    clearCache(true);
                    clearHistory();
                    CleanUtils.cleanExternalCache();
                    CleanUtils.cleanInternalCache();
                    CleanUtils.cleanInternalDbs();
                    CacheMemoryUtils.getInstance().clear();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((response) -> {
                    clearCache();
                    destroy();
                }, throwable -> {
                });
    }

    @Override
    public void setOverScrollMode(int mode) {
        try {
            super.setOverScrollMode(mode);
        } catch (Exception ignored) {

        }
    }
}
