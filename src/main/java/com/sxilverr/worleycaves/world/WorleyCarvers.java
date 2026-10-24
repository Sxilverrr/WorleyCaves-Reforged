package com.sxilverr.worleycaves.world;

import com.sxilverr.worleycaves.WorleyCaves;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
//? if >=1.20 {
import net.minecraft.core.registries.Registries;
//?} else {
/*import net.minecraft.core.Registry;*/
//?}

public final class WorleyCarvers {

    public static final WorleyCarver CARVER = new WorleyCarver();

    public static final ResourceKey<ConfiguredWorldCarver<?>> CONFIGURED_WORLEY_CAVE = key("worleycaves:worley_cave");
    public static final ResourceKey<ConfiguredWorldCarver<?>> VANILLA_CAVE = key("minecraft:cave");
    public static final ResourceKey<ConfiguredWorldCarver<?>> VANILLA_CAVE_EXTRA_UNDERGROUND = key("minecraft:cave_extra_underground");

    private WorleyCarvers() {
    }

    private static ResourceKey<ConfiguredWorldCarver<?>> key(String id) {
        //? if >=1.20 {
        return ResourceKey.create(Registries.CONFIGURED_CARVER, WorleyCaves.id(id));
        //?} else {
        /*return ResourceKey.create(Registry.CONFIGURED_CARVER_REGISTRY, WorleyCaves.id(id));*/
        //?}
    }

    public static boolean isVanillaCave(Holder<ConfiguredWorldCarver<?>> carver) {
        return carver.is(VANILLA_CAVE) || carver.is(VANILLA_CAVE_EXTRA_UNDERGROUND);
    }
}
