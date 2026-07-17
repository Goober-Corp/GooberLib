package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.EvilBaseWidget;
import com.goobercorp.gooberlib.option.individual.primitive.NumberOption;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.Tweener;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import net.minecraft.client.InputType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

public class EvilSliderWidget extends EvilBaseWidget {
	protected double value;
	private final double spaceBetweenSteps;
	private final Supplier<Component> valueFormatter;
	protected boolean sliderFocused;
	private boolean dragging;
	private final NumberOption<?> numberOption;
	private final Tweener valTweener = new Tweener(() -> value);
	private float scrollAmount = 0;
	//if true, the slider will fill the space available to it, otherwise will stick to 50%. don't forget about shouldDrawName though
	private final boolean flexible = false;

	public <T extends NumberOption<T>> EvilSliderWidget(T numberOption, int x, int y, int width, int height, Function<T, Component> valueFormatter, double spaceBetweenSteps) {
		super(numberOption.name(), x, y, width, height);
		this.numberOption = numberOption;
		this.value = getInterpolatedValue(numberOption.getNumberValue().doubleValue(), numberOption.getDoubleMin(), numberOption.getDoubleMax());
		this.spaceBetweenSteps = spaceBetweenSteps;
		this.valueFormatter = () -> valueFormatter.apply(numberOption);
		valTweener.snapToTarget();
	}

	public <T extends NumberOption<T>> EvilSliderWidget(T numberOption, int x, int y, int width, int height) {
		this(numberOption, x, y, width, height, t -> Component.nullToEmpty(t.getNumberValue().toString()), 0);
	}

	public <T extends NumberOption<T>> EvilSliderWidget(T numberOption, int x, int y, int width, int height, boolean includeName) {
		this(numberOption, x, y, width, height, t -> Component.nullToEmpty(t.getNumberValue().toString()), 0);
		//TODO: add functionality
		this.shouldDrawName = includeName;
	}

	@Override
	protected void drawText(GuiGraphics drawContext) {
		drawContext.drawString(Minecraft.getInstance().font, numberOption.name(), getX() + 5, getY() + Minecraft.getInstance().font.lineHeight / 2, getColor(), true);
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
	public void renderWidget(GuiGraphics context, double mouseX, double mouseY, float delta) {
		valTweener.update();
		active = numberOption.isEnabled();
		scrollAmount = (float) RenderUtils.ease(scrollAmount, 0, 5);
		newMatrixScope(context, stack -> {
			float yeah = getX() + (this.width - 5) / 2F;
			stack.translate(yeah, 0);
			stack.scale(hoverTweener.getF(), 1);
			RenderUtils.drawHorizontalLine(context, -0.5F, this.getRight() - 5.5F - yeah, getY() + getHeight() / 2F, MainConfig.primaryCol);
			RenderUtils.drawVerticalLine(context, ((valTweener.getF() / 2F * (this.width - 5)) - 0.5F) + 1, getY() + 4, getBottom() - 2, MainConfig.shadowCol);
			RenderUtils.drawHorizontalLine(context, -0.5F + 1, this.getRight() - 5.5F + 1 - yeah, getY() + getHeight() / 2F + 1, MainConfig.shadowCol);
			RenderUtils.drawVerticalLine(context, ((valTweener.getF() / 2 * (this.width - 5)) - 0.5F), getY() + 3, getBottom() - 3, MainConfig.primaryCol);
		});
		newMatrixScope(context, stack -> {
//			stack.scale(0.5F, 0.5F);
			//TODO: standardize horizontal padding of 5
			float xOffset = Mth.lerp(hoverTweener.getF(), getRight() - 5 - Minecraft.getInstance().font.width(valueFormatter.get()), ((getX() + (this.width - 5) / 2F) + (valTweener.getF() / 2 * (this.width - 5))) + 1 - Minecraft.getInstance().font.width(valueFormatter.get()) / 2F);
			stack.translate(xOffset, Mth.lerp(hoverTweener.getF(), 9 / 2F, this.getY() - 10));
			context.drawString(Minecraft.getInstance().font, valueFormatter.get(), 0, 0, getColor());
		});
		if (this.isHovered() && active) {
			context.requestCursor(this.dragging ? CursorTypes.RESIZE_EW : CursorTypes.POINTING_HAND);
		}
	}


	@Override
	public boolean mouseScrolled(double d, double e, double f, double g) {
		scrollAmount += (float) Math.abs(g);
		this.setValue(this.value + g / (this.width - 8));
		return true;
	}

	@Override
	public void onClick(MouseButtonEvent click, boolean bl) {

		this.dragging = this.active;
		this.setValueFromMouse(click);
	}

	@Override
	public void onFocus() {
		InputType guiNavigationType = Minecraft.getInstance().getLastInputType();
		if (guiNavigationType == InputType.MOUSE || guiNavigationType == InputType.KEYBOARD_TAB) {
			this.sliderFocused = true;
		}
	}

	@Override
	public void onLostFocus() {
		this.sliderFocused = false;
	}

	@Override
	public boolean keyPressed(KeyEvent keyInput) {
		if (keyInput.isSelection()) {
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

	private void setValueFromMouse(MouseButtonEvent click) {
		this.setValue((click.x() - (this.getX() + ((this.width - 5) / 2F))) / (this.width - 5) * 2);
	}

	protected void setValue(double d) {
		this.value = Mth.clamp(d, 0.0, 1.0);
		double doubleVal = (1.0 - value) * numberOption.getDoubleMin() + value * numberOption.getDoubleMax();
		if (spaceBetweenSteps != 0) {
			//TODO: add checks for negative? or clamp?
			doubleVal = doubleVal % spaceBetweenSteps;
		}
		numberOption.setDoubleValue(doubleVal);
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

	@Override
	public void reset() {
		numberOption.resetToDefault();
		this.value = getInterpolatedValue(numberOption.getNumberValue().doubleValue(), numberOption.getDoubleMin(), numberOption.getDoubleMax());
	}
}
