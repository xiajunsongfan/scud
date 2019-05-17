package com.xj.scud.atomic;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author: xiajun
 * @date: 2018/6/2 下午3:10
 * @since 1.0.0
 */
public class AtomicNumber {
    private AtomicReference<Long> ref;

    public AtomicNumber(long init) {
        ref = new AtomicReference<>(init);
    }

    public void incr() {
        this.incr(1);
    }

    public void incr(int num) {
        boolean ok = false;
        while (!ok) {
            Long counter = ref.get();
            ok = ref.weakCompareAndSet(counter, counter + num);
        }
    }

    public void decr() {
        this.decr(1);
    }

    public void decr(int num) {
        long counter = ref.get();
        ref.compareAndSet(counter, counter - num);
    }

    public long get() {
        return ref.get();
    }

    public static void main(String[] args) throws InterruptedException {
        AtomicNumber atomicNumber = new AtomicNumber(0);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000000; j++) {
                    atomicNumber.incr();
                }
            }).start();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(atomicNumber.get());
    }
}
