package com.github.alexnijjar.ad_astra.mixin.gravity;

import com.github.alexnijjar.ad_astra.AdAstra;
import com.github.alexnijjar.ad_astra.util.ModUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityGravityMixin {

	private static final double CONSTANT = 0.08;

	@Inject(method = "travel", at = @At("TAIL"), cancellable = true)
	public void travel(CallbackInfo ci) {
		if (AdAstra.CONFIG.general.doLivingEntityGravity) {
			LivingEntity entity = (LivingEntity) (Object) this;

			Vec3d velocity = entity.getVelocity();

			if (!entity.hasNoGravity() && !entity.isTouchingWater() && !entity.isInLava() && !entity.isFallFlying() && !entity.hasStatusEffect(StatusEffects.SLOW_FALLING)) {
				double newGravity = ModUtils.getMixinGravity(CONSTANT, this);
				entity.setVelocity(velocity.getX(), velocity.getY() + CONSTANT - newGravity, velocity.getZ());
			}
		}
	}

	// Make fall damage gravity-dependant
	@ModifyVariable(method = "handleFallDamage", at = @At("HEAD"), ordinal = 1)
	private float handleFallDamage(float damageMultiplier) {
		LivingEntity entity = ((LivingEntity) (Object) this);
		return damageMultiplier * ModUtils.getPlanetGravity(entity.world);
	}
}
