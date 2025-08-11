/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class BoxBlockEntity extends PlushieHoldingBlockEntity {
	public BoxBlockEntity(
		BlockPos pos,
		BlockState blockState
	) {
		super(MtfbBlockEntityTypes.CARDBOARD_BOX.get(), pos, blockState);
	}

	@Override
	public Optional<ResourceLocation> getPoseModel(BlockState state) {
		// TODO!
		return Optional.empty();
	}
}
