package com.goobercorp.gooberlib.gui.nav;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class PlainTextWidget extends AbstractWidget {
	private final int col;
	private final boolean centered;

	public PlainTextWidget(int i, int j, int k, int l, Component component, int col, boolean centered) {
		super(i, j, k, l, component);
		//TODO: use supplier for color if it is in need of updating
		this.col = col;
		this.centered = centered;
		this.width = Minecraft.getInstance().font.width(component);
	}

	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
		String yeah = Minecraft.getInstance().font.plainSubstrByWidth(message.getString(), this.width);
		if (centered) {
			guiGraphics.drawCenteredString(Minecraft.getInstance().font, yeah, getX(), this.getY() + this.height / 2 - Minecraft.getInstance().font.lineHeight / 2, col);
		} else {
			guiGraphics.drawString(Minecraft.getInstance().font, yeah, getX(), this.getY() + this.height / 2 - Minecraft.getInstance().font.lineHeight / 2, col, true);
		}
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

	}
}
