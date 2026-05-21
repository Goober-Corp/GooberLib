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

	public OptionContext<CategoryBuilder> option(Option<?> option) {
		OptionContext<CategoryBuilder> optionContext = new OptionContext<>(this, option);
		elements.add(optionContext);
		return optionContext;
	}

	public CategoryBuilder optionWithChildren(Option<?> option, Option<?>... childOptions) {
		this.option(option).children(childOptions);
		return this;
	}

	public CategoryBuilder options(Option<?>... options) {
		for (Option<?> option : options) option(option);
		return this;
	}

	public SectionBuilder section(Text name, Text description) {
		return new SectionBuilder(this, elements::add, name, description);
	}

	public SectionBuilder section(String name, String description) {
		return section(Text.literal(name), Text.literal(description));
	}

	public SectionBuilder section(Text name) {
		return section(name, Text.empty());
	}

	public SectionBuilder section(String name) {
		return section(Text.of(name), Text.empty());
	}

	public GooberConfigBuilder build() {
		if (parent == null)
			throw new IllegalStateException("If you're trying to make a ConfigCategory, use buildCategory() instead!");
		parent.addCategory(new ConfigCategory(new Metadata(name, description), elements));
		return parent;
	}

	public ConfigCategory buildCategory() {
		return new ConfigCategory(new Metadata(name, description), elements);
	}

	public GooberConfigBuilder getParent() {
		if (parent == null)
			throw new IllegalStateException("If you're trying to make a ConfigCategory, use buildCategory() instead!");
		return parent;
	}

	public CategoryBuilder sectionMaker(String name, String description, Consumer<SectionBuilder> categoryBuilderConsumer) {
		SectionBuilder sectionBuilder = this.section(name, description);
		categoryBuilderConsumer.accept(sectionBuilder);
		return sectionBuilder.build();
	}
}
