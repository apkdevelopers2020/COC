package com.hashstar.cocbases.notifications;

/**
 * Created by suhas on 25/4/17 .
 */
public class NotiItem {

    String id,url,title,descreption;

    public NotiItem(String id, String url, String title, String descreption)
    {
        this.id = id;
        this.url = url;
        this.title = title;
        this.descreption = descreption;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescreption() {
        return descreption;
    }

    public void setDescreption(String descreption) {
        this.descreption = descreption;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
