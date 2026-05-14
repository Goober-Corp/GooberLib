package com.goobercorp.gooberlib.builder.v2;

import com.goobercorp.gooberlib.builder.ConfigOption;
import com.goobercorp.gooberlib.builder.ConfigSection;
import com.goobercorp.gooberlib.builder.MetadataHolder;
import com.goobercorp.gooberlib.builder.v3.Option;
import com.goobercorp.gooberlib.builder.v3.OptionContext;
import com.goobercorp.gooberlib.builder.v3.OptionHolderV3;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class SectionBuilder {
    private final CategoryBuilder parent;
    private final SectionAppender appender;

    private final List<OptionHolderV3> options = new ArrayList<>();

    private Text name;
    private Text description;


    public SectionBuilder(CategoryBuilder parent, SectionAppender appender) {
        this.parent = parent;
        this.appender = appender;
    }

    public OptionContext<SectionBuilder> option(Option option) {
        OptionContext<SectionBuilder> optionContext = new OptionContext<>(this, option);
        options.add(optionContext);
        return optionContext;
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
