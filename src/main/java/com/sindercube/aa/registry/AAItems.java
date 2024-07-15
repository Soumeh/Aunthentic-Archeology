package com.sindercube.aa.registry;

import com.sindercube.aa.AuthenticArcheology;
import com.sindercube.aa.content.item.PickItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class AAItems {

    public static void init() {}


    public static final Item PICK = register("pick", new PickItem(
            new Item.Settings().maxCount(1).maxDamage(256)
    ));


    public static Item register(String path, Item item) {
        return Registry.register(Registries.ITEM, AuthenticArcheology.of(path), item);
    }

}
