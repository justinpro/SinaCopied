package com.example.justin.sinacopied.service;

public class RequestWebData {
    private int port;

    public RequestWebData(int port) {
        this.port = port;
    }

    public String setUrl(String date, String filename) {
        //基本的url
        String baseUrl = "http://localhost:" + port + "/";
        //给baseUrl添加日期
        String baseDataUrl = baseUrl + date + "/";
        //添加当天新闻的id
        String todayNewsUrl = baseDataUrl + filename + ".html";
        return todayNewsUrl;
    }

}
