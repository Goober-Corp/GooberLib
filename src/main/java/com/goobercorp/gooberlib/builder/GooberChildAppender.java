package com.goobercorp.gooberlib.builder;

public interface GooberChildAppender<T extends OptionHolder<T>> extends OptionHolder<GooberChildAppender<T>> {
    GooberFieldBuilder<GooberChildAppender<T>> child(String fieldName);

    T build();

}