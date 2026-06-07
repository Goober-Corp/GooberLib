package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.EvilBaseWidget;
import com.goobercorp.gooberlib.option.individual.primitive.range.NumberRangeOption;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.Tweener;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.InputType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

// todo: make option widgets widget
//  widget? i think you meant wider
public class RangeSliderWidget extends EvilBaseWidget {
	protected double minValue;
	protected double maxValue;
	private final Supplier<Component> valueFormatter;
	protected boolean sliderFocused;
	private boolean dragging;
	private final NumberRangeOption<?> numberOption;
	private final Tweener minValTweener = new Tweener(() -> minValue);
	private final Tweener maxValTweener = new Tweener(() -> maxValue);

	public <T extends NumberRangeOption<T>> RangeSliderWidget(T numberOption, int x, int y, int width, int height, Function<T, Component> valueFormatter) {
		super(numberOption.name(), x, y, width, height);
		this.numberOption = numberOption;
		this.minValue = getInterpolatedValue(numberOption.getNumberMinValue().doubleValue(), numberOption.getDoubleMin(), numberOption.getDoubleMax());
		this.maxValue = getInterpolatedValue(numberOption.getNumberMaxValue().doubleValue(), numberOption.getDoubleMin(), numberOption.getDoubleMax());
		this.valueFormatter = () -> valueFormatter.apply(numberOption);
		this.shouldDrawName = true;
	}

	public <T extends NumberRangeOption<T>> RangeSliderWidget(T numberOption, int x, int y, int width, int height) {
		this(numberOption, x, y, width, height, _ -> Component.nullToEmpty((numberOption).getNumberMinValue() + "-" + (numberOption.getNumberMaxValue())));
	}

	@Override
	protected void drawText(GuiGraphics drawContext) {
		newMatrixScope(drawContext, stack -> {
//			stack.scale(0.5F, 0.5F);
			drawContext.drawCenteredString(Minecraft.getInstance().font, valueFormatter.get(), this.getRight() - this.getWidth() / 4, this.getY() - 10, ARGB.color(clickTweener.getF(), MainConfig.primaryCol));
		});
		drawContext.drawString(Minecraft.getInstance().font, numberOption.name(), getX() + 5, getY() + Minecraft.getInstance().font.lineHeight / 2, MainConfig.primaryCol, true);
//		super.drawText(drawContext);
	}

	public static double getInterpolatedValue(double val, double min, double max) {
		return (val - min) / (max - min);
	}

	@Override
	protected MutableComponent createNarrationMessage() {
		return Component.translatable("gui.narrate.slider", this.getMessage());
	}

	@Override
	public void updateWidgetNarration(NarrationElementOutput narrationMessageBuilder) {
		narrationMessageBuilder.add(NarratedElementType.TITLE, this.createNarrationMessage());
		if (this.active) {
			if (this.isFocused()) {
				if (this.sliderFocused) {
					narrationMessageBuilder.add(NarratedElementType.USAGE, Component.translatable("narration.slider.usage.focused"));
				} else {
					narrationMessageBuilder.add(NarratedElementType.USAGE, Component.translatable("narration.slider.usage.focused.keyboard_cannot_change_value"));
				}
			} else {
				narrationMessageBuilder.add(NarratedElementType.USAGE, Component.translatable("narration.slider.usage.hovered"));
			}
		}
	}

