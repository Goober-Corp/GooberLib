package com.goobercorp.gooberlib.gui.nav;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.util.RenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class Scrollbar extends AbstractWidget {
	private final Consumer<Double> writer;
	private boolean dragging;
	private double knobProgress; // 0 to 1
	private static final double knobHeight = 2;

	public Scrollbar(int x, int y, int width, int height, Consumer<Double> writer) {
		super(x, y, width, height, Component.literal("Scrollbar"));
		this.writer = writer;
	}

	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		guiGraphics.fill(this.getX(), this.getY(), this.getRight(), this.getBottom(), MainConfig.bgColor);
		RenderUtils.fillEvil(guiGraphics, this.getX(), (float) (getKnobY() - knobHeight / 2), this.getRight(), (float) (getKnobY() + knobHeight / 2), MainConfig.primaryCol);
	}

	@Override
	public void onClick(MouseButtonEvent click, boolean doubleClick) {
		this.dragging = true;
		if (!mouseOverKnob(click.x(), click.y())) {
			knobProgress = fromScreenY(click.y());
			this.writer.accept(knobProgress);
		}
	}

	@Override
	protected void onDrag(MouseButtonEvent mouseButtonEvent, double d, double e) {
		if (dragging) {
			knobProgress = fromScreenY(mouseButtonEvent.y());
			this.writer.accept(knobProgress);
		}
	}

	@Override
	public boolean mouseScrolled(double d, double e, double f, double g) {
		knobProgress = Math.clamp(knobProgress + (-g / (height / 2.0)), 0, 1);
		this.writer.accept(knobProgress);
		return true;
	}

	private double fromScreenY(double y) {
		return Math.clamp((y - this.getY()) / (this.getBottom() - this.getY()), 0, 1);
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

	public void setValue(double meow) {
		this.knobProgress = Math.clamp(meow, 0, 1);
	}

	public double getValue() {
		return this.knobProgress;
	}
}
