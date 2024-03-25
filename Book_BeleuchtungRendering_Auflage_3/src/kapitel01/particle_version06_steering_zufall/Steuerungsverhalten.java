package kapitel01.particle_version06_steering_zufall;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import kapitel04.Vektor2D;

public class Steuerungsverhalten {
   public Vektor2D acceleration;
   private Random zuf = ThreadLocalRandom.current();
   
   public Steuerungsverhalten() {
      acceleration = new Vektor2D(0, 0);
   }
   
   public void resetAcceleration() {
      acceleration.mult(0);
   }
   
   public void applyForce(Vektor2D force) {
      Vektor2D forceHelp = new Vektor2D(force);
      acceleration.add(forceHelp);
   }
   
   public Vektor2D randomForce() {
      return new Vektor2D(zuf.nextFloat()*10-5, zuf.nextFloat()*10-5);
   }
}
