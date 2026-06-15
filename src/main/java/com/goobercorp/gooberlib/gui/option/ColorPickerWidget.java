package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.EvilBaseWidget;
import com.goobercorp.gooberlib.option.individual.java.ColorOption;
import com.goobercorp.gooberlib.util.ColorTweener;
import com.goobercorp.gooberlib.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.world.phys.Vec2;

import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

public class ColorPickerWidget extends EvilBaseWidget {
	private final ColorOption opt;
	private final ColorTweener colorTweener;

	public ColorPickerWidget(ColorOption theOption, int x, int y, int width, int height) {
		super(theOption.name(), x, y, width, height);
		opt = theOption;
		colorTweener = new ColorTweener(() -> opt.value, 5);
		shouldDrawName = true;
	}

	@Override
	protected void renderWidget(GuiGraphics context, int i, int j, float f) {
		super.renderWidget(context, i, j, f);
		newMatrixScope(context, stack -> {
			stack.translate(horizontalPosOffset, verticalPosOffset);
			float widthAndHeight = (getHeight() - getY() - 4);
			float midpoint = (widthAndHeight / 2);
			Vec2 center = new Vec2(getRight() - midpoint - 2, midpoint + 2);
			midpoint *= 0.75F;
			RenderUtils.fillEvil(context, center.x - midpoint + 1, center.y - midpoint + 1, center.x + midpoint - 1, center.y + midpoint - 1, colorTweener.get());
			RenderUtils.drawBoxOutline(context, center.x - midpoint, center.y - midpoint, center.x + midpoint - 1, center.y + midpoint - 1, 0xFF000000);
		});
//		drawRainbowGradient(context, getX(), getY(), getRight(), getBottom());
	}

	@Override
	protected void drawText(GuiGraphics drawContext) {
		super.drawText(drawContext);
		drawContext.drawString(Minecraft.getInstance().font, "Edit", getRight() - getHeight() * 2, this.getY() + this.height / 2 - Minecraft.getInstance().font.lineHeight / 2, MainConfig.primaryCol);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent click, boolean bl) {
		return super.mouseClicked(click, bl);
	}

	protected void drawRainbowGradient(GuiGraphics graphics, int x1, int y1, int x2, int y2) {
		//Draws a rainbow gradient, left to right
		int[] colors = new int[]{0xFFFF0000, 0xFFFFFF00, 0xFF00FF00, 0xFF00FFFF, 0xFF0000FF, 0xFFFF00FF, 0xFFFF0000}; //all the colors in the gradient
		int width = x2 - x1;
		int maxColors = colors.length - 1;

		for (int color = 0; color < maxColors; color++) {
			//First checks if the final color is being rendered, if true -> uses x2 int instead of x1
			//if false -> it adds the width divided by the max colors multiplied by the current color plus one to the x1 int
			//the x2 int for the fillSidewaysGradient is the same formula, excluding the additional plus one.
			//The gradient colors is determined by the color int and the color int plus one, which is why red is in the colors array twice
			RenderUtils.fillEvil(
					graphics,
					(x1 + (float) width / maxColors * color), ((float) y1),
					color == maxColors - 1 ? (x2) : ((x1 + ((float) width / maxColors * (color + 1)))), ((float) y2),
					colors[color], colors[color + 1]
			);
		}

	}

	@Override
	public void reset() {
		opt.resetToDefault();
	}
}
