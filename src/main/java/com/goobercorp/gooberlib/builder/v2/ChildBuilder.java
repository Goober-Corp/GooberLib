package com.goobercorp.gooberlib.builder.v2;

import com.goobercorp.gooberlib.builder.ConfigOption;

import java.util.ArrayList;
import java.util.List;

public class ChildBuilder<P> {
    private final Class<?> configClass;
    private final OptionBuilder<P> parent;

    private final List<ConfigOption> children = new ArrayList<>();

    ChildBuilder(Class<?> configClass, OptionBuilder<P> parent) {
        this.configClass = configClass;
        this.parent = parent;
    }

    public OptionBuilder<ChildBuilder<P>> child(String fieldName) {
        return new OptionBuilder<>(this, configClass,
                new OptionAccessors.FieldAccessors(configClass, fieldName), children::add);
    }

    public OptionBuilder<P> build() {
        parent.addChildren(children);
        return parent;
    }

}
