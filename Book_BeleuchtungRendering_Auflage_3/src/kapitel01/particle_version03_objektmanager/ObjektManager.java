package kapitel01.particle_version03_objektmanager;

import java.util.HashMap;

public class ObjektManager {
   private HashMap<Integer, Flummi> flummies;

   // ****************************************************
   // ObjektManager als Singleton realisieren
   private static ObjektManager exemplar = new ObjektManager();

   private ObjektManager() {
      flummies = new HashMap<Integer, Flummi>();
   }

   public static ObjektManager getExemplar() {
      return exemplar;
   }

   public Object clone() throws CloneNotSupportedException {
      throw new CloneNotSupportedException("Clonen ist nicht erlaubt");
   }
   // ***************************************************
   
   public void registriereFlummi(Flummi obj) {
      flummies.put(obj.id, obj);
   }

   public void entferneFlummi(Flummi obj) {
      flummies.remove(obj);
   }
   
   public Flummi getFlummi(int objID) {
      return flummies.get(objID);
   }
   
   public HashMap<Integer, Flummi> getFlummiMap() {
      return flummies;
   }
   
   public int getFlummiSize() {
      return flummies.size();
   }
}
