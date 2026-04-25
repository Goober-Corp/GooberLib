package com.goobercorp.gooberlib.screen;

import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.Tweener;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.DrawnTextConsumer;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.tab.Tab;
import net.minecraft.client.gui.tab.TabManager;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

import static com.goobercorp.gooberlib.util.RenderUtils.fillEvil;

@Environment(EnvType.CLIENT)
public class EvilTabButtonWidget extends ClickableWidget.InactivityIndicatingWidget {
    private final TabManager tabManager;
    private final Tab tab;
    private final Tweener currentTabProgress = new Tweener(() -> isCurrentTab() ? 1 : 0);
    private final Tweener isSelectedProgress = new Tweener(() -> isSelected() ? 1 : 0);

    public EvilTabButtonWidget(TabManager tabManager, Tab tab, int i, int j) {
        super(0, 0, i, j, tab.getTitle());
        this.tabManager = tabManager;
        this.tab = tab;
    }

    @Override
    public void renderWidget(DrawContext context, int i, int j, float f) {
        float yeah = (float) currentTabProgress.getLerped(4, 0);
        int ohyeah = ColorHelper.lerp((float) isSelectedProgress.get(), 0x33FFFFFF, 0xFFFFFFFF);
        int specialCol = ColorHelper.lerp((float) isSelectedProgress.get(), 0x00000000, 0xFFFFFFFF);
        int ough = ColorHelper.lerp((float) currentTabProgress.get(), 0xDB000000, 0x40000000);
        //outer black line
        drawOutsideBorder(context, yeah);

        RenderUtils.drawHorizontalLine(context, this.getX() + 1, getRight() - 2, getBottom() - 1 - yeah, ohyeah);
        RenderUtils.drawVerticalLine(context, this.getX() + 1, this.getY() + (isCurrentTab() ? 0 : 1), this.height - 1 - yeah, ohyeah);
        RenderUtils.drawVerticalLine(context, getRight() - 2, this.getY() + (isCurrentTab() ? 0 : 1), this.height - 1 - yeah, ohyeah);
        //bottom bullshit
        if (isCurrentTab()) {
            context.drawVerticalLine(getRight() - 1, getY() + 2, getY(), 0x33FFFFFF);
            context.drawVerticalLine(getX(), getY() + 2, getY(), 0x33FFFFFF);
            context.drawHorizontalLine(this.getX(), this.getX() + 1, getY(), 0xBF000000);
            context.drawHorizontalLine(this.getRight() - 1, this.getRight() - 2, getY(), 0xBF000000);
        }
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int k = this.active ? -1 : -6250336;
        fillEvil(context, this.getX() + 2, this.getY() + (isCurrentTab() ? 0 : 2), getRight() - 2, getBottom() - 1 - yeah, ough);
        if (!isCurrentTab() && !MathHelper.approximatelyEquals(isSelectedProgress.get(), 0)) {
            context.drawHorizontalLine(this.getX() + 2, getRight() - 3, getY() + 2, specialCol);
        }
        if (this.isCurrentTab()) {
            this.drawCurrentTabLine(context, textRenderer, k);
        }
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(0, (float) currentTabProgress.getLerped(0, 3));
        this.drawMessage(context.getHoverListener(this, DrawContext.HoverType.NONE));
        context.getMatrices().popMatrix();
        this.setCursor(context);
    }


    private void drawMessage(DrawnTextConsumer drawnTextConsumer) {
        int i = this.getX() + 1;
        int j = this.getY();
        int k = this.getX() + this.getWidth() - 1;
        int l = this.getY() + this.getHeight();
        drawnTextConsumer.text(this.getMessage(), i, k, j, l);
    }

    private void drawCurrentTabLine(DrawContext drawContext, TextRenderer textRenderer, int i) {
        float j = (float) (Math.min(textRenderer.getWidth(this.getMessage()), this.getWidth() - 4) * currentTabProgress.get());
        float k = this.getX() + (this.getWidth() - j) / 2F;
        float l = (this.getY());
        //TODO: unsure if i like it coming in from the bottom or top
        fillEvil(drawContext, k, l, k + j, (float) (l + 1 - (1 - currentTabProgress.get())), i);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {
        narrationMessageBuilder.put(NarrationPart.TITLE, Text.translatable("gui.narrate.tab", this.tab.getTitle()));
        narrationMessageBuilder.put(NarrationPart.HINT, this.tab.getNarratedHint());
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    public Tab getTab() {
        return this.tab;
    }

    public boolean isCurrentTab() {
        return this.tabManager.getCurrentTab() == this.tab;
    }

    public void renderForBackground(DrawContext context) {
        float yeah = (float) currentTabProgress.getLerped(4, 0);
        drawOutsideBorder(context, yeah);
    }

    private void drawOutsideBorder(DrawContext context, float yeah) {
        RenderUtils.drawHorizontalLine(context, this.getX(), getRight() - 1, getBottom() - yeah, 0xBF000000);
        RenderUtils.drawVerticalLine(context, this.getX(), this.getY() + 1, this.height - yeah, 0xBF000000);
        RenderUtils.drawVerticalLine(context, getRight() - 1, this.getY() + 1, this.height - yeah, 0xBF000000);
    }
}
