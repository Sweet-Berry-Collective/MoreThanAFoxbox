/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.network.clientbound;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public class ClientboundSewingTablePlushies implements CustomPacketPayload {
	public static final Type<ClientboundSewingTablePlushies> TYPE = new Type<>(
		MoreThanAFoxbox.id("sewing_table_plushies"));
	public static final ClientboundSewingTablePlushies INSTANCE = new ClientboundSewingTablePlushies();
	public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSewingTablePlushies> STREAM_CODEC = StreamCodec.unit(INSTANCE);

	protected ClientboundSewingTablePlushies() {}

	@Override
	public @NotNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
