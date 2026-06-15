package com.goobercorp.gooberlib.test.config.magic;

import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.annotations.Section;
import com.goobercorp.gooberlib.option.individual.primitive.CharOption;

@GooberConfig(modId = "lazy-and-magic-test", lazy = true)
public class TestConfiggg {
	@Section("meow meow")
	public static final CharOption charOption = new CharOption("char option", "char description");

	static {
		IO.println("I'm late!");
	}
}
