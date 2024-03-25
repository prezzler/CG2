package kapitel01.particle_version03_objektmanager;

public abstract class BewegendesObjekt extends BasisObjekt {
   public float ySpeed;
   
   public BewegendesObjekt(float xPos, float yPos, float ySpeed) {
      super(xPos, yPos);
      this.ySpeed = ySpeed;
   }

   public abstract void update();
}
