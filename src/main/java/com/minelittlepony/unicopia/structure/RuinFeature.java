package com.minelittlepony.unicopia.structure;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureStart;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.DecoratorConfig;
import net.minecraft.world.gen.feature.AbstractTempleFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

class RuinFeature extends AbstractTempleFeature<DefaultFeatureConfig> {
    private static final BlockPos POS = new BlockPos(4, 0, 15);
    private static final Identifier[] VARIANTS = new Identifier[] {
            new Identifier("unicopia", "ground/tower"),
            new Identifier("unicopia", "ground/temple_with_book"),
            new Identifier("unicopia", "ground/temple_without_book"),
            new Identifier("unicopia", "ground/wizard_tower_red"),
            new Identifier("unicopia", "ground/wizard_tower_blue")
    };

    public RuinFeature(Function<Dynamic<?>, ? extends DefaultFeatureConfig> func) {
        super(func);

        Arrays.asList(
                Biomes.TAIGA,
                Biomes.TAIGA_HILLS,
                Biomes.GIANT_TREE_TAIGA,
                Biomes.GIANT_TREE_TAIGA_HILLS,
                Biomes.SNOWY_TAIGA,
                Biomes.SNOWY_TAIGA_HILLS,
                Biomes.GIANT_SPRUCE_TAIGA,
                Biomes.GIANT_TREE_TAIGA_HILLS,
                Biomes.SNOWY_TAIGA_MOUNTAINS,
                Biomes.DARK_FOREST,
                Biomes.DARK_FOREST_HILLS
        ).forEach(biome -> {
            biome.addFeature(GenerationStep.Feature.SURFACE_STRUCTURES,
                    configure(FeatureConfig.DEFAULT)
                    .createDecoratedFeature(Decorator.NOPE.configure(DecoratorConfig.DEFAULT)));
        });
    }

    @Override
    public String getName() {
        return "unicopia:ruins";
    }

    @Override
    public int getRadius() {
        return 12;
    }

    @Override
    protected int getSeedModifier() {
        return 39548;
    }

    @Override
    public StructureStartFactory getStructureStartFactory() {
        return Start::new;
    }

    public static class Start extends StructureStart {
        public Start(StructureFeature<?> feature, int x, int z, BlockBox bound, int var6, long var7) {
            super(feature, x, z, bound, var6, var7);
        }

        @Override
        public void initialize(ChunkGenerator<?> generator, StructureManager manager, int x, int z, Biome biome) {

             BlockRotation rotation = BlockRotation.values()[random.nextInt(BlockRotation.values().length)];
             BlockPos pos = new BlockPos(x * 16, 150, z * 16);

             Identifier template = VARIANTS[random.nextInt(VARIANTS.length) % VARIANTS.length];
             children.add(new Piece(manager, template, pos, rotation));
             setBoundingBoxFromChildren();
        }
    }

    public static class Piece extends SimpleStructurePiece {

        private final BlockRotation rotation;
        private final Identifier template;

        public Piece(StructureManager manager, Identifier template, BlockPos pos, BlockRotation rotation) {
            super(UStructures.RUIN_PART, 0);
            this.pos = pos;
            this.rotation = rotation;
            this.template = template;
            init(manager);

        }

        public Piece(StructureManager manager, CompoundTag tag) {
           super(UStructures.RUIN_PART, tag);
           this.template = new Identifier(tag.getString("Template"));
           this.rotation = BlockRotation.valueOf(tag.getString("Rot"));
           init(manager);
        }

        @Override
        protected void toNbt(CompoundTag compoundTag_1) {
           super.toNbt(compoundTag_1);
           compoundTag_1.putString("Template", template.toString());
           compoundTag_1.putString("Rot", rotation.name());
        }

        private void init(StructureManager manager) {
           setStructureData(manager.getStructureOrBlank(template), pos, new StructurePlacementData()
                   .setRotation(rotation)
                   .setMirrored(BlockMirror.NONE)
                   .setPosition(POS)
                   .addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS));
        }

        @Override
        protected void handleMetadata(String var1, BlockPos var2, IWorld var3, Random var4, BlockBox var5) {
        }
    }
}