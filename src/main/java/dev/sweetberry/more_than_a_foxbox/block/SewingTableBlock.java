/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.block;

import com.mojang.math.OctahedralGroup;
import com.mojang.serialization.MapCodec;
import dev.sweetberry.more_than_a_foxbox.menu.SewingTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SewingTableBlock extends Block {
	public static final MapCodec<SewingTableBlock> CODEC = simpleCodec(SewingTableBlock::new);
	private static final Component CONTAINER_TITLE = Component.translatable("container.more_than_a_foxbox.sewing_table");
	public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
	public static final VoxelShape NORTH_SHAPE = makeShape();
	public static final VoxelShape EAST_SHAPE = Shapes.rotate(NORTH_SHAPE, OctahedralGroup.ROT_90_Y_NEG);
	public static final VoxelShape SOUTH_SHAPE = Shapes.rotate(EAST_SHAPE, OctahedralGroup.ROT_90_Y_NEG);
	public static final VoxelShape WEST_SHAPE = Shapes.rotate(SOUTH_SHAPE, OctahedralGroup.ROT_90_Y_NEG);

	private static VoxelShape makeShape(){
		VoxelShape shape = Shapes.empty();
		shape = Shapes.join(shape, Shapes.box(0, 0, 0, 0.125, 0.75, 0.125), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0, 0, 0.875, 0.125, 0.75, 1), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.875, 0, 0, 1, 0.75, 0.125), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.875, 0, 0.875, 1, 0.75, 1), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0, 0.75, 0, 1, 0.875, 1), BooleanOp.OR);
		shape = Shapes.join(shape, Shapes.box(0.6875, 0.875, 0.6875, 0.9375, 1, 0.9375), BooleanOp.OR);

		return shape;
	}

	@Override
	protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(FACING)) {
			case Direction.NORTH -> NORTH_SHAPE;
			case Direction.SOUTH -> SOUTH_SHAPE;
			case Direction.EAST -> EAST_SHAPE;
			case Direction.WEST ->  WEST_SHAPE;
			default -> throw new IllegalArgumentException();
		};
	}

	public SewingTableBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected @NotNull MapCodec<SewingTableBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType pathComputationType) {
		return false;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (!level.isClientSide)
			player.openMenu(state.getMenuProvider(level, pos));

		return InteractionResult.SUCCESS;
	}

	@Nullable
	@Override
	protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		return new SimpleMenuProvider((i, inventory, player) -> new SewingTableMenu(i, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE);
	}
}
