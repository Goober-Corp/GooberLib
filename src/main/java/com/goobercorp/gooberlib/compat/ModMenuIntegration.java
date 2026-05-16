package com.goobercorp.gooberlib.compat;

import com.goobercorp.gooberlib.api.GooberLibApi;
import com.goobercorp.gooberlib.util.ConfigDiscovery;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import java.util.HashMap;
import java.util.Map;

public class ModMenuIntegration implements ModMenuApi {
	@Override
	public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
		HashMap<String, ConfigScreenFactory<?>> map = new HashMap<>();
		ConfigDiscovery.getConfigs().forEach((modId, config) -> map.put(modId, parent -> GooberLibApi.getScreenFor(modId, parent)));
		return map;
	}
}
