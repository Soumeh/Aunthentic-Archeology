package com.sindercube.aa.client.registry;

import com.sindercube.aa.client.content.entityRenderer.PickableBlockEntityRenderer;
import com.sindercube.aa.registry.AABlockEntities;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class AABlockEntityRenderers {

    public static void init() {
        BlockEntityRendererFactories.register(AABlockEntities.PICKABLE_BLOCK, PickableBlockEntityRenderer::new);
    }

}
