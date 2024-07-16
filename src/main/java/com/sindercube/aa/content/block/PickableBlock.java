package com.sindercube.aa.content.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sindercube.aa.content.blockEntity.PickableBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PickableBlock extends BlockWithEntity {

    public static final MapCodec<PickableBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Registries.BLOCK.getCodec().fieldOf("turns_into").forGetter(PickableBlock::getBaseBlock),
            createSettingsCodec()
    ).apply(instance, PickableBlock::new));

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    public static final int TICK_DELAY = 8;


    private final Block baseBlock;

    public Block getBaseBlock() {
        return baseBlock;
    }


    public static final IntProperty PICKED = IntProperty.of("picked", 0, 4);

    public PickableBlock(Block baseBlock, Settings settings) {
        super(settings);
        this.baseBlock = baseBlock;
        this.setDefaultState(this.stateManager.getDefaultState().with(PICKED, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(PICKED);
    }


    public static void updateState(World world, BlockPos pos, int picksCount, int maxPicks) {
        BlockState state = world.getBlockState(pos);

        int pickedValue;
        if (maxPicks == picksCount) pickedValue = 4;
        else pickedValue = (int)Math.ceil(((double)picksCount / (double)maxPicks) * 3);

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
