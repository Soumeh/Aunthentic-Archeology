package com.sindercube.aa.content.item;

import com.sindercube.aa.content.block.PickableBlock;
import com.sindercube.aa.content.blockEntity.PickableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BrushItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PickItem extends Item {

    public PickItem(Settings settings) {
        super(settings);
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world.isClient) return ActionResult.CONSUME;

        PlayerEntity player = context.getPlayer();
        if (player == null) return ActionResult.FAIL;

        BlockPos pos = context.getBlockPos();
        BlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof PickableBlock)) return ActionResult.FAIL;

        var entity = PickableBlockEntity.find(world, pos);
        if (entity == null) return ActionResult.FAIL;

        ItemStack stack = context.getStack();
        Hand hand = context.getHand();
        Direction direction = context.getSide();
        EquipmentSlot usedSlot = switch (hand) {
            case MAIN_HAND -> EquipmentSlot.MAINHAND;
            case OFF_HAND -> EquipmentSlot.OFFHAND;
        };

        entity.pick(world, pos, player, direction);
        stack.damage(1, player, usedSlot);
        addDustParticles(world, direction, context.getHitPos(), state, player.getRotationVec(0.0F), hand);
        return ActionResult.SUCCESS;
    }

    private void addDustParticles(World world, Direction direction, Vec3d pos, BlockState state, Vec3d userRotation, Hand arm) {
        int multiplier = arm == Hand.OFF_HAND ? 1 : -1;
        int random = world.getRandom().nextBetweenExclusive(7, 12);
        BlockStateParticleEffect particle = new BlockStateParticleEffect(ParticleTypes.BLOCK, state);
        BrushItem.DustParticlesOffset offset = BrushItem.DustParticlesOffset.fromSide(userRotation, direction);

        for(int i = 0; i < random; ++i) {
            world.addParticle(
                    particle,
                    pos.x - (double)(direction == Direction.WEST ? 1.0E-6F : 0.0F),
                    pos.y,
                    pos.z - (double)(direction == Direction.NORTH ? 1.0E-6F : 0.0F),
                    offset.xd() * (double) multiplier * 3.0 * world.getRandom().nextDouble(),
                    0.0,
                    offset.zd() * (double) multiplier * 3.0 * world.getRandom().nextDouble()
            );
        }

    }

}
