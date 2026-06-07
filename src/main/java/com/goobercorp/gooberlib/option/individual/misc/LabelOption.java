package com.goobercorp.gooberlib.option.individual.misc;

import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import org.jetbrains.annotations.Nullable;

public class LabelOption extends DummyOption<LabelOption> {
	public LabelOption(CharSequence label, @Nullable WidgetProvider<LabelOption> provider) {
		super(label, _ -> "", provider);
	}

	public LabelOption(CharSequence label) {
		this(label, null);
	}
}
