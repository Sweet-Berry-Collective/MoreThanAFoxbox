/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.block.entity;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.block.MtfbBlocks;
import dev.sweetberry.more_than_a_foxbox.registry.RegistryContext;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class MtfbBlockEntityTypes {
	private static final RegistryContext<BlockEntityType<?>, Builder> CONTEXT =  new RegistryContext<>(
		BuiltInRegistries.BLOCK_ENTITY_TYPE,
		MoreThanAFoxbox.ID,
		key -> FabricBlockEntityTypeBuilder::create
	);
	
	public static final RegistryContext.Value<BlockEntityType<?>> CARDBOARD_BOX = CONTEXT.defer(
		"cardboard_box",
		builder -> builder.create(BoxBlockEntity::new, MtfbBlocks.CARDBOARD_BOX.get()).build()
	);
	
	private MtfbBlockEntityTypes() {}
	
	public static void register() {
		CONTEXT.register();
	}
	
	@FunctionalInterface
	public interface Builder {
		FabricBlockEntityTypeBuilder<?> create(FabricBlockEntityTypeBuilder.Factory<?> factory, Block... blocks);
	}
}
