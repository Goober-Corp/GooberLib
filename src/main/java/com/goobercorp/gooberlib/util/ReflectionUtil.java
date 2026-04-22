package com.goobercorp.gooberlib.util;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ReflectionUtil {

    public static Supplier<Object> staticFieldGetter(Field field) {
        return () -> {
            try {
                return field.get(null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static Consumer<Object> staticFieldSetter(Field field) {
        return o -> {
            try {
                field.set(null, o);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
    }

}
