package com.sxilverr.worleycaves.fabric;

import com.sxilverr.worleycaves.WorleyCaves;
import com.sxilverr.worleycaves.config.WorleyConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class ClothConfigScreen {

    private ClothConfigScreen() {
    }

    public static Screen create(Screen parent) {
        FabricConfig.load();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal(WorleyCaves.MOD_NAME))
                .setSavingRunnable(FabricConfig::save);

        ConfigEntryBuilder entries = builder.entryBuilder();
        ConfigCategory general = builder.getOrCreateCategory(Component.literal("General"));

        general.addEntry(entries.startIntField(Component.literal("lavaDepth"), WorleyConfig.lavaDepth)
                .setDefaultValue(WorleyConfig.DEFAULT_LAVA_DEPTH)
                .setMin(WorleyConfig.WORLD_MIN_Y)
                .setMax(WorleyConfig.WORLD_MAX_Y)
                .setTooltip(Component.literal("Air blocks at or below this y level will generate as lavaBlock."))
                .requireRestart()
                .setSaveConsumer(value -> WorleyConfig.lavaDepth = value)
                .build());

        general.addEntry(entries.startIntField(Component.literal("easeInDepth"), WorleyConfig.easeInDepth)
                .setDefaultValue(WorleyConfig.DEFAULT_EASE_IN_DEPTH)
                .setMin(0)
                .setMax(256)
                .setTooltip(Component.literal("Reduces number of caves at surface level, becoming more common until caves generate normally X number of blocks below the surface."))
                .requireRestart()
                .setSaveConsumer(value -> WorleyConfig.easeInDepth = value)
                .build());

        general.addEntry(entries.startStrField(Component.literal("lavaBlock"), WorleyConfig.lavaBlock)
                .setDefaultValue(WorleyConfig.DEFAULT_LAVA_BLOCK)
                .setTooltip(Component.literal("Block to use when generating large lava lakes below lavaDepth."))
                .requireRestart()
                .setSaveConsumer(value -> WorleyConfig.lavaBlock = value)
                .build());

        general.addEntry(entries.startIntField(Component.literal("maxCaveHeight"), WorleyConfig.maxCaveHeight)
                .setDefaultValue(WorleyConfig.DEFAULT_MAX_CAVE_HEIGHT)
                .setMin(WorleyConfig.WORLD_MIN_Y)
                .setMax(WorleyConfig.WORLD_MAX_Y)
                .setTooltip(Component.literal("Caves will not attempt to generate above this y level."))
                .requireRestart()
                .setSaveConsumer(value -> WorleyConfig.maxCaveHeight = value)
                .build());

        general.addEntry(entries.startIntField(Component.literal("minCaveHeight"), WorleyConfig.minCaveHeight)
                .setDefaultValue(WorleyConfig.DEFAULT_MIN_CAVE_HEIGHT)
                .setMin(WorleyConfig.WORLD_MIN_Y)
                .setMax(WorleyConfig.WORLD_MAX_Y)
                .setTooltip(Component.literal("Caves will not attempt to generate below this y level."))
                .requireRestart()
                .setSaveConsumer(value -> WorleyConfig.minCaveHeight = value)
                .build());

        general.addEntry(entries.startDoubleField(Component.literal("noiseCutoffValue"), WorleyConfig.noiseCutoffValue)
                .setDefaultValue(WorleyConfig.DEFAULT_NOISE_CUTOFF)
                .setMin(-1.0D)
                .setMax(1.0D)
                .setTooltip(Component.literal("Controls size of caves. Smaller values = larger caves."))
                .requireRestart()
                .setSaveConsumer(value -> WorleyConfig.noiseCutoffValue = value)
                .build());

        general.addEntry(entries.startDoubleField(Component.literal("surfaceCutoffValue"), WorleyConfig.surfaceCutoffValue)
                .setDefaultValue(WorleyConfig.DEFAULT_SURFACE_CUTOFF)
                .setMin(-1.0D)
                .setMax(1.0D)
                .setTooltip(Component.literal("Controls size of caves at the surface. Smaller values = more caves break through the surface."))
                .requireRestart()
                .setSaveConsumer(value -> WorleyConfig.surfaceCutoffValue = value)
                .build());

        general.addEntry(entries.startDoubleField(Component.literal("verticalCompressionMultiplier"), WorleyConfig.verticalCompressionMultiplier)
                .setDefaultValue(WorleyConfig.DEFAULT_VERTICAL_COMPRESSION)
                .setMin(0.1D)
                .setMax(100.0D)
                .setTooltip(Component.literal("Squishes caves on the Y axis. Lower values = taller caves and more steep drops."))
                .requireRestart()
                .setSaveConsumer(value -> WorleyConfig.verticalCompressionMultiplier = value)
                .build());

        general.addEntry(entries.startDoubleField(Component.literal("horizonalCompressionMultiplier"), WorleyConfig.horizonalCompressionMultiplier)
                .setDefaultValue(WorleyConfig.DEFAULT_HORIZONTAL_COMPRESSION)
                .setMin(0.1D)
                .setMax(100.0D)
                .setTooltip(Component.literal("Stretches (when < 1.0) or compresses (when > 1.0) cave generation along X and Z axis."))
                .requireRestart()
                .setSaveConsumer(value -> WorleyConfig.horizonalCompressionMultiplier = value)
                .build());

        general.addEntry(entries.startDoubleField(Component.literal("warpAmplifier"), WorleyConfig.warpAmplifier)
                .setDefaultValue(WorleyConfig.DEFAULT_WARP_AMPLIFIER)
                .setMin(0.0D)
                .setMax(100.0D)
                .setTooltip(Component.literal("Controls how much to warp caves. Lower values = straighter caves."))
                .requireRestart()
                .setSaveConsumer(value -> WorleyConfig.warpAmplifier = value)
                .build());

        return builder.build();
    }
}
