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

public class RegistryContext<TValue> {
	public final Registry<TValue> registry;
	public final String namespace;

	private final List<Value<TValue>> values = new ArrayList<>();

	public RegistryContext(Registry<TValue> registry, String namespace) {
		this.registry = registry;
		this.namespace = namespace;
	}

	@SuppressWarnings("unchecked")
	public <T extends TValue> Value<T> defer(String path, Function<ResourceKey<T>, T> createCallback) {
		var value = new Value<T>(ResourceLocation.fromNamespaceAndPath(namespace, path), createCallback);

		values.add((Value<TValue>) value);

		return value;
	}

	public void register() {
		for (var value : values) {
			value.register(registry);
		}
	}

	public static class Value<TValue> {
		public final ResourceLocation location;
		public final Function<ResourceKey<TValue>, TValue> createCallback;

		private @Nullable TValue value = null;

		public Value(ResourceLocation location, Function<ResourceKey<TValue>, TValue> createCallback) {
			this.location = location;
			this.createCallback = createCallback;
		}

		public @NotNull TValue get() {
			if (value == null)
				throw new NullPointerException();

			return value;
		}

		private void register(Registry<TValue> registry) {
			value = Registry.register(registry, location, createCallback.apply(ResourceKey.create(registry.key(), location)));
		}
	}
}
