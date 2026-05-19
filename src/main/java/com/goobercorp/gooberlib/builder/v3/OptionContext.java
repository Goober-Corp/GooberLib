package com.goobercorp.gooberlib.builder.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record OptionContext<P>(P parent, Option<?> option,
                               List<OptionContext<?>> childOptions) implements OptionHolderV3 {
    public OptionContext(P parent, Option<?> option) {
        this(parent, option, new ArrayList<>());
    }

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
    public String toString() {
        return "OptionContext{" +
                "option=" + option +
                ", parent=" + parent +
                '}';
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
