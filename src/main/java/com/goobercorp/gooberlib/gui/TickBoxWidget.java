package com.goobercorp.gooberlib.gui;

import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.Tweener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.util.math.Vec2f;

public class TickBoxWidget extends ClickableWidget {
	BooleanOption opt;
	Tweener t;

	public TickBoxWidget(int x, int y, int width, int height, BooleanOption opt) {
		super(x, y, width, height, opt.name());
		this.opt = opt;
		t = new Tweener(() -> opt.value ? 1 : 0);
	}

	@Override
	protected void renderWidget(DrawContext drawContext, int i, int j, float f) {
		//TODO: left shadow left red. need to figure out a theme
		t.update();
		RenderUtils.fillEvil(drawContext, getX(), getY(), getRight(), getBottom(), 0x80000000);
		float widthAndHeight = (getHeight() - getY());
		float midpoint = widthAndHeight / 2;
		Vec2f center = new Vec2f(getRight() - midpoint, midpoint);
		midpoint *= 0.75F;
		RenderUtils.drawBoxOutline(drawContext, center.x - midpoint + 1, center.y - midpoint + 1, center.x + midpoint, center.y + midpoint, 0x80FF0000);
		RenderUtils.drawBoxOutline(drawContext, center.x - midpoint, center.y - midpoint, center.x + midpoint - 1, center.y + midpoint - 1, -1);
		if (t.get() > 0.1F) {
			midpoint *= 0.75F;
			midpoint *= (float) (t.get() * 0.9F);
			RenderUtils.fillEvil(drawContext, center.x - midpoint + 1, center.y - midpoint + 1, center.x + midpoint + 1, center.y + midpoint + 1, 0x80FF0000);
			RenderUtils.fillEvil(drawContext, center.x - midpoint, center.y - midpoint, center.x + midpoint, center.y + midpoint, -1);
		}
		drawContext.drawText(MinecraftClient.getInstance().textRenderer, this.message, getX() + 5, getY() + MinecraftClient.getInstance().textRenderer.fontHeight / 2, -1, true);
	}

	@Override
	public boolean mouseClicked(Click click, boolean bl) {
		if (RenderUtils.isInBounds(click.comp_4798(), click.comp_4799(), new ScreenRect(getRight() - width + 1, getY() + 1, getRight() - 2, getBottom() - 2))) {
			opt.setValue(!opt.value);
		}
		return super.mouseClicked(click, bl);
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {

	}
}
