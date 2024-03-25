package kapitel01.particle_version08_steering_separation;

import kapitel04.Vektor2D;

public abstract class BewegendesObjekt extends BasisObjekt {
   public Vektor2D velocity;
   public Verhalten verhalten = null;
   
   public BewegendesObjekt(Vektor2D position, Vektor2D velocity) {
      super(position);
      this.velocity = new Vektor2D(velocity);
   }
      
   public void setVerhalten(Verhalten verhalten) {
      this.verhalten = verhalten;
   }

   public void update() {
      if (verhalten!=null)
         verhalten.update();
   }
}
