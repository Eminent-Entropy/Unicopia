package com.minelittlepony.unicopia.ability;

import java.util.Optional;

import com.google.common.collect.Streams;
import com.minelittlepony.unicopia.Race;
import com.minelittlepony.unicopia.ability.data.Hit;
import com.minelittlepony.unicopia.ability.magic.Thrown;
import com.minelittlepony.unicopia.ability.magic.spell.AttractiveSpell;
import com.minelittlepony.unicopia.ability.magic.spell.SpellRegistry;
import com.minelittlepony.unicopia.entity.player.Pony;
import com.minelittlepony.unicopia.particle.MagicParticleEffect;

/**
 * A magic casting ability for unicorns.
 * (only shields for now)
 */
public class UnicornProjectileAbility implements Ability<Hit> {

    @Override
    public int getWarmupTime(Pony player) {
        return 4;
    }

    @Override
    public int getCooldownTime(Pony player) {
        return 0;
    }

    @Override
    public boolean canUse(Race race) {
        return race.canCast();
    }

    @Override
    public Hit tryActivate(Pony player) {
        return Hit.INSTANCE;
    }

    @Override
    public Hit.Serializer<Hit> getSerializer() {
        return Hit.SERIALIZER;
    }

    @Override
    public void apply(Pony player, Hit data) {
        getThrown(player).orElseGet(AttractiveSpell::new).toss(player);
    }

    private Optional<Thrown> getThrown(Pony player) {
        return Streams.stream(player.getMaster().getItemsHand())
                .map(SpellRegistry.instance()::getSpellFrom)
                .filter(i -> i != null && i instanceof Thrown)
                .map(Thrown.class::cast)
                .findFirst();
    }

    @Override
    public void preApply(Pony player, AbilitySlot slot) {
        player.getMagicalReserves().getEnergy().multiply(3.3F);
        player.spawnParticles(MagicParticleEffect.UNICORN, 5);
    }

    @Override
    public void postApply(Pony player, AbilitySlot slot) {
        player.spawnParticles(MagicParticleEffect.UNICORN, 5);
    }
}