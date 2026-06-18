package com.goobercorp.gooberlib.builder;

import com.goobercorp.gooberlib.builder.category.CategoryBuilder;
import com.goobercorp.gooberlib.builder.category.ConfigCategory;
import com.goobercorp.gooberlib.screen.GooberScreen;
import com.goobercorp.gooberlib.util.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.function.TriFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GooberConfigBuilder {
	private final Component title;
	private final List<ConfigCategory> categories = new ArrayList<>();
	private TriFunction<BuiltConfig, Screen, String, Screen> screenSupplier = GooberScreen::new;

	public GooberConfigBuilder(CharSequence title) {
		this.title = Util.fromChars(title);
	}

	/**
	 * Returns (but does not yet register) a {@link CategoryBuilder} for building a new category
	 *
	 * @param name        the category name
	 * @param description the category description
	 * @return a new category builder
	 */
	public CategoryBuilder category(CharSequence name, CharSequence description) {
		return new CategoryBuilder(this, name, description);
	}

	/**
	 * Returns (but does not yet register) a {@link CategoryBuilder} for building a new category
	 *
	 * @param name the category name
	 * @return a new category builder
	 */
	public CategoryBuilder category(CharSequence name) {
		return category(name, "");
	}

	/**
	 * Sets a custom screen supplier for this config
	 *
	 * @param screenSupplier the screen supplier
	 * @return this
	 */
	public GooberConfigBuilder screenSupplier(TriFunction<BuiltConfig, Screen, String, Screen> screenSupplier) {
		this.screenSupplier = screenSupplier;
		return this;
	}

	/**
	 * Adds a built category to this config
	 *
	 * @param category the category
	 */
	public GooberConfigBuilder addBuiltCategory(ConfigCategory category) {
		this.categories.add(category);
		return this;
	}

	public BuiltConfig build() {
		return new BuiltConfig(title, categories, screenSupplier);
	}

	public static GooberConfigBuilder create(CharSequence title, Consumer<GooberConfigBuilder> configBuilderConsumer) {
		var builder = create(title);
		configBuilderConsumer.accept(builder);
		return builder;
	}

	public static GooberConfigBuilder create(CharSequence title) {
		return new GooberConfigBuilder(title);
	}

	public static GooberConfigBuilder ofCategories(CharSequence title, ConfigCategory... categories) {
		GooberConfigBuilder gooberConfigBuilder = create(title);
		for (ConfigCategory category : categories) {
			gooberConfigBuilder.addBuiltCategory(category);
		}
		return gooberConfigBuilder;
	}

	public GooberConfigBuilder makeBuiltCategory(Class<?> clazz, CharSequence name, CharSequence description) {
		return this.addBuiltCategory(ConfigCategory.ofClass(clazz, name, description));
	}

	public GooberConfigBuilder makeBuiltCategory(Class<?> clazz, CharSequence name) {
		return this.makeBuiltCategory(clazz, name, "");
	}

	public GooberConfigBuilder category(CharSequence name, CharSequence description, Consumer<CategoryBuilder> categoryBuilderConsumer) {
		var builder = new CategoryBuilder(this, name, description);
		categoryBuilderConsumer.accept(builder);
		return builder.build();
	}

	public GooberConfigBuilder category(CharSequence name, Consumer<CategoryBuilder> categoryBuilderConsumer) {
		return category(name, "", categoryBuilderConsumer);
	}
}
