/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.client;

import com.mojang.serialization.MapCodec;
import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.data.MtfbComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlushieModel implements ItemModel {
	public static final PlushieModel INSTANCE = new PlushieModel();

	private static final Matrix4fc IDENTITY = new Matrix4f();
	private static final Map<Identifier, BlockStateModel> MODEL_CACHE = new HashMap<>();

	public static void updatePlushieModelState(BlockModelRenderState state, Identifier poseModel) {
		BlockStateModel model;
		ModelManager modelManager = Minecraft.getInstance().getModelManager();

		model = MODEL_CACHE.computeIfAbsent(
			poseModel,
			asset -> modelManager.getModel(MoreThanAFoxboxClient.MODEL_KEYS.get(asset))
		);

		if (model == null) {
			Identifier placeholderId = Identifier.fromNamespaceAndPath(MoreThanAFoxbox.ID, MoreThanAFoxbox.ID + "/placeholder");
			model = MODEL_CACHE.computeIfAbsent(
				placeholderId,
				asset -> modelManager.getModel(MoreThanAFoxboxClient.MODEL_KEYS.get(asset))
			);
		}

		if (model == null) {
			MoreThanAFoxbox.LOGGER.error("Can not find placeholder plushie.");
			return;
		}

		state.clear();
		model.collectParts(state.scratchRandomSource(42L), state.setupModel(IDENTITY, false));
	}

	@Override
	public void update(@NonNull ItemStackRenderState renderState, ItemStack stack, @NonNull ItemModelResolver itemModelResolver, @NonNull ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
		var plushie = stack.get(MtfbComponents.PLUSHIE.get());

		if (plushie == null)
			return;

		var modelManager = Minecraft.getInstance().getModelManager();
		var itemModel = modelManager.getItemModel(plushie.variant().identifier().withPrefix("more_than_a_foxbox/"));
		itemModel.update(renderState, stack, itemModelResolver, displayContext, level, owner, seed);
	}

	public record Unbaked() implements ItemModel.Unbaked {
		public static final MapCodec<ItemModel.Unbaked> CODEC = MapCodec.unit(new Unbaked());

		@Override
		public @NotNull MapCodec<? extends ItemModel.Unbaked> type() {
			return CODEC;
		}

		@Override
		public @NonNull ItemModel bake(BakingContext context, Matrix4fc transformation) {
			return INSTANCE;
		}

		@Override
		public void resolveDependencies(Resolver resolver) {}
	}
}
