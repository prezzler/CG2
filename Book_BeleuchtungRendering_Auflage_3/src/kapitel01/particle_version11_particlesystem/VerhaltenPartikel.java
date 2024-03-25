package kapitel01.particle_version11_particlesystem;

import kapitel04.Vektor2D;

public class VerhaltenPartikel implements Verhalten {
   private Partikel partikel;
   private Steuerungsverhalten steering;
   private final float MAX_VELOCITY = 10;
   
   public VerhaltenPartikel(Partikel partikel) {
      this.partikel   = partikel;
      this.steering = new Steuerungsverhalten();
   }
   
   @Override
   public void update(double time) {
      Vektor2D mouseForce = steering.followMousePosition(partikel.position);
      mouseForce.mult(1f);
      
      partikel.applyForce(mouseForce);
      partikel.acceleration.mult((float)time);
      partikel.velocity.add(partikel.acceleration);
      partikel.velocity.truncate(MAX_VELOCITY);
      partikel.position.add(partikel.velocity);

      partikel.resetAcceleration();
   }
}
