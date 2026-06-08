package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.EvilBaseWidget;
import com.goobercorp.gooberlib.interfaces.AdvanceableOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.function.Function;
import java.util.function.Supplier;

import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;


public class CyclingOptionWidget extends EvilBaseWidget {
	private final AdvanceableOption<?> opt;
	private final Supplier<Component> valueFormatter;

	public <T extends AdvanceableOption<T>> CyclingOptionWidget(T opt, int x, int y, int width, int height, Function<T, Component> valueFormatter) {
		super(opt.name(), x, y, width, height);
		this.valueFormatter = () -> valueFormatter.apply(opt);
		this.opt = opt;
		this.shouldDrawName = true;
	}

	public <T extends AdvanceableOption<T>> CyclingOptionWidget(T opt, int x, int y, int width, int height, Function<T, Component> valueFormatter, boolean centerName) {
		this(opt, x, y, width, height, valueFormatter);
		this.centerName = centerName;
		if (centerName) {
			this.shouldDrawName = false;
		}
	}

	@Override
	protected void drawText(GuiGraphics drawContext) {
		newMatrixScope(drawContext, stack -> {
			Component displayName = valueFormatter.get();
			if (this.centerName) {
				stack.translate(this.getX() + (this.getRight() / 2F) - Minecraft.getInstance().font.width(displayName) / 2F, this.getY() + this.height / 2F - Minecraft.getInstance().font.lineHeight / 2f);
			} else {
				stack.translate(this.getRight() - 5 - Minecraft.getInstance().font.width(displayName), this.getY() + this.height / 2F - Minecraft.getInstance().font.lineHeight / 2f);
			}
			stack.scaleAround(1 - clickTweener.getF() * 0.25F, Minecraft.getInstance().font.width(displayName) / 2F, 5F);
			drawContext.drawString(Minecraft.getInstance().font, displayName, 0, 0, MainConfig.primaryCol);
		});
		super.drawText(drawContext);
	}

	@Override
	public void onClick(MouseButtonEvent click, boolean bl) {
		if (click.button() == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {
			opt.regress();
		} else if (click.button() == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
			opt.advance();
		}
	}

	@Override
	protected boolean isValidClickButton(MouseButtonInfo mouseButtonInfo) {
		int button = mouseButtonInfo.button();
		return button == GLFW.GLFW_MOUSE_BUTTON_LEFT || button == GLFW.GLFW_MOUSE_BUTTON_RIGHT;
	}
}
