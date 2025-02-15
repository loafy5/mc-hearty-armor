package com.github.loafy5.heartyarmor.mixin;

import com.github.loafy5.heartyarmor.HeartyArmor;
import com.github.loafy5.heartyarmor.armorshield.ArmorShieldHolder;
import com.github.loafy5.heartyarmor.armorshield.EntityArmorShield;
import net.minecraft.entity.DamageUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

	@Shadow public abstract boolean damage(DamageSource source, float amount);

	@Redirect(
			method = "applyArmorToDamage",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/DamageUtil;getDamageLeft(FFF)F")
	)
	private float damageArmorShield(float damage, float armor, float armorToughness) {
		// runs when taking damage (to reduce it with armor)
		if (HeartyArmor.IS_PLAYER_ONLY && !(getThis() instanceof PlayerEntity))
			return DamageUtil.getDamageLeft(damage, armor, armorToughness);

		if (!getThis().getWorld().isClient()) {
			EntityArmorShield armorShield = ArmorShieldHolder.getArmorShield(getThis());
			if (armorShield != null) {
			return armorShield.damage(damage);
			}
		}
		return damage;
	}

	@Inject(
			method = "remove",
			at = @At(value = "TAIL")
	)
	private void removeArmorShield(Entity.RemovalReason reason, CallbackInfo ci) {
		LivingEntity entity = (LivingEntity)(Object)this;
		if (entity.getWorld().isClient() || HeartyArmor.IS_PLAYER_ONLY && !(entity instanceof PlayerEntity)) return;

		ArmorShieldHolder.removeArmorShield(entity);
	}

	@Inject(
			method = "tick",
			at = @At(value = "TAIL")
	)
	private void tickArmorShield(CallbackInfo ci) {
		if (getThis().getWorld().isClient() || HeartyArmor.IS_PLAYER_ONLY && !(getThis() instanceof PlayerEntity)) return;

		EntityArmorShield armorShield = ArmorShieldHolder.getArmorShield(getThis());
		if (armorShield != null) {
			armorShield.tick();
		}
	}

	private LivingEntity getThis() {
		return (LivingEntity)(Object)this;
	}
}