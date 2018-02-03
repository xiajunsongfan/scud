package com.xj.scud.monitor;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Author: baichuan - xiajun
 * Date: 2017/08/25 11:41
 * 方法性能数据
 */
public class PerformanceData {
    private String methodName;//方法名，不支持重载方法

    public PerformanceData(String methodName) {
        this.methodName = methodName;
    }

    private AtomicInteger amount = new AtomicInteger();//总调用量
    private AtomicInteger[] container = new AtomicInteger[1537];//记录耗时

    /**
     * 添加调用时间到链表
     *
     * @param costTime 方法耗时
     */
    public void add(int costTime) {
        amount.incrementAndGet();
        Integer index = this.index(costTime);
        if (container[index] == null) {
            synchronized (container) {
                if (container[index] == null) {
                    AtomicInteger count = new AtomicInteger();
                    container[index] = count;
                }
            }
        }
        container[index].incrementAndGet();
    }

    private Integer index(int costTime) {
        int index;
        if (costTime < 256) {
            index = costTime;
        } else if (costTime >= 16128) {
            index = 1536;
        } else if (costTime >= 7936) {
            index = costTime / 32 + 956;
        } else if (costTime >= 3840) {
            index = costTime / 16 + 784;
        } else if (costTime >= 1792) {
            index = costTime / 8 + 544;
        } else if (costTime >= 768) {
            index = costTime / 4 + 320;
        } else {//costTime >= 256
            index = costTime / 2 + 128;
        }
        return index;
    }

    private int unindex(int index) {
        int value;
        if (index >= 1536) {
            value = 16128;
        } else if (index >= 1280) {
            value = (index - 956) * 32;
        } else if (index >= 1024) {
            value = (index - 784) * 16;
        } else if (index >= 768) {
            value = (index - 544) * 8;
        } else if (index >= 512) {
            value = (index - 320) * 4;
        } else if (index >= 256) {
            value = (index - 128) * 2;
        } else {
            value = index;
        }
        return value;
    }

    /**
     * 获取系统性能数据
     *
     * @return
     */
    public TopPercentile getTP() {
        int snapshotSize = this.amount.get();
        int tp50Index = snapshotSize * 5;
        tp50Index = tp50Index % 10 == 0 ? tp50Index / 10 : tp50Index / 10 + 1;
        int tp90Index = snapshotSize * 9;
        tp90Index = tp90Index % 10 == 0 ? tp90Index / 10 : tp90Index / 10 + 1;
        int tp99Index = snapshotSize * 99;
        tp99Index = tp99Index % 100 == 0 ? tp99Index / 100 : tp99Index / 100 + 1;
        int tp999Index = snapshotSize * 999;
        tp999Index = tp999Index % 1000 == 0 ? tp999Index / 1000 : tp999Index / 1000 + 1;
        int tp50 = -1, tp90 = -1, tp99 = -1, tp999 = -1, min = -1, max = -1;
        int count = 0;
        for (int i = 0; i < container.length; i++) {
            AtomicInteger c = container[i];
            if (c == null) {
                continue;
            }
            count += c.get();
            if (min < 0) {
                min = unindex(i);
            }
            if (tp50 < 0 && count >= tp50Index) {
                tp50 = unindex(i);
            }
            if (tp90 < 0 && count >= tp90Index) {
                tp90 = unindex(i);
            }
            if (tp99 < 0 && count >= tp99Index) {
                tp99 = unindex(i);
            }
            if (tp999 < 0 && count >= tp999Index) {
                tp999 = unindex(i);
            }
            if (count >= amount.get()) {
                max = unindex(i);
            }
        }
        return new TopPercentile(tp50, tp90, tp99, tp999, snapshotSize, min, max);
    }

    public String getMethodName() {
        return methodName;
    }

    public int getSize() {
        return amount.get();
    }
}
