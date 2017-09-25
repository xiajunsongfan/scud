package com.xj.scud.monitor;

/**
 * Author: baichuan - xiajun
 * Date: 2017/09/25 14:08
 */
public class TopPercentile {
    private int count;
    private int tp50;
    private int tp90;
    private int tp99;
    private int tp999;
    private int min;
    private int max;

    public TopPercentile(int tp50, int tp90, int tp99, int tp999, int count, int min, int max) {
        this.tp50 = tp50;
        this.tp90 = tp90;
        this.tp99 = tp99;
        this.tp999 = tp999;
        this.count = count;
        this.min = min;
        this.max = max;
    }

    public int getTp50() {
        return tp50;
    }

    public void setTp50(int tp50) {
        this.tp50 = tp50;
    }

    public int getTp90() {
        return tp90;
    }

    public void setTp90(int tp90) {
        this.tp90 = tp90;
    }

    public int getTp99() {
        return tp99;
    }

    public void setTp99(int tp99) {
        this.tp99 = tp99;
    }

    public int getTp999() {
        return tp999;
    }

    public void setTp999(int tp999) {
        this.tp999 = tp999;
    }

    public int getCount() {
        return count;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public String toString() {
        return "{" +
                "count=" + count +
                ", tp50=" + tp50 +
                ", tp90=" + tp90 +
                ", tp99=" + tp99 +
                ", tp999=" + tp999 +
                ", min=" + min +
                ", max=" + max +
                '}';
    }
}
