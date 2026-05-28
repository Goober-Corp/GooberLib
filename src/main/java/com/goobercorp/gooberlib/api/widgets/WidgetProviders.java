package com.goobercorp.gooberlib.api.widgets;

import com.goobercorp.gooberlib.gui.*;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.individual.java.StringOption;
import com.goobercorp.gooberlib.option.individual.minecraft.IdentifierOption;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;
import com.goobercorp.gooberlib.option.individual.primitive.CharOption;
import com.goobercorp.gooberlib.option.individual.primitive.NumberOption;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.util.function.Function;
import java.util.function.Predicate;

public class WidgetProviders {
	public static class Predicates {
		public static final Predicate<String> IDENTIFIER = s -> {
			try {
				Identifier.of(s);
				return true;
			} catch (InvalidIdentifierException _) {
				return false;
			}
		};
	}


	public static <T extends NumberOption<T>> WidgetProvider<T> numberSlider() {
		return EvilSliderWidget::new;
	}

	public static <T extends NumberOption<T>> WidgetProvider<T> numberSliderWithFormatter(Function<T, Text> valueFormatter) {
		return (theOption, x, y, width, height) -> new EvilSliderWidget(theOption, x, y, width, height, valueFormatter);
	}

	// todo: remove name from evil string widget
	public static <T extends NumberOption<T>> WidgetProvider<T> numberField() {
		return ((theOption, x, y, width, height) -> new EvilStringWidgetWithName(theOption.name(), x, y, width, height, theOption::setFromString, theOption.getPredicate(), theOption instanceof CharOption c ? String.valueOf(c.value) : theOption.getNumberValue().toString()));
	}

	public static WidgetProvider<IdentifierOption> identifierOneField() {
		return (theOption, x, y, width, height) -> new EvilStringWidgetWithName(theOption.name(), x, y, width, height, s -> {
			try {
				theOption.setValue(Identifier.of(s));
			} catch (InvalidIdentifierException _) {
			}
		}, Predicates.IDENTIFIER, theOption.getValue().toShortString());
	}

	public static WidgetProvider<IdentifierOption> identifierTwoFields() {
		return IdentifiersWidget::new;
	}

	public static WidgetProvider<BooleanOption> booleanTickBox() {
		return TickBoxWidget::new;
	}

	public static WidgetProvider<StringOption> stringField() {
		return ((theOption, x, y, width, height) -> new EvilStringWidgetWithName(theOption.name(), x, y, width, height, theOption::setValue, _ -> true, theOption.value));
	}
}
