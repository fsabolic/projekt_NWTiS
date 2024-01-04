package org.foi.nwtis.podaci;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.fsabolic.aplikacija_4.jpa.Airports;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * Predstavlja objekt aerodroma. Objekt aerodroma sadrži informacije o ICAO kodu, nazivu, državi i
 * lokaciji aerodroma.
 * 
 * @author Dragutin Kermek
 * @version 2.3.0
 */
@AllArgsConstructor()
public class Aerodrom {

  /**
   * 
   * ICAO kod aerodroma.
   */
  @Getter
  @Setter
  private String icao;

  /**
   * 
   * Naziv aerodroma.
   */
  @Getter
  @Setter
  private String naziv;

  /**
   * 
   * Država u kojoj se nalazi aerodrom.
   */
  @Getter
  @Setter
  private String drzava;

  /**
   * 
   * Status aerodroma;
   */
  @Getter
  @Setter
  private boolean aktivan;
  
  /**
   * 
   * Lokacija aerodroma.
   */
  @Getter
  @Setter
  private Lokacija lokacija;

  /**
   * Stvara objekt Lokacija na temelju koordinata aerodroma.
   *
   * @param koordinate Koordinate aerodroma u formatu "latitude, longitude"
   * @return Objekt Lokacija koji predstavlja lokaciju aerodroma
   */
  private Lokacija vratiLokaciju(String koordinate) {
    Lokacija lokacija = new Lokacija();
    String[] stringKoordinate = koordinate.split(", ");
    lokacija.setLatitude(stringKoordinate[0]);
    lokacija.setLongitude(stringKoordinate[1]);
    return lokacija;
  }

  /**
   * Konstruktor koji stvara objekt Aerodrom na temelju objekta Airports.
   *
   * @param aerodrom Objekt Airports koji sadrži informacije o aerodromu
   */
  public Aerodrom(Airports aerodrom) {
    this.icao = aerodrom.getIcao();
    this.naziv = aerodrom.getName();
    this.drzava = aerodrom.getIsoCountry();
    this.lokacija = vratiLokaciju(aerodrom.getCoordinates());
  }


  /**
   * 
   * Konstruktor bez parametara za objekt aerodroma.
   */
  public Aerodrom() {}

  /**
   * Metoda koja konvertira listu objekata Airports u listu objekata Aerodrom.
   *
   * @param aerodromi Lista objekata Airports koje treba konvertirati
   * @return Lista objekata Aerodrom rezultirana konverzijom
   */
  public static List<Aerodrom> vratiAerodrome(List<Airports> aerodromi) {
    List<Aerodrom> aerodromiKonv = new ArrayList<Aerodrom>();
    for (Airports a : aerodromi) {
      aerodromiKonv.add(new Aerodrom(a));
    }
    return aerodromiKonv;
  }
}
