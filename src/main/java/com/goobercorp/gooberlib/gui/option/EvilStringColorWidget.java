package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.option.individual.java.ColorOption;
import com.goobercorp.gooberlib.util.ColorTweener;
import com.goobercorp.gooberlib.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.phys.Vec2;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class EvilStringColorWidget extends EvilStringWidget {
	private final Component name;
	private final int x;
	private final ColorOption opt;
	private final ColorTweener colorTweener;

	public EvilStringColorWidget(Component name, int x, int y, int width, int height, @Nullable Consumer<String> changedListener, Predicate<String> predicate, Predicate<String> immediatePredicate, String initial, ColorOption opt) {
		super(x + font().width(name), y, width - font().width(name), height, changedListener, predicate, immediatePredicate, initial, () -> {
		});
		this.name = name;
		this.x = x;
		this.opt = opt;
		this.setFormatter(s -> {
			MutableComponent component = Component.empty();
			if (s.startsWith("#")) {
				component.append("#");
				s = s.substring(1);
			} else if (s.startsWith("0x")) {
				component.append("0x");
				s = s.substring(2);
			}
			switch (s.length()) {
				case 3 -> component
						.append(Component.literal(s.substring(0, 1)).withColor(0xFF0000))
						.append(Component.literal(s.substring(1, 2)).withColor(0x00FF00))
						.append(Component.literal(s.substring(2, 3)).withColor(0x0000FF));
				case 4 -> component
						.append(Component.literal(s.substring(0, 1)).withColor(0xFFFFFF))
						.append(Component.literal(s.substring(1, 2)).withColor(0xFF0000))
						.append(Component.literal(s.substring(2, 3)).withColor(0x00FF00))
						.append(Component.literal(s.substring(3, 4)).withColor(0x0000FF));
				case 5 -> component
						.append(Component.literal(s.substring(0, 1)).withColor(0xFF0000))
						.append(Component.literal(s.substring(1, 3)).withColor(0x00FF00))
						.append(Component.literal(s.substring(3, 5)).withColor(0x0000FF));
				case 6 -> component
						.append(Component.literal(s.substring(0, 2)).withColor(0xFF0000))
						.append(Component.literal(s.substring(2, 4)).withColor(0x00FF00))
						.append(Component.literal(s.substring(4, 6)).withColor(0x0000FF));
				case 7 -> component
						.append(Component.literal(s.substring(0, 1)).withColor(0xFFFFFF))
						.append(Component.literal(s.substring(1, 3)).withColor(0xFF0000))
						.append(Component.literal(s.substring(3, 5)).withColor(0x00FF00))
						.append(Component.literal(s.substring(5, 7)).withColor(0x0000FF));
				case 8 -> component
						.append(Component.literal(s.substring(0, 2)).withColor(0xFFFFFF))
						.append(Component.literal(s.substring(2, 4)).withColor(0xFF0000))
						.append(Component.literal(s.substring(4, 6)).withColor(0x00FF00))
						.append(Component.literal(s.substring(6, 8)).withColor(0x0000FF));
				default -> component.append(s);
			}
			return component;
		});
		colorTweener = new ColorTweener(() -> opt.value, 5);
		this.shouldDrawName = false;
	}

	public static Font font() {
		return Minecraft.getInstance().font;
	}

	@Override
	public void renderWidget(GuiGraphics context, double mouseX, double mouseY, float delta) {
		super.renderWidget(context, mouseX, mouseY, delta);
		float widthAndHeight = (getHeight() - getY() - 4);
		float midpoint = (widthAndHeight / 2);
		Vec2 center = new Vec2(getRight() - midpoint - 2, midpoint + 2);
		midpoint *= 0.75F;
		RenderUtils.fillEvil(context, center.x - midpoint + 1, center.y - midpoint + 1, center.x + midpoint - 1, center.y + midpoint - 1, colorTweener.get());
		RenderUtils.drawBoxOutline(context, center.x - midpoint, center.y - midpoint, center.x + midpoint - 1, center.y + midpoint - 1, 0xFF000000);
	}

	@Override
	public void renderWidget(GuiGraphics drawContext, int i, int j, float f) {
		super.renderWidget(drawContext, i, j, f);
		colorTweener.update();
		drawContext.drawString(font(), name, x + 1, getY() + 3, MainConfig.primaryCol, true);
	}

	@Override
	public void reset() {
		opt.resetToDefault();
	}
}
