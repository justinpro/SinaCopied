package com.example.justin.sinacopied.service;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * 返回标题的类，回调接口
 */
public class CustomWebChromeClient extends WebChromeClient {

    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
        String webTitle = title;

    }
}
