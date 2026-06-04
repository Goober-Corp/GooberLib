package com.goobercorp.gooberlib.option.individual.misc;

import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import net.minecraft.network.chat.Component;

public class ButtonOption extends DummyOption<ButtonOption> {
	private final Runnable r;

	public ButtonOption(Component name, Function<ButtonOption, Component> description, Runnable r, @Nullable WidgetProvider<ButtonOption> provider) {
		super(name, description, provider);
		this.r = r;
	}

	public ButtonOption(Component name, Component description, Runnable r) {
		this(name, _ -> description, r, null);
	}

	public ButtonOption(String name, String description, Runnable r) {
		this(Component.nullToEmpty(name), _ -> Component.nullToEmpty(description), r, null);
	}

	public ButtonOption(String name, Runnable r) {
		this(Component.nullToEmpty(name), _ -> Component.empty(), r, null);
	}

	public void execute() {
		r.run();
	}
}
