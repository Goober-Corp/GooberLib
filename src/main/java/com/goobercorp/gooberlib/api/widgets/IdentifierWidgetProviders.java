package com.goobercorp.gooberlib.api.widgets;

import com.goobercorp.gooberlib.gui.ClickableParentWidget;
import com.goobercorp.gooberlib.gui.EvilStringWidget;
import com.goobercorp.gooberlib.gui.EvilStringWidgetWithName;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.individual.minecraft.IdentifierOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class IdentifierWidgetProviders {
	public static final Predicate<String> IDENTIFIER_PREDICATE = s -> {
		try {
			Identifier.of(s);
			return true;
		} catch (InvalidIdentifierException _) {
			return false;
		}
	};

	public static WidgetProvider<IdentifierOption> oneField() {
		return (theOption, x, y, width, height) -> new EvilStringWidgetWithName(theOption.name(), x, y, width, height, s -> {
			try {
				theOption.setValue(Identifier.of(s));
			} catch (InvalidIdentifierException _) {
			}
		}, IDENTIFIER_PREDICATE, theOption.getValue().toShortString());
	}

	public static WidgetProvider<IdentifierOption> twoFields() {
		return IdentifiersWidget::new;
	}

	// todo: colon in between the text field widgets
	public static class IdentifiersWidget extends ClickableParentWidget {
		private final EvilStringWidget namespace;
		private final EvilStringWidget path;

		public IdentifiersWidget(IdentifierOption option, int x, int y, int width, int height) {
			super(x, y, width, height, option.name(), new ArrayList<>(List.of(
					new TextWidget(x, y, width, height, option.name(), MinecraftClient.getInstance().textRenderer),
					(this.namespace = new EvilStringWidget(Text.empty(), x + MinecraftClient.getInstance().textRenderer.getWidth(option.name()), y, (width - MinecraftClient.getInstance().textRenderer.getWidth(option.name())) / 2 - 2, height, _ -> {
					}, IDENTIFIER_PREDICATE, option.getValue().getNamespace())),
					(this.path = new EvilStringWidget(Text.empty(), x + (width - MinecraftClient.getInstance().textRenderer.getWidth(option.name())) / 2 - 2 + MinecraftClient.getInstance().textRenderer.getWidth(option.name()), y, (width - MinecraftClient.getInstance().textRenderer.getWidth(option.name())) / 2 - 2, height, _ -> {
					}, IDENTIFIER_PREDICATE, option.getValue().getPath()))
			)));
			namespace.setChangedListener((s) -> {
				try {
					option.setValue(Identifier.of(s, path.getText()));
				} catch (InvalidIdentifierException _) {
				}
			});
			path.setChangedListener((s) -> {
				try {
					option.setValue(Identifier.of(namespace.getText(), s));
				} catch (InvalidIdentifierException _) {
				}
			});
		}
	}
}
