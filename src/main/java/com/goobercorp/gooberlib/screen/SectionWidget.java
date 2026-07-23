package com.goobercorp.gooberlib.screen;

import com.goobercorp.gooberlib.builder.misc.OptionHolder;
import com.goobercorp.gooberlib.builder.section.ConfigSection;
import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.EvilBaseWidget;
import com.goobercorp.gooberlib.gui.nav.GroupDividerWidget;
import com.goobercorp.gooberlib.gui.util.ClickableParentWidget;
import com.goobercorp.gooberlib.gui.util.PrecisePositionWidgetWrapper;
import com.goobercorp.gooberlib.interfaces.Hoverable;
import com.goobercorp.gooberlib.option.Option;
import com.goobercorp.gooberlib.option.OptionContext;
import com.goobercorp.gooberlib.util.RenderUtils;
import com.goobercorp.gooberlib.util.TargetedTweener;
import com.goobercorp.gooberlib.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.goobercorp.gooberlib.screen.GooberScreen.CHILD_INSET;
import static com.goobercorp.gooberlib.screen.GooberScreen.VERTICAL_PADDING;
import static com.goobercorp.gooberlib.util.RenderUtils.newMatrixScope;

public class SectionWidget extends ClickableParentWidget implements Hoverable {
	public final HashMap<OptionContext<?>, PrecisePositionWidgetWrapper<?>> evilLayout = new HashMap<>();
	@Nullable
	public final PrecisePositionWidgetWrapper<GroupDividerWidget> dividerWidget;
	private final List<OptionContext<?>> options;
	private final TargetedTweener collapsedTweener = new TargetedTweener();
	public final int uncollapsedHeight;

	public SectionWidget(ConfigSection section, int x, int y, int width, int height) {
		super(x, y, width, height, section.metadata().name(), new ArrayList<>());
		this.options = section.childOptions();

		GroupDividerWidget t = new GroupDividerWidget(width, Minecraft.getInstance().font.lineHeight, section.metadata().name(), Minecraft.getInstance().font);
		PrecisePositionWidgetWrapper<GroupDividerWidget> groupDivider = new PrecisePositionWidgetWrapper<>(t, x, y, section.metadata()::description);
		dividerWidget = groupDivider;
		if (new ScreenRectangle((int) groupDivider.getRealX(), (int) groupDivider.getRealY(), groupDivider.getWrapped().getRight(), groupDivider.getWrapped().getBottom()).overlaps(new ScreenRectangle(0, 0, width, height))) {
			t.renderProgress = 1;
		}
		children().add(groupDivider);
		y += VERTICAL_PADDING;
		if (MainConfig.EXPERIMENTAL_DUAL_COLUMN_LAYOUT.getValue()) {
			int width1 = ((width * 2) / 3) - (x % width) - (CHILD_INSET / 3);
			if (section.childOptions().size() == 1) {
				y += addOptionWithChildren(section.childOptions().getFirst(), y, x + (CHILD_INSET / 2), width1, width / 6);
			} else {
				for (int i = 0; i < section.childOptions().size() - 1; i += 2) {
					if (i + 1 < section.childOptions().size()) {
						//TODO: center the smaller option vertically ?
						int temp, temp2;
						temp = addOptionWithChildren(section.childOptions().get(i + 1), y, x + (CHILD_INSET / 2), width / 2);
						temp2 = addOptionWithChildren(section.childOptions().get(i), y, x + (CHILD_INSET / 2), 0);
						y += Math.max(temp, temp2);
					}
				}
				if (section.childOptions().size() % 2 != 0) {
					y += addOptionWithChildren(section.childOptions().getLast(), y, x + (CHILD_INSET / 2), width1, width / 6);
				}
			}
		} else {
			for (OptionContext<?> yeah : section.childOptions()) {
				y += addOptionWithChildren(yeah, y, x + (CHILD_INSET / 2), 0);
			}
		}
		this.setHeight(y);
		this.uncollapsedHeight = y;
		collapsedTweener.setTarget(dividerWidget.getWrapped().isCollapsed ? 0 : 1);
		collapsedTweener.snapToTarget();
	}

