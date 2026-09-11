package com.sxilverr.worleycaves.util;

import com.sxilverr.worleycaves.WorleyCaves;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
//? if >=1.20 {
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
//?} else {
/*import net.minecraft.core.Registry;*/
//?}

public final class BlockUtil {

    private BlockUtil() {
    }

    public static TagKey<Block> blockTag(ResourceLocation id) {
        //? if >=1.20 {
        return TagKey.create(Registries.BLOCK, id);
        //?} else {
        /*return TagKey.create(Registry.BLOCK_REGISTRY, id);*/
        //?}
    }

    public static BlockState stateFromString(String name, BlockState fallback) {
        ResourceLocation id = ResourceLocation.tryParse(name);
        if (id != null) {
            Block block = lookup(id);
            if (block != null && block != Blocks.AIR) {
                return block.defaultBlockState();
            }
        }
        WorleyCaves.LOGGER.warn("Unable to find block '{}', falling back to {}", name, fallback);
        return fallback;
    }

    private static Block lookup(ResourceLocation id) {
        //? if >=1.20 {
        return BuiltInRegistries.BLOCK.containsKey(id) ? BuiltInRegistries.BLOCK.get(id) : null;
        //?} else {
        /*return Registry.BLOCK.containsKey(id) ? Registry.BLOCK.get(id) : null;*/
        //?}
    }
}
