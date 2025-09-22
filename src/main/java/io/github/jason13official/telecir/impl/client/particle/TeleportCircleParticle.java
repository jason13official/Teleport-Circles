package io.github.jason13official.telecir.impl.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.RisingParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

public class TeleportCircleParticle extends RisingParticle {

  public TeleportCircleParticle(ClientLevel level, double x, double y, double z, double xSpeed,
      double ySpeed, double zSpeed) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
  }

  @Override
  public void move(double x, double y, double z) {
    this.setBoundingBox(this.getBoundingBox().move(x, y, z));
    this.setLocationFromBoundingbox();
  }

  @Override
  public float getQuadSize(float scaleFactor) {
    float f = ((float) this.age + scaleFactor) / (float) this.lifetime;
    return this.quadSize * (1.0F - f * f * 0.5F);
  }

  @Override
  public int getLightColor(float partialTick) {
    float f = ((float) this.age + partialTick) / (float) this.lifetime;
    f = Mth.clamp(f, 0.0F, 1.0F);
    int i = super.getLightColor(partialTick);
    int j = i & 255;
    int k = i >> 16 & 255;
    j += (int) (f * 15.0F * 16.0F);
    if (j > 240) {
      j = 240;
    }

    return j | k << 16;
  }

  public static class Provider implements ParticleProvider<SimpleParticleType> {

    private final SpriteSet sprite;

    public Provider(SpriteSet sprites) {
      this.sprite = sprites;
    }

    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y,
        double z, double xSpeed, double ySpeed, double zSpeed) {
      TeleportCircleParticle circleParticle = new TeleportCircleParticle(level, x, y, z, xSpeed,
          ySpeed, zSpeed);
      circleParticle.pickSprite(this.sprite);
      return circleParticle;
    }
  }

  public static class SmallProvider implements ParticleProvider<SimpleParticleType> {

    private final SpriteSet sprite;

    public SmallProvider(SpriteSet sprites) {
      this.sprite = sprites;
    }

    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y,
        double z, double xSpeed, double ySpeed, double zSpeed) {
      TeleportCircleParticle circleParticle = new TeleportCircleParticle(level, x, y, z, xSpeed,
          ySpeed, zSpeed);
      circleParticle.pickSprite(this.sprite);
      circleParticle.scale(0.5F);
      return circleParticle;
    }
  }
}
