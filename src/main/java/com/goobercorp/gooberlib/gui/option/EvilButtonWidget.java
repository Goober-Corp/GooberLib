package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.EvilBaseWidget;
import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.option.individual.misc.ButtonOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.function.Function;

import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;


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
	protected void drawText(GuiGraphics drawContext) {
		newMatrixScope(drawContext, stack -> {
			Component yeah = opt.name();
			stack.translate(this.getX() + this.getWidth() / 2F, this.getY() + this.height / 2F - Minecraft.getInstance().font.lineHeight / 2);
			stack.scaleAround(1 - clickTweener.getF() * 0.25F, Minecraft.getInstance().font.width(yeah) / 2F, 5F);
			drawContext.drawString(Minecraft.getInstance().font, yeah, 0, 0, MainConfig.primaryCol);
		});
	}

	@Override
	public void onClick(MouseButtonEvent click, boolean bl) {
		opt.execute();
	}
}
