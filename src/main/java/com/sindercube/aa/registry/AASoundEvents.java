package com.sindercube.aa.registry;

import com.sindercube.aa.AuthenticArcheology;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundEvent;

public class AASoundEvents {

    public static void init() {}


    public static final RegistryEntry<SoundEvent> CREEPER_WHISTLE_DEFAULT = register("item.creeper_whistle.sound.default");


    private static RegistryEntry<SoundEvent> register(String path) {
        var id = AuthenticArcheology.of(path);
        return Registry.registerReference(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

}
