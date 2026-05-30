package com.goobercorp.gooberlib.gui;

import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.option.individual.misc.ButtonOption;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.function.Function;

import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

public class EvilButtonWidget extends EvilBaseWidget {
	private final ButtonOption opt;

	public EvilButtonWidget(ButtonOption opt, int x, int y, int width, int height, Function<BaseOption<?>, Text> valueFormatter) {
		super(opt.name(), x, y, width, height, valueFormatter);
		this.opt = opt;
	}

	@Override
	public boolean mouseClicked(Click click, boolean bl) {
		opt.execute();
		return super.mouseClicked(click, bl);
	}

	@Override
	protected void renderWidget(DrawContext drawContext, int i, int j, float f) {
		newMatrixScope(drawContext, stack -> {
			stack.translate(horizontalPosOffset, verticalPosOffset);
			super.renderWidget(drawContext, i, j, f);
		});
	}

	public EvilButtonWidget(ButtonOption opt, int x, int y, int width, int height) {
		this(opt, x, y, width, height, BaseOption::name);
	}

}
