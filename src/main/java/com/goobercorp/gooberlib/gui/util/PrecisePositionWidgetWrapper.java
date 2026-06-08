package com.goobercorp.gooberlib.gui.util;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.narration.NarrationSupplier;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.function.Supplier;

import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

public class PrecisePositionWidgetWrapper<T extends AbstractWidget> implements Renderable, GuiEventListener, NarratableEntry, NarrationSupplier {
	private final T wrapped;
	private Supplier<Component> hoverMessage;
	private double x;
	private double y;
	private float renderProgress = 0;
	// todo: move this outside of this class (not related to a precise position wrapper gui element; should extend this or be handled in the screen)
	private float targetInset;

	public void setHoverMessage(Supplier<Component> hoverMessage) {
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

	public Supplier<Component> getHoverMessage() {
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

	public PrecisePositionWidgetWrapper(T wrapped, double x, double y, Supplier<Component> description) {
		this.wrapped = wrapped;
		renderProgress = new ScreenRectangle((int) getRealX(), (int) getRealY(), wrapped.getRight(), wrapped.getBottom()).overlaps(new ScreenRectangle(0, 0, Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight())) ? 1 : 0;
		this.x = x - wrapped.getWidth() + 1;
		this.targetInset = (float) x;
		this.y = y;
		if (description != null) {
			this.hoverMessage = description;
		} else {
			this.hoverMessage = Component::empty;
		}
	}

	@Override
	public void render(GuiGraphics drawContext, int i, int j, float f) {
		float screenX = drawContext.pose().m20 + (float) getRealX();
		float screenY = drawContext.pose().m21 + (float) getRealY();

		boolean isVisible = new ScreenRectangle((int) screenX, (int) screenY, wrapped.getWidth(), wrapped.getHeight()).overlaps(new ScreenRectangle(0, 0, drawContext.guiWidth(), drawContext.guiHeight()));

		this.x = RenderUtils.ease(this.x, targetInset, 10);
		renderProgress = (float) RenderUtils.ease(renderProgress, 1, 15);
		if (isVisible) {
			newMatrixScope(drawContext, matrix3x2fStack -> {
				matrix3x2fStack.translate((float) getRealX(), (float) getRealY());

				wrapped.render(drawContext, (int) Math.round(i - getRealX()), (int) Math.round(j - getRealY()), f);
			});
		}

		// blue bounds for !isVisible, red for isVisible
		if (MainConfig.PPWW_BOUNDS.value) {
			RenderUtils.fillEvil(drawContext, (float) getRealX(), (float) getRealY(), (float) (getRealX() + wrapped.getWidth()), (float) (getRealY() + wrapped.getHeight()), 0x11FFFFFF);
			RenderUtils.drawBoxOutline(drawContext, (float) getRealX(), (float) getRealY(), (float) (getRealX() + wrapped.getWidth() - 1), (float) (getRealY() + wrapped.getHeight() - 1), isVisible ? 0xAAFF0000 : 0xAA0000FF);
			drawContext.drawString(Minecraft.getInstance().font, wrapped.getMessage(), (int) getRealX(), (int) getRealY(), -1);
		}
	}

	@Override
	public void mouseMoved(double mX, double mY) {
		wrapped.mouseMoved(mX - getRealX(), mY - getRealY());
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent click, boolean bl) {
		return wrapped.mouseClicked(new MouseButtonEvent(click.x() - getRealX(), click.y() - getRealY(), click.buttonInfo()), bl);
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent click) {
		return wrapped.mouseReleased(new MouseButtonEvent(click.x() - getRealX(), click.y() - getRealY(), click.buttonInfo()));
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent click, double d, double e) {
//		this.offsetX(x -> x + d);
//		this.offsetY(y -> y + e);

		return wrapped.mouseDragged(new MouseButtonEvent(click.x() - getRealX(), click.y() - getRealY(), click.buttonInfo()), d, e);
	}

	@Override
	public boolean mouseScrolled(double d, double e, double f, double g) {
		return wrapped.mouseScrolled(d - getRealX(), e - getRealY(), f, g);
	}

	@Override
	public boolean keyPressed(KeyEvent keyInput) {
		return wrapped.keyPressed(keyInput);
	}

	@Override
	public boolean keyReleased(KeyEvent keyInput) {
		return wrapped.keyReleased(keyInput);
	}

	@Override
	public boolean charTyped(CharacterEvent charInput) {
		return wrapped.charTyped(charInput);
	}

	@Override
	public @Nullable ComponentPath nextFocusPath(FocusNavigationEvent guiNavigation) {
		return wrapped.nextFocusPath(guiNavigation);
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
	public boolean shouldTakeFocusAfterInteraction() {
		return wrapped.shouldTakeFocusAfterInteraction();
	}

	@Override
	public @Nullable ComponentPath getCurrentFocusPath() {
		return wrapped.getCurrentFocusPath();
	}

	@Override
	public ScreenRectangle getRectangle() {
		return wrapped.getRectangle();
	}

	@Override
	public ScreenRectangle getBorderForArrowNavigation(ScreenDirection navigationDirection) {
		return wrapped.getBorderForArrowNavigation(navigationDirection);
	}

	@Override
	public NarrationPriority narrationPriority() {
		return wrapped.narrationPriority();
	}

	@Override
	public boolean isActive() {
		return wrapped.isActive();
	}

	@Override
	public Collection<? extends NarratableEntry> getNarratables() {
		return wrapped.getNarratables();
	}

	@Override
	public void updateNarration(NarrationElementOutput narrationMessageBuilder) {
		wrapped.updateNarration(narrationMessageBuilder);
	}

	@Override
	public int getTabOrderGroup() {
		return wrapped.getTabOrderGroup();
	}
}
