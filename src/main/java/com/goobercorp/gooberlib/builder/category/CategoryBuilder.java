package com.goobercorp.gooberlib.builder.category;

import com.goobercorp.gooberlib.builder.GooberConfigBuilder;
import com.goobercorp.gooberlib.builder.misc.Metadata;
import com.goobercorp.gooberlib.builder.misc.OptionHolder;
import com.goobercorp.gooberlib.builder.section.SectionBuilder;
import com.goobercorp.gooberlib.option.Option;
import com.goobercorp.gooberlib.option.OptionContext;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CategoryBuilder {
	private final Text name;
	private final Text description;

	private final List<OptionHolder> elements = new ArrayList<>();

	@Nullable
	private final GooberConfigBuilder parent;

	public CategoryBuilder(@Nullable GooberConfigBuilder parent, Text name, Text description) {
		this.parent = parent;
		this.name = name;
		this.description = description;
	}

	/**
	 * Registers {@code option} to this category
	 *
	 * @param option the option
	 * @return the options {@link OptionContext} for adding children
	 */
	public OptionContext<CategoryBuilder> option(Option<?> option) {
		OptionContext<CategoryBuilder> optionContext = new OptionContext<>(this, option);
		elements.add(optionContext);
		return optionContext;
	}

	/**
	 * Registers {@code option} to this category, and registers {@code childOptions} to it as children
	 *
	 * @param option       the option
	 * @param childOptions its child options
	 * @return this
	 */
	public CategoryBuilder optionWithChildren(Option<?> option, Option<?>... childOptions) {
		this.option(option).children(childOptions);
		return this;
	}

	/**
	 * Registers all {@link Option}s in {@code options} to this category
	 *
	 * @param options the options
	 * @return this
	 */
	public CategoryBuilder options(Option<?>... options) {
		for (Option<?> option : options) option(option);
		return this;
	}

	/**
	 * Returns (but does not yet register) a {@link SectionBuilder} for building a new section
	 *
	 * @param name        the name
	 * @param description the description
	 * @return the {@link SectionBuilder}
	 */
	public SectionBuilder section(Text name, Text description) {
		return new SectionBuilder(this, elements::add, name, description);
	}

	/**
	 * Returns (but does not yet register) a {@link SectionBuilder} for building a new section
	 *
	 * @param name        the name
	 * @param description the description
	 * @return the {@link SectionBuilder}
	 */
	public SectionBuilder section(String name, String description) {
		return section(Text.literal(name), Text.literal(description));
	}

	/**
	 * Returns (but does not yet register) a {@link SectionBuilder} for building a new section
	 *
	 * @param name the name
	 * @return the {@link SectionBuilder}
	 */
	public SectionBuilder section(Text name) {
		return section(name, Text.empty());
	}

	/**
	 * Returns (but does not yet register) a {@link SectionBuilder} for building a new section
	 *
	 * @param name the name
	 * @return the {@link SectionBuilder}
	 */
	public SectionBuilder section(String name) {
		return section(Text.of(name), Text.empty());
	}

	// todo: be able to register/make a built section

	/**
	 * Builds and registers this category to the parent
	 *
	 * @return the parent
	 */
	public GooberConfigBuilder build() {
		if (parent == null)
			throw new IllegalStateException("If you're trying to make a ConfigCategory, use buildCategory() instead!");
		parent.addBuiltCategory(new ConfigCategory(new Metadata(name, description), elements));
		return parent;
	}

	/**
	 * Builds this category
	 *
	 * @return the built category
	 */
	public ConfigCategory buildCategory() {
		return new ConfigCategory(new Metadata(name, description), elements);
	}

	/**
	 * Returns the parent
	 *
	 * @return the parent
	 */
	public GooberConfigBuilder getParent() {
		if (parent == null)
			throw new IllegalStateException("If you're trying to make a ConfigCategory, use buildCategory() instead!");
		return parent;
	}

	/**
	 * Allows you to build a section programmatically
	 * <p>
	 * For example:
	 * <pre><code>
	 * builder.sectionMaker("General", "General settings", sectionBuilder -> {
	 *         for (int i = 0; i < 5; i++) {
	 *             sectionBuilder.option(new IntOption("option " + i, "description");
	 *         }
	 *     }
	 * );
	 * </code></pre>
	 *
	 * @param name                    the name
	 * @param description             the description
	 * @param categoryBuilderConsumer the category builder
	 * @return this
	 */
	public CategoryBuilder sectionMaker(String name, String description, Consumer<SectionBuilder> categoryBuilderConsumer) {
		SectionBuilder sectionBuilder = this.section(name, description);
		categoryBuilderConsumer.accept(sectionBuilder);
		return sectionBuilder.build();
	}
}
