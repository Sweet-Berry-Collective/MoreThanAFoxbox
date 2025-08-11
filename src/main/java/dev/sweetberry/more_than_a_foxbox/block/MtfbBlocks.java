/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.block;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.registry.RegistryContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;
import java.util.function.Supplier;

public final class MtfbBlocks {
	private static final RegistryContext<Block> CONTEXT = new RegistryContext<>(
		BuiltInRegistries.BLOCK,
		MoreThanAFoxbox.ID
	);

	public static final RegistryContext.Value<Block> POLYFILL_BLOCK = CONTEXT.defer(
		"polyfill_block",
		withProperties(properties -> new WebBlock(
			properties
				.noCollission()
				.instabreak()
				.sound(SoundType.WOOL)
		))
	);
	
	public static final Supplier<Block> CARDBOARD_BOX = CONTEXT.defer(
		"cardboard_box",
		withProperties(properties -> new BoxBlock(
			properties
				.instabreak()
				.sound(SoundType.BAMBOO_WOOD)
				.mapColor(MapColor.WOOD)
				.noOcclusion()
		))
	);
	
	private MtfbBlocks() {}
	
	public static void register() {
		CONTEXT.register();
	}

	@SuppressWarnings("unchecked")
	private static <T extends Block> Function<ResourceKey<T>, T> withProperties(Function<BlockBehaviour.Properties, T> callback) {
		return key -> callback.apply(BlockBehaviour.Properties.of().setId((ResourceKey<Block>) key));
	}
}
