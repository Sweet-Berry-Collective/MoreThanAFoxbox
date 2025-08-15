/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */

package dev.sweetberry.more_than_a_foxbox.entity;

import dev.sweetberry.more_than_a_foxbox.block.MtfbBlocks;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class BoxSeatEntity extends VehicleEntity {
	public BoxSeatEntity(
		EntityType<?> entityType,
		Level level
	) {
		super(entityType, level);
	}

	@Override
	protected boolean couldAcceptPassenger() {
		return !this.isVehicle();
	}

	@Override
	public void tick() {
		super.tick();

		if (!this.level().isClientSide()) {
			BlockState state = this.level().getBlockState(this.blockPosition());
			if (!state.is(MtfbBlocks.CARDBOARD_BOX.get()) || !this.isVehicle()) {
				this.discard();
			}
		}
	}

	@Override
	public void move(MoverType type, Vec3 movement) {
		if (type == MoverType.PISTON)
			return;
		super.move(type, movement);
	}

	@Override
	public boolean isNoGravity() {
		return true;
	}

	@Override
	protected @NotNull Item getDropItem() {
		return Items.AIR;
	}

	@Override
	protected void readAdditionalSaveData(ValueInput input) {
	}

	@Override
	protected void addAdditionalSaveData(ValueOutput output) {
	}
}
