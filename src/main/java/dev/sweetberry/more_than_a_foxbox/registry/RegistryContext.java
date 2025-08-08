/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.registry;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RegistryContext<TValue, TProperties> {
	public final Registry<TValue> registry;
	public final String namespace;
	public final Function<ResourceKey<TValue>, TProperties> propertiesCallback;

	private final List<Pair<Value<TValue>, Function<TProperties, ? extends TValue>>> values = new ArrayList<>();

	public RegistryContext(Registry<TValue> registry, String namespace, Function<ResourceKey<TValue>, TProperties> propertiesCallback) {
		this.registry = registry;
		this.namespace = namespace;
		this.propertiesCallback = propertiesCallback;
	}

	public <T extends TValue> Value<TValue> defer(String path, Function<TProperties, T> createCallback) {
		var value = new Value<>(ResourceKey.create(registry.key(), ResourceLocation.fromNamespaceAndPath(namespace, path)));

		values.add(Pair.of(value, createCallback));

		return value;
	}

	public void register() {
		for (var pair : values) {
			var value = pair.getFirst();
			var createCallback = pair.getSecond();
			value.update(Registry.register(registry, value.key, createCallback.apply(propertiesCallback.apply(value.key))));
		}
	}

	public static class Value<TValue> {
		public final ResourceKey<TValue> key;

		private @Nullable TValue value = null;

		public Value(ResourceKey<TValue> key) {
			this.key = key;
		}

		public @NotNull TValue get() {
			if (value == null)
				throw new NullPointerException();

			return value;
		}

		private <T extends TValue> void update(T newValue) {
			value = newValue;
		}
	}
}
