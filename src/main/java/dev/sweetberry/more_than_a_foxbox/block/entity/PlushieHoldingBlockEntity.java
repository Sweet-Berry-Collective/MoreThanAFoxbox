/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.block.entity;

import dev.sweetberry.more_than_a_foxbox.component.MtfbComponents;
import dev.sweetberry.more_than_a_foxbox.component.PlushieDataComponent;
import dev.sweetberry.more_than_a_foxbox.data.PlushieVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public abstract class PlushieHoldingBlockEntity extends BlockEntity {
	public PlushieHoldingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	public void setPlushieVariant(Holder<PlushieVariant> variant) {
		this.applyComponents(
			this.components(),
			DataComponentPatch.builder()
				.set(MtfbComponents.PLUSHIE.get(), new PlushieDataComponent(variant, Optional.empty()))
				.build()
		);
	}

	public Optional<Holder<PlushieVariant>> getPlushieVariant() {
		return Optional.ofNullable(this.components().get(MtfbComponents.PLUSHIE.get()))
			.map(PlushieDataComponent::variant);
	}

	public abstract Optional<ResourceLocation> getPoseModel(BlockState state);
}
