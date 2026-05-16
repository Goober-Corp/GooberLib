package com.goobercorp.gooberlib.test.config;

import com.goobercorp.gooberlib.builder.ConfigCategory;
import com.goobercorp.gooberlib.builder.v3.individual.HotkeyOption;
import com.goobercorp.gooberlib.builder.v3.individual.java.ColorOption;
import com.goobercorp.gooberlib.builder.v3.individual.java.EnumOption;
import com.goobercorp.gooberlib.builder.v3.individual.java.StringOption;
import com.goobercorp.gooberlib.builder.v3.individual.primitive.*;

public class TheOne {
	/* commented out until all the options are implemented
//	public static boolean booleanOption;
//	public static byte byteOption;
//	public static short shortOption;
//	public static char charOption;
//	public static int intOption;
//	public static long longOption;
//	public static float floatOption;
//	public static double doubleOption;
	/*and lists...*/

	/* commented out until all the options are implemented
//	public static String StringOption;
//	public static Color ColorOption;
//	public static SomeEnum EnumOption;
	public static Path FileOption;
	public static URI URLOption;

	public static Identifier IdentifierOption;
	public static Text TextOption;
	public static BlockPos BlockPosOption;
	public static Vec2f Vec2fOption;
	public static Vec3d Vec3dOption;
	public static Vec3i Vec3iOption;
	public static Item ItemOption;
	public static Block BlockOption;

	public static Hotkey HotkeyOption;

	*/

	public static final BooleanOption booleanOption = new BooleanOption("boolean option", "boolean description");
	public static final ByteOption byteOption = new ByteOption("byte option", "byte description");
	public static final ShortOption shortOption = new ShortOption("short option", "short description");
	public static final CharOption charOption = new CharOption("char option", "char description");
	public static final IntOption intOption = new IntOption("int option", "int description");
	public static final LongOption longOption = new LongOption("long option", "long description");
	public static final FloatOption floatOption = new FloatOption("float option", "float description");
	public static final DoubleOption doubleOption = new DoubleOption("double option", "double description");

	// todo: this
//	@Section("meow meow")
	public static final StringOption stringOption = new StringOption("string option", "string description");
	public static final ColorOption colorOption = new ColorOption("color option", "color description");
	public static final EnumOption<SomeEnum> enumOption = new EnumOption<>("enum option", "enum description", SomeEnum.class);

	public static final HotkeyOption hotkeyOption = new HotkeyOption("hotkey option", "hotkey description", "g, c", 2, () -> System.out.println("meow meow"));

	@SuppressWarnings("unused")
	public enum SomeEnum {OPTION_ONE, OPTION_TWO, OPTION_THREE}

	// todo: this
//	public static ConfigCategory category = ConfigCategory.ofClass(TheOne.class, "the one", "the description");

	// @formatter:off
	public static ConfigCategory category = ConfigCategory.builder("the one", "description of meow")
			.section("primitive", "")
				.options(booleanOption, byteOption, shortOption, charOption, intOption, longOption, floatOption, doubleOption)
				.build()
			.section("java", "")
				.options(stringOption, colorOption, enumOption)
				.build()
			.section("goober", "")
				.options(hotkeyOption)
				.option(hotkeyOption).build()
				.build()
			// todo: this
			/*.sectionMaker("my section", "", s -> {
				// s is instance of SectionBuilder
				for (int i = 0; i < 5; i++) {
					s.option(new IntOption("option " + i, "")); // youd have to keep track of them for it to be useful but ok
				}
			})*/
		.buildCategory();
	// @formatter:on
}






















