package com.goobercorp.gooberlib.builder.v3;

import java.util.ArrayList;
import java.util.List;

public record OptionContext<P>(P parent, OptionHolderV3 optionHolder, List<OptionHolderV3> childOptions) implements OptionHolderV3 {
    public OptionContext(P parent, OptionHolderV3 optionHolder) {
        this(parent, optionHolder, new ArrayList<>());
    }

    public OptionContext<P> child(Option option) {
        childOptions.add(option);
        return this;
    }

    /**
    @return an OptionContext for the `option`
     */
    public OptionContext<OptionContext<P>> editChild /*TODO: improve name buildChild? childContext?*/(Option option) {
        childOptions.add(option);
        return new OptionContext<>(this, option);
    }

    public OptionContext<P> children(Option... options) {
        for (Option option : options) child(option);

        return this;
    }

    public P build() {
        return parent;
    }
}
