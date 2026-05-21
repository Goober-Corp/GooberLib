package com.goobercorp.gooberlib.gui;

import com.goobercorp.gooberlib.option.Option;
import com.goobercorp.gooberlib.util.RenderUtils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

public class ColorPickerWidget extends ClickableWidget {
	Option<?> option;

	public ColorPickerWidget(int i, int j, int k, int l, Text text) {
		super(i, j, k, l, text);
	}

	public ColorPickerWidget(Option<?> theOption, int x, int y, int width, int height) {
		super(x, y, width, height, theOption.name());
		option = theOption;
	}

	@Override
	protected void renderWidget(DrawContext drawContext, int i, int j, float f) {
		drawRainbowGradient(drawContext, getX(), getY(), getRight(), getBottom());
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {

	}

	protected void drawRainbowGradient(DrawContext graphics, int x1, int y1, int x2, int y2) {
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
}
