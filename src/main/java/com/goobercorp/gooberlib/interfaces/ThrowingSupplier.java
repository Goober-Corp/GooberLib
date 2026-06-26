package com.goobercorp.gooberlib.interfaces;

public interface ThrowingSupplier<T> {

    T get() throws Throwable;

}
