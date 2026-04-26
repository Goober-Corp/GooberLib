package com.goobercorp.gooberlib.compat;

import com.goobercorp.gooberlib.screen.GooberScreen;
import com.goobercorp.gooberlib.util.ConfigDiscovery;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import java.util.HashMap;
import java.util.Map;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        HashMap<String, ConfigScreenFactory<?>> map = new HashMap<>();
        ConfigDiscovery.getConfigs().forEach((modid, config) -> map.put(modid, parent -> new GooberScreen(config, parent, modid)));
        return map;
    }
}
