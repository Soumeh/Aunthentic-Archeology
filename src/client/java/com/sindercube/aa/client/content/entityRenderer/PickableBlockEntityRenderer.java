package com.sindercube.aa.client.content.entityRenderer;

import com.sindercube.aa.content.blockEntity.PickableBlockEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.World;

public class PickableBlockEntityRenderer implements BlockEntityRenderer<PickableBlockEntity> {

    private final ItemRenderer itemRenderer;

    public PickableBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    public void render(PickableBlockEntity entity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        World world = entity.getWorld();
        if (world == null) return;

        ItemStack stack = entity.getItem();
        if (stack.isEmpty()) return;

        int picked = entity.getCachedState().get(Properties.DUSTED);
        if (picked <= 0) return;

        Direction direction = entity.getHitDirection();
        if (direction == null) return;

        matrixStack.push();
        matrixStack.translate(0.0F, 0.5F, 0.0F);
        float[] fs = this.getTranslation(direction, picked);
        matrixStack.translate(fs[0], fs[1], fs[2]);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(75.0F));
        boolean bl = direction == Direction.EAST || direction == Direction.WEST;
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)((bl ? 90 : 0) + 11)));
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        int l = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getCachedState(), entity.getPos().offset(direction));
        this.itemRenderer.renderItem(stack, ModelTransformationMode.FIXED, l, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, entity.getWorld(), 0);
        matrixStack.pop();
    }

    private float[] getTranslation(Direction direction, int dustedLevel) {
        float[] fs = new float[]{0.5F, 0.0F, 0.5F};
        float f = (float)dustedLevel / 10.0F * 0.75F;
        switch (direction) {
            case EAST -> fs[0] = 0.73F + f;
            case WEST -> fs[0] = 0.25F - f;
            case UP -> fs[1] = 0.25F + f;
            case DOWN -> fs[1] = -0.23F - f;
            case NORTH -> fs[2] = 0.25F - f;
            case SOUTH -> fs[2] = 0.73F + f;
        }

        return fs;
    }
}
