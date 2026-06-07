package com.goobercorp.gooberlib.test.config;

import com.goobercorp.gooberlib.annotations.Section;
import com.goobercorp.gooberlib.api.widgets.WidgetProviders;
import com.goobercorp.gooberlib.builder.GooberConfigBuilder;
import com.goobercorp.gooberlib.builder.category.ConfigCategory;
import com.goobercorp.gooberlib.builder.section.ConfigSection;
import com.goobercorp.gooberlib.option.individual.hotkey.HotkeyOption;
import com.goobercorp.gooberlib.option.individual.java.ColorOption;
import com.goobercorp.gooberlib.option.individual.java.CycleOption;
import com.goobercorp.gooberlib.option.individual.java.EnumOption;
import com.goobercorp.gooberlib.option.individual.java.StringOption;
import com.goobercorp.gooberlib.option.individual.minecraft.*;
import com.goobercorp.gooberlib.option.individual.misc.ListOption;
import com.goobercorp.gooberlib.option.individual.misc.ObjectOption;
import com.goobercorp.gooberlib.option.individual.primitive.*;
import com.goobercorp.gooberlib.option.individual.primitive.range.*;
import net.minecraft.resources.Identifier;
import org.joml.Vector4i;

import java.util.List;
import java.util.function.Supplier;


// todo?: tests fpr all options
@SuppressWarnings("unused")
public class TheOne {
	/* commented out until all the options are implemented
	loosely ordered in difficulty

//	public static boolean booleanOption;
//	public static HotkeyOption HotkeyOption;
//	public static String StringOption;
	public static URI URLOption;
	public static Path FileOption;
//	public static SomeEnum EnumOption;
//	public static Identifier IdentifierOption;

//	public static byte byteOption;
//	public static short shortOption;
//	public static int intOption;
//	public static long longOption;
//	public static float floatOption;
//	public static double doubleOption;

//	public static char charOption;
//	public static BlockPos BlockPosOption;
//	public static Vec2f Vec2fOption;
//	public static Vec3d Vec3dOption;
//	public static Vec3i Vec3iOption;


	public static Item ItemOption;
	public static Block BlockOption;
	and lists...
	and objects...
	public static Component ComponentOption;
//	public static Color ColorOption;
	 */
	@Section("fancy ass booleans")
	public static final BooleanOption booleanOption = new BooleanOption("boolean option", "boolean description");
	public static final BooleanOption secondBooleanOption = new BooleanOption("coolean option", "boolean description", WidgetProviders.booleanSliderWidget());
	public static final BooleanOption thirdBooleanOption = new BooleanOption("droolean option", "boolean description", WidgetProviders.booleanToggleWidget());
	public static final BooleanOption fourthBooleanOption = new BooleanOption("ghoulean option", "boolean description", WidgetProviders.booleanToggleWidget("Yes", "No"));

	@Section("field test")
	public static final StringOption stringOption = new StringOption("string option", "string description");
	public static final ByteOption byteOption = new ByteOption("byte option field", "byte description", WidgetProviders.numberField());
	public static final ShortOption shortOption = new ShortOption("short option field", "short description", WidgetProviders.numberField());
	public static final IntOption intOption = new IntOption("int option field", "int description", WidgetProviders.numberField());
	public static final LongOption longOption = new LongOption("long option field", "long description", WidgetProviders.numberField());
	public static final FloatOption floatOption = new FloatOption("float option field", "float description", WidgetProviders.numberField());
	public static final DoubleOption doubleOption = new DoubleOption("double option field", "double description", WidgetProviders.numberField());
	public static final CharOption charOption = new CharOption("char option field", "char description", WidgetProviders.numberField());
//removed because it felt redundant when we have the value formatted ones
//	@Section("slider test")
//	public static final ByteOption byteOptionSlider = new ByteOption("byte option slider", "byte description", WidgetProviders.numberSlider());
//	public static final ShortOption shortOptionSlider = new ShortOption("short option slider", "short description", WidgetProviders.numberSlider());
//	public static final IntOption intOptionSlider = new IntOption("int option slider", "int description", WidgetProviders.numberSlider());
//	public static final LongOption longOptionSlider = new LongOption("long option slider", "long description", WidgetProviders.numberSlider());
//	public static final FloatOption floatOptionSlider = new FloatOption("float option slider", "float description", WidgetProviders.numberSlider());
//	public static final DoubleOption doubleOptionSlider = new DoubleOption("double option slider", "double description", WidgetProviders.numberSlider());
//	public static final CharOption charOptionSlider = new CharOption("char option slider", "char description", WidgetProviders.numberSlider());

