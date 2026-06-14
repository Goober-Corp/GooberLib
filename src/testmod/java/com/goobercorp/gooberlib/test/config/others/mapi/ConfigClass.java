package com.goobercorp.gooberlib.test.config.others.mapi;

import com.goobercorp.gooberlib.gui.option.EvilButtonWidget;
import com.goobercorp.gooberlib.option.BaseOption;
import com.mojang.serialization.DynamicOps;

import java.util.function.Function;

public class ConfigClass extends BaseOption<ConfigClass> {
	protected ConfigClass(CharSequence name, Function<ConfigClass, CharSequence> description) {
		super(name, description, (theOption, x, y, width, height) -> new EvilButtonWidget(theOption.name, () -> System.out.println("Custom button!!"), x, y, width, height, false));
	}

	public ConfigClass(String name, String comment) {
		this(name, _ -> comment);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.empty();
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
	}
}
