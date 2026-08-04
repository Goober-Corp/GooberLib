package com.goobercorp.gooberlib.screen;

import com.goobercorp.gooberlib.builder.category.ConfigCategory;
import com.goobercorp.gooberlib.builder.misc.OptionHolder;
import com.goobercorp.gooberlib.builder.section.ConfigSection;
import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.EvilBaseWidget;
import com.goobercorp.gooberlib.gui.util.ClickableParentWidget;
import com.goobercorp.gooberlib.gui.util.PrecisePositionWidgetWrapper;
import com.goobercorp.gooberlib.interfaces.Hoverable;
import com.goobercorp.gooberlib.option.Option;
import com.goobercorp.gooberlib.option.OptionContext;
import com.goobercorp.gooberlib.util.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.goobercorp.gooberlib.screen.GooberScreen.CHILD_INSET;
import static com.goobercorp.gooberlib.screen.GooberScreen.VERTICAL_PADDING;

public class CategoryWidget extends ClickableParentWidget implements Hoverable {
	private final List<PrecisePositionWidgetWrapper<?>> values = new ArrayList<>();
	private final HashMap<OptionHolder, PrecisePositionWidgetWrapper<?>> evilLayout = new HashMap<>();
	private final ConfigCategory category;

	public CategoryWidget(ConfigCategory category, int x, int y, int width, int height) {
		super(x, y, width, height, category.metadata().name(), new ArrayList<>());
		this.category = category;
		for (int i = 0; i < category.elements().size(); i++) {
			OptionHolder o = category.elements().get(i);
			if (o instanceof ConfigSection section) {
				var sectionWidget = new SectionWidget(section, 0, 0, width, height);
				var wrapper = new PrecisePositionWidgetWrapper<>(sectionWidget, x, y, () -> section.metadata().description());
				children().add(wrapper);
				evilLayout.put(section, wrapper);
				values.add(wrapper);
				y += sectionWidget.uncollapsedHeight;
			} else {
				int index = i;
				while (index < category.elements().size()) {
					if (!(category.elements().get(index) instanceof ConfigSection)) {
						index++;
					} else {
						break;
					}
				}
				//note to self: this is the worst code i've written, maybe ever. but it worked first try.
				List<OptionHolder> loneOptions = category.elements().subList(i, index);
				if (MainConfig.EXPERIMENTAL_DUAL_COLUMN_LAYOUT.getValue()) {
					int width1 = ((width * 2) / 3) - (x % width) - (CHILD_INSET / 3);
					if (loneOptions.size() == 1) {
						y += addOptionWithChildren((OptionContext<?>) loneOptions.getFirst(), y, x + (CHILD_INSET / 2), width1, width / 6);
					} else {
						for (int j = 0; j < loneOptions.size() - 1; j += 2) {
							if (j + 1 < loneOptions.size()) {
								//TODO: center the smaller option vertically ?
								int temp, temp2;
								temp = addOptionWithChildren((OptionContext<?>) loneOptions.get(j + 1), y, x + (CHILD_INSET / 2), width / 2);
								temp2 = addOptionWithChildren((OptionContext<?>) loneOptions.get(j), y, x + (CHILD_INSET / 2), 0);
								y += Math.max(temp, temp2);
							}
						}
						if (loneOptions.size() % 2 != 0) {
							y += addOptionWithChildren((OptionContext<?>) loneOptions.getLast(), y, x + (CHILD_INSET / 2), width1, width / 6);
						}
					}
				} else {
					for (OptionHolder yeah : loneOptions) {
						y += addOptionWithChildren((OptionContext<?>) yeah, y, x + (CHILD_INSET / 2), 0);
					}
				}
				i = index - 1;
			}
		}
		this.setHeight(y);
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
		values.add(pw);
		addY += VERTICAL_PADDING;

		for (OptionHolder holder : optionContext.childOptions()) {
			if (holder instanceof ConfigSection section) {
				int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
				var sectionWidget = new SectionWidget(section, 0, 0, screenWidth - CHILD_INSET * 2, height);
				var wrapper = new PrecisePositionWidgetWrapper<>(sectionWidget, CHILD_INSET, y + addY, () -> section.metadata().description());
				children().add(wrapper);
				evilLayout.put(section, wrapper);
				values.add(wrapper);
				addY += sectionWidget.uncollapsedHeight;
			} else {
				addY += addOptionWithChildren(((OptionContext<?>) holder), y + addY, x + CHILD_INSET, width - CHILD_INSET, offset);
			}
		}
		return addY;
	}

