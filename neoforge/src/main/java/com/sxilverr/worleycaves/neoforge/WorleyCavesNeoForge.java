package com.sxilverr.worleycaves.neoforge;

import com.mojang.serialization.MapCodec;
import com.sxilverr.worleycaves.WorleyCaves;
import com.sxilverr.worleycaves.world.WorleyCarver;
import com.sxilverr.worleycaves.world.WorleyCarverConfiguration;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

@Mod(WorleyCaves.MOD_ID)
public class WorleyCavesNeoForge {

    public static final WorleyCarver WORLEY_CARVER = new WorleyCarver(WorleyCarverConfiguration.CODEC);

    private static final DeferredRegister<WorldCarver<?>> CARVERS =
            DeferredRegister.create(Registries.CARVER, WorleyCaves.MOD_ID);

    private static final DeferredHolder<WorldCarver<?>, WorldCarver<?>> WORLEY_CAVE =
            CARVERS.register("worley_cave", () -> WORLEY_CARVER);

    private static final DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, WorleyCaves.MOD_ID);

    private static final DeferredHolder<MapCodec<? extends BiomeModifier>, MapCodec<WorleyBiomeModifierNeoForge>> WORLEY_BIOME_MODIFIER =
            BIOME_MODIFIERS.register("worley_caves", () -> WorleyBiomeModifierNeoForge.CODEC);

    public WorleyCavesNeoForge(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, NeoForgeConfig.SPEC);

        CARVERS.register(modBus);
        BIOME_MODIFIERS.register(modBus);

        NeoForge.EVENT_BUS.addListener(this::onLevelLoad);
        NeoForge.EVENT_BUS.addListener(this::onLevelUnload);

        WorleyCaves.LOGGER.info("{} loaded", WorleyCaves.MOD_NAME);
    }

    private void onLevelLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            NeoForgeConfig.bake();
            WORLEY_CARVER.init(serverLevel.getSeed());
        }
    }

    private void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel serverLevel && serverLevel.dimensionType().hasSkyLight()) {
            WORLEY_CARVER.reset();
        }
    }
}
