package com.goobercorp.gooberlib.test;

import com.goobercorp.gooberlib.GooberConfig;
import com.goobercorp.gooberlib.builder.GooberConfigBuilder;
import net.minecraft.text.Text;

@SuppressWarnings("unused")
@GooberConfig(modId = "gooberlib")
public class TestConfig {
    public static final GooberConfigBuilder BUILDER = GooberConfigBuilder.create()
            .title(Text.of("YEAH!!!"))
            .category()
            .name("Int fields")
            .descriptionTranslation("A description")
                    .field("int1")
                    .name("Standalone field")
                    .build()
                .section()
                .name("A section")
            .description(Text.of("description of doom and despair"))
                    .field("int2")
                    .name("Int field in a section")
                    .withChildren()
                        .child("int3")
                        .name("int3 name")
                        .build()

                    .build()
                .build()
            .build();

    public static int int1;
    public static int int2;
    public static int int3;
}
