package com.sxilverr.worleycaves.neoforge;

import com.sxilverr.worleycaves.config.WorleyConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class NeoForgeConfig {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder().push("general");

    private static final ModConfigSpec.IntValue LAVA_DEPTH = BUILDER
            .comment("\nAir blocks at or below this y level will generate as lavaBlock \nDefault: -54")
            .worldRestart()
            .defineInRange("lavaDepth", WorleyConfig.DEFAULT_LAVA_DEPTH, WorleyConfig.WORLD_MIN_Y, WorleyConfig.WORLD_MAX_Y);

    private static final ModConfigSpec.IntValue EASE_IN_DEPTH = BUILDER
            .comment("\nReduces number of caves at surface level, becoming more common until caves generate normally X number of blocks below the surface \nDefault: 15")
            .worldRestart()
            .defineInRange("easeInDepth", WorleyConfig.DEFAULT_EASE_IN_DEPTH, 0, 256);

    private static final ModConfigSpec.ConfigValue<String> LAVA_BLOCK = BUILDER
            .comment("\nBlock to use when generating large lava lakes below lavaDepth \nDefault: minecraft:lava")
            .worldRestart()
            .define("lavaBlock", WorleyConfig.DEFAULT_LAVA_BLOCK);

    private static final ModConfigSpec.IntValue MAX_CAVE_HEIGHT = BUILDER
            .comment("\nCaves will not attempt to generate above this y level. \nDefault: 128")
            .worldRestart()
            .defineInRange("maxCaveHeight", WorleyConfig.DEFAULT_MAX_CAVE_HEIGHT, WorleyConfig.WORLD_MIN_Y, WorleyConfig.WORLD_MAX_Y);

    private static final ModConfigSpec.IntValue MIN_CAVE_HEIGHT = BUILDER
            .comment("\nCaves will not attempt to generate below this y level. \nDefault: -56")
            .worldRestart()
            .defineInRange("minCaveHeight", WorleyConfig.DEFAULT_MIN_CAVE_HEIGHT, WorleyConfig.WORLD_MIN_Y, WorleyConfig.WORLD_MAX_Y);

    private static final ModConfigSpec.DoubleValue NOISE_CUTOFF = BUILDER
            .comment("\nControls size of caves. Smaller values = larger caves. \nDefault: -0.18")
            .worldRestart()
            .defineInRange("noiseCutoffValue", WorleyConfig.DEFAULT_NOISE_CUTOFF, -1.0D, 1.0D);

    private static final ModConfigSpec.DoubleValue SURFACE_CUTOFF = BUILDER
            .comment("\nControls size of caves at the surface. Smaller values = more caves break through the surface. \nDefault: -0.081 (45% of noiseCutoffValue)")
            .worldRestart()
            .defineInRange("surfaceCutoffValue", WorleyConfig.DEFAULT_SURFACE_CUTOFF, -1.0D, 1.0D);

    private static final ModConfigSpec.DoubleValue VERTICAL_COMPRESSION = BUILDER
            .comment("\nSquishes caves on the Y axis. Lower values = taller caves and more steep drops \nDefault: 2.0")
            .worldRestart()
            .defineInRange("verticalCompressionMultiplier", WorleyConfig.DEFAULT_VERTICAL_COMPRESSION, 0.1D, 100.0D);

    private static final ModConfigSpec.DoubleValue HORIZONTAL_COMPRESSION = BUILDER
            .comment("\nStretches (when < 1.0) or compresses (when > 1.0) cave generation along X and Z axis \nDefault: 1.0")
            .worldRestart()
            .defineInRange("horizonalCompressionMultiplier", WorleyConfig.DEFAULT_HORIZONTAL_COMPRESSION, 0.1D, 100.0D);

    private static final ModConfigSpec.DoubleValue WARP_AMPLIFIER = BUILDER
            .comment("\nControls how much to warp caves. Lower values = straighter caves \nDefault: 8.0")
            .worldRestart()
            .defineInRange("warpAmplifier", WorleyConfig.DEFAULT_WARP_AMPLIFIER, 0.0D, 100.0D);

    public static final ModConfigSpec SPEC = BUILDER.pop().build();

    private NeoForgeConfig() {
    }

    public static void bake() {
        if (!SPEC.isLoaded()) {
            return;
        }
        WorleyConfig.lavaDepth = LAVA_DEPTH.get();
        WorleyConfig.easeInDepth = EASE_IN_DEPTH.get();
        WorleyConfig.lavaBlock = LAVA_BLOCK.get();
        WorleyConfig.maxCaveHeight = MAX_CAVE_HEIGHT.get();
        WorleyConfig.minCaveHeight = MIN_CAVE_HEIGHT.get();
        WorleyConfig.noiseCutoffValue = NOISE_CUTOFF.get();
        WorleyConfig.surfaceCutoffValue = SURFACE_CUTOFF.get();
        WorleyConfig.verticalCompressionMultiplier = VERTICAL_COMPRESSION.get();
        WorleyConfig.horizonalCompressionMultiplier = HORIZONTAL_COMPRESSION.get();
        WorleyConfig.warpAmplifier = WARP_AMPLIFIER.get();
    }
}
