package com.goobercorp.gooberlib.api.widgets;

import com.goobercorp.gooberlib.gui.TickBoxWidget;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;

public class BooleanWidgetProviders {
	public static WidgetProvider<BooleanOption> tickBox() {
		return TickBoxWidget::new;
	}
}
