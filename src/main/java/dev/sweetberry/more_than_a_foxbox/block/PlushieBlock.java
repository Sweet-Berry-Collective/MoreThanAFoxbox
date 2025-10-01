/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.block;

import com.mojang.serialization.MapCodec;
import com.sun.jna.platform.win32.Variant;
import dev.sweetberry.more_than_a_foxbox.block.entity.MtfbBlockEntityTypes;
import dev.sweetberry.more_than_a_foxbox.block.entity.PlushieBlockEntity;
import dev.sweetberry.more_than_a_foxbox.block.entity.PlushieHoldingBlockEntity;
import dev.sweetberry.more_than_a_foxbox.block.property.MtfbBlockProperties;
import dev.sweetberry.more_than_a_foxbox.data.MtfbComponents;
import dev.sweetberry.more_than_a_foxbox.data.PlushieVariant;
import dev.sweetberry.more_than_a_foxbox.util.OctalDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PlushieBlock extends PlushieHoldingBlock {
	public static final MapCodec<PlushieBlock> CODEC = simpleCodec(PlushieBlock::new);
	public static final EnumProperty<PlushieVariant.Pose> POSE = EnumProperty.create("pose", PlushieVariant.Pose.class);
	private static final VoxelShape SHAPE = Block.column(10.0f, 0.0f, 8.0f);

	protected PlushieBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(
			this.defaultBlockState()
				.setValue(POSE, PlushieVariant.Pose.SIT)
				.setValue(MtfbBlockProperties.FACING, OctalDirection.SOUTH)
		);
	}

	@Override
	public @NotNull InteractionResult crouchUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, PlushieHoldingBlockEntity entity) {
		level.setBlock(pos, state.cycle(POSE), Block.UPDATE_ALL);

		return InteractionResult.SUCCESS;
	}

	@Override
	public @NotNull BlockEntityType<? extends PlushieHoldingBlockEntity> getPlushieHoldingBlockEntityType() {
		return MtfbBlockEntityTypes.PLUSHIE.get();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POSE, MtfbBlockProperties.FACING, POWERED);
	}

	@Override
	public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
		return Objects.requireNonNull(super.getStateForPlacement(context), "Superclass returned null state for placement. This shouldn't happen!")
			.setValue(
				MtfbBlockProperties.FACING,
				OctalDirection.fromYRot(context.getRotation()).getOpposite()
			);
	}

	@Override
	protected @NotNull VoxelShape getShape(
		BlockState state,
		BlockGetter level,
		BlockPos pos,
		CollisionContext context
	) {
		return SHAPE;
	}

	@Override
	protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	protected @NotNull RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(
		BlockPos pos,
		BlockState state
	) {
		return new PlushieBlockEntity(pos, state);
	}

	@Override
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	protected @NotNull ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
		var stack = super.getCloneItemStack(level, pos, state, includeData);

		var maybeEntity = level.getBlockEntity(pos, MtfbBlockEntityTypes.PLUSHIE.get());

		if (maybeEntity.isEmpty())
			return stack;

		var entity = maybeEntity.get();

		return getPlushieStack(entity);
	}

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos blockPos, Direction direction) {
		return state.getValue(POSE).ordinal() + 1;
	}
}
