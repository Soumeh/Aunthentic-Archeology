package com.sindercube.aa.content.blockEntity;

import com.sindercube.aa.content.block.PickableBlock;
import com.sindercube.aa.registry.AABlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PickableBlockEntity extends BlockEntity {

    private static final int MAX_PICKS = 15;
    private static final int MIN_PICKS = 25;

    private ItemStack item;
    private int picksCount;
    private int maxPicks;

    @Nullable private Direction hitDirection;
    @Nullable private RegistryKey<LootTable> lootTable;
    private long lootTableSeed = 0;


    @Nullable
    public static PickableBlockEntity find(World world, BlockPos pos) {
        var entity = world.getBlockEntity(pos);
        if (entity instanceof PickableBlockEntity pickable) return pickable;
        return null;
    }

    public PickableBlockEntity(BlockPos pos, BlockState state) {
        super(AABlockEntities.PICKABLE_BLOCK, pos, state);
        this.item = Items.DIAMOND.getDefaultStack();
        this.picksCount = 0;
        this.maxPicks = 0;
    }

    public void pick(World world, BlockPos pos, PlayerEntity player, Direction hitDirection) {
        this.picksCount++;
        if (picksCount == 1) {
            maxPicks = world.random.nextBetween(MIN_PICKS, MAX_PICKS);
            world.scheduleBlockTick(getPos(), getCachedState().getBlock(), PickableBlock.TICK_DELAY);
        }
        if (picksCount > maxPicks) {
            this.markRemoved();
            world.breakBlock(pos, false);
        } else {
            PickableBlock.updateState(world, pos, picksCount, maxPicks);
        }
    }

    @Nullable
    public Direction getHitDirection() {
        return hitDirection;
    }

    public ItemStack getItem() {
        return item;
    }


    public void tick() {
        if (world == null) return;
        if (picksCount <= 0) return;

        this.picksCount--;
        PickableBlock.updateState(world, pos, picksCount, maxPicks);
        world.scheduleBlockTick(this.getPos(), this.getCachedState().getBlock(), PickableBlock.TICK_DELAY);
    }

}
