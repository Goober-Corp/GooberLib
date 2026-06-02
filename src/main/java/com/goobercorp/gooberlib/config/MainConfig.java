package com.goobercorp.gooberlib.config;

import com.goobercorp.gooberlib.GooberLibEntrypoint;
import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.option.individual.hotkey.HotkeyOption;
import com.goobercorp.gooberlib.option.individual.misc.ButtonOption;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;

@GooberConfig(modId = "gooberlib")
public class MainConfig {

	public static final int primaryCol = 0xFF3d9db9;
	public static final int shadowCol = 0xFF0f272e;
	public static final int bgColor = 0x80000000;
	public static final BooleanOption ENABLE_INFINITE_TAB_SCROLLING = new BooleanOption("Enable infinite tab scrolling", "");
	public static final ButtonOption A = new ButtonOption("Rediscover configs", GooberLibEntrypoint::init);
	public static final HotkeyOption HOTKEY = new HotkeyOption("Rediscover configs (hotkey)", "", "LEFT_CONTROL, r", 5, GooberLibEntrypoint::init);
}
