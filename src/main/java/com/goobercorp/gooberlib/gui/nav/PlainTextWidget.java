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
	//BIG TODO: actually correctly draw and process components instead of strings, like AbstractStringWidget. Also hover n click type shit

	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
		if (centered) {
			guiGraphics.drawCenteredString(Minecraft.getInstance().font, message, getX(), this.getY() + this.height / 2 - Minecraft.getInstance().font.lineHeight / 2, col.getAsInt());
		} else {
			guiGraphics.drawString(Minecraft.getInstance().font, message, getX() + 1, this.getY() + this.height / 2 - Minecraft.getInstance().font.lineHeight / 2, col.getAsInt(), true);
		}
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

	}
}
