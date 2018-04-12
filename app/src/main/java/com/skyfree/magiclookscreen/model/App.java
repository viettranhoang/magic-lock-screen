package com.skyfree.magiclookscreen.model;

import java.io.Serializable;

/**
 * Created by Admin on 10/3/2018.
 */

public class App implements Serializable{
    private String name;
    private String id;
    private boolean isChoose;

    public App(String name, String id, boolean isChoose) {
        this.name = name;
        this.id = id;
        this.isChoose = isChoose;
    }

    public App(String name, String id) {
        this.name = name;
        this.id = id;
        this.isChoose = false;
    }

    public String getName() {
        return name;
    }

    public boolean getChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
