package com.goobercorp.gooberlib.test.config;

import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.builder.GooberConfigBuilder;
import com.goobercorp.gooberlib.option.individual.java.ColorOption;
import com.goobercorp.gooberlib.option.individual.java.CycleOption;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;
import com.goobercorp.gooberlib.option.individual.primitive.FloatOption;
import com.goobercorp.gooberlib.option.individual.primitive.IntOption;

@GooberConfig(modId = "larp-custom-block-highlight", title = "Larp CBH")
public class LarpCBH {
	public static final BooleanOption OUTLINE_ENABLED = new BooleanOption("Outline enabled", true);
	public static final ColorOption OUTLINE_COLOR = new ColorOption("Outline color", 0xFF000000);
	public static final ColorOption OUTLINE_SECONDARY_COLOR = new ColorOption("Secondary Outline color", 0xFFFFFFFF);
	public static final IntOption OUTLINE_ALPHA = new IntOption("Outline alpha", 255);
	public static final BooleanOption OUTLINE_RAINBOW = new BooleanOption("Outline rainbow", true);
	public static final CycleOption<String> OUTLINE_MODE = new CycleOption<>("Mode", "yeah", "All", "Edges", "Air Exposed", "Concealed Faces", "Looked At");
	public static final FloatOption OUTLINE_WIDTH = new FloatOption("Width");
	public static final FloatOption OUTLINE_EXPAND = new FloatOption("Expand By");
	public static final CycleOption<String> OUTLINE_DEPTH_TEST = new CycleOption<>("Depth Test", "yeah", "Always Pass", "Default", "Concealed Only");
	public static final FloatOption OUTLINE_CUT_CENTER = new FloatOption("Cut From Center");
	public static final FloatOption OUTLINE_CUT_CORNER = new FloatOption("Cut From Corner");

	public static final GooberConfigBuilder BUILDER = GooberConfigBuilder.create("LARP CBH")
			.category("Outline", "")
			.option(OUTLINE_ENABLED)
			.build()
			.sectionWithOptions("Color", OUTLINE_COLOR, OUTLINE_SECONDARY_COLOR, OUTLINE_ALPHA, OUTLINE_RAINBOW)
			.sectionWithOptions("Misc.", OUTLINE_MODE, OUTLINE_WIDTH, OUTLINE_EXPAND, OUTLINE_DEPTH_TEST, OUTLINE_CUT_CENTER, OUTLINE_CUT_CORNER)
			.build()
			.category("Fill", "")
			.build();
}
