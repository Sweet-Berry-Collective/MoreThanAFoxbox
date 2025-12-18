/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.block.entity;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.block.MtfbBlocks;
import dev.sweetberry.more_than_a_foxbox.registry.RegistryContext;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;

public final class MtfbBlockEntityTypes {
	private static final RegistryContext<BlockEntityType<?>> CONTEXT =  new RegistryContext<>(
		BuiltInRegistries.BLOCK_ENTITY_TYPE,
		MoreThanAFoxbox.ID
	);

	public static final Supplier<BlockEntityType<BoxBlockEntity>> CARDBOARD_BOX = CONTEXT.defer(
		"cardboard_box",
		withBuilder(
			FabricBlockEntityTypeBuilder::build,
			BoxBlockEntity::new,
			MtfbBlocks.CARDBOARD_BOX
		)
	);
	
	public static final Supplier<BlockEntityType<PlushieBlockEntity>> PLUSHIE = CONTEXT.defer(
		"plushie",
		withBuilder(
			FabricBlockEntityTypeBuilder::build,
			PlushieBlockEntity::new,
			MtfbBlocks.PLUSHIE
		)
	);

	private MtfbBlockEntityTypes() {}

	public static void register() {
		CONTEXT.register();
	}

	@SafeVarargs // We don't actually care about the type of Block that's supplied.
	private static <T extends BlockEntity> Function<ResourceKey<BlockEntityType<T>>, BlockEntityType<T>> withBuilder(
		Function<FabricBlockEntityTypeBuilder<T>, BlockEntityType<T>> callback,
		FabricBlockEntityTypeBuilder.Factory<? extends T> factory,
		Supplier<? extends Block>... blocks
	) {
		return key -> callback.apply(
			FabricBlockEntityTypeBuilder.create(
				factory,
				Arrays
					.stream(blocks)
					.map(Supplier::get)
					.toArray(Block[]::new)
			)
		);
	}
}
