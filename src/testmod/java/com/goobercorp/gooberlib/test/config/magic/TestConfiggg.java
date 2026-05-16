package com.goobercorp.gooberlib.test.config.magic;

import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.annotations.Section;
import com.goobercorp.gooberlib.builder.v3.individual.primitive.CharOption;

@GooberConfig(modId = "gooberlib")
public class TestConfiggg {
	@Section("meow meow")
	public static final CharOption charOption = new CharOption("char option", "char description");
}
