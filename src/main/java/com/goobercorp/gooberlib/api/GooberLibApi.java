package com.goobercorp.gooberlib.api;

import com.goobercorp.gooberlib.GooberLibEntrypoint;
import com.goobercorp.gooberlib.api.widgets.WidgetProviders;
import com.goobercorp.gooberlib.builder.BuiltConfig;
import com.goobercorp.gooberlib.builder.misc.Metadata;
import com.goobercorp.gooberlib.builder.category.ConfigCategory;
import com.goobercorp.gooberlib.builder.section.ConfigSection;
import com.goobercorp.gooberlib.gui.EvilButtonWidget;
import com.goobercorp.gooberlib.option.Option;
import com.goobercorp.gooberlib.option.OptionContext;
import com.goobercorp.gooberlib.builder.misc.OptionHolder;
import com.goobercorp.gooberlib.option.individual.java.ColorOption;
import com.goobercorp.gooberlib.option.individual.java.StringOption;
import com.goobercorp.gooberlib.option.individual.minecraft.IdentifierOption;
import com.goobercorp.gooberlib.option.individual.misc.ButtonOption;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;
import com.goobercorp.gooberlib.gui.ColorPickerWidget;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.individual.primitive.CharOption;
import com.goobercorp.gooberlib.option.individual.primitive.NumberOption;
import com.goobercorp.gooberlib.screen.GooberScreen;
import com.goobercorp.gooberlib.util.ConfigDiscovery;
import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.JsonOps;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;
import oshi.util.tuples.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.io.function.Erase.rethrow;

// glappy
public class GooberLibApi {
	/// Saves all registered GooberLib configs
	@SuppressWarnings("unused")
	public static void saveAll() {
		ConfigDiscovery.getConfigs().forEach(GooberLibApi::save);
	}

	/// Loads all registered GooberLib configs
	public static void loadAll() {
		ConfigDiscovery.getConfigs().forEach(GooberLibApi::load);
	}

	private static Path getPath(String modId, BuiltConfig config) {
		return FabricLoader.getInstance().getConfigDir().resolve(modId).resolve(config.title().getString());
	}

