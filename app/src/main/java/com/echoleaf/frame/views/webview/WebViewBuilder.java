package com.echoleaf.frame.views.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.echoleaf.frame.utils.StringUtils;

/**
 * Created by echoleaf on 2017/12/11.
 */

public class WebViewBuilder {

    private Activity mActivity;
    private WebView mWebView;
    private OnPageFinishedListener onPageFinishedListener;
    private OnReceivedTitleListener onReceivedTitleListener;
    private OnLoadUrlListener onLoadUrlListener;
    private OnReceivedSslErrorListener onReceivedSslErrorListener;
    private String initUrl;

    private WebViewBuilder(@Nullable Activity activity, @Nullable WebView webView) {
        mActivity = activity;
        mWebView = webView;
    }

    public WebViewBuilder loadUrlListener(@Nullable OnLoadUrlListener onLoadUrlListener) {
        this.onLoadUrlListener = onLoadUrlListener;
        return this;
    }

    public WebViewBuilder pageFinishedListener(@Nullable OnPageFinishedListener onPageFinishedListener) {
        this.onPageFinishedListener = onPageFinishedListener;
        return this;
    }

    public WebViewBuilder receivedSslErrorListener(@Nullable OnReceivedSslErrorListener onReceivedSslErrorListener) {
        this.onReceivedSslErrorListener = onReceivedSslErrorListener;
        return this;
    }

    public WebViewBuilder receivedTitleListener(@Nullable OnReceivedTitleListener onReceivedTitleListener) {
        this.onReceivedTitleListener = onReceivedTitleListener;
        return this;
    }

    public WebViewBuilder initUrl(@Nullable String url) {
        this.initUrl = url;
        return this;
    }

    public WebView build() {
        if (mActivity == null || mWebView == null)
            return null;
        WebSettings webSettings = mWebView.getSettings();
        // User settings
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);//关键点
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);


        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true); // 设置支持javascript脚本
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        webSettings.setSupportZoom(true); // 支持缩放

        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);

        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        Log.d("maomao", "densityDpi = " + mDensity);
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
        /**
         * 用WebView显示图片，可使用这个参数 设置网页布局类型： 1、LayoutAlgorithm.NARROW_COLUMNS ：
         * 适应内容大小 2、LayoutAlgorithm.SINGLE_COLUMN:适应屏幕，内容将自动缩放
         */
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);//设置https链接中显示http图片
        }

        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (onReceivedTitleListener != null)
                    onReceivedTitleListener.onReceivedTitle(view, title);
            }

        };
        // 设置setWebChromeClient对象
        mWebView.setWebChromeClient(wvcc);

        // 创建WebViewClient对象
        WebViewClient wvc = new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                boolean override = false;
                if (onPageFinishedListener != null)
                    override = onPageFinishedListener.onPageFinished(view, url);
                if (!override)
                    super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
                // 消耗掉这个事件。Android中返回True的即到此为止吧,事件就会不会冒泡传递了，我们称之为消耗掉
                boolean override = false;
                if (onLoadUrlListener != null)
                    override = onLoadUrlListener.onLoadUrl(view, url);
                if (!override)
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        mWebView.loadUrl(url);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        mActivity.startActivity(intent);
                    }
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                boolean override = false;
                if (onReceivedSslErrorListener != null)
                    override = onReceivedSslErrorListener.onReceivedSslError(view, handler, error);
                if (!override)
                    handler.proceed();  // 接受所有网站的证书
            }

        };
        mWebView.setWebViewClient(wvc);
        WebViewJScript webViewJScript = new WebViewJScript(mActivity, mWebView);
        mWebView.addJavascriptInterface(webViewJScript, "EchoLeafAjs");

        if (StringUtils.notEmpty(initUrl)) {
            if (initUrl.indexOf("http") != 0)
                initUrl = "http://" + initUrl;
            mWebView.loadUrl(initUrl);
        }

        return mWebView;
    }

    public static WebViewBuilder newBuilder(Activity activity, WebView webView) {
        return new WebViewBuilder(activity, webView);
    }

    public interface OnPageFinishedListener {
        boolean onPageFinished(WebView view, String url);
    }

    public interface OnReceivedTitleListener {
        boolean onReceivedTitle(WebView view, String title);
    }

    public interface OnLoadUrlListener {
        boolean onLoadUrl(WebView view, String url);
    }

    public interface OnReceivedSslErrorListener {
        boolean onReceivedSslError(WebView view, SslErrorHandler handler, SslError error);
    }


}
