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

	// TODO: remove
	private final Class<?> configClass;

	private final GsonBuilder gsonBuilder = new GsonBuilder().setPrettyPrinting();

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

	public GooberConfigBuilder title(String title) {
		return title(Text.literal(title));
	}

	void addCategory(ConfigCategory category) {
		this.categories.add(category);
	}

	Class<?> getConfigClass() {
		return configClass;
	}

	public CategoryBuilder category(String name, String description) {
		return category(Text.literal(name), Text.literal(description));
	}

	public CategoryBuilder category(Text name, Text description) {
		return new CategoryBuilder(this)
				.name(name)
				.description(description);
	}

	public CategoryBuilder category(Text name) {
		return category(name, Text.empty());
	}

	public GooberConfigBuilder addBuiltCategory(ConfigCategory category) {
		addCategory(category);
		return this;
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

	public static GooberConfigBuilder ofCategories(Text title, ConfigCategory... categories) {
		GooberConfigBuilder gooberConfigBuilder = create().title(title);
		for (ConfigCategory category : categories) {
			gooberConfigBuilder.addBuiltCategory(category);
		}
		return gooberConfigBuilder;
	}

	public static GooberConfigBuilder ofCategories(String title, ConfigCategory... categories) {
		return ofCategories(Text.of(title), categories);
	}

	public GooberConfigBuilder makeBuiltCategory(Class<?> clazz, Text name, Text description) {
		return this.addBuiltCategory(ConfigCategory.ofClass(clazz, name, description));
	}

	public GooberConfigBuilder makeBuiltCategory(Class<?> clazz, String name, String description) {
		return this.makeBuiltCategory(clazz, Text.literal(name), Text.literal(description));
	}

	public GooberConfigBuilder makeBuiltCategory(Class<?> clazz, String name) {
		return this.makeBuiltCategory(clazz, name, "");
	}
}
