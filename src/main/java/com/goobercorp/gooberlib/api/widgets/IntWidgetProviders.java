package com.goobercorp.gooberlib.api.widgets;

import com.goobercorp.gooberlib.gui.EvilSliderWidget;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.individual.primitive.IntOption;
import net.minecraft.text.Text;

import java.util.function.Function;

public class IntWidgetProviders {
	public static WidgetProvider<IntOption> slider() {
		return EvilSliderWidget::new;
	}

	public static WidgetProvider<IntOption> sliderWithFormatter(Function<IntOption, Text> valueFormatter) {
		return (theOption, x, y, width, height) -> new EvilSliderWidget(theOption, x, y, width, height, valueFormatter);
	}
}
