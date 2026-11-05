package com.otus.proxy;


import com.otus.measure.TaskImpl;
import com.otus.measure.TaskInterface;

public class ProxyMeasureDemo {

    public static void main(String[] args) {
        System.out.println("=== PROXY: BEFORE (no wrapping) ===");
        TaskInterface plain = new TaskImpl();
        plain.compute(1);
        plain.compute(1, 2);
        plain.compute(1, 2, "hello");
        plain.compute(25L);
        plain.cleanup();
        System.out.println("(no output above = no measurement)");
        System.out.println(plain.getClass());

        System.out.println();
        System.out.println("=== PROXY: AFTER (wrapped) ===");
        TaskInterface measured = MeasuringProxy.wrap(new TaskImpl(), TaskInterface.class);
        measured.compute(1);
        measured.compute(1, 2);
        measured.compute(1, 2, "hello");
        measured.compute(25L);
        measured.cleanup();   // тишина — без @Measure
        System.out.println("(last line above should NOT appear)");
        System.out.println(measured.getClass());
    }
}