package com.sindercube.aa.registry;

import com.sindercube.aa.AunthenticArcheology;
import com.sindercube.aa.content.SlimeMoldBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AABlocks {

    public static void init() {}


    public static final Block SLIME_MOLD = new SlimeMoldBlock(AbstractBlock.Settings.create());


    public Block register(String path, Block block) {
        return Registry.register(Registries.BLOCK, AunthenticArcheology.of(path), block);
    }

}
