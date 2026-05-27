package com.goobercorp.gooberlib.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class EvilStringWidget extends TextFieldWidget {
	private final Text name;
	private final int x;
	private final Predicate<String> predicate;
	private final Consumer<String> changedListener;
	private String lastAccepted;

	public EvilStringWidget(Text name, int x, int y, int width, int height, Consumer<String> changedListener, Predicate<String> predicate, String initial) {
		super(font(), x + font().getWidth(name) + 3, y, width - font().getWidth(name) - 3, height, name);
		this.name = name;
		this.x = x;
		this.setText(initial);
		this.changedListener = changedListener;
		this.predicate = predicate;
		this.lastAccepted = initial;
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

	@Override
	public void setFocused(boolean bl) {
		if (!bl) {
			this.setSelectionEnd(0);
			this.setSelectionStart(0);
			if (this.predicate.test(this.getText())) {
				this.changedListener.accept(this.getText());
				this.lastAccepted = this.getText();
			} else {
				this.setText(lastAccepted);
			}
		}
		super.setFocused(bl);
	}
}
