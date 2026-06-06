package com.goobercorp.gooberlib.config;

import com.goobercorp.gooberlib.GooberLibEntrypoint;
import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.option.individual.hotkey.HotkeyOption;
import com.goobercorp.gooberlib.option.individual.misc.ButtonOption;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;
import com.goobercorp.gooberlib.screen.ShowcaseScreen;
import net.minecraft.client.Minecraft;

@GooberConfig(modId = "gooberlib")
public class MainConfig {

	public static final int primaryCol = 0xFFd1bbfc;
	public static final int shadowCol = 0xFF342e3f;
	public static final int bgColor = 0x80000000;
	public static final BooleanOption ENABLE_INFINITE_TAB_SCROLLING = new BooleanOption("Enable infinite tab scrolling", "");
	public static final BooleanOption PPWW_BOUNDS = new BooleanOption("Bounds for PPWW", "");
	public static final ButtonOption A = new ButtonOption("Rediscover configs", GooberLibEntrypoint::init);
	public static final ButtonOption SHOWCASE = new ButtonOption("Open showcase screen", () -> Minecraft.getInstance().setScreen(new ShowcaseScreen()));
	public static final HotkeyOption HOTKEY = new HotkeyOption("Rediscover configs (hotkey)", "", "LEFT_CONTROL, r", 5, GooberLibEntrypoint::init);
}
