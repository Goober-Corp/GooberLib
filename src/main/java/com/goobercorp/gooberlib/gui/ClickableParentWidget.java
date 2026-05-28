package com.goobercorp.gooberlib.gui;

import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import org.jspecify.annotations.Nullable;

import java.util.List;

public class ClickableParentWidget extends ClickableWidget implements ParentElement {
	private final List<? extends Element> children;
	private boolean dragging;
	private @Nullable Element focussed;

	public ClickableParentWidget(int x, int y, int width, int height, Text message, List<? extends Element> children) {
		super(x, y, width, height, message);
		this.children = children;
	}

	@Override
	public List<? extends Element> children() {
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
	public @Nullable Element getFocused() {
		return this.focussed;
	}

	@Override
	public void setFocused(@Nullable Element element) {
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
	protected void renderWidget(DrawContext drawContext, int mouseX, int mouseY, float delta) {
		for (var child : children) {
			if (child instanceof Drawable widget) {
				widget.render(drawContext, mouseX, mouseY, delta);
			}
		}
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {
		for (var child : children) {
			if (child instanceof Narratable widget) {
				widget.appendNarrations(narrationMessageBuilder);
			}
		}
	}

	@Override
	public boolean mouseScrolled(double d, double e, double f, double g) {
		return ParentElement.super.mouseScrolled(d, e, f, g);
	}

	@Override
	public boolean keyPressed(KeyInput keyInput) {
		return ParentElement.super.keyPressed(keyInput);
	}

	@Override
	public boolean keyReleased(KeyInput keyInput) {
		return ParentElement.super.keyReleased(keyInput);
	}

	@Override
	public boolean charTyped(CharInput charInput) {
		return ParentElement.super.charTyped(charInput);
	}

	@Override
	public boolean mouseClicked(Click click, boolean bl) {
		return ParentElement.super.mouseClicked(click, bl);
	}

	@Override
	public boolean mouseReleased(Click click) {
		return ParentElement.super.mouseReleased(click);
	}

	@Override
	public boolean mouseDragged(Click click, double d, double e) {
		return ParentElement.super.mouseDragged(click, d, e);
	}
}
