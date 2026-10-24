package com.sxilverr.worleycaves.fabric;

import com.sxilverr.worleycaves.WorleyCaves;
import com.sxilverr.worleycaves.world.WorleyCarvers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
//? if <1.21.2 {
import net.minecraft.world.level.levelgen.GenerationStep;
//?}

public class WorleyCavesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        FabricConfig.load();
        Registry.register(BuiltInRegistries.CARVER, WorleyCaves.id("worleycaves:worley_cave"), WorleyCarvers.CARVER);

        BiomeModifications.create(WorleyCaves.id("worleycaves:worley_caves"))
                .add(ModificationPhase.REMOVALS, BiomeSelectors.foundInOverworld(), context -> {
                    //? if >=1.21.2 {
                    /*context.getGenerationSettings().removeCarver(WorleyCarvers.VANILLA_CAVE);
                    context.getGenerationSettings().removeCarver(WorleyCarvers.VANILLA_CAVE_EXTRA_UNDERGROUND);
                    *///?} else {
                    context.getGenerationSettings().removeCarver(GenerationStep.Carving.AIR, WorleyCarvers.VANILLA_CAVE);
                    context.getGenerationSettings().removeCarver(GenerationStep.Carving.AIR, WorleyCarvers.VANILLA_CAVE_EXTRA_UNDERGROUND);
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
            WorleyCarvers.CARVER.init(level.getSeed());
        });

        ServerWorldEvents.UNLOAD.register((server, level) -> {
            if (level.dimensionType().hasSkyLight()) {
                WorleyCarvers.CARVER.reset();
            }
        });

        WorleyCaves.LOGGER.info("{} loaded", WorleyCaves.MOD_NAME);
    }
}
