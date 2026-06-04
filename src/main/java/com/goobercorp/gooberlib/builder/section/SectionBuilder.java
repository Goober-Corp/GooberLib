package com.goobercorp.gooberlib.builder.section;

import com.goobercorp.gooberlib.builder.misc.Metadata;
import com.goobercorp.gooberlib.builder.category.CategoryBuilder;
import com.goobercorp.gooberlib.option.Option;
import com.goobercorp.gooberlib.option.OptionContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;

public class SectionBuilder {
	@Nullable
	private final CategoryBuilder parent;

	private final List<OptionContext<?>> options = new ArrayList<>();

	private final Component name;
	private final Component description;


	public SectionBuilder(@Nullable CategoryBuilder parent, Component name, Component description) {
		this.parent = parent;
		this.name = name;
		this.description = description;
	}


	/**
	 * Registers {@code option} to this section
	 *
	 * @param option the option
	 * @return the options {@link OptionContext} for adding children
	 */
	public OptionContext<SectionBuilder> option(Option<?> option) {
		OptionContext<SectionBuilder> optionContext = new OptionContext<>(this, option);
		options.add(optionContext);
		return optionContext;
	}

	/**
	 * Registers {@code option} to this section, and registers {@code childOptions} to it as children
	 *
	 * @param option       the option
	 * @param childOptions its child options
	 * @return this
	 */
	public SectionBuilder optionWithChildren(Option<?> option, Option<?>... childOptions) {
		return this.option(option).children(childOptions).build();
	}

	/**
	 * Registers all {@link Option}s in {@code options} to this category
	 *
	 * @param options the options
	 * @return this
	 */
	public SectionBuilder options(Option<?>... options) {
		for (Option<?> option : options) {
			option(option);
		}
		return this;
	}

	/**
	 * Builds and registers this section to the parent
	 *
	 * @return the parent
	 */
	public CategoryBuilder build() {
		if (parent == null)
			throw new IllegalStateException("If you are trying to make a ConfigSection, use buildSection() instead!");
		parent.addBuiltSection(new ConfigSection(new Metadata(name, description), options));
		return parent;
	}

	/**
	 * Builds this {@link ConfigSection}
	 *
	 * @return the built {@link ConfigSection}
	 */
	public ConfigSection buildSection() {
		return new ConfigSection(new Metadata(name, description), options);
	}

	/**
	 * Allows you to build an option programmatically
	 * <p>
	 * For example:
	 * <pre><code>
	 * builder.option(myIntOption, option -> {
	 *         for (int i = 0; i < 5; i++) {
	 *             option.child(new IntOption("option " + i, "description");
	 *         }
	 *     }
	 * );
	 * </code></pre>
	 *
	 * @param option                the option
	 * @param optionContextConsumer the option builder
	 * @return this
	 */
	public SectionBuilder option(Option<?> option, Consumer<OptionContext<SectionBuilder>> optionContextConsumer) {
		var optionContext = this.option(option);
		optionContextConsumer.accept(optionContext);
		return optionContext.build();
	}
}
