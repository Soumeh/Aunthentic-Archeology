package com.sindercube.aa.registry;

import com.mojang.datafixers.types.Type;
import com.sindercube.aa.AuthenticArcheology;
import com.sindercube.aa.content.blockEntity.PickableBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

public class AABlockEntities {

    public static void init() {}


    public static BlockEntityType<PickableBlockEntity> PICKABLE_BLOCK = register("pickable_block",
            BlockEntityType.Builder.create(PickableBlockEntity::new,
                    AABlocks.SUSPICIOUS_STONE
            )
    );


    private static <T extends BlockEntity> BlockEntityType<T> register(String path, BlockEntityType.Builder<T> builder) {
        Identifier id = AuthenticArcheology.of(path);
        Type<?> type = Util.getChoiceType(TypeReferences.BLOCK_ENTITY, id.toString());
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, id, builder.build(type));
    }

}
