/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.component;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.data.PlushieVariant;
import dev.sweetberry.more_than_a_foxbox.registry.MtfbRegistries;
import dev.sweetberry.more_than_a_foxbox.registry.RegistryContext;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

import java.util.function.Function;

public final class MtfbComponents {
	private static final RegistryContext<DataComponentType<?>> CONTEXT = new RegistryContext<>(
		BuiltInRegistries.DATA_COMPONENT_TYPE,
		MoreThanAFoxbox.ID
	);

	public static final RegistryContext.Value<DataComponentType<PlushieDataComponent>> PLUSHIE = CONTEXT.defer(
		"plushie",
		withBuilder(builder -> builder
			.persistent(PlushieDataComponent.CODEC)
			.networkSynchronized(PlushieDataComponent.STREAM_CODEC)
			.build()
		)
	);

	private MtfbComponents() {}

	public static void register() {
		CONTEXT.register();
	}

	private static <T> Function<ResourceKey<DataComponentType<T>>, DataComponentType<T>> withBuilder(
		Function<DataComponentType.Builder<T>, DataComponentType<T>> callback
	) {
		return key -> callback.apply(DataComponentType.builder());
	}
}
