package com.goobercorp.gooberlib.test.config;

import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.builder.GooberConfigBuilder;
import com.goobercorp.gooberlib.option.individual.misc.ButtonOption;
import com.goobercorp.gooberlib.option.individual.primitive.IntOption;
import net.minecraft.text.Text;

@SuppressWarnings("unused")
@GooberConfig(modId = "testmod")
public class TestConfig {

	public static final IntOption int1 = new IntOption(Text.of("Standalone field"), _ -> Text.of("meow"), 0, 0, 25, null);
	public static final IntOption int2 = new IntOption("standalone child", "");
	public static final IntOption int3 = new IntOption("second standalone child", "");
	public static final IntOption int4 = new IntOption("nested child", "");
	public static final IntOption int5 = new IntOption("Int field in a section", "");
	public static final IntOption int6 = new IntOption("int3 name", "");
	public static final IntOption int7 = new IntOption("yeah", "");
	public static final IntOption int8 = new IntOption("yeah", "");
	public static final IntOption hotkey = new IntOption("nya gothkey", "");
	public static final ButtonOption evilButtonOpt = new ButtonOption("yeah", "yeah", () -> System.out.println("YEAH!!!"));

	// @formatter:off
    public static final GooberConfigBuilder BUILDER = GooberConfigBuilder.create("YEAH!!!")
            .category("Int fields", "A description")
                .option(int1)
                    .child(int2)
				    .childWithChildren(int3, int4, int8)
                    .build()
                .section("A section", "description of doom and despair")
			        .optionWithChildren(int5, int6, hotkey)
                    .build()
                .section("second section yknow how it be")
                    .options(int7, evilButtonOpt)
                    .build()
                .build()
            .category("second category")
                .section("second page section", "yah")
                    .build()
                .build()
			.addBuiltCategory(TheOne.category)
		    .addBuiltCategory(Magic.category)
		    .makeBuiltCategory(Magic2.class, "name 2", "description 2");
    // @formatter:on
}
