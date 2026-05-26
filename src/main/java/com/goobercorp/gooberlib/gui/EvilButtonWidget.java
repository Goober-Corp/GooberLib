package com.goobercorp.gooberlib.gui;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.option.individual.misc.ButtonOption;
import net.minecraft.client.gui.Click;
import net.minecraft.text.Text;

import java.util.function.Function;

public class EvilButtonWidget extends EvilBaseWidget {
	private final ButtonOption opt;

	public EvilButtonWidget(ButtonOption opt, int x, int y, int width, int height, Function<BaseOption<?>, Text> valueFormatter) {
		super(opt, x, y, width, height, BaseOption::name);
		this.opt = opt;
	}

	@Override
	public boolean mouseClicked(Click click, boolean bl) {
		opt.execute();
		return super.mouseClicked(click, bl);
	}

	public EvilButtonWidget(ButtonOption opt, int x, int y, int width, int height) {
		this(opt, x, y, width, height, BaseOption::name);
	}

}
