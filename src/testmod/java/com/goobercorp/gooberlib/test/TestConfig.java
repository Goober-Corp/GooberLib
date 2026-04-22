package com.goobercorp.gooberlib.test;

import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.builder.v2.GooberConfigBuilder;
import net.minecraft.text.Text;

@SuppressWarnings("unused")
@GooberConfig(modId = "testmod")
public class TestConfig {
    public static final GooberConfigBuilder BUILDER = GooberConfigBuilder.create()
            .title(Text.of("YEAH!!!"))
            .category()
                .name("Int fields")
                .descriptionTranslation("A description")
                        .option("int1")
                        .name("Standalone field")
                        .build()
                    .section()
                    .name("A section")
                    .description(Text.of("description of doom and despair"))
                        .option("int2")
                        .name("Int field in a section")
                            .withChildren()
                                .child("int3")
                                .name("int3 name")
                                .build()
                            .build()
                        .build()
                    .build()
                    .section(Text.of("second section yknow how it be"))
                    .build()
                .build()
            .category(Text.of("second category"))
                .section(Text.of("second page section"))
                .build()
            .build();

    public static int int1;
    public static int int2;
    public static int int3;
}