	/// Loads a BuiltConfig from disk
	public static void load(String modId, BuiltConfig config) {
		Path saveFilePath = getPath(modId, config).resolve("config.json");
		if (!Files.exists(saveFilePath)) {
			save(modId, config);
		}

		JsonObject theObject;
		try {
			theObject = JsonParser.parseString(Files.readString(saveFilePath)).getAsJsonObject();
		} catch (IOException e) {
			throw rethrow(e);
		}

		for (ConfigCategory category : config.categories()) {
			if (category.metadata().name() == null)
				throw new IllegalStateException("Please provide a name for your category! " + category);
			var objectName = category.metadata().name().getString();
			if (!theObject.has(objectName)) continue;
			JsonObject object = theObject.get(objectName).getAsJsonObject();
			for (var entry : object.asMap().entrySet()) {
				for (OptionHolder o : category.elements()) {
					if (o instanceof OptionContext<?> optionContext) {
						if (optionContext.option().name().getString().equals(entry.getKey())) {
							deserializeOption(optionContext, entry.getValue().getAsJsonObject());
						}
					} else if (o instanceof ConfigSection(
							Metadata metadata, List<OptionContext<?>> childOptions
					)) {
						if (metadata.name().getString().equals(entry.getKey())) {
							JsonObject sectionObject = object.get(metadata.name().getString()).getAsJsonObject();
							for (var sectionEntry : sectionObject.asMap().entrySet()) {
								for (OptionContext<?> context : childOptions) {
									if (context.option().name().getString().equals(sectionEntry.getKey())) {
										try {
											deserializeOption(context, sectionEntry.getValue().getAsJsonObject());
										} catch (Exception e) {
											GooberLibEntrypoint.LOGGER.error("Couldn't deserialize option {}", context.option().name().getString(), e);
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/// Saves a BuiltConfig to disk
	public static void save(String modId, BuiltConfig config) {
		JsonObject theObject = new JsonObject();
		for (ConfigCategory category : config.categories()) {
			JsonObject object = new JsonObject();
			for (OptionHolder o : category.elements()) {
				if (o instanceof OptionContext<?> option) {
					serializeOption(option, object);
				} else if (o instanceof ConfigSection(
						Metadata metadata,
						List<OptionContext<?>> options
				)) {
					JsonObject sectionObject = new JsonObject();
					for (OptionContext<?> optionContext : options) {
						serializeOption(optionContext, sectionObject);
					}
					String name = metadata.name().getString();
					object.add(name, sectionObject);
				}
			}
			theObject.add(category.metadata().name().getString(), object);
		}

		Path saveFilePath = getPath(modId, config);
		try {
			if (Files.notExists(saveFilePath))
				Files.createDirectories(saveFilePath);
			if (Files.notExists(saveFilePath.resolve("config.json")))
				Files.createFile(saveFilePath.resolve("config.json"));

			StringBuilder stringBuilder = new StringBuilder();
			JsonWriter jsonWriter = new JsonWriter(Streams.writerForAppendable(stringBuilder));
			jsonWriter.setStrictness(Strictness.LENIENT);
			jsonWriter.setFormattingStyle(FormattingStyle.PRETTY);
			Streams.write(theObject, jsonWriter);
			String str = stringBuilder.toString();

			Files.writeString(saveFilePath.resolve("config.json"), str);
		} catch (IOException e) {
			GooberLibEntrypoint.LOGGER.error("Couldn't save config for {}", config.title().getString(), e);
			throw rethrow(e);
		}
	}

	private static void deserializeOption(OptionContext<?> option, JsonObject jo) {
		if (jo == null) return;
		JsonElement valueObject = jo.get("value");
		JsonObject childrenObject = jo.get("children").getAsJsonObject();

		option.option().deserialize(JsonOps.INSTANCE, valueObject);
		for (OptionContext<?> childOptionHolder : option.childOptions()) {
			deserializeOption(childOptionHolder, childrenObject.getAsJsonObject(childOptionHolder.option().name().getString()));
		}
	}

	private static void serializeOption(OptionContext<?> option, JsonObject out) {
		String optionName = option.option().name().getString();
		JsonElement value = option.option().serialize(JsonOps.INSTANCE);

		JsonObject jo = new JsonObject();
		jo.add("value", value);

		JsonObject childrenObject = new JsonObject();
		for (OptionContext<?> childOption : option.childOptions()) {
			serializeOption(childOption, childrenObject);
		}
		jo.add("children", childrenObject);

		out.add(optionName, jo);
	}

	private static final List<Pair<Class<? extends Option<?>>, WidgetProvider<?>>> widgetProviders = new ArrayList<>();

	static {
		registerWidgetProvider(CharOption.class, WidgetProviders.numberField());
		//noinspection unchecked
		registerWidgetProvider(NumberOption.class, WidgetProviders.numberSlider());
		registerWidgetProvider(ColorOption.class, ColorPickerWidget::new);
		registerWidgetProvider(BooleanOption.class, WidgetProviders.booleanTickBox());
		registerWidgetProvider(ButtonOption.class, EvilButtonWidget::new);
		registerWidgetProvider(StringOption.class, WidgetProviders.stringField());
		registerWidgetProvider(IdentifierOption.class, WidgetProviders.identifierTwoFields());
	}

	/**
	 * Registers or replaces the default {@link WidgetProvider} for an option class. This provider gets used when no widget provider was supplier upon creating an instance of the option class
	 *
	 * @param optionClass    the option class
	 * @param widgetProvider the default widget provider to use
	 */
	public static <T extends Option<T>> void registerWidgetProvider(Class<T> optionClass, WidgetProvider<T> widgetProvider) {
		widgetProviders.add(new Pair<>(optionClass, widgetProvider));
	}

	/**
	 * Returns the default {@link WidgetProvider} for the option class
	 *
	 * @param optionClass the option class
	 * @return the default {@link WidgetProvider} for the option class
	 */
//	 * @throws IllegalArgumentException if no default widget provider for the given option class was registered
	public static <T extends Option<T>> WidgetProvider<T> getDefaultWidgetProvider(Class<T> optionClass) {
		for (Pair<Class<? extends Option<?>>, WidgetProvider<?>> widgetProvider : widgetProviders) {
			if (widgetProvider.getA().isAssignableFrom(optionClass)) {
				//noinspection unchecked
				return (WidgetProvider<T>) widgetProvider.getB();
			}
		}
		return (theOption, x, y, width, height) -> new TextWidget(x, y, width, height, theOption.name(), MinecraftClient.getInstance().textRenderer);
//		throw new IllegalArgumentException("No default widget provider for " + optionClass);
	}

	/**
	 * Returns a GooberScreen instance
	 *
	 * @param modId  the mod id of the config that should be displayed
	 * @param parent the parent that should be used
	 * @return a GooberScreen instance
	 */
	public static GooberScreen getScreenFor(String modId, Screen parent) {
		BuiltConfig config = getConfigFor(modId);
		return config.getScreen(parent, modId);
	}

	private static BuiltConfig getConfigFor(String modId) {
		return ConfigDiscovery.getConfigs().get(modId);
	}
}
