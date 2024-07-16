package com.sindercube.aa.registry;

import com.sindercube.aa.AuthenticArcheology;
import com.sindercube.aa.content.block.PickableBlock;
import com.sindercube.aa.content.block.SlimeMoldBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AABlocks {

    public static void init() {}


    public static final Block SLIME_MOLD = register("slime_mold", new SlimeMoldBlock(
            AbstractBlock.Settings.create()
    ));
    public static final Block SUSPICIOUS_STONE = register("suspicious_stone", new PickableBlock(
            Blocks.STONE,
            AbstractBlock.Settings.create()
    ));


    public static Block register(String path, Block block) {
        block = Registry.register(Registries.BLOCK, AuthenticArcheology.of(path), block);
        Registry.register(Registries.ITEM, AuthenticArcheology.of(path), new BlockItem(block, new Item.Settings()));
        return block;
    }

}
