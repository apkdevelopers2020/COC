package com.hashstar.cocbases.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by phoenix on 20/4/17.
 */
public class TownHall implements Serializable {

    String name;
    HashMap<String,ArrayList<String>> townHallObj;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, ArrayList<String>> getTownHallObj() {
        return townHallObj;
    }

    public void setTownHallObj(HashMap<String, ArrayList<String>> townHallObj) {
        this.townHallObj = townHallObj;
    }
}

