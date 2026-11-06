package com.otus.measure;


import com.otus.annotation.Measure;

public class TaskImpl implements TaskInterface {

    @Measure public void compute(int a)                    { sleep(10); }
    @Measure public void compute(int a, int b)             { sleep(20); }
    @Measure public void compute(int a, int b, String tag) { sleep(30); }
    @Measure public void compute(long duration)            { sleep(duration); }

    public void cleanup() { /* пусто */ }

    private static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
