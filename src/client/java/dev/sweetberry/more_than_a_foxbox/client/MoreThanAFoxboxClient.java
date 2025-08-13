/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.client;

import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.block.BoxBlock;
import dev.sweetberry.more_than_a_foxbox.block.MtfbBlocks;
import dev.sweetberry.more_than_a_foxbox.block.entity.MtfbBlockEntityTypes;
import dev.sweetberry.more_than_a_foxbox.block.property.MtfbBlockProperties;
import dev.sweetberry.more_than_a_foxbox.client.block.entity.render.PlushieBlockEntityRenderer;
import dev.sweetberry.more_than_a_foxbox.client.screen.SewingTableScreen;
import dev.sweetberry.more_than_a_foxbox.client.util.ModelUtil;
import dev.sweetberry.more_than_a_foxbox.menu.MtfbMenus;
import dev.sweetberry.more_than_a_foxbox.util.OctalDirection;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.fabricmc.fabric.api.client.model.loading.v1.PreparableModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.SimpleUnbakedExtraModel;
import net.fabricmc.fabric.api.client.model.loading.v1.wrapper.WrapperBlockStateModel;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.item.ItemModels;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class MoreThanAFoxboxClient implements ClientModInitializer {
	public static final Map<ResourceLocation, ExtraModelKey<BlockStateModel>> MODEL_KEYS = new HashMap<>();
	
	@Override
	public void onInitializeClient() {
		PreparableModelLoadingPlugin.register(
			ModelUtil::getPlushieModels,
			(data, pluginContext) -> {
				for (ResourceLocation id : data) {
					ExtraModelKey<BlockStateModel> modelKey = ExtraModelKey.create(id::toString);
					MODEL_KEYS.put(id, modelKey);
					pluginContext.addModel(modelKey, SimpleUnbakedExtraModel.blockStateModel(id));
				}
			}
		);

		ItemModels.ID_MAPPER.put(MoreThanAFoxbox.id("plushie"), PlushieModel.Unbaked.CODEC);
		
		// Box Rotation
		PreparableModelLoadingPlugin.register(
			ModelUtil::getBoxModels,
			(data, pluginContext) -> {
				for (ResourceLocation id : data) {
					pluginContext.addModel(ExtraModelKey.create(id::toString), SimpleUnbakedExtraModel.blockStateModel(id));
				}
				
				pluginContext.modifyBlockModelAfterBake().register((model, context) -> {
					BlockState state = context.state();
					if (!state.is(MtfbBlocks.CARDBOARD_BOX.get())) return model;
					OctalDirection direction = state.getValue(
						MtfbBlockProperties.FACING);

					return new WrapperBlockStateModel(model) {
						@Override
						public void emitQuads(
							QuadEmitter emitter,
							BlockAndTintGetter blockView,
							BlockPos pos,
							BlockState state,
							RandomSource random,
							Predicate<@Nullable Direction> cullTest
						) {
							var rotationTransform = BoxBlock.pointBlockToward(direction, 0.5f);
							emitter.pushTransform(quad -> {
								for (int i = 0; i < 4; i++) {
									var point = new Vector3f();
									quad.copyPos(i, point);
									var point4 = new Vector4f(point.x, point.y, point.z, 1.0f);
									rotationTransform.transform(point4);
									point.set(point4);
									quad.pos(i, point);
								}
								return true;
							});
							super.emitQuads(
								emitter,
								blockView,
								pos,
								state,
								random,
								cullTest
							);
							emitter.popTransform();
						}
					};
				});
			}
		);

		BlockRenderLayerMap.putBlocks(
			ChunkSectionLayer.CUTOUT,
			MtfbBlocks.CARDBOARD_BOX.get()
		);

		BlockEntityRenderers.register(
			MtfbBlockEntityTypes.CARDBOARD_BOX.get(),
			PlushieBlockEntityRenderer::new
		);

		BlockEntityRenderers.register(
			MtfbBlockEntityTypes.PLUSHIE.get(),
			PlushieBlockEntityRenderer::new
		);

		MenuScreens.register(MtfbMenus.SEWING_TABLE.get(), SewingTableScreen::new);
	}
}
