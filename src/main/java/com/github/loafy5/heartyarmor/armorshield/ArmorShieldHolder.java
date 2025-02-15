package com.github.loafy5.heartyarmor.armorshield;

import net.minecraft.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

public class ArmorShieldHolder {

    private static ArmorShieldHolder instance;
    private static Map<LivingEntity, EntityArmorShield> armorShieldMap = new HashMap<>();

    private static void addArmorShield(LivingEntity entity, EntityArmorShield armorShield) {
        armorShieldMap.put(entity, armorShield);
    }

    public static EntityArmorShield getArmorShield(LivingEntity entity) {
        if (armorShieldMap.get(entity) == null && !entity.getWorld().isClient()) {
            addArmorShield(entity, new EntityArmorShield(entity));
        }
        return armorShieldMap.get(entity);
    }

    public static void removeArmorShield(LivingEntity entity) {
        armorShieldMap.remove(entity);
    }
}
