package com.sxilverr.worleycaves.world;

import com.sxilverr.worleycaves.WorleyCaves;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
//? if >=1.20 {
import net.minecraft.core.registries.Registries;
//?} else {
/*import net.minecraft.core.Registry;*/
//?}
//? if >=1.21.11 {
/*import net.minecraft.resources.Identifier;
*///?} else {
import net.minecraft.resources.ResourceLocation;
//?}

public final class WorleyCarvers {

    //? if >=1.21.11 {
    /*public static final Identifier WORLEY_CAVE = WorleyCaves.id("worley_cave");

    public static final Identifier VANILLA_CAVE = WorleyCaves.rl("minecraft", "cave");
    public static final Identifier VANILLA_CAVE_EXTRA_UNDERGROUND = WorleyCaves.rl("minecraft", "cave_extra_underground");
    *///?} else {
    public static final ResourceLocation WORLEY_CAVE = WorleyCaves.id("worley_cave");

    public static final ResourceLocation VANILLA_CAVE = WorleyCaves.rl("minecraft", "cave");
    public static final ResourceLocation VANILLA_CAVE_EXTRA_UNDERGROUND = WorleyCaves.rl("minecraft", "cave_extra_underground");
    //?}

    //? if >=1.20 {
    public static final ResourceKey<ConfiguredWorldCarver<?>> CONFIGURED_WORLEY_CAVE =
            ResourceKey.create(Registries.CONFIGURED_CARVER, WORLEY_CAVE);
    //?} else {
    /*public static final ResourceKey<ConfiguredWorldCarver<?>> CONFIGURED_WORLEY_CAVE =
            ResourceKey.create(Registry.CONFIGURED_CARVER_REGISTRY, WORLEY_CAVE);
    *///?}

    private WorleyCarvers() {
    }

    //? if >=1.21.11 {
    /*public static boolean isVanillaCave(Identifier id) {
    *///?} else {
    public static boolean isVanillaCave(ResourceLocation id) {
    //?}
        return VANILLA_CAVE.equals(id) || VANILLA_CAVE_EXTRA_UNDERGROUND.equals(id);
    }
}
