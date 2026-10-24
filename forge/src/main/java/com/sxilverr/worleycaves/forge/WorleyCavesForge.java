package com.sxilverr.worleycaves.forge;

import com.sxilverr.worleycaves.WorleyCaves;
import com.sxilverr.worleycaves.world.WorleyCarvers;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
//? if >=1.19 {
import com.mojang.serialization.Codec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.registries.ForgeRegistries;
//?} else {
/*import com.sxilverr.worleycaves.world.WorleyCarverConfiguration;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.List;
*///?}
//? if >=1.20 {
import net.minecraft.core.registries.Registries;
//?}

@Mod(WorleyCaves.MOD_ID)
public class WorleyCavesForge {

    //? if >=1.20 {
    private static final DeferredRegister<WorldCarver<?>> CARVERS =
            DeferredRegister.create(Registries.CARVER, WorleyCaves.MOD_ID);
    //?} else {
    /*private static final DeferredRegister<WorldCarver<?>> CARVERS =
            DeferredRegister.create(net.minecraft.core.Registry.CARVER_REGISTRY, WorleyCaves.MOD_ID);
    *///?}

    //? if >=1.19 {
    private static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIERS =
            DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, WorleyCaves.MOD_ID);
    //?}

    public WorleyCavesForge() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ForgeConfig.SPEC);
        CARVERS.register("worley_cave", () -> WorleyCarvers.CARVER);
        CARVERS.register(modBus);

        //? if >=1.19 {
        BIOME_MODIFIERS.register("worley_caves", () -> WorleyBiomeModifier.CODEC);
        BIOME_MODIFIERS.register(modBus);
        forgeBus.addListener(this::onLevelLoad);
        forgeBus.addListener(this::onLevelUnload);
        //?} else {
        /*modBus.addListener(this::onCommonSetup);
        forgeBus.addListener(this::onBiomeLoading);
        forgeBus.addListener(this::onWorldLoad);
        forgeBus.addListener(this::onWorldUnload);
        *///?}

        WorleyCaves.LOGGER.info("{} loaded", WorleyCaves.MOD_NAME);
    }

    //? if >=1.19 {
    private void onLevelLoad(LevelEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            ForgeConfig.bake();
            WorleyCarvers.CARVER.init(serverLevel.getSeed());
        }
    }

    private void onLevelUnload(LevelEvent.Unload event) {
        if (event.getLevel() instanceof ServerLevel serverLevel && serverLevel.dimensionType().hasSkyLight()) {
            WorleyCarvers.CARVER.reset();
        }
    }
    //?} else {
    /*private void onCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> Registry.register(BuiltinRegistries.CONFIGURED_CARVER,
                WorleyCarvers.CONFIGURED_WORLEY_CAVE, WorleyCarvers.CARVER.configured(new WorleyCarverConfiguration())));
    }

    private void onBiomeLoading(BiomeLoadingEvent event) {
        Biome.BiomeCategory category = event.getCategory();
        if (category == Biome.BiomeCategory.NETHER
                || category == Biome.BiomeCategory.THEEND
                || category == Biome.BiomeCategory.NONE) {
            return;
        }

        List<Holder<ConfiguredWorldCarver<?>>> air = event.getGeneration().getCarvers(GenerationStep.Carving.AIR);
        air.removeIf(WorleyCarvers::isVanillaCave);

        Holder<ConfiguredWorldCarver<?>> worley =
                BuiltinRegistries.CONFIGURED_CARVER.getHolderOrThrow(WorleyCarvers.CONFIGURED_WORLEY_CAVE);
        if (!air.contains(worley)) {
            air.add(worley);
        }
    }

    private void onWorldLoad(WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerLevel serverLevel) {
            ForgeConfig.bake();
            WorleyCarvers.CARVER.init(serverLevel.getSeed());
        }
    }

    private void onWorldUnload(WorldEvent.Unload event) {
        if (event.getWorld() instanceof ServerLevel serverLevel && serverLevel.dimensionType().hasSkyLight()) {
            WorleyCarvers.CARVER.reset();
        }
    }
    *///?}
}
