package com.goobercorp.gooberlib.option.individual.misc;

import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class ButtonOption extends DummyOption<ButtonOption> {
	private final Runnable r;

	public ButtonOption(CharSequence name, Function<ButtonOption, CharSequence> description, Runnable r, @Nullable WidgetProvider<ButtonOption> provider) {
		super(name, description, provider);
		this.r = r;
	}

	public ButtonOption(CharSequence name, CharSequence description, Runnable r) {
		this(name, _ -> description, r, null);
	}

	public ButtonOption(String name, String description, Runnable r) {
		this(name, _ -> description, r, null);
	}

	public ButtonOption(CharSequence name, Runnable r) {
		this(name, _ -> "", r, null);
	}

	public void execute() {
		r.run();
	}
}
