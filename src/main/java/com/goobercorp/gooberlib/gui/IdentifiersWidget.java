package com.goobercorp.gooberlib.gui;

import com.goobercorp.gooberlib.api.widgets.WidgetProviders;
import com.goobercorp.gooberlib.option.individual.minecraft.IdentifierOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;

import java.util.ArrayList;
import java.util.List;

// todo: colon in between the text field widgets
public class IdentifiersWidget extends ClickableParentWidget {
	private final EvilStringWidget namespace;
	private final EvilStringWidget path;

	public IdentifiersWidget(IdentifierOption option, int x, int y, int width, int height) {
		super(x, y, width, height, option.name(), new ArrayList<>(List.of(
				new TextWidget(x, y, width, height, option.name(), MinecraftClient.getInstance().textRenderer),
				(this.namespace = new EvilStringWidget(Text.empty(), x + MinecraftClient.getInstance().textRenderer.getWidth(option.name()), y, (width - MinecraftClient.getInstance().textRenderer.getWidth(option.name())) / 2 - 2, height, _ -> {
				}, WidgetProviders.Predicates.IDENTIFIER, option.getValue().getNamespace())),
				(this.path = new EvilStringWidget(Text.empty(), x + (width - MinecraftClient.getInstance().textRenderer.getWidth(option.name())) / 2 - 2 + MinecraftClient.getInstance().textRenderer.getWidth(option.name()), y, (width - MinecraftClient.getInstance().textRenderer.getWidth(option.name())) / 2 - 2, height, _ -> {
				}, WidgetProviders.Predicates.IDENTIFIER, option.getValue().getPath()))
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
