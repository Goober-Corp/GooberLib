package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.EvilBaseWidget;
import com.goobercorp.gooberlib.option.individual.primitive.range.NumberRangeOption;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.Tweener;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.ScreenRect;
import net.minecraft.client.gui.cursor.StandardCursors;
import net.minecraft.client.gui.navigation.GuiNavigationType;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

// todo: make option widgets widget
//  widget? i think you meant wider
public class RangeSliderWidget extends EvilBaseWidget {
	protected double minValue;
	protected double maxValue;
	private final Supplier<Text> valueFormatter;
	protected boolean sliderFocused;
	private boolean dragging;
	private final NumberRangeOption<?> numberOption;
	private final Tweener minValTweener = new Tweener(() -> minValue);
	private final Tweener maxValTweener = new Tweener(() -> maxValue);

	public <T extends NumberRangeOption<T>> RangeSliderWidget(T numberOption, int x, int y, int width, int height, Function<T, Text> valueFormatter) {
		super(numberOption.name(), x, y, width, height);
		this.numberOption = numberOption;
		this.minValue = getInterpolatedValue(numberOption.getNumberMinValue().doubleValue(), numberOption.getDoubleMin(), numberOption.getDoubleMax());
		this.maxValue = getInterpolatedValue(numberOption.getNumberMaxValue().doubleValue(), numberOption.getDoubleMin(), numberOption.getDoubleMax());
		this.valueFormatter = () -> valueFormatter.apply(numberOption);
	}

	public <T extends NumberRangeOption<T>> RangeSliderWidget(T numberOption, int x, int y, int width, int height) {
		this(numberOption, x, y, width, height, _ -> Text.of((numberOption).getNumberMinValue() + "-" + (numberOption.getNumberMaxValue())));
	}

	@Override
	protected void drawText(DrawContext drawContext) {
		newMatrixScope(drawContext, stack -> {
//			stack.scale(0.5F, 0.5F);
			drawContext.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, valueFormatter.get(), this.getRight() - this.getWidth() / 4, this.getY() - 10, ColorHelper.withAlpha(clickTweener.getF(), MainConfig.primaryCol));
		});
		drawContext.drawText(MinecraftClient.getInstance().textRenderer, numberOption.name(), getX() + 5, getY() + MinecraftClient.getInstance().textRenderer.fontHeight / 2, MainConfig.primaryCol, true);
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
	public void renderWidget(DrawContext drawContext, double mouseX, double mouseY, float delta) {
		minValTweener.update();
		maxValTweener.update();
		float minX = getX() + (this.width - 5) / 2F;
		RenderUtils.drawHorizontalLine(drawContext, minX - 0.5F, this.getRight() - 5.5F, this.getY() + getHeight() / 2F, MainConfig.primaryCol);
		RenderUtils.drawHorizontalLine(drawContext, minX - 0.5F + 1, this.getRight() - 5.5F + 1, this.getY() + getHeight() / 2F + 1, MainConfig.shadowCol);
		RenderUtils.drawVerticalLine(drawContext, minX + (maxValTweener.getF() / 2 * (this.width - 5)) - 0.5F, getY() + 3, getBottom() - 3, MainConfig.primaryCol);
		RenderUtils.drawVerticalLine(drawContext, minX + (minValTweener.getF() / 2 * (this.width - 5)) - 0.5F, getY() + 3, getBottom() - 3, MainConfig.primaryCol);
		RenderUtils.drawVerticalLine(drawContext, (minX + (maxValTweener.getF() / 2 * (this.width - 5)) - 0.5F) + 1, getY() + 4, getBottom() - 2, MainConfig.shadowCol);
		RenderUtils.drawVerticalLine(drawContext, (minX + (minValTweener.getF() / 2 * (this.width - 5)) - 0.5F) + 1, getY() + 4, getBottom() - 2, MainConfig.shadowCol);
		RenderUtils.fillEvil(drawContext, (minX + (minValTweener.getF() / 2 * (this.width - 5)) - 0.5F) + 1, getY() + 6, (minX + (maxValTweener.getF() / 2 * (this.width - 5)) - 0.5F), getBottom() - 5, ColorHelper.withAlpha(0.5F, MainConfig.shadowCol));
		if (RenderUtils.isInBounds(mouseX, mouseY, new ScreenRect(this.getX() + this.getWidth() / 2, this.getY(), this.getRight() - 5, this.getBottom()))) {
			drawContext.setCursor(this.dragging ? StandardCursors.RESIZE_EW : StandardCursors.POINTING_HAND);
		}
	}

	@Override
	public boolean mouseScrolled(double d, double e, double f, double g) {
//		this.setValue(this.value + g / (this.width - 8));
		//TODO: make it move the nearest one? idk
		return false;
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

	private void setValueFromMouse(Click click) {
		this.setValue((click.comp_4798() - (this.getX() + ((this.width - 5) / 2F))) / (this.width - 5) * 2);
	}

	protected void setValue(double d) {
		if (Math.abs(getInterpolatedValue(d, numberOption.getDoubleMin(), numberOption.getDoubleMax()) - getInterpolatedValue(minValue, numberOption.getDoubleMin(), numberOption.getDoubleMax())) < Math.abs((getInterpolatedValue(d, numberOption.getDoubleMin(), numberOption.getDoubleMax()) - getInterpolatedValue(maxValue, numberOption.getDoubleMin(), numberOption.getDoubleMax())))) {
			this.minValue = MathHelper.clamp(d, 0, 1);
			numberOption.setMinDoubleValue(((1.0 - minValue) * numberOption.getDoubleMin() + minValue * numberOption.getDoubleMax()));
		} else {
			this.maxValue = MathHelper.clamp(d, 0, 1);
			numberOption.setMaxDoubleValue(((1.0 - maxValue) * numberOption.getDoubleMin() + maxValue * numberOption.getDoubleMax()));
		}
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
