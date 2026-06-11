package com.goobercorp.gooberlib.gui.option;

import com.goobercorp.gooberlib.gui.EvilBaseWidget;
import com.goobercorp.gooberlib.option.individual.hotkey.HotkeyOption;

public class HotkeyWidget extends EvilBaseWidget {
	HotkeyOption opt;

	public HotkeyWidget(HotkeyOption opt, int x, int y, int width, int height) {
		super(opt.name(), x, y, width, height);
		this.opt = opt;
	}

}
