package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 
 * Predstavlja geografsku lokaciju.
 * 
 * Geografska lokacija sastoji se od širine i dužine.
 * 
 * @author Dragutin Kermek
 * 
 * @version 2.3.0
 */
@AllArgsConstructor()
public class Lokacija {

  /**
   * Geografska širina.
   */
  @Getter
  @Setter
  private String latitude;

  /**
   * Geografska dužina.
   */
  @Getter
  @Setter
  private String longitude;

  /**
   * Konstruktor bez parametara za objekt Lokacija.
   */
  public Lokacija() {}

  /**
   * Postavlja vrijednosti za širinu i dužinu.
   * 
   * @param latitude geografska širina
   * @param longitude geografska dužina
   */
  public void postavi(String latitude, String longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
