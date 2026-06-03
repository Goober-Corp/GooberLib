package com.goobercorp.gooberlib.gui;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.option.Option;
import com.goobercorp.gooberlib.option.individual.java.CycleOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.util.function.Function;
import java.util.function.Supplier;


public class CyclingOptionWidget extends EvilBaseWidget {
	private final CycleOption<?> opt;
	private final Supplier<Text> valueFormatter;

	public <T extends Option<T>> CyclingOptionWidget(T opt, int x, int y, int width, int height, Function<T, Text> valueFormatter) {
		super(opt.name(), x, y, width, height);
		this.valueFormatter = () -> valueFormatter.apply(opt);
		this.opt = (CycleOption<?>) opt;
	}


	@Override
	protected void drawText(DrawContext drawContext) {
		drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, opt.value.toString(), this.getRight() - 5 - MinecraftClient.getInstance().textRenderer.getWidth(opt.value.toString()), this.getY() + this.height / 2 - MinecraftClient.getInstance().textRenderer.fontHeight / 2, MainConfig.primaryCol);
		drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, opt.name(), this.getX() + 5, this.getY() + this.height / 2 - MinecraftClient.getInstance().textRenderer.fontHeight / 2, MainConfig.primaryCol);
	}

	@Override
	public void onClick(Click click, boolean bl) {
		opt.advance();
	}
}
