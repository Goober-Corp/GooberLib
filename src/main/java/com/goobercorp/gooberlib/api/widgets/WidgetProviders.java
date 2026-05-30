package com.goobercorp.gooberlib.api.widgets;

import com.goobercorp.gooberlib.gui.*;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.individual.java.StringOption;
import com.goobercorp.gooberlib.option.individual.minecraft.BlockPosOption;
import com.goobercorp.gooberlib.option.individual.minecraft.IdentifierOption;
import com.goobercorp.gooberlib.option.individual.minecraft.Vec3dOption;
import com.goobercorp.gooberlib.option.individual.minecraft.Vec3iOption;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;
import com.goobercorp.gooberlib.option.individual.primitive.CharOption;
import com.goobercorp.gooberlib.option.individual.primitive.NumberOption;
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
	public static class Predicates {
		public static final Predicate<String> IDENTIFIER = s -> {
			try {
				Identifier.of(s);
				return true;
			} catch (InvalidIdentifierException _) {
				return false;
			}
		};
		public static final Predicate<String> INTEGER = s -> {
			try {
				Integer.parseInt(s);
				return true;
			} catch (NumberFormatException _) {
				return false;
			}
		};
		public static final Predicate<String> DOUBLE = s -> {
			try {
				Double.parseDouble(s);
				return true;
			} catch (NumberFormatException _) {
				return false;
			}
		};
	}

	private static TextRenderer font() {
		return MinecraftClient.getInstance().textRenderer;
	}

	public static WidgetProvider<BlockPosOption> blockPosFields() {
		return ((theOption, x, y, width, height) -> {
			var widgetX = font().getWidth(theOption.name()) + 2;
			var widgetWidth = width - font().getWidth(theOption.name());
			var xWidget = new EvilStringWidget(widgetX, y, widgetWidth / 3, height, null, Predicates.INTEGER, "" + theOption.getX());
			var yWidget = new EvilStringWidget(widgetX + widgetWidth / 3, y, widgetWidth / 3, height, null, Predicates.INTEGER, "" + theOption.getY());
			var zWidget = new EvilStringWidget(widgetX + widgetWidth * 2 / 3, y, widgetWidth / 3, height, null, Predicates.INTEGER, "" + theOption.getZ());
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
			var xWidget = new EvilStringWidget(widgetX, y, widgetWidth / 3, height, null, Predicates.INTEGER, "" + theOption.getX());
			var yWidget = new EvilStringWidget(widgetX + widgetWidth / 3, y, widgetWidth / 3, height, null, Predicates.INTEGER, "" + theOption.getY());
			var zWidget = new EvilStringWidget(widgetX + widgetWidth * 2 / 3, y, widgetWidth / 3, height, null, Predicates.INTEGER, "" + theOption.getZ());
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
			var xWidget = new EvilStringWidget(widgetX, y, widgetWidth / 3, height, null, Predicates.DOUBLE, "" + theOption.getX());
			var yWidget = new EvilStringWidget(widgetX + widgetWidth / 3, y, widgetWidth / 3, height, null, Predicates.DOUBLE, "" + theOption.getY());
			var zWidget = new EvilStringWidget(widgetX + widgetWidth * 2 / 3, y, widgetWidth / 3, height, null, Predicates.DOUBLE, "" + theOption.getZ());
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
		// todo: colon in between the text field widgets
		return (theOption, x, y, width, height) -> {
			var widgetX = font().getWidth(theOption.name()) + 2;
			var widgetWidth = width - font().getWidth(theOption.name());

			var namespace = new EvilStringWidget(widgetX, y, widgetWidth / 2, height, null, Predicates.IDENTIFIER, theOption.getValue().getNamespace());
			var path = new EvilStringWidget(widgetX + widgetWidth / 2, y, widgetWidth / 2, height, null, Predicates.IDENTIFIER, theOption.getValue().getNamespace());

			Consumer<String> changedListener = _ -> {
				try {
					theOption.setValue(Identifier.of(namespace.getText(), path.getText()));
				} catch (InvalidIdentifierException _) {
				}
			};
			namespace.setChangedListener(changedListener);
			path.setChangedListener(changedListener);

			return new ClickableParentWidget(x, y, width, height, Text.empty(), List.of(new TextWidget(x, y, width, height, theOption.name(), font()), namespace, path));
		};
	}

	public static WidgetProvider<BooleanOption> booleanTickBox() {
		return TickBoxWidget::new;
	}

	public static WidgetProvider<StringOption> stringField() {
		return ((theOption, x, y, width, height) -> new EvilStringWidgetWithName(theOption.name(), x, y, width, height, theOption::setValue, _ -> true, theOption.value));
	}
}
