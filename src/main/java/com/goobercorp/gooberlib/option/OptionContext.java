package com.goobercorp.gooberlib.option;

import com.goobercorp.gooberlib.builder.misc.OptionHolder;
import com.goobercorp.gooberlib.builder.section.ConfigSection;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record OptionContext<P>(P parent, Option<?> option,
                               List<OptionHolder> childOptions) implements OptionHolder {
	public OptionContext(P parent, Option<?> option) {
		this(parent, option, new ArrayList<>());
	}

	public OptionContext<P> child(Option<?> option) {
		childOptions.add(new OptionContext<>(this, option));
		return this;
	}

	public OptionContext<P> child(ConfigSection section) {
		childOptions.add(section);
		return this;
	}

	/**
	 * @return an OptionContext for the `option`
	 */
	public OptionContext<OptionContext<P>> nestedChild(Option<?> option) {
		var optionContext = new OptionContext<>(this, option);
		childOptions.add(optionContext);
		return optionContext;
	}

	public OptionContext<P> childWithChildren(Option<?> child, Option<?>... itsChildren) {
		return this.nestedChild(child).children(itsChildren).build();
	}

	public OptionContext<P> children(Option<?>... options) {
		for (Option<?> option : options) child(option);

		return this;
	}


	public OptionContext<P> children(ConfigSection... sections) {
		for (ConfigSection section : sections) child(section);

		return this;
	}

	public P build() {
		return parent;
	}

	@Override
	public int hashCode() {
		return Objects.hash(option, childOptions);
	}
}
