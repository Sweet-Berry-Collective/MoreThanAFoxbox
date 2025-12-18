/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.client.util;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import org.jetbrains.annotations.NotNull;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;

public class ModelUtil {
	public static CompletableFuture<List<Identifier>> getPlushieModels(PreparableReloadListener.SharedState state, Executor executor) {
		return getModels(MoreThanAFoxbox.ID, state, executor);
	}
	
	public static CompletableFuture<List<Identifier>> getBoxModels(PreparableReloadListener.SharedState state, Executor executor) {
		return getModels("block/cardboard_box", state, executor);
	}

	private static @NotNull CompletableFuture<List<Identifier>> getModels(
		String path,
		PreparableReloadListener.SharedState state,
		Executor executor
	) {
		FileToIdConverter fileToIdConverter = FileToIdConverter.json("models/" + path);
		return CompletableFuture.supplyAsync(
			() -> fileToIdConverter.listMatchingResources(state.resourceManager())
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
