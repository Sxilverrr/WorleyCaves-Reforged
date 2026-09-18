package com.sxilverr.worleycaves.fabric;

import com.sxilverr.worleycaves.WorleyCaves;
import com.sxilverr.worleycaves.world.WorleyCarver;
import com.sxilverr.worleycaves.world.WorleyCarverConfiguration;
import com.sxilverr.worleycaves.world.WorleyCarvers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
//? if <1.21.2 {
import net.minecraft.world.level.levelgen.GenerationStep;
//?}

public class WorleyCavesFabric implements ModInitializer {

    public static final WorleyCarver WORLEY_CARVER = new WorleyCarver(WorleyCarverConfiguration.CODEC);

    private static final ResourceKey<ConfiguredWorldCarver<?>> VANILLA_CAVE =
            ResourceKey.create(Registries.CONFIGURED_CARVER, WorleyCarvers.VANILLA_CAVE);
    private static final ResourceKey<ConfiguredWorldCarver<?>> VANILLA_CAVE_EXTRA_UNDERGROUND =
            ResourceKey.create(Registries.CONFIGURED_CARVER, WorleyCarvers.VANILLA_CAVE_EXTRA_UNDERGROUND);

    @Override
    public void onInitialize() {
        FabricConfig.load();
        Registry.register(BuiltInRegistries.CARVER, WorleyCarvers.WORLEY_CAVE, WORLEY_CARVER);

        BiomeModifications.create(WorleyCaves.id("worley_caves"))
                .add(ModificationPhase.REMOVALS, BiomeSelectors.foundInOverworld(), context -> {
                    //? if >=1.21.2 {
                    /*context.getGenerationSettings().removeCarver(VANILLA_CAVE);
                    context.getGenerationSettings().removeCarver(VANILLA_CAVE_EXTRA_UNDERGROUND);
                    *///?} else {
                    context.getGenerationSettings().removeCarver(GenerationStep.Carving.AIR, VANILLA_CAVE);
                    context.getGenerationSettings().removeCarver(GenerationStep.Carving.AIR, VANILLA_CAVE_EXTRA_UNDERGROUND);
                    //?}
                })
                .add(ModificationPhase.ADDITIONS, BiomeSelectors.foundInOverworld(), context ->
                        //? if >=1.21.2 {
                        /*context.getGenerationSettings().addCarver(WorleyCarvers.CONFIGURED_WORLEY_CAVE));
                        *///?} else {
                        context.getGenerationSettings()
                                .addCarver(GenerationStep.Carving.AIR, WorleyCarvers.CONFIGURED_WORLEY_CAVE));
                        //?}

        ServerWorldEvents.LOAD.register((server, level) -> {
            FabricConfig.load();
            WORLEY_CARVER.init(level.getSeed());
        });

        ServerWorldEvents.UNLOAD.register((server, level) -> {
            if (level.dimensionType().hasSkyLight()) {
                WORLEY_CARVER.reset();
            }
        });

        WorleyCaves.LOGGER.info("{} loaded", WorleyCaves.MOD_NAME);
    }
}
