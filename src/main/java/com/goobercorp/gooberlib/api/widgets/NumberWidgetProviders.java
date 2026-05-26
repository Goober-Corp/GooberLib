package com.goobercorp.gooberlib.api.widgets;

import com.goobercorp.gooberlib.gui.EvilSliderWidget;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.individual.primitive.NumberOption;
import net.minecraft.text.Text;

import java.util.function.Function;

public class NumberWidgetProviders {
	public static <T extends NumberOption<T>> WidgetProvider<T> slider() {
		return EvilSliderWidget::new;
	}

	public static <T extends NumberOption<T>> WidgetProvider<T> sliderWithFormatter(Function<T, Text> valueFormatter) {
		return (theOption, x, y, width, height) -> new EvilSliderWidget(theOption, x, y, width, height, valueFormatter);
	}
}
