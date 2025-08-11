/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.block.entity;

import dev.sweetberry.more_than_a_foxbox.block.PlushieBlock;
import dev.sweetberry.more_than_a_foxbox.data.PlushieVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class PlushieBlockEntity extends PlushieHoldingBlockEntity {
	public PlushieBlockEntity(
			BlockPos pos,
			BlockState blockState
	) {
		super(MtfbBlockEntityTypes.PLUSHIE.get(), pos, blockState);
	}

	@Override
	public Optional<ClientAsset> getPoseModel(BlockState state) {
		if (this.getLevel() == null) return Optional.empty();
		RegistryAccess registryAccess = this.getLevel().registryAccess();
		PlushieVariant.Pose pose = state.getValue(PlushieBlock.POSE);
		return this.getPlushieVariant()
			.flatMap(
			variant -> registryAccess.lookup(variant.registryKey())
				.map(registry -> registry.get(variant.location()))
			)
			.orElse(Optional.empty())
			.map(reference -> reference.value().poses().getModelFromPose(pose));
	}
}
