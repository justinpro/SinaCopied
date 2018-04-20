package com.example.justin.sinacopied.service;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.example.justin.sinacopied.ui.HomeDetailImageContext;

public class JsInterface {
    private Context context;
    private String[] imgUrls;

    public JsInterface(Context context) {
        this.context = context;
    }

    /**  ###### 注意  #######
     * 没有这个@android.webkit.JavascriptInterface 是无法给android识别的 */

    @JavascriptInterface
    public void openImage(String urls) {   //提供给js调用的方法
        //Toast.makeText(context, urls, Toast.LENGTH_SHORT).show();
        Intent imgDetail=new Intent(context, HomeDetailImageContext.class);
        imgDetail.putExtra("url",urls);
        context.startActivity(imgDetail);
    }
}
