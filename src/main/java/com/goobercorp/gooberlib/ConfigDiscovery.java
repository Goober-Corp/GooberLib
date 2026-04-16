package com.goobercorp.gooberlib;

import com.goobercorp.gooberlib.asm.MethodResult;
import com.goobercorp.gooberlib.asm.ModClassVisitor;
import com.goobercorp.gooberlib.builder.BuiltConfig;
import com.goobercorp.gooberlib.builder.GooberConfigBuilder;
import com.google.common.reflect.ClassPath;
import net.minecraft.client.gui.screen.Screen;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

class ConfigDiscovery {
    private static final String[] BLACKLISTED_PACKAGES = {
            "org.objectweb", "net.minecraft", "com.sun", "it.unimi",
            "org.lwjgl", "org.slf4j", "com.mojang", "net.fabricmc",
            "com.google", "com.jcraft", "org.apache", "org.joml",
            "io.netty", "com.ibm", "com.llamalad7.mixinextras",
            "oshi", "org.spongepowered", "java"
    }; // TODO improve hacky approach

    static DiscoveryResult discover() throws IOException {
        Map<String, String> className2ModId = new HashMap<>();
        Map<String, MethodResult> className2AccessorMethod = new HashMap<>();

        ModClassVisitor modClassVisitor = new ModClassVisitor(className2ModId::put, className2AccessorMethod::put);
        ClassLoader classLoader = getDiscoveryClassLoader();

        ClassPath.from(classLoader)
                .getAllClasses()
                .forEach(classInfo -> {
                    String packageName = classInfo.getPackageName();
                    for (String blacklistedPackage : BLACKLISTED_PACKAGES)
                        if (packageName.startsWith(blacklistedPackage)) return;

                    try {
                        ClassReader classReader = new ClassReader(classInfo.asByteSource().read());
                        classReader.accept(modClassVisitor, 0);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        return new DiscoveryResult(className2ModId, className2AccessorMethod);
    }

    static Map<String, BuiltConfig> flatten(DiscoveryResult discoveryResult) {
        ClassLoader classLoader = getDiscoveryClassLoader();

        final Map<String, BuiltConfig> flattened = new HashMap<>();
        discoveryResult.className2ModId()
                .forEach((className, modId) -> {
                    if (flattened.containsKey(modId))
                        throw new IllegalStateException("Multiple config classes found for the same mod id: %s"
                                .formatted(modId));

                    try {
                        Class<?> configClass = Class.forName(className.replace('/', '.'));

                        MethodResult accessorMethodResult = discoveryResult.className2AccessorMethod().get(className);
                        if (accessorMethodResult == null)
                            throw new IllegalStateException("Config class " + configClass.getName() + " has no defined GooberBuilderAccessor method");

                        Method accessorMethod = ReflectionUtil.getMethod(classLoader, configClass, accessorMethodResult);
                        int mods = accessorMethod.getModifiers();

                        if (!Modifier.isStatic(mods))
                            throw new IllegalStateException("Accessor method is not static");

                        if (accessorMethod.getReturnType() != GooberConfigBuilder.class)
                            throw new IllegalStateException("Accessor method does not return GooberConfigBuilder");

                        GooberConfigBuilder gooberConfigBuilder = (GooberConfigBuilder) accessorMethod.invoke(null);

                        flattened.put(modId, gooberConfigBuilder.build());
                    } catch (ClassNotFoundException cnf) {
                        throw new RuntimeException("Couldn't load config class. why???", cnf);
                    } catch (NoSuchMethodException nsm) {
                        throw new RuntimeException("Discovered method doesn't exist. why???", nsm);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        return flattened;
    }
    //TODO: stop being retarded
    public static BuiltConfig getBuiltConfig(String modid){
        return GooberLibEntrypoint.builtConfigMap.get(modid);
    }
    public static Map<String, BuiltConfig> getConfigs(){
        return GooberLibEntrypoint.builtConfigMap;
    }

    private static ClassLoader getDiscoveryClassLoader() {
        return GooberConfig.class.getClassLoader();
    }

    record DiscoveryResult(Map<String, String> className2ModId, Map<String, MethodResult> className2AccessorMethod) {}
}
