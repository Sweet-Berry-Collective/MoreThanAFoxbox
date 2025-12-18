/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.registry;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.data.PlushieVariant;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import net.fabricmc.fabric.api.event.registry.DynamicRegistries;

public class MtfbRegistries {
	public static final ResourceKey<Registry<PlushieVariant>> PLUSHIE_VARIANT = create("plushie_variant");

	private static <T> ResourceKey<Registry<T>> create(String path) {
		return ResourceKey.createRegistryKey(MoreThanAFoxbox.id(path));
	}

	public static void register() {
		DynamicRegistries.registerSynced(MtfbRegistries.PLUSHIE_VARIANT, PlushieVariant.DIRECT_CODEC, PlushieVariant.NETWORK_CODEC);
	}
}
