package com.goobercorp.gooberlib;

import com.goobercorp.gooberlib.builder.BuiltConfig;
import com.goobercorp.gooberlib.util.ConfigDiscovery;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.function.Failable.rethrow;

public class GooberLibEntrypoint implements ModInitializer {
    private static final String MOD_ID = "gooberlib";
    public static Map<String, BuiltConfig> builtConfigMap = new HashMap<>();

    @Override
    public void onInitialize() {
        System.out.println("lib init");

        ClientLifecycleEvents.CLIENT_STARTED.register(_ -> {
            try {
                long start = System.nanoTime();

				builtConfigMap = ConfigDiscovery.discover();

                System.out.printf("Discovered %d configs in %dms%n", builtConfigMap.size(),
                        Duration.ofNanos(System.nanoTime() - start).toMillis());

            } catch (IOException e) {
                throw rethrow(e);
            }
        });
    }
}
