package com.otus.asm;

import java.util.HashMap;
import java.util.Map;

/**
 * Загружает класс из подменённых байтов, минуя parent для известных имён.
 * Без этого defineClass не сработает: TaskImpl уже есть на classpath.
 */
public class ByteArrayClassLoader extends ClassLoader {

    private final Map<String, byte[]> classes = new HashMap<>();

    public ByteArrayClassLoader(ClassLoader parent) {
        super(parent);
    }

    public Class<?> defineAndLoad(String name, byte[] bytes) {
        classes.put(name, bytes);
        try {
            return loadClass(name);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        byte[] bytes = classes.get(name);
        if (bytes == null) {
            return super.loadClass(name, resolve);
        }
        synchronized (getClassLoadingLock(name)) {
            Class<?> c = findLoadedClass(name);
            if (c == null) {
                c = defineClass(name, bytes, 0, bytes.length);
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }
}