package com.goobercorp.gooberlib.interfaces;

import com.goobercorp.gooberlib.option.Option;
import net.minecraft.client.gui.widget.ClickableWidget;

public interface WidgetProvider<T extends Option<T>> {
	// x and y are secretly always 0. shh, don't tell anyone!
	ClickableWidget makeWidget(T theOption, int x, int y, int width, int height);
}
