package com.goobercorp.gooberlib.test.config;

import com.goobercorp.gooberlib.builder.ConfigCategory;
import com.goobercorp.gooberlib.misc.Hotkey;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.awt.*;
import java.net.URI;
import java.nio.file.Path;

public class TheOne {
	/* commented out until all the options are implemented
	public static ConfigCategory category = ConfigCategory.builder(TheOne.class)
			.name("meow meow")
			.description("wawaawa")
			.section()
				.name("primitives")
				.description("primitive types")
				.option("booleanOption").name("boolean option").build()
				.option("byteOption").name("byte option").build()
				.option("shortOption").name("short option").build()
				.option("charOption").name("char option").build()
				.option("intOption").name("int option").build()
				.option("longOption").name("long option").build()
				.option("floatOption").name("float option").build()
				.option("doubleOption").name("double option").build()
				.build()
			.section()
				.name("class")
				.description("class types")
				.option("StringOption").name("String option").build()
				.option("ColorOption").name("Color option").build()
				.option("EnumOption").name("Enum option").build()
				.option("FileOption").name("File option").build()
				.option("URLOption").name("URL option").build()
				.build()
			.section()
				.name("minecraft")
				.description("minecraft types")
				.option("IdentifierOption").name("Identifier option").build()
				.option("TextOption").name("Text option").build()
				.build()
			.section()
				.name("gooberlib")
			.description("gooberlib types")
			.option("HotkeyOption").name("Hotkey option").build()
			.build()
			.buildCategory();

	public static boolean booleanOption;
	public static byte byteOption;
	public static short shortOption;
	public static char charOption;
	public static int intOption;
	public static long longOption;
	public static float floatOption;
	public static double doubleOption;
	/*and boxed ones...*/
	/*and arrays...*/
	/*and lists...*/

	/* commented out until all the options are implemented
	public static String StringOption;
	public static Color ColorOption;
	public static SomeEnum EnumOption;
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

	public enum SomeEnum { OPTION_ONE, OPTION_TWO, OPTION_THREE}
	*/
}






















