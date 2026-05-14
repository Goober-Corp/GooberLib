package com.goobercorp.gooberlib.builder.v3;

import net.minecraft.text.Text;

public abstract class ValueOption<V> implements Option {
    public V value;

    protected Text name;
    protected Text description;

    protected ValueOption(Text name, Text description) {
        this.name = name;
        this.description = description;
    }

    protected ValueOption(String name, String description) {
        this(Text.literal(name), Text.literal(description));
    }

    public V get() {
        return value;
    }

    public void set(V value) {
        this.value = value;
        onChange();
    }

    @Override
    public Text name() {
        return name;
    }

    @SuppressWarnings("unchecked")
    public <O extends ValueOption<V>> O name(Text name) {
        this.name = name;
        return (O) this;
    }

    @Override
    public Text description() {
        return description;
    }

    @SuppressWarnings("unchecked")
    public <O extends ValueOption<V>> O description(Text description) {
        this.description = description;
        return (O) this;
    }
}
