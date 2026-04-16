package com.goobercorp.gooberlib.builder;

import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record ConfigField(@NonNull String category, @Nullable String section) {
}