	public SectionWidget(CharSequence name, List<Option<?>> options, int x, int y, int width, int height) {
		super(x, y, width, height, Util.fromChars(name), new ArrayList<>());

		this.options = new ArrayList<>();
		for (Option<?> option : options) {
			this.options.add(new OptionContext<>(null, option));
		}

		dividerWidget = null;
		y += VERTICAL_PADDING / 2;
		for (OptionContext<?> yeah : this.options) {
			y += addOptionWithChildren(yeah, y, x + (CHILD_INSET / 2), 0);
		}
		this.setHeight(y);
		this.uncollapsedHeight = y;
		collapsedTweener.setTarget(1);
		collapsedTweener.snapToTarget();
	}

	@Override
	protected void renderWidget(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
		if (dividerWidget != null) {
			dividerWidget.render(drawContext, mouseX, mouseY, delta);
			collapsedTweener.setTarget(dividerWidget.getWrapped().isCollapsed ? 0 : 1);
			collapsedTweener.update();
			if (dividerWidget.getWrapped().isCollapsed && collapsedTweener.isAtTarget()) {
				return; // dont render if fully collapsed
			}
		}
		newMatrixScope(drawContext, stack -> {
			if (dividerWidget != null) {
				stack.scaleAround(1, collapsedTweener.getF(), this.getX() + this.getWidth() / 2F, this.getY() + dividerWidget.getWrapped().getHeight());
			}
			drawLines(drawContext);
			for (PrecisePositionWidgetWrapper<?> entry : evilLayout.values()) {
				entry.render(drawContext, mouseX, mouseY, delta);
			}
		});
		if (dividerWidget != null) {
			this.setHeight((int) getHeightWithoutSectionDivider() + this.dividerWidget.getWrapped().getHeight());
		}
	}

	private void drawLines(GuiGraphics guiGraphics) {
		for (OptionContext<?> o : options) {
//			drawLinesForOption(guiGraphics, o);
		}
	}

	private void drawLinesForOption(GuiGraphics drawContext, OptionContext<?> o) {
		if (o.childOptions().isEmpty()) return;
		PrecisePositionWidgetWrapper<?> mainWidget = evilLayout.get(o);
		PrecisePositionWidgetWrapper<?> lastChildWidget = evilLayout.get(o.childOptions().getLast());
		float offsetX = 0;
		float offsetY = 0;
		float clickVal = 0;
		float yeah = 0;
		if (mainWidget.getWrapped() instanceof EvilBaseWidget) {
			offsetX = ((EvilBaseWidget) mainWidget.getWrapped()).horizontalPosOffset;
			offsetY = ((EvilBaseWidget) mainWidget.getWrapped()).verticalPosOffset;
			clickVal = ((EvilBaseWidget) mainWidget.getWrapped()).clickTweener.getF();
		}
		if (lastChildWidget.getWrapped() instanceof EvilBaseWidget) {
			yeah = ((EvilBaseWidget) lastChildWidget.getWrapped()).verticalPosOffset;
		}
		RenderUtils.drawVerticalLine(drawContext, (float) mainWidget.getRealX() + 6 + offsetX, (float) mainWidget.getRealY() + mainWidget.getWrapped().getHeight() - 1 + offsetY - clickVal, (float) lastChildWidget.getRealY() + (lastChildWidget.getWrapped().getHeight() / 2F) + 1 + yeah, MainConfig.bgColor);
		RenderUtils.drawVerticalLine(drawContext, (float) mainWidget.getRealX() + 5 + offsetX, (float) mainWidget.getRealY() + mainWidget.getWrapped().getHeight() - 1 + offsetY - clickVal, (float) lastChildWidget.getRealY() + (lastChildWidget.getWrapped().getHeight() / 2F) + yeah, MainConfig.primaryCol);
//		for (OptionContext<?> opt : o.childOptions()) {
//			PrecisePositionWidgetWrapper<?> optionWidget = evilLayout.get(opt);
//			float offX = 0;
//			float offY = 0;
//			float cVal = 0;
//			if (optionWidget.getWrapped() instanceof EvilBaseWidget) {
//				offX = ((EvilBaseWidget) optionWidget.getWrapped()).horizontalPosOffset;
//				offY = ((EvilBaseWidget) optionWidget.getWrapped()).verticalPosOffset;
//				cVal = ((EvilBaseWidget) optionWidget.getWrapped()).clickTweener.getF();
//			}
//			RenderUtils.drawHorizontalLine(drawContext, (float) mainWidget.getRealX() + 6 + offsetX, (float) evilLayout.get(opt).getRealX() + offX + cVal, (float) optionWidget.getRealY() + optionWidget.getWrapped().getHeight() / 2F + 1 + offY, MainConfig.bgColor);
//			RenderUtils.drawHorizontalLine(drawContext, (float) mainWidget.getRealX() + 5 + offsetX, (float) evilLayout.get(opt).getRealX() + offX + cVal, (float) optionWidget.getRealY() + optionWidget.getWrapped().getHeight() / 2F + offY, MainConfig.primaryCol);
//			drawLinesForOption(drawContext, opt);
//		}
	}

