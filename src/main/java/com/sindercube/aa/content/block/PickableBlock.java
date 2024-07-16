package com.sindercube.aa.content.block;

import com.mojang.serialization.MapCodec;
import com.sindercube.aa.content.blockEntity.PickableBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PickableBlock extends BlockWithEntity {

    public static final int TICK_DELAY = 8;

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return createCodec(PickableBlock::new);
    }


    public static final IntProperty PICKED = IntProperty.of("picked", 0, 9);

    public PickableBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(PICKED, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PICKED);
    }


    public static void updateState(World world, BlockPos pos, int picksCount, int maxPicks) {
        BlockState state = world.getBlockState(pos);
        int pickedValue;
        if (maxPicks == picksCount) pickedValue = 9;
        else {
            pickedValue = (int)Math.ceil(((double)picksCount / (double)maxPicks) * 9);
            if (pickedValue > 0) pickedValue--;
        }
        if (state.get(PICKED) == pickedValue) return;

        world.setBlockState(pos, state.with(PICKED, pickedValue));
    }


    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        var entity = PickableBlockEntity.find(world, pos);
        if (entity != null) entity.tick();
        world.scheduleBlockTick(pos, this, TICK_DELAY);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new PickableBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

}
