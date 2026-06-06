package com.goobercorp.gooberlib.screen;

import com.goobercorp.gooberlib.builder.misc.OptionHolder;
import com.goobercorp.gooberlib.builder.section.ConfigSection;
import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.nav.GroupDividerWidget;
import com.goobercorp.gooberlib.gui.util.ClickableParentWidget;
import com.goobercorp.gooberlib.gui.util.PrecisePositionWidgetWrapper;
import com.goobercorp.gooberlib.option.Option;
import com.goobercorp.gooberlib.option.OptionContext;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.TargetedTweener;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.navigation.ScreenRectangle;

import java.util.ArrayList;
import java.util.HashMap;

import static com.goobercorp.gooberlib.screen.GooberScreen.CHILD_INSET;
import static com.goobercorp.gooberlib.screen.GooberScreen.VERTICAL_PADDING;
import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

public class SectionWidget extends ClickableParentWidget {
	private final HashMap<OptionHolder, PrecisePositionWidgetWrapper<?>> evilLayout = new HashMap<>();
	private final PrecisePositionWidgetWrapper<GroupDividerWidget> dividerWidget;
	private final ConfigSection section;
	private final TargetedTweener collapsedTweener = new TargetedTweener();
	public final int uncollapsedHeight;

	public SectionWidget(ConfigSection section, int x, int y, int width, int height) {
		super(x, y, width, height, section.metadata().name(), new ArrayList<>());
		this.section = section;

		GroupDividerWidget t = new GroupDividerWidget(section.metadata().name(), Minecraft.getInstance().font);
		PrecisePositionWidgetWrapper<GroupDividerWidget> groupDivider = new PrecisePositionWidgetWrapper<>(t, x + ((double) Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2) - (double) Minecraft.getInstance().font.width(section.metadata().name()) / 2, y, section.metadata()::description);
		dividerWidget = groupDivider;
		if (new ScreenRectangle((int) groupDivider.getRealX(), (int) groupDivider.getRealY(), groupDivider.getWrapped().getRight(), groupDivider.getWrapped().getBottom()).overlaps(new ScreenRectangle(0, 0, width, height))) {
			t.renderProgress = 1;
		}
		children().add(groupDivider);
		y += VERTICAL_PADDING;
		for (OptionContext<?> yeah : section.childOptions()) {
			y += addOptionWithChildren(yeah, y, x + CHILD_INSET);
		}
		this.setHeight(y);
		this.uncollapsedHeight = y;
	}

	@Override
	protected void renderWidget(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
		dividerWidget.render(drawContext, mouseX, mouseY, delta);
		collapsedTweener.setTarget(dividerWidget.getWrapped().isCollapsed ? 0 : 1);
		collapsedTweener.update();
		if (dividerWidget.getWrapped().isCollapsed && collapsedTweener.isAtTarget()) {
			return; // dont render if fully collapsed
		}
		newMatrixScope(drawContext, stack -> {
			stack.scaleAround(1, collapsedTweener.getF(), this.getX() + this.getWidth() / 2F, this.getY() + dividerWidget.getWrapped().getHeight());
			drawLines(drawContext);
			for (PrecisePositionWidgetWrapper<?> entry : evilLayout.values()) {
				entry.render(drawContext, mouseX, mouseY, delta);
			}
		});
		this.setHeight((int) getHeightWithoutSectionDivider() + this.dividerWidget.getWrapped().getHeight());
	}

	private void drawLines(GuiGraphics guiGraphics) {
		for (OptionHolder o : section.childOptions()) {
			drawLinesForOption(guiGraphics, o);
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

	private int addOptionWithChildren(OptionContext<?> optionContext, int y, int x) {
		int addY = 0;
		Option<?> option = optionContext.option();
		AbstractWidget widget = option.makeWidget(0, 0, width / 2 - (x % width), VERTICAL_PADDING / 2);

		PrecisePositionWidgetWrapper<?> pw = new PrecisePositionWidgetWrapper<>(widget, x, y + addY, option::getDescription);
		children().add(pw);
		evilLayout.put(optionContext, pw);
		addY += VERTICAL_PADDING;

		for (OptionContext<?> child : optionContext.childOptions()) {
			addY += addOptionWithChildren(child, y + addY, x + CHILD_INSET);
		}

		return addY;
	}

	public float getHeightWithoutSectionDivider() {
		int dividerHeight = this.dividerWidget.getWrapped().getHeight() + VERTICAL_PADDING / 2;
		return (float) ((this.uncollapsedHeight - dividerHeight) * (this.collapsedTweener.get()));
	}

	public float getOffsetRequired() {
		int dividerHeight = this.dividerWidget.getWrapped().getHeight() + VERTICAL_PADDING / 2;
		return (float) ((this.uncollapsedHeight - dividerHeight) * (1 - this.collapsedTweener.get()));
	}
}
