/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.client.screen;

import dev.sweetberry.more_than_a_foxbox.data.PlushieVariant;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.state.gui.pip.PictureInPictureRenderState;
import net.minecraft.core.Holder;
import org.jspecify.annotations.Nullable;

public record GuiPlushieRenderState(Holder<PlushieVariant> variant, PlushieVariant.Pose plushiePose, float yRot, int x0, int y0, int x1, int y1, float scale, @Nullable ScreenRectangle scissorArea, @Nullable ScreenRectangle bounds) implements PictureInPictureRenderState {
	public GuiPlushieRenderState(final Holder<PlushieVariant> variant, final PlushieVariant.Pose plushiePose, final float yRot, final int x0, final int y0, final int x1, final int y1, final float scale, final @Nullable ScreenRectangle scissorArea) {
		this(variant, plushiePose, yRot, x0, y0, x1, y1, scale, scissorArea, PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea));
	}
}
