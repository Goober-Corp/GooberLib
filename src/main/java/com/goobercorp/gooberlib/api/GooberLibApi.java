package com.goobercorp.gooberlib.api;

import com.goobercorp.gooberlib.GooberLibEntrypoint;
import com.goobercorp.gooberlib.api.widgets.WidgetProviders;
import com.goobercorp.gooberlib.builder.BuiltConfig;
import com.goobercorp.gooberlib.builder.category.ConfigCategory;
import com.goobercorp.gooberlib.builder.misc.Metadata;
import com.goobercorp.gooberlib.builder.misc.OptionHolder;
import com.goobercorp.gooberlib.builder.section.ConfigSection;
import com.goobercorp.gooberlib.gui.option.ColorPickerWidget;
import com.goobercorp.gooberlib.gui.option.EvilButtonWidget;
import com.goobercorp.gooberlib.interfaces.DefaultHandler;
import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.Option;
import com.goobercorp.gooberlib.option.OptionContext;
import com.goobercorp.gooberlib.option.individual.hotkey.HotkeyOption;
import com.goobercorp.gooberlib.option.individual.java.ColorOption;
import com.goobercorp.gooberlib.option.individual.java.CycleOption;
import com.goobercorp.gooberlib.option.individual.java.StringOption;
import com.goobercorp.gooberlib.option.individual.minecraft.*;
import com.goobercorp.gooberlib.option.individual.misc.ButtonOption;
import com.goobercorp.gooberlib.option.individual.misc.LabelOption;
import com.goobercorp.gooberlib.option.individual.misc.ObjectOption;
import com.goobercorp.gooberlib.option.individual.primitive.BooleanOption;
import com.goobercorp.gooberlib.option.individual.primitive.CharOption;
import com.goobercorp.gooberlib.option.individual.primitive.NumberOption;
import com.goobercorp.gooberlib.option.individual.primitive.range.NumberRangeOption;
import com.goobercorp.gooberlib.screen.GooberScreen;
import com.goobercorp.gooberlib.util.ConfigDiscovery;
import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

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

	private static final List<Pair<String, DefaultHandler>> defaultHandlers = new ArrayList<>();

	public static void registerDefaultHandler(String name, DefaultHandler handler) {
		defaultHandlers.add(new Pair<>(name, handler));
	}

	// todo: figure out when to call this
	public static void resetDefaultValues() {
		for (Pair<String, DefaultHandler> handler : defaultHandlers) {
			try {
				handler.getSecond().reset();
			} catch (Throwable t) {
				GooberLibEntrypoint.LOGGER.error("Handler {} failed: ", handler.getFirst(), t);
				throw t;
			}
		}
	}

	static {
		registerDefaultHandler("primitives", () -> {
			Defaults.booleanDefault = false;
			Defaults.byteDefault = (byte) 0;
			Defaults.shortDefault = (short) 0;
			Defaults.charDefault = (char) 0;
			Defaults.intDefault = 0;
			Defaults.longDefault = 0;
			Defaults.floatDefault = 0;
			Defaults.doubleDefault = 0;

			Defaults.byteDefaultMin = Byte.MIN_VALUE;
			Defaults.shortDefaultMin = Short.MIN_VALUE;
			Defaults.charDefaultMin = Character.MIN_VALUE;
			Defaults.intDefaultMin = Integer.MIN_VALUE;
			Defaults.longDefaultMin = Long.MIN_VALUE;
			Defaults.floatDefaultMin = -Float.MAX_VALUE;
			Defaults.doubleDefaultMin = -Double.MIN_VALUE;

			Defaults.byteDefaultMax = Byte.MAX_VALUE;
			Defaults.shortDefaultMax = Short.MAX_VALUE;
			Defaults.charDefaultMax = Character.MAX_VALUE;
			Defaults.intDefaultMax = Integer.MAX_VALUE;
			Defaults.longDefaultMax = Long.MAX_VALUE;
			Defaults.floatDefaultMax = Float.MAX_VALUE;
			Defaults.doubleDefaultMax = Double.MAX_VALUE;
		});
		registerDefaultHandler("primitive_range", () -> {
			Defaults.byteRangeDefaultMinValue = (byte) -1;
			Defaults.shortRangeDefaultMinValue = (short) -1;
			Defaults.charRangeDefaultMinValue = (char) 0;
			Defaults.intRangeDefaultMinValue = -1;
			Defaults.longRangeDefaultMinValue = -1;
			Defaults.floatRangeDefaultMinValue = -1;
			Defaults.doubleRangeDefaultMinValue = -1;

			Defaults.byteRangeDefaultMaxValue = (byte) 1;
			Defaults.shortRangeDefaultMaxValue = (short) 1;
			Defaults.charRangeDefaultMaxValue = (char) 1;
			Defaults.intRangeDefaultMaxValue = 1;
			Defaults.longRangeDefaultMaxValue = 1;
			Defaults.floatRangeDefaultMaxValue = 1;
			Defaults.doubleRangeDefaultMaxValue = 1;

			Defaults.byteRangeDefaultMin = Byte.MIN_VALUE;
			Defaults.shortRangeDefaultMin = Short.MIN_VALUE;
			Defaults.charRangeDefaultMin = Character.MIN_VALUE;
			Defaults.intRangeDefaultMin = Integer.MIN_VALUE;
			Defaults.longRangeDefaultMin = Long.MIN_VALUE;
			Defaults.floatRangeDefaultMin = -Float.MAX_VALUE;
			Defaults.doubleRangeDefaultMin = -Float.MAX_VALUE;

			Defaults.byteRangeDefaultMax = Byte.MAX_VALUE;
			Defaults.shortRangeDefaultMax = Short.MAX_VALUE;
			Defaults.charRangeDefaultMax = Character.MAX_VALUE;
			Defaults.intRangeDefaultMax = Integer.MAX_VALUE;
			Defaults.longRangeDefaultMax = Long.MAX_VALUE;
			Defaults.floatRangeDefaultMax = Float.MAX_VALUE;
			Defaults.doubleRangeDefaultMax = Double.MAX_VALUE;
		});
		registerDefaultHandler("misc", () -> {
			Defaults.identifierDefault = Identifier.parse("minecraft:stone");
			Defaults.colorDefault = 0xFF000000;
		});
	}

	private record WidgetHandler<T extends Option<T>>(Function<T, Boolean> checkCanHandle, int priority,
	                                                  WidgetProvider<T> widgetProvider) {
		boolean genericCanHandle(Object o) {
			try {
				//noinspection unchecked
				Boolean result = checkCanHandle.apply((T) o);
				return result != null && result;
			} catch (ClassCastException _) {
				return false;
			}
		}
	}

	private static final SortedSet<WidgetHandler<?>> widgetProviders = new TreeSet<>(Comparator.<WidgetHandler<?>>comparingInt(o -> o.priority).thenComparing((_, _) -> -1));

	static {
		// noinspection unchecked, rawtypes
		GooberLibApi.<NumberOption>registerWidgetProvider(t -> t instanceof NumberOption, WidgetProviders.numberSlider(), -2);
		// noinspection unchecked, rawtypes
		GooberLibApi.<NumberRangeOption>registerWidgetProvider(t -> t instanceof NumberRangeOption, WidgetProviders.rangeOption(), -2);
		GooberLibApi.<CharOption>registerWidgetProvider(t -> t instanceof CharOption, WidgetProviders.numberField(), -1);
		GooberLibApi.<ButtonOption>registerWidgetProvider(t -> t instanceof ButtonOption, EvilButtonWidget::new, -1);
		registerWidgetProvider(t -> t instanceof ColorOption, ColorPickerWidget::new, -1);
		registerWidgetProvider(t -> t instanceof BooleanOption, WidgetProviders.booleanTickBox(), -1);
		registerWidgetProvider(t -> t instanceof StringOption, WidgetProviders.stringField(), -1);
		registerWidgetProvider(t -> t instanceof IdentifierOption, WidgetProviders.identifierTwoFields(), -1);
		registerWidgetProvider(t -> t instanceof BlockPosOption, WidgetProviders.blockPosFields(), -1);
		registerWidgetProvider(t -> t instanceof Vec3iOption, WidgetProviders.vec3iFields(), -1);
		registerWidgetProvider(t -> t instanceof Vec3dOption, WidgetProviders.vec3dFields(), -1);
		registerWidgetProvider(t -> t instanceof Vec2fOption, WidgetProviders.vec2fFields(), -1);
		registerWidgetProvider(t -> t instanceof CycleOption, WidgetProviders.cyclingOption(), -1);
		registerWidgetProvider(t -> t instanceof ObjectOption<?>, WidgetProviders.objectOption(), -1);
		registerWidgetProvider(t -> t instanceof HotkeyOption, WidgetProviders.hotkey(), -1);
		registerWidgetProvider(t -> t instanceof LabelOption, WidgetProviders.label(), -1);
	}

	/**
	 * Registers a default {@link WidgetProvider} for an option with the default priority. This provider gets used when no widget provider was supplier upon creating an instance of the option class
	 * <p>
	 *
	 * @param widgetProvider the widget provider
	 * @param checkCanHandle the function to check if given widget provider can handle a given option
	 * @param <T>            the option type
	 */
	public static <T extends Option<T>> void registerWidgetProvider(Function<T, Boolean> checkCanHandle, WidgetProvider<T> widgetProvider) {
		registerWidgetProvider(checkCanHandle, widgetProvider, 1000);
	}

	/**
	 * Registers a default {@link WidgetProvider} for an option. This provider gets used when no widget provider was supplier upon creating an instance of the option class
	 * <p>
	 *
	 * @param widgetProvider the widget provider
	 * @param checkCanHandle the function to check if given widget provider can handle a given option
	 * @param priority       priority of the widget provider (the one with the highest priority will be picked)
	 * @param <T>            the option type
	 */
	public static <T extends Option<T>> void registerWidgetProvider(Function<T, Boolean> checkCanHandle, WidgetProvider<T> widgetProvider, int priority) {
		widgetProviders.add(new WidgetHandler<>(checkCanHandle, priority, widgetProvider));
	}

	/**
	 * Returns the default {@link WidgetProvider} for the option instance
	 *
	 * @param option the option instance
	 * @param <T>    the option type
	 * @return the default {@link WidgetProvider} for the option instance
	 */
	public static <T extends Option<T>> WidgetProvider<T> getDefaultWidgetProvider(T option) {
		WidgetHandler<T> bestSoFar = null;
		for (WidgetHandler<?> handler : widgetProviders) {
			if (handler.genericCanHandle(option)) {
				if (bestSoFar == null || bestSoFar.priority < handler.priority) {
					//noinspection unchecked
					bestSoFar = (WidgetHandler<T>) handler;
				}
			}
		}
		if (bestSoFar != null) {
			return bestSoFar.widgetProvider();
		}
		GooberLibEntrypoint.LOGGER.warn("Option class {} does not have a widget provider! Proceed with care.", option.getClass());
		return (theOption, x, y, width, height) -> new StringWidget(x, y, width, height, theOption.name(), Minecraft.getInstance().font);
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

	public static class Defaults {
		public static boolean booleanDefault;

		public static byte byteDefault;
		public static char charDefault;
		public static double doubleDefault;
		public static float floatDefault;
		public static int intDefault;
		public static long longDefault;
		public static short shortDefault;

		public static byte byteDefaultMin;
		public static char charDefaultMin;
		public static double doubleDefaultMin;
		public static float floatDefaultMin;
		public static int intDefaultMin;
		public static long longDefaultMin;
		public static short shortDefaultMin;

		public static byte byteDefaultMax;
		public static char charDefaultMax;
		public static double doubleDefaultMax;
		public static float floatDefaultMax;
		public static int intDefaultMax;
		public static long longDefaultMax;
		public static short shortDefaultMax;

		public static byte byteRangeDefaultMinValue;
		public static char charRangeDefaultMinValue;
		public static double doubleRangeDefaultMinValue;
		public static float floatRangeDefaultMinValue;
		public static int intRangeDefaultMinValue;
		public static long longRangeDefaultMinValue;
		public static short shortRangeDefaultMinValue;

		public static byte byteRangeDefaultMaxValue;
		public static char charRangeDefaultMaxValue;
		public static double doubleRangeDefaultMaxValue;
		public static float floatRangeDefaultMaxValue;
		public static int intRangeDefaultMaxValue;
		public static long longRangeDefaultMaxValue;
		public static short shortRangeDefaultMaxValue;

		public static byte byteRangeDefaultMin;
		public static char charRangeDefaultMin;
		public static double doubleRangeDefaultMin;
		public static float floatRangeDefaultMin;
		public static int intRangeDefaultMin;
		public static long longRangeDefaultMin;
		public static short shortRangeDefaultMin;

		public static byte byteRangeDefaultMax;
		public static char charRangeDefaultMax;
		public static double doubleRangeDefaultMax;
		public static float floatRangeDefaultMax;
		public static int intRangeDefaultMax;
		public static long longRangeDefaultMax;
		public static short shortRangeDefaultMax;

		public static Identifier identifierDefault;
		public static int colorDefault;
	}
}
