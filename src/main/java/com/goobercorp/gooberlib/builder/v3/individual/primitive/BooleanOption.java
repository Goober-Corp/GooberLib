package com.goobercorp.gooberlib.builder.v3.individual.primitive;

import com.goobercorp.gooberlib.builder.v3.BaseOption;
import com.goobercorp.gooberlib.builder.v3.individual.java.StringOption;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;

public class BooleanOption extends BaseOption<StringOption> {
    private final boolean defaultValue;
    /// @implNote Modifying this value directly will *not* trigger
    public boolean value;

    public BooleanOption(Text name, Text description, boolean defaultValue, WidgetProvider provider) {
        super(name, description, provider);
        this.value = defaultValue;
        this.defaultValue = defaultValue;
    }

    public BooleanOption(String name, String description) {
        this(Text.literal(name), Text.literal(description), false, null);
    }

    public BooleanOption(String name, String description, WidgetProvider provider) {
        this(Text.literal(name), Text.literal(description), false, provider);
    }

    @Override
    public <S> S serialize(DynamicOps<S> ops) {
        return ops.createBoolean(value);
    }

    @Override
    public <S> void deserialize(DynamicOps<S> ops, S object) {
        this.value = ops.getBooleanValue(object)
                .getOrThrow();
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean newValue) {
        if (this.value != newValue) {
            this.value = newValue;
            this.onChange();
        }
    }


    public void resetToDefault() {
        if (this.value != this.defaultValue) {
            setValue(this.defaultValue);
        }
    }

    public boolean getDefaultValue() {
        return defaultValue;
    }
}
