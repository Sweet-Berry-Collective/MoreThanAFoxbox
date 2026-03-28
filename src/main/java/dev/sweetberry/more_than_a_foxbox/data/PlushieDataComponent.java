/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.data;

import java.util.Locale;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.sweetberry.more_than_a_foxbox.registry.MtfbRegistries;
import dev.sweetberry.more_than_a_foxbox.sound.MtfbSounds;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.StringRepresentable;
import org.jspecify.annotations.Nullable;

public record PlushieDataComponent(
	ResourceKey<PlushieVariant> variant,
	@Nullable SoundType soundType
) {
	public static final Codec<PlushieDataComponent> CODEC = RecordCodecBuilder.create(inst -> inst.group(
		ResourceKey.codec(MtfbRegistries.PLUSHIE_VARIANT).fieldOf("variant").forGetter(PlushieDataComponent::variant),
		SoundType.CODEC.optionalFieldOf("sound_type").forGetter(c -> Optional.ofNullable(c.soundType()))
	).apply(inst, (variant, soundType) -> new PlushieDataComponent(variant, soundType.orElse(null))));
	public static final StreamCodec<RegistryFriendlyByteBuf, PlushieDataComponent> STREAM_CODEC = ByteBufCodecs.fromCodecWithRegistries(CODEC);

	public PlushieDataComponent(ResourceKey<PlushieVariant> variant) {
		this(variant, null);
	}

	public @Nullable SoundEvent getInteractionSound(RegistryAccess access) {
		if (soundType == null)
			return null;

		if (soundType == SoundType.SQUEAKER)
			return MtfbSounds.SQUEAK.get();

		var variant = access.get(this.variant);

		return variant.map(plushieVariantReference -> plushieVariantReference
			.value()
			.mobSounds()
			.value())
			.orElse(null);
	}

	public MutableComponent getDisplayName() {
		return Component.translatable("item.more_than_a_foxbox.plushie." + variant.identifier().toLanguageKey());
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
