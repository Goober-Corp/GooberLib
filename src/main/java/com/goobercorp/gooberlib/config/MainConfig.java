package com.goobercorp.gooberlib.config;

import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;

@GooberConfig(modId = "gooberlib")
public class MainConfig {
	public static final BooleanOption ENABLE_INFINITE_TAB_SCROLLING = new BooleanOption("Enable infinite tab scrolling", "");
}
