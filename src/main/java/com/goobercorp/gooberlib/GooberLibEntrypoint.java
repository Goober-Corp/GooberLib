package com.goobercorp.gooberlib;

import com.goobercorp.gooberlib.api.GooberLibApi;
import com.goobercorp.gooberlib.builder.BuiltConfig;
import com.goobercorp.gooberlib.util.ConfigDiscovery;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.io.function.Erase.rethrow;

public class GooberLibEntrypoint implements ModInitializer {
	public static final String MOD_ID = "gooberlib";
	public static final Logger LOGGER = LogUtils.getLogger();
	public static Map<String, BuiltConfig> builtConfigMap = new HashMap<>();

	public static void init() {
		try {
			long start = System.nanoTime();

			builtConfigMap = ConfigDiscovery.discover(false);
			//TODO make this async
			LOGGER.info("Discovered {} configs in {}ms", builtConfigMap.size(), Duration.ofNanos(System.nanoTime() - start).toMillis());

			GooberLibApi.loadAll();
		} catch (IOException e) {
			throw rethrow(e);
		}
	}

	@Override
	public void onInitialize() {
		try {
			long start = System.nanoTime();

			builtConfigMap.putAll(ConfigDiscovery.discover(true));
			LOGGER.info("Discovered {} late configs in {}ms", builtConfigMap.size(), Duration.ofNanos(System.nanoTime() - start).toMillis());

			GooberLibApi.loadAll();
		} catch (IOException e) {
			throw rethrow(e);
		}
	}
}
