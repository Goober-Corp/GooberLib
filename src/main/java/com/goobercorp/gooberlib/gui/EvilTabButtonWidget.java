package com.goobercorp.gooberlib.gui;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.Tweener;
import net.minecraft.client.MinecraftClient;
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
import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

public class EvilTabButtonWidget extends ClickableWidget.InactivityIndicatingWidget {
	private final TabManager tabManager;
	private final Tab tab;
	private final Tweener currentTabProgress;
	private final Tweener isSelectedProgress;

	public EvilTabButtonWidget(TabManager tabManager, Tab tab, int i, int j) {
		super(0, 0, i, j, tab.getTitle());
		this.tabManager = tabManager;
		this.tab = tab;

		this.currentTabProgress = new Tweener(() -> isCurrentTab() ? 1 : isHovered() ? 0.5F : 0);
		this.isSelectedProgress = new Tweener(() -> isSelected() ? 1 : 0);
	}

	@Override
	public void renderWidget(DrawContext context, int i, int j, float f) {
		currentTabProgress.update();
		isSelectedProgress.update();

		float yeah = (float) currentTabProgress.getLerped(4, 0);
		int ohyeah = ColorHelper.lerp(isSelectedProgress.getF(), 0x33FFFFFF, MainConfig.primaryCol);
		int specialCol = ColorHelper.lerp(isSelectedProgress.getF(), 0x00000000, MainConfig.primaryCol);
		int ough = ColorHelper.lerp(currentTabProgress.getF(), 0xB0000000, 0x40000000);
		//outer black line
		drawOutsideBorder(context, yeah);

		RenderUtils.drawHorizontalLine(context, this.getX() + 1, getRight() - 2, getBottom() - 1 - yeah, ohyeah);
		RenderUtils.drawVerticalLine(context, this.getX() + 1, this.getY() + (isCurrentTab() ? 0 : 1), this.height - 1 - yeah, ohyeah);
		RenderUtils.drawVerticalLine(context, getRight() - 2, this.getY() + (isCurrentTab() ? 0 : 1), this.height - 1 - yeah, ohyeah);
		//bottom bullshit
		// kr1v: :pleading_face:
		if (isCurrentTab()) {
			context.drawVerticalLine(getRight() - 1, getY() + 2, getY(), 0x33FFFFFF);
			context.drawVerticalLine(getX(), getY() + 2, getY(), 0x33FFFFFF);
			context.drawHorizontalLine(this.getX(), this.getX() + 1, getY(), 0xBF000000);
			context.drawHorizontalLine(this.getRight() - 1, this.getRight() - 2, getY(), 0xBF000000);
		}
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		int k = this.active ? MainConfig.primaryCol : -6250336;
		fillEvil(context, this.getX() + 2, this.getY() + (isCurrentTab() ? 0 : 2), getRight() - 2, getBottom() - 1 - yeah, ough);
		if (!isCurrentTab() && !MathHelper.approximatelyEquals(isSelectedProgress.get(), 0)) {
			context.drawHorizontalLine(this.getX() + 2, getRight() - 3, getY() + 2, specialCol);
		}
		if (this.isCurrentTab()) {
			this.drawCurrentTabLine(context, textRenderer, k);
		}
		newMatrixScope(context, stack -> {
			stack.translate(0, (float) currentTabProgress.getLerped(0, 4));
			context.drawCenteredTextWithShadow(textRenderer, textRenderer.trimToWidth(this.getMessage(), this.width).getString(), (getRight() - 2) - this.getWidth() / 2 + 2, this.getY() + 5, MainConfig.primaryCol);
		});
		this.setCursor(context);
	}


	private void drawCurrentTabLine(DrawContext drawContext, TextRenderer textRenderer, int i) {
		float j = (Math.min(textRenderer.getWidth(this.getMessage()), this.getWidth() - 4) * currentTabProgress.getF());
		float k = this.getX() + (this.getWidth() - j) / 2F;
		float l = (this.getY());
		fillEvil(drawContext, k, l, k + j, (l + 1 - (1 - currentTabProgress.getF())), i);
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
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		newMatrixScope(context, stack -> {
			stack.translate(0, (float) currentTabProgress.getLerped(0, 4));
			context.drawCenteredTextWithShadow(textRenderer, textRenderer.trimToWidth(this.getMessage(), this.width).getString(), (getRight() - 2) - this.getWidth() / 2 + 2, this.getY() + 5, MainConfig.primaryCol);
		});
		int k = this.active ? MainConfig.primaryCol : -6250336;
		if (this.isCurrentTab()) {
			this.drawCurrentTabLine(context, textRenderer, k);
		}
	}
}