	private int addOptionWithChildren(OptionContext<?> optionContext, int y, int x, int offset) {
		return addOptionWithChildren(optionContext, y, x, width / 2 - (x % width) - (CHILD_INSET / 2), offset);
	}

	private int addOptionWithChildren(OptionContext<?> optionContext, int y, int x, int width, int offset) {
		int addY = 0;
		Option<?> option = optionContext.option();
		AbstractWidget widget = option.makeWidget(0, 0, width, VERTICAL_PADDING / 2);

		PrecisePositionWidgetWrapper<?> pw = new PrecisePositionWidgetWrapper<>(widget, x + offset, y + addY, option::getDescription);
		children().add(pw);
		evilLayout.put(optionContext, pw);
		addY += VERTICAL_PADDING;

		for (OptionHolder holder : optionContext.childOptions()) {
			if (holder instanceof ConfigSection section) {
				for (OptionContext<?> child : section.childOptions()) {
					addY += addOptionWithChildren(child, y + addY, x + CHILD_INSET, width - CHILD_INSET, offset);
				}
			} else {
				addY += addOptionWithChildren(((OptionContext<?>) holder), y + addY, x + CHILD_INSET, width - CHILD_INSET, offset);
			}
		}

		return addY;
	}

	public float getHeightWithoutSectionDivider() {
		int dividerHeight = dividerWidget != null ? this.dividerWidget.getWrapped().getHeight() + VERTICAL_PADDING / 2 : 0;
		return (float) ((this.uncollapsedHeight - dividerHeight) * (this.collapsedTweener.get()));
	}

	public float getOffsetRequired() {
		if (!options.isEmpty()) {
			int dividerHeight = dividerWidget != null ? this.dividerWidget.getWrapped().getHeight() + VERTICAL_PADDING / 2 : 0;
			return (float) ((this.uncollapsedHeight - dividerHeight) * (1 - this.collapsedTweener.get()));
		} else {
			return 0;
		}
	}

	@Override
	public Component getHoverMessage(double mouseX, double mouseY) {
		var dividerHoverMessage = dividerWidget != null ? dividerWidget.getHoverMessage(mouseX, mouseY) : null;
		if (dividerHoverMessage != null && !dividerHoverMessage.isEmpty())
			return dividerWidget.getHoverMessage(mouseX, mouseY);
		for (var child : evilLayout.values()) {
			var childHoverMessage = child.getHoverMessage(mouseX, mouseY);
			if (childHoverMessage != null && !childHoverMessage.isEmpty())
				return child.getHoverMessage(mouseX, mouseY);
		}
		return null;
	}
}
