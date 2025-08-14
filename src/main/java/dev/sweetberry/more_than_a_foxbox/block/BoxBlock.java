/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.block;

import com.mojang.serialization.MapCodec;
import dev.sweetberry.more_than_a_foxbox.block.entity.BoxBlockEntity;
import dev.sweetberry.more_than_a_foxbox.block.entity.MtfbBlockEntityTypes;
import dev.sweetberry.more_than_a_foxbox.block.entity.PlushieHoldingBlockEntity;
import dev.sweetberry.more_than_a_foxbox.block.property.MtfbBlockProperties;
import dev.sweetberry.more_than_a_foxbox.data.MtfbComponents;
import dev.sweetberry.more_than_a_foxbox.item.MtfbItems;
import dev.sweetberry.more_than_a_foxbox.util.OctalDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.Objects;
import java.util.Optional;

public class BoxBlock extends PlushieHoldingBlock {
	public static final MapCodec<BoxBlock> CODEC = simpleCodec(BoxBlock::new);
	public static final float Z_DEFENSE = 0.005f;
	private static final VoxelShape SHAPE = Block.column(10.0f, 0.0f, 8.0f);

	public BoxBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(
			this.stateDefinition.any()
				.setValue(MtfbBlockProperties.FACING, OctalDirection.SOUTH)
		);
	}

	@Override
	public @NotNull InteractionResult crouchUseWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, PlushieHoldingBlockEntity entity) {
		if (!entity.hasPlushieData())
			return InteractionResult.PASS;

		var stack = getPlushieStack(entity);

		entity.removePlushieData();

		if (player.hasInfiniteMaterials())
			return InteractionResult.SUCCESS;

		player.getInventory().setSelectedItem(stack);

		return InteractionResult.SUCCESS;
	}

	@Override
	public @NotNull BlockEntityType<? extends PlushieHoldingBlockEntity> getPlushieHoldingBlockEntityType() {
		return MtfbBlockEntityTypes.CARDBOARD_BOX.get();
	}

	/**
	 * Point a block toward an {@link OctalDirection}.
	 * @param direction The direction to point toward.
	 * @return A transformation matrix representing the rotation and z-fighting
	 * translation.
	 */
	public static @NotNull Matrix4f pointBlockToward(OctalDirection direction, float centerXZ) {
		var rotationTransform = new Matrix4f();
		rotationTransform.rotateAround(
			new Quaternionf().rotateY(-direction.getDegrees() * Mth.DEG_TO_RAD + Mth.PI),
			centerXZ,
			0.0f,
			centerXZ
		);
		// Fix z-fighting
		rotationTransform.translate(0.0f, Z_DEFENSE, 0.0f);
		return rotationTransform;
	}

	@Override
	protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
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
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(MtfbBlockProperties.FACING, POWERED);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(
		BlockPos pos,
		BlockState state
	) {
		return new BoxBlockEntity(pos, state);
	}

	@Override
	protected @NotNull InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (player.isShiftKeyDown())
			return super.useItemOn(stack, state, level, pos, player, hand, hitResult);

		if (!stack.is(MtfbItems.PLUSHIE.get()))
			return super.useItemOn(stack, state, level, pos, player, hand, hitResult);

		var maybeEntity = level.getBlockEntity(pos, MtfbBlockEntityTypes.CARDBOARD_BOX.get());

		if (maybeEntity.isEmpty())
			return super.useItemOn(stack, state, level, pos, player, hand, hitResult);

		var entity = maybeEntity.get();

		if (entity.hasPlushieData())
			return super.useItemOn(stack, state, level, pos, player, hand, hitResult);

		if (!stack.has(MtfbComponents.PLUSHIE.get()))
			return super.useItemOn(stack, state, level, pos, player, hand, hitResult);

		entity.setPlushieData(stack.get(MtfbComponents.PLUSHIE.get()), Optional.ofNullable(stack.get(DataComponents.CUSTOM_NAME)));

		stack.consume(1, player);

		return InteractionResult.SUCCESS;
	}

	@Override
	protected boolean hasAnalogOutputSignal(BlockState state) {
		return true;
	}

	@Override
	protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
		var maybeEntity = level.getBlockEntity(pos, MtfbBlockEntityTypes.CARDBOARD_BOX.get());

		if (maybeEntity.isEmpty())
			return 0;

		var entity = maybeEntity.get();

		return entity.hasPlushieData() ? 1 : 0;
	}
}
