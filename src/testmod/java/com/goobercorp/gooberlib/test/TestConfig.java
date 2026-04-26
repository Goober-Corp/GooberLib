package com.goobercorp.gooberlib.test;

import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.builder.v2.GooberConfigBuilder;
import net.minecraft.text.Text;

@SuppressWarnings("unused")
@GooberConfig(modId = "testmod")
public class TestConfig {
    // @formatter:off
    public static final GooberConfigBuilder BUILDER = GooberConfigBuilder.create()
            .title(Text.of("YEAH!!!"))
            .category()
                .name("Int fields")
                .descriptionTranslation("A description")
                .option("int1")
                    .name("Standalone field")
                    .withChildren()
                        .child("int2")
                            .name("standalone child")
                            .build()
                        .child("int3")
                            .name("second standalone child")
                            .withChildren()
                                .child("int4")
                                    .name("nested child")
                                    .build()
                                .build()
                            .build()
                        .build()
                    .build()
                .section()
                    .name("A section")
                    .description(Text.of("description of doom and despair"))
                    .option("int5")
                        .name("Int field in a section")
                        .withChildren()
                            .child("int6")
                                .name("int3 name")
                                .build()
                            .build()
                        .build()
                    .build()
                .section(Text.of("second section yknow how it be"))
                    .description("oh yeahhh")
                    .option("int7")
                        .name("yeah")
                        .build()
                    .build()
                .build()
            .category(Text.of("second category"))
                .section(Text.of("second page section"))
                    .description("yah")
                    .build()
                .build();
    // @formatter:on

    public static int int1;
    public static int int2;
    public static int int3;
    public static int int4;
    public static int int5;
    public static int int6;
    public static int int7;

}
