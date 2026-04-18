package com.goobercorp.gooberlib.builder;

import net.minecraft.text.Text;

public interface GooberCategoryBuilder extends MetadataHolder<GooberCategoryBuilder>, OptionHolder<GooberCategoryBuilder> {

    GooberSectionBuilder section();

    default GooberSectionBuilder section(String name) {
        return section().name(name);
    }

    default GooberSectionBuilder section(String name, String description) {
        return section(name).descriptionPlain(description);
    }

    default GooberSectionBuilder section(Text name) {
        return section().name(name);
    }

    default GooberSectionBuilder section(Text name, Text description) {
        return section(name).description(description);
    }

    GooberConfigBuilder build();

}
