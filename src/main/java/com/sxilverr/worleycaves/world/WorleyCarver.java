package com.sxilverr.worleycaves.world;

import com.sxilverr.worleycaves.WorleyCaves;
import com.sxilverr.worleycaves.config.WorleyConfig;
import com.sxilverr.worleycaves.noise.FastNoise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.WorldCarver;

import java.util.Set;
import java.util.function.Function;
//? if >=1.19 {
import net.minecraft.util.RandomSource;
//?} else {
/*import java.util.Random;*/
//?}
//? if >=1.20 {
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
//?} else {
/*import net.minecraft.core.Registry;*/
//?}

public class WorleyCarver extends WorldCarver<WorleyCarverConfiguration> {

    private static final int SAMPLE_GRID = 5;
    private static final int SAMPLE_STEP = 2;
    private static final int SEA_LEVEL = 63;
    private static final float WORLEY_FREQUENCY = 0.016F;
    private static final float DISPLACEMENT_FREQUENCY = 0.05F;
    private static final double LEGACY_MAX_HEIGHT = 128.0D;

    private static final BlockState AIR = Blocks.AIR.defaultBlockState();
    private static final BlockState SANDSTONE = Blocks.SANDSTONE.defaultBlockState();
    private static final BlockState RED_SANDSTONE = Blocks.RED_SANDSTONE.defaultBlockState();

    private static final TagKey<Block> CARVABLE = blockTag("worleycaves:carvable");
    private static final TagKey<Block> FORGE_STONE = blockTag("forge:stone");
    private static final TagKey<Block> COMMON_STONE = blockTag("c:stone");
    private static final TagKey<Block> TERRACOTTA = blockTag("minecraft:terracotta");

    private static final Set<Block> EXTRA_CARVABLE = Set.of(
            Blocks.GRAVEL,
            Blocks.SANDSTONE,
            Blocks.RED_SANDSTONE,
            Blocks.CLAY,
            Blocks.CALCITE,
            Blocks.TUFF,
            Blocks.DRIPSTONE_BLOCK,
            Blocks.MOSS_BLOCK,
            Blocks.SNOW_BLOCK,
            Blocks.POWDER_SNOW,
            Blocks.PACKED_ICE,
            Blocks.BLUE_ICE,
            Blocks.ICE
    );

    private volatile boolean initialised;
    private long seed;

    private BlockState lavaBlock;
    private int maxCaveHeight;
    private int minCaveHeight;
    private int lavaDepth;
    private float noiseCutoff;
    private float surfaceCutoff;
    private float warpAmplifier;
    private float easeInDepth;
    private float yCompression;
    private float xzCompression;

    WorleyCarver() {
        super(WorleyCarverConfiguration.CODEC);
    }

    public void init(long worldSeed) {
        if (initialised && seed == worldSeed) {
            return;
        }
        maxCaveHeight = WorleyConfig.maxCaveHeight;
        minCaveHeight = WorleyConfig.minCaveHeight;
        lavaDepth = WorleyConfig.lavaDepth;
        noiseCutoff = (float) WorleyConfig.noiseCutoffValue;
        surfaceCutoff = (float) WorleyConfig.surfaceCutoffValue;
        warpAmplifier = (float) WorleyConfig.warpAmplifier;
        easeInDepth = (float) WorleyConfig.easeInDepth;
        yCompression = (float) WorleyConfig.verticalCompressionMultiplier;
        xzCompression = (float) WorleyConfig.horizonalCompressionMultiplier;
        lavaBlock = blockOrLava(WorleyConfig.lavaBlock);
        seed = worldSeed;
        initialised = true;
    }

    public void reset() {
        initialised = false;
    }

    //? if >=1.19 {
    @Override
    public boolean carve(CarvingContext context, WorleyCarverConfiguration config, ChunkAccess chunk,
                         Function<BlockPos, Holder<Biome>> biomeAccessor, RandomSource random,
                         Aquifer aquifer, ChunkPos originPos, CarvingMask carvingMask) {
        return carveChunk(chunk, originPos, carvingMask);
    }

    @Override
    public boolean isStartChunk(WorleyCarverConfiguration config, RandomSource random) {
        return true;
    }
    //?} else {
    /*@Override
    public boolean carve(CarvingContext context, WorleyCarverConfiguration config, ChunkAccess chunk,
                         Function<BlockPos, Holder<Biome>> biomeAccessor, Random random,
                         Aquifer aquifer, ChunkPos originPos, CarvingMask carvingMask) {
        return carveChunk(chunk, originPos, carvingMask);
    }

    @Override
    public boolean isStartChunk(WorleyCarverConfiguration config, Random random) {
        return true;
    }
    *///?}

