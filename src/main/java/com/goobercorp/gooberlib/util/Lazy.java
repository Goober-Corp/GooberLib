package com.goobercorp.gooberlib.util;

import com.goobercorp.gooberlib.interfaces.ThrowingSupplier;

import java.util.function.Supplier;

import static org.apache.commons.lang3.function.Failable.rethrow;

public class Lazy<T> implements Supplier<T> {

    private final ThrowingSupplier<T> supplier;
    private T value;

    public Lazy(ThrowingSupplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public T get() {
        if (value == null) {
            try {
                value = supplier.get();
            } catch (Throwable e) {
                throw rethrow(e);
            }
        }
        return value;
    }
}
