package com.goobercorp.gooberlib.test.config;

import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.builder.GooberConfigBuilder;
import com.goobercorp.gooberlib.builder.section.ConfigSection;
import com.goobercorp.gooberlib.builder.section.SectionBuilder;
import com.goobercorp.gooberlib.option.individual.primitive.IntOption;

@GooberConfig(modId = "parented-sections")
public class ParentedSections {
    public static final GooberConfigBuilder BUILDER = GooberConfigBuilder.create("Parented sections", config -> {
        config.category("Main", cat -> {
           cat.option(new IntOption("Meow"), o -> {
               ConfigSection section = new SectionBuilder(null, "Child section", "Meow")
                       .options(new IntOption("Child section option"))
                       .buildSection();
               o.child(section);
               o.children(section, section);
           });
        });
    });
}
