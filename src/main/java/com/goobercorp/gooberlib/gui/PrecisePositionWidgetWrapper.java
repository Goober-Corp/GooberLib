package com.goobercorp.gooberlib.gui;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.navigation.NavigationDirection;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import org.jspecify.annotations.Nullable;

import java.util.Collection;

public class PrecisePositionWidgetWrapper<T extends Drawable & Element & Selectable & Narratable> implements Drawable, Element, Selectable, Narratable {
	private final T wrapped;
	private double x;
	private double y;

	public void setX(double x) { this.x = x; }
	public void setY(double y) { this.y = y; }

	public T getWrapped() {
		return wrapped;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public PrecisePositionWidgetWrapper(T wrapped, double x, double y) {
		this.wrapped = wrapped;
		this.x = x;
		this.y = y;
	}

	@Override
	public void render(DrawContext drawContext, int i, int j, float f) {
		drawContext.getMatrices().pushMatrix().translate((float) x, (float) y);
		wrapped.render(drawContext, (int) Math.round(i-x), (int) Math.round(j-y), f);
		drawContext.getMatrices().popMatrix();
	}

	@Override
	public void mouseMoved(double mX, double mY) {
		wrapped.mouseMoved(mX-x, mY-y);
	}

	@Override
	public boolean mouseClicked(Click click, boolean bl) {
		return wrapped.mouseClicked(new Click(click.comp_4798()-x, click.comp_4799()-y, click.comp_4800()), bl);
	}

	@Override
	public boolean mouseReleased(Click click) {
		return wrapped.mouseReleased(new Click(click.comp_4798()-x, click.comp_4799()-y, click.comp_4800()));
	}

	@Override
	public boolean mouseDragged(Click click, double d, double e) {
		return wrapped.mouseDragged(new Click(click.comp_4798()-x, click.comp_4799()-y, click.comp_4800()), d, e);
	}

	@Override
	public boolean mouseScrolled(double d, double e, double f, double g) {
		return wrapped.mouseScrolled(d-x, e-y, f, g);
	}

	@Override
	public boolean keyPressed(KeyInput keyInput) {
		return wrapped.keyPressed(keyInput);
	}

	@Override
	public boolean keyReleased(KeyInput keyInput) {
		return wrapped.keyReleased(keyInput);
	}

	@Override
	public boolean charTyped(CharInput charInput) {
		return wrapped.charTyped(charInput);
	}

	@Override
	public @Nullable GuiNavigationPath getNavigationPath(GuiNavigation guiNavigation) {
		return wrapped.getNavigationPath(guiNavigation);
	}

	@Override
	public boolean isMouseOver(double d, double e) {
		return wrapped.isMouseOver(d-x, e-y);
	}

	@Override
	public void setFocused(boolean bl) {
		wrapped.setFocused(bl);
	}

	@Override
	public boolean isFocused() {
		return wrapped.isFocused();
	}

	@Override
	public boolean isClickable() {
		return wrapped.isClickable();
	}

	@Override
	public @Nullable GuiNavigationPath getFocusedPath() {
		return wrapped.getFocusedPath();
	}

	@Override
	public ScreenRect getNavigationFocus() {
		return wrapped.getNavigationFocus();
	}

	@Override
	public ScreenRect getBorder(NavigationDirection navigationDirection) {
		return wrapped.getBorder(navigationDirection);
	}

	@Override
	public SelectionType getType() {
		return wrapped.getType();
	}

	@Override
	public boolean isInteractable() {
		return wrapped.isInteractable();
	}

	@Override
	public Collection<? extends Selectable> getNarratedParts() {
		return wrapped.getNarratedParts();
	}

	@Override
	public void appendNarrations(NarrationMessageBuilder narrationMessageBuilder) {
		wrapped.appendNarrations(narrationMessageBuilder);
	}

	@Override
	public int getNavigationOrder() {
		return wrapped.getNavigationOrder();
	}
}
