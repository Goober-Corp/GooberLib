package com.goobercorp.gooberlib.gui.nav;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.util.RenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class Scrollbar extends AbstractWidget {
	private boolean dragging;
	private double knobProgress; // 0 to 1
	private static final double knobHeight = 2;

	public Scrollbar(int x, int y, int width, int height) {
		super(x, y, width, height, Component.literal("Scrollbar"));
	}

	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		knobProgress = Math.clamp(knobProgress, 0, 1);
		guiGraphics.fill(this.getX(), this.getY(), this.getRight(), this.getBottom(), MainConfig.bgColor);
		RenderUtils.fillEvil(guiGraphics, this.getX(), (float) (getKnobY() - knobHeight / 2), this.getRight(), (float) (getKnobY() + knobHeight / 2), MainConfig.primaryCol);
	}

	@Override
	public void onClick(MouseButtonEvent click, boolean doubleClick) {
		this.dragging = true;
		if (!mouseOverKnob(click.x(), click.y())) {
			knobProgress = fromScreenY(click.y());
		}
	}

	@Override
	protected void onDrag(MouseButtonEvent mouseButtonEvent, double d, double e) {
		if (dragging) {
			knobProgress = fromScreenY(mouseButtonEvent.y());
		}
	}

	@Override
	public boolean mouseScrolled(double d, double e, double f, double g) {
		knobProgress += -g / (height / 2.0);
		return true;
	}

	private double fromScreenY(double y) {
		return (y - this.getY()) / (this.getBottom() - this.getY());
	}

	private boolean mouseOverKnob(double x, double screenY) {
		return x >= this.getX() && x < this.getRight() && screenY >= getKnobY() && screenY < getKnobY() + knobHeight;
	}

	private double getKnobY() {
		return this.getY() + knobProgress * getHeight();
	}

	@Override
	public void onRelease(MouseButtonEvent click) {
		this.dragging = false;
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
	}

	public void setKnobProgress(double meow) {
		this.knobProgress = meow;
	}
}
