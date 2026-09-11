package com.sxilverr.worleycaves.world;

import com.sxilverr.worleycaves.WorleyCaves;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
//? if >=1.20 {
import net.minecraft.core.registries.Registries;
//?} else {
/*import net.minecraft.core.Registry;*/
//?}

public final class WorleyCarvers {

    public static final ResourceLocation WORLEY_CAVE = WorleyCaves.id("worley_cave");

    public static final ResourceLocation VANILLA_CAVE = WorleyCaves.rl("minecraft", "cave");
    public static final ResourceLocation VANILLA_CAVE_EXTRA_UNDERGROUND = WorleyCaves.rl("minecraft", "cave_extra_underground");

    //? if >=1.20 {
    public static final ResourceKey<ConfiguredWorldCarver<?>> CONFIGURED_WORLEY_CAVE =
            ResourceKey.create(Registries.CONFIGURED_CARVER, WORLEY_CAVE);
    //?} else {
    /*public static final ResourceKey<ConfiguredWorldCarver<?>> CONFIGURED_WORLEY_CAVE =
            ResourceKey.create(Registry.CONFIGURED_CARVER_REGISTRY, WORLEY_CAVE);
    *///?}

    private WorleyCarvers() {
    }

    public static boolean isVanillaCave(ResourceLocation id) {
        return VANILLA_CAVE.equals(id) || VANILLA_CAVE_EXTRA_UNDERGROUND.equals(id);
    }
}
