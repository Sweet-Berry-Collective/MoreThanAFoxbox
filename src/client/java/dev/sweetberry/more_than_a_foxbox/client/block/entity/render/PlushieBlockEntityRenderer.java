/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.client.block.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.sweetberry.more_than_a_foxbox.block.BoxBlock;
import dev.sweetberry.more_than_a_foxbox.block.entity.PlushieHoldingBlockEntity;
import dev.sweetberry.more_than_a_foxbox.block.property.MtfbBlockProperties;
import dev.sweetberry.more_than_a_foxbox.util.OctalDirection;
import net.fabricmc.fabric.api.client.model.loading.v1.ExtraModelKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class PlushieBlockEntityRenderer implements BlockEntityRenderer<PlushieHoldingBlockEntity> {
	private final Map<ClientAsset, ModelPart> models = new HashMap<>();
	
	public PlushieBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}
	
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
		Optional<ClientAsset> optionalPoseModel = blockEntity.getPoseModel(blockEntity.getBlockState());
		if (optionalPoseModel.isEmpty()) return;
		ClientAsset poseModel = optionalPoseModel.get();
		ModelManager modelManager = Minecraft.getInstance().getModelManager();
		ModelPart modelPart = Objects.requireNonNull(
			models.computeIfAbsent(
				poseModel,
				asset -> modelManager.getModel(ExtraModelKey.create(asset.id()::toString))
			),
			"Computed plushie model should not be null"
		);
		
		poseStack.pushPose();
		
		OctalDirection direction = blockEntity.getBlockState().getValue(
			MtfbBlockProperties.FACING);
		Matrix4f rotationTransform = BoxBlock.pointBlockToward(direction, 0.5f);
		poseStack.mulPose(rotationTransform);
		
		modelPart.render(
			poseStack,
			bufferSource.getBuffer(RenderType.cutout()), 
			packedLight,
			packedOverlay
		);
		
		poseStack.popPose();
	}
}
