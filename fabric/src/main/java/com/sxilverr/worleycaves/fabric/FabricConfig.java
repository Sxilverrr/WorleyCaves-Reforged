package com.sxilverr.worleycaves.fabric;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.sxilverr.worleycaves.WorleyCaves;
import com.sxilverr.worleycaves.config.WorleyConfig;
import net.fabricmc.loader.api.FabricLoader;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FabricConfig {

    private static final String FILE_NAME = "worleycaves.json";

    private static final String DEFAULTS = """
            {
              // Air blocks at or below this y level will generate as lavaBlock. Default: -54
              "lavaDepth": -54,

              // Reduces number of caves at surface level, becoming more common until caves
              // generate normally X number of blocks below the surface. Default: 15
              "easeInDepth": 15,

              // Block to use when generating large lava lakes below lavaDepth. Default: minecraft:lava
              "lavaBlock": "minecraft:lava",

              // Caves will not attempt to generate above this y level. Default: 128
              "maxCaveHeight": 128,

              // Caves will not attempt to generate below this y level. Default: -56
              "minCaveHeight": -56,

              // Controls size of caves. Smaller values = larger caves. Default: -0.18
              "noiseCutoffValue": -0.18,

              // Controls size of caves at the surface. Smaller values = more caves break
              // through the surface. Default: -0.081 (45% of noiseCutoffValue)
              "surfaceCutoffValue": -0.081,

              // Squishes caves on the Y axis. Lower values = taller caves and more steep drops. Default: 2.0
              "verticalCompressionMultiplier": 2.0,

              // Stretches (when < 1.0) or compresses (when > 1.0) cave generation along X and Z axis. Default: 1.0
              "horizonalCompressionMultiplier": 1.0,

              // Controls how much to warp caves. Lower values = straighter caves. Default: 8.0
              "warpAmplifier": 8.0
            }
            """;

    private FabricConfig() {
    }

    public static void load() {
        Path path = FabricLoader.getInstance().getConfigDir().resolve(FILE_NAME);
        if (!Files.exists(path)) {
            writeDefaults(path);
            return;
        }

        try (BufferedReader buffered = Files.newBufferedReader(path, StandardCharsets.UTF_8);
             JsonReader reader = new JsonReader(buffered)) {
            reader.setLenient(true);
            JsonElement parsed = JsonParser.parseReader(reader);
            if (!parsed.isJsonObject()) {
                WorleyCaves.LOGGER.warn("{} is not a JSON object, using defaults", FILE_NAME);
                return;
            }
            JsonObject root = parsed.getAsJsonObject();
            WorleyConfig.lavaDepth = asInt(root, "lavaDepth", WorleyConfig.DEFAULT_LAVA_DEPTH);
            WorleyConfig.easeInDepth = asInt(root, "easeInDepth", WorleyConfig.DEFAULT_EASE_IN_DEPTH);
            WorleyConfig.lavaBlock = asString(root, "lavaBlock", WorleyConfig.DEFAULT_LAVA_BLOCK);
            WorleyConfig.maxCaveHeight = asInt(root, "maxCaveHeight", WorleyConfig.DEFAULT_MAX_CAVE_HEIGHT);
            WorleyConfig.minCaveHeight = asInt(root, "minCaveHeight", WorleyConfig.DEFAULT_MIN_CAVE_HEIGHT);
            WorleyConfig.noiseCutoffValue = asDouble(root, "noiseCutoffValue", WorleyConfig.DEFAULT_NOISE_CUTOFF);
            WorleyConfig.surfaceCutoffValue = asDouble(root, "surfaceCutoffValue", WorleyConfig.DEFAULT_SURFACE_CUTOFF);
            WorleyConfig.verticalCompressionMultiplier =
                    asDouble(root, "verticalCompressionMultiplier", WorleyConfig.DEFAULT_VERTICAL_COMPRESSION);
            WorleyConfig.horizonalCompressionMultiplier =
                    asDouble(root, "horizonalCompressionMultiplier", WorleyConfig.DEFAULT_HORIZONTAL_COMPRESSION);
            WorleyConfig.warpAmplifier = asDouble(root, "warpAmplifier", WorleyConfig.DEFAULT_WARP_AMPLIFIER);
        } catch (Exception exception) {
            WorleyCaves.LOGGER.error("Failed to read {}, using defaults", FILE_NAME, exception);
        }
    }

    private static void writeDefaults(Path path) {
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, DEFAULTS.getBytes(StandardCharsets.UTF_8));
        } catch (Exception exception) {
            WorleyCaves.LOGGER.error("Failed to write default {}", FILE_NAME, exception);
        }
    }

    private static int asInt(JsonObject root, String key, int fallback) {
        JsonElement element = root.get(key);
        return element != null && element.isJsonPrimitive() ? element.getAsInt() : fallback;
    }

    private static double asDouble(JsonObject root, String key, double fallback) {
        JsonElement element = root.get(key);
        return element != null && element.isJsonPrimitive() ? element.getAsDouble() : fallback;
    }

    private static String asString(JsonObject root, String key, String fallback) {
        JsonElement element = root.get(key);
        return element != null && element.isJsonPrimitive() ? element.getAsString() : fallback;
    }
}
