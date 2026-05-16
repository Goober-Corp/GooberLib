package com.goobercorp.gooberlib.test.config;

import com.goobercorp.gooberlib.annotations.Section;
import com.goobercorp.gooberlib.builder.ConfigCategory;
import com.goobercorp.gooberlib.builder.v3.individual.java.StringOption;
import net.minecraft.world.chunk.Chunk;

public class Magic2 {
	public static final StringOption stringOption = new StringOption("string option 2", "string description2 ");

	@Section("meow meow")
	public static final StringOption secondStringOption = new StringOption("second string option2 ", "second string description2 ");

	@Section("meow meow mrrp")
	public static final Chunk meow = null;

	public static final StringOption thirdStringOption = new StringOption("third string option2 ", "third string description2 ");

	public static ConfigCategory category = ConfigCategory.ofClass(Magic2.class, "magic 2", "magic 2 meow");
}
