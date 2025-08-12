/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.block.entity;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.component.MtfbComponents;
import dev.sweetberry.more_than_a_foxbox.component.PlushieDataComponent;
import dev.sweetberry.more_than_a_foxbox.data.PlushieVariant;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class PlushieHoldingBlockEntity extends BlockEntity {
	public static final String PLUSHIE_KEY = "plushie";
	public static final ResourceLocation PLUSHIE_DYNAMIC_DROP = MoreThanAFoxbox.id(PLUSHIE_KEY);

	public PlushieHoldingBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
		super(type, pos, blockState);
	}

	public void setPlushieData(PlushieDataComponent component) {
		applyComponents(
			components(),
			DataComponentPatch.builder()
				.set(MtfbComponents.PLUSHIE.get(), component)
				.build()
		);
		setChanged();
	}

	public void removePlushieData() {
		applyComponents(
			components(),
			DataComponentPatch.builder()
				.remove(MtfbComponents.PLUSHIE.get())
				.build()
		);
		setChanged();
	}

	public Optional<PlushieDataComponent> getPlushieData() {
		return Optional.ofNullable(components().get(MtfbComponents.PLUSHIE.get()));
	}

	public Optional<Holder<PlushieVariant>> getPlushieVariant() {
		return getPlushieData()
			.map(PlushieDataComponent::variant);
	}

	public abstract Optional<ResourceLocation> getPoseModel(BlockState state);

	@Override
	protected void loadAdditional(ValueInput input) {
		var component = input.read(PLUSHIE_KEY, PlushieDataComponent.CODEC);

		if (component.isEmpty())
			return;

		setPlushieData(component.get());
	}

	@Override
	protected void saveAdditional(ValueOutput output) {
		getPlushieData()
			.ifPresent(it -> output.store(PLUSHIE_KEY, PlushieDataComponent.CODEC, it));
	}

	@Override
	public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider registries) {
		return this.saveWithoutMetadata(registries);
	}

	public void playSound(Level level, BlockPos pos) {
		if (level.isClientSide)
			return;

		var variant = this.components().get(MtfbComponents.PLUSHIE.get());

		if (variant == null)
			return;

		var maybeSound = variant.getInteractionSound(level.registryAccess());

		if (maybeSound.isEmpty())
			return;

		var center = pos.getCenter();

		level.playSeededSound(null, center.x, center.y, center.z, maybeSound.get(), SoundSource.PLAYERS, 1f, 1f,  level.random.nextLong());
	}
}
