package com.goobercorp.gooberlib.api.widgets;

import com.goobercorp.gooberlib.gui.EvilSliderWidget;
import com.goobercorp.gooberlib.gui.EvilStringWidget;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.individual.primitive.CharOption;
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

	public static <T extends NumberOption<T>> WidgetProvider<T> field() {
		return ((theOption, x, y, width, height) -> new EvilStringWidget(theOption.name(), x, y, width, height, theOption::setFromString, theOption.getPredicate(), theOption instanceof CharOption c ? String.valueOf(c.value) : theOption.getNumberValue().toString()));
	}
}
