package com.goobercorp.gooberlib.builder.v2;

import com.goobercorp.gooberlib.builder.ConfigOption;
import com.goobercorp.gooberlib.builder.MetadataHolder;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class OptionBuilder<P> {
    private final P parent;
    private final Class<?> configClass;
    private final OptionAccessors accessors;
    private final OptionAppender appender;
    
    private Text name;
    private Text description;

    private final List<ConfigOption> children = new ArrayList<>();
    
    OptionBuilder(P parent, Class<?> configClass, OptionAccessors accessors, OptionAppender appender) {
        this.parent = parent;
        this.configClass = configClass;
        this.accessors = accessors;
        this.appender = appender;
    }

    public ChildBuilder<P> withChildren() {
        return new ChildBuilder<>(configClass, this);
    }

    public OptionBuilder<P> name(Text name) {
        this.name = name;
        return this;
    }

    public OptionBuilder<P> name(String name) {
        return name(Text.literal(name));
    }

    public OptionBuilder<P> nameTranslation(String key) {
        return name(Text.translatable(key));
    }

    public OptionBuilder<P> description(Text description) {
        this.description = description;
        return this;
    }

    public OptionBuilder<P> description(String description) {
        return description(Text.literal(description));
    }

    public OptionBuilder<P> descriptionTranslation(String key) {
        return description(Text.translatable(key));
    }

    void addChildren(List<ConfigOption> children) {
        this.children.addAll(children);
    }


    public P build() {
        appender.append(new ConfigOption(new MetadataHolder.Metadata(name, description),
                accessors.type(), accessors.getter(), accessors.setter(),
                children
        ));
        return parent;
    }

}
