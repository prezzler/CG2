package kapitel01.particle_version07_steering_mausverfolgung;

public class VerhaltenMausVerfolgung implements Verhalten {
   private Flummi flummi;
   private Steuerungsverhalten steering;
   private final float MAX_VELOCITY = 10;
   
   public VerhaltenMausVerfolgung(Flummi flummi) {
      this.flummi   = flummi;
      this.steering = new Steuerungsverhalten();
   }
   
   @Override
   public void update() {
      steering.applyForce(steering.followMousePosition(flummi.position));
      
      flummi.velocity.add(steering.acceleration);
      flummi.velocity.truncate(MAX_VELOCITY);
      flummi.position.add(flummi.velocity);
      
      steering.resetAcceleration();
   }
}
