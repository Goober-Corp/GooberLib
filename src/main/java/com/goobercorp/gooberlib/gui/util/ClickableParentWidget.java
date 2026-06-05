package com.goobercorp.gooberlib.gui.util;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.narration.NarrationSupplier;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class ClickableParentWidget extends AbstractWidget implements ContainerEventHandler {
	private final List<GuiEventListener> children;
	private boolean dragging;
	private @Nullable GuiEventListener focussed;

	public ClickableParentWidget(int x, int y, int width, int height, Component message, List<GuiEventListener> children) {
		super(x, y, width, height, message);
		this.children = children;
	}

	@Override
	public List<GuiEventListener> children() {
		return this.children;
	}

	@Override
	public boolean isDragging() {
		return this.dragging;
	}

	@Override
	public void setDragging(boolean bl) {
		this.dragging = bl;
	}

	@Override
	public @Nullable GuiEventListener getFocused() {
		return this.focussed;
	}

	@Override
	public void setFocused(@Nullable GuiEventListener element) {
		if (this.focussed != element) {
			if (this.focussed != null) {
				this.focussed.setFocused(false);
			}
			this.focussed = element;
			if (this.focussed != null) {
				this.focussed.setFocused(true);
			}
		}
	}

	@Override
	public void setFocused(boolean bl) {
		if (!bl) this.setFocused(null);
		super.setFocused(bl);
	}

	@Override
	protected void renderWidget(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
		for (var child : children) {
			if (child instanceof Renderable widget) {
				widget.render(drawContext, mouseX, mouseY, delta);
			}
		}
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationMessageBuilder) {
		for (var child : children) {
			if (child instanceof NarrationSupplier widget) {
				widget.updateNarration(narrationMessageBuilder);
			}
		}
	}

	@Override
	public boolean mouseScrolled(double d, double e, double f, double g) {
		return ContainerEventHandler.super.mouseScrolled(d, e, f, g);
	}

	@Override
	public boolean keyPressed(KeyEvent keyInput) {
		return ContainerEventHandler.super.keyPressed(keyInput);
	}

	@Override
	public boolean keyReleased(KeyEvent keyInput) {
		return ContainerEventHandler.super.keyReleased(keyInput);
	}

	@Override
	public boolean charTyped(CharacterEvent charInput) {
		return ContainerEventHandler.super.charTyped(charInput);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent click, boolean bl) {
		return ContainerEventHandler.super.mouseClicked(click, bl);
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent click) {
		return ContainerEventHandler.super.mouseReleased(click);
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent click, double d, double e) {
		return ContainerEventHandler.super.mouseDragged(click, d, e);
	}
}
