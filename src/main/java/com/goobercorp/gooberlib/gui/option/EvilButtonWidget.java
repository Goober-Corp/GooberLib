package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.gui.EvilBaseWidget;
import com.goobercorp.gooberlib.option.individual.misc.ButtonOption;
import com.goobercorp.gooberlib.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;

import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;


public class EvilButtonWidget extends EvilBaseWidget {
	private final Runnable r;

	//TODO: add "execute" text field like in YACL
	//|<name>                          "EXECUTE"|
	public EvilButtonWidget(ButtonOption opt, int x, int y, int width, int height) {
		this(opt, x, y, width, height, true);
	}

	public EvilButtonWidget(ButtonOption opt, int x, int y, int width, int height, boolean centerName) {
		this(opt.name(), opt.getRunnable(), x, y, width, height, centerName);
	}

	public EvilButtonWidget(CharSequence name, Runnable r, int x, int y, int width, int height) {
		this(name, r, x, y, width, height, true);
	}

	public EvilButtonWidget(CharSequence name, Runnable r, int x, int y, int width, int height, boolean centerName) {
		super(Util.fromChars(name), x, y, width, height);
		this.r = r;
		this.centerName = centerName;
	}

	@Override
	protected void drawText(GuiGraphics drawContext) {
		newMatrixScope(drawContext, stack -> {
			if (this.centerName) {
				stack.scaleAround(1 - clickTweener.getF() * 0.25F, this.getX() + this.getWidth() / 2F, Minecraft.getInstance().font.lineHeight / 2F + 2.5F);
				drawContext.drawCenteredString(Minecraft.getInstance().font, name, this.getX() + this.getWidth() / 2, this.getY() + this.height / 2 - Minecraft.getInstance().font.lineHeight / 2, getColor());
			} else {
				stack.scaleAround(1 - clickTweener.getF() * 0.25F, Minecraft.getInstance().font.width(name) / 2F, Minecraft.getInstance().font.lineHeight / 2F);
				drawContext.drawString(Minecraft.getInstance().font, name, this.getX() + 5, this.getY() + this.height / 2 - Minecraft.getInstance().font.lineHeight / 2, getColor());
			}
		});
	}

	@Override
	public void onClick(MouseButtonEvent click, boolean bl) {
		r.run();
	}
}
