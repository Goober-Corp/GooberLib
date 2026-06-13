package com.goobercorp.gooberlib.gui.nav;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.function.IntSupplier;

public class PlainTextWidget extends AbstractWidget {
	private final IntSupplier col;
	private final boolean centered;

	public PlainTextWidget(int i, int j, int k, int l, Component component, IntSupplier col, boolean centered) {
		super(i, j, k, l, component);
		this.col = col;
		this.centered = centered;
		this.width = Minecraft.getInstance().font.width(component);
	}

	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
		String yeah = Minecraft.getInstance().font.plainSubstrByWidth(message.getString(), this.width);
		if (centered) {
			guiGraphics.drawCenteredString(Minecraft.getInstance().font, yeah, getX(), this.getY() + this.height / 2 - Minecraft.getInstance().font.lineHeight / 2, col.getAsInt());
		} else {
			guiGraphics.drawString(Minecraft.getInstance().font, yeah, getX() + 1, this.getY() + this.height / 2 - Minecraft.getInstance().font.lineHeight / 2, col.getAsInt(), true);
		}
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

	}
}
