package com.goobercorp.gooberlib.builder.v3;

import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;

public class SimpleIntOption extends ValueOption<Integer> {
    public SimpleIntOption(Text name, Text description) {
        super(name, description);
    }

    public SimpleIntOption(String name, String description) {
        super(name, description);
    }

    @Override
    public <T> T serialize(DynamicOps<T> ops) {
        return ops.createInt(value);
    }

    @Override
    public <T> void deserialize(DynamicOps<T> ops, T object) {
        this.value = ops.getNumberValue(object)
                .getOrThrow()
                .intValue();
    }
}
