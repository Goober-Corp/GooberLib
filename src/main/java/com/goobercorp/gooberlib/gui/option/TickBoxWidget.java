package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.EvilBaseWidget;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.Tweener;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.world.phys.Vec2;

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
		shouldDrawName = true;
	}

	public TickBoxWidget(BooleanOption opt, int x, int y, int width, int height, boolean centerName) {
		this(opt, x, y, width, height);
		this.centerName = centerName;
	}

	@Override
	public void renderWidget(GuiGraphics drawContext, double mouseX, double mouseY, float delta) {
		t.update();
		float widthAndHeight = (getHeight() - getY() - 4);
		float midpoint = (widthAndHeight / 2);
		Vec2 center = new Vec2(getRight() - midpoint - 2, midpoint + 2);
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
	public void onClick(MouseButtonEvent click, boolean bl) {
		if (RenderUtils.isInBounds(click.x(), click.y(), new ScreenRectangle(getRight() - width + 1, getY() + 1, getRight() - 2, getBottom() - 2))) {
			opt.setValue(!opt.value);
		}
	}

}
