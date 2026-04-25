package com.goobercorp.gooberlib.test;

import net.fabricmc.api.ModInitializer;

public class GooberLibTest implements ModInitializer {

    @Override
    public void onInitialize() {
        System.out.println("initialized test");
		System.out.println(TestConfig.int2++);
    }
}
