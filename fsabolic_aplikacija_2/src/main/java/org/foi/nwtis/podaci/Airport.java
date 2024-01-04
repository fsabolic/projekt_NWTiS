package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Klasa koja predstavlja zračnu luku. Sadrži podatke o identifikacijskom oznakom, tipu, nazivu,
 * visini, kontinentu, državi, regiji, općini, GPS kodu, IATA kodu, lokalnom kodu i koordinatama
 * zračne luke.
 * 
 * @author Dragutin Kermek
 * @version 2.3.0
 */
@AllArgsConstructor()
public class Airport {

  /**
   * Identifikacijska oznaka zračne luke.
   */
  @Getter
  @Setter
  private String ident;

  /**
   * Tip zračne luke.
   */
  @Getter
  @Setter
  private String type;

  /**
   * Naziv zračne luke.
   */
  @Getter
  @Setter
  private String name;

  /**
   * Visina zračne luke u stopama.
   */
  @Getter
  @Setter
  private String elevation_ft;

  /**
   * Kontinent na kojem se nalazi zračna luka.
   */
  @Getter
  @Setter
  private String continent;

  /**
   * Država u kojoj se nalazi zračna luka.
   */
  @Getter
  @Setter
  private String iso_country;

  /**
   * Regija u kojoj se nalazi zračna luka.
   */
  @Getter
  @Setter
  private String iso_region;

  /**
   * Općina u kojoj se nalazi zračna luka.
   */
  @Getter
  @Setter
  private String municipality;

  /**
   * GPS kod zračne luke.
   */
  @Getter
  @Setter
  private String gps_code;

  /**
   * IATA kod zračne luke.
   */
  @Getter
  @Setter
  private String iata_code;

  /**
   * Lokalni kod zračne luke.
   */
  @Getter
  @Setter
  private String local_code;

  /**
   * Koordinate zračne luke.
   */
  @Getter
  @Setter
  private String coordinates;

  /**
   * Konstruktor bez parametara za klasu Airport.
   */
  public Airport() {}
}
