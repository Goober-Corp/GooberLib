package com.goobercorp.gooberlib.builder;

public interface GooberFieldBuilder<T extends OptionHolder<T>> extends MetadataHolder<GooberFieldBuilder<T>> {

    GooberChildAppender<T> withChildren();

    T build();

}
