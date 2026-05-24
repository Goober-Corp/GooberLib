package com.goobercorp.gooberlib.api.widgets;

import com.goobercorp.gooberlib.gui.TickBoxWidget;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import java.util.function.Function;

public class BooleanWidgetProviders {
	public static WidgetProvider<BooleanOption> tickBox() {
		return TickBoxWidget::new;
	}

	public static WidgetProvider<BooleanOption> text(Function<BooleanOption, Text> valueFormatter) {
		return (theOption, x, y, width, height) -> new TextWidget(x, y, width, height, valueFormatter.apply(theOption), MinecraftClient.getInstance().textRenderer);
	}

	public static WidgetProvider<BooleanOption> textValue(Text whenItsTrue, Text whenItsFalse) {
		return (theOption, x, y, width, height) -> new TextWidget(x, y, width, height, theOption.value ? whenItsTrue : whenItsFalse, MinecraftClient.getInstance().textRenderer);
	}

	// usage:
	public static final BooleanOption bool1 = new BooleanOption(Text.of("tick box option"), _ -> Text.of("description"), false, BooleanWidgetProviders.tickBox());
	public static final BooleanOption bool2 = new BooleanOption(Text.of("text option"), _ -> Text.of("description"), false, BooleanWidgetProviders.text(o -> o.getValue() ? Text.of("its on!") : Text.of("its off...")));
}
