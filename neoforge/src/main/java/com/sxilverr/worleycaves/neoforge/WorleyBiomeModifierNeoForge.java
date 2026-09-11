package com.sxilverr.worleycaves.neoforge;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sxilverr.worleycaves.world.WorleyCarvers;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.ModifiableBiomeInfo;

import java.util.List;

public record WorleyBiomeModifierNeoForge(HolderSet<Biome> biomes, Holder<ConfiguredWorldCarver<?>> carver)
        implements BiomeModifier {

    public static final MapCodec<WorleyBiomeModifierNeoForge> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Biome.LIST_CODEC.fieldOf("biomes").forGetter(WorleyBiomeModifierNeoForge::biomes),
            RegistryFixedCodec.<ConfiguredWorldCarver<?>>create(Registries.CONFIGURED_CARVER)
                    .fieldOf("carver").forGetter(WorleyBiomeModifierNeoForge::carver)
    ).apply(instance, WorleyBiomeModifierNeoForge::new));

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (!biomes.contains(biome)) {
            return;
        }
        List<Holder<ConfiguredWorldCarver<?>>> carvers =
                builder.getGenerationSettings().getCarvers(GenerationStep.Carving.AIR);
        if (phase == Phase.REMOVE) {
            carvers.removeIf(holder -> holder.unwrapKey()
                    .map(key -> WorleyCarvers.isVanillaCave(key.location()))
                    .orElse(false));
        } else if (phase == Phase.ADD && !carvers.contains(carver)) {
            carvers.add(carver);
        }
    }

    @Override
    public MapCodec<? extends BiomeModifier> codec() {
        return CODEC;
    }
}
