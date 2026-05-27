package com.goobercorp.gooberlib.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class EvilStringWidget extends TextFieldWidget {
	private final Text name;
	private final int x;

	public EvilStringWidget(Text name, int x, int y, int width, int height, Consumer<String> changedListener, String initial) {
		super(font(), x + font().getWidth(name) + 3, y, width - font().getWidth(name) - 3, height, name);
		this.name = name;
		this.x = x;
		this.setText(initial);
		this.setChangedListener(changedListener);
	}

	@Override
	public void renderWidget(DrawContext drawContext, int i, int j, float f) {
		drawContext.drawText(font(), name, x, this.getY() + 4, 0xFFFFFFFF, true);
		drawContext.fill(this.getX(), this.getY(), this.getRight(), this.getBottom(), 0xFF000000);
		super.renderWidget(drawContext, i, j, f);
	}

	public static TextRenderer font() {
		return MinecraftClient.getInstance().textRenderer;
	}
}
