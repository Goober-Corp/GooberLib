package com.goobercorp.gooberlib;

import com.goobercorp.gooberlib.builder.GooberConfigBuilder;
import net.minecraft.text.Text;

@GooberConfig(modId = "gooberlib")
public class TestConfig {

    private static final GooberConfigBuilder BUILDER = GooberConfigBuilder.create()
            .title(Text.of("YEAH!!!"))
            .category()
            .name("Int fields")
            .descriptionPlain("A description")
                    .field("int1")
                    .name("Standalone field")
                    .build()
                .section()
                .name("A section")
                    .field("int2")
                    .name("Int field in a section")
                    .build()

                    .field("int3")
                    .name("int3 name")
                    .build()
                .build()
            .build();

    public static int int1;
    public static int int2;
    public static int int3;

    @GooberBuilderAccessor
    public static GooberConfigBuilder getBuilder() {
        return BUILDER;
    }

}
