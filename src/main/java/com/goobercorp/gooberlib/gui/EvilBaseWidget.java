package com.goobercorp.gooberlib.gui;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.Tweener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;

import java.util.function.Function;

import static com.goobercorp.gooberlib.util.RenderUtils.ease;
import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

public class EvilBaseWidget extends AbstractWidget {
	protected Tweener hoverTweener;
	protected boolean mouseDown = false;
	protected float verticalPosOffset = 0;
	protected float horizontalPosOffset = 0;
	protected Tweener clickTweener;
	protected boolean centerName;
	protected boolean shouldDrawName;
	protected final Component name;
	private final Function<BaseOption<?>, Component> valueFormatter;

	public EvilBaseWidget(Component name, int x, int y, int width, int height, Function<BaseOption<?>, Component> valueFormatter) {
		super(x, y, width, height, name);
		this.name = name;
		this.valueFormatter = valueFormatter;
		hoverTweener = new Tweener(() -> this.isHovered || mouseDown ? 1 : 0, 10);
		clickTweener = new Tweener(() -> this.mouseDown ? 1 : 0);
	}

	public EvilBaseWidget(Component name, int x, int y, int width, int height) {
		this(name, x, y, width, height, BaseOption::name);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent click, boolean bl) {
		if (super.mouseClicked(click, bl)) {
			mouseDown = true;
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent click) {
		mouseDown = false;
		return super.mouseReleased(click);
	}

	/// Override this if you don't want the moving effect
	@Override
	protected void renderWidget(GuiGraphics drawContext, int i, int j, float f) {
		if (!mouseDown) {
			verticalPosOffset = (float) ease(verticalPosOffset, 0, 15);
			horizontalPosOffset = (float) ease(horizontalPosOffset, 0, 15);
		}
		hoverTweener.update();
		clickTweener.update();
//		stack.translate(horizontalPosOffset, verticalPosOffset);

		newMatrixScope(drawContext, stack -> {
			stack.translate(horizontalPosOffset, verticalPosOffset);
			drawContext.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath("gooberlib", "widget/button"), this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0x80808080);
			drawText(drawContext);
			RenderUtils.drawBoxOutline(drawContext, this.getX() + clickTweener.getF(), this.getY() + clickTweener.getF(), this.getRight() - 1 - clickTweener.getF(), this.getBottom() - 1 - clickTweener.getF(), ARGB.srgbLerp(hoverTweener.getF(), 0xFF000000, MainConfig.primaryCol));

			double mouseX = Minecraft.getInstance().mouseHandler.getScaledXPos(Minecraft.getInstance().getWindow());
			double mouseY = Minecraft.getInstance().mouseHandler.getScaledYPos(Minecraft.getInstance().getWindow());
			this.renderWidget(drawContext, mouseX, mouseY, f);
		});
	}

	/// Override this if you want the moving effect
	public void renderWidget(GuiGraphics context, double mouseX, double mouseY, float delta) {
	}

	protected void drawText(GuiGraphics drawContext) {
		if (shouldDrawName) {
			if (centerName) {
				drawContext.drawCenteredString(Minecraft.getInstance().font, name, this.getX() + this.width / 2, this.getY() + this.height / 2 - Minecraft.getInstance().font.lineHeight / 2, MainConfig.primaryCol);
			} else {
				drawContext.drawString(Minecraft.getInstance().font, name, this.getX() + 5, this.getY() + this.height / 2 - Minecraft.getInstance().font.lineHeight / 2, MainConfig.primaryCol);
			}
		}
	}

	@Override
	public boolean mouseDragged(MouseButtonEvent click, double d, double e) {
		this.verticalPosOffset += (float) e * 0.025F * Math.min(1 / Math.abs(verticalPosOffset) / 2, 1);
		this.horizontalPosOffset += (float) d * 0.025F * Math.min(1 / Math.abs(horizontalPosOffset) / 2, 1);
		return super.mouseDragged(click, d, e);
	}

	public Function<BaseOption<?>, Component> getValueFormatter() {
		return valueFormatter;
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationMessageBuilder) {
	}
}
