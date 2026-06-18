package com.goobercorp.gooberlib.test.config.others.mapi;

import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.api.GooberLibApi;
import com.goobercorp.gooberlib.builder.GooberConfigBuilder;
import com.goobercorp.gooberlib.builder.category.CategoryBuilder;
import com.goobercorp.gooberlib.builder.category.ConfigCategory;
import com.goobercorp.gooberlib.option.individual.hotkey.HotkeyOption;
import com.goobercorp.gooberlib.option.individual.java.ColorOption;
import com.goobercorp.gooberlib.option.individual.java.CycleOption;
import com.goobercorp.gooberlib.option.individual.java.StringOption;
import com.goobercorp.gooberlib.option.individual.misc.ButtonOption;
import com.goobercorp.gooberlib.option.individual.misc.LabelOption;
import com.goobercorp.gooberlib.option.individual.misc.ListOption;
import com.goobercorp.gooberlib.option.individual.misc.ObjectOption;
import com.goobercorp.gooberlib.option.individual.primitive.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;

import java.util.List;
import java.util.function.Consumer;

@GooberConfig(modId = "mapi-test")
public class Mapi {
	public static class ConfigObjectTest {
		static class BlockPosConfig {
			public final IntOption x = new IntOption("x");
			public final IntOption y = new IntOption("y");
			public final IntOption z = new IntOption("z");
			public final LabelOption label = new LabelOption("Hi");

			// todo:
			/*
			@Extras
			void add(List<IConfigBase> existing) {
				existing.add(new ConfigLabel("Hi"));
			}*/

			public BlockPos get() {
				return new BlockPos(x.getValue(), y.getValue(), z.getValue());
			}

			@Override
			public String toString() {
				return get().toString();
			}
		}

		static final ListOption<ObjectOption<BlockPosConfig>> BLOCKS = new ListOption<>("Block positions", List.of(), () -> new ObjectOption<>("", new BlockPosConfig(), "Edit block pos"));
		public static final ListOption<StringOption> STRING_LIST_PLUS = new ListOption<>("Test", List.of(), () -> new StringOption(""));
		public static final StringOption STRING_PLUS = new StringOption("S");

		static final HotkeyOption PRINT_KEY = new HotkeyOption("Print blocks", "", "", 5, () -> {
			StringBuilder s = new StringBuilder();

			for (ObjectOption<BlockPosConfig> config : BLOCKS.getValue()) {
				BlockPos pos = config.getInstance().get();
				s.append(pos.toString()).append(", \n");
			}

			System.out.println(s.substring(0, s.length() - 3));
		});

		public static final ConfigCategory CATEGORY = ConfigCategory.ofClass(ConfigObjectTest.class, "ConfigObjectTest");
	}

	public static class CustomConfigTest {
		public static final ConfigClass TEST_CUSTOM = new ConfigClass("Test custom", "This is a test for a custom config class");

		public static final ConfigCategory CATEGORY = ConfigCategory.ofClass(CustomConfigTest.class, "CustomConfigTest");
	}

	public static class MalilibConfigTest {
		//		@Label("MaLiLib API")
		public static final LabelOption LABEL_1 = new LabelOption("MaLiLib API");
		public static final ButtonOption TEST_BUTTON = new ButtonOption("button", "button", () -> System.out.println("Hi!"));

		//	public static final ConfigDropdown<String> TEST_DROPDOWN = new ConfigDropdown<>("dropdown", ImmutableList.of("Wawa", "Wawa 2", "Wawa 3", "Wawa 4", "5", "6", "seven", "eight", "9 (nine)"), "Wawa", "", "dropdown", "dropdown");
		public static class ObjectThing {
			public final StringOption STRING = new StringOption("String");
		}

		public static final ObjectOption<ObjectThing> TEST_OBJECT = new ObjectOption<>("object", new ObjectThing(), "");
		public static final BooleanOption TEST_BOOLEAN_PLUS = new BooleanOption("boolean_plus").setOnValueChange(b -> System.out.println(b.getValue()));

