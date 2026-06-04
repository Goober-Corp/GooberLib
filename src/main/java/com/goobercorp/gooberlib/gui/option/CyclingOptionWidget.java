package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.EvilBaseWidget;
import com.goobercorp.gooberlib.option.Option;
import com.goobercorp.gooberlib.option.individual.java.CycleOption;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;


public class CyclingOptionWidget extends EvilBaseWidget {
	private final CycleOption<?> opt;
	private final Supplier<Component> valueFormatter;

	public <T extends Option<T>> CyclingOptionWidget(T opt, int x, int y, int width, int height, Function<T, Component> valueFormatter) {
		super(opt.name(), x, y, width, height);
		this.valueFormatter = () -> valueFormatter.apply(opt);
		this.opt = (CycleOption<?>) opt;
	}


	@Override
	protected void drawText(GuiGraphics drawContext) {
		newMatrixScope(drawContext, stack -> {
			//TODO: make this use value formatter
			String yeah = opt.value.toString();
			//TODO: give this text scaling effect to something like ButtonOption
			stack.translate(this.getRight() - 5 - Minecraft.getInstance().font.width(yeah), this.getY() + this.height / 2F - Minecraft.getInstance().font.lineHeight / 2);
			stack.scaleAround(1 - clickTweener.getF() * 0.25F, Minecraft.getInstance().font.width(yeah) / 2F, 5F);
			drawContext.drawString(Minecraft.getInstance().font, yeah, 0, 0, MainConfig.primaryCol);
		});
		drawContext.drawString(Minecraft.getInstance().font, opt.name(), this.getX() + 5, this.getY() + this.height / 2 - Minecraft.getInstance().font.lineHeight / 2, MainConfig.primaryCol);
	}

	@Override
	public void onClick(MouseButtonEvent click, boolean bl) {
		opt.advance();
	}
}
