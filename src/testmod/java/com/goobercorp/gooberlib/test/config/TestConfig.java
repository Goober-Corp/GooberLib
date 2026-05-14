package com.goobercorp.gooberlib.test.config;

import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.builder.v2.GooberConfigBuilder;
import com.goobercorp.gooberlib.builder.v3.SimpleIntOption;
import com.goobercorp.gooberlib.misc.Hotkey;
import net.minecraft.text.Text;

@SuppressWarnings("unused")
@GooberConfig(modId = "testmod")
public class TestConfig {
    // @formatter:off

    public static final SimpleIntOption int1 = new SimpleIntOption("Standalone field", "meow");
    public static final SimpleIntOption int2 = new SimpleIntOption("standalone child", "");
    public static final SimpleIntOption int3 = new SimpleIntOption("second standalone child", "");
    public static final SimpleIntOption int4 = new SimpleIntOption("nested child", "");
    public static final SimpleIntOption int5 = new SimpleIntOption("Int field in a section", "");
    public static final SimpleIntOption int6 = new SimpleIntOption("int3 name", "");
    public static final SimpleIntOption int7 = new SimpleIntOption("yeah", "");
    public static final SimpleIntOption int8 = new SimpleIntOption("yeah", "");
    public static final SimpleIntOption hotkey = new SimpleIntOption("nya gothkey", "");

    public static final GooberConfigBuilder BUILDER = GooberConfigBuilder.create()
            .title(Text.of("YEAH!!!"))
            .category("Int fields", "A description")
                .option(int1)
                    .child(int2)
                    .editChild(int3)
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
            .category("THIRD ONE !!", "")
            .build()
            .category("FOURTH ONE !!", "")
            .build()
            .category("FIFTH ONE !!", "")
            .build();
//			.addBuiltCategory(TheOne.category);
    // @formatter:on


//	public static Hotkey hotkey = new Hotkey("g, c", 5, () -> IO.println("nya"));
}
