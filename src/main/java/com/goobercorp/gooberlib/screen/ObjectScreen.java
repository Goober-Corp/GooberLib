package com.goobercorp.gooberlib.screen;

import com.goobercorp.gooberlib.option.individual.misc.ObjectOption;
import com.goobercorp.gooberlib.util.RenderUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

public class ObjectScreen extends Screen {
	private final Screen parent;
	private final ObjectOption<?> option;
	private SectionWidget widget;

	public ObjectScreen(Screen parent, ObjectOption<?> option) {
		super(option.name());
		this.parent = parent;
		this.option = option;
	}

	@Override
	protected void init() {
		clearWidgets();
		widget = this.addRenderableWidget(new SectionWidget(option.name(), option.options, 100, 100, 200, 200));
	}

	@Override
	public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f) {
		this.parent.render(guiGraphics, -1, -1, f);
		super.renderBackground(guiGraphics, i, j, f);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int i, int j, float f) {
		this.parent.render(guiGraphics, -1, -1, f);
		RenderUtils.fillEvil(guiGraphics, widget.getX(), widget.getY(), widget.getRight(), widget.getX() + widget.uncollapsedHeight, -1);
		super.render(guiGraphics, i, j, f);
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(parent);
	}
}