	@Override
	public void renderWidget(GuiGraphics drawContext, double mouseX, double mouseY, float delta) {
		minValTweener.update();
		maxValTweener.update();
		float minX = getX() + (this.width - 5) / 2F;
		RenderUtils.drawHorizontalLine(drawContext, minX - 0.5F, this.getRight() - 5.5F, this.getY() + getHeight() / 2F, MainConfig.primaryCol);
		RenderUtils.drawHorizontalLine(drawContext, minX - 0.5F + 1, this.getRight() - 5.5F + 1, this.getY() + getHeight() / 2F + 1, MainConfig.shadowCol);
		RenderUtils.drawVerticalLine(drawContext, minX + (maxValTweener.getF() / 2 * (this.width - 5)) - 0.5F, getY() + 3, getBottom() - 3, MainConfig.primaryCol);
		RenderUtils.drawVerticalLine(drawContext, minX + (minValTweener.getF() / 2 * (this.width - 5)) - 0.5F, getY() + 3, getBottom() - 3, MainConfig.primaryCol);
		RenderUtils.drawVerticalLine(drawContext, (minX + (maxValTweener.getF() / 2 * (this.width - 5)) - 0.5F) + 1, getY() + 4, getBottom() - 2, MainConfig.shadowCol);
		RenderUtils.drawVerticalLine(drawContext, (minX + (minValTweener.getF() / 2 * (this.width - 5)) - 0.5F) + 1, getY() + 4, getBottom() - 2, MainConfig.shadowCol);
		RenderUtils.fillEvil(drawContext, (minX + (minValTweener.getF() / 2 * (this.width - 5)) - 0.5F) + 1, getY() + 6, (minX + (maxValTweener.getF() / 2 * (this.width - 5)) - 0.5F), getBottom() - 5, ARGB.color(0.5F, MainConfig.shadowCol));
		if (RenderUtils.isInBounds(mouseX, mouseY, new ScreenRectangle(this.getX() + this.getWidth() / 2, this.getY(), this.getRight() - 5, this.getBottom()))) {
			drawContext.requestCursor(this.dragging ? CursorTypes.RESIZE_EW : CursorTypes.POINTING_HAND);
		}
	}

	@Override
	public boolean mouseScrolled(double d, double e, double f, double g) {
//		this.setValue(this.value + g / (this.width - 8));
		//TODO: make it move the nearest one? idk
		return false;
	}

	@Override
	public void onClick(MouseButtonEvent click, boolean bl) {
		this.dragging = this.active;
		this.setValueFromMouse(click);
	}

	@Override
	public void setFocused(boolean bl) {
		super.setFocused(bl);
		if (!bl) {
			this.sliderFocused = false;
		} else {
			InputType guiNavigationType = Minecraft.getInstance().getLastInputType();
			if (guiNavigationType == InputType.MOUSE || guiNavigationType == InputType.KEYBOARD_TAB) {
				this.sliderFocused = true;
			}
		}
	}

	@Override
	public boolean keyPressed(KeyEvent keyInput) {
		if (keyInput.isSelection()) {
			this.sliderFocused = !this.sliderFocused;
			return true;
		} else {
//			if (this.sliderFocused) {
//				boolean bl = keyInput.isLeft();
//				boolean bl2 = keyInput.isRight();
//				if (bl || bl2) {
//					float f = bl ? -1.0F : 1.0F;
//					this.setValue(this.value + f / (this.width - 8));
//					return true;
//				}
//			}

			return false;
		}
	}

	private void setValueFromMouse(MouseButtonEvent click) {
		this.setValue((click.x() - (this.getX() + ((this.width - 5) / 2F))) / (this.width - 5) * 2);
	}

	protected void setValue(double d) {
		if (Math.abs(getInterpolatedValue(d, numberOption.getDoubleMin(), numberOption.getDoubleMax()) - getInterpolatedValue(minValue, numberOption.getDoubleMin(), numberOption.getDoubleMax())) < Math.abs((getInterpolatedValue(d, numberOption.getDoubleMin(), numberOption.getDoubleMax()) - getInterpolatedValue(maxValue, numberOption.getDoubleMin(), numberOption.getDoubleMax())))) {
			this.minValue = Mth.clamp(d, 0, 1);
			numberOption.setMinDoubleValue(((1.0 - minValue) * numberOption.getDoubleMin() + minValue * numberOption.getDoubleMax()));
		} else {
			this.maxValue = Mth.clamp(d, 0, 1);
			numberOption.setMaxDoubleValue(((1.0 - maxValue) * numberOption.getDoubleMin() + maxValue * numberOption.getDoubleMax()));
		}
	}

	@Override
	protected void onDrag(MouseButtonEvent click, double d, double e) {
		this.setValueFromMouse(click);
		super.onDrag(click, d, e);
	}


	@Override
	public void playDownSound(SoundManager soundManager) {
	}

	@Override
	public void onRelease(MouseButtonEvent click) {
		this.dragging = false;
		super.playDownSound(Minecraft.getInstance().getSoundManager());
	}

}
