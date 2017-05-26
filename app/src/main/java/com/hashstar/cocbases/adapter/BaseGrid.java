package com.hashstar.cocbases.adapter;

/**
 * Created by phoenix on 21/4/17.
 */
public class BaseGrid
{
    String url;
    String name;
    public BaseGrid (String name, String url)
    {
        this.name = name;
        this.url = url;

    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

