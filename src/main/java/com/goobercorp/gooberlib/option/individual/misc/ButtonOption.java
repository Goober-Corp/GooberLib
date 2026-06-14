package com.goobercorp.gooberlib.option.individual.misc;

import com.goobercorp.gooberlib.interfaces.WidgetProvider;

import java.util.function.Function;

public class ButtonOption extends DummyOption<ButtonOption> {
	private final Runnable r;

	public ButtonOption(CharSequence name, Function<ButtonOption, CharSequence> description, Runnable r, WidgetProvider<ButtonOption> provider) {
		super(name, description, provider);
		this.r = r;
	}

	public ButtonOption(CharSequence name, CharSequence description, Runnable r, WidgetProvider<ButtonOption> provider) {
		this(name, _ -> description, r, provider);
	}

	public ButtonOption(CharSequence name, CharSequence description, Runnable r) {
		this(name, _ -> description, r, null);
	}

	public ButtonOption(CharSequence name, Runnable r, WidgetProvider<ButtonOption> provider) {
		this(name, _ -> "", r, provider);
	}

	public ButtonOption(CharSequence name, Runnable r) {
		this(name, _ -> "", r, null);
	}

	public void execute() {
		r.run();
	}

	public Runnable getRunnable() {
		return r;
	}
}
