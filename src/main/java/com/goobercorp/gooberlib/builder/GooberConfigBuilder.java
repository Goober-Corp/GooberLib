package com.goobercorp.gooberlib.builder;

import net.minecraft.text.Text;

import java.lang.reflect.Type;

public interface GooberConfigBuilder {

    GooberConfigBuilder title(Text title);

    GooberConfigBuilder typeAdapter(Type type, Object typeAdapter);

    GooberCategoryBuilder category();

    default GooberCategoryBuilder category(String name) {
        return category().name(name);
    }

    default GooberCategoryBuilder category(String name, String description) {
        return category(name).descriptionPlain(description);
    }

    default GooberCategoryBuilder category(Text name) {
        return category().name(name);
    }

    default GooberCategoryBuilder category(Text name, Text description) {
        return category(name).description(description);
    }

    BuiltConfig build();

    static GooberConfigBuilder create() {
        return new ConfigBuilderImpl();
    }

}
