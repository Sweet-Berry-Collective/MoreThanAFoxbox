/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.client.block.entity.render;

import org.jetbrains.annotations.Nullable;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class PlushieHoldingBlockEntityRenderState extends BlockEntityRenderState {
	@Nullable BlockStateModel model = null;
	float deltaStretch, deltaSquish;
	boolean isBox;
}