	@Override
	protected void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
		drawLines(guiGraphics);
		double totalYOffset = 0;
		double height = 0;
		for (PrecisePositionWidgetWrapper<?> entry : values) {
			entry.setOffsetY(totalYOffset);
			entry.render(guiGraphics, i, j, f);
			var wrapped = entry.getWrapped();
			if (wrapped instanceof SectionWidget section) {
				totalYOffset -= section.getOffsetRequired();
			}
			//TODO: height calculation for rendering does not take into account dual-column layouts
			height += entry.getWrapped().getHeight() + VERTICAL_PADDING / 2f;
		}
		this.setHeight((int) height);
	}

	private void drawLines(GuiGraphics guiGraphics) {
		for (OptionHolder o : category.elements()) {
			if (!(o instanceof ConfigSection)) {
				drawLinesForOption(guiGraphics, (OptionContext<?>) o);
			}
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
		PrecisePositionWidgetWrapper<?> lastOne = evilLayout.get(o.childOptions().getLast());
		if (lastChildWidget.getWrapped() instanceof EvilBaseWidget) {
			yeah = ((EvilBaseWidget) lastOne.getWrapped()).verticalPosOffset;
		}
		if (lastOne.getWrapped() instanceof SectionWidget section) {
			if (section.dividerWidget.getWrapped().isCollapsed) {
//			lastOne = ((SectionWidget) evilLayout.get(o.childOptions().getLast()).getWrapped()).evilLayout.get(o.childOptions().getLast().childOptions().getFirst());
////			temp += section.dividerWidget.getWrapped().getHeight() + (VERTICAL_PADDING / 4 * 3);
//			yeah += (VERTICAL_PADDING / 4F * 3);
//			yeah += ((EvilBaseWidget) ((SectionWidget) evilLayout.get(o.childOptions().getLast()).getWrapped()).evilLayout.get(o.childOptions().getLast().childOptions().getFirst()).getWrapped()).verticalPosOffset;
			}
		}
		float finalVerticalYPos;
		RenderUtils.drawVerticalLine(drawContext, (float) mainWidget.getRealX() + 6 + offsetX, (float) mainWidget.getRealY() + mainWidget.getWrapped().getHeight() - 1 + offsetY - clickVal, (float) (lastOne.getRealY() + lastOne.getWrapped().getHeight() / 2F) + yeah, MainConfig.bgColor);
		RenderUtils.drawVerticalLine(drawContext, (float) mainWidget.getRealX() + 5 + offsetX, (float) mainWidget.getRealY() + mainWidget.getWrapped().getHeight() - 1 + offsetY - clickVal, (float) (lastOne.getRealY() + lastOne.getWrapped().getHeight() / 2F) + yeah, MainConfig.primaryCol);
		for (OptionHolder opt : o.childOptions()) {
			if (opt instanceof ConfigSection section) {
				PrecisePositionWidgetWrapper<?> sec = evilLayout.get(opt);
				RenderUtils.drawHorizontalLine(drawContext, (float) mainWidget.getRealX() + 6 + offsetX, (float) mainWidget.getRealX() + (CHILD_INSET * 0.66F) + 1, (float) sec.getRealY() + (sec.getWrapped().getHeight() / 2F) + 1, MainConfig.bgColor);
				RenderUtils.drawHorizontalLine(drawContext, (float) mainWidget.getRealX() + 5 + offsetX, (float) mainWidget.getRealX() + (CHILD_INSET * 0.66F), (float) sec.getRealY() + (sec.getWrapped().getHeight() / 2F), MainConfig.primaryCol);

				RenderUtils.drawVerticalLine(drawContext, (float) mainWidget.getRealX() + (CHILD_INSET * 0.66F), (float) sec.getRealY(), (float) (sec.getRealY() + sec.getWrapped().getHeight()), MainConfig.primaryCol);

				RenderUtils.drawHorizontalLine(drawContext, (float) mainWidget.getRealX() + (CHILD_INSET * 0.66F), (float) mainWidget.getRealX() + (CHILD_INSET), (float) sec.getRealY(), MainConfig.primaryCol);
				RenderUtils.drawHorizontalLine(drawContext, (float) mainWidget.getRealX() + (CHILD_INSET * 0.66F), (float) mainWidget.getRealX() + (CHILD_INSET), (float) sec.getRealY() + sec.getWrapped().getHeight(), MainConfig.primaryCol);


//				if (!section.childOptions().isEmpty()) {
//					PrecisePositionWidgetWrapper<?> firstChildOption = ((SectionWidget) evilLayout.get(section).getWrapped()).evilLayout.get(section.childOptions().getFirst());
//					int divHeight = ((SectionWidget) evilLayout.get(section).getWrapped()).dividerWidget.getWrapped().getHeight();
//					float offX = 0;
//					float offY = 0;
//					float cVal = 0;
//					if (firstChildOption.getWrapped() instanceof EvilBaseWidget) {
//						offX = ((EvilBaseWidget) firstChildOption.getWrapped()).horizontalPosOffset;
//						offY = ((EvilBaseWidget) firstChildOption.getWrapped()).verticalPosOffset;
//						cVal = ((EvilBaseWidget) firstChildOption.getWrapped()).clickTweener.getF();
//					}
//					float bleghhh = firstChildOption.getWrapped().getHeight() / 2F + offY + divHeight + (VERTICAL_PADDING / 4 * 3);
//					float v = Mth.lerp(((SectionWidget) evilLayout.get(section).getWrapped()).collapsedTweener.getF(), divHeight / 2F, bleghhh);
//					if (!opt.equals(o.childOptions().stream().filter(optionHolder -> optionHolder instanceof ConfigSection).findFirst().get())) {
//						v += (float) (evilLayout.get(section).getRealY() - (VERTICAL_PADDING / 4 * 3) - divHeight);
//					}
//					RenderUtils.drawHorizontalLine(drawContext, (float) mainWidget.getRealX() + 6 + offsetX, (float) (firstChildOption.getRealX() + offX + cVal) + CHILD_INSET + (CHILD_INSET * (1 - ((SectionWidget) evilLayout.get(section).getWrapped()).collapsedTweener.getF())), (float) firstChildOption.getRealY() + v + 1, MainConfig.bgColor, ARGB.srgbLerp((1 - ((SectionWidget) evilLayout.get(section).getWrapped()).collapsedTweener.getF()), MainConfig.bgColor, 0x00000000));
//					RenderUtils.drawHorizontalLine(drawContext, (float) mainWidget.getRealX() + 5 + offsetX, (float) (firstChildOption.getRealX() + offX + cVal) + CHILD_INSET + (CHILD_INSET * (1 - ((SectionWidget) evilLayout.get(section).getWrapped()).collapsedTweener.getF())), (float) firstChildOption.getRealY() + v, MainConfig.primaryCol, ARGB.srgbLerp((1 - ((SectionWidget) evilLayout.get(section).getWrapped()).collapsedTweener.getF()), MainConfig.primaryCol, 0x00000000));
//
//
//				}
			} else {
				PrecisePositionWidgetWrapper<?> optionWidget = evilLayout.get(opt);
				float offX = 0;
				float offY = 0;
				float cVal = 0;
				if (optionWidget.getWrapped() instanceof EvilBaseWidget) {
					offX = ((EvilBaseWidget) optionWidget.getWrapped()).horizontalPosOffset;
					offY = ((EvilBaseWidget) optionWidget.getWrapped()).verticalPosOffset;
					cVal = ((EvilBaseWidget) optionWidget.getWrapped()).clickTweener.getF();
				}
				RenderUtils.drawHorizontalLine(drawContext, (float) mainWidget.getRealX() + 6 + offsetX, (float) optionWidget.getRealX() + offX + cVal, (float) optionWidget.getRealY() + optionWidget.getWrapped().getHeight() / 2F + 1 + offY, MainConfig.bgColor);
				RenderUtils.drawHorizontalLine(drawContext, (float) mainWidget.getRealX() + 5 + offsetX, (float) optionWidget.getRealX() + offX + cVal, (float) optionWidget.getRealY() + optionWidget.getWrapped().getHeight() / 2F + offY, MainConfig.primaryCol);
				drawLinesForOption(drawContext, (OptionContext<?>) opt);
			}
		}
	}

	@Override
	public Component getHoverMessage(double mouseX, double mouseY) {
		for (var child : values) {
			var childHoverMessage = child.getHoverMessage(mouseX, mouseY);
			if (childHoverMessage != null && !childHoverMessage.isEmpty()) return childHoverMessage;
		}
		return null;
	}
}
