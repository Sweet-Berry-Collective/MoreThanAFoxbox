/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.block.entity;

import java.util.Optional;

import dev.sweetberry.more_than_a_foxbox.block.PlushieBlock;
import dev.sweetberry.more_than_a_foxbox.data.PlushieVariant;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;

public class PlushieBlockEntity extends PlushieHoldingBlockEntity {
	public PlushieBlockEntity(
			BlockPos pos,
			BlockState blockState
	) {
		super(MtfbBlockEntityTypes.PLUSHIE.get(), pos, blockState);
	}

	@Override
	public Optional<Identifier> getPoseModel(BlockState state) {
		PlushieVariant.Pose pose = state.getValue(PlushieBlock.POSE);
		return this.getPlushieVariant()
			.map(holder -> holder.value().poses().getModelFromPose(pose));
	}
}
