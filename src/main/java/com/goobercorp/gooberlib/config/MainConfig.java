package com.goobercorp.gooberlib.config;

import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;

@GooberConfig(modId = "gooberlib")
public class MainConfig {

	public static final int primaryCol = 0xFFCCCCFF;
	public static final int shadowCol = 0xFF32323e;
	public static final int bgColor = 0x80000000;
	public static final BooleanOption ENABLE_INFINITE_TAB_SCROLLING = new BooleanOption("Enable infinite tab scrolling", "");
}
