package com.sindercube.aa.registry;

import com.sindercube.aa.AuthenticArcheology;
import net.minecraft.item.Instrument;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class AATags {

    public static void init() {}


    public static final TagKey<Instrument> CREEPER_WHISTLES = of(RegistryKeys.INSTRUMENT, "creeper_whistles");


    public static <T> TagKey<T> of(RegistryKey<Registry<T>> registry, String path) {
        return TagKey.of(registry, AuthenticArcheology.of(path));
    }

}
