/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class PolyfillBlock extends Block {
	public static final MapCodec<PolyfillBlock> CODEC = simpleCodec(PolyfillBlock::new);

	public PolyfillBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected @NotNull MapCodec<? extends Block> codec() {
		return CODEC;
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier insideBlockEffectApplier, boolean bl) {
		entity.makeStuckInBlock(state, new Vec3(0.5, 0.5, 0.5));
	}
}
