package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * Predstavlja objekt aerodroma.
 * 
 * Objekt aerodroma sadrži informacije o identifikatoru, tipu, nazivu, nadmorskoj visini,
 * 
 * kontinentu, ISO kodu države, ISO kodu regije, općini, GPS kodu, IATA kodu, lokalnom kodu
 * 
 * i koordinatama aerodroma.
 * 
 * @author Dragutin Kermek
 * 
 * @version 2.3.0
 */
@AllArgsConstructor()
public class Airport {

  /**
   * Identifikator aerodroma.
   */
  @Getter
  @Setter
  private String ident;

  /**
   * Tip aerodroma.
   */
  @Getter
  @Setter
  private String type;

  /**
   * Naziv aerodroma.
   */
  @Getter
  @Setter
  private String name;

  /**
   * Nadmorska visina aerodroma.
   */
  @Getter
  @Setter
  private String elevation_ft;

  /**
   * Kontinent na kojem se nalazi aerodrom.
   */
  @Getter
  @Setter
  private String continent;

  /**
   * ISO kod države u kojoj se nalazi aerodrom.
   */
  @Getter
  @Setter
  private String iso_country;

  /**
   * ISO kod regije u kojoj se nalazi aerodrom.
   */
  @Getter
  @Setter
  private String iso_region;

  /**
   * Općina u kojoj se nalazi aerodrom.
   */
  @Getter
  @Setter
  private String municipality;

  /**
   * GPS kod aerodroma.
   */
  @Getter
  @Setter
  private String gps_code;

  /**
   * IATA kod aerodroma.
   */
  @Getter
  @Setter
  private String iata_code;

  /**
   * Lokalni kod aerodroma.
   */
  @Getter
  @Setter
  private String local_code;

  /**
   * Koordinate aerodroma.
   */
  @Getter
  @Setter
  private String coordinates;

  /**
   * Konstruktor bez parametara za objekt aerodroma.
   */
  public Airport() {}
}
