package com.goobercorp.gooberlib.builder;

import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;

public interface MetadataHolder<T extends MetadataHolder<T>> {

    T name(Text name);
    T description(Text description);

    default T name(String plainName) {
        return name(Text.literal(plainName));
    }

    default T nameTranslation(String key) {
        return name(Text.translatable(key));
    }

    default T descriptionPlain(String description) {
        return description(Text.literal(description));
    }

    default T descriptionTranslation(String key) {
        return description(Text.translatable(key));
    }

    record Metadata(Text name, @Nullable Text description) {}

}
