package com.goobercorp.gooberlib.builder.v2;

import com.goobercorp.gooberlib.builder.ConfigSection;
import com.goobercorp.gooberlib.builder.MetadataHolder;
import com.goobercorp.gooberlib.builder.v3.Option;
import com.goobercorp.gooberlib.builder.v3.OptionContext;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SectionBuilder {
	private final CategoryBuilder parent;
	private final SectionAppender appender;

	private final List<OptionContext<?>> options = new ArrayList<>();

	private final Text name;
	private final Text description;


	public SectionBuilder(CategoryBuilder parent, SectionAppender appender, Text name, Text description) {
		this.parent = parent;
		this.appender = appender;
		this.name = name;
		this.description = description;
	}

	public OptionContext<SectionBuilder> option(Option<?> option) {
		OptionContext<SectionBuilder> optionContext = new OptionContext<>(this, option);
		options.add(optionContext);
		return optionContext;
	}

	public SectionBuilder options(Option<?>... options) {
		for (Option<?> option : options) {
			option(option);
		}
		return this;
	}


	public CategoryBuilder build() {
		appender.append(new ConfigSection(new MetadataHolder.Metadata(name, description), options));
		return parent;
	}

	public SectionBuilder optionMaker(Option<?> option, Consumer<OptionContext<SectionBuilder>> o) {
		var optionContext = this.option(option);
		o.accept(optionContext);
		return optionContext.build();
	}

	public SectionBuilder optionWithChildren(Option<?> option, Option<?>... children) {
		return this.option(option).children(children).build();
	}
}
