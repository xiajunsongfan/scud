package com.xj.scud.console.view;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: baichuan - xiajun
 * Date: 2017/09/26 12:35
 */
public class MonitorChartBean {
    private String title;
    private List<Integer> TP50 = new ArrayList<>(30);
    private List<Integer> TP90 = new ArrayList<>(30);

    private List<Integer> TP99 = new ArrayList<>(30);

    private List<Integer> TP999 = new ArrayList<>(30);

    private List<Integer> MAX = new ArrayList<>(30);

    private List<String> time = new ArrayList<>(30);


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Integer> getTP50() {
        return TP50;
    }

    public void addTP50(Integer TP50) {
        this.TP50.add(TP50);
    }

    public List<Integer> getTP90() {
        return TP90;
    }

    public void addTP90(Integer TP90) {
        this.TP90.add(TP90);
    }

    public List<Integer> getTP99() {
        return TP99;
    }

    public void addTP99(Integer TP99) {
        this.TP99.add(TP99);
    }

    public List<Integer> getTP999() {
        return TP999;
    }

    public void addTP999(Integer TP999) {
        this.TP999.add(TP999);
    }

    public List<Integer> getMAX() {
        return MAX;
    }

    public void addMAX(Integer MAX) {
        this.MAX.add(MAX);
    }

    public List<String> getTime() {
        return time;
    }

    public void addTime(String time) {
        this.time.add(time);
    }

    public String buildTime() {
        return new Gson().toJson(time);
    }
}
