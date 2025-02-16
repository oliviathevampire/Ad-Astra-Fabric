package com.github.alexnijjar.ad_astra.mixin;

import com.github.alexnijjar.ad_astra.entities.vehicles.VehicleEntity;
import com.github.alexnijjar.ad_astra.items.armour.JetSuit;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
	@Inject(method = "tick", at = @At("HEAD"))
	public void tick(CallbackInfo ci) {
		ServerPlayNetworkHandlerAccessor handler = (ServerPlayNetworkHandlerAccessor) this;
		ServerPlayerEntity player = handler.getPlayer();

		// Prevent the player from being kicked for flying a jet suit
		if (JetSuit.hasFullSet(player)) {
			handler.setFloatingTicks(0);
		}

		// Prevent the player from being kicked for flying in a rocket
		if (player.getVehicle() instanceof VehicleEntity) {
			handler.seVehicleFloatingTicks(0);
		}
	}
}
