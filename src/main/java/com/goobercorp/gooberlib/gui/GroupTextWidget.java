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

@Environment(EnvType.CLIENT)
public class GroupTextWidget extends AbstractTextWidget {
    private int maxWidth = 0;
    private int cachedWidth = 0;
    private boolean cachedWidthDirty = true;
    private GroupTextWidget.TextOverflow textOverflow = GroupTextWidget.TextOverflow.CLAMPED;

    public GroupTextWidget(Text text, TextRenderer textRenderer) {
        this(0, 0, textRenderer.getWidth(text.asOrderedText()), 9, text, textRenderer);
    }

    public GroupTextWidget(int i, int j, Text text, TextRenderer textRenderer) {
        this(0, 0, i, j, text, textRenderer);
    }

    public GroupTextWidget(int i, int j, int k, int l, Text text, TextRenderer textRenderer) {
        super(i, j, k, l, text, textRenderer);
        this.active = false;
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
        //this requires the widget to be centered. fuck
        RenderUtils.drawHorizontalLine(drawContext,  this.getX() - drawContext.getScaledWindowWidth() / 2F + getTextRenderer().getWidth(message) / 2F, this.getX() - 2, this.getY() + 3, 0,  -1);
        RenderUtils.drawHorizontalLine(drawContext,  this.getX() + getTextRenderer().getWidth(message), this.getX() + getTextRenderer().getWidth(message) / 2 + drawContext.getScaledWindowWidth() / 2F - 1, this.getY() + 3, -1, 0);
        RenderUtils.drawHorizontalLine(drawContext,  this.getX() - drawContext.getScaledWindowWidth() / 2F + getTextRenderer().getWidth(message) / 2F + 1, this.getX() - 1, this.getY() + 4, 0, 0xFF3e3e3e);
        RenderUtils.drawHorizontalLine(drawContext,  this.getX() + getTextRenderer().getWidth(message) + 1, this.getX() + getTextRenderer().getWidth(message) / 2 + drawContext.getScaledWindowWidth() / 2F - 1, this.getY() + 4, 0xFF3e3e3e, 0);
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
        SCROLLING;
    }
}