	@Section("slider test w/ value formatter")
	public static final ByteOption byteOptionSliderFormatter = new ByteOption("byte option slider", "byte description", WidgetProviders.numberSliderWithFormatter(t -> t.name().copy().append("!!! with: " + t.getValue())));
	public static final ShortOption shortOptionSliderFormatter = new ShortOption("short option slider", "short description", WidgetProviders.numberSliderWithFormatter(t -> t.name().copy().append("!!! with: " + t.getValue())));
	public static final IntOption intOptionSliderFormatter = new IntOption("int option slider", "int description", WidgetProviders.numberSliderWithFormatter(t -> t.name().copy().append("!!! with: " + t.getValue())));
	public static final LongOption longOptionSliderFormatter = new LongOption("long option slider", "long description", WidgetProviders.numberSliderWithFormatter(t -> t.name().copy().append("!!! with: " + t.getValue())));
	public static final FloatOption floatOptionSliderFormatter = new FloatOption("float option slider", "float description", WidgetProviders.numberSliderWithFormatter(t -> t.name().copy().append("!!! with: " + t.getValue())));
	public static final DoubleOption doubleOptionSliderFormatter = new DoubleOption("double option slider", "double description", WidgetProviders.numberSliderWithFormatter(t -> t.name().copy().append("!!! with: " + t.getValue())));
	public static final CharOption charOptionSliderFormatter = new CharOption("char option slider", "char description", WidgetProviders.numberSliderWithFormatter(t -> t.name().copy().append("!!! with: " + t.getValue())));

	@Section("misc options")
	public static final ColorOption colorOption = new ColorOption("color option", "color description");
	public static final ColorOption colorOptionString = new ColorOption("string color option", "color description", WidgetProviders.colorField());
	public static final EnumOption<SomeEnum> enumOption = new EnumOption<>("enum option", "enum description", SomeEnum.class);
	public static final CycleOption<String> cycleOption = new CycleOption<>("cycle option", "cycle description", "Option one", "Option two", "Option three");
	public static final IdentifierOption identifierOptionOne = new IdentifierOption("single string identifier", "identifier description", Identifier.parse("minecraft:stone"), WidgetProviders.identifierOneField());
	public static final IdentifierOption identifierOptionTwo = new IdentifierOption("dual string identifier", "identifier description", Identifier.parse("minecraft:stone"), WidgetProviders.identifierTwoFields());
	public static final BlockPosOption blockPosOption = new BlockPosOption("blockpos", "blockpos description");
	public static final Vec2fOption vec2fOption = new Vec2fOption("vec2f option", "vec2f description");
	public static final Vec3dOption vec3dOption = new Vec3dOption("vec3d option", "vec3d description");
	public static final Vec3iOption vec3iOption = new Vec3iOption("vec3i option", "vec3i description");
	public static final HotkeyOption hotkeyOption = new HotkeyOption("hotkey option", "hotkey description", "g, c", 2, () -> System.out.println("meow meow"));

