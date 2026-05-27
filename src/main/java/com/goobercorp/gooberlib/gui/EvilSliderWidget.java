package com.goobercorp.gooberlib.gui;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.option.individual.primitive.NumberOption;
import com.goobercorp.gooberlib.util.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.gui.navigation.GuiNavigationType;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

// todo: make option widgets widget
//  widget? i think you meant wider
public class EvilSliderWidget extends EvilBaseWidget {
	protected double value;
	private final Supplier<Text> valueFormatter;
	protected boolean sliderFocused;
	private boolean dragging;
	private final NumberOption<?> numberOption;

	public <T extends NumberOption<T>> EvilSliderWidget(T numberOption, int x, int y, int width, int height, Function<T, Text> valueFormatter) {
		super((BaseOption<?>) numberOption, x, y, width, height);
		this.numberOption = numberOption;
		this.value = getInterpolatedValue(numberOption.getDoubleValue().doubleValue(), numberOption.getDoubleMin(), numberOption.getDoubleMax());
		this.valueFormatter = () -> valueFormatter.apply(numberOption);
	}

	public <T extends NumberOption<T>> EvilSliderWidget(T numberOption, int x, int y, int width, int height) {
		this(numberOption, x, y, width, height, t -> t.name().copy().append(": " + t.getDoubleValue()));
	}

	@Override
	protected void drawText(DrawContext drawContext) {
		drawContext.drawText(MinecraftClient.getInstance().textRenderer, this.valueFormatter.get(), getX() + 5, getY() + MinecraftClient.getInstance().textRenderer.fontHeight / 2, -1, true);
//		super.drawText(drawContext);
	}

	public static double getInterpolatedValue(double val, double min, double max) {
		return (val - min) / (max - min);
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
		newMatrixScope(drawContext, stack -> {
//			stack.translate(horizontalPosOffset, verticalPosOffset);
			//TODO: properly center
//			RenderUtils.fillEvil(drawContext, this.getX(), this.getY(), getRight() + 4, getBottom(), MainConfig.bgColor);
			super.renderWidget(drawContext, i, j, f);
			RenderUtils.fillEvil(drawContext, (float) (getX() + (this.value * (this.width))), (float) getY(), (float) (getX() + (this.value * (this.width)) + 4), getBottom(), MainConfig.primaryCol);
//			drawContext.drawText(MinecraftClient.getInstance().textRenderer, this.valueFormatter.get(), getX() + 5, getY() + MinecraftClient.getInstance().textRenderer.fontHeight / 2, -1, true);
			if (this.isHovered()) {
				drawContext.setCursor(this.dragging ? StandardCursors.RESIZE_EW : StandardCursors.POINTING_HAND);
			}
		});
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
		this.value = MathHelper.clamp(d, 0.0, 1.0);
		numberOption.setDoubleValue((1.0 - value) * numberOption.getDoubleMin() + value * numberOption.getDoubleMax());
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
}
