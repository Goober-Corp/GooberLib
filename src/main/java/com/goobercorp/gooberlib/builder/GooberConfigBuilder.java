package com.goobercorp.gooberlib.builder;

import net.minecraft.text.Text;

import java.lang.reflect.Type;

public interface GooberConfigBuilder {

    GooberConfigBuilder title(Text title);

    GooberConfigBuilder typeAdapter(Type type, Object typeAdapter);

    GooberCategoryBuilder category();

    BuiltConfig build();

    static GooberConfigBuilder create() {
        return new ConfigBuilderImpl();
    }

}
