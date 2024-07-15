package com.sindercube.aa.client;

import com.sindercube.aa.client.registry.AABlockEntityRenderers;
import net.fabricmc.api.ClientModInitializer;

public class AuthenticArcheologyClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        register();
    }

    public void register() {
        AABlockEntityRenderers.init();
    }

}
