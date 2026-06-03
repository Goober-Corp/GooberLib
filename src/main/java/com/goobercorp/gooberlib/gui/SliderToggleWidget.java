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

public class SliderToggleWidget extends EvilBaseWidget {
	private final BooleanOption opt;
	private final Tweener boxHoverTweener;
	private final Tweener tweener;

	public SliderToggleWidget(BooleanOption opt, int x, int y, int width, int height) {
		super(opt.name(), x, y, width, height);
		this.opt = opt;
		tweener = new Tweener(() -> opt.value ? 0 : 1);
		boxHoverTweener = new Tweener(() -> Math.abs(0.5F - tweener.getF()), 10);
	}

	@Override
	public void renderWidget(DrawContext drawContext, double mouseX, double mouseY, float delta) {
		boxHoverTweener.update();
		tweener.update();
		float widthAndHeight = (getHeight() - getY() - 4);
		float midpoint = (widthAndHeight / 2);
		Vec2f center = new Vec2f(getRight() - midpoint - 2, midpoint + 2);
		midpoint *= 0.75F;
		RenderUtils.drawBoxOutline(drawContext, center.x - midpoint * 2 + 1, center.y - midpoint + 1, center.x + midpoint, center.y + midpoint, MainConfig.shadowCol);
		RenderUtils.drawBoxOutline(drawContext, center.x - midpoint * 2, center.y - midpoint, center.x + midpoint - 1, center.y + midpoint - 1, MainConfig.primaryCol);
		float finalMidpoint = midpoint;
		float yeah = (midpoint * boxHoverTweener.getF());
		newMatrixScope(drawContext, stack -> {
			stack.translate((-finalMidpoint * tweener.getF()), 0);
			RenderUtils.fillEvil(drawContext, center.x - yeah + 1, center.y - yeah + 1, center.x + yeah + 1, center.y + yeah + 1, MainConfig.shadowCol);
			RenderUtils.fillEvil(drawContext, center.x - yeah, center.y - yeah, center.x + yeah, center.y + yeah, MainConfig.primaryCol);
		});
	}

	@Override
	public boolean mouseClicked(Click click, boolean bl) {
		if (RenderUtils.isInBounds(click.comp_4798(), click.comp_4799(), new ScreenRect(getRight() - width + 1, getY() + 1, getRight() - 2, getBottom() - 2))) {
			opt.setValue(!opt.value);
		}
		return super.mouseClicked(click, bl);
	}
}
