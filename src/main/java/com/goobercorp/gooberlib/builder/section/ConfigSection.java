package com.goobercorp.gooberlib.builder.section;

import com.goobercorp.gooberlib.builder.misc.Metadata;
import com.goobercorp.gooberlib.builder.misc.OptionHolder;
import com.goobercorp.gooberlib.option.OptionContext;

import java.util.List;

public record ConfigSection(Metadata metadata, List<OptionContext<?>> childOptions)
		implements OptionHolder {
}
