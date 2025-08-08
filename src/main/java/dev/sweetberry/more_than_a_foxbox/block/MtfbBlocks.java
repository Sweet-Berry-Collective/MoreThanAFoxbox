/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.block;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.registry.RegistryContext;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

public final class MtfbBlocks {
	private static final RegistryContext<Block, BlockBehaviour.Properties> CONTEXT = new RegistryContext<>(
		BuiltInRegistries.BLOCK,
		MoreThanAFoxbox.ID,
		key -> BlockBehaviour.Properties.of().setId(key)
	);
	
	public static final RegistryContext.Value<Block> CARDBOARD_BOX = CONTEXT.defer(
		"cardboard_box",
		properties -> new CardboardBoxBlock(
			properties
				.instabreak()
				.sound(SoundType.WOOL)
				.mapColor(MapColor.WOOD)
		)
	);
	
	private MtfbBlocks() {}
	
	public static void register() {
		CONTEXT.register();
	}
}
