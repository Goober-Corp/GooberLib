package com.goobercorp.gooberlib.asm;

import org.objectweb.asm.AnnotationVisitor;

import static org.objectweb.asm.Opcodes.ASM9;

public class ModIdAnnotationVisitor extends AnnotationVisitor {
    private final ResultHandler resultHandler;


    public ModIdAnnotationVisitor(ResultHandler resultHandler) {
        super(ASM9);
        this.resultHandler = resultHandler;
    }

    @Override
    public void visit(String name, Object value) {
        if (name.equals("modId")) resultHandler.handle((String) value);
    }

    public interface ResultHandler {
        void handle(String modId);
    }
}
