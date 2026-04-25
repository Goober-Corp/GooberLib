package com.goobercorp.gooberlib.api;

import com.goobercorp.gooberlib.builder.*;
import com.goobercorp.gooberlib.util.ConfigDiscovery;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

import static org.apache.commons.io.function.Erase.rethrow;

public class GooberLibApi {
	public static void saveAll() {
		ConfigDiscovery.getConfigs().forEach((modId, config) -> {
			save(modId, config);
		});
	}

	public static void loadAll() {
		ConfigDiscovery.getConfigs().forEach((modId, config) -> {
			try {// todo move this around to a more specific place
				load(modId, config);
			} catch (IOException e) {
				rethrow(e);
			}
		});
	}

	private static Path getPath(String modId, BuiltConfig config) {
		return FabricLoader.getInstance().getConfigDir().resolve(modId).resolve(config.title().getString());
	}

	public static void load(String modId, BuiltConfig config) throws IOException {
		Path saveFilePath = getPath(modId, config).resolve("config.json");
		if (!Files.exists(saveFilePath)) {
			save(modId, config);
		}
		JsonObject theObject = JsonParser.parseString(Files.readString(saveFilePath)).getAsJsonObject();

		for (ConfigCategory category : config.categories()) {
			JsonObject object = theObject.get(category.metadata().name().getString()).getAsJsonObject();
			for (Object o : category.elements()) {
				if (o instanceof ConfigOption option) {
					String name = option.metadata().name().getString();
					Type type = option.type();
					JsonElement toLoad = object.get(name);
					Object loaded = config.gson().fromJson(toLoad, type);
					Consumer setter = option.setter();
					setter.accept(loaded);
				} else if (o instanceof ConfigSection section) {
					JsonObject sectionObject = object.get(section.metadata().name().getString()).getAsJsonObject();
					for (ConfigOption option : section.options()) {
						String optionName = option.metadata().name().getString();
						Type type = option.type();
						JsonElement toLoad = sectionObject.get(optionName);
						Object loaded = config.gson().fromJson(toLoad, type);
						Consumer setter = option.setter();
						setter.accept(loaded);
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
				JsonElement toAdd = null;
				String name = null;
				if (o instanceof ConfigOption option) {
					name = option.metadata().name().getString();
					Object toSave = option.getter().get();

					toAdd = config.gson().toJsonTree(toSave, option.type());
				} else if (o instanceof ConfigSection(
						MetadataHolder.Metadata metadata,
						List<ConfigOption> options
				)) {
					JsonObject sectionObject = new JsonObject();
					for (ConfigOption option : options) {
						String optionName = option.metadata().name().getString();
						Object toSave = option.getter().get();

						sectionObject.add(optionName, config.gson().toJsonTree(toSave, option.type()));
					}
					name = metadata.name().getString();
					toAdd = sectionObject;
				}
				object.add(name, toAdd);
			}
			theObject.add(category.metadata().name().getString(), object);
		}

		Path saveFilePath = getPath(modId, config);
		try {
			if (Files.notExists(saveFilePath))
				Files.createDirectories(saveFilePath);
			if (Files.notExists(saveFilePath.resolve("config.json")))
				Files.createFile(saveFilePath.resolve("config.json"));
			Files.writeString(saveFilePath.resolve("config.json"), config.gson().toJson(theObject));
		} catch (IOException e) {
			System.out.println("Couldn't save config for " + config.title().getString() + ": " + e);
			throw rethrow(e);
		}
	}

	static {
//		// primitives
//		registerHandler(byte.class, JsonPrimitive::new, JsonElement::getAsByte);
//		registerHandler(short.class, JsonPrimitive::new, JsonElement::getAsShort);
//		registerHandler(int.class, JsonPrimitive::new, JsonElement::getAsInt);
//		registerHandler(long.class, JsonPrimitive::new, JsonElement::getAsLong);
//		registerHandler(float.class, JsonPrimitive::new, JsonElement::getAsFloat);
//		registerHandler(double.class, JsonPrimitive::new, JsonElement::getAsDouble);
//		registerHandler(boolean.class, JsonPrimitive::new, JsonElement::getAsBoolean);
//		registerHandler(char.class, (c) -> new JsonPrimitive(String.valueOf(c)), (element) -> element.getAsString().charAt(0));
//		// boxed primitives
//		registerHandler(Byte.class, JsonPrimitive::new, JsonElement::getAsByte);
//		registerHandler(Short.class, JsonPrimitive::new, JsonElement::getAsShort);
//		registerHandler(Integer.class, JsonPrimitive::new, JsonElement::getAsInt);
//		registerHandler(Long.class, JsonPrimitive::new, JsonElement::getAsLong);
//		registerHandler(Float.class, JsonPrimitive::new, JsonElement::getAsFloat);
//		registerHandler(Double.class, JsonPrimitive::new, JsonElement::getAsDouble);
//		registerHandler(Boolean.class, JsonPrimitive::new, JsonElement::getAsBoolean);
//		registerHandler(Character.class, (c) -> new JsonPrimitive(String.valueOf(c)), (element) -> element.getAsString().charAt(0));
//		// other
//		registerHandler(String.class, JsonPrimitive::new, JsonElement::getAsString);
//		registerHandler(List.class, list -> {
//			JsonArray array = new JsonArray();
//			for (Object o : list) {
//				Saver s = getSaverFor(o);
//				if (s != null) {
//					JsonObject jsonObject = new JsonObject();
//					jsonObject.add("value", s.save(o));
//					jsonObject.addProperty("clazz", o.getClass().getName());
//					array.add(jsonObject);
//				} else {
//					throw new IllegalStateException("No handler for " + o.getClass());
//				}
//			}
//			return array;
//		}, (element) -> {
//			List list = new ArrayList();
//			try {
//				for (JsonElement inner : element.getAsJsonArray()) {
//					JsonObject jsonObject = inner.getAsJsonObject();
//					JsonElement value = jsonObject.get("value");
//					JsonElement clazz = jsonObject.get("clazz");
//					Class<?> theClass = Class.forName(clazz.getAsString());
//					Loader loader = getLoaderFor(theClass);
//				}
//			} catch (ClassNotFoundException e) {
//				throw new RuntimeException(e);
//			}
//		});
	}
}
