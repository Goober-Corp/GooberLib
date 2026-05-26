package com.goobercorp.gooberlib.option.individual.misc;

import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ButtonOption extends DummyOption<ButtonOption> {
	private final Runnable r;

	public ButtonOption(Text name, Function<ButtonOption, Text> description, Runnable r, @Nullable WidgetProvider<ButtonOption> provider) {
		super(name, description, provider);
		this.r = r;
	}

	public ButtonOption(Text name, Text description, Runnable r) {
		this(name, _ -> description, r, null);
	}

	public ButtonOption(String name, String description, Runnable r) {
		this(Text.of(name), _ -> Text.of(description), r, null);
	}

	public void execute() {
		r.run();
	}
}
