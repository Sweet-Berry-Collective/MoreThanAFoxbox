/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.block;

import com.mojang.serialization.MapCodec;
import dev.sweetberry.more_than_a_foxbox.block.entity.BoxBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BoxBlock extends BaseEntityBlock {
	public static final MapCodec<BoxBlock> CODEC = simpleCodec(BoxBlock::new);
	
	public BoxBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public @Nullable BlockEntity newBlockEntity(
		BlockPos pos,
		BlockState state
	) {
		return new BoxBlockEntity(pos, state);
	}
}
