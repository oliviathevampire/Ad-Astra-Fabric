package com.github.alexnijjar.ad_astra.world.structures;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.structure.PostPlacementProcessor;
import net.minecraft.structure.StructurePiecesGenerator;
import net.minecraft.structure.StructurePiecesGeneratorFactory;
import net.minecraft.structure.piece.PoolStructurePiece;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

import java.util.Optional;
import java.util.function.Predicate;

public class SimpleStructure extends StructureFeature<StructurePoolFeatureConfig> {

	public static final Codec<StructurePoolFeatureConfig> CODEC = RecordCodecBuilder
			.create((instance) -> instance.group(StructurePool.REGISTRY_CODEC.fieldOf("start_pool").forGetter(StructurePoolFeatureConfig::getStartPool), Codec.intRange(0, 50).fieldOf("size").forGetter(StructurePoolFeatureConfig::getSize)).apply(instance,
					StructurePoolFeatureConfig::new));

	public SimpleStructure() {
		super(CODEC, context -> SimpleStructure.generate(context, 0, context1 -> true), PostPlacementProcessor.EMPTY);
	}

	public SimpleStructure(int heightOffset) {
		super(CODEC, context -> SimpleStructure.generate(context, heightOffset, context1 -> true), PostPlacementProcessor.EMPTY);
	}

	public SimpleStructure(int heightOffset, Predicate<StructurePiecesGeneratorFactory.Context<StructurePoolFeatureConfig>> canPlace) {
		super(CODEC, context -> SimpleStructure.generate(context, heightOffset, canPlace), PostPlacementProcessor.EMPTY);
	}

	@Override
	public GenerationStep.Feature getGenerationStep() {
		return GenerationStep.Feature.SURFACE_STRUCTURES;
	}

	private static Optional<StructurePiecesGenerator<StructurePoolFeatureConfig>> generate(StructurePiecesGeneratorFactory.Context<StructurePoolFeatureConfig> context, int heightOffset,
																						   Predicate<StructurePiecesGeneratorFactory.Context<StructurePoolFeatureConfig>> canPlace) {

		if (!canPlace.test(context)) {
			return Optional.empty();
		}

		BlockPos blockpos = context.chunkPos().getCenterAtY(0);

		int topLandY = context.chunkGenerator().getHeightOnGround(blockpos.getX(), blockpos.getZ(), Heightmap.Type.WORLD_SURFACE_WG, context.heightLimitView());
		blockpos = blockpos.up(topLandY + heightOffset);

		return StructurePoolBasedGenerator.method_30419(context, PoolStructurePiece::new, blockpos, false, false);
	}

}
