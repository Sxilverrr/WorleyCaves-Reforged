package com.sxilverr.worleycaves.util;

import com.sxilverr.worleycaves.WorleyCaves;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
//? if >=1.21.11 {
/*import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
*///?} elif >=1.20 {
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
//?} else {
/*import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
*///?}

public final class BlockUtil {

    private BlockUtil() {
    }

    //? if >=1.21.11 {
    /*public static TagKey<Block> blockTag(Identifier id) {
        return TagKey.create(Registries.BLOCK, id);
    }
    *///?} elif >=1.20 {
    public static TagKey<Block> blockTag(ResourceLocation id) {
        return TagKey.create(Registries.BLOCK, id);
    }
    //?} else {
    /*public static TagKey<Block> blockTag(ResourceLocation id) {
        return TagKey.create(Registry.BLOCK_REGISTRY, id);
    }
    *///?}

    public static BlockState stateFromString(String name, BlockState fallback) {
        Block block = lookup(name);
        if (block != null && block != Blocks.AIR) {
            return block.defaultBlockState();
        }
        WorleyCaves.LOGGER.warn("Unable to find block '{}', falling back to {}", name, fallback);
        return fallback;
    }

    private static Block lookup(String name) {
        //? if >=1.21.11 {
        /*Identifier id = Identifier.tryParse(name);
        return id != null && BuiltInRegistries.BLOCK.containsKey(id) ? BuiltInRegistries.BLOCK.getValue(id) : null;
        *///?} elif >=1.20 {
        ResourceLocation id = ResourceLocation.tryParse(name);
        return id != null && BuiltInRegistries.BLOCK.containsKey(id) ? BuiltInRegistries.BLOCK.get(id) : null;
        //?} else {
        /*ResourceLocation id = ResourceLocation.tryParse(name);
        return id != null && Registry.BLOCK.containsKey(id) ? Registry.BLOCK.get(id) : null;
        *///?}
    }
}
