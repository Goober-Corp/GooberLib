package com.goobercorp.gooberlib.gui;

import com.goobercorp.gooberlib.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.navigation.GuiNavigation;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.navigation.NavigationDirection;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

public class PrecisePositionWidgetWrapper<T extends ClickableWidget> implements Drawable, Element, Selectable, Narratable {
	private final T wrapped;
	private Supplier<Text> hoverMessage;
	private double x;
	private double y;
	private float renderProgress = 0;
	private float targetInset;

	public void setHoverMessage(Supplier<Text> hoverMessage) {
		this.hoverMessage = hoverMessage;
	}

	public float getRenderProgress() {
		return renderProgress;
	}

	public void setRenderProgress(float renderProgress) {
		this.renderProgress = renderProgress;
	}


	public float getTargetInset() {
		return targetInset;
	}

	public void setTargetInset(float targetInset) {
		this.targetInset = targetInset;
	}

	public double getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(double offsetX) {
		this.offsetX = offsetX;
	}

	public double getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(double offsetY) {
		this.offsetY = offsetY;
	}

	private double offsetX, offsetY;

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public Supplier<Text> getHoverMessage() {
		return hoverMessage;
	}


	public T getWrapped() {
		return wrapped;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getRealX() {
		return x + offsetX;
	}

	public double getRealY() {
		return y + offsetY;
	}


	public void offsetX(Function<Double, Double> theFunc) {
		this.setX(theFunc.apply(this.getX()));
	}

	public void offsetY(Function<Double, Double> theFunc) {
		this.setY(theFunc.apply(this.getY()));
	}

	public PrecisePositionWidgetWrapper(T wrapped, double x, double y, Supplier<Text> description) {
		this.wrapped = wrapped;
		this.x = x - wrapped.getWidth() + 1;
		this.targetInset = (float) x;
		this.y = y;
		if (description != null) {
			this.hoverMessage = description;
		} else {
			this.hoverMessage = Text::empty;
		}
		renderProgress = new ScreenRect((int) getRealX(), (int) getRealY(), wrapped.getRight(), wrapped.getBottom()).overlaps(new ScreenRect(0, 0, MinecraftClient.getInstance().getWindow().getScaledWidth(), MinecraftClient.getInstance().getWindow().getScaledHeight())) ? 1 : 0;
	}

	@Override
	public void render(DrawContext drawContext, int i, int j, float f) {
		boolean isOnScreen = new ScreenRect((int) getRealX(), (int) getRealY(), wrapped.getRight(), wrapped.getBottom()).overlaps(new ScreenRect(0, 0, drawContext.getScaledWindowWidth(), drawContext.getScaledWindowHeight()));
		this.x = RenderUtils.ease(this.x, isOnScreen ? targetInset : x, 10);
		renderProgress = (float) RenderUtils.ease(renderProgress, isOnScreen ? 1 : 0, 15);
		if (isOnScreen) {
			drawContext.getMatrices().pushMatrix().translate((float) getRealX(), (float) getRealY());
			wrapped.render(drawContext, (int) Math.round(i - getRealX()), (int) Math.round(j - getRealY()), f);
			drawContext.getMatrices().popMatrix();
		}
	}

	@Override
	public void mouseMoved(double mX, double mY) {
		wrapped.mouseMoved(mX - getRealX(), mY - getRealY());
	}

	@Override
	public boolean mouseClicked(Click click, boolean bl) {
		return wrapped.mouseClicked(new Click(click.comp_4798() - getRealX(), click.comp_4799() - getRealY(), click.comp_4800()), bl);
	}

	@Override
	public boolean mouseReleased(Click click) {
		return wrapped.mouseReleased(new Click(click.comp_4798() - getRealX(), click.comp_4799() - getRealY(), click.comp_4800()));
	}

	@Override
	public boolean mouseDragged(Click click, double d, double e) {
//		this.offsetX(x -> x + d);
//		this.offsetY(y -> y + e);

		return wrapped.mouseDragged(new Click(click.comp_4798() - getRealX(), click.comp_4799() - getRealY(), click.comp_4800()), d, e);
	}

	@Override
	public boolean mouseScrolled(double d, double e, double f, double g) {
		return wrapped.mouseScrolled(d - getRealX(), e - getRealY(), f, g);
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
		return wrapped.isMouseOver(d - getRealX(), e - getRealY());
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
