package com.goobercorp.gooberlib.test.config;

import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.api.widgets.WidgetProviders;
import com.goobercorp.gooberlib.builder.GooberConfigBuilder;
import com.goobercorp.gooberlib.option.individual.java.CycleOption;
import com.goobercorp.gooberlib.option.individual.java.StringOption;
import com.goobercorp.gooberlib.option.individual.misc.ButtonOption;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;

import java.util.function.Supplier;

@GooberConfig(modId = "mixinextras")

public class WidgetVariations {

	public static final BooleanOption boolopt = new BooleanOption("yeah", "", WidgetProviders.booleanTickBox());
	public static final BooleanOption boolopt2 = new BooleanOption("yeah", "", WidgetProviders.booleanTickBoxWithCenteredName());
	public static final BooleanOption boolopt3 = new BooleanOption("yeah", "", WidgetProviders.booleanSliderWidget());
	public static final BooleanOption boolopt4 = new BooleanOption("yeah", "", WidgetProviders.booleanSliderWidgetWithCenteredName());

	public static final StringOption left = new StringOption("left", "");
	public static final StringOption centered = new StringOption("centered", "", WidgetProviders.stringFieldCentered());
	public static final StringOption right = new StringOption("right", "", WidgetProviders.stringFieldAlignedRight());
	public static final StringOption evil = new StringOption("evil", "", WidgetProviders.stringFieldNameInsideAlignedRight());

	public static final ButtonOption yeah = new ButtonOption("yeah", () -> System.out.println("yah"));

	public static final CycleOption<String> cycleOption = new CycleOption<>("cycle option", "cycle description", WidgetProviders.cyclingOptionWithButtons(), "Option one", "Option two", "Option three");
	public static final CycleOption<String> cycleOption2 = new CycleOption<>("cycle option", "cycle description", "Option one", "Option two", "Option three");

	public static final Supplier<GooberConfigBuilder> BUILDER = () -> GooberConfigBuilder.create("widget_variations")
			.category("Widget Variations")
			.section("Tick Box Widget", "")
			.options(boolopt, boolopt2)
			.build()
			.section("Slider Toggle Widget", "")
			.options(boolopt3, boolopt4)
			.build()
			.section("String Widget", "")
			.options(left, centered, right, evil)
			.build()
			//TODO:
			.section("Button Widget", "")
			.options()
			.build()
			.section("Cycling Widget", "")
			.options(cycleOption, cycleOption2)
			.build()
			.build();
}
