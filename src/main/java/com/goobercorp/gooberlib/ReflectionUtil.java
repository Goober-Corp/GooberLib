package com.goobercorp.gooberlib;

import com.goobercorp.gooberlib.asm.MethodResult;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;

class ReflectionUtil {

    static Method getMethod(ClassLoader classLoader, Class<?> owner, MethodResult methodResult) throws ClassNotFoundException, NoSuchMethodException {
        Type[] paramTypes = Type.getArgumentTypes(methodResult.descriptor());
        Class<?>[] paramClasses = new Class[paramTypes.length];

        for (int i = 0; i < paramClasses.length; i++) {
            paramClasses[i] = asmType2Class(classLoader, paramTypes[i]);
        }

        return owner.getMethod(methodResult.name(), paramClasses);
    }

    static Class<?> asmType2Class(ClassLoader classLoader, Type type) throws ClassNotFoundException {
        return switch (type.getSort()) {
            case Type.VOID -> void.class;
            case Type.BOOLEAN -> boolean.class;
            case Type.CHAR -> char.class;
            case Type.BYTE -> byte.class;
            case Type.SHORT -> short.class;
            case Type.INT -> int.class;
            case Type.FLOAT -> float.class;
            case Type.LONG -> long.class;
            case Type.DOUBLE -> double.class;
            case Type.ARRAY, Type.OBJECT -> Class.forName(type.getClassName(), false, classLoader);

            default -> throw new IllegalArgumentException();
        };
    }

}
