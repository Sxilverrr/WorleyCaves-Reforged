package com.sxilverr.worleycaves.forge;

import com.sxilverr.worleycaves.config.WorleyConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public final class ForgeConfig {

    public static final ForgeConfigSpec SPEC;
    private static final Values VALUES;

    static {
        Pair<Values, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Values::new);
        VALUES = pair.getLeft();
        SPEC = pair.getRight();
    }

    private ForgeConfig() {
    }

    public static void bake() {
        if (!SPEC.isLoaded()) {
            return;
        }
        WorleyConfig.lavaDepth = VALUES.lavaDepth.get();
        WorleyConfig.easeInDepth = VALUES.easeInDepth.get();
        WorleyConfig.lavaBlock = VALUES.lavaBlock.get();
        WorleyConfig.maxCaveHeight = VALUES.maxCaveHeight.get();
        WorleyConfig.minCaveHeight = VALUES.minCaveHeight.get();
        WorleyConfig.noiseCutoffValue = VALUES.noiseCutoffValue.get();
        WorleyConfig.surfaceCutoffValue = VALUES.surfaceCutoffValue.get();
        WorleyConfig.verticalCompressionMultiplier = VALUES.verticalCompressionMultiplier.get();
        WorleyConfig.horizonalCompressionMultiplier = VALUES.horizonalCompressionMultiplier.get();
        WorleyConfig.warpAmplifier = VALUES.warpAmplifier.get();
    }

    private static final class Values {

        private final ForgeConfigSpec.IntValue lavaDepth;
        private final ForgeConfigSpec.IntValue easeInDepth;
        private final ForgeConfigSpec.ConfigValue<String> lavaBlock;
        private final ForgeConfigSpec.IntValue maxCaveHeight;
        private final ForgeConfigSpec.IntValue minCaveHeight;
        private final ForgeConfigSpec.DoubleValue noiseCutoffValue;
        private final ForgeConfigSpec.DoubleValue surfaceCutoffValue;
        private final ForgeConfigSpec.DoubleValue verticalCompressionMultiplier;
        private final ForgeConfigSpec.DoubleValue horizonalCompressionMultiplier;
        private final ForgeConfigSpec.DoubleValue warpAmplifier;

        private Values(ForgeConfigSpec.Builder builder) {
            builder.push("general");

            lavaDepth = builder
                    .comment("\nAir blocks at or below this y level will generate as lavaBlock \nDefault: -54")
                    .worldRestart()
                    .defineInRange("lavaDepth", WorleyConfig.DEFAULT_LAVA_DEPTH,
                            WorleyConfig.WORLD_MIN_Y, WorleyConfig.WORLD_MAX_Y);

            easeInDepth = builder
                    .comment("\nReduces number of caves at surface level, becoming more common until caves generate normally X number of blocks below the surface \nDefault: 15")
                    .worldRestart()
                    .defineInRange("easeInDepth", WorleyConfig.DEFAULT_EASE_IN_DEPTH, 0, 256);

            lavaBlock = builder
                    .comment("\nBlock to use when generating large lava lakes below lavaDepth \nDefault: minecraft:lava")
                    .worldRestart()
                    .define("lavaBlock", WorleyConfig.DEFAULT_LAVA_BLOCK);

            maxCaveHeight = builder
                    .comment("\nCaves will not attempt to generate above this y level. \nDefault: 128")
                    .worldRestart()
                    .defineInRange("maxCaveHeight", WorleyConfig.DEFAULT_MAX_CAVE_HEIGHT,
                            WorleyConfig.WORLD_MIN_Y, WorleyConfig.WORLD_MAX_Y);

            minCaveHeight = builder
                    .comment("\nCaves will not attempt to generate below this y level. \nDefault: -56")
                    .worldRestart()
                    .defineInRange("minCaveHeight", WorleyConfig.DEFAULT_MIN_CAVE_HEIGHT,
                            WorleyConfig.WORLD_MIN_Y, WorleyConfig.WORLD_MAX_Y);

            noiseCutoffValue = builder
                    .comment("\nControls size of caves. Smaller values = larger caves. \nDefault: -0.18")
                    .worldRestart()
                    .defineInRange("noiseCutoffValue", WorleyConfig.DEFAULT_NOISE_CUTOFF, -1.0D, 1.0D);

            surfaceCutoffValue = builder
                    .comment("\nControls size of caves at the surface. Smaller values = more caves break through the surface. \nDefault: -0.081 (45% of noiseCutoffValue)")
                    .worldRestart()
                    .defineInRange("surfaceCutoffValue", WorleyConfig.DEFAULT_SURFACE_CUTOFF, -1.0D, 1.0D);

            verticalCompressionMultiplier = builder
                    .comment("\nSquishes caves on the Y axis. Lower values = taller caves and more steep drops \nDefault: 2.0")
                    .worldRestart()
                    .defineInRange("verticalCompressionMultiplier", WorleyConfig.DEFAULT_VERTICAL_COMPRESSION, 0.1D, 100.0D);

            horizonalCompressionMultiplier = builder
                    .comment("\nStretches (when < 1.0) or compresses (when > 1.0) cave generation along X and Z axis \nDefault: 1.0")
                    .worldRestart()
                    .defineInRange("horizonalCompressionMultiplier", WorleyConfig.DEFAULT_HORIZONTAL_COMPRESSION, 0.1D, 100.0D);

            warpAmplifier = builder
                    .comment("\nControls how much to warp caves. Lower values = straighter caves \nDefault: 8.0")
                    .worldRestart()
                    .defineInRange("warpAmplifier", WorleyConfig.DEFAULT_WARP_AMPLIFIER, 0.0D, 100.0D);

            builder.pop();
        }
    }
}
