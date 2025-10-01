/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.client.block.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.sweetberry.more_than_a_foxbox.MoreThanAFoxbox;
import dev.sweetberry.more_than_a_foxbox.block.BoxBlock;
import dev.sweetberry.more_than_a_foxbox.block.entity.BoxBlockEntity;
import dev.sweetberry.more_than_a_foxbox.block.entity.PlushieHoldingBlockEntity;
import dev.sweetberry.more_than_a_foxbox.block.property.MtfbBlockProperties;
import dev.sweetberry.more_than_a_foxbox.client.MoreThanAFoxboxClient;
import dev.sweetberry.more_than_a_foxbox.util.OctalDirection;
import net.fabricmc.fabric.api.renderer.v1.render.RenderLayerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ShelfRenderer;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static dev.sweetberry.more_than_a_foxbox.block.entity.PlushieHoldingBlockEntity.SCALE_SLOWNESS;
import static dev.sweetberry.more_than_a_foxbox.block.entity.PlushieHoldingBlockEntity.SQUISH_TIME;
import static dev.sweetberry.more_than_a_foxbox.block.entity.PlushieHoldingBlockEntity.STRETCH_TIME;

public class PlushieBlockEntityRenderer implements BlockEntityRenderer<PlushieHoldingBlockEntity, PlushieHoldingBlockEntityRenderState> {
	private final Map<ResourceLocation, BlockStateModel> models = new HashMap<>();
	private final BlockEntityRendererProvider.Context renderContext;
	
	public PlushieBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.renderContext = context;
	}

	@Override
	public @NotNull PlushieHoldingBlockEntityRenderState createRenderState() {
		return new PlushieHoldingBlockEntityRenderState();
	}

	@Override
	public void extractRenderState(PlushieHoldingBlockEntity blockEntity, PlushieHoldingBlockEntityRenderState state, float partialTick, Vec3 vec3, @Nullable ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTick, vec3, crumblingOverlay);

		state.deltaStretch = blockEntity.getDeltaStretch();
		state.deltaSquish = blockEntity.getDeltaSquish();

		if (state.deltaStretch < 0.0f) {
			blockEntity.setDeltaStretch(state.deltaStretch + partialTick / SCALE_SLOWNESS);
		}
		if (blockEntity.getDeltaSquish() < SQUISH_TIME) {
			blockEntity.setDeltaSquish(blockEntity.getDeltaSquish() + partialTick / SCALE_SLOWNESS);
		}

		state.model = null;

		state.isBox = blockEntity instanceof BoxBlockEntity;

		Optional<ResourceLocation> optionalPoseModel = blockEntity.getPoseModel(blockEntity.getBlockState());
		ResourceLocation poseModel;

		if (blockEntity instanceof BoxBlockEntity && optionalPoseModel.isEmpty())
			return;

		poseModel = optionalPoseModel.orElseGet(() -> MoreThanAFoxbox.id(
			MoreThanAFoxbox.ID + "/placeholder"));
		ModelManager modelManager = Minecraft.getInstance().getModelManager();
		state.model = models.computeIfAbsent(
			poseModel,
			asset -> modelManager.getModel(MoreThanAFoxboxClient.MODEL_KEYS.get(asset))
		);

		if (state.model == null)
			state.model = modelManager.getModel(MoreThanAFoxboxClient.MODEL_KEYS.get(ResourceLocation.fromNamespaceAndPath(MoreThanAFoxbox.ID, MoreThanAFoxbox.ID + "/placeholder")));

		if (state.model == null)
			MoreThanAFoxbox.LOGGER.error("Cannot find placeholder plushie.");
	}

	@Override
	public void submit(PlushieHoldingBlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
		if (state.model == null)
			return;

		poseStack.pushPose();

		OctalDirection direction = state.blockState.getValue(
			MtfbBlockProperties.FACING);
		Matrix4f rotationTransform = BoxBlock.pointBlockToward(direction, 0.5f);
		poseStack.mulPose(rotationTransform);

		// Stretch & Squish
		float stretch = 1.0f - state.deltaStretch / STRETCH_TIME;
		float squish = state.deltaSquish / SQUISH_TIME;

		poseStack.translate(scaleInPlace(stretch), 0.0f, 0.0f);
		if (state.isBox)
			poseStack.translate(0.0f, scaleInPlace(squish), 0.0f);

		poseStack.scale(stretch, squish, 1.0f);

		submitNodeCollector
			.submitBlockModel(
				poseStack,
				Sheets.translucentItemSheet(),
				state.model,
				1,
				1,
				1,
				state.lightCoords,
				OverlayTexture.NO_OVERLAY,
				0
			);

		poseStack.popPose();
	}

	private float scaleInPlace(float scale) {
		return 1.0f - scale / 2.0f - 0.5f;
	}
}
