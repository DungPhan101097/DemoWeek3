package com.example.lap10715.demoweek3.demo_green_dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

@Entity(nameInDb = "NOTE")
public class Note {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @Property (nameInDb = "Title")
    private String title;

    @NotNull
    @Property(nameInDb = "Content")
    private String content;

    @NotNull
    @Property(nameInDb = "Date")
    private Date date;

    @Generated(hash = 1582243354)
    public Note(Long id, @NotNull String title, @NotNull String content,
            @NotNull Date date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
    }

    @Generated(hash = 1272611929)
    public Note() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
   
}
