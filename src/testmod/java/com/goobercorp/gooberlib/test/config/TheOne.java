package com.goobercorp.gooberlib.test.config;

import com.goobercorp.gooberlib.builder.ConfigCategory;
import com.goobercorp.gooberlib.builder.v3.individual.HotkeyOption;
import com.goobercorp.gooberlib.builder.v3.individual.java.ColorOption;
import com.goobercorp.gooberlib.builder.v3.individual.java.CycleOption;
import com.goobercorp.gooberlib.builder.v3.individual.java.EnumOption;
import com.goobercorp.gooberlib.builder.v3.individual.java.StringOption;
import com.goobercorp.gooberlib.builder.v3.individual.minecraft.IdentifierOption;
import com.goobercorp.gooberlib.builder.v3.individual.primitive.*;
import net.minecraft.util.Identifier;


// todo?: tests fpr all options
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

//	public static Identifier IdentifierOption;
	public static Text TextOption;
	public static BlockPos BlockPosOption;
	public static Vec2f Vec2fOption;
	public static Vec3d Vec3dOption;
	public static Vec3i Vec3iOption;
	public static Item ItemOption;
	public static Block BlockOption;

//	public static HotkeyOption HotkeyOption;

	*/

	public static final BooleanOption booleanOption = new BooleanOption("boolean option", "boolean description");
	public static final ByteOption byteOption = new ByteOption("byte option", "byte description");
	public static final ShortOption shortOption = new ShortOption("short option", "short description");
	public static final CharOption charOption = new CharOption("char option", "char description");
	public static final IntOption intOption = new IntOption("int option", "int description");
	public static final LongOption longOption = new LongOption("long option", "long description");
	public static final FloatOption floatOption = new FloatOption("float option", "float description");
	public static final DoubleOption doubleOption = new DoubleOption("double option", "double description");

	public static final StringOption stringOption = new StringOption("string option", "string description");
	public static final ColorOption colorOption = new ColorOption("color option", "color description");
	public static final EnumOption<SomeEnum> enumOption = new EnumOption<>("enum option", "enum description", SomeEnum.class);
	public static final CycleOption<String> cycleOption = new CycleOption<>("cycle option", "cycle description", "Option one", "Option two", "Option three");

	public static final IdentifierOption identifierOption = new IdentifierOption("identifier option", "identifier description", Identifier.of("minecraft:stone"));

	public static final HotkeyOption hotkeyOption = new HotkeyOption("hotkey option", "hotkey description", "g, c", 2, () -> System.out.println("meow meow"));

	@SuppressWarnings("unused")
	public enum SomeEnum {OPTION_ONE, OPTION_TWO, OPTION_THREE}

	public static final IntOption testOption = new IntOption("test option", "test description");
	public static final IntOption testChildOption = new IntOption("test child option", "test child description");
	// @formatter:off
	public static ConfigCategory category = ConfigCategory.builder("the one", "description of meow")
			.section("primitive", "")
				.options(booleanOption, byteOption, shortOption, charOption, intOption, longOption, floatOption, doubleOption)
				.build()
			.section("java", "")
				.options(stringOption, colorOption, enumOption, cycleOption)
				.build()
			.section("minecraft", "")
				.options(identifierOption)
				.build()
			.section("goober", "")
				.options(hotkeyOption, hotkeyOption, hotkeyOption, hotkeyOption)
				.build()
			.section("childOptionTest section", "")
				.optionMaker(testOption, o -> o.child(testChildOption))
				.build()
			.sectionMaker("my section", "", s -> {
				for (int i = 0; i < 5; i++) {
					int finalI = i;
					s.optionMaker(new IntOption("option " + i, ""), o -> o.child(new DoubleOption("child option " + finalI, "")));
				}
			})
		.buildCategory();
	// @formatter:on
}
