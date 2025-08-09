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

public abstract class BasePlushieBlockEntity extends BlockEntity {
	private @Nullable ResourceKey<PlushieVariant> variant = null;

	public BasePlushieBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
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
