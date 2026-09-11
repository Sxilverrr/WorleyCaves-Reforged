package com.sxilverr.worleycaves.forge;

//? if >=1.19 {
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sxilverr.worleycaves.world.WorleyCarvers;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

import java.util.List;
//? if >=1.20 {
import net.minecraft.core.registries.Registries;
//?} else {
/*import net.minecraft.core.Registry;*/
//?}

public record WorleyBiomeModifier(HolderSet<Biome> biomes, Holder<ConfiguredWorldCarver<?>> carver)
        implements BiomeModifier {

    public static final Codec<WorleyBiomeModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Biome.LIST_CODEC.fieldOf("biomes").forGetter(WorleyBiomeModifier::biomes),
            carverCodec().fieldOf("carver").forGetter(WorleyBiomeModifier::carver)
    ).apply(instance, WorleyBiomeModifier::new));

    private static Codec<Holder<ConfiguredWorldCarver<?>>> carverCodec() {
        //? if >=1.20 {
        return RegistryFixedCodec.create(Registries.CONFIGURED_CARVER);
        //?} else {
        /*return RegistryFixedCodec.create(Registry.CONFIGURED_CARVER_REGISTRY);*/
        //?}
    }

    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (!biomes.contains(biome)) {
            return;
        }
        if (phase == Phase.REMOVE) {
            removeVanillaCaves(builder.getGenerationSettings().getCarvers(GenerationStep.Carving.AIR));
        } else if (phase == Phase.ADD) {
            List<Holder<ConfiguredWorldCarver<?>>> air =
                    builder.getGenerationSettings().getCarvers(GenerationStep.Carving.AIR);
            if (!air.contains(carver)) {
                air.add(carver);
            }
        }
    }

    private static void removeVanillaCaves(List<Holder<ConfiguredWorldCarver<?>>> carvers) {
        carvers.removeIf(holder -> holder.unwrapKey()
                .map(key -> WorleyCarvers.isVanillaCave(key.location()))
                .orElse(false));
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return CODEC;
    }
}
//?}
