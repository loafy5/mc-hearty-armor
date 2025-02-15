package com.github.loafy5.heartyarmor.armorshield;

import com.github.loafy5.heartyarmor.HeartyArmor;
import com.github.loafy5.heartyarmor.utils.UtilsClass;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;

public class EntityArmorShield {

    public static final float MAXSHIELD_PER_ARMOR = 1;
    public static final float BASE_SHIELD_RECHARGE_RATE = 1.0f;
    public static final float RECHARGE_RATE_PER_TOUGHNESS = 0.1f;
    public static final int UPDATE_TIME = 10;
    public static final int STAT_UPDATE_TIME = 40;
    public static final int BASE_RECHARGE_DELAY = 200;

    public LivingEntity entity;
    private float shield;
    private float maxShield;

    private float rechargeRate = BASE_SHIELD_RECHARGE_RATE;

    private float rechargeTimer;
    private int delayTimer;

    public EntityArmorShield(LivingEntity entity) {
        this.entity = entity;
        updateStats();
    }

    public void tick() {

        if (entity.getWorld().getTime() % UPDATE_TIME == 0) {
        if (shield < maxShield) {
            // run every 'UPDATE_TIME' ticks

            // waiting for recharge delay
            if (delayTimer > 0) {
                delayTimer -= UPDATE_TIME;

            } else {
                rechargeTimer += rechargeRate * UtilsClass.toSeconds(UPDATE_TIME);
                shield = Math.min(shield + (int)rechargeTimer, maxShield);
                rechargeTimer -= (int)rechargeTimer;
            }
        } else if (delayTimer < BASE_RECHARGE_DELAY) {delayTimer = BASE_RECHARGE_DELAY; }
        }

        if ((entity.getWorld().getTime() & STAT_UPDATE_TIME) == 0) {
            updateStats();
        }
    }

    public void updateStats() {
        maxShield = entity.getArmor() * MAXSHIELD_PER_ARMOR;
        rechargeRate = BASE_SHIELD_RECHARGE_RATE +
                (BASE_SHIELD_RECHARGE_RATE *
                 (float)entity.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS) *
                        RECHARGE_RATE_PER_TOUGHNESS);
    }

    public float damage(float damage) {
        delayTimer = Math.max(BASE_RECHARGE_DELAY, delayTimer);
        //if (shield <= 0) { return damage; }

        float returnValue = Math.max(damage - shield, 0);
        shield = Math.max(shield - damage, 0);

        return returnValue;
    }

    public float getShield() {
        return shield;
    }

}
