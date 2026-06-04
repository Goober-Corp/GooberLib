package com.goobercorp.gooberlib.option.individual.misc;

import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class LabelOption extends DummyOption<LabelOption> {
	public LabelOption(Component label, @Nullable WidgetProvider<LabelOption> provider) {
		super(label, _ -> Component.empty(), provider);
	}

	public LabelOption(Component label) {
		this(label, null);
	}

	public LabelOption(String label) {
		this(Component.nullToEmpty(label), null);
	}
}
