package com.goobercorp.gooberlib.builder.section;

import com.goobercorp.gooberlib.builder.misc.Metadata;
import com.goobercorp.gooberlib.builder.misc.OptionHolder;
import com.goobercorp.gooberlib.option.OptionContext;
import java.util.List;
import net.minecraft.network.chat.Component;

public record ConfigSection(Metadata metadata, List<OptionContext<?>> childOptions)
		implements OptionHolder {
	/**
	 * Makes a new {@link SectionBuilder}
	 *
	 * @param name        the name of the section
	 * @param description the description of the section
	 * @return the section builder
	 */
	public static SectionBuilder builder(Component name, Component description) {
		return new SectionBuilder(null, name, description);
	}

	/**
	 * Makes a new {@link SectionBuilder}
	 *
	 * @param name        the name of the section
	 * @param description the description of the section
	 * @return the section builder
	 */
	public static SectionBuilder builder(String name, String description) {
		return builder(Component.nullToEmpty(name), Component.nullToEmpty(description));
	}

	/**
	 * Makes a new {@link SectionBuilder}
	 *
	 * @param name the name of the section
	 * @return the section builder
	 */
	public static SectionBuilder builder(Component name) {
		return builder(name, Component.empty());
	}

	/**
	 * Makes a new {@link SectionBuilder}
	 *
	 * @param name the name of the section
	 * @return the section builder
	 */
	public static SectionBuilder builder(String name) {
		return builder(Component.nullToEmpty(name), Component.empty());
	}
}
