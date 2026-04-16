package com.goobercorp.gooberlib;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import java.io.ObjectInputFilter;
import java.util.HashMap;
import java.util.Map;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        HashMap<String, ConfigScreenFactory<?>> map = new HashMap<>();
        ConfigDiscovery.getConfigs().forEach((modid, config) -> map.put(modid, parent -> new GooberScreen(config, parent)));
        return map;
    }
}
