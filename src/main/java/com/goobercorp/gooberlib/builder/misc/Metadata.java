package com.goobercorp.gooberlib.builder.misc;

import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;

public record Metadata(Text name, @Nullable Text description) {
}
