package com.github.alexnijjar.ad_astra.entities.vehicles;

import com.github.alexnijjar.ad_astra.AdAstra;
import com.github.alexnijjar.ad_astra.screen.LanderScreenHandlerFactory;
import com.github.alexnijjar.ad_astra.util.ModKeyBindings;
import com.github.alexnijjar.ad_astra.util.ModUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LanderEntity extends VehicleEntity {

	public LanderEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	public int getInventorySize() {
		return 11;
	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand) {
		super.interact(player, hand);
		this.openInventory(player, new LanderScreenHandlerFactory(this));
		return ActionResult.SUCCESS;
	}

	@Override
	public double getMountedHeightOffset() {
		return super.getMountedHeightOffset() + 1.6f;
	}

	// Drop inventory contents instead of dropping itself.
	@Override
	public void drop() {
		ItemScatterer.spawn(this.world, this.getBlockPos(), this.getInventory());
		super.drop();
	}

	@Override
	public boolean shouldRenderPlayer() {
		return false;
	}

	@Override
	public boolean doHighFov() {
		return true;
	}

	@Override
	public boolean fullyConcealsRider() {
		return true;
	}

	@Override
	public boolean canRiderTakeFallDamage() {
		return false;
	}

	@Override
	public boolean renderPlanetBar() {
		return true;
	}

	@Override
	public void tick() {
		super.tick();

		if (this.getFirstPassenger() instanceof PlayerEntity player) {

			// Player is clicking 'space' to move upward.
			if (!this.isOnGround()) {
				if (ModKeyBindings.jumpKeyDown(player)) {
					this.applyBoosters();
				}
			}
		}
	}

	public void applyBoosters() {
		this.setVelocity(this.getVelocity().add(0.0, 0.1, 0.0));

		if (this.getVelocity().getY() > AdAstra.CONFIG.lander.boosterThreshold) {
			this.setVelocity(0.0, AdAstra.CONFIG.lander.boosterThreshold, 0.0);
		}

		// Particles
		if (this.world instanceof ServerWorld serverWorld) {
			Vec3d pos = this.getPos();
			ModUtils.spawnForcedParticles(serverWorld, ParticleTypes.SPIT, pos.getX(), pos.getY() - 0.3, pos.getZ(), 3, 0.1, 0.1, 0.1, 0.001);
		}
	}
}