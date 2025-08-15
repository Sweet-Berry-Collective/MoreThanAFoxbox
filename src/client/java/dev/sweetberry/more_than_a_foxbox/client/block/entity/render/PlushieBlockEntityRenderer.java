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
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static dev.sweetberry.more_than_a_foxbox.block.entity.PlushieHoldingBlockEntity.SCALE_SLOWNESS;
import static dev.sweetberry.more_than_a_foxbox.block.entity.PlushieHoldingBlockEntity.SQUISH_TIME;
import static dev.sweetberry.more_than_a_foxbox.block.entity.PlushieHoldingBlockEntity.STRETCH_TIME;

public class PlushieBlockEntityRenderer implements BlockEntityRenderer<PlushieHoldingBlockEntity> {
	private final Map<ResourceLocation, BlockStateModel> models = new HashMap<>();
	private final BlockEntityRendererProvider.Context renderContext;
	
	public PlushieBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		this.renderContext = context;
	}
	
	@Override
	public void render(
		PlushieHoldingBlockEntity blockEntity,
		float partialTick,
		PoseStack poseStack,
		MultiBufferSource bufferSource,
		int packedLight,
		int packedOverlay,
		Vec3 cameraPos
	) {
		Optional<ResourceLocation> optionalPoseModel = blockEntity.getPoseModel(blockEntity.getBlockState());
		ResourceLocation poseModel;

		if (blockEntity instanceof BoxBlockEntity && optionalPoseModel.isEmpty())
			return;

		poseModel = optionalPoseModel.orElseGet(() -> MoreThanAFoxbox.id(
			MoreThanAFoxbox.ID + "/placeholder"));
		ModelManager modelManager = Minecraft.getInstance().getModelManager();
		BlockStateModel blockStateModel = models.computeIfAbsent(
				poseModel,
			asset -> modelManager.getModel(MoreThanAFoxboxClient.MODEL_KEYS.get(asset))
		);

		if (blockStateModel == null)
			blockStateModel = modelManager.getModel(MoreThanAFoxboxClient.MODEL_KEYS.get(ResourceLocation.fromNamespaceAndPath(MoreThanAFoxbox.ID, MoreThanAFoxbox.ID + "/placeholder")));

		if (blockStateModel == null) {
			MoreThanAFoxbox.LOGGER.error("Cannot find placeholder plushie.");

			return;
		}
		
		poseStack.pushPose();
		
		OctalDirection direction = blockEntity.getBlockState().getValue(
			MtfbBlockProperties.FACING);
		Matrix4f rotationTransform = BoxBlock.pointBlockToward(direction, 0.5f);
		poseStack.mulPose(rotationTransform);

		// Stretch & Squish
		float stretch = 1.0f - blockEntity.getDeltaStretch() / STRETCH_TIME;
		float squish = blockEntity.getDeltaSquish() / SQUISH_TIME;
		if (blockEntity.getDeltaStretch() < 0.0f) {
			blockEntity.setDeltaStretch(blockEntity.getDeltaStretch() + partialTick / SCALE_SLOWNESS);
		}
		if (blockEntity.getDeltaSquish() < SQUISH_TIME) {
			blockEntity.setDeltaSquish(blockEntity.getDeltaSquish() + partialTick / SCALE_SLOWNESS);
		}
		poseStack.translate(scaleInPlace(stretch), 0.0f, 0.0f);
		if (blockEntity instanceof BoxBlockEntity) {
			poseStack.translate(0.0f, scaleInPlace(squish), 0.0f);
		}
		poseStack.scale(stretch, squish, 1.0f);

		this.renderContext.getBlockRenderDispatcher()
			.getModelRenderer()
			.render(
				blockEntity.getLevel(),
				blockStateModel,
				blockEntity.getBlockState(),
				blockEntity.getBlockPos(),
				poseStack,
				RenderLayerHelper.entityDelegate(bufferSource),
				false,
				blockEntity.getBlockState().getSeed(blockEntity.getBlockPos()),
				packedOverlay
			);
		
		poseStack.popPose();
	}

	private float scaleInPlace(float scale) {
		return 1.0f - scale / 2.0f - 0.5f;
	}
}
