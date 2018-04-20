package com.example.justin.sinacopied.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.justin.sinacopied.R;
import com.example.justin.sinacopied.service.CustomWebViewClient;
import com.example.justin.sinacopied.service.LocalWebService;
import com.example.justin.sinacopied.service.RequestWebData;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryDetailContext extends Activity {
    public static final String DETAIL_ID = "detail:id";
    public static final String DETAIL_DATE = "detail:date";

    private LocalWebService localWebService;

    public static final String DETAIL_TITLE = "detail:title";
    public static final String DETAIL_CONTEXT = "detail:context";
    public static final String DETAIL_IMAGE = "detail:image";

    private TextView detailTitle;
    private TextView detailContext;
    private ImageView detailImage;

    private WebView webView;

    private String getDataFilename = null;
    private String getDataDate = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //因为是历史记录，所以详情页一定要和之前的页面一样
        setContentView(R.layout.home_child_fragment);

        getDataFilename = getIntent().getStringExtra(DETAIL_ID);
        getDataDate = getIntent().getStringExtra(DETAIL_DATE);

        detailTitle = (TextView) this.findViewById(R.id.home_detail_title);
        //测试用的,获得当前日期
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        detailTitle.setText(getDataFilename + " " + date.format(new Date()));

        detailContext = (TextView) this.findViewById(R.id.home_detail_content);
        detailImage = (ImageView) this.findViewById(R.id.home_detail_image);

        webView = (WebView) this.findViewById(R.id.webView);

        ViewCompat.setTransitionName(detailTitle, DETAIL_TITLE);
        ViewCompat.setTransitionName(detailContext, DETAIL_CONTEXT);
        ViewCompat.setTransitionName(detailImage, DETAIL_IMAGE);

    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onResume() {
        super.onResume();
        final int port = 8080;
        localWebService = new LocalWebService(port, getResources().getAssets());
        localWebService.start();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.INTERNET}, 1);
        } else {

            RequestWebData requestWebData = new RequestWebData(port);
            String getUrl = requestWebData.setUrl(getDataDate, getDataFilename);
            //加载新闻
            webView.loadUrl(getUrl);

            webView.setWebViewClient(new CustomWebViewClient());
            //webView.setWebChromeClient(new CustomWebChromeClient());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        localWebService.stop();
    }

}
