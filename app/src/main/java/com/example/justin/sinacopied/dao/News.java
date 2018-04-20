package com.example.justin.sinacopied.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class News {

    @Id(autoincrement = true)
    private Long id;
    private String title;
    private String date;

    @Generated(hash = 42447757)
    public News(Long id, String title, String date) {
        this.id = id;
        this.title = title;
        this.date = date;
    }

    @Generated(hash = 1579685679)
    public News() {
    }

    public Long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
