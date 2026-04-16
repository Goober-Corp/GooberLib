package com.goobercorp.gooberlib.asm;

import com.goobercorp.gooberlib.GooberConfig;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM9;

public class ModClassVisitor extends ClassVisitor {
    private static final String CONFIG_ANNOTATION_DESCRIPTOR = GooberConfig.class.descriptorString();

    private final ClassResultHandler classResultHandler;
    private final AccessorResultHandler accessorResultHandler;

    private String className;

    public ModClassVisitor(ClassResultHandler classResultHandler, AccessorResultHandler accessorResultHandler) {
        super(ASM9);
        this.classResultHandler = classResultHandler;
        this.accessorResultHandler = accessorResultHandler;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.className = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        return new ConfigMethodVisitor(() -> accessorResultHandler.handle(className, new MethodResult(name, descriptor)));
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor.equals(CONFIG_ANNOTATION_DESCRIPTOR)) {
            return new ModIdAnnotationVisitor(modId -> classResultHandler.handle(className, modId));
        }

        return null;
    }

    public interface ClassResultHandler {
        void handle(String className, String modId);
    }

    public interface AccessorResultHandler {
        void handle(String className, MethodResult methodResult);
    }

    private enum CurrentNode {
        CLASS,
        METHOD;
    }
}
