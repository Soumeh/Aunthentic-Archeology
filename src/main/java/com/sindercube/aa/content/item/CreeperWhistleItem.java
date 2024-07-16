package com.sindercube.aa.content.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GoatHornItem;
import net.minecraft.item.Instrument;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CreeperWhistleItem extends GoatHornItem {

    public CreeperWhistleItem(Settings settings, TagKey<Instrument> instrumentTag) {
        super(settings, instrumentTag);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        TypedActionResult<ItemStack> result = super.use(world, user, hand);
        if (!result.getResult().isAccepted()) return result;

        System.out.println("SCARY");
        return result;
    }

}
