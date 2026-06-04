package com.goobercorp.gooberlib.test.config;

import com.goobercorp.gooberlib.annotations.Section;
import com.goobercorp.gooberlib.builder.category.ConfigCategory;
import com.goobercorp.gooberlib.option.individual.java.StringOption;
import net.minecraft.world.level.chunk.ChunkAccess;

public class Magic {
	public static final StringOption stringOption = new StringOption("string option", "string description");

	@Section("meow meow")
	public static final StringOption secondStringOption = new StringOption("second string option", "second string description");

	@Section("meow meow mrrp")
	public static final ChunkAccess meow = null;
	public static final StringOption thirdStringOption = new StringOption("third string option", "third string description");

	public static final ConfigCategory category = ConfigCategory.ofClass(Magic.class, "magic 1", "magic 1 meow");
}
