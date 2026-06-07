package com.goobercorp.gooberlib.api.widgets;

import com.goobercorp.gooberlib.gui.option.*;
import com.goobercorp.gooberlib.gui.util.ClickableParentWidget;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.individual.java.ColorOption;
import com.goobercorp.gooberlib.option.individual.java.CycleOption;
import com.goobercorp.gooberlib.option.individual.java.StringOption;
import com.goobercorp.gooberlib.option.individual.minecraft.BlockPosOption;
import com.goobercorp.gooberlib.option.individual.minecraft.IdentifierOption;
import com.goobercorp.gooberlib.option.individual.minecraft.Vec3dOption;
import com.goobercorp.gooberlib.option.individual.minecraft.Vec3iOption;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;
import com.goobercorp.gooberlib.option.individual.primitive.CharOption;
import com.goobercorp.gooberlib.option.individual.primitive.NumberOption;
import com.goobercorp.gooberlib.option.individual.primitive.range.NumberRangeOption;
import com.goobercorp.gooberlib.util.Predicates;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.goobercorp.gooberlib.util.Util;
import net.minecraft.IdentifierException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

import static com.google.common.base.Predicates.alwaysTrue;

public class WidgetProviders {
	private static Font font() {
		return Minecraft.getInstance().font;
	}

	public static WidgetProvider<BlockPosOption> blockPosFields() {
		return ((theOption, x, y, width, height) -> {
			var widgetX = font().width(theOption.name()) + 2;
			var widgetWidth = width - font().width(theOption.name());
			var xWidget = new EvilStringWidget(widgetX, y, widgetWidth / 3, height, null, Predicates.INTEGER, Predicates.INTEGER_IMMEDIATE, "" + theOption.getX(), 0xFFFF0000);
			var yWidget = new EvilStringWidget(widgetX + widgetWidth / 3, y, widgetWidth / 3, height, null, Predicates.INTEGER, Predicates.INTEGER_IMMEDIATE, "" + theOption.getY(), 0xFF00FF00);
			var zWidget = new EvilStringWidget(widgetX + widgetWidth * 2 / 3, y, widgetWidth / 3, height, null, Predicates.INTEGER, Predicates.INTEGER_IMMEDIATE, "" + theOption.getZ(), 0xFF0000FF);
			Consumer<String> changedListener = _ -> {
				try {
					theOption.setValue(new BlockPos(Integer.parseInt(xWidget.getText()), Integer.parseInt(yWidget.getText()), Integer.parseInt(zWidget.getText())));
				} catch (NumberFormatException _) {
				}
			};
			xWidget.setChangedListener(changedListener);
			yWidget.setChangedListener(changedListener);
			zWidget.setChangedListener(changedListener);

			return new ClickableParentWidget(x, y, width, height, Component.empty(), List.of(new StringWidget(x, y, width, height, theOption.name(), font()), xWidget, yWidget, zWidget));
		});
	}

	public static WidgetProvider<Vec3iOption> vec3iFields() {
		return ((theOption, x, y, width, height) -> {
			var widgetX = font().width(theOption.name()) + 2;
			var widgetWidth = width - font().width(theOption.name());
			var xWidget = new EvilStringWidget(widgetX, y, widgetWidth / 3, height, null, Predicates.INTEGER, Predicates.INTEGER_IMMEDIATE, "" + theOption.getX(), 0xFFFF0000);
			var yWidget = new EvilStringWidget(widgetX + widgetWidth / 3, y, widgetWidth / 3, height, null, Predicates.INTEGER, Predicates.INTEGER_IMMEDIATE, "" + theOption.getY(), 0xFF00FF00);
			var zWidget = new EvilStringWidget(widgetX + widgetWidth * 2 / 3, y, widgetWidth / 3, height, null, Predicates.INTEGER, Predicates.INTEGER_IMMEDIATE, "" + theOption.getZ(), 0xFF0000FF);
			Consumer<String> changedListener = _ -> {
				try {
					theOption.setValue(new Vec3i(Integer.parseInt(xWidget.getText()), Integer.parseInt(yWidget.getText()), Integer.parseInt(zWidget.getText())));
				} catch (NumberFormatException _) {
				}
			};
			xWidget.setChangedListener(changedListener);
			yWidget.setChangedListener(changedListener);
			zWidget.setChangedListener(changedListener);

			return new ClickableParentWidget(x, y, width, height, Component.empty(), List.of(new StringWidget(x, y, width, height, theOption.name(), font()), xWidget, yWidget, zWidget));
		});
	}

	public static WidgetProvider<Vec3dOption> vec3dFields() {
		return ((theOption, x, y, width, height) -> {
			var widgetX = font().width(theOption.name()) + 2;
			var widgetWidth = width - font().width(theOption.name());
			var xWidget = new EvilStringWidget(widgetX, y, widgetWidth / 3, height, null, Predicates.DOUBLE, Predicates.DOUBLE_IMMEDIATE, "" + theOption.getX(), 0xFFFF0000);
			var yWidget = new EvilStringWidget(widgetX + widgetWidth / 3, y, widgetWidth / 3, height, null, Predicates.DOUBLE, Predicates.DOUBLE_IMMEDIATE, "" + theOption.getY(), 0xFF00FF00);
			var zWidget = new EvilStringWidget(widgetX + widgetWidth * 2 / 3, y, widgetWidth / 3, height, null, Predicates.DOUBLE, Predicates.DOUBLE_IMMEDIATE, "" + theOption.getZ(), 0xFF0000FF);
			Consumer<String> changedListener = _ -> {
				try {
					theOption.setValue(new Vec3(Double.parseDouble(xWidget.getText()), Double.parseDouble(yWidget.getText()), Double.parseDouble(zWidget.getText())));
				} catch (NumberFormatException _) {
				}
			};
			xWidget.setChangedListener(changedListener);
			yWidget.setChangedListener(changedListener);
			zWidget.setChangedListener(changedListener);

			return new ClickableParentWidget(x, y, width, height, Component.empty(), List.of(new StringWidget(x, y, width, height, theOption.name(), font()), xWidget, yWidget, zWidget));
		});
	}

