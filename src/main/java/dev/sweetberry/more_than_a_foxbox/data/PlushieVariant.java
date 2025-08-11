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
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public record PlushieVariant(
	Holder<SoundEvent> mobSounds,
	Poses poses
) {
	public static final Codec<PlushieVariant> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
		SoundEvent.CODEC
			.fieldOf("mob_sounds")
			.forGetter(PlushieVariant::mobSounds),
		Poses.DIRECT_CODEC
			.fieldOf("poses")
			.forGetter(PlushieVariant::poses)
	).apply(inst, PlushieVariant::new));

	public static final Codec<PlushieVariant> NETWORK_CODEC = RecordCodecBuilder.create(inst -> inst.group(
		SoundEvent.CODEC
			.fieldOf("mob_sounds")
			.forGetter(PlushieVariant::mobSounds),
		Poses.NETWORK_CODEC
			.fieldOf("poses")
			.forGetter(PlushieVariant::poses)
	).apply(inst, PlushieVariant::new));

	public static final Codec<Holder<PlushieVariant>> CODEC = RegistryFixedCodec.create(MtfbRegistries.PLUSHIE_VARIANT);
	public static final StreamCodec<RegistryFriendlyByteBuf, Holder<PlushieVariant>> STREAM_CODEC = ByteBufCodecs.holderRegistry(MtfbRegistries.PLUSHIE_VARIANT);

	public record Poses(
		ResourceLocation sit,
		ResourceLocation stand,
		ResourceLocation lay,
		ResourceLocation box
	) {
		public static final Codec<Poses> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
			ResourceLocation.CODEC.fieldOf("sit").forGetter(Poses::sit),
			ResourceLocation.CODEC.fieldOf("stand").forGetter(Poses::stand),
			ResourceLocation.CODEC.fieldOf("lay").forGetter(Poses::lay),
			ResourceLocation.CODEC.fieldOf("box").forGetter(Poses::box)
		).apply(inst, Poses::new));

		public static final Codec<Poses> NETWORK_CODEC = RecordCodecBuilder.create(inst -> inst.group(
			ResourceLocation.CODEC.fieldOf("sit").forGetter(Poses::sit),
			ResourceLocation.CODEC.fieldOf("stand").forGetter(Poses::stand),
			ResourceLocation.CODEC.fieldOf("lay").forGetter(Poses::lay),
			ResourceLocation.CODEC.fieldOf("box").forGetter(Poses::box)
		).apply(inst, Poses::new));
		
		public ResourceLocation getModelFromPose(Pose pose) {
			return switch (pose) {
				case SIT -> sit;
				case STAND -> stand;
				case LAY -> lay;
			};
		}
	}
	
	public enum Pose implements StringRepresentable {
		SIT,
		STAND,
		LAY;

		@Override
		public @NotNull String getSerializedName() {
			return this.name().toLowerCase(Locale.ROOT);
		}
	}
}
