package com.minelittlepony.unicopia.client.particle;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.world.World;

import com.minelittlepony.unicopia.client.render.model.SphereModel;
import com.minelittlepony.unicopia.magic.Caster;
import com.minelittlepony.unicopia.particles.ParticleHandle.Attachment;
import com.minelittlepony.util.Color;

public class SphereParticle extends Particle implements Attachment {

    protected float red;
    protected float green;
    protected float blue;
    protected float alpha;

    protected float radius;

    private Caster<?> caster;

    private static final SphereModel model = new SphereModel();

    public SphereParticle(World w, double x, double y, double z, float radius, int red, int green, int blue, float alpha, double vX, double vY, double vZ) {
        this(w, x, y, z, radius, red, green, blue, alpha);

        this.velocityX = vX;
        this.velocityY = vY;
        this.velocityZ = vZ;
    }

    public SphereParticle(World w, double x, double y, double z, float radius, int red, int green, int blue, float alpha) {
        super(w, x, y, z);

        this.radius = radius;
        this.red = red/255F;
        this.green = green/255F;
        this.blue = blue/255F;
        this.alpha = alpha;

        setMaxAge(10);
    }

    @Override
    public boolean isStillAlive() {
        return age < (maxAge - 1);
    }

    @Override
    public void attach(Caster<?> caster) {
        setMaxAge(50000);
        this.caster = caster;
    }

    @Override
    public void setAttribute(int key, Object value) {
        if (key == 0) {
            radius = (float)value;
        }
        if (key == 1) {
            int tint = (int)value;
            red = Color.r(tint);
            green = Color.g(tint);
            blue = Color.b(tint);
            alpha = Color.a(tint);
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.CUSTOM;
    }

    @Override
    public void tick() {
        super.tick();

        if (caster != null) {
            if (!caster.hasEffect() || caster.getEffect().isDead() || caster.getEntity().removed) {
                markDead();
            } else {
                Entity e = caster.getEntity();

                if (caster.getWorld().getEntityById(e.getEntityId()) == null) {
                    markDead();
                }

                setPos(e.getX(), e.getY(), e.getZ());
            }
        } else {
            radius *= 0.9998281;
        }
    }

    @Override
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {
        if (alpha <= 0 || radius <= 0) {
            return;
        }

        MatrixStack matrices = new MatrixStack();
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        model.setPosition(x, y, z);
        model.render(matrices, radius, immediate.getBuffer(RenderLayer.getTranslucent()), 1, 1, red, green, blue, alpha);
        immediate.draw();
    }

    public static class Factory implements ParticleFactory<DefaultParticleType> {
        public Factory(SpriteProvider provider) {
        }

        @Override
        public Particle createParticle(DefaultParticleType type, World world, double x, double y, double z, double dx, double dy, double dz) {
            return new RaindropsParticle(world, x, y, z, dx, dy, dz);
        }
    }
}
