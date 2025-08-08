/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CardboardBoxBlockEntity extends BlockEntity {
	public CardboardBoxBlockEntity(
		BlockPos pos,
		BlockState blockState
	) {
		super(MtfbBlockEntityTypes.CARDBOARD_BOX.get(), pos, blockState);
	}
}
