package com.goobercorp.gooberlib.gui;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.option.individual.misc.ButtonOption;
import net.minecraft.client.gui.Click;
import net.minecraft.text.Text;

import java.util.function.Function;


public class EvilButtonWidget extends EvilBaseWidget {
	private final ButtonOption opt;

	public EvilButtonWidget(ButtonOption opt, int x, int y, int width, int height, Function<BaseOption<?>, Text> valueFormatter) {
		super(opt.name(), x, y, width, height, valueFormatter);
		this.opt = opt;
	}

	public EvilButtonWidget(ButtonOption opt, int x, int y, int width, int height) {
		this(opt, x, y, width, height, BaseOption::name);
	}

	@Override
	public void onClick(Click click, boolean bl) {
		opt.execute();
	}
}
