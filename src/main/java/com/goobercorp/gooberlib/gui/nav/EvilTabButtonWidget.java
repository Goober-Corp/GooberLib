package com.goobercorp.gooberlib.gui.nav;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.Tweener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

import static com.goobercorp.gooberlib.util.RenderUtils.fillEvil;
import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

public class EvilTabButtonWidget extends AbstractWidget.WithInactiveMessage {
	private final TabManager tabManager;
	private final Tab tab;
	private final Tweener currentTabProgress;
	private final Tweener isSelectedProgress;

	public EvilTabButtonWidget(TabManager tabManager, Tab tab, int i, int j) {
		super(0, 0, i, j, tab.getTabTitle());
		this.tabManager = tabManager;
		this.tab = tab;

		this.currentTabProgress = new Tweener(() -> isCurrentTab() ? 1 : isHovered() ? 0.5F : 0);
		this.isSelectedProgress = new Tweener(() -> isHoveredOrFocused() ? 1 : 0);
	}

	@Override
	public void renderWidget(GuiGraphics context, int i, int j, float f) {
		currentTabProgress.update();
		isSelectedProgress.update();

		float yeah = (float) currentTabProgress.getLerped(4, 0);
		int ohyeah = ARGB.srgbLerp(isSelectedProgress.getF(), 0x33FFFFFF, MainConfig.primaryCol);
		int specialCol = ARGB.srgbLerp(isSelectedProgress.getF(), 0x00000000, MainConfig.primaryCol);
		int ough = ARGB.srgbLerp(currentTabProgress.getF(), 0xB0000000, 0x40000000);
		//outer black line
		drawOutsideBorder(context, yeah);

		RenderUtils.drawHorizontalLine(context, this.getX() + 1, getRight() - 2, getBottom() - 1 - yeah, ohyeah);
		RenderUtils.drawVerticalLine(context, this.getX() + 1, this.getY() + (isCurrentTab() ? 0 : 1), this.height - 1 - yeah, ohyeah);
		RenderUtils.drawVerticalLine(context, getRight() - 2, this.getY() + (isCurrentTab() ? 0 : 1), this.height - 1 - yeah, ohyeah);
		//bottom bullshit
		// kr1v: 🥺
		if (isCurrentTab()) {
			context.vLine(getRight() - 1, getY() + 2, getY(), 0x33FFFFFF);
			context.vLine(getX(), getY() + 2, getY(), 0x33FFFFFF);
			context.hLine(this.getX(), this.getX() + 1, getY(), 0xBF000000);
			context.hLine(this.getRight() - 1, this.getRight() - 2, getY(), 0xBF000000);
		}
		Font textRenderer = Minecraft.getInstance().font;
		int k = this.active ? MainConfig.primaryCol : -6250336;
		fillEvil(context, this.getX() + 2, this.getY() + (isCurrentTab() ? 0 : 2), getRight() - 2, getBottom() - 1 - yeah, ough);
		if (!isCurrentTab() && !Mth.equal(isSelectedProgress.get(), 0)) {
			context.hLine(this.getX() + 2, getRight() - 3, getY() + 2, specialCol);
		}
		if (this.isCurrentTab()) {
			this.drawCurrentTabLine(context, textRenderer, k);
		}
		newMatrixScope(context, stack -> {
			stack.translate(0, (float) currentTabProgress.getLerped(0, 4));
			context.drawCenteredString(textRenderer, textRenderer.substrByWidth(this.getMessage(), this.width).getString(), (getRight() - 2) - this.getWidth() / 2 + 2, this.getY() + 5, MainConfig.primaryCol);
		});
		this.handleCursor(context);
	}


	private void drawCurrentTabLine(GuiGraphics drawContext, Font textRenderer, int i) {
		float j = (Math.min(textRenderer.width(this.getMessage()), this.getWidth() - 4) * currentTabProgress.getF());
		float k = this.getX() + (this.getWidth() - j) / 2F;
		float l = (this.getY());
		fillEvil(drawContext, k, l, k + j, (l + 1 - (1 - currentTabProgress.getF())), i);
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationMessageBuilder) {
		narrationMessageBuilder.add(NarratedElementType.TITLE, Component.translatable("gui.narrate.tab", this.tab.getTabTitle()));
		narrationMessageBuilder.add(NarratedElementType.HINT, this.tab.getTabExtraNarration());
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

	public void renderForBackground(GuiGraphics context) {
		float yeah = (float) currentTabProgress.getLerped(4, 0);
		drawOutsideBorder(context, yeah);
	}

	private void drawOutsideBorder(GuiGraphics context, float yeah) {
		RenderUtils.drawHorizontalLine(context, this.getX(), getRight() - 1, getBottom() - yeah, 0xBF000000);
		RenderUtils.drawVerticalLine(context, this.getX(), this.getY() + 1, this.height - yeah, 0xBF000000);
		RenderUtils.drawVerticalLine(context, getRight() - 1, this.getY() + 1, this.height - yeah, 0xBF000000);
		Font textRenderer = Minecraft.getInstance().font;
		newMatrixScope(context, stack -> {
			stack.translate(0, (float) currentTabProgress.getLerped(0, 4));
			context.drawCenteredString(textRenderer, textRenderer.substrByWidth(this.getMessage(), this.width).getString(), (getRight() - 2) - this.getWidth() / 2 + 2, this.getY() + 5, MainConfig.primaryCol);
		});
		int k = this.active ? MainConfig.primaryCol : -6250336;
		if (this.isCurrentTab()) {
			this.drawCurrentTabLine(context, textRenderer, k);
		}
	}
}
