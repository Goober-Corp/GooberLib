package com.goobercorp.gooberlib.gui.nav;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.util.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ActiveTextCollector;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractStringWidget;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class GroupDividerWidget extends AbstractStringWidget {
	private int maxWidth = 0;
	private int cachedWidth = 0;
	private boolean cachedWidthDirty = true;
	private GroupDividerWidget.TextOverflow textOverflow = GroupDividerWidget.TextOverflow.CLAMPED;
	public float renderProgress = 0;
	public boolean isCollapsed;

	public GroupDividerWidget(Component text, Font textRenderer) {
		this(0, 0, textRenderer.width(text.getVisualOrderText()), 9, text, textRenderer);
	}

	public GroupDividerWidget(int i, int j, Component text, Font textRenderer) {
		this(0, 0, i, j, text, textRenderer);
	}

	public GroupDividerWidget(int i, int j, int k, int l, Component text, Font textRenderer) {
		super(i, j, k, l, text, textRenderer);
	}

	@Override
	public void setMessage(Component text) {
		super.setMessage(text);
		this.cachedWidthDirty = true;
	}

	public GroupDividerWidget setMaxWidth(int i) {
		return this.setMaxWidth(i, GroupDividerWidget.TextOverflow.CLAMPED);
	}

	public GroupDividerWidget setMaxWidth(int i, GroupDividerWidget.TextOverflow textOverflow) {
		this.maxWidth = i;
		this.textOverflow = textOverflow;
		return this;
	}

	@Override
	public int getWidth() {
		if (this.maxWidth > 0) {
			if (this.cachedWidthDirty) {
				this.cachedWidth = Math.min(this.maxWidth, this.getFont().width(this.getMessage().getVisualOrderText()));
				this.cachedWidthDirty = false;
			}

			return this.cachedWidth;
		} else {
			return super.getWidth();
		}
	}


	@Override
	public void renderWidget(GuiGraphics drawContext, int i, int j, float f) {
		super.renderWidget(drawContext, i, j, f);
		renderProgress = (float) RenderUtils.ease(renderProgress, 1, 5F);
		//this requires the widget to be centered. fuck
		float yeah = this.getX() - getWidth() / 2F + getFont().width(message) / 2F;
		RenderUtils.drawThinningHorizontalLine(drawContext, Mth.lerp(1 - renderProgress, yeah, this.getX() - 2), this.getX() - 2, this.getY() + 4.5F, 0, MainConfig.shadowCol, 2.25F, false);
		RenderUtils.drawThinningHorizontalLine(drawContext, Mth.lerp(1 - renderProgress, yeah, this.getX() - 2), this.getX() - 2, this.getY() + 3.5F, 0, MainConfig.primaryCol, 2.25F, false);
		int otherYeah = this.getX() + getFont().width(message);
		RenderUtils.drawThinningHorizontalLine(drawContext, otherYeah + 1, Mth.lerp(1 - renderProgress, this.getX() + getFont().width(message) / 2f + getWidth() / 2F - 1, otherYeah), this.getY() + 4.5F, MainConfig.shadowCol, 0, 2.25F, true);
		RenderUtils.drawThinningHorizontalLine(drawContext, otherYeah, Mth.lerp(1 - renderProgress, this.getX() + getFont().width(message) / 2f + getWidth() / 2F - 1, otherYeah), this.getY() + 3.5F, MainConfig.primaryCol, 0, 2.25F, true);
		drawContext.drawCenteredString(Minecraft.getInstance().font, message, Minecraft.getInstance().font.width(message) / 2, 0, MainConfig.primaryCol);
	}

	@Override
	public void visitLines(ActiveTextCollector drawnTextConsumer) {
//		Text text = this.getMessage();
//		TextRenderer textRenderer = this.getTextRenderer();
//		int i = this.maxWidth > 0 ? this.maxWidth : this.getWidth();
//		int j = textRenderer.getWidth(text);
//		int k = this.getX();
//		int l = this.getY() + (this.getHeight() - 9) / 2;
//		boolean bl = j > i;
//		if (bl) {
//			switch (this.textOverflow) {
//				case CLAMPED:
//					drawnTextConsumer.text(k, l, trim(text, textRenderer, i));
//					break;
//				case SCROLLING:
//					this.drawTextWithMargin(drawnTextConsumer, text, 2);
//			}
//		} else {
//			drawnTextConsumer.text(k, l, text.asOrderedText());
//		}
	}

	public static FormattedCharSequence trim(Component text, Font textRenderer, int i) {
		FormattedText stringVisitable = textRenderer.substrByWidth(text, i - textRenderer.width(CommonComponents.ELLIPSIS));
		return Language.getInstance().getVisualOrder(FormattedText.composite(stringVisitable, CommonComponents.ELLIPSIS));
	}

	@Override
	public void onClick(MouseButtonEvent mouseButtonEvent, boolean bl) {
		this.isCollapsed = !this.isCollapsed;
		super.onClick(mouseButtonEvent, bl);
	}

	@Environment(EnvType.CLIENT)
	public enum TextOverflow {
		CLAMPED,
		SCROLLING,
	}
}
