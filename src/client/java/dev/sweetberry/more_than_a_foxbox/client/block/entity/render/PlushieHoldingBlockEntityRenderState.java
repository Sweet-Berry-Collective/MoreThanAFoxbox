/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.client.block.entity.render;

import dev.sweetberry.more_than_a_foxbox.util.OctalDirection;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;

public class PlushieHoldingBlockEntityRenderState extends BlockEntityRenderState {
	BlockModelRenderState model = new BlockModelRenderState();
	OctalDirection direction;
	float deltaStretch, deltaSquish;
	boolean isBox;
}
