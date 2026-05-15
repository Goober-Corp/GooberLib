package com.goobercorp.gooberlib.interfaces;

import com.goobercorp.gooberlib.builder.v3.Option;
import net.minecraft.client.gui.widget.ClickableWidget;

public interface WidgetProvider {
	// x and y are secretly always 0. shh, don't tell anyone!
	ClickableWidget makeWidget(Option<?> theOption, int x, int y, double width, double height);
}
