package com.goobercorp.gooberlib.builder;

public interface OptionHolder<T extends OptionHolder<T>> {

    GooberFieldBuilder<T> field(String name);

}
