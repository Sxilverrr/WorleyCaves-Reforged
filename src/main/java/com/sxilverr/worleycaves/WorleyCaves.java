package com.sxilverr.worleycaves;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class WorleyCaves {

    public static final String MOD_ID = "worleycaves";
    public static final String MOD_NAME = "Worley's Caves Reforged";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    private WorleyCaves() {
    }

    public static ResourceLocation id(String path) {
        return rl(MOD_ID, path);
    }

    public static ResourceLocation rl(String namespace, String path) {
        //? if >=1.21 {
        /*return ResourceLocation.fromNamespaceAndPath(namespace, path);
        *///?} else {
        return new ResourceLocation(namespace, path);
        //?}
    }
}
