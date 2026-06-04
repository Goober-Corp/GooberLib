package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.gui.EvilBaseWidget;
import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.option.individual.misc.ButtonOption;
import java.util.function.Function;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;


public class EvilButtonWidget extends EvilBaseWidget {
	private final ButtonOption opt;

	public EvilButtonWidget(ButtonOption opt, int x, int y, int width, int height, Function<BaseOption<?>, Component> valueFormatter) {
		super(opt.name(), x, y, width, height, valueFormatter);
		this.opt = opt;
	}

	public EvilButtonWidget(ButtonOption opt, int x, int y, int width, int height) {
		this(opt, x, y, width, height, BaseOption::name);
	}

	@Override
	public void onClick(MouseButtonEvent click, boolean bl) {
		opt.execute();
	}
}
