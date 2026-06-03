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

import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;


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
		newMatrixScope(drawContext, stack -> {
			//TODO: make this use value formatter
			String yeah = opt.value.toString();
			//TODO: give this text scaling effect to something like ButtonOption
			stack.translate(this.getRight() - 5 - MinecraftClient.getInstance().textRenderer.getWidth(yeah), this.getY() + this.height / 2F - MinecraftClient.getInstance().textRenderer.fontHeight / 2);
			stack.scaleAround(1 - clickTweener.getF() * 0.25F, MinecraftClient.getInstance().textRenderer.getWidth(yeah) / 2F, 5F);
			drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, yeah, 0, 0, MainConfig.primaryCol);
		});
		drawContext.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, opt.name(), this.getX() + 5, this.getY() + this.height / 2 - MinecraftClient.getInstance().textRenderer.fontHeight / 2, MainConfig.primaryCol);
	}

	@Override
	public void onClick(Click click, boolean bl) {
		opt.advance();
	}
}
