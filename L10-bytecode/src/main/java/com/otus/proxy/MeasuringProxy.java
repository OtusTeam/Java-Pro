package com.otus.proxy;

import com.otus.annotation.Measure;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MeasuringProxy implements InvocationHandler {

    private final Object target;
    // кэш «метод интерфейса → метод реализации», чтобы не резолвить на каждом вызове
    private final Map<Method, Method> resolvedCache = new ConcurrentHashMap<>();

    private MeasuringProxy(Object target) {
        this.target = target;
    }

    @SuppressWarnings("unchecked")
    public static <T> T wrap(T target, Class<T> iface) {
        if (!iface.isInterface()) {
            throw new IllegalArgumentException(iface + " is not an interface");
        }
        return (T) Proxy.newProxyInstance(
                iface.getClassLoader(),
                new Class<?>[]{iface},
                new MeasuringProxy(target));
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // Метод, пришедший сюда, — из интерфейса. Аннотация стоит на реализации.
        Method impl = resolve(method);

        if (!impl.isAnnotationPresent(Measure.class)) {
            return impl.invoke(target, args);
        }

        long start = System.nanoTime();
        try {
            return impl.invoke(target, args);
        } finally {
            long elapsed = System.nanoTime() - start;
            System.out.println(format(impl, args, elapsed));
        }
    }

    private Method resolve(Method interfaceMethod) {
        return resolvedCache.computeIfAbsent(interfaceMethod, im -> {
            try {
                return target.getClass()
                        .getMethod(im.getName(), im.getParameterTypes());
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException(
                        "Target does not implement " + im, e);
            }
        });
    }

    private static String format(Method method, Object[] args, long elapsedNs) {
        StringBuilder sb = new StringBuilder("[proxy] ")
                .append(method.getName())
                .append('(');
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(args[i]);
            }
        }
        return sb.append(") took ").append(elapsedNs).append(" ns").toString();
    }
}