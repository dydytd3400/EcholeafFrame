package com.echoleaf.frame.views.webview;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.echoleaf.frame.utils.StringUtils;
import com.echoleaf.frame.utils.ViewUtils;

/**
 * Created by echoleaf on 2017/9/27.
 */

public class WebViewJScript {
    private Activity activity;
    private WebView webView;

    //sdk17版本以上加上注解

    public WebViewJScript(Activity activity, WebView webView) {
        this.activity = activity;
        this.webView = webView;
    }

    @JavascriptInterface
    public void finish() {
        if (activity != null)
            activity.finish();
    }


    @JavascriptInterface
    public void goBack() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        }
    }


    @JavascriptInterface
    public void goForward() {
        if (webView != null && webView.canGoForward()) {
            webView.goForward();
        }
    }

    @JavascriptInterface
    public boolean canGoBack() {
        if (webView != null)
            return webView.canGoBack();
        return false;
    }

    @JavascriptInterface
    public boolean canGoForward() {
        if (webView != null)
            return webView.canGoForward();
        return false;
    }

    @JavascriptInterface
    public void load(String url) {
        if (webView != null && StringUtils.notEmpty(url)) {
            if (url.indexOf("http") != 0)
                url = "http://" + url;
            webView.loadUrl(url);
        }
    }

    @JavascriptInterface
    public void toast(String msg) {
        if (activity != null)
            ViewUtils.toastMessage(activity, msg);
    }

    @JavascriptInterface
    public void exit() {
        if (activity != null) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            activity.startActivity(home);
        }
    }

}
