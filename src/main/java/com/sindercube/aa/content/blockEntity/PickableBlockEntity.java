package com.sindercube.aa.content.blockEntity;

import com.sindercube.aa.content.block.PickableBlock;
import com.sindercube.aa.registry.AABlockEntities;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BrushableBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

public class PickableBlockEntity extends BlockEntity {

    private static final int MIN_PICKS = 15;
    private static final int MAX_PICKS = 25;

    private static final int LEEWAY = 1;

    private int picksCount;
    private int maxPicks;
    private int degradingTimeoutTicks = 0;
    private int finalTicks = 0;

    private ItemStack item;
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
        this.item = ItemStack.EMPTY;
        this.picksCount = 0;
        this.maxPicks = 0;
    }

    public void pick(World world, BlockPos pos, PlayerEntity player, Direction hitDirection) {
        this.picksCount++;
        this.degradingTimeoutTicks = 2;

        if (picksCount == 1) {
            generateItem(player);
            this.hitDirection = hitDirection;
            maxPicks = world.random.nextBetween(MIN_PICKS, MAX_PICKS);
            world.scheduleBlockTick(getPos(), getCachedState().getBlock(), PickableBlock.TICK_DELAY);
        }

        else if (picksCount > maxPicks + LEEWAY) {
            this.markRemoved();
            world.breakBlock(pos, false);
        }

        else {
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

        if (degradingTimeoutTicks >= 0) {
            degradingTimeoutTicks--;
            world.scheduleBlockTick(this.getPos(), this.getCachedState().getBlock(), PickableBlock.TICK_DELAY);
            return;
        }

        if (picksCount <= 0) {
            this.hitDirection = null;
        }

        else if (picksCount >= maxPicks) {
            this.finalTicks++;
            if (this.finalTicks > 5) finishPicking();
        }

        else {
            this.finalTicks = 0;
            this.picksCount--;
            PickableBlock.updateState(world, pos, picksCount, maxPicks);
            world.scheduleBlockTick(this.getPos(), this.getCachedState().getBlock(), PickableBlock.TICK_DELAY);
        }
    }

    public void generateItem(PlayerEntity player) {
        if (this.lootTable == null || this.world == null || this.world.isClient() || this.world.getServer() == null) return;

        LootTable table = this.world.getServer().getReloadableRegistries().getLootTable(this.lootTable);
        if (player instanceof ServerPlayerEntity serverPlayer) {
            Criteria.PLAYER_GENERATES_CONTAINER_LOOT.trigger(serverPlayer, this.lootTable);
        }

        LootContextParameterSet context = (new LootContextParameterSet.Builder((ServerWorld)this.world)).add(LootContextParameters.ORIGIN, Vec3d.ofCenter(this.pos)).luck(player.getLuck()).add(LootContextParameters.THIS_ENTITY, player).build(LootContextTypes.CHEST);
        List<ItemStack> items = table.generateLoot(context, this.lootTableSeed);
        items.add(ItemStack.EMPTY);

        this.item = items.getFirst();
        this.lootTable = null;
        this.markDirty();
    }

    private void finishPicking() {
        if (this.world == null || this.world.getServer() == null) return;

        this.spawnItem();

        BlockState state = this.getCachedState();

        Block block = state.getBlock();
        BlockState placedState;
        if (block instanceof PickableBlock pickable) {
            placedState = pickable.getBaseBlock().getDefaultState();
        } else {
            placedState = Blocks.AIR.getDefaultState();
        }

        this.world.syncWorldEvent(3008, this.getPos(), Block.getRawIdFromState(state));
        this.world.setBlockState(this.pos, placedState, 3);
    }

    private void spawnItem() {
        if (this.world == null || this.world.getServer() == null) return;
        if (this.item.isEmpty()) return;

        double width = EntityType.ITEM.getWidth();
        double nWidth = 1.0 - width;
        double halfWidth = width / 2.0;

        Direction direction = Objects.requireNonNullElse(this.hitDirection, Direction.UP);
        BlockPos pos = this.pos.offset(direction, 1);
        double g = (double) pos.getX() + 0.5 * nWidth + halfWidth;
        double h = (double) pos.getY() + 0.5 + (double)(EntityType.ITEM.getHeight() / 2.0F);
        double i = (double) pos.getZ() + 0.5 * nWidth + halfWidth;

        ItemEntity entity = new ItemEntity(this.world, g, h, i, this.item.split(this.world.random.nextInt(21) + 10));
        entity.setVelocity(Vec3d.ZERO);
        this.world.spawnEntity(entity);
        this.item = ItemStack.EMPTY;
    }


    private boolean readLootTableFromNbt(NbtCompound nbt) {
        if (!nbt.contains("loot_table", 8)) return false;

        this.lootTable = RegistryKey.of(RegistryKeys.LOOT_TABLE, Identifier.of(nbt.getString("loot_table")));
        this.lootTableSeed = nbt.getLong("loot_table_seed");
        return true;
    }

    private boolean writeLootTableToNbt(NbtCompound nbt) {
        if (this.lootTable == null) return false;

        nbt.putString("loot_table", this.lootTable.getValue().toString());
        if (this.lootTableSeed != 0L) nbt.putLong("loot_table_seed", this.lootTableSeed);

        return true;
    }

    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound nbtCompound = super.toInitialChunkDataNbt(registryLookup);

        if (this.hitDirection != null) nbtCompound.putInt("hit_direction", this.hitDirection.ordinal());

        if (!this.item.isEmpty()) nbtCompound.put("item", this.item.encode(registryLookup));

        return nbtCompound;
    }

    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (!this.readLootTableFromNbt(nbt) && nbt.contains("item")) {
            this.item = ItemStack.fromNbt(registryLookup, nbt.getCompound("item")).orElse(ItemStack.EMPTY);
        } else {
            this.item = ItemStack.EMPTY;
        }

        if (nbt.contains("hit_direction")) {
            this.hitDirection = Direction.values()[nbt.getInt("hit_direction")];
        }
    }

    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if (!this.writeLootTableToNbt(nbt) && !this.item.isEmpty()) {
            nbt.put("item", this.item.encode(registryLookup));
        }
    }

}
