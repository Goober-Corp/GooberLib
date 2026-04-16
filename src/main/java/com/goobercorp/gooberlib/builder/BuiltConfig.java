package com.goobercorp.gooberlib.builder;

import com.goobercorp.gooberlib.ConfigCategory;
import com.google.gson.Gson;
import net.minecraft.text.Text;

import java.util.List;

// TODO add parent and child options
public record BuiltConfig(Gson gson, Text title, List<ConfigCategory> categories) {}
