package com.sxilverr.worleycaves.world;

import com.mojang.serialization.Codec;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.heightproviders.ConstantHeight;
//? if >=1.19 {
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.block.Block;

import java.util.List;
//?}

public class WorleyCarverConfiguration extends CarverConfiguration {

    public static final Codec<WorleyCarverConfiguration> CODEC = Codec.unit(WorleyCarverConfiguration::new);

    public WorleyCarverConfiguration() {
        //? if >=1.19 {
        super(1.0F,
                ConstantHeight.of(VerticalAnchor.absolute(0)),
                ConstantFloat.of(0.0F),
                VerticalAnchor.aboveBottom(0),
                CarverDebugSettings.DEFAULT,
                HolderSet.<Block>direct(List.of()));
        //?} else {
        /*super(1.0F,
                ConstantHeight.of(VerticalAnchor.absolute(0)),
                ConstantFloat.of(0.0F),
                VerticalAnchor.aboveBottom(0),
                CarverDebugSettings.DEFAULT);
        *///?}
    }
}
