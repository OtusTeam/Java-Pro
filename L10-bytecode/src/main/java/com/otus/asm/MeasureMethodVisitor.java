package com.otus.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

class MeasureMethodVisitor extends MethodVisitor {

    private static final String MEASURE_DESC = "Lcom/otus/annotation/Measure;";

    private final int access;
    private final String name;
    private final String descriptor;
    private boolean measured;
    private int startTimeLocal;

    MeasureMethodVisitor(MethodVisitor mv, int access, String name, String descriptor) {
        super(Opcodes.ASM9, mv);
        this.access = access;
        this.name = name;
        this.descriptor = descriptor;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String annDesc, boolean visible) {
        if (MEASURE_DESC.equals(annDesc)) {
            measured = true;
        }
        return super.visitAnnotation(annDesc, visible);
    }

    @Override
    public void visitCode() {
        super.visitCode();
        if (!measured) {
            return;
        }
        int thisSlot = (access & Opcodes.ACC_STATIC) == 0 ? 1 : 0;
        int argSlots = 0;
        for (Type t : Type.getArgumentTypes(descriptor)) {
            argSlots += t.getSize();
        }
        startTimeLocal = thisSlot + argSlots;   // первый свободный слот

        super.visitMethodInsn(Opcodes.INVOKESTATIC,
                "java/lang/System", "nanoTime", "()J", false);
        super.visitVarInsn(Opcodes.LSTORE, startTimeLocal);
    }

    @Override
    public void visitInsn(int opcode) {
        if (measured && opcode == Opcodes.RETURN) {
            emitPrint();
        }
        super.visitInsn(opcode);
    }

    private void emitPrint() {
        // System.out
        super.visitFieldInsn(Opcodes.GETSTATIC,
                "java/lang/System", "out", "Ljava/io/PrintStream;");

        // new StringBuilder("[asm] name(")
        super.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
        super.visitInsn(Opcodes.DUP);
        super.visitMethodInsn(Opcodes.INVOKESPECIAL,
                "java/lang/StringBuilder", "<init>", "()V", false);
        super.visitLdcInsn("[asm] " + name + "(");
        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                "java/lang/StringBuilder", "append",
                "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

        // параметры
        int slot = (access & Opcodes.ACC_STATIC) == 0 ? 1 : 0;
        Type[] args = Type.getArgumentTypes(descriptor);
        for (int i = 0; i < args.length; i++) {
            if (i > 0) {
                super.visitLdcInsn(", ");
                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                        "java/lang/StringBuilder", "append",
                        "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            }
            switch (args[i].getSort()) {
                case Type.INT -> {
                    super.visitVarInsn(Opcodes.ILOAD, slot);
                    super.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                            "java/lang/StringBuilder", "append",
                            "(I)Ljava/lang/StringBuilder;", false);
                }
                case Type.LONG -> {
                    super.visitVarInsn(Opcodes.LLOAD, slot);
                    super.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                            "java/lang/StringBuilder", "append",
                            "(J)Ljava/lang/StringBuilder;", false);
                }
                default -> {
                    super.visitVarInsn(Opcodes.ALOAD, slot);
                    super.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                            "java/lang/StringBuilder", "append",
                            "(Ljava/lang/Object;)Ljava/lang/StringBuilder;", false);
                }
            }
            slot += args[i].getSize();
        }

        // ") took " + (nanoTime() - startTimeLocal) + " ns"
        super.visitLdcInsn(") took ");
        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                "java/lang/StringBuilder", "append",
                "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

        super.visitMethodInsn(Opcodes.INVOKESTATIC,
                "java/lang/System", "nanoTime", "()J", false);
        super.visitVarInsn(Opcodes.LLOAD, startTimeLocal);
        super.visitInsn(Opcodes.LSUB);

        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                "java/lang/StringBuilder", "append",
                "(J)Ljava/lang/StringBuilder;", false);
        super.visitLdcInsn(" ns");
        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                "java/lang/StringBuilder", "append",
                "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                "java/lang/StringBuilder", "toString",
                "()Ljava/lang/String;", false);

        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
                "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }
}