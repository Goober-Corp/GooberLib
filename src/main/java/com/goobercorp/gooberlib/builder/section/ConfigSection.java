package com.goobercorp.gooberlib.builder.section;

import com.goobercorp.gooberlib.builder.misc.Metadata;
import com.goobercorp.gooberlib.builder.misc.OptionHolder;
import com.goobercorp.gooberlib.option.OptionContext;

import java.util.List;

public record ConfigSection(Metadata metadata, List<OptionContext<?>> childOptions)
		implements OptionHolder {
	/**
	 * Makes a new {@link SectionBuilder}
	 *
	 * @param name        the name of the section
	 * @param description the description of the section
	 * @return the section builder
	 */
	public static SectionBuilder builder(CharSequence name, CharSequence description) {
		return new SectionBuilder(null, name, description);
	}

	/**
	 * Makes a new {@link SectionBuilder}
	 *
	 * @param name the name of the section
	 * @return the section builder
	 */
	public static SectionBuilder builder(CharSequence name) {
		return builder(name, "");
	}
}
