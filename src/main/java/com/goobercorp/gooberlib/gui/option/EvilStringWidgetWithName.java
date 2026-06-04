package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.config.MainConfig;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class EvilStringWidgetWithName extends EvilStringWidget {
	private final Component name;
	private final int x;

	public EvilStringWidgetWithName(Component name, int x, int y, int width, int height, @Nullable Consumer<String> changedListener, Predicate<String> predicate, Predicate<String> immediatePredicate, String initial) {
		super(x + font().width(name) + 2, y, width - font().width(name), height, changedListener, predicate, immediatePredicate, initial);
		this.name = name;
		this.x = x;
	}

	public static Font font() {
		return Minecraft.getInstance().font;
	}

	@Override
	public void renderWidget(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
		drawContext.drawString(font(), name, x, getY() + 3, MainConfig.primaryCol, true);
		super.renderWidget(drawContext, mouseX, mouseY, delta);
	}
}
