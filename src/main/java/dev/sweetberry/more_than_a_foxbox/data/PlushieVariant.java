/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.sweetberry.more_than_a_foxbox.registry.MtfbRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public record PlushieVariant(Map<ResourceLocation, Pose> poses) {
	public static final Codec<PlushieVariant> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
		Codec.unboundedMap(ResourceLocation.CODEC, Pose.CODEC)
			.fieldOf("poses")
			.forGetter(PlushieVariant::poses)
	).apply(inst, PlushieVariant::new));

	// Used for dynamic registry syncing.
	public static final Codec<PlushieVariant> NETWORK_CODEC = RecordCodecBuilder.create(inst -> inst.group(
		Codec.unboundedMap(ResourceLocation.CODEC, Pose.NETWORK_CODEC)
			.fieldOf("poses")
			.forGetter(PlushieVariant::poses)
	).apply(inst, PlushieVariant::new));

	public static final Codec<Holder<PlushieVariant>> CODEC = RegistryFixedCodec.create(MtfbRegistries.PLUSHIE_VARIANT);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<PlushieVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(MtfbRegistries.PLUSHIE_VARIANT);
}
