package com.goobercorp.gooberlib.builder.v2;

import com.goobercorp.gooberlib.builder.ConfigCategory;
import com.goobercorp.gooberlib.builder.MetadataHolder;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class CategoryBuilder {
    private Text name;
    private Text description;

    private final List<Object> elements = new ArrayList<>();

    private final GooberConfigBuilder parent;

    CategoryBuilder(GooberConfigBuilder parent) {
        this.parent = parent;
    }

    public OptionBuilder<CategoryBuilder> option(String fieldName) {
        return new OptionBuilder<>(this, parent.getConfigClass(),
                new OptionAccessors.FieldAccessors(parent.getConfigClass(), fieldName), elements::add);
    }

    public SectionBuilder section() {
        return new SectionBuilder(this, elements::add);
    }

    public SectionBuilder section(Text name) {
        return section().name(name);
    }

    public CategoryBuilder name(Text name) {
        this.name = name;
        return this;
    }

    public CategoryBuilder name(String name) {
        return name(Text.literal(name));
    }

    public CategoryBuilder nameTranslation(String key) {
        return name(Text.translatable(key));
    }

    public CategoryBuilder description(Text description) {
        this.description = description;
        return this;
    }

    public CategoryBuilder description(String description) {
        return description(Text.literal(description));
    }

    public CategoryBuilder descriptionTranslation(String key) {
        return description(Text.translatable(key));
    }


    public GooberConfigBuilder build() {
        parent.addCategory(new ConfigCategory(new MetadataHolder.Metadata(name, description), elements));
        return parent;
    }

    GooberConfigBuilder getParent() {
        return parent;
    }

}