	@Section("range options")
	public static final ByteRangeOption byteRangeOption = new ByteRangeOption("byte range option", _ -> "byte range description", (byte) 2, (byte) 4, (byte) 0, (byte) 10, WidgetProviders.rangeOption());
	public static final ShortRangeOption shortRangeOption = new ShortRangeOption("short range option", _ -> "short range description", (short) 2, (short) 4, (short) 0, (short) 10, WidgetProviders.rangeOption());
	public static final IntRangeOption intRangeOption = new IntRangeOption("int range option", _ -> "int range description", 2, 4, 0, 10, WidgetProviders.rangeOption());
	public static final LongRangeOption longRangeOption = new LongRangeOption("long range option", _ -> "long range description", 2, 4, 0, 10, WidgetProviders.rangeOption());
	public static final FloatRangeOption floatRangeOption = new FloatRangeOption("float range option", _ -> "float range description", 2, 4, 0, 10, WidgetProviders.rangeOption());
	public static final DoubleRangeOption doubleRangeOption = new DoubleRangeOption("double range option", _ -> "double range description", 2, 4, 0, 10, WidgetProviders.rangeOption());

	public static class InstanceMeow {
		public final StringOption s = new StringOption("Wow!", "");
	}

	@Section("object")
	public static final ObjectOption<InstanceMeow> o = new ObjectOption<>("instance meow object", new InstanceMeow(), "");
	private static final Supplier<ObjectOption<InstanceMeow>> supplier = () -> new ObjectOption<>("instance meow", new InstanceMeow(), "");

	public static final ListOption<ObjectOption<InstanceMeow>> list = new ListOption<>("list object", List.of(supplier.get(), supplier.get()), supplier);
	public static final Vec4iOption vec4iOption = new Vec4iOption("vec4i option", "");

	public static class Vec4iOption extends ObjectOption<Vec4iOption.Vec4iModel> {
		public Vec4iOption(String name, String description) {
			super(name, new Vec4iModel(), _ -> description, null); // TOdo: pretend it has a custom widget provider
		}

		public Vector4i getValue() {
			return new Vector4i(getInstance().x.value, getInstance().y.value, getInstance().z.value, getInstance().w.value);
		}

		public void setValue(Vector4i pos) {
			getInstance().x.setValue(pos.x);
			getInstance().y.setValue(pos.y);
			getInstance().z.setValue(pos.z);
			getInstance().w.setValue(pos.w);
		}

		public static class Vec4iModel {
			public final IntOption x = new IntOption("x", "");
			public final IntOption y = new IntOption("y", "");
			public final IntOption z = new IntOption("z", "");
			public final IntOption w = new IntOption("w", "");
		}
	}

	@SuppressWarnings("unused")
	public enum SomeEnum {OPTION_ONE, OPTION_TWO, OPTION_THREE}

	public static final IntOption testOption = new IntOption("test option", "test description");
	public static final IntOption testChildOption = new IntOption("test child option", "test child description");

	public static final GooberConfigBuilder config = GooberConfigBuilder.create("test", config ->
			config.category("category name", category -> {
				category.option(longOption, o ->
						o.child(colorOption)
				);

				category.section("section", section -> {
					section.option(booleanOption, o ->
							o.child(intOption)
					);
					section.options(booleanOption, shortOption);
				});
			})
	);

	public static final ConfigSection testSection = ConfigSection.builder("test section", "this tests if meow meow").options(new IntOption("meow", "mrp")).buildSection();

	// @formatter:off
	public static final ConfigCategory category = ConfigCategory.ofClassBuildable(TheOne.class, "the one", "description of meow")
			.section("childOptionTest section", "")
				.option(testOption, o -> o.child(testChildOption))
				.build()
			.section("my section", "", s -> {
				for (int i = 0; i < 5; i++) {
					int finalI = i;
					s.option(new IntOption("option " + i, ""), o -> o.child(new DoubleOption("child option " + finalI, "")));
				}
			})
			.options(new IntOption("meoww", o -> "meow: " + o.getValue(), 0, 0, 1000, null))
			.addBuiltSection(testSection)
		.buildCategory();
	// @formatter:on

	static {
		IO.println("I'm early!");
	}
}
