package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.EvilBaseWidget;
import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.option.individual.misc.ButtonOption;
import com.goobercorp.gooberlib.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.function.Function;

import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;


public class EvilButtonWidget extends EvilBaseWidget {
	private final Runnable r;

	//TODO: add "execute" text field like in YACL
	//|<name>                          "EXECUTE"|
	public EvilButtonWidget(ButtonOption opt, int x, int y, int width, int height, Function<BaseOption<?>, Component> valueFormatter) {
		super(opt.name(), x, y, width, height, valueFormatter);
		r = opt.getRunnable();
	}

	public EvilButtonWidget(ButtonOption opt, int x, int y, int width, int height) {
		this(opt, x, y, width, height, BaseOption::name);
	}

	public EvilButtonWidget(ButtonOption opt, int x, int y, int width, int height, boolean centerName) {
		this(opt, x, y, width, height, BaseOption::name);
		this.centerName = centerName;
	}

	public EvilButtonWidget(CharSequence name, Runnable r, int x, int y, int width, int height, Function<BaseOption<?>, Component> valueFormatter) {
		super(Util.fromChars(name), x, y, width, height, valueFormatter);
		this.r = r;
	}

	public EvilButtonWidget(CharSequence name, Runnable r, int x, int y, int width, int height, boolean centerName) {
		super(Util.fromChars(name), x, y, width, height, BaseOption::name);
		this.r = r;
		this.centerName = centerName;
	}

	@Override
	protected void drawText(GuiGraphics drawContext) {
		newMatrixScope(drawContext, stack -> {
			if (this.centerName) {
				stack.scaleAround(1 - clickTweener.getF() * 0.25F, this.getX() + this.getWidth() / 2F, Minecraft.getInstance().font.lineHeight / 2F + 2.5F);
				drawContext.drawCenteredString(Minecraft.getInstance().font, name, this.getX() + this.getWidth() / 2, this.getY() + this.height / 2 - Minecraft.getInstance().font.lineHeight / 2, MainConfig.primaryCol);
			} else {
				stack.scaleAround(1 - clickTweener.getF() * 0.25F, Minecraft.getInstance().font.width(name) / 2F, Minecraft.getInstance().font.lineHeight / 2F);
				drawContext.drawString(Minecraft.getInstance().font, name, this.getX() + 5, this.getY() + this.height / 2 - Minecraft.getInstance().font.lineHeight / 2, MainConfig.primaryCol);
			}
		});
	}

	@Override
	public void onClick(MouseButtonEvent click, boolean bl) {
		r.run();
	}
}
