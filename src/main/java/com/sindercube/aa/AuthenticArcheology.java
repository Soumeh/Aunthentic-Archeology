package com.sindercube.aa;

import com.sindercube.aa.registry.AABlockEntities;
import com.sindercube.aa.registry.AABlocks;
import com.sindercube.aa.registry.AAItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthenticArcheology implements ModInitializer {

    public static final String MOD_ID = "authentic_archeology";
    public static final Logger LOGGER = LoggerFactory.getLogger("Authentic Archeology");

    public static Identifier of(String path) {
        return Identifier.of(MOD_ID, path);
    }


    @Override
    public void onInitialize() {
        register();
        LOGGER.info("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm egg");
    }


    public void register() {
        AABlocks.init();
        AAItems.init();
        AABlockEntities.init();
    }

}
