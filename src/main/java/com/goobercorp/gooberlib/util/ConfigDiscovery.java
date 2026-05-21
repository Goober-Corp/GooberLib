package com.goobercorp.gooberlib.util;

import com.goobercorp.gooberlib.GooberLibEntrypoint;
import com.goobercorp.gooberlib.annotations.GooberConfig;
import com.goobercorp.gooberlib.builder.BuiltConfig;
import com.goobercorp.gooberlib.builder.GooberConfigBuilder;
import com.google.common.reflect.ClassPath;
import org.spongepowered.asm.mixin.transformer.ClassInfo;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static org.apache.commons.lang3.function.Failable.rethrow;

public class ConfigDiscovery {
	private static final String[] BLACKLISTED_PACKAGES = {
			"org.objectweb", "net.minecraft", "com.sun", "it.unimi",
			"org.lwjgl", "org.slf4j", "com.mojang", "net.fabricmc",
			"com.google", "com.jcraft", "org.apache", "org.joml",
			"io.netty", "com.ibm", "com.llamalad7.mixinextras",
			"oshi", "org.spongepowered", "java"
	}; // TODO improve hacky approach


	public static Map<String, BuiltConfig> discover() throws IOException {
		final Map<String, BuiltConfig> flattened = new HashMap<>();

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

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

							GooberConfigBuilder gooberConfigBuilder;
							if (!builderFields.isEmpty()) {
								if (builderFields.size() > 1)
									throw new IllegalStateException("Please only have one builder field");

								Field builderField = builderFields.getFirst();
								gooberConfigBuilder = (GooberConfigBuilder) builderField.get(null);
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
