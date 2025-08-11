/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.block.entity;

import dev.sweetberry.more_than_a_foxbox.component.MtfbComponents;
import dev.sweetberry.more_than_a_foxbox.component.PlushieDataComponent;
import dev.sweetberry.more_than_a_foxbox.data.PlushieVariant;
import dev.sweetberry.more_than_a_foxbox.registry.MtfbRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class PlushieHoldingBlockEntity extends BlockEntity {
	private ResourceLocation variant;

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

	@Override
	public void setLevel(Level level) {
		super.setLevel(level);
		if (this.getLevel() != null && this.variant != null) {
			RegistryAccess registryAccess = this.getLevel().registryAccess();
			registryAccess.lookup(MtfbRegistries.PLUSHIE_VARIANT)
				.flatMap(
					registry -> registry.get(this.variant)
				)
				.ifPresent(this::setPlushieVariant);
		}
	}

	@Override
	protected void loadAdditional(ValueInput input) {
		input.getString("variant")
			.ifPresent(variant -> this.variant = ResourceLocation.parse(variant));
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		if (this.components().has(MtfbComponents.PLUSHIE.get())) {
			@SuppressWarnings("DataFlowIssue") ResourceLocation variant = this.components().get(MtfbComponents.PLUSHIE.get()).variant().unwrapKey().orElseThrow().location();
			output.putString("variant", variant.toString());
		}
	}

	@Override
	public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return this.saveWithoutMetadata(registries);
	}
}
