package com.goobercorp.gooberlib.screen;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.nav.GroupDividerWidget;
import com.goobercorp.gooberlib.gui.util.PrecisePositionWidgetWrapper;
import com.goobercorp.gooberlib.option.individual.misc.ObjectOption;
import com.goobercorp.gooberlib.util.RenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class ObjectScreen extends Screen {
	private final Screen parent;
	private final ObjectOption<?> option;
	@SuppressWarnings("FieldCanBeLocal")
	private PrecisePositionWidgetWrapper<SectionWidget> widget;

	private int popupX;
	private int popupY;
	private int popupWidth;

	public ObjectScreen(Screen parent, ObjectOption<?> option) {
		super(option.name());
		this.parent = parent;
		this.option = option;
	}

	@Override
	protected void init() {
		clearWidgets();
		popupWidth = Math.min(400, this.width - 50);
		popupX = width / 2 - popupWidth / 2;
		popupY = 100;
		int popupHeight = 1000;
		this.addRenderableOnly(new PrecisePositionWidgetWrapper<>(new GroupDividerWidget(0, 0, popupWidth, popupHeight, option.name(), font), popupX, popupY - 12, Component::empty));
		widget = this.addRenderableWidget(new PrecisePositionWidgetWrapper<>(new SectionWidget(option.name(), option.options, 0, 0, popupWidth, popupHeight), popupX, popupY, Component::empty));
		if (widget.getWrapped().uncollapsedHeight > this.height - popupY * 2) {
			popupY = (this.height - widget.getWrapped().uncollapsedHeight) / 2;
			widget.setY(popupY);
		}
	}

	@Override
	public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f) {
		parent.renderWithTooltipAndSubtitles(guiGraphics, -1, -1, f);
		super.renderBackground(guiGraphics, i, j, f);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int i, int j, float f) {
		RenderUtils.fillEvil(guiGraphics, popupX, popupY - 14, popupX + popupWidth, popupY + this.height - popupY * 2, MainConfig.bgColor);
		super.render(guiGraphics, i, j, f);
	}

	@Override
	public void onClose() {
		minecraft.screen = parent;
	}

	@Override
	public void tick() {
		parent.tick();
		super.tick();
	}

	@Override
	public void resize(int i, int j) {
		parent.resize(i, j);
		super.resize(i, j);
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent mouseButtonEvent) {
		parent.mouseReleased(mouseButtonEvent);
		return super.mouseReleased(mouseButtonEvent);
	}
}
