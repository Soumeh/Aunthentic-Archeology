package com.sindercube.aa.content.blockEntity;

import com.sindercube.aa.registry.AABlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PickableBlockEntity extends BlockEntity {

    private int picksCount;
    private ItemStack item;
    @Nullable private Direction hitDirection;

    @Nullable private RegistryKey<LootTable> lootTable;
    private long lootTableSeed;

    public PickableBlockEntity(BlockPos pos, BlockState state) {
        super(AABlockEntities.PICKABLE_BLOCK, pos, state);
    }

    public void pick(World world, PlayerEntity player, Direction hitDirection) {
    }

    public Direction getHitDirection() {
        return this.hitDirection;
    }

    public ItemStack getItem() {
        return null;
    }

}
