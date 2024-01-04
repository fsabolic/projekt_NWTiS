package org.foi.nwtis.podaci;

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
   * Lokacija aerodroma.
   */
  @Getter
  @Setter
  private Lokacija lokacija;

  /**
   * 
   * Konstruktor bez parametara za objekt aerodroma.
   */
  public Aerodrom() {}
}
