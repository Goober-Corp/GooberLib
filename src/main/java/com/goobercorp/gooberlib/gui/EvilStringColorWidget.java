package com.goobercorp.gooberlib.gui;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.option.individual.java.ColorOption;
import com.goobercorp.gooberlib.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class EvilStringColorWidget extends EvilStringWidget {
	private final Text name;
	private final int x;
	private final ColorOption opt;

	public EvilStringColorWidget(Text name, int x, int y, int width, int height, @Nullable Consumer<String> changedListener, Predicate<String> predicate, String initial, ColorOption opt) {
		super(x + font().getWidth(name) + 2, y, width - font().getWidth(name), height, changedListener, predicate, initial);
		this.name = name;
		this.x = x;
		this.opt = opt;
	}

	public static TextRenderer font() {
		return MinecraftClient.getInstance().textRenderer;
	}

	@Override
	public void renderWidget(DrawContext drawContext, int i, int j, float f) {
		drawContext.drawText(font(), name, x, getY() + 3, MainConfig.primaryCol, true);
		RenderUtils.fillEvil(drawContext, getRight(), getY(), getRight() + 10, getBottom(), opt.value);
		super.renderWidget(drawContext, i, j, f);
	}
}
