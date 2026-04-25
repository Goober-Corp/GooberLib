package com.goobercorp.gooberlib.builder.v2;

import com.goobercorp.gooberlib.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.function.Consumer;
import java.util.function.Supplier;

public interface OptionAccessors {

    Supplier<?> getter();
    Consumer<?> setter();

    Type type();

    class FieldAccessors implements OptionAccessors {
        private final Supplier<?> getter;
        private final Consumer<?> setter;
        private final Type type;

        FieldAccessors(Class<?> clazz, String fieldName) {
            try {
                Field field = clazz.getDeclaredField(fieldName);

                int modifiers = field.getModifiers();
                if (!Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) {
                    throw new IllegalArgumentException("Trying to create an accessor for a non-static or final field %s of class %s"
                            .formatted(fieldName, clazz.getName()));
                }

                field.setAccessible(true);

                this.getter = ReflectionUtil.staticFieldGetter(field);
                this.setter = ReflectionUtil.staticFieldSetter(field);
                this.type = field.getGenericType();
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Supplier<?> getter() {
            return getter;
        }

        @Override
        public Consumer<?> setter() {
            return setter;
        }

        @Override
        public Type type() {
            return type;
        }
    }

}
