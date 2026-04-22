package com.goobercorp.gooberlib.builder.v2;

import com.goobercorp.gooberlib.builder.ConfigOption;
import com.goobercorp.gooberlib.builder.ConfigSection;
import com.goobercorp.gooberlib.builder.MetadataHolder;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class SectionBuilder {
    private final CategoryBuilder parent;
    private final SectionAppender appender;

    private final List<ConfigOption> options = new ArrayList<>();

    private Text name;
    private Text description;


    public SectionBuilder(CategoryBuilder parent, SectionAppender appender) {
        this.parent = parent;
        this.appender = appender;
    }

    public OptionBuilder<SectionBuilder> option(String fieldName) {
        return new OptionBuilder<>(this, parent.getParent().getConfigClass(),
                new OptionAccessors.FieldAccessors(parent.getParent().getConfigClass(), fieldName), options::add);
    }


    public CategoryBuilder build() {
        appender.append(new ConfigSection(new MetadataHolder.Metadata(name, description), options));
        return parent;
    }


    public SectionBuilder name(Text name) {
        this.name = name;
        return this;
    }

    public SectionBuilder name(String name) {
        return name(Text.literal(name));
    }

    public SectionBuilder nameTranslation(String key) {
        return name(Text.translatable(key));
    }

    public SectionBuilder description(Text description) {
        this.description = description;
        return this;
    }

    public SectionBuilder description(String description) {
        return description(Text.literal(description));
    }

    public SectionBuilder descriptionTranslation(String key) {
        return description(Text.translatable(key));
    }
    
}