    private boolean carveChunk(ChunkAccess chunk, ChunkPos originPos, CarvingMask carvingMask) {
        if (!initialised || !originPos.equals(chunk.getPos())) {
            return false;
        }

        //? if >=1.21.2 {
        /*int chunkMinY = Math.max(minCaveHeight, chunk.getMinY());
        int chunkMaxY = Math.min(maxCaveHeight, chunk.getMaxY());
        *///?} else {
        int chunkMinY = Math.max(minCaveHeight, chunk.getMinBuildHeight());
        int chunkMaxY = Math.min(maxCaveHeight, chunk.getMaxBuildHeight() - 1);
        //?}
        if (chunkMaxY <= chunkMinY) {
            return false;
        }

        int[] surfaceY = new int[256];
        int[] floorY = new int[256];
        int chunkSurfaceMax = Integer.MIN_VALUE;
        for (int localX = 0; localX < 16; localX++) {
            for (int localZ = 0; localZ < 16; localZ++) {
                int index = (localX << 4) | localZ;
                surfaceY[index] = chunk.getHeight(Heightmap.Types.WORLD_SURFACE_WG, localX, localZ);
                floorY[index] = chunk.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, localX, localZ) - 1;
                chunkSurfaceMax = Math.max(chunkSurfaceMax, surfaceY[index]);
            }
        }
        if (chunkSurfaceMax < chunkMinY) {
            return false;
        }

        int baseY = Math.floorDiv(chunkMinY, SAMPLE_STEP) * SAMPLE_STEP;
        int ceilingY = Math.min(chunkMaxY, chunkSurfaceMax) + SAMPLE_STEP * 2;
        int sampleCount = ((ceilingY - baseY) / SAMPLE_STEP) + 1;
        if (sampleCount < 3) {
            return false;
        }

        boolean[][] hasCaves = new boolean[SAMPLE_GRID][SAMPLE_GRID];
        float[][][] samples = sampleNoise(originPos.x, originPos.z, baseY, sampleCount,
                chunkSurfaceMax, chunkMinY, chunkMaxY, hasCaves);

