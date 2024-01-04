package org.foi.nwtis.podaci;

/**
 * 
 * Predstavlja klasu koja sadrži informacije o udaljenosti aerodroma. Klasa sadrži identifikacijski
 * kod aerodroma (ICAO kod) te udaljenost aerodroma u kilometrima.
 * 
 */
public class UdaljenostAerodromKlasa {

  /**
   * 
   * ICAO kod aerodroma.
   */
  private String icao;

  /**
   * 
   * Udaljenost aerodroma u kilometrima.
   */
  private float km;

  /**
   * 
   * Konstruktor koji inicijalizira objekt UdaljenostAerodromKlasa s zadanim vrijednostima.
   * 
   * @param icao ICAO kod aerodroma
   * @param km udaljenost aerodroma u kilometrima
   */
  public UdaljenostAerodromKlasa(String icao, float km) {
    super();
    this.icao = icao;
    this.km = km;
  }

  /**
   * 
   * Vraća ICAO kod aerodroma.
   * 
   * @return ICAO kod aerodroma
   */
  public String getIcao() {
    return icao;
  }

  /**
   * 
   * Postavlja ICAO kod aerodroma.
   * 
   * @param icao ICAO kod aerodroma
   */
  public void setIcao(String icao) {
    this.icao = icao;
  }

  /**
   * 
   * Vraća udaljenost aerodroma u kilometrima.
   * 
   * @return udaljenost aerodroma u kilometrima
   */
  public float getKm() {
    return km;
  }

  /**
   * 
   * Postavlja udaljenost aerodroma u kilometrima.
   * 
   * @param km udaljenost aerodroma u kilometrima
   */
  public void setKm(float km) {
    this.km = km;
  }
}