		// todo?:
//		public static final ConfigBooleanHotkeyedPlus TEST_BOOLEAN_HOTKEYED_PLUS = new ConfigBooleanHotkeyedPlus("boolean_hotkeyed_plus").setChangeCallbackCBH(b -> System.out.println("Is now " + b.getBooleanValue() + " and " + b.getKeybind().getStringValue()));
		public static final ByteOption TEST_BYTE = new ByteOption("byte");
		public static final CharOption TEST_CHAR = new CharOption("char");
		public static final FloatOption TEST_FLOAT = new FloatOption("float");
		public static final ShortOption TEST_SHORT = new ShortOption("short");

		//		@Label
//		@Label("MaLiLib")
		public static final LabelOption LABEL_2 = new LabelOption("");
		public static final LabelOption LABEL_3 = new LabelOption("MaLiLib");
		public static final BooleanOption TEST_BOOLEAN = new BooleanOption("boolean", "", true);
		//		public static final ConfigBooleanHotkeyed TEST_BOOLEAN_HOTKEYED = new ConfigBooleanHotkeyed("boolean_hotkeyed", true, "", "");
		public static final ColorOption TEST_COLOR = new ColorOption("color");

		//		public static final ConfigColorList TEST_COLOR_LIST = new ConfigColorList("color_list", ImmutableList.of(), "");
		public static final DoubleOption TEST_DOUBLE = new DoubleOption("double", "", 0);
		public static final HotkeyOption TEST_HOTKEY = new HotkeyOption("hotkey", "", "", 5, () -> {
		});
		public static final IntOption TEST_INTEGER = new IntOption("integer", "", 0);
		public static final CycleOption<String> TEST_OPTION_LIST = new CycleOption<>("option_list", "", str -> str, "", "awaw", "wawaw");
		public static final StringOption TEST_STRING = new StringOption("string", "", "");
		public static final ListOption<StringOption> TEST_STRING_LIST = new ListOption<>("string_list", List.of(), () -> new StringOption(""));
//		public static final ConfigStringList TEST_STRING_LIST = new ConfigStringList("string_list", ImmutableList.of(), "");

		public static final ConfigCategory CATEGORY = ConfigCategory.ofClass(MalilibConfigTest.class, "MalilibConfigTest");
	}

	public static class Silly {
		public static final ConfigCategory CATEGORY = ConfigCategory.builder("MAPI Test", cat -> {
			Consumer<CategoryBuilder> wawa1 = cat1 -> cat1.options(new LabelOption("meow :3"));
			Consumer<CategoryBuilder> wawa2 = cat1 -> cat1.options(new LabelOption("meow 1"));
			Consumer<CategoryBuilder> wawa3 = cat1 -> cat1.options(new LabelOption("meow 2"));
			Consumer<CategoryBuilder> wawa4 = cat1 -> cat1.options(new LabelOption("meow I am at 2 places"));

			cat.option(new LabelOption("Label 1"));
			wawa2.accept(cat);
			wawa3.accept(cat);
			cat.option(new LabelOption("Label 2"));
			wawa3.accept(cat);
			cat.option(new LabelOption("Label :3"));
			wawa1.accept(cat);
			cat.option(new LabelOption(""));
			wawa4.accept(cat);
			cat.option(new LabelOption("wawawa mrppp :33"));
			wawa4.accept(cat);
		});
	}

	public static class Test {
		public static final CategoryBuilder BUILDER = new CategoryBuilder(null, "Test", "");

		public static final StringOption TEST = new StringOption("Hiii");

		static {
			BUILDER.options(TEST);
		}
//		public static final ConfigStringPlus TEST = new ConfigStringPlus("Hiii");

		static final HotkeyOption HOTKEY = new HotkeyOption("Open gui", "", "G,    C", 5, () -> {
			Minecraft.getInstance().setScreen(GooberLibApi.getScreenFor("mapi-test", Minecraft.getInstance().screen));
		});

