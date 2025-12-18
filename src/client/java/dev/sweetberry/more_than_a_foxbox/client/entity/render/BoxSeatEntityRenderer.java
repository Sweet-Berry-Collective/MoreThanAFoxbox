/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.client.entity.render;

import dev.sweetberry.more_than_a_foxbox.entity.BoxSeatEntity;
import org.jetbrains.annotations.NotNull;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class BoxSeatEntityRenderer extends EntityRenderer<BoxSeatEntity, EntityRenderState> {
	public BoxSeatEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public @NotNull EntityRenderState createRenderState() {
		return new EntityRenderState();
	}
}
