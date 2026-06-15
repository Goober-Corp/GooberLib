package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.EvilBaseWidget;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.Tweener;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.world.phys.Vec2;
import org.lwjgl.glfw.GLFW;

import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

public class SliderToggleWidget extends EvilBaseWidget {
	private final BooleanOption opt;
	private final Tweener boxHoverTweener;
	private final Tweener tweener;

	public SliderToggleWidget(BooleanOption opt, int x, int y, int width, int height) {
		super(opt.name(), x, y, width, height);
		shouldDrawName = true;
		this.opt = opt;
		tweener = new Tweener(() -> opt.getValue() ? 0 : 1);
		boxHoverTweener = new Tweener(() -> Math.abs(0.5F - tweener.getF()), 10);
	}

	public SliderToggleWidget(BooleanOption opt, int x, int y, int width, int height, boolean centerName) {
		this(opt, x, y, width, height);
		this.centerName = centerName;
	}

	@Override
	public void renderWidget(GuiGraphics drawContext, double mouseX, double mouseY, float delta) {
		boxHoverTweener.update();
		tweener.update();
		float widthAndHeight = (getHeight() - getY() - 4);
		float midpoint = (widthAndHeight / 2);
		Vec2 center = new Vec2(getRight() - midpoint - 2, midpoint + 2);
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
	public void onClick(MouseButtonEvent click, boolean bl) {
		if (RenderUtils.isInBounds(click.x(), click.y(), new ScreenRectangle(getRight() - width + 1, getY() + 1, getRight() - 2, getBottom() - 2))) {
			opt.setValue(!opt.getValue());
			this.horizontalPosOffset += (opt.getValue() ? 0.75F : -0.75F);
		}
	}

	@Override
	protected boolean isValidClickButton(MouseButtonInfo mouseButtonInfo) {
		int button = mouseButtonInfo.button();
		return button == GLFW.GLFW_MOUSE_BUTTON_LEFT;
	}

	@Override
	public void reset() {
		opt.resetToDefault();
	}
}
