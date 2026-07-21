package com.goobercorp.gooberlib.option;

import com.goobercorp.gooberlib.builder.misc.OptionHolder;
import com.goobercorp.gooberlib.builder.section.ConfigSection;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record OptionContext<P>(P parent, Option<?> option,
                               List<OptionHolder> childOptions) implements OptionHolder {
	public OptionContext(P parent, Option<?> option) {
		this(parent, option, new ArrayList<>());
	}

	// todo check if this can be removed yet
	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;

		OptionContext<?> that = (OptionContext<?>) o;
		return Objects.equals(parent, that.parent) && Objects.equals(option, that.option) && Objects.equals(childOptions, that.childOptions);
	}

	@Override
	public int hashCode() {
		int result = Objects.hashCode(parent);
		result = 31 * result + Objects.hashCode(option);
		return result;
	}

	@Override
	public @NonNull String toString() {
		return "OptionContext{" +
				"option=" + option +
				", parent=" + parent +
				'}';
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
}
