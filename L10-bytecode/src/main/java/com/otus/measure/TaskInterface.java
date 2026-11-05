package com.otus.measure;

public interface TaskInterface {
    void compute(int a);
    void compute(int a, int b);
    void compute(int a, int b, String tag);
    void compute(long duration);
    void cleanup();               // без @Measure — логировать НЕ должен
}