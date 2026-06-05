package com.goobercorp.gooberlib.screen;


import com.goobercorp.gooberlib.api.widgets.WidgetProviders;
import com.goobercorp.gooberlib.gui.option.EvilStringWidget;
import com.goobercorp.gooberlib.gui.util.PrecisePositionWidgetWrapper;
import com.goobercorp.gooberlib.option.individual.java.ColorOption;
import com.goobercorp.gooberlib.option.individual.minecraft.BlockPosOption;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;
import com.goobercorp.gooberlib.option.individual.primitive.IntOption;
import com.goobercorp.gooberlib.option.individual.primitive.range.FloatRangeOption;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ShowcaseScreen extends Screen {
	private int index = 0;

	public ShowcaseScreen() {
		super(Component.literal("Showcase Screen"));
	}

	@Override
	protected void init() {
		this.clearWidgets();
		var x = 0;
		var y = 0;
		var width = 200;
		var height = 15;
		switch (index) {
			case 0 ->
					this.addRenderableWidget(new PrecisePositionWidgetWrapper<>(new EvilStringWidget(0, 0, 200, 15, null, _ -> true, _ -> true, "", -1), this.width / 2.0 - 100, this.height / 2.0, Component::empty));
			case 1 ->
					this.addRenderableWidget(new PrecisePositionWidgetWrapper<>(new BooleanOption("bool", "", WidgetProviders.booleanSliderWidget()).makeWidget(x, y, width, height), this.width / 2.0 - 100, this.height / 2.0, Component::empty));
			case 2 ->
					this.addRenderableWidget(new PrecisePositionWidgetWrapper<>(new IntOption(Component.literal("int"), _ -> Component.empty(), 0, -20, 20, null).makeWidget(x, y, width, height), this.width / 2.0 - 100, this.height / 2.0, Component::empty));
			case 3 ->
					this.addRenderableWidget(new PrecisePositionWidgetWrapper<>(new ColorOption("color", "", WidgetProviders.colorField()).makeWidget(x, y, width, height), this.width / 2.0 - 100, this.height / 2.0, Component::empty));
			case 4 ->
					this.addRenderableWidget(new PrecisePositionWidgetWrapper<>(new BlockPosOption("block pos", "").makeWidget(x, y, width, height), this.width / 2.0 - 100, this.height / 2.0, Component::empty));
			case 5 ->
					this.addRenderableWidget(new PrecisePositionWidgetWrapper<>(new FloatRangeOption(Component.literal("float range"), _ -> Component.empty(), -5, 5, -20, 30, null).makeWidget(x, y, width, height), this.width / 2.0 - 100, this.height / 2.0, Component::empty));
		}

		this.addRenderableWidget(Button.builder(Component.literal("next"), (button) -> {
			index++;
			if (index > 5) index = 0;
			init();
		}).width(50).pos((int) (this.width / 2.0 - 50), (int) (this.height / 2.0 - 100)).build());
	}
}
