package com.sindercube.aa.registry;

import com.sindercube.aa.AuthenticArcheology;
import net.minecraft.item.Instrument;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class AAInstruments {

    public static void init() {}


    public static final RegistryKey<Instrument> BUTCHER_HORN_DEFAULT = register(
            "creeper_whistle/default",
            new Instrument(AASoundEvents.CREEPER_WHISTLE_DEFAULT, 140, 256.0F)
    );


    public static RegistryKey<Instrument> register(String path, Instrument instrument) {
        var id = AuthenticArcheology.of(path);
        var key = RegistryKey.of(RegistryKeys.INSTRUMENT, id);
        Registry.register(Registries.INSTRUMENT, id, instrument);
        return key;
    }

}
