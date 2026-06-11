package com.goobercorp.gooberlib.screen;

import com.goobercorp.gooberlib.config.MainConfig;
import com.goobercorp.gooberlib.gui.util.PrecisePositionWidgetWrapper;
import com.goobercorp.gooberlib.option.individual.misc.ObjectOption;
import com.goobercorp.gooberlib.util.RenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class ObjectScreen extends Screen {
	private final Screen parent;
	private final ObjectOption<?> option;
	private PrecisePositionWidgetWrapper<SectionWidget> widget;

	private int popupX;
	private int popupY;
	private int popupWidth;
	private int popupHeight;

	public ObjectScreen(Screen parent, ObjectOption<?> option) {
		super(option.name());
		this.parent = parent;
		this.option = option;
	}

	@Override
	protected void init() {
		clearWidgets();
		popupX = width / 2 - 200;
		popupY = 100;
		popupWidth = 400;
		popupHeight = 1000;
		widget = this.addRenderableWidget(new PrecisePositionWidgetWrapper<>(new SectionWidget(option.name(), option.options, 0, 0, popupWidth, popupHeight), popupX, popupY, Component::empty));
	}

	@Override
	public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f) {
		this.parent.render(guiGraphics, -1, -1, f);
		super.renderBackground(guiGraphics, i, j, f);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int i, int j, float f) {
		this.parent.render(guiGraphics, -1, -1, f);
		RenderUtils.fillEvil(guiGraphics, popupX, popupY, popupX + popupWidth, popupY + widget.getWrapped().uncollapsedHeight, MainConfig.bgColor);
		super.render(guiGraphics, i, j, f);
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(parent);
	}
}
