package com.example.justin.sinacopied.service;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CustomWebViewClient extends WebViewClient {


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        view.getSettings().setJavaScriptEnabled(true);

        //待网页加载完全后设置图片点击的监听方法
        OnImageClickListener(view);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        view.getSettings().setJavaScriptEnabled(true);
    }

    private void OnImageClickListener(WebView webView){
        //这段js函数的功能就是注册监听，遍历所有的img标签，并添加onClick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        webView.loadUrl("javascript:(function(){"
                + "var objs = document.getElementsByTagName(\"img\"); "
                + "for(var i=0;i<objs.length;i++)  " + "{"
                + "    objs[i].onclick=function()  " + "    {  "
                + "        window.imageListener.openImage(this.src);  "
                + "    }  "
                + "}"
                + "})()");
    }
}
