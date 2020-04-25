package com.minelittlepony.unicopia.toxin;

import com.minelittlepony.unicopia.item.UEffects;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface Toxin {
    Toxin DAMAGE = (player, toxicity, stack) -> {
        if (player.world.random.nextInt(30) == 0) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_DAMAGE, 1, 1, false, false));
        }
    };
    Toxin RADIOACTIVITY = (player, toxicity, stack) -> {
        if (player.world.random.nextInt(30) == 0) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 10, 1, false, false));
        }
    };
    Toxin NAUSEA = (player, toxicity, stack) -> {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 30, 1, false, false));
    };
    Toxin STRENGTH = (player, toxicity, stack) -> {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 30, 1, false, false));
    };
    Toxin BLINDNESS = (player, toxicity, stack) -> {
        player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 30, 1, false, false));
    };
    Toxin FOOD = (player, toxicity, stack) -> {
        if (toxicity.toxicWhenRaw()) {
            player.addStatusEffect(toxicity.getPoisonEffect());
        }

        if (toxicity.isLethal()) {
            player.addStatusEffect(new StatusEffectInstance(UEffects.FOOD_POISONING, 300, 7, false, false));
        } else if (toxicity.toxicWhenCooked()) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 3, 1, false, false));
        }
    };

    void addSecondaryEffects(PlayerEntity player, Toxicity toxicity, ItemStack stack);

    default Toxin and(Toxin other) {
        return (player, toxicity, stack) -> {
            other.addSecondaryEffects(player, toxicity, stack);
            this.addSecondaryEffects(player, toxicity, stack);
        };
    }
}