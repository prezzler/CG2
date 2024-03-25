package kapitel01.particle_version13_focus_on_swarm;

import kapitel04.Vektor2D;

public abstract class BasisObjekt {
   public int id;
   public Vektor2D position;
   
   public BasisObjekt() {
      this(new Vektor2D(0,0));
   }
   
   public BasisObjekt(Vektor2D position) {
      this.position = new Vektor2D(position);
   }
   
   public Vektor2D getPosition() {
	   return position;
   }
   
   public void setPosition(Vektor2D pos) {
	   position = new Vektor2D(pos);
   }
  
   public abstract void render();
}
