package com.otus.asm;

import com.otus.measure.TaskImpl;
import com.otus.measure.TaskInterface;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.InputStream;

public class AsmMeasureDemo {

    private static final String IMPL_NAME = "com.otus.measure.TaskImpl";
    private static final String IMPL_PATH = "com/otus/measure/TaskImpl.class";

    public static void main(String[] args) throws Exception {
        System.out.println("=== ASM: BEFORE (no transformation) ===");
        TaskInterface plain = new TaskImpl();
        plain.compute(1);
        plain.compute(1, 2);
        plain.compute(1, 2, "hello");
        plain.compute(25L);
        plain.cleanup();
        System.out.println("(no output above = no measurement)");

        System.out.println();
        System.out.println("=== ASM: AFTER (transformed bytes) ===");

        byte[] original = readResource(IMPL_PATH);
        byte[] transformed = transform(original);

        ByteArrayClassLoader loader =
                new ByteArrayClassLoader(Thread.currentThread().getContextClassLoader());
        Class<?> clazz = loader.defineAndLoad(IMPL_NAME, transformed);

        TaskInterface measured = (TaskInterface) clazz.getDeclaredConstructor().newInstance();
        measured.compute(1);
        measured.compute(1, 2);
        measured.compute(1, 2, "hello");
        measured.compute(25L);
        measured.cleanup();
        System.out.println("(last line above should NOT appear)");
    }

    private static byte[] transform(byte[] input) {
        ClassReader cr = new ClassReader(input);
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        // Наша точка входа
        cr.accept(new MeasureClassVisitor(cw), 0);
        return cw.toByteArray();
    }

    private static byte[] readResource(String path) throws Exception {
        try (InputStream is = Thread.currentThread()
                .getContextClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalStateException("Not found on classpath: " + path);
            }
            return is.readAllBytes();
        }
    }
}