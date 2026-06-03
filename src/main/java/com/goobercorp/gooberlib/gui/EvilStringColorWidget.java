package com.goobercorp.gooberlib.gui;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.option.individual.java.ColorOption;
import com.goobercorp.gooberlib.util.ColorTweener;
import com.goobercorp.gooberlib.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec2f;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class EvilStringColorWidget extends EvilStringWidget {
	private final Text name;
	private final int x;
	private final ColorOption opt;
	private final ColorTweener colorTweener;

	public EvilStringColorWidget(Text name, int x, int y, int width, int height, @Nullable Consumer<String> changedListener, Predicate<String> predicate, Predicate<String> immediatePredicate, String initial, ColorOption opt) {
		super(x + font().getWidth(name) + 2, y, width - font().getWidth(name), height, changedListener, predicate, immediatePredicate, initial);
		this.name = name;
		this.x = x;
		this.opt = opt;
		colorTweener = new ColorTweener(() -> opt.value, 5);
	}

	public static TextRenderer font() {
		return MinecraftClient.getInstance().textRenderer;
	}

	@Override
	public void renderWidget(DrawContext context, double mouseX, double mouseY, float delta) {
		super.renderWidget(context, mouseX, mouseY, delta);
		float widthAndHeight = (getHeight() - getY() - 4);
		float midpoint = (widthAndHeight / 2);
		Vec2f center = new Vec2f(getRight() - midpoint - 2, midpoint + 2);
		midpoint *= 0.75F;
		RenderUtils.fillEvil(context, center.x - midpoint + 1, center.y - midpoint + 1, center.x + midpoint - 1, center.y + midpoint - 1, colorTweener.get());
		RenderUtils.drawBoxOutline(context, center.x - midpoint, center.y - midpoint, center.x + midpoint - 1, center.y + midpoint - 1, 0xFF000000);
	}

	@Override
	public void renderWidget(DrawContext drawContext, int i, int j, float f) {
		super.renderWidget(drawContext, i, j, f);
		colorTweener.update();
		drawContext.drawText(font(), name, x, getY() + 3, MainConfig.primaryCol, true);
	}
}
