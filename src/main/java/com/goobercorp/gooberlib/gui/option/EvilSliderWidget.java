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
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

public class EvilSliderWidget extends EvilBaseWidget {
	protected double value;
	private final Supplier<Component> valueFormatter;
	protected boolean sliderFocused;
	private boolean dragging;
	private final NumberOption<?> numberOption;
	private final Tweener valTweener = new Tweener(() -> value);
	private float scrollAmount = 0;

	public <T extends NumberOption<T>> EvilSliderWidget(T numberOption, int x, int y, int width, int height, Function<T, Component> valueFormatter) {
		super(numberOption.name(), x, y, width, height);
		this.numberOption = numberOption;
		this.value = getInterpolatedValue(numberOption.getNumberValue().doubleValue(), numberOption.getDoubleMin(), numberOption.getDoubleMax());
		this.valueFormatter = () -> valueFormatter.apply(numberOption);
		valTweener.snapToTarget();
	}

	public <T extends NumberOption<T>> EvilSliderWidget(T numberOption, int x, int y, int width, int height) {
		this(numberOption, x, y, width, height, t -> Component.nullToEmpty(t.getNumberValue().toString()));
	}

	public <T extends NumberOption<T>> EvilSliderWidget(T numberOption, int x, int y, int width, int height, boolean includeName) {
		this(numberOption, x, y, width, height, t -> Component.nullToEmpty(t.getNumberValue().toString()));
		//TODO: add functionality
		this.shouldDrawName = includeName;
	}

	@Override
	protected void drawText(GuiGraphics drawContext) {
		newMatrixScope(drawContext, stack -> {
//			stack.scale(0.5F, 0.5F);
			stack.translate(((getX() + (this.width - 5) / 2F) + (valTweener.getF() / 2 * (this.width - 5))) + 1, this.getY() - 10);
			drawContext.drawCenteredString(Minecraft.getInstance().font, valueFormatter.get(), 0, 0, ARGB.color(Math.max(clickTweener.getF(), Math.clamp(scrollAmount, 0, 1)), MainConfig.primaryCol));
		});
		drawContext.drawString(Minecraft.getInstance().font, numberOption.name(), getX() + 5, getY() + Minecraft.getInstance().font.lineHeight / 2, MainConfig.primaryCol, true);
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
		scrollAmount = (float) RenderUtils.ease(scrollAmount, 0, 5);
		RenderUtils.drawHorizontalLine(context, (getX() + (this.width - 5) / 2F) - 0.5F, this.getRight() - 5.5F, getY() + getHeight() / 2F, MainConfig.primaryCol);
		RenderUtils.drawVerticalLine(context, ((getX() + (this.width - 5) / 2F) + (valTweener.getF() / 2F * (this.width - 5)) - 0.5F) + 1, getY() + 4, getBottom() - 2, MainConfig.shadowCol);
		RenderUtils.drawHorizontalLine(context, (getX() + (this.width - 5) / 2F) - 0.5F + 1, this.getRight() - 5.5F + 1, getY() + getHeight() / 2F + 1, MainConfig.shadowCol);
		RenderUtils.drawVerticalLine(context, ((getX() + (this.width - 5) / 2F) + (valTweener.getF() / 2 * (this.width - 5)) - 0.5F), getY() + 3, getBottom() - 3, MainConfig.primaryCol);
		if (this.isHovered()) {
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
		numberOption.setDoubleValue((1.0 - value) * numberOption.getDoubleMin() + value * numberOption.getDoubleMax());
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
