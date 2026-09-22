package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.EvilBaseWidget;
import com.goobercorp.gooberlib.option.individual.java.CycleOption;
import com.goobercorp.gooberlib.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

import java.util.function.Function;

public class DropdownOptionWidget<E> extends EvilBaseWidget {
	public final Function<E, Component> valueFormatter;
	public final CycleOption<E> option;
	public boolean open = false;

	public DropdownOptionWidget(CycleOption<E> option, int x, int y, int width, int height, Function<E, Component> valueFormatter) {
		super(option.name(), x, y, width, height);
		this.option = option;
		this.valueFormatter = valueFormatter;
		this.shouldDrawName = true;
	}

	@Override
	protected void renderWidget(GuiGraphics context, int mouseX, int mouseY, float f) {
		hoverTweener.update();
		context.drawString(Minecraft.getInstance().font, this.option.name(), this.getX() + 4, this.getY() + 4, MainConfig.primaryCol);
		int dropdownWidth = this.getWidth() / 3;
		int dropdownX = this.getX() + 2 * dropdownWidth;
		RenderUtils.drawBoxOutline(context, dropdownX, this.getY(), dropdownX + dropdownWidth, this.getHeight(), ARGB.srgbLerp(hoverTweener.getF(), 0xFF000000, MainConfig.primaryCol));
		context.drawString(Minecraft.getInstance().font, this.valueFormatter.apply(this.option.value), dropdownX + 4, this.getY() + 4, MainConfig.primaryCol);
		if (open) {
			for (int i = 0; i < this.option.getPossibleOptions().size(); i++) {
				E possibleOption = this.option.getPossibleOptions().get(i);
				int optionY = this.getY() + this.getHeight() * (i + 1);
				context.drawString(Minecraft.getInstance().font, this.valueFormatter.apply(possibleOption), dropdownX + 4, optionY + 4, MainConfig.primaryCol);
				if (possibleOption == this.option.value) {
					RenderUtils.drawBoxOutline(context, dropdownX, optionY, dropdownX + dropdownWidth, optionY + this.getHeight(), MainConfig.primaryCol);
				} else {
					RenderUtils.drawBoxOutline(context, dropdownX, optionY, dropdownX + dropdownWidth, optionY + this.getHeight(), 0xFF000000);
				}
			}
		} else {
		}
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent click, boolean bl) {
		if (this.getY() < click.y() && this.getY() + this.getHeight() > click.y() &&
				this.getX() + 2 * (this.getWidth() / 3f) < click.x() && this.getX() + this.getWidth() > click.x()) {
			this.open = !this.open;
			return true;
		}
		return false;
	}
}
