/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.client.util;

import dev.sweetberry.more_than_a_foxbox.data.PlushieVariant;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ModelUtil {
	public static CompletableFuture<List<ResourceLocation>> getModels(ResourceManager manager, Executor executor) {
		FileToIdConverter fileToIdConverter = FileToIdConverter.json("models/more_than_a_fox_box");
		return CompletableFuture.supplyAsync(() -> fileToIdConverter.listMatchingResources(manager)
			.keySet()
			.stream().map(resourceLocation ->
				resourceLocation.withPath(s -> s.substring(7, s.length() - 5)))
			.toList(), executor);
	}

	public static SimpleUnbakedExtraModel<BlockStateModel> getPlushieModel(PlushieVariant variant, ResourceLocation activePose) {
		return Minecraft.getInstance().getModelManager().getModel(createModelKey(variant, activePose));
	}

	private static ExtraModelKey<SimpleUnbakedExtraModel<BlockStateModel>> createModelKey(PlushieVariant variant, ResourceLocation activePose) {
		return ExtraModelKey.create(() -> variant.poses().get(activePose).model().texturePath().toString());
	}
}
