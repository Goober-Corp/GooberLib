package com.goobercorp.gooberlib.gui;

import com.goobercorp.gooberlib.util.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.DrawnTextConsumer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.AbstractTextWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Language;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class GroupTextWidget extends AbstractTextWidget {
	private int maxWidth = 0;
	private int cachedWidth = 0;
	private boolean cachedWidthDirty = true;
	private GroupTextWidget.TextOverflow textOverflow = GroupTextWidget.TextOverflow.CLAMPED;
	public float renderProgress = 0;

	public GroupTextWidget(Text text, TextRenderer textRenderer) {
		this(0, 0, textRenderer.getWidth(text.asOrderedText()), 9, text, textRenderer);
	}

	public GroupTextWidget(int i, int j, Text text, TextRenderer textRenderer) {
		this(0, 0, i, j, text, textRenderer);
	}

	public GroupTextWidget(int i, int j, int k, int l, Text text, TextRenderer textRenderer) {
		super(i, j, k, l, text, textRenderer);
	}

	@Override
	public void setMessage(Text text) {
		super.setMessage(text);
		this.cachedWidthDirty = true;
	}

	public GroupTextWidget setMaxWidth(int i) {
		return this.setMaxWidth(i, GroupTextWidget.TextOverflow.CLAMPED);
	}

	public GroupTextWidget setMaxWidth(int i, GroupTextWidget.TextOverflow textOverflow) {
		this.maxWidth = i;
		this.textOverflow = textOverflow;
		return this;
	}

	@Override
	public int getWidth() {
		if (this.maxWidth > 0) {
			if (this.cachedWidthDirty) {
				this.cachedWidth = Math.min(this.maxWidth, this.getTextRenderer().getWidth(this.getMessage().asOrderedText()));
				this.cachedWidthDirty = false;
			}

			return this.cachedWidth;
		} else {
			return super.getWidth();
		}
	}


	@Override
	public void renderWidget(DrawContext drawContext, int i, int j, float f) {
		super.renderWidget(drawContext, i, j, f);
		renderProgress = (float) RenderUtils.ease(renderProgress, 1, 5F);
		//this requires the widget to be centered. fuck
		float yeah = this.getX() - drawContext.getScaledWindowWidth() / 2F + getTextRenderer().getWidth(message) / 2F;
		RenderUtils.drawThinningHorizontalLine(drawContext, MathHelper.lerp(1 - renderProgress, yeah, this.getX() - 2), this.getX() - 2, this.getY() + 4.5F, 0, 0x80000000, 2.25F, false);
		RenderUtils.drawThinningHorizontalLine(drawContext, MathHelper.lerp(1 - renderProgress, yeah, this.getX() - 2), this.getX() - 2, this.getY() + 3.5F, 0, -1, 2.25F, false);
		int otherYeah = this.getX() + getTextRenderer().getWidth(message);
		RenderUtils.drawThinningHorizontalLine(drawContext, otherYeah + 1, MathHelper.lerp(1 - renderProgress, this.getX() + getTextRenderer().getWidth(message) / 2f + drawContext.getScaledWindowWidth() / 2F - 1, otherYeah), this.getY() + 4.5F, 0xFF3e3e3e, 0, 2.25F, true);
		RenderUtils.drawThinningHorizontalLine(drawContext, otherYeah, MathHelper.lerp(1 - renderProgress, this.getX() + getTextRenderer().getWidth(message) / 2f + drawContext.getScaledWindowWidth() / 2F - 1, otherYeah), this.getY() + 3.5F, -1, 0, 2.25F, true);
	}

	@Override
	public void draw(DrawnTextConsumer drawnTextConsumer) {
		Text text = this.getMessage();
		TextRenderer textRenderer = this.getTextRenderer();
		int i = this.maxWidth > 0 ? this.maxWidth : this.getWidth();
		int j = textRenderer.getWidth(text);
		int k = this.getX();
		int l = this.getY() + (this.getHeight() - 9) / 2;
		boolean bl = j > i;
		if (bl) {
			switch (this.textOverflow) {
				case CLAMPED:
					drawnTextConsumer.text(k, l, trim(text, textRenderer, i));
					break;
				case SCROLLING:
					this.drawTextWithMargin(drawnTextConsumer, text, 2);
			}
		} else {
			drawnTextConsumer.text(k, l, text.asOrderedText());
		}
	}

	public static OrderedText trim(Text text, TextRenderer textRenderer, int i) {
		StringVisitable stringVisitable = textRenderer.trimToWidth(text, i - textRenderer.getWidth(ScreenTexts.ELLIPSIS));
		return Language.getInstance().reorder(StringVisitable.concat(stringVisitable, ScreenTexts.ELLIPSIS));
	}

	@Environment(EnvType.CLIENT)
	public enum TextOverflow {
		CLAMPED,
		SCROLLING,
	}
}
