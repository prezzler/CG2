package kapitel01.particle_version11_particlesystem;

import java.util.HashMap;

public class ObjektManager {
   private HashMap<Integer, Partikel> partikel;
   private static ObjektManager exemplar = new ObjektManager();

   private ObjektManager() {
      partikel = new HashMap<Integer, Partikel>();
   }

   public static ObjektManager getExemplar() {
      return exemplar;
   }

   public Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException("Clonen ist nicht erlaubt");
   }
   
   public void registrierePartikel(Partikel obj) {
      partikel.put(new Integer(obj.id), obj);
   }

   public void entfernePartikel(Partikel obj) {
      partikel.remove(obj);
   }
   
   public Partikel getPartikel(int objID) {
      return partikel.get(new Integer(objID));
   }
   
   public HashMap<Integer, Partikel> getPartikelMap() {
      return partikel;
   }
   
   public int getPartikelSize() {
      return partikel.size();
   }
}
