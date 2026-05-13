package com.goobercorp.gooberlib.api;

import com.goobercorp.gooberlib.builder.*;
import com.goobercorp.gooberlib.util.ConfigDiscovery;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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
			if (category.metadata().name() == null) throw new IllegalStateException("Please provide a name for your category! " + category);
			var objectName = category.metadata().name().getString();
			if (!theObject.has(objectName)) continue;
			JsonObject object = theObject.get(objectName).getAsJsonObject();
			for (var entry : object.asMap().entrySet()) {
				for (Object o : category.elements()) {
					if (o instanceof ConfigOption option) {
						if (option.metadata().name().getString().equals(entry.getKey())) {
							deserializeOption(option, entry.getValue().getAsJsonObject(), config.gson());
						}
					} else if (o instanceof ConfigSection(MetadataHolder.Metadata metadata, List<ConfigOption> childOptions)) {
						if (metadata.name().getString().equals(entry.getKey())) {
							JsonObject sectionObject = object.get(metadata.name().getString()).getAsJsonObject();
							for (var sectionEntry : sectionObject.asMap().entrySet()) {
								for (ConfigOption option : childOptions) {
									if (option.metadata().name().getString().equals(sectionEntry.getKey())) {
										deserializeOption(option, sectionEntry.getValue().getAsJsonObject(), config.gson());
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
				if (o instanceof ConfigOption option) {
					serializeOption(option, object, config.gson());
				} else if (o instanceof ConfigSection(
						MetadataHolder.Metadata metadata,
						List<ConfigOption> options
				)) {
					JsonObject sectionObject = new JsonObject();
					for (ConfigOption option : options) {
						serializeOption(option, sectionObject, config.gson());
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
			Files.writeString(saveFilePath.resolve("config.json"), config.gson().toJson(theObject));
		} catch (IOException e) {
			System.out.println("Couldn't save config for " + config.title().getString() + ": " + e);
			throw rethrow(e);
		}
	}

	private static void deserializeOption(ConfigOption option, JsonObject jo, Gson gson) {
		if (jo == null) return;
		JsonElement valueObject = jo.get("value");
		JsonObject childrenObject = jo.get("children").getAsJsonObject();

		option.setter().accept(gson.fromJson(valueObject, option.type()));
		for (ConfigOption childOption : option.childOptions()) {
			deserializeOption(childOption, childrenObject.getAsJsonObject(childOption.metadata().name().getString()), gson);
		}
	}

	private static void serializeOption(ConfigOption option, JsonObject out, Gson gson) {
		String optionName = option.metadata().name().getString();
		Object value = option.getter().get();

		JsonObject jo = new JsonObject();
		jo.add("value", gson.toJsonTree(value, option.type()));

		JsonObject childrenObject = new JsonObject();
		for (ConfigOption childOption : option.childOptions()) {
			serializeOption(childOption, childrenObject, gson);
		}
		jo.add("children", childrenObject);

		out.add(optionName, jo);
	}

//	static {
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
//	}
}
