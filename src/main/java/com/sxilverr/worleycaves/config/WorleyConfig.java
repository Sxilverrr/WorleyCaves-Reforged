package com.sxilverr.worleycaves.config;

public final class WorleyConfig {

    public static final int WORLD_MIN_Y = -64;
    public static final int WORLD_MAX_Y = 320;

    public static final int DEFAULT_LAVA_DEPTH = -54;
    public static final int DEFAULT_EASE_IN_DEPTH = 15;
    public static final String DEFAULT_LAVA_BLOCK = "minecraft:lava";
    public static final int DEFAULT_MAX_CAVE_HEIGHT = 128;
    public static final int DEFAULT_MIN_CAVE_HEIGHT = -56;
    public static final double DEFAULT_NOISE_CUTOFF = -0.18D;
    public static final double DEFAULT_SURFACE_CUTOFF = -0.081D;
    public static final double DEFAULT_VERTICAL_COMPRESSION = 2.0D;
    public static final double DEFAULT_HORIZONTAL_COMPRESSION = 1.0D;
    public static final double DEFAULT_WARP_AMPLIFIER = 8.0D;

    public static int lavaDepth = DEFAULT_LAVA_DEPTH;
    public static int easeInDepth = DEFAULT_EASE_IN_DEPTH;
    public static String lavaBlock = DEFAULT_LAVA_BLOCK;
    public static int maxCaveHeight = DEFAULT_MAX_CAVE_HEIGHT;
    public static int minCaveHeight = DEFAULT_MIN_CAVE_HEIGHT;
    public static double noiseCutoffValue = DEFAULT_NOISE_CUTOFF;
    public static double surfaceCutoffValue = DEFAULT_SURFACE_CUTOFF;
    public static double verticalCompressionMultiplier = DEFAULT_VERTICAL_COMPRESSION;
    public static double horizonalCompressionMultiplier = DEFAULT_HORIZONTAL_COMPRESSION;
    public static double warpAmplifier = DEFAULT_WARP_AMPLIFIER;

    private WorleyConfig() {
    }
}
