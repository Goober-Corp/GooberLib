package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.gui.EvilBaseWidget;
import com.goobercorp.gooberlib.option.individual.java.CycleOption;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

import java.util.function.Function;
import java.util.function.Supplier;

public class DropdownOptionWidget extends EvilBaseWidget {
	public final Supplier<Component> valueFormatter;
	public final CycleOption<?> option;
	public boolean open = false;

	public <E> DropdownOptionWidget(CycleOption<E> option, int x, int y, int width, int height, Function<CycleOption<E>, Component> valueFormatter) {
		super(option.name(), x, y, width, height);
		this.option = option;
		this.valueFormatter = () -> valueFormatter.apply(option);
		this.shouldDrawName = true;
	}

	@Override
	protected void renderWidget(GuiGraphics drawContext, int mouseX, int mouseY, float f) {
		drawContext.blitSprite(RenderPipelines.GUI_TEXTURED, BUTTON_TEXTURE, this.getX(), this.getY(), this.getWidth(), this.getHeight(), active ? 0xA0A0A0A0 : 0xA0808080);
		if (open) {

		} else {
		}
	}
}
