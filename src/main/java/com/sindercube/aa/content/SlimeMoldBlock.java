package com.sindercube.aa.content;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.Chunk;

public class SlimeMoldBlock extends PlantBlock implements Fertilizable {

    @Override
    protected MapCodec<? extends PlantBlock> getCodec() {
        return createCodec(SlimeMoldBlock::new);
    }

    public static final IntProperty STAGE = IntProperty.of("stage", 0, 2);
    protected static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 1, 16);

    public SlimeMoldBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(STAGE, 0));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }






    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return state.get(STAGE) < 2;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (Math.random() < 0.6) return;
        if (state.get(STAGE) < 2) world.setBlockState(pos, state.cycle(STAGE));
        Chunk chunk = world.getChunk(pos);
//        chunk.

//            int origX = level.getChunk(pos).getPos().x;
//            int origZ = level.getChunk(pos).getPos().z;
//            long seed = level.getSeed();
//            outer:
//            for(int i =0;i<6;i++)
//                for(int x = -i; x <=i; x++)
//                {
//                    for(int z = -i; z <=i; z++)
//                    {
//                        Random actualrnd = new Random();
//                        Random rnd = new Random(level.getSeed()+
//                                (int) ((origX+x) * (origX+x) * 0x4c1906) +
//                                (int) ((origX+x) * 0x5ac0db) +
//                                (int) ((origZ+z) * (origZ+z)) * 0x4307a7L +
//                                (int) ((origZ+z) * 0x5f24f) ^ 0x3ad8025fL);
//                        if(rnd.nextInt(10)==0)
//                        {
//                            ChunkPos chunky = level.getChunk(origX+x,origZ+z).getPos();
//                            if(chunky.x == origX && chunky.z == origZ)
//                            {
//                                Direction dir = Direction.fromYRot(actualrnd.nextInt(360));
//                                yup:
//                                for(int j = -1; j<=1; j++)
//                                    if(level.getBlockState(pos.offset(dir.getNormal()).offset(0,j,0)).getTags().toList().contains(BlockTags.REPLACEABLE) && this.mayPlaceOn(level.getBlockState(pos.offset(dir.getNormal()).offset(0,j-1,0)),level,pos.offset(dir.getNormal()).offset(0,j-1,0)))
//                                    {
//                                        System.out.println("yippee");
//                                        level.setBlock(pos.offset(dir.getNormal()).offset(0,j,0), AAblockRegistry.SLIME_MOLD.get().defaultBlockState(),2);
//                                        break yup;
//                                    }
//                            }
//                            else
//                            {
//                                int offsetX = chunky.x-origX;
//                                int offsetZ = chunky.z-origZ;
//                                Vec3 dir = new Vec3(offsetX,0,offsetZ);
//                                dir = dir.yRot((float)((Math.random()-0.5)/6*3.14f));
//                                dir = dir.normalize();
//                                dir = dir.scale(1.45);
//                                System.out.println(dir);
//                                yup:
//                                for(int j = -1; j<=1; j++)
//                                    if(level.getBlockState(pos.offset((int) dir.x,0, (int) dir.z).offset(0,j,0)).getTags().toList().contains(BlockTags.REPLACEABLE) && this.mayPlaceOn(level.getBlockState(pos.offset((int) dir.x,0, (int) dir.z).offset(0,j-1,0)),level,pos.offset((int) dir.x,0, (int) dir.z).offset(0,j-1,0)))
//                                    {
//                                        System.out.println("yippioe " + dir);
//                                        level.setBlock(pos.offset((int) dir.x,j, (int) dir.z), AAblockRegistry.SLIME_MOLD.get().defaultBlockState(),2);
//
//                                        break yup;
//                                    }
//                            }
//                            break outer;
//                        }
//                    }
//                }

    }

    public static boolean isSlimeChunk(ServerWorld world, int x, int z) {
        ChunkPos pos = new ChunkPos(new BlockPos(x, 0, z));
        return ChunkRandom.getSlimeRandom(pos.x, pos.z, world.getSeed(), 987234911L).nextInt(10) == 0;
    }

//    @Override
//    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
//        super.randomTick(state, world, pos, random);
////        world.getChunk(pos);
//        if (world.isChunkLoaded(pos)) {
//            this.grow(state, level, pos);
//        }
//    }


    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

}
