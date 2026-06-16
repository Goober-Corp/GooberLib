package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.config.MainConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

public class EvilStringWidgetWithName extends EvilStringWidget {
	private final Component name;
	private final int x;
	private boolean drawInside;

	public EvilStringWidgetWithName(Component name, int x, int y, int width, int height, @Nullable Consumer<String> changedListener, Predicate<String> predicate, Predicate<String> immediatePredicate, String initial, Consumer<EvilStringWidget> onReset) {
		super(x + font().width(name), y, width - font().width(name), height, changedListener, predicate, immediatePredicate, initial, onReset);
		this.name = name;
		this.x = x;
		this.shouldDrawName = true;
		textXTweener.snapToTarget();
	}

	public EvilStringWidgetWithName(Component name, int x, int y, int width, int height, @Nullable Consumer<String> changedListener, Predicate<String> predicate, Predicate<String> immediatePredicate, String initial, Consumer<EvilStringWidget> onReset, boolean alignRight, boolean centered, boolean drawInside) {
		super(drawInside ? x : x + font().width(name), y, drawInside ? width : width - font().width(name), height, changedListener, predicate, immediatePredicate, initial, onReset);
		this.name = name;
		this.x = drawInside ? x + font().width(name) : x;
		this.shouldDrawName = true;
		this.centered = centered;
		this.alignRight = alignRight;
		this.drawInside = drawInside;
		if (drawInside) {
			this.centered = false;
			this.alignRight = true;
		}
		this.updateTextPosition();
		textXTweener.snapToTarget();
	}

	public static Font font() {
		return Minecraft.getInstance().font;
	}

	@Override
	public void renderWidget(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
		super.renderWidget(drawContext, mouseX, mouseY, delta);
		newMatrixScope(drawContext, stack -> {
			if (drawInside) {
				stack.translate(horizontalPosOffset, verticalPosOffset);
			}
			drawContext.drawString(Minecraft.getInstance().font, name, this.getX() + (drawInside ? 5 : -font().width(name)), this.getY() + this.height / 2 - Minecraft.getInstance().font.lineHeight / 2, MainConfig.primaryCol);
		});
	}

	@Override
	protected void updateTextPosition() {
		if (this.textRenderer != null) {
			String string = this.textRenderer.plainSubstrByWidth(this.getText().substring(this.firstCharacterIndex), this.getInnerWidth());
			if (this.isCentered()) {
				this.textXTweener.setTarget(this.getX() + (this.width / 2) - (textRenderer.width(string) / 2));
			} else {
				if (alignRight) {
					//TODO: take into account width of the cursor
					if (drawInside) {
						//TODO: kr1v please fix this
						this.textXTweener.setTarget(this.getRight() - x - textRenderer.width(string));
					} else {
						this.textXTweener.setTarget(this.getRight() - 10 - textRenderer.width(string));
					}
				} else {
					this.textXTweener.setTarget(this.getX() + (this.drawsBackground() ? 4 : 0));
				}
			}
			this.textY = this.drawsBackground() ? this.getY() + (this.height - 8) / 2 : this.getY();
			textXTweener.update();
		}
	}
}
