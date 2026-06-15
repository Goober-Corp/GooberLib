package com.goobercorp.gooberlib.option.individual.misc;

import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.option.Option;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DynamicOps;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.apache.commons.io.function.Erase.rethrow;

@ApiStatus.Experimental
public class ObjectOption<T> extends BaseOption<ObjectOption<T>> {
	private final T instance;
	public final List<Option<?>> options;

	public ObjectOption(CharSequence name, T instance, Function<ObjectOption<T>, CharSequence> description, @Nullable WidgetProvider<ObjectOption<T>> provider) {
		super(name, description, provider);
		this.instance = instance;
		this.options = new ArrayList<>();
		Class<?> tClass = instance.getClass();
		for (Field f : tClass.getFields()) {
			if (Option.class.isAssignableFrom(f.getType())) {
				int mods = f.getModifiers();
				if (Modifier.isPublic(mods) && !Modifier.isStatic(mods) && Modifier.isFinal(mods)) {
					try {
						options.add((Option<?>) f.get(instance));
					} catch (IllegalAccessException e) {
						throw rethrow(e);
					}
				}
			}
		}
	}

	public ObjectOption(CharSequence name, T instance, CharSequence description) {
		this(name, instance, _ -> description, null);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createMap(options.stream().map(v -> Pair.of(ops.createString(v.name().getString()), v.serialize(ops))));
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		Stream<Pair<S, S>> s = ops.getMapValues(object).getOrThrow();
		s.forEach(v -> {
			String name = ops.getStringValue(v.getFirst()).getOrThrow();
			for (Option<?> o : options) {
				if (o.name().getString().equals(name)) {
					o.deserialize(ops, v.getSecond());
				}
			}
		});
	}

	@Override
	public void resetToDefault() {
		for (Option<?> opt : options) {
			opt.resetToDefault();
		}
	}

	public T getInstance() {
		return instance;
	}
}
