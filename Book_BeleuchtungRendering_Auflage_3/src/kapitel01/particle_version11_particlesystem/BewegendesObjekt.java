package kapitel01.particle_version11_particlesystem;

import kapitel04.Vektor2D;

public abstract class BewegendesObjekt extends BasisObjekt {
   public Vektor2D velocity;
   public Vektor2D acceleration;
   public Verhalten verhalten = null;
   
   public BewegendesObjekt(Vektor2D position, Vektor2D velocity) {
      super(position);
      this.velocity = new Vektor2D(velocity);
      this.acceleration = new Vektor2D(0, 0);
   }
   
   public void resetAcceleration() {
      acceleration.mult(0);
   }

   public void applyForce(Vektor2D force) {
      Vektor2D forceHelp = new Vektor2D(force);
      acceleration.add(forceHelp);
   }
      
   public void setVerhalten(Verhalten verhalten) {
      this.verhalten = verhalten;
   }

   public void update(double time) {
      if (verhalten!=null)
         verhalten.update(time);
   }
}