	public static <T extends NumberOption<T>> WidgetProvider<T> numberSlider() {
		return EvilSliderWidget::new;
	}

	public static <T extends NumberRangeOption<T>> WidgetProvider<T> rangeOption() {
		return RangeSliderWidget::new;
	}

	public static <E> WidgetProvider<CycleOption<E>> cyclingOption() {
		return (opt, x, y, width, height) -> new CyclingOptionWidget(opt, x, y, width, height, Util.fromCharsFunction(opt.getDisplayNameProvider()));
	}

	public static <T extends NumberOption<T>> WidgetProvider<T> numberSliderWithFormatter(Function<T, CharSequence> valueFormatter) {
		return (theOption, x, y, width, height) -> new EvilSliderWidget(theOption, x, y, width, height, Util.fromCharsFunction(valueFormatter));
	}

	public static <T extends NumberOption<T>> WidgetProvider<T> numberField() {
		return ((theOption, x, y, width, height) -> new EvilStringWidgetWithName(theOption.name(), x, y, width, height, theOption::setFromString, theOption.getPredicate(), theOption.getImmediatePredicate(), theOption instanceof CharOption c ? String.valueOf(c.value) : theOption.getNumberValue().toString()));
	}

	public static WidgetProvider<ColorOption> colorField() {
		return ((theOption, x, y, width, height) -> new EvilStringColorWidget(theOption.name(), x, y, width, height, theOption::setFromString, theOption.getPredicate(), theOption.getImmediatePredicate(), "#" + Integer.toHexString(theOption.value), theOption));
	}

	public static WidgetProvider<IdentifierOption> identifierOneField() {
		return (theOption, x, y, width, height) -> new EvilStringWidgetWithName(theOption.name(), x, y, width, height, s -> {
			try {
				theOption.setValue(Identifier.parse(s));
			} catch (IdentifierException _) {
			}
		}, Predicates.IDENTIFIER, Predicates.IDENTIFIER_IMMEDIATE, theOption.getValue().toShortString());
	}

	public static WidgetProvider<IdentifierOption> identifierTwoFields() {
		return (theOption, x, y, width, height) -> {
			var widgetX = font().width(theOption.name()) + 2;
			var widgetWidth = width - font().width(theOption.name());

			var namespace = new EvilStringWidget(widgetX, y, widgetWidth / 2 - 4, height, null, Predicates.IDENTIFIER, Predicates.IDENTIFIER_IMMEDIATE, theOption.getValue().getNamespace());
			var path = new EvilStringWidget(widgetX + widgetWidth / 2, y, widgetWidth / 2 + 4, height, null, Predicates.IDENTIFIER, Predicates.IDENTIFIER_IMMEDIATE, theOption.getValue().getNamespace());

			Consumer<String> changedListener = _ -> {
				try {
					theOption.setValue(Identifier.fromNamespaceAndPath(namespace.getText(), path.getText()));
				} catch (IdentifierException _) {
				}
			};
			namespace.setChangedListener(changedListener);
			path.setChangedListener(changedListener);
			//TODO: replace text widget with my own impl that allows a custom text color
			return new ClickableParentWidget(x, y, width, height, Component.empty(), List.of(new StringWidget(x, y, width, height, theOption.name(), font()), namespace, path, new StringWidget(widgetX + widgetWidth / 2 - 3, y, width, height, Component.nullToEmpty(":"), font())));
		};
	}

	public static WidgetProvider<BooleanOption> booleanTickBox() {
		return TickBoxWidget::new;
	}

	public static WidgetProvider<BooleanOption> booleanSliderWidget() {
		return SliderToggleWidget::new;
	}

	public static WidgetProvider<BooleanOption> booleanToggleWidget() {
		return (theOption, x, y, width, height) -> new CyclingOptionWidget(theOption, x, y, width, height, o -> CommonComponents.optionStatus(o.getValue()));
	}

	public static WidgetProvider<BooleanOption> booleanToggleWidget(CharSequence whenTrue, CharSequence whenFalse) {
		return (theOption, x, y, width, height) -> new CyclingOptionWidget(theOption, x, y, width, height, Util.fromCharsFunction(o -> o.getValue() ? whenTrue : whenFalse));
	}

	public static WidgetProvider<StringOption> stringField() {
		return ((theOption, x, y, width, height) -> new EvilStringWidgetWithName(theOption.name(), x, y, width, height, theOption::setValue, alwaysTrue(), alwaysTrue(), theOption.value));
	}

	public static WidgetProvider<StringOption> stringField(Function<String, List<String>> suggestionProvider) {
		return (theOption, x, y, width, height) -> {
			var widget = new EvilStringWidgetWithName(theOption.name(), x, y, width, height, theOption::setValue, alwaysTrue(), alwaysTrue(), theOption.value);
			widget.setSuggestionProvider(suggestionProvider);
			return widget;
		};
	}
}
