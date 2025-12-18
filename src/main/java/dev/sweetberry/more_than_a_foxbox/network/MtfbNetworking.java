/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.network;

import dev.sweetberry.more_than_a_foxbox.network.clientbound.ClientboundPlushieSquish;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public final class MtfbNetworking {
	private MtfbNetworking() {
	}
	
	public static void register() {
		PayloadTypeRegistry.playS2C().register(ClientboundPlushieSquish.TYPE, ClientboundPlushieSquish.STREAM_CODEC);
	}
}
