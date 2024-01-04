package org.foi.nwtis.klase;

import com.google.gson.Gson;
import jakarta.ws.rs.core.Response;

/***
 * Klasa za rad s Json odgovorima
 * 
 * @author Fran Sabolić
 *
 */
public class JsonOdgovor {

  /***
   * Metoda koja dani objekt pretvara u json odgovor
   * 
   * @param objekt objekt
   * @return odgovor u json obliku
   */
  public static Response vratiJsonOdgovor(Object objekt) {
    var gson = new Gson();
    var jsonOdgovor = gson.toJson(objekt);
    var odgovor = Response.ok().entity(jsonOdgovor).build();
    return odgovor;
  }

  /***
   * Metoda koja vraća određeni Json odgovor
   * 
   * @param status željeni status
   * @param poruka željena poruka odgovora
   * @return odgovor u json obliku
   */
  public static Response vratiVlastitiJsonOdgovor(int status, String poruka) {
    return Response.status(status, poruka).build();
  }

}
