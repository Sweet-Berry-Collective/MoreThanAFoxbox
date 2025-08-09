/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.sweetberry.more_than_a_foxbox.registry.MtfbRegistries;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;

import java.util.Optional;

public record PlushieVariant(
	ResourceKey<SoundEvent> sounds,
	Optional<Poses> poses
) {
	public static final Codec<PlushieVariant> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
		ResourceKey.codec(Registries.SOUND_EVENT)
			.fieldOf("mob_sounds")
			.forGetter(PlushieVariant::sounds),
		Poses.DIRECT_CODEC
			.optionalFieldOf("poses")
			.forGetter(PlushieVariant::poses)
	).apply(inst, PlushieVariant::new));

	public static final Codec<PlushieVariant> NETWORK_CODEC = RecordCodecBuilder.create(inst -> inst.group(
		ResourceKey.codec(Registries.SOUND_EVENT)
			.fieldOf("mob_sounds")
			.forGetter(PlushieVariant::sounds),
		Poses.NETWORK_CODEC
			.optionalFieldOf("poses")
			.forGetter(PlushieVariant::poses)
	).apply(inst, PlushieVariant::new));

	public static final Codec<Holder<PlushieVariant>> CODEC = RegistryFixedCodec.create(MtfbRegistries.PLUSHIE_VARIANT);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<PlushieVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(MtfbRegistries.PLUSHIE_VARIANT);

	public record Poses(
		Optional<ClientAsset> sit,
		Optional<ClientAsset> stand,
		Optional<ClientAsset> lay,
		Optional<ClientAsset> box
	) {
		public static final Codec<Poses> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
			ClientAsset.CODEC.optionalFieldOf("sit").forGetter(Poses::sit),
			ClientAsset.CODEC.optionalFieldOf("stand").forGetter(Poses::stand),
			ClientAsset.CODEC.optionalFieldOf("lay").forGetter(Poses::lay),
			ClientAsset.CODEC.optionalFieldOf("box").forGetter(Poses::box)
		).apply(inst, Poses::new));

		public static final Codec<Poses> NETWORK_CODEC = RecordCodecBuilder.create(inst -> inst.group(
			ClientAsset.CODEC.optionalFieldOf("sit").forGetter(Poses::sit),
			ClientAsset.CODEC.optionalFieldOf("stand").forGetter(Poses::stand),
			ClientAsset.CODEC.optionalFieldOf("lay").forGetter(Poses::lay),
			ClientAsset.CODEC.optionalFieldOf("box").forGetter(Poses::box)
		).apply(inst, Poses::new));
	}
}
