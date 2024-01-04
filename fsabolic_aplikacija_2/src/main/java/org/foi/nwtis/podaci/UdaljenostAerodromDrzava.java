package org.foi.nwtis.podaci;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.fsabolic.aplikacija_2.jpa.AirportsDistanceMatrix;

/***
 * Klasa za pohranu udaljenosti između 2 aerodroma
 * 
 * @author Fran Sabolić
 *
 */
public record UdaljenostAerodromDrzava(String icao, String drzava, float km) {
  /***
   * konvertira listu objekata AirportsDistanceMatrix u listu objekata UdaljenostAerodromDrzava,
   * pridružujući vrijednosti ICAO koda aerodroma, države i udaljenosti.
   * 
   * @param udaljenostiAerodroma udaljenost
   * @return udaljenost aerodroma
   */
  public static List<UdaljenostAerodromDrzava> vratiUdaljenostiAerodroma(
      List<AirportsDistanceMatrix> udaljenostiAerodroma) {
    List<UdaljenostAerodromDrzava> udaljenostiAerodromaKonv = new ArrayList<>();

    for (AirportsDistanceMatrix adm : udaljenostiAerodroma) {
      udaljenostiAerodromaKonv.add(new UdaljenostAerodromDrzava(adm.getAirport2().getIcao(),
          adm.getAirport2().getIsoCountry(), adm.getDistCtry()));
    }
    return udaljenostiAerodromaKonv;
  }
}
