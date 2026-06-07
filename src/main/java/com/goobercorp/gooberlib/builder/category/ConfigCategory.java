package com.goobercorp.gooberlib.builder.category;

import com.goobercorp.gooberlib.annotations.Section;
import com.goobercorp.gooberlib.builder.misc.Metadata;
import com.goobercorp.gooberlib.builder.misc.OptionHolder;
import com.goobercorp.gooberlib.builder.section.SectionBuilder;
import com.goobercorp.gooberlib.option.Option;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.function.Consumer;

import static org.apache.commons.io.function.Erase.rethrow;

public record ConfigCategory(Metadata metadata, List<OptionHolder> elements) {
	/**
	 * Makes a new {@link CategoryBuilder}
	 *
	 * @param name        the name of the category
	 * @param description the description of the category
	 * @return the category builder
	 */
	public static CategoryBuilder builder(CharSequence name, CharSequence description) {
		return new CategoryBuilder(null, name, description);
	}

	/**
	 * Makes a new {@link CategoryBuilder}
	 *
	 * @param name the name of the category
	 * @return the category builder
	 */
	public static CategoryBuilder builder(CharSequence name) {
		return builder(name, "");
	}

	/**
	 * Makes a new {@link CategoryBuilder}, lets the {@code consumer} consume it, builds it, and returns it
	 *
	 * @param name        the name of the category
	 * @param description the description of the category
	 * @param consumer    the lambda that consumes the builder
	 * @return the category builder
	 */
	public static ConfigCategory builder(CharSequence name, CharSequence description, Consumer<CategoryBuilder> consumer) {
		CategoryBuilder builder = new CategoryBuilder(null, name, description);
		consumer.accept(builder);
		return builder.buildCategory();
	}

	/**
	 * Makes a new {@link CategoryBuilder}, lets the {@code consumer} consume it, builds it, and returns it
	 *
	 * @param name     the name of the category
	 * @param consumer the lambda that consumes the builder
	 * @return the category builder
	 */
	public static ConfigCategory builder(CharSequence name, Consumer<CategoryBuilder> consumer) {
		return builder(name, "", consumer);
	}

	/**
	 * Reads {@code clazz}'s fields for fields that extend {@link Option} and for @{@link Section} annotations and builds a {@link CategoryBuilder} for that
	 *
	 * @param clazz       the class to read
	 * @param name        the name of the category
	 * @param description the description of the category
	 * @return a {@link CategoryBuilder} with the options of the class
	 */
	public static CategoryBuilder ofClassBuildable(Class<?> clazz, CharSequence name, CharSequence description) {
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

	/**
	 * Reads {@code clazz}'s fields for fields that extend {@link Option} and for @{@link Section} annotations and builds a {@link CategoryBuilder} for that
	 *
	 * @param clazz the class to read
	 * @param name  the name of the category
	 * @return a {@link CategoryBuilder} with the options of the class
	 */
	public static CategoryBuilder ofClassBuildable(Class<?> clazz, CharSequence name) {
		return ofClassBuildable(clazz, name, "");
	}


	/**
	 * Reads {@code clazz}'s fields for fields that extend {@link Option} and for @{@link Section} annotations and makes a {@link ConfigCategory} for that
	 *
	 * @param clazz       the class to read
	 * @param name        the name of the category
	 * @param description the description of the category
	 * @return a {@link ConfigCategory} with the options of the class
	 */
	public static ConfigCategory ofClass(Class<?> clazz, CharSequence name, CharSequence description) {
		return ofClassBuildable(clazz, name, description).buildCategory();
	}

	/**
	 * Reads {@code clazz}'s fields for fields that extend {@link Option} and for @{@link Section} annotations and makes a {@link ConfigCategory} for that
	 *
	 * @param clazz the class to read
	 * @param name  the name of the category
	 * @return a {@link ConfigCategory} with the options of the class
	 */
	public static ConfigCategory ofClass(Class<?> clazz, CharSequence name) {
		return ofClass(clazz, name, "");
	}

	@Override
	public @NonNull String toString() {
		return "ConfigCategory{" +
				"metadata=" + metadata +
				'}';
	}
}
