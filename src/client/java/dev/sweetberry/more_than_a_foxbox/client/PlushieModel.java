/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.client;

import com.mojang.serialization.MapCodec;
import dev.sweetberry.more_than_a_foxbox.data.MtfbComponents;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class PlushieModel implements ItemModel {
	public static final PlushieModel INSTANCE = new PlushieModel();

	@Override
	public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver itemModelResolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
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
		public @NotNull ItemModel bake(BakingContext context) {
			return INSTANCE;
		}

		@Override
		public void resolveDependencies(Resolver resolver) {}
	}
}
