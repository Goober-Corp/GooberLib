package com.goobercorp.gooberlib.option.individual.minecraft;

import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.individual.misc.DummyOption;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class LabelOption extends DummyOption<LabelOption> {
	public LabelOption(Text label, @Nullable WidgetProvider<LabelOption> provider) {
		super(label, _ -> Text.empty(), provider);
	}

	public LabelOption(Text label) {
		this(label, null);
	}

	public LabelOption(String label) {
		this(Text.of(label), null);
	}
}
