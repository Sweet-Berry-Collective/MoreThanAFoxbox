/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.network.clientbound;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record ClientboundPlushieSquish(BlockPos pos) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<ClientboundPlushieSquish> TYPE = new CustomPacketPayload.Type<>(
		MoreThanAFoxbox.id("plushie_squish"));
	public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundPlushieSquish> STREAM_CODEC = StreamCodec.composite(
		BlockPos.STREAM_CODEC,
		ClientboundPlushieSquish::pos,
		ClientboundPlushieSquish::new
	);

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
