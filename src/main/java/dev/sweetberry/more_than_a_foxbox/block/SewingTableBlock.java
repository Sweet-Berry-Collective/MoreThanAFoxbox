package dev.sweetberry.more_than_a_foxbox.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class SewingTableBlock extends Block {
	public static final MapCodec<SewingTableBlock> CODEC = simpleCodec(SewingTableBlock::new);

	public SewingTableBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected @NotNull MapCodec<SewingTableBlock> codec() {
		return CODEC;
	}
}