		static {
			BUILDER.options(HOTKEY);
		}
//		static final ConfigHotkeyPlus HOTKEY = new ConfigHotkeyPlus("Open gui", "G,    C").setPressCallback((keyAction, iKeybind) -> {
//			MalilibApi.openScreenFor(MOD_ID);
//			return true;
//		});

		//		@Hide todo
//		public static final ConfigStringPlus INVISIBLE = new ConfigStringPlus("I'm invisible!");
		public static final StringOption INVISIBLE = new StringOption("I'm invisible!");

		static {
			BUILDER.options(INVISIBLE);
		}

		static {
			for (int i = 0; i < 4; i++) {
				ListOption<StringOption> list = new ListOption<>("I'm also invisible!  " + i, List.of(), () -> new StringOption(""));
				BUILDER.options(INVISIBLE);
//				InternalMalilibApi.addHide(list); // todo
			}
		}

//		@Extras
//		private static void moreCustom(List<IConfigBase> existing) {
//			for (int i = 0; i < 4; i++) {
//				ConfigStringListPlus list = new ConfigStringListPlus("I'm also invisible!  " + i);
//				existing.add(list);
//				InternalMalilibApi.addHide(list); // this makes it invisible
//			}
//		}

		//		@Label("Label above class")
//		@PopupConfig
		public static class Popup {
			ListOption<StringOption> list1 = new ListOption<>("String list " + 1, List.of(), () -> new StringOption(""));
			ListOption<StringOption> list2 = new ListOption<>("String list " + 2, List.of(), () -> new StringOption(""));
			ListOption<StringOption> list3 = new ListOption<>("String list " + 3, List.of(), () -> new StringOption(""));
			ListOption<StringOption> list4 = new ListOption<>("String list " + 4, List.of(), () -> new StringOption(""));
//			@Extras
//			private static void moreCustom(List<IConfigBase> existing) {
//				for (int i = 0; i < 4; i++) {
//					existing.add(new ConfigStringListPlus("String list " + i));
//				}
//			}
		}

		static {
			BUILDER.options(new LabelOption("Label above class"), new ObjectOption<>("Popup", new Popup(), ""));
		}

//		@Label("Label above method")
//		@Extras
//		private static void addCustom(List<IConfigBase> existing) {
//			existing.add(new ConfigBooleanPlus("Programmatically add new ones"));
//			for (int i = 0; i < 6; i++) {
//				existing.add(new ConfigLabel("" + i));
//			}
//		}

		static {
			BUILDER.options(new LabelOption("Label above method"));

			BUILDER.option(new BooleanOption("Programmatically add new ones"));
			for (int i = 0; i < 6; i++) {
				BUILDER.option(new LabelOption("" + i));
			}
		}

		public static final ListOption<StringOption> STRING = new ListOption<>("String", List.of(), () -> new StringOption(""));

		//		@Label("Labels and markers mixed in")
//		@Marker("Marker name")
//		@Label("Another label sandwiching the marker")
		public static final IntOption INT = new IntOption("Integer");

		static {
			BUILDER.options(STRING);
			BUILDER.options(new LabelOption("Labels and markers mixed in"), new StringOption("In between the labels"), new LabelOption("Another label sandwiching the marker"));
			BUILDER.options(INT);

			BUILDER.option(new BooleanOption("Programmatically add new ones"));
			for (int i = 0; i < 6; i++) {
				BUILDER.option(new LabelOption("" + i));
			}
		}

		public static final ConfigCategory CATEGORY = BUILDER.buildCategory();

//		@Extras(runAt = "Marker name")
//		private static void addMoreCustom(List<IConfigBase> existing) {
//			existing.add(new ConfigStringPlus("In between the labels"));
//		}
	}

	public static final GooberConfigBuilder BUILDER = GooberConfigBuilder.create("MaLiLib API Test", config -> {
		config.addBuiltCategory(ConfigObjectTest.CATEGORY);
		config.addBuiltCategory(CustomConfigTest.CATEGORY);
		config.addBuiltCategory(MalilibConfigTest.CATEGORY);
		config.addBuiltCategory(Silly.CATEGORY);
		config.addBuiltCategory(Test.CATEGORY);
	});
}
