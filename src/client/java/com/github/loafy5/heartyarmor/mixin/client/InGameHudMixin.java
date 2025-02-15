package com.github.loafy5.heartyarmor.mixin.client;

import com.github.loafy5.heartyarmor.armorshield.ArmorShieldHolder;
import com.github.loafy5.heartyarmor.armorshield.EntityArmorShield;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

	@Redirect(
			method = "renderStatusBars",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getArmor()I")
	)
	private int getArmorShield(PlayerEntity instance) {
		EntityArmorShield armorShield = ArmorShieldHolder.getArmorShield(instance);
		if (armorShield == null) return 0;
		return Math.round(armorShield.getShield());
	}
}