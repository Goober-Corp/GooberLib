package com.goobercorp.gooberlib.gui;

import com.goobercorp.gooberlib.builder.v3.Option;
import com.goobercorp.gooberlib.util.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.gui.navigation.GuiNavigationType;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class EvilSliderWidget extends ClickableWidget.InactivityIndicatingWidget {
	protected double value;
	protected boolean sliderFocused;
	private boolean dragging;

	public EvilSliderWidget(int i, int j, int k, int l, Text text, double d) {
		super(i, j, k, l, text);
		this.value = d;
	}

	public EvilSliderWidget(Option<?> opt, int i, int j, int k, int l) {
		super(i, j, k, l, opt.name());
//        this.value = d;
	}


	@Override
	protected MutableText getNarrationMessage() {
		return Text.translatable("gui.narrate.slider", this.getMessage());
	}

	@Override
	public void appendClickableNarrations(NarrationMessageBuilder narrationMessageBuilder) {
		narrationMessageBuilder.put(NarrationPart.TITLE, this.getNarrationMessage());
		if (this.active) {
			if (this.isFocused()) {
				if (this.sliderFocused) {
					narrationMessageBuilder.put(NarrationPart.USAGE, Text.translatable("narration.slider.usage.focused"));
				} else {
					narrationMessageBuilder.put(NarrationPart.USAGE, Text.translatable("narration.slider.usage.focused.keyboard_cannot_change_value"));
				}
			} else {
				narrationMessageBuilder.put(NarrationPart.USAGE, Text.translatable("narration.slider.usage.hovered"));
			}
		}
	}

	@Override
	public void renderWidget(DrawContext drawContext, int i, int j, float f) {
		RenderUtils.fillEvil(drawContext, this.getX(), this.getY(), getRight(), getBottom(), 0x80000000);
		RenderUtils.fillEvil(drawContext, getX() + (int) (this.value * (this.width - 8)), getY(), getX() + (int) (this.value * (this.width + 8)), getBottom(), 0xFFFF0080);
		drawContext.drawText(MinecraftClient.getInstance().textRenderer, this.message, getX() + 5, getY() + MinecraftClient.getInstance().textRenderer.fontHeight / 2, -1, true);
//		this.drawTextWithMargin(drawContext.getHoverListener(this, DrawContext.HoverType.NONE), this.getMessage(), 2);
		if (this.isHovered()) {
			drawContext.setCursor(this.dragging ? StandardCursors.RESIZE_EW : StandardCursors.POINTING_HAND);
		}
	}

	@Override
	public void onClick(Click click, boolean bl) {
		this.dragging = this.active;
		this.setValueFromMouse(click);
	}

	@Override
	public void setFocused(boolean bl) {
		super.setFocused(bl);
		if (!bl) {
			this.sliderFocused = false;
		} else {
			GuiNavigationType guiNavigationType = MinecraftClient.getInstance().getNavigationType();
			if (guiNavigationType == GuiNavigationType.MOUSE || guiNavigationType == GuiNavigationType.KEYBOARD_TAB) {
				this.sliderFocused = true;
			}
		}
	}

	@Override
	public boolean keyPressed(KeyInput keyInput) {
		if (keyInput.isEnterOrSpace()) {
			this.sliderFocused = !this.sliderFocused;
			return true;
		} else {
			if (this.sliderFocused) {
				boolean bl = keyInput.isLeft();
				boolean bl2 = keyInput.isRight();
				if (bl || bl2) {
					float f = bl ? -1.0F : 1.0F;
					this.setValue(this.value + f / (this.width - 8));
					return true;
				}
			}

			return false;
		}
	}

	private void setValueFromMouse(Click click) {
		this.setValue((click.comp_4798() - (this.getX() + 4)) / (this.width - 8));
	}

	protected void setValue(double d) {
		double e = this.value;
		this.value = MathHelper.clamp(d, 0.0, 1.0);
		if (e != this.value) {
			this.applyValue();
		}

		this.updateMessage();
	}

	@Override
	protected void onDrag(Click click, double d, double e) {
		this.setValueFromMouse(click);
		super.onDrag(click, d, e);
	}

	@Override
	public void playDownSound(SoundManager soundManager) {
	}

	@Override
	public void onRelease(Click click) {
		this.dragging = false;
		super.playDownSound(MinecraftClient.getInstance().getSoundManager());
	}

	protected void updateMessage() {

	}

	protected void applyValue() {

	}
}
