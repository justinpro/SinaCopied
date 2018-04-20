package com.example.justin.sinacopied.dao;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DaoManager {
    private Map<Long, String> historyMaps;
    private NewsDao newsDao;
    private Long pid = 0L;

    public void setupDataBase(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "newsHistory.db");
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();

        newsDao = daoSession.getNewsDao();
    }

    public void insert(String filename) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy年MM月dd日 HH:mm:ss");
        String clickTime = simpleDateFormat.format(new Date());

        //注意!!!!
        //因为是长整型，所以不能和int 一样用++
        // 也不能这么用 pid += 1L; 会报错
        //最好用null,自动填充
        News news = new News(null, filename, clickTime);
        newsDao.insert(news);
    }

    public Map<Long, String> query() {
        List<News> allList = null;
        historyMaps = new LinkedHashMap<>();
        allList = newsDao.queryBuilder().orderAsc(NewsDao.Properties.Id).build().list();

        for (int i = 0; i < allList.size(); i++) {
            Long id = allList.get(i).getId();
            String date = allList.get(i).getTitle() + "," + allList.get(i).getDate();
            historyMaps.put(id, date);
        }
        return historyMaps;
    }

    public void removeAll(){
        newsDao.deleteAll();
    }
}
