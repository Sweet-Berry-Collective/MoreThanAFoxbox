/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.client.network;

import dev.sweetberry.more_than_a_foxbox.block.entity.PlushieHoldingBlockEntity;
import dev.sweetberry.more_than_a_foxbox.network.clientbound.ClientboundPlushieSquish;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Optional;

public final class MtfbClientNetworking {
	private MtfbClientNetworking() {}
	
	public static void register() {
		ClientPlayNetworking.registerGlobalReceiver(ClientboundPlushieSquish.TYPE, (payload, context) -> {
			Optional<BlockEntity> maybeEntity = Optional.ofNullable(
				context.player().level().getBlockEntity(payload.pos())
			);
			if (maybeEntity.isEmpty() || !(maybeEntity.get() instanceof PlushieHoldingBlockEntity entity))
				return;

			entity.resetStretchSquish();
		});
	}
}
