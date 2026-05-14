package com.goobercorp.gooberlib.builder.v3;

import com.mojang.serialization.DynamicOps;
import net.minecraft.text.Text;

public interface Option extends OptionHolderV3 {
    <T> T serialize(DynamicOps<T> ops);
    <T> void deserialize(DynamicOps<T> ops, T object);

    default void onChange() {}

    Text name();
    Text description();
}
