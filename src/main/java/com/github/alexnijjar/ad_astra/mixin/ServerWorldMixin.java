package com.github.alexnijjar.ad_astra.mixin;

import com.github.alexnijjar.ad_astra.world.LunarianWanderingTraderManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Holder;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Spawner;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

// Adds lunarian wandering trader spawning
@Mixin(ServerWorld.class)
public class ServerWorldMixin {
	@Inject(at = @At(value = "TAIL"), method = "<init>")
	public void ServerWorld(MinecraftServer minecraftServer, Executor executor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey<World> registryKey, Holder<DimensionType> holder,
							WorldGenerationProgressListener worldGenerationProgressListener, ChunkGenerator chunkGenerator, boolean debugWorld, long seed, List<Spawner> spawners, boolean shouldTickTime, CallbackInfo ci) {

		List<Spawner> serverSpawners = new ArrayList<>(((ServerWorld) (Object) this).spawners);
		if (!shouldTickTime) {
			serverSpawners.add(new LunarianWanderingTraderManager(properties));
			((ServerWorld) (Object) this).spawners = serverSpawners;
		}
	}
}