        carveSamples(chunk, originPos, carvingMask, samples, hasCaves, baseY, sampleCount,
                chunkMinY, chunkMaxY, surfaceY, floorY);
        return true;
    }

    private float[][][] sampleNoise(int chunkX, int chunkZ, int baseY, int sampleCount, int surfaceMaxY,
                                    int chunkMinY, int chunkMaxY, boolean[][] hasCaves) {
        float[][][] samples = new float[SAMPLE_GRID][sampleCount][SAMPLE_GRID];
        int noiseSeed = (int) seed;

        for (int x = 0; x < SAMPLE_GRID; x++) {
            int realX = x * 4 + chunkX * 16;
            float warpX = realX * DISPLACEMENT_FREQUENCY;
            for (int z = 0; z < SAMPLE_GRID; z++) {
                int realZ = z * 4 + chunkZ * 16;
                float xWarp = FastNoise.perlin(noiseSeed, warpX, realZ * DISPLACEMENT_FREQUENCY);
                float yWarp = FastNoise.perlin(noiseSeed, warpX, (realZ + 67.0F) * DISPLACEMENT_FREQUENCY);
                float zWarp = FastNoise.perlin(noiseSeed, warpX, (realZ + 149.0F) * DISPLACEMENT_FREQUENCY);

                for (int i = sampleCount - 1; i >= 0; i--) {
                    int realY = baseY + i * SAMPLE_STEP;
                    if (realY > surfaceMaxY || realY > chunkMaxY || realY < chunkMinY) {
                        samples[x][i][z] = -1.1F;
                        continue;
                    }

                    float dispAmp = (float) (warpAmplifier
                            * ((LEGACY_MAX_HEIGHT - realY / 2.0D) / (LEGACY_MAX_HEIGHT * 0.85D)));

                    float noise = FastNoise.cellularEdge(noiseSeed,
                            (realX * xzCompression + xWarp * dispAmp) * WORLEY_FREQUENCY,
                            (realY * yCompression + yWarp * dispAmp) * WORLEY_FREQUENCY,
                            (realZ * xzCompression + zWarp * dispAmp) * WORLEY_FREQUENCY);
                    samples[x][i][z] = noise;

                    if (noise > noiseCutoff) {
                        hasCaves[x][z] = true;
                        if (x > 0) {
                            samples[x - 1][i][z] = (noise * 0.2F) + (samples[x - 1][i][z] * 0.8F);
                        }
                        if (z > 0) {
                            samples[x][i][z - 1] = (noise * 0.2F) + (samples[x][i][z - 1] * 0.8F);
                        }
                        if (i < sampleCount - 1) {
                            float noiseAbove = samples[x][i + 1][z];
                            if (noise > noiseAbove) {
                                samples[x][i + 1][z] = (noise * 0.8F) + (noiseAbove * 0.2F);
                            }
                            if (i < sampleCount - 2) {
                                float noiseTwoAbove = samples[x][i + 2][z];
                                if (noise > noiseTwoAbove) {
                                    samples[x][i + 2][z] = (noise * 0.35F) + (noiseTwoAbove * 0.65F);
                                }
                            }
                        }
                    }
                }
            }
        }
        return samples;
    }

    private void carveSamples(ChunkAccess chunk, ChunkPos originPos, CarvingMask carvingMask,
                              float[][][] samples, boolean[][] hasCaves, int baseY, int sampleCount,
                              int chunkMinY, int chunkMaxY, int[] surfaceY, int[] floorY) {
        int originX = originPos.getMinBlockX();
        int originZ = originPos.getMinBlockZ();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos scratch = new BlockPos.MutableBlockPos();

        for (int x = 0; x < 4; x++) {
            for (int z = 0; z < 4; z++) {
                if (!hasCaves[x][z] && !hasCaves[x + 1][z] && !hasCaves[x][z + 1] && !hasCaves[x + 1][z + 1]) {
                    continue;
                }

                for (int i = sampleCount - 2; i >= 0; i--) {
                    float noiseStartX0 = samples[x][i][z];
                    float noiseStartX1 = samples[x][i][z + 1];
                    float noiseEndX0 = samples[x + 1][i][z];
                    float noiseEndX1 = samples[x + 1][i][z + 1];

                    float noiseStepY00 = (samples[x][i + 1][z] - noiseStartX0) * -0.5F;
                    float noiseStepY01 = (samples[x][i + 1][z + 1] - noiseStartX1) * -0.5F;
                    float noiseStepY10 = (samples[x + 1][i + 1][z] - noiseEndX0) * -0.5F;
                    float noiseStepY11 = (samples[x + 1][i + 1][z + 1] - noiseEndX1) * -0.5F;

                    for (int suby = 1; suby >= 0; suby--) {
                        int localY = baseY + i * SAMPLE_STEP + suby;
                        if (localY >= chunkMinY && localY <= chunkMaxY) {
                            float noiseStartZ = noiseStartX0;
                            float noiseEndZ = noiseStartX1;
                            float noiseStepX0 = (noiseEndX0 - noiseStartX0) * 0.25F;
                            float noiseStepX1 = (noiseEndX1 - noiseStartX1) * 0.25F;

                            for (int subx = 0; subx < 4; subx++) {
                                int localX = subx + x * 4;
                                float noiseStepZ = (noiseEndZ - noiseStartZ) * 0.25F;
                                float noiseVal = noiseStartZ;

                                for (int subz = 0; subz < 4; subz++) {
                                    int localZ = subz + z * 4;
                                    int column = (localX << 4) | localZ;
                                    int depth = surfaceY[column] - localY;
                                    if (depth > 0 && noiseVal > cutoffAt(depth, localY)) {
                                        tryCarve(chunk, pos, scratch, carvingMask,
                                                originX + localX, localY, originZ + localZ,
                                                localX, localZ, depth, floorY[column]);
                                    }
                                    noiseVal += noiseStepZ;
                                }

                                noiseStartZ += noiseStepX0;
                                noiseEndZ += noiseStepX1;
                            }
                        }

                        noiseStartX0 += noiseStepY00;
                        noiseStartX1 += noiseStepY01;
                        noiseEndX0 += noiseStepY10;
                        noiseEndX1 += noiseStepY11;
                    }
                }
            }
        }
    }

    private float cutoffAt(int depth, int localY) {
        float cutoff = depth < easeInDepth
                ? (float) Mth.lerp((easeInDepth - (float) depth) / easeInDepth, noiseCutoff, surfaceCutoff)
                : noiseCutoff;
        return localY < minCaveHeight + 5 ? cutoff + (minCaveHeight + 5 - localY) * 0.05F : cutoff;
    }

    private void tryCarve(ChunkAccess chunk, BlockPos.MutableBlockPos pos, BlockPos.MutableBlockPos scratch,
                          CarvingMask carvingMask, int worldX, int localY, int worldZ,
                          int localX, int localZ, int depth, int columnFloorY) {
        BlockState aboveBlock = chunk.getBlockState(scratch.set(worldX, localY + 1, worldZ));
        if (isFluid(aboveBlock) && localY > lavaDepth) {
            return;
        }

        if ((depth < easeInDepth || localY > (SEA_LEVEL - 8)) && localY > lavaDepth) {
            if (isFluid(chunk.getBlockState(scratch.set(worldX + 1, localY, worldZ)))
                    || isFluid(chunk.getBlockState(scratch.set(worldX - 1, localY, worldZ)))
                    || isFluid(chunk.getBlockState(scratch.set(worldX, localY, worldZ + 1)))
                    || isFluid(chunk.getBlockState(scratch.set(worldX, localY, worldZ - 1)))) {
                return;
            }
        }

        pos.set(worldX, localY, worldZ);
        BlockState currentBlock = chunk.getBlockState(pos);
        if (!canCarveThrough(currentBlock)) {
            return;
        }

        carvingMask.set(localX, localY, localZ);
        if (localY <= lavaDepth) {
            setBlock(chunk, pos, lavaBlock);
            return;
        }
        setBlock(chunk, pos, AIR);

        if (localY == columnFloorY) {
            BlockState floorBlock = chunk.getBlockState(scratch.set(worldX, localY - 1, worldZ));
            if (!floorBlock.isAir() && canCarveThrough(floorBlock)) {
                setBlock(chunk, scratch, currentBlock);
            }
        }

        if (aboveBlock.is(Blocks.SAND)) {
            setBlock(chunk, scratch.set(worldX, localY + 1, worldZ), SANDSTONE);
        } else if (aboveBlock.is(Blocks.RED_SAND)) {
            setBlock(chunk, scratch.set(worldX, localY + 1, worldZ), RED_SANDSTONE);
        }
    }

    private static void setBlock(ChunkAccess chunk, BlockPos pos, BlockState state) {
        //? if >=1.21.5 {
        /*chunk.setBlockState(pos, state);
        *///?} else {
        chunk.setBlockState(pos, state, false);
        //?}
    }

    private static boolean isFluid(BlockState state) {
        return !state.getFluidState().isEmpty();
    }

    private static boolean canCarveThrough(BlockState state) {
        if (state.isAir()) {
            return false;
        }
        //? if >=1.19 {
        if (state.is(BlockTags.OVERWORLD_CARVER_REPLACEABLES)) {
            return true;
        }
        //?}
        return state.is(CARVABLE)
                || state.is(BlockTags.BASE_STONE_OVERWORLD)
                || state.is(BlockTags.DIRT)
                || state.is(BlockTags.SAND)
                || state.is(BlockTags.SNOW)
                || state.is(TERRACOTTA)
                || state.is(FORGE_STONE)
                || state.is(COMMON_STONE)
                || EXTRA_CARVABLE.contains(state.getBlock());
    }

    private static TagKey<Block> blockTag(String id) {
        //? if >=1.20 {
        return TagKey.create(Registries.BLOCK, WorleyCaves.id(id));
        //?} else {
        /*return TagKey.create(Registry.BLOCK_REGISTRY, WorleyCaves.id(id));*/
        //?}
    }

    private static BlockState blockOrLava(String name) {
        var id = WorleyCaves.id(name);
        //? if >=1.21.2 {
        /*Block block = id == null ? Blocks.AIR : BuiltInRegistries.BLOCK.getValue(id);
        *///?} elif >=1.20 {
        Block block = id == null ? Blocks.AIR : BuiltInRegistries.BLOCK.get(id);
        //?} else {
        /*Block block = id == null ? Blocks.AIR : Registry.BLOCK.get(id);*/
        //?}
        if (block != Blocks.AIR) {
            return block.defaultBlockState();
        }
        BlockState fallback = Blocks.LAVA.defaultBlockState();
        WorleyCaves.LOGGER.warn("Unable to find block '{}', falling back to {}", name, fallback);
        return fallback;
    }
}
