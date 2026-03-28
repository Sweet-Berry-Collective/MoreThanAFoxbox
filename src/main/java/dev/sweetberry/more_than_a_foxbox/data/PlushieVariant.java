/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.data;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.registry.MtfbRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.StringRepresentable;

public record PlushieVariant(
	Holder<SoundEvent> mobSounds,
	Poses poses
) {
	public static final ResourceKey<PlushieVariant> PLACEHOLDER = ResourceKey.create(MtfbRegistries.PLUSHIE_VARIANT, MoreThanAFoxbox.id("placeholder"));

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

	public static final TagKey<PlushieVariant> ITEM_ORDER = key("item_order");
	public static final TagKey<PlushieVariant> EXCLUDED_IN_SEWING_TABLE = key("excluded_in_sewing_table");

	public record Poses(
		Identifier sit,
		Identifier stand,
		Identifier lay,
		Identifier box
	) {
		public static final Codec<Poses> DIRECT_CODEC = RecordCodecBuilder.create(inst -> inst.group(
			Identifier.CODEC.fieldOf("sit").forGetter(Poses::sit),
			Identifier.CODEC.fieldOf("stand").forGetter(Poses::stand),
			Identifier.CODEC.fieldOf("lay").forGetter(Poses::lay),
			Identifier.CODEC.fieldOf("box").forGetter(Poses::box)
		).apply(inst, Poses::new));

		public static final Codec<Poses> NETWORK_CODEC = RecordCodecBuilder.create(inst -> inst.group(
			Identifier.CODEC.fieldOf("sit").forGetter(Poses::sit),
			Identifier.CODEC.fieldOf("stand").forGetter(Poses::stand),
			Identifier.CODEC.fieldOf("lay").forGetter(Poses::lay),
			Identifier.CODEC.fieldOf("box").forGetter(Poses::box)
		).apply(inst, Poses::new));
		
		public Identifier getModelFromPose(Pose pose) {
			return switch (pose) {
				case SIT -> sit;
				case STAND -> stand;
				case LAY -> lay;
			};
		}
	}

	public static List<Holder.Reference<PlushieVariant>> orderedPlushies(HolderLookup.Provider lookupProvider, Predicate<Holder.Reference<PlushieVariant>> filterPredicate) {
		var optionalRegistry = lookupProvider.lookup(PlushieVariant.PLACEHOLDER.registryKey());
		if (optionalRegistry.isEmpty()) {
			return Collections.emptyList();
		}

		var registry = optionalRegistry.get();
		var tagAsList = registry.get(PlushieVariant.ITEM_ORDER)
			.filter(HolderSet.Named::isBound)
			.map(holders -> holders.stream().toList())
			.orElse(Collections.emptyList());

		return registry.listElements()
			.filter(filterPredicate)
			.sorted((p1, p2) -> {
				int p1Order = tagAsList.indexOf(p1);
				int p2Order = tagAsList.indexOf(p2);

				// If both are inside the tag, order them based on index.
				if (p1Order != -1 && p2Order != -1) {
					return Integer.compare(p1Order, p2Order);
				}

				// If only PlushieVariant 1 is inside the tag, use it first.
				if (p1Order != -1) {
					return -1;
				}

				// If only PlushieVariant 2 is inside the tag, use it first.
				if (p2Order != -1) {
					return 1;
				}

				// Otherwise, compare the IDs of the plushies.
				return p1.key().identifier().compareTo(p2.key().identifier());
			})
			.toList();
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

	private static TagKey<PlushieVariant> key(String name) {
		return TagKey.create(PLACEHOLDER.registryKey(), MoreThanAFoxbox.id(name));
	}
}
