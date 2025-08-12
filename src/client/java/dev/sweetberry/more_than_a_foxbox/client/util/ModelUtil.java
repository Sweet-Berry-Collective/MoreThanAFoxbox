/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.client.util;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ModelUtil {
	public static CompletableFuture<List<ResourceLocation>> getPlushieModels(ResourceManager manager, Executor executor) {
		return getModels(MoreThanAFoxbox.ID, manager, executor);
	}
	
	public static CompletableFuture<List<ResourceLocation>> getBoxModels(ResourceManager manager, Executor executor) {
		return getModels("block/cardboard_box", manager, executor);
	}

	private static @NotNull CompletableFuture<List<ResourceLocation>> getModels(
		String path,
		ResourceManager manager,
		Executor executor
	) {
		FileToIdConverter fileToIdConverter = FileToIdConverter.json("models/" + path);
		return CompletableFuture.supplyAsync(
			() -> fileToIdConverter.listMatchingResources(manager)
				.keySet()
				.stream().map(resourceLocation ->
					resourceLocation.withPath(s -> s.substring(
						7,
						s.length() - 5
					)))
				.toList(), executor
		);
	}
}
