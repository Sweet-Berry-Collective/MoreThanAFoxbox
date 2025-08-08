/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.ClientAsset;
import net.minecraft.core.Holder;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.Optional;

public record Pose(ClientAsset model, Optional<Holder<LootItemCondition>> condition) {
	private static final Codec<ClientAsset> MODEL_CODEC = ResourceLocation.CODEC.xmap(id -> new ClientAsset(id, id.withPath(path -> "more_than_a_fox_box/" + path)), ClientAsset::id);

	public static final Codec<Pose> CODEC = RecordCodecBuilder.create(inst -> inst.group(
		ClientAsset.CODEC
			.fieldOf("model_id")
			.forGetter(Pose::model),
		LootItemCondition.CODEC
			.optionalFieldOf("condition")
			.forGetter(Pose::condition)
	).apply(inst, Pose::new));
	public static final StreamCodec<ByteBuf, Pose> STREAM_CODEC = ClientAsset.STREAM_CODEC
		.map(Pose::new, Pose::model);

	// Used for dynamic registry syncing.
	public static final Codec<Pose> NETWORK_CODEC = RecordCodecBuilder.create(inst -> inst.group(
		ClientAsset.CODEC
			.fieldOf("model_id")
			.forGetter(Pose::model)
	).apply(inst, Pose::new));

	public Pose(ClientAsset model) {
		this(model, Optional.empty());
	}
}
