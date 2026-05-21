package com.goobercorp.gooberlib.builder.category;

import com.goobercorp.gooberlib.annotations.Section;
import com.goobercorp.gooberlib.builder.misc.Metadata;
import com.goobercorp.gooberlib.builder.misc.OptionHolder;
import com.goobercorp.gooberlib.builder.section.SectionBuilder;
import com.goobercorp.gooberlib.option.Option;
import net.minecraft.text.Text;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.apache.commons.io.function.Erase.rethrow;

public record ConfigCategory(Metadata metadata, List<OptionHolder> elements) {
	public static CategoryBuilder builder(Text name, Text description) {
		return new CategoryBuilder(null, name, description);
	}

	public static CategoryBuilder builder(String name, String description) {
		return builder(Text.of(name), Text.of(description));
	}

	public static CategoryBuilder builder(String name) {
		return builder(Text.of(name), Text.empty());
	}

	public static CategoryBuilder builder(Text name) {
		return builder(name, Text.empty());
	}

	public static CategoryBuilder ofClassBuildable(Class<?> clazz, Text name, Text description) {
		CategoryBuilder builder = builder(name, description);
		SectionBuilder sectionBuilder = null;
		for (Field f : clazz.getDeclaredFields()) {
			if (f.isAnnotationPresent(Section.class)) {
				Section section = f.getAnnotation(Section.class);
				if (sectionBuilder != null) sectionBuilder.build();
				sectionBuilder = builder.section(section.value(), section.sectionDescription());
			}
			int mods = f.getModifiers();
			if (Modifier.isPublic(mods) && Modifier.isStatic(mods) && Modifier.isFinal(mods) && Option.class.isAssignableFrom(f.getType())) {
				try {
					if (sectionBuilder != null) {
						sectionBuilder.option((Option<?>) f.get(null));
					} else {
						builder.option((Option<?>) f.get(null));
					}
				} catch (IllegalAccessException e) {
					throw rethrow(e);
				}
			}
		}
		if (sectionBuilder != null) sectionBuilder.build();
		return builder;
	}

	@Override
	public @NonNull String toString() {
		return "ConfigCategory{" +
				"metadata=" + metadata +
				'}';
	}

	public static CategoryBuilder ofClassBuildable(Class<?> clazz, String name, String description) {
		return ofClassBuildable(clazz, Text.literal(name), Text.literal(description));
	}

	public static CategoryBuilder ofClassBuildable(Class<?> clazz, String name) {
		return ofClassBuildable(clazz, Text.literal(name), Text.empty());
	}

	public static ConfigCategory ofClass(Class<?> clazz, Text name, Text description) {
		return ofClassBuildable(clazz, name, description).buildCategory();
	}

	public static ConfigCategory ofClass(Class<?> clazz, String name, String description) {
		return ofClass(clazz, Text.of(name), Text.of(description));
	}

	public static ConfigCategory ofClass(Class<?> clazz, String name) {
		return ofClass(clazz, Text.of(name), Text.empty());
	}
}
