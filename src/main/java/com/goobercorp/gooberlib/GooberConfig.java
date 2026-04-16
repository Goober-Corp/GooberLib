package com.goobercorp.gooberlib;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface GooberConfig {
    String modId();
}
