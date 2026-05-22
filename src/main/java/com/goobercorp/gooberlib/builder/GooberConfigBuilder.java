package com.goobercorp.gooberlib.builder;

import com.goobercorp.gooberlib.builder.category.CategoryBuilder;
import com.goobercorp.gooberlib.builder.category.ConfigCategory;
import com.goobercorp.gooberlib.screen.GooberScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.apache.commons.lang3.function.TriFunction;

import java.util.ArrayList;
import java.util.List;

public class GooberConfigBuilder {
	private final Text title;
	private final List<ConfigCategory> categories = new ArrayList<>();
	private TriFunction<BuiltConfig, Screen, String, GooberScreen> screenSupplier = GooberScreen::new;

	public GooberConfigBuilder(Text title) {
		this.title = title;
	}

	/**
	 * Returns (but does not yet register) a {@link CategoryBuilder} for building a new category
	 *
	 * @param name        the category name
	 * @param description the category description
	 * @return a new category builder
	 */
	public CategoryBuilder category(Text name, Text description) {
		return new CategoryBuilder(this, name, description);
	}

	/**
	 * Returns (but does not yet register) a {@link CategoryBuilder} for building a new category
	 *
	 * @param name        the category name
	 * @param description the category description
	 * @return a new category builder
	 */
	public CategoryBuilder category(String name, String description) {
		return category(Text.literal(name), Text.literal(description));
	}


	/**
	 * Returns (but does not yet register) a {@link CategoryBuilder} for building a new category
	 *
	 * @param name the category name
	 * @return a new category builder
	 */
	public CategoryBuilder category(Text name) {
		return category(name, Text.empty());
	}

	/**
	 * Returns (but does not yet register) a {@link CategoryBuilder} for building a new category
	 *
	 * @param name the category name
	 * @return a new category builder
	 */
	public CategoryBuilder category(String name) {
		return category(Text.of(name));
	}

	/**
	 * Sets a custom screen supplier for this config
	 *
	 * @param screenSupplier the screen supplier
	 * @return this
	 */
	// todo: test this, though should just work
	public GooberConfigBuilder screenSupplier(TriFunction<BuiltConfig, Screen, String, GooberScreen> screenSupplier) {
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

	public static GooberConfigBuilder create(String title) {
		return new GooberConfigBuilder(Text.of(title));
	}

	public static GooberConfigBuilder create(Text title) {
		return new GooberConfigBuilder(title);
	}

	public static GooberConfigBuilder ofCategories(Text title, ConfigCategory... categories) {
		GooberConfigBuilder gooberConfigBuilder = create(title);
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
		return this.makeBuiltCategory(clazz, Text.of(name), Text.of(description));
	}

	public GooberConfigBuilder makeBuiltCategory(Class<?> clazz, String name) {
		return this.makeBuiltCategory(clazz, Text.of(name), Text.empty());
	}
}
