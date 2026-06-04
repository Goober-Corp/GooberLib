package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.option.individual.java.ColorOption;
import com.goobercorp.gooberlib.util.ColorTweener;
import com.goobercorp.gooberlib.util.RenderUtils;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;

public class EvilStringColorWidget extends EvilStringWidget {
	private final Component name;
	private final int x;
	private final ColorOption opt;
	private final ColorTweener colorTweener;

	public EvilStringColorWidget(Component name, int x, int y, int width, int height, @Nullable Consumer<String> changedListener, Predicate<String> predicate, Predicate<String> immediatePredicate, String initial, ColorOption opt) {
		super(x + font().width(name) + 2, y, width - font().width(name), height, changedListener, predicate, immediatePredicate, initial);
		this.name = name;
		this.x = x;
		this.opt = opt;
		colorTweener = new ColorTweener(() -> opt.value, 5);
	}

	public static Font font() {
		return Minecraft.getInstance().font;
	}

	@Override
	public void renderWidget(GuiGraphics context, double mouseX, double mouseY, float delta) {
		super.renderWidget(context, mouseX, mouseY, delta);
		float widthAndHeight = (getHeight() - getY() - 4);
		float midpoint = (widthAndHeight / 2);
		Vec2 center = new Vec2(getRight() - midpoint - 2, midpoint + 2);
		midpoint *= 0.75F;
		RenderUtils.fillEvil(context, center.x - midpoint + 1, center.y - midpoint + 1, center.x + midpoint - 1, center.y + midpoint - 1, colorTweener.get());
		RenderUtils.drawBoxOutline(context, center.x - midpoint, center.y - midpoint, center.x + midpoint - 1, center.y + midpoint - 1, 0xFF000000);
	}

	@Override
	public void renderWidget(GuiGraphics drawContext, int i, int j, float f) {
		super.renderWidget(drawContext, i, j, f);
		colorTweener.update();
		drawContext.drawString(font(), name, x, getY() + 3, MainConfig.primaryCol, true);
	}
}
