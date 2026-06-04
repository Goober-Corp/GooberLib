package com.goobercorp.gooberlib.builder.misc;

import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

public record Metadata(Component name, @Nullable Component description) {
}
