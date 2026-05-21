package com.goobercorp.gooberlib.api;

import com.goobercorp.gooberlib.GooberLibEntrypoint;
import com.goobercorp.gooberlib.builder.BuiltConfig;
import com.goobercorp.gooberlib.builder.ConfigCategory;
import com.goobercorp.gooberlib.builder.ConfigSection;
import com.goobercorp.gooberlib.builder.MetadataHolder;
import com.goobercorp.gooberlib.builder.v3.Option;
import com.goobercorp.gooberlib.builder.v3.OptionContext;
import com.goobercorp.gooberlib.builder.v3.individual.java.ColorOption;
import com.goobercorp.gooberlib.builder.v3.individual.primitive.BooleanOption;
import com.goobercorp.gooberlib.builder.v3.individual.primitive.IntOption;
import com.goobercorp.gooberlib.gui.ColorPickerWidget;
import com.goobercorp.gooberlib.gui.EvilSliderWidget;
import com.goobercorp.gooberlib.gui.TickBoxWidget;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.screen.GooberScreen;
import com.goobercorp.gooberlib.util.ConfigDiscovery;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextWidget;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.io.function.Erase.rethrow;

public class GooberLibApi {
	public static void saveAll() {
		ConfigDiscovery.getConfigs().forEach(GooberLibApi::save);
	}

	public static void loadAll() {
		ConfigDiscovery.getConfigs().forEach(GooberLibApi::load);
	}

	private static Path getPath(String modId, BuiltConfig config) {
		return FabricLoader.getInstance().getConfigDir().resolve(modId).resolve(config.title().getString());
	}

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
				for (Object o : category.elements()) {
					if (o instanceof OptionContext<?> optionContext) {
						if (optionContext.option().name().getString().equals(entry.getKey())) {
							deserializeOption(optionContext, entry.getValue().getAsJsonObject());
						}
					} else if (o instanceof ConfigSection(
							MetadataHolder.Metadata metadata, List<OptionContext<?>> childOptions
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

	public static void save(String modId, BuiltConfig config) {
		JsonObject theObject = new JsonObject();
		for (ConfigCategory category : config.categories()) {
			JsonObject object = new JsonObject();
			for (Object o : category.elements()) {
				if (o instanceof OptionContext<?> option) {
					serializeOption(option, object);
				} else if (o instanceof ConfigSection(
						MetadataHolder.Metadata metadata,
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
			Files.writeString(saveFilePath.resolve("config.json"), theObject.toString());
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

	private static final Map<Class<? extends Option<?>>, WidgetProvider> widgetProviders = new HashMap<>();

	static {
		registerWidgetProvider(IntOption.class, (theOption, x, y, width, height) -> new EvilSliderWidget(theOption, x, y, (int) width, (int) height));
		registerWidgetProvider(ColorOption.class, (theOption, x, y, width, height) -> new ColorPickerWidget(theOption, x, y, (int) width, (int) height));
		registerWidgetProvider(BooleanOption.class, (theOption, x, y, width, height) -> new TickBoxWidget(x, y, (int) width, (int) height, (BooleanOption) theOption));
	}

	public static void registerWidgetProvider(Class<? extends Option<?>> optionClass, WidgetProvider widgetProvider) {
		widgetProviders.put(optionClass, widgetProvider);
	}

	public static WidgetProvider getDefaultWidgetProvider(Class<? extends Option<?>> optionClass) {
		if (widgetProviders.containsKey(optionClass)) {
			return widgetProviders.get(optionClass);
		}
		return (theOption, x, y, width, height) -> new TextWidget(x, y, (int) width, (int) height, theOption.name(), MinecraftClient.getInstance().textRenderer);
//		throw new IllegalArgumentException("No default widget provider for " + optionClass);
	}

	public static GooberScreen getScreenFor(String modId, Screen parent) {
		BuiltConfig config = getConfigFor(modId);
		return config.getScreen(parent, modId);
	}

	private static BuiltConfig getConfigFor(String modId) {
		return ConfigDiscovery.getConfigs().get(modId);
	}
}
