package org.foi.nwtis.podaci;

import java.util.ArrayList;
import java.util.List;
import org.foi.nwtis.fsabolic.aplikacija_2.jpa.Airports;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Klasa koja predstavlja aerodrom. Sadrži podatke o identifikacijskom oznakom (ICAO kod), nazivu,
 * državi i lokaciji aerodroma.
 * 
 * @author Dragutin Kermek
 * @version 2.3.0
 */
@AllArgsConstructor()
public class Aerodrom {

  /**
   * Identifikacijska oznaka (ICAO kod) aerodroma.
   */
  @Getter
  @Setter
  private String icao;

  /**
   * Naziv aerodroma.
   */
  @Getter
  @Setter
  private String naziv;

  /**
   * Država u kojoj se nalazi aerodrom.
   */
  @Getter
  @Setter
  private String drzava;

  /**
   * Lokacija aerodroma (latitude i longitude).
   */
  @Getter
  @Setter
  private Lokacija lokacija;

  /**
   * Konstruktor bez parametara za klasu Aerodrom.
   */
  public Aerodrom() {}

  /**
   * Konstruktor koji stvara objekt klase Aerodrom na temelju objekta klase Airports.
   * 
   * @param aerodrom objekt klase Airports koji se pretvara u objekt klase Aerodrom
   */
  public Aerodrom(Airports aerodrom) {
    this.icao = aerodrom.getIcao();
    this.naziv = aerodrom.getName();
    this.drzava = aerodrom.getIsoCountry();
    this.lokacija = vratiLokaciju(aerodrom.getCoordinates());
  }

  /**
   * Privatna pomoćna metoda koja pretvara koordinate iz stringa u objekt klase Lokacija.
   * 
   * @param koordinate string koji sadrži koordinate aerodroma
   * @return objekt klase Lokacija s pretvorenim koordinatama aerodroma
   */
  private Lokacija vratiLokaciju(String koordinate) {
    Lokacija lokacija = new Lokacija();
    String[] stringKoordinate = koordinate.split(", ");
    lokacija.setLatitude(stringKoordinate[0]);
    lokacija.setLongitude(stringKoordinate[1]);
    return lokacija;
  }

  /**
   * Statička metoda koja pretvara listu objekata klase Airports u listu objekata klase Aerodrom.
   * 
   * @param aerodromi lista objekata klase Airports koja se pretvara
   * @return lista objekata klase Aerodrom dobivena pretvorbom
   */
  public static List<Aerodrom> vratiAerodrome(List<Airports> aerodromi) {
    List<Aerodrom> aerodromiKonv = new ArrayList<Aerodrom>();
    for (Airports a : aerodromi) {
      aerodromiKonv.add(new Aerodrom(a));
    }
    return aerodromiKonv;
  }
}
