/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.sweetberry.more_than_a_foxbox.data.PlushieVariant;
import dev.sweetberry.more_than_a_foxbox.registry.MtfbRegistries;
import dev.sweetberry.more_than_a_foxbox.sound.MtfbSounds;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Optional;

public record PlushieDataComponent(
	ResourceKey<PlushieVariant> variant,
	Optional<SoundType> soundType
) {
	public static final Codec<PlushieDataComponent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
		ResourceKey.codec(MtfbRegistries.PLUSHIE_VARIANT).fieldOf("variant").forGetter(PlushieDataComponent::variant),
		SoundType.CODEC.optionalFieldOf("sound_type").forGetter(PlushieDataComponent::soundType)
	).apply(inst, PlushieDataComponent::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, PlushieDataComponent> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

	public Optional<SoundEvent> getInteractionSound(RegistryAccess access) {
		var maybeSoundType = soundType;

		if (maybeSoundType.isEmpty())
			return Optional.empty();

		var soundType = maybeSoundType.get();

		if (soundType == SoundType.SQUEAKER)
			return Optional.of(MtfbSounds.SQUEAK.get());

		var variant = access.get(this.variant);

		return variant.map(plushieVariantReference -> plushieVariantReference
			.value()
			.mobSounds()
			.value());
	}

	public enum SoundType implements StringRepresentable {
		SQUEAKER,
		SPEAKER;

		public static final EnumCodec<SoundType> CODEC = StringRepresentable.fromEnum(SoundType::values);

		@Override
		public @NotNull String getSerializedName() {
			return toString().toLowerCase(Locale.ROOT);
		}
	}
}
