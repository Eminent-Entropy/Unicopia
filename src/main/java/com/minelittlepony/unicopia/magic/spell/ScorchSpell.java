package com.minelittlepony.unicopia.magic.spell;

import javax.annotation.Nullable;

import com.minelittlepony.unicopia.magic.Affinity;
import com.minelittlepony.unicopia.magic.Caster;
import com.minelittlepony.unicopia.magic.TossedMagicEffect;
import com.minelittlepony.unicopia.util.PosHelper;
import com.minelittlepony.unicopia.util.particles.UParticles;
import com.minelittlepony.unicopia.util.projectile.AdvancedProjectile;
import com.minelittlepony.unicopia.util.shape.Sphere;

import net.minecraft.block.BlockState;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.explosion.Explosion.DestructionType;

public class ScorchSpell extends FireSpell implements TossedMagicEffect {

    @Override
    public String getName() {
        return "scorch";
    }

    @Override
    public boolean isCraftable() {
        return false;
    }

    @Override
    public int getTint() {
        return 0;
    }

    @Override
    public boolean update(Caster<?> source) {

        BlockPos pos = PosHelper.findSolidGroundAt(source.getWorld(), source.getOrigin());

        BlockState state = source.getWorld().getBlockState(pos);

        BlockState newState = affected.getConverted(state);

        if (!state.equals(newState)) {
            source.getWorld().setBlockState(pos, newState, 3);
            source.spawnParticles(new Sphere(false, 1), 5, p -> {
                p = PosHelper.offset(p, pos);

                source.getWorld().addParticle(ParticleTypes.SMOKE, p.x, p.y, p.z, 0, 0, 0);
            });
        }

        return true;
    }

    @Override
    public void render(Caster<?> source) {
        source.spawnParticles(ParticleTypes.FLAME, 3);
        source.spawnParticles(UParticles.UNICORN_MAGIC, 3); // getTint()
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.BAD;
    }

    @Override
    @Nullable
    public AdvancedProjectile toss(Caster<?> caster) {
        AdvancedProjectile projectile = TossedMagicEffect.super.toss(caster);

        if (projectile != null) {
            projectile.setGravity(false);
        }

        return projectile;
    }

    @Override
    public void onImpact(Caster<?> caster, BlockPos pos, BlockState state) {
        if (caster.isLocal()) {
            caster.getWorld().createExplosion(caster.getOwner(), pos.getX(), pos.getY(), pos.getZ(), 2, DestructionType.DESTROY);
        }
    }
}
