package com.goobercorp.gooberlib.builder.v3;

import java.util.ArrayList;
import java.util.List;

public record OptionContext<P>(P parent, Option<?> option,
                               List<OptionContext<?>> childOptions) implements OptionHolderV3 {
	public OptionContext(P parent, Option<?> option) {
		this(parent, option, new ArrayList<>());
	}

	public OptionContext<P> child(Option<?> option) {
		childOptions.add(new OptionContext<>(this, option));
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

	public OptionContext<P> children(Option<?>... options) {
		for (Option<?> option : options) child(option);

		return this;
	}

	public P build() {
		return parent;
	}
}
