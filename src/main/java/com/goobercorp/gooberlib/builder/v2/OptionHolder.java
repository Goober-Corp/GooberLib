package com.goobercorp.gooberlib.builder.v2;

import com.goobercorp.gooberlib.builder.ConfigOption;
import com.goobercorp.gooberlib.builder.MetadataHolder;

import java.util.List;

public interface OptionHolder {

    MetadataHolder.Metadata metadata();
    List<ConfigOption> childOptions();

}
