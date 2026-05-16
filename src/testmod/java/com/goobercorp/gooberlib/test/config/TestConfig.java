package com.goobercorp.gooberlib.test.config;

import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.builder.v2.GooberConfigBuilder;
import com.goobercorp.gooberlib.builder.v3.individual.primitive.IntOption;
import net.minecraft.text.Text;

@SuppressWarnings("unused")
@GooberConfig(modId = "testmod")
public class TestConfig {
	// @formatter:off

    public static final IntOption int1 = new IntOption("Standalone field", "meow");
    public static final IntOption int2 = new IntOption("standalone child", "");
    public static final IntOption int3 = new IntOption("second standalone child", "");
    public static final IntOption int4 = new IntOption("nested child", "");
    public static final IntOption int5 = new IntOption("Int field in a section", "");
    public static final IntOption int6 = new IntOption("int3 name", "");
    public static final IntOption int7 = new IntOption("yeah", "");
    public static final IntOption int8 = new IntOption("yeah", "");
    public static final IntOption hotkey = new IntOption("nya gothkey", "");

//	public static final GooberConfigBuilder BUILDER2 = GooberConfigBuilder.ofCategories("the name of the config", TheOne.category);

    public static final GooberConfigBuilder BUILDER = GooberConfigBuilder.create()
            .title(Text.of("YEAH!!!"))
            .category("Int fields", "A description")
                .option(int1)
                    .child(int2)
                    .nestedChild(int3)
                        .children(int4, int8)
                        .build()
                    .build()
                .section("A section", "description of doom and despair")
                    .option(int5)
                    .children(int6, hotkey)
                    .build()
                .build()
                .section("second section yknow how it be", "oh yeahhh")
                    .option(int7)
                    .build()
                .build()
            .build()
            .category(Text.of("second category"))
                .section(Text.of("second page section"))
                    .description("yah")
                    .build()
                .build()
			.addBuiltCategory(TheOne.category)
		    .addBuiltCategory(Magic.category)
		    .makeBuiltCategory(Magic2.class, "name 2", "description 2");
    // @formatter:on


//	public static Hotkey hotkey = new Hotkey("g, c", 5, () -> IO.println("nya"));
}
