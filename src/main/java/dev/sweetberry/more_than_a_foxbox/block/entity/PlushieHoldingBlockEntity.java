/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.block.entity;

import dev.sweetberry.more_than_a_foxbox.data.PlushieVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class PlushieHoldingBlockEntity extends BlockEntity {
	private @Nullable ResourceKey<PlushieVariant> variant = null;

	public PlushieHoldingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	public void setPlushieVariant(ResourceKey<PlushieVariant> variant) {
		this.variant = variant;
	}

	public Optional<ResourceKey<PlushieVariant>> getPlushieVariant() {
		return Optional.ofNullable(variant);
	}

	public abstract Optional<ClientAsset> getModel(BlockState state);
}
