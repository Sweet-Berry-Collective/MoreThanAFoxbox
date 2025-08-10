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
import net.minecraft.resources.ResourceKey;
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
