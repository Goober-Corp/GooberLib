package com.goobercorp.gooberlib.builder;

import com.google.gson.Gson;
import net.minecraft.text.Text;

import java.util.List;

public record BuiltConfig(Gson gson, Text title, List<ConfigCategory> categories) {}
