package com.goobercorp.gooberlib.screen;

import com.goobercorp.gooberlib.builder.category.ConfigCategory;
import com.goobercorp.gooberlib.builder.misc.OptionHolder;
import com.goobercorp.gooberlib.builder.section.ConfigSection;
import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.util.ClickableParentWidget;
import com.goobercorp.gooberlib.gui.util.PrecisePositionWidgetWrapper;
import com.goobercorp.gooberlib.option.Option;
import com.goobercorp.gooberlib.option.OptionContext;
import com.goobercorp.gooberlib.util.RenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;

import java.util.ArrayList;
import java.util.HashMap;

import static com.goobercorp.gooberlib.screen.GooberScreen.CHILD_INSET;
import static com.goobercorp.gooberlib.screen.GooberScreen.VERTICAL_PADDING;

public class CategoryWidget extends ClickableParentWidget {
	private final int maxY;
	private final HashMap<OptionHolder, PrecisePositionWidgetWrapper<?>> evilLayout = new HashMap<>();
	private final ConfigCategory category;

	public CategoryWidget(ConfigCategory category, int x, int y, int width, int height) {
		super(x, y, width, height, category.metadata().name(), new ArrayList<>());
		this.category = category;
		for (OptionHolder o : category.elements()) {
			if (o instanceof ConfigSection section) {
				var sectionWidget = new SectionWidget(section, 0, 0, width, height);
				var wrapper = new PrecisePositionWidgetWrapper<>(sectionWidget, x, y, () -> section.metadata().description());
				children().add(wrapper);
				evilLayout.put(section, wrapper);
				y += sectionWidget.getHeight();
			} else {
				y += addOptionWithChildren((OptionContext<?>) o, y, x + 5);
			}
		}
		this.maxY = y;
	}

	private int addOptionWithChildren(OptionContext<?> optionContext, int y, int x) {
		int addY = 0;
		Option<?> option = optionContext.option();
		AbstractWidget widget = option.makeWidget(0, 0, 250, VERTICAL_PADDING / 2);

		PrecisePositionWidgetWrapper<?> pw = new PrecisePositionWidgetWrapper<>(widget, x, y + addY, option::getDescription);
		children().add(pw);
		evilLayout.put(optionContext, pw);
		addY += VERTICAL_PADDING;

		for (OptionContext<?> child : optionContext.childOptions()) {
			addY += addOptionWithChildren(child, y + addY, x + CHILD_INSET);
		}

		return addY;
	}

	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
		drawLines(guiGraphics);
		evilLayout.values().forEach(entry -> entry.render(guiGraphics, i, j, f));
	}

	private void drawLines(GuiGraphics guiGraphics) {
		for (OptionHolder o : category.elements()) {
			if (!(o instanceof ConfigSection)) {
				drawLinesForOption(guiGraphics, o);
			}
		}
	}

	private void drawLinesForOption(GuiGraphics drawContext, OptionHolder o) {
		if (o.childOptions().isEmpty()) return;
		PrecisePositionWidgetWrapper<?> mainWidget = evilLayout.get(o);
		PrecisePositionWidgetWrapper<?> lastChildWidget = evilLayout.get(o.childOptions().getLast());
		RenderUtils.drawVerticalLine(drawContext, (float) mainWidget.getRealX() + 6, (float) mainWidget.getRealY() + mainWidget.getWrapped().getHeight(), (float) lastChildWidget.getRealY() + (lastChildWidget.getWrapped().getHeight() / 2F) + 1, MainConfig.bgColor);
		RenderUtils.drawVerticalLine(drawContext, (float) mainWidget.getRealX() + 5, (float) mainWidget.getRealY() + mainWidget.getWrapped().getHeight() - 1, (float) lastChildWidget.getRealY() + (lastChildWidget.getWrapped().getHeight() / 2F), MainConfig.primaryCol);
		for (OptionHolder opt : o.childOptions()) {
			PrecisePositionWidgetWrapper<?> optionWidget = evilLayout.get(opt);
			RenderUtils.drawHorizontalLine(drawContext, (float) mainWidget.getRealX() + 6, (float) evilLayout.get(opt).getRealX(), (float) optionWidget.getRealY() + optionWidget.getWrapped().getHeight() / 2F + 1, MainConfig.bgColor);
			RenderUtils.drawHorizontalLine(drawContext, (float) mainWidget.getRealX() + 5, (float) evilLayout.get(opt).getRealX(), (float) optionWidget.getRealY() + optionWidget.getWrapped().getHeight() / 2F, MainConfig.primaryCol);
			drawLinesForOption(drawContext, opt);
		}
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
	}

	public int getMaxY() {
		return this.maxY;
	}
}
