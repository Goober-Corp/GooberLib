package com.goobercorp.gooberlib.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class EvilStringWidgetWithName extends EvilStringWidget {
	private final Text name;
	private final int x;

	public EvilStringWidgetWithName(Text name, int x, int y, int width, int height, @Nullable Consumer<String> changedListener, Predicate<String> predicate, String initial) {
		super(Text.empty(), x + MinecraftClient.getInstance().textRenderer.getWidth(name) + 2, y, width - MinecraftClient.getInstance().textRenderer.getWidth(name), height, changedListener, predicate, initial);
		this.name = name;
		this.x = x;
	}

	@Override
	public void renderWidget(DrawContext drawContext, int i, int j, float f) {
		drawContext.drawText(MinecraftClient.getInstance().textRenderer, name, x, getY() + 3, 0xFFFFFFFF, true);
		super.renderWidget(drawContext, i, j, f);
	}
}
