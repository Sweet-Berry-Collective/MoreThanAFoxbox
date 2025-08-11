/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.block;

import com.mojang.serialization.MapCodec;
import dev.sweetberry.more_than_a_foxbox.block.entity.BoxBlockEntity;
import dev.sweetberry.more_than_a_foxbox.block.property.MtfbBlockProperties;
import dev.sweetberry.more_than_a_foxbox.util.OctalDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import java.util.Objects;

public class BoxBlock extends BaseEntityBlock {
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
		builder.add(MtfbBlockProperties.FACING);
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(
		BlockPos pos,
		BlockState state
	) {
		return new BoxBlockEntity(pos, state);
	}
}
