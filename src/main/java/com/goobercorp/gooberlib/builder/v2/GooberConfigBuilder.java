package com.goobercorp.gooberlib.builder.v2;

import com.goobercorp.gooberlib.builder.BuiltConfig;
import com.goobercorp.gooberlib.builder.ConfigCategory;
import com.google.gson.GsonBuilder;
import net.minecraft.text.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

    /* metadata methods template

    public CLASSNAME name(Text name) {
        this.name = name;
        return this;
    }

    public CLASSNAME name(String name) {
        return name(Text.literal(name));
    }

    public CLASSNAME nameTranslation(String key) {
        return name(Text.translatable(key));
    }

    public CLASSNAME description(Text description) {
        this.description = description;
        return this;
    }

    public CLASSNAME description(String description) {
        return description(Text.literal(description));
    }

    public CLASSNAME descriptionTranslation(String key) {
        return description(Text.translatable(key));
    }

     */

public class GooberConfigBuilder {
    private Text title;

    private final List<ConfigCategory> categories = new ArrayList<>();
    private final Class<?> configClass;

    private final GsonBuilder gsonBuilder = new GsonBuilder();

    public GooberConfigBuilder() {
        this.configClass = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE)
                .walk(frames -> frames.skip(2)
                        .findFirst()
                        .orElseThrow()
                        .getDeclaringClass());
    }

    public GooberConfigBuilder title(Text title) {
        this.title = title;
        return this;
    }

    void addCategory(ConfigCategory category) {
        this.categories.add(category);
    }

    Class<?> getConfigClass() {
        return configClass;
    }

    public CategoryBuilder category() {
        return new CategoryBuilder(this);
    }

    public CategoryBuilder category(Text name) {
        return category().name(name);
    }

    public GooberConfigBuilder typeAdapter(Type type, Object adapter) {
        gsonBuilder.registerTypeAdapter(type, adapter);
        return this;
    }

    public BuiltConfig build() {
        return new BuiltConfig(gsonBuilder.create(), title, categories);
    }

    public static GooberConfigBuilder create() {
        return new GooberConfigBuilder();
    }
}
