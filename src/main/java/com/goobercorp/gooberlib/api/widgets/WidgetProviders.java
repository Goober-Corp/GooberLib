package com.goobercorp.gooberlib.api.widgets;

import com.goobercorp.gooberlib.gui.*;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.individual.java.ColorOption;
import com.goobercorp.gooberlib.option.individual.java.StringOption;
import com.goobercorp.gooberlib.option.individual.minecraft.BlockPosOption;
import com.goobercorp.gooberlib.option.individual.minecraft.IdentifierOption;
import com.goobercorp.gooberlib.option.individual.minecraft.Vec3dOption;
import com.goobercorp.gooberlib.option.individual.minecraft.Vec3iOption;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;
import com.goobercorp.gooberlib.option.individual.primitive.CharOption;
import com.goobercorp.gooberlib.option.individual.primitive.NumberOption;
import com.goobercorp.gooberlib.option.individual.primitive.range.NumberRangeOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class WidgetProviders {
	@SuppressWarnings("ResultOfMethodCallIgnored")
	public static class Predicates {


		private static Predicate<String> falseIfException(Consumer<String> sEr, Class<? extends Throwable> clazz) {
			return s -> {
				try {
					sEr.accept(s);
					return true;
				} catch (Throwable t) {
					if (clazz.isInstance(t)) {
						return false;
					} else {
						throw t;
					}
				}
			};
		}

		public static final Predicate<String> COLOR = falseIfException(Long::decode, NumberFormatException.class);
		public static final Predicate<String> IDENTIFIER = falseIfException(Identifier::of, InvalidIdentifierException.class);
		public static final Predicate<String> INTEGER = falseIfException(Integer::parseInt, NumberFormatException.class);
		public static final Predicate<String> DOUBLE = falseIfException(Double::parseDouble, NumberFormatException.class);
		public static final Predicate<String> FLOAT = falseIfException(Float::parseFloat, NumberFormatException.class);
		public static final Predicate<String> LONG = falseIfException(Long::parseLong, NumberFormatException.class);
		public static final Predicate<String> SHORT = falseIfException(Short::parseShort, NumberFormatException.class);
		public static final Predicate<String> BYTE = falseIfException(Byte::parseByte, NumberFormatException.class);
	}

	private static TextRenderer font() {
		return MinecraftClient.getInstance().textRenderer;
	}

	public static WidgetProvider<BlockPosOption> blockPosFields() {
		return ((theOption, x, y, width, height) -> {
			var widgetX = font().getWidth(theOption.name()) + 2;
			var widgetWidth = width - font().getWidth(theOption.name());
			var xWidget = new EvilStringWidget(widgetX, y, widgetWidth / 3, height, null, Predicates.INTEGER, "" + theOption.getX(), 0xFFFF0000);
			var yWidget = new EvilStringWidget(widgetX + widgetWidth / 3, y, widgetWidth / 3, height, null, Predicates.INTEGER, "" + theOption.getY(), 0xFF00FF00);
			var zWidget = new EvilStringWidget(widgetX + widgetWidth * 2 / 3, y, widgetWidth / 3, height, null, Predicates.INTEGER, "" + theOption.getZ(), 0xFF0000FF);
			Consumer<String> changedListener = _ -> {
				try {
					theOption.setValue(new BlockPos(Integer.parseInt(xWidget.getText()), Integer.parseInt(yWidget.getText()), Integer.parseInt(zWidget.getText())));
				} catch (NumberFormatException _) {
				}
			};
			xWidget.setChangedListener(changedListener);
			yWidget.setChangedListener(changedListener);
			zWidget.setChangedListener(changedListener);

			return new ClickableParentWidget(x, y, width, height, Text.empty(), List.of(new TextWidget(x, y, width, height, theOption.name(), font()), xWidget, yWidget, zWidget));
		});
	}

	public static WidgetProvider<Vec3iOption> vec3iFields() {
		return ((theOption, x, y, width, height) -> {
			var widgetX = font().getWidth(theOption.name()) + 2;
			var widgetWidth = width - font().getWidth(theOption.name());
			var xWidget = new EvilStringWidget(widgetX, y, widgetWidth / 3, height, null, Predicates.INTEGER, "" + theOption.getX(), 0xFFFF0000);
			var yWidget = new EvilStringWidget(widgetX + widgetWidth / 3, y, widgetWidth / 3, height, null, Predicates.INTEGER, "" + theOption.getY(), 0xFF00FF00);
			var zWidget = new EvilStringWidget(widgetX + widgetWidth * 2 / 3, y, widgetWidth / 3, height, null, Predicates.INTEGER, "" + theOption.getZ(), 0xFF0000FF);
			Consumer<String> changedListener = _ -> {
				try {
					theOption.setValue(new Vec3i(Integer.parseInt(xWidget.getText()), Integer.parseInt(yWidget.getText()), Integer.parseInt(zWidget.getText())));
				} catch (NumberFormatException _) {
				}
			};
			xWidget.setChangedListener(changedListener);
			yWidget.setChangedListener(changedListener);
			zWidget.setChangedListener(changedListener);

			return new ClickableParentWidget(x, y, width, height, Text.empty(), List.of(new TextWidget(x, y, width, height, theOption.name(), font()), xWidget, yWidget, zWidget));
		});
	}

	public static WidgetProvider<Vec3dOption> vec3dFields() {
		return ((theOption, x, y, width, height) -> {
			var widgetX = font().getWidth(theOption.name()) + 2;
			var widgetWidth = width - font().getWidth(theOption.name());
			var xWidget = new EvilStringWidget(widgetX, y, widgetWidth / 3, height, null, Predicates.DOUBLE, "" + theOption.getX(), 0xFFFF0000);
			var yWidget = new EvilStringWidget(widgetX + widgetWidth / 3, y, widgetWidth / 3, height, null, Predicates.DOUBLE, "" + theOption.getY(), 0xFF00FF00);
			var zWidget = new EvilStringWidget(widgetX + widgetWidth * 2 / 3, y, widgetWidth / 3, height, null, Predicates.DOUBLE, "" + theOption.getZ(), 0xFF0000FF);
			Consumer<String> changedListener = _ -> {
				try {
					theOption.setValue(new Vec3d(Double.parseDouble(xWidget.getText()), Double.parseDouble(yWidget.getText()), Double.parseDouble(zWidget.getText())));
				} catch (NumberFormatException _) {
				}
			};
			xWidget.setChangedListener(changedListener);
			yWidget.setChangedListener(changedListener);
			zWidget.setChangedListener(changedListener);

			return new ClickableParentWidget(x, y, width, height, Text.empty(), List.of(new TextWidget(x, y, width, height, theOption.name(), font()), xWidget, yWidget, zWidget));
		});
	}

	public static <T extends NumberOption<T>> WidgetProvider<T> numberSlider() {
		return EvilSliderWidget::new;
	}

	public static <T extends NumberRangeOption<T>> WidgetProvider<T> rangeOption() {
		return RangeSliderWidget::new;
	}

	public static <T extends NumberOption<T>> WidgetProvider<T> numberSliderWithFormatter(Function<T, Text> valueFormatter) {
		return (theOption, x, y, width, height) -> new EvilSliderWidget(theOption, x, y, width, height, valueFormatter);
	}

	public static <T extends NumberOption<T>> WidgetProvider<T> numberField() {
		return ((theOption, x, y, width, height) -> new EvilStringWidgetWithName(theOption.name(), x, y, width, height, theOption::setFromString, theOption.getPredicate(), theOption instanceof CharOption c ? String.valueOf(c.value) : theOption.getNumberValue().toString()));
	}

	public static <T extends NumberOption<T>> WidgetProvider<ColorOption> colorField() {
		return ((theOption, x, y, width, height) -> new EvilStringColorWidget(theOption.name(), x, y, width, height, theOption::setFromString, Predicates.COLOR, "#" + Integer.toHexString(theOption.value), theOption));
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
		return (theOption, x, y, width, height) -> {
			var widgetX = font().getWidth(theOption.name()) + 2;
			var widgetWidth = width - font().getWidth(theOption.name());

			var namespace = new EvilStringWidget(widgetX, y, widgetWidth / 2 - 4, height, null, Predicates.IDENTIFIER, theOption.getValue().getNamespace());
			var path = new EvilStringWidget(widgetX + widgetWidth / 2, y, widgetWidth / 2 + 4, height, null, Predicates.IDENTIFIER, theOption.getValue().getNamespace());

			Consumer<String> changedListener = _ -> {
				try {
					theOption.setValue(Identifier.of(namespace.getText(), path.getText()));
				} catch (InvalidIdentifierException _) {
				}
			};
			namespace.setChangedListener(changedListener);
			path.setChangedListener(changedListener);
			//TODO: replace text widget with my own impl that allows a custom text color
			return new ClickableParentWidget(x, y, width, height, Text.empty(), List.of(new TextWidget(x, y, width, height, theOption.name(), font()), namespace, path, new TextWidget(widgetX + widgetWidth / 2 - 3, y, width, height, Text.of(":"), font())));
		};
	}

	public static WidgetProvider<BooleanOption> booleanTickBox() {
		return TickBoxWidget::new;
	}

	public static WidgetProvider<StringOption> stringField() {
		return ((theOption, x, y, width, height) -> new EvilStringWidgetWithName(theOption.name(), x, y, width, height, theOption::setValue, _ -> true, theOption.value));
	}
}
