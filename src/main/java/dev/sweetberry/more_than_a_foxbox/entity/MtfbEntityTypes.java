/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.entity;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.registry.RegistryContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public final class MtfbEntityTypes {
	private static final RegistryContext<EntityType<?>> CONTEXT = new RegistryContext<>(
		BuiltInRegistries.ENTITY_TYPE,
		MoreThanAFoxbox.ID
	);
	
	public static final Supplier<EntityType<BoxSeatEntity>> BOX_SEAT = CONTEXT.defer(
		"box_seat",
		withBuilder(
			(key, builder) -> builder
				.noSummon()
				.sized(0.001f, 0.001f)
				.clientTrackingRange(10)
				.build(key),
			BoxSeatEntity::new,
			MobCategory.MISC
		)
	);
	
	private MtfbEntityTypes() {}
	
	public static void register() {
		CONTEXT.register();
	}
	
	private static <T extends Entity> Function<ResourceKey<EntityType<T>>, EntityType<T>> withBuilder(
		BiFunction<ResourceKey<EntityType<?>>, EntityType.Builder<T>, EntityType<T>> callback,
		EntityType.EntityFactory<T> factory,
		MobCategory mobCategory
	) {
		return key -> callback.apply(
			toWildcard(key),
			EntityType.Builder.of(
				factory,
				mobCategory
			)
		);
	}
	
	private static <T extends Entity> ResourceKey<EntityType<?>> toWildcard(ResourceKey<EntityType<T>> key) {
		return ResourceKey.create(Registries.ENTITY_TYPE, key.location());
	}
}
