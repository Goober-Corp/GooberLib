package com.goobercorp.gooberlib.option.individual.misc;

import com.goobercorp.gooberlib.interfaces.WidgetProvider;
import com.goobercorp.gooberlib.option.BaseOption;
import com.goobercorp.gooberlib.option.Option;
import com.mojang.serialization.DynamicOps;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

// todo: test lol
public class ListOption<T extends Option<T>> extends BaseOption<ListOption<T>> implements Iterable<T> {
	private final Supplier<T> initial;
	private final boolean insertAtEnd;
	private final @Unmodifiable List<T> defaultValue;
	private final List<T> value;

	public ListOption(CharSequence name, Function<ListOption<T>, CharSequence> description, Supplier<T> initial, boolean insertAtEnd, List<T> defaultValue, @Nullable WidgetProvider<ListOption<T>> provider) {
		super(name, description, provider);
		this.initial = initial;
		this.insertAtEnd = insertAtEnd;
		this.defaultValue = Collections.unmodifiableList(defaultValue);
		this.value = new ArrayList<>(defaultValue);
	}

	public ListOption(CharSequence name, List<T> defaultValue, Supplier<T> initial) {
		this(name, _ -> "", initial, true, defaultValue, null);
	}

	@Override
	public <S> S serialize(DynamicOps<S> ops) {
		return ops.createList(defaultValue.stream().map(v -> v.serialize(ops)));
	}

	@Override
	public <S> void deserialize(DynamicOps<S> ops, S object) {
		Stream<S> list = ops.getStream(object).getOrThrow();
		this.value.clear();
		list.forEach(s -> {
			var v = initial.get();
			v.deserialize(ops, s);
			this.value.add(v);
		});
	}

	@Override
	public Iterator<T> iterator() {
		return value.iterator();
	}

	public List<T> getValue() {
		return value;
	}

	public @Unmodifiable List<T> getDefaultValue() {
		return this.defaultValue;
	}

	public void setValue(List<T> list) {
		this.value.clear();
		this.value.addAll(list);
	}

	public boolean isInsertAtEnd() {
		return insertAtEnd;
	}
}
