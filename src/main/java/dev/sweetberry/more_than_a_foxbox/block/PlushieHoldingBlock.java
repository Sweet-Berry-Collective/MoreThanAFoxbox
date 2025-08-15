/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.block;

import dev.sweetberry.more_than_a_foxbox.block.entity.PlushieHoldingBlockEntity;
import dev.sweetberry.more_than_a_foxbox.data.MtfbComponents;
import dev.sweetberry.more_than_a_foxbox.item.MtfbItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class PlushieHoldingBlock extends BaseEntityBlock {
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	protected PlushieHoldingBlock(Properties properties) {
		super(properties);
	}

	public abstract @NotNull InteractionResult crouchUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, PlushieHoldingBlockEntity entity);

	public abstract @NotNull BlockEntityType<? extends PlushieHoldingBlockEntity> getPlushieHoldingBlockEntityType();

	@Override
	protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		var maybeEntity = level.getBlockEntity(pos, getPlushieHoldingBlockEntityType());

		if (maybeEntity.isEmpty())
			return super.useWithoutItem(state, level, pos, player, hitResult);

		var entity = maybeEntity.get();

		if (player.isShiftKeyDown())
			return crouchUseWithoutItem(state, level, pos, player, hitResult, entity);
		
		entity.resetStretchSquish();
		entity.playSound(level, pos);

		return InteractionResult.SUCCESS;
	}

	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, @Nullable Orientation orientation, boolean movedByPiston) {
		if (level.isClientSide)
			return;

		boolean powered = level.hasNeighborSignal(pos);

		if (powered == state.getValue(POWERED))
			return;

		level.setBlock(pos, state.setValue(POWERED, powered), 2);

		if (!powered)
			return;

		var maybeEntity = level.getBlockEntity(pos, getPlushieHoldingBlockEntityType());

		if (maybeEntity.isEmpty())
			return;

		var entity = maybeEntity.get();

		entity.playSound(level, pos);
	}

	@Override
	protected boolean isSignalSource(BlockState state) {
		return true;
	}

	public ItemStack getPlushieStack(PlushieHoldingBlockEntity plushie) {
		var maybePlushieData = plushie.getPlushieData();

		if (maybePlushieData.isEmpty())
			return ItemStack.EMPTY;

		var plushieData = maybePlushieData.get();

		var stack = MtfbItems.PLUSHIE.get().getDefaultInstance();

		stack.set(MtfbComponents.PLUSHIE.get(), plushieData);

		plushie.getName().ifPresent(it -> stack.set(DataComponents.CUSTOM_NAME, it));

		return stack;
	}

	@Override
	protected @NotNull List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
		var items =  new ArrayList<>(super.getDrops(state, params));

		var blockEntity = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);

		if (blockEntity instanceof PlushieHoldingBlockEntity plushie) {
			var stack = getPlushieStack(plushie);

			if (stack != ItemStack.EMPTY)
				items.add(stack);
		}

		return items;
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return canSupportCenter(level, pos.below(), Direction.UP);
	}
}
