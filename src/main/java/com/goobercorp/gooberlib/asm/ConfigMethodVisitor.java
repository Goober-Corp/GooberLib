package com.goobercorp.gooberlib.asm;

import com.goobercorp.gooberlib.GooberBuilderAccessor;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.ASM9;

public class ConfigMethodVisitor extends MethodVisitor {
    private static final String ACCESSOR_ANNOTATION_DESCRIPTOR = GooberBuilderAccessor.class.descriptorString();

    private final ResultHandler resultHandler;

    public ConfigMethodVisitor(ResultHandler resultHandler) {
        super(ASM9);
        this.resultHandler = resultHandler;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor.equals(ACCESSOR_ANNOTATION_DESCRIPTOR))
            resultHandler.handle();

        return null;
    }

    public interface ResultHandler {
        void handle();
    }
}
