package com.goobercorp.gooberlib.util;

import com.goobercorp.gooberlib.GooberLibEntrypoint;
import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.api.GooberLibApi;
import com.goobercorp.gooberlib.builder.BuiltConfig;
import com.goobercorp.gooberlib.builder.GooberConfigBuilder;
import com.google.common.reflect.ClassPath;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Supplier;

import static org.apache.commons.lang3.function.Failable.rethrow;

public class ConfigDiscovery {
	private static final String[] BLACKLISTED_PACKAGES = {
			"org.objectweb", "net.minecraft", "com.sun", "it.unimi",
			"org.lwjgl", "org.slf4j", "com.mojang", "net.fabricmc",
			"com.google", "com.jcraft", "org.apache", "org.joml",
			"io.netty", "com.ibm", "com.llamalad7.mixinextras",
			"oshi", "org.spongepowered", "java"
	};


	public static Map<String, BuiltConfig> discover(boolean late) throws IOException {
		final Map<String, BuiltConfig> flattened = new HashMap<>();

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		GooberLibApi.resetDefaultValues();

		ClassPath.from(classLoader)
				.getAllClasses()
				.forEach(classInfo -> {
					try {
						String className = classInfo.getName();
						if (ClassInfo.isMixin(className)) return;
						for (String blacklistedPackage : BLACKLISTED_PACKAGES)
							if (classInfo.getPackageName().startsWith(blacklistedPackage)) return;
						if (classInfo.getPackageName().isEmpty()) return;
						if (classInfo.getName().endsWith("module-info")) return;
						if (classInfo.getPackageName().contains("META-INF")) return;

						Class<?> configClass = Class.forName(className, false, classLoader);

						if (configClass.isAnnotationPresent(GooberConfig.class)) {
							GooberConfig gooberConfig = configClass.getAnnotation(GooberConfig.class);
							if (gooberConfig.lazy() != late) return;
							String modId = gooberConfig.modId();
							if (flattened.containsKey(modId)) {
								throw new IllegalStateException("Multiple config classes found for the same mod id: %s".formatted(modId));
							}

							List<Field> builderFields = Arrays.stream(configClass.getDeclaredFields())
									.filter(f -> f.getType() == GooberConfigBuilder.class)
									.filter(f -> Modifier.isPublic(f.getModifiers()))
									.filter(f -> Modifier.isStatic(f.getModifiers()))
									.filter(f -> Modifier.isFinal(f.getModifiers()))
									.toList();

							List<Field> builderSupplierFields = Arrays.stream(configClass.getDeclaredFields())
									.filter(f -> f.getType() == Supplier.class)
									.filter(f -> f.getGenericType().getTypeName().equals("java.util.function.Supplier<com.goobercorp.gooberlib.builder.GooberConfigBuilder>"))
									.filter(f -> Modifier.isPublic(f.getModifiers()))
									.filter(f -> Modifier.isStatic(f.getModifiers()))
									.filter(f -> Modifier.isFinal(f.getModifiers()))
									.toList();

							GooberConfigBuilder gooberConfigBuilder;
							if (!builderFields.isEmpty()) {
								if (builderFields.size() > 1)
									throw new IllegalStateException("Please only have one builder field");

								Field builderField = builderFields.getFirst();
								gooberConfigBuilder = (GooberConfigBuilder) builderField.get(null);
							} else if (!builderSupplierFields.isEmpty()) {
								if (builderSupplierFields.size() > 1)
									throw new IllegalStateException("Please only have one builder field");

								Field builderField = builderSupplierFields.getFirst();
								//noinspection unchecked
								gooberConfigBuilder = ((Supplier<GooberConfigBuilder>) builderField.get(null)).get();
							} else {
								// switch to magic
								List<Class<?>> classesToMagic = new ArrayList<>(List.of(gooberConfig.additionalClasses()));
								classesToMagic.add(configClass);
								gooberConfigBuilder = GooberConfigBuilder.create(modId);
								for (Class<?> clazz : classesToMagic) {
									gooberConfigBuilder.makeBuiltCategory(clazz, clazz.getSimpleName());
								}
							}
							flattened.put(modId, gooberConfigBuilder.build());
							GooberLibApi.resetDefaultValues();
						}
					} catch (IllegalAccessException | ClassNotFoundException e) {
						throw rethrow(e);
					}
				});
		return flattened;
	}

	public static Map<String, BuiltConfig> getConfigs() {
		return GooberLibEntrypoint.builtConfigMap;
	}
}
