package com.goobercorp.gooberlib.builder;

import com.goobercorp.gooberlib.annotations.Section;
import com.goobercorp.gooberlib.builder.v2.CategoryBuilder;
import com.goobercorp.gooberlib.builder.v2.SectionBuilder;
import com.goobercorp.gooberlib.builder.v3.Option;
import com.goobercorp.gooberlib.builder.v3.OptionHolderV3;
import net.minecraft.text.Text;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.apache.commons.io.function.Erase.rethrow;

public record ConfigCategory(MetadataHolder.Metadata metadata, List<OptionHolderV3> elements) {
	public static CategoryBuilder builder() {
		return new CategoryBuilder(null);
	}

	public static CategoryBuilder builder(String name, String description) {
		var categoryBuilder = new CategoryBuilder(null);
		categoryBuilder.name(name);
		categoryBuilder.description(description);
		return categoryBuilder;
	}

	public static CategoryBuilder ofClassBuildable(Class<?> clazz, Text name, Text description) {
		CategoryBuilder builder = builder();
		builder.name(name);
		builder.description(description);
		SectionBuilder sectionBuilder = null;
		for (Field f : clazz.getDeclaredFields()) {
			if (f.isAnnotationPresent(Section.class)) {
				Section section = f.getAnnotation(Section.class);
				if (sectionBuilder != null) sectionBuilder.build();
				sectionBuilder = builder.section(section.value(), section.sectionDescription());
			}
			int mods = f.getModifiers();
			if (Modifier.isPublic(mods) && Modifier.isStatic(mods) && Modifier.isFinal(mods) && Option.class.isAssignableFrom(f.getType())) {
				if (sectionBuilder != null) {
					try {
						sectionBuilder.option((Option<?>) f.get(null));
					} catch (IllegalAccessException e) {
						throw rethrow(e);
					}
				} else {
					try {
						builder.option((Option<?>) f.get(null));
					} catch (IllegalAccessException e) {
						throw rethrow(e);
					}
				}
			}
		}
		if (sectionBuilder != null) sectionBuilder.build();
		return builder;
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
