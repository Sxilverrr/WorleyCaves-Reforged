package com.sxilverr.worleycaves;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//? if >=1.21.11 {
/*import net.minecraft.resources.Identifier;
*///?} else {
import net.minecraft.resources.ResourceLocation;
//?}

public final class WorleyCaves {

    public static final String MOD_ID = "worleycaves";
    public static final String MOD_NAME = "Worley's Caves Reforged";
    public static final Logger LOGGER = LogManager.getLogger(MOD_NAME);

    private WorleyCaves() {
    }

    //? if >=1.21.11 {
    /*public static Identifier id(String path) {
        return rl(MOD_ID, path);
    }

    public static Identifier rl(String namespace, String path) {
        return Identifier.fromNamespaceAndPath(namespace, path);
    }
    *///?} elif >=1.21 {
    /*public static ResourceLocation id(String path) {
        return rl(MOD_ID, path);
    }

    public static ResourceLocation rl(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }
    *///?} else {
    public static ResourceLocation id(String path) {
        return rl(MOD_ID, path);
    }

    public static ResourceLocation rl(String namespace, String path) {
        return new ResourceLocation(namespace, path);
    }
    //?}
}
