package com.hashstar.cocbases.adapter;

import org.json.JSONArray;

/**
 * Created by suhas on 11/4/17.
 */
public class TownHallGridItem {

    String name, url;
    JSONArray jsonArray;
    int order;


    public TownHallGridItem(String name, String url, JSONArray jsonArray,int order)
    {
        this.name=name;
        this.url=url;
        this.jsonArray=jsonArray;
        this.order=order;

    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
