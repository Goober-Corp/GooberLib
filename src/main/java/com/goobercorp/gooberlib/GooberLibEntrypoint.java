package com.goobercorp.gooberlib;

import com.goobercorp.gooberlib.asm.ModClassVisitor;
import com.goobercorp.gooberlib.builder.BuiltConfig;
import com.google.common.reflect.ClassPath;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import org.objectweb.asm.ClassReader;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class GooberLibEntrypoint implements ModInitializer {
    private static final String MOD_ID = "gooberlib";
    protected static Map<String, BuiltConfig> builtConfigMap = new HashMap<>();

    @Override
    public void onInitialize() {
        System.out.println("lib init");

        ClientLifecycleEvents.CLIENT_STARTED.register(_ -> {
            try {
                long start = System.nanoTime();

                ConfigDiscovery.DiscoveryResult discoveryResult = ConfigDiscovery.discover();

                System.out.printf("Discovered %d configs in %dms%n", discoveryResult.className2ModId().size(),
                        Duration.ofNanos(System.nanoTime() - start).toMillis());

                builtConfigMap = ConfigDiscovery.flatten(discoveryResult);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
