package com.goobercorp.gooberlib.gui;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.Tweener;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.util.math.Vec2f;

import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

public class TickBoxWidget extends EvilBaseWidget {
	private final BooleanOption opt;
	private final Tweener t;

	public TickBoxWidget(BooleanOption opt, int x, int y, int width, int height) {
		super(opt.name(), x, y, width, height);
		this.opt = opt;
		t = new Tweener(() -> {
			if (opt.value) {
				return isHovered() ? 0.8F : 1;
			} else {
				return isHovered() ? 0.2F : 0;
			}
		});
	}

	@Override
	public void renderWidget(DrawContext drawContext, double mouseX, double mouseY, float delta) {
		t.update();
		float widthAndHeight = (getHeight() - getY() - 4);
		float midpoint = (widthAndHeight / 2);
		Vec2f center = new Vec2f(getRight() - midpoint - 2, midpoint + 2);
		midpoint *= 0.75F;
		RenderUtils.drawBoxOutline(drawContext, center.x - midpoint + 1, center.y - midpoint + 1, center.x + midpoint, center.y + midpoint, MainConfig.shadowCol);
		RenderUtils.drawBoxOutline(drawContext, center.x - midpoint, center.y - midpoint, center.x + midpoint - 1, center.y + midpoint - 1, MainConfig.primaryCol);
		if (t.get() > 0.05F) {
			midpoint *= 0.65F;
			midpoint *= t.getF() * 0.9F;
			RenderUtils.fillEvil(drawContext, center.x - midpoint + 1, center.y - midpoint + 1, center.x + midpoint + 1, center.y + midpoint + 1, MainConfig.shadowCol);
			RenderUtils.fillEvil(drawContext, center.x - midpoint, center.y - midpoint, center.x + midpoint, center.y + midpoint, MainConfig.primaryCol);
		}
//		drawContext.drawText(MinecraftClient.getInstance().textRenderer, this.message, getX() + 5, getY() + MinecraftClient.getInstance().textRenderer.fontHeight / 2, MainConfig.primaryCol, true);
	}

	@Override
	public boolean mouseClicked(Click click, boolean bl) {
		if (RenderUtils.isInBounds(click.comp_4798(), click.comp_4799(), new ScreenRect(getRight() - width + 1, getY() + 1, getRight() - 2, getBottom() - 2))) {
			opt.setValue(!opt.value);
		}
		return super.mouseClicked(click, bl);
	}
}
