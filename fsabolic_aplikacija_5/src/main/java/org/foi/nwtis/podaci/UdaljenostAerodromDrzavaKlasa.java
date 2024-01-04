package org.foi.nwtis.podaci;

/**
 * 
 * Predstavlja klasu koja sadrži informacije o udaljenosti aerodroma i države. Klasa sadrži
 * identifikacijski kod aerodroma (ICAO kod), naziv države te udaljenost između aerodroma i države u
 * kilometrima.
 * 
 */
public class UdaljenostAerodromDrzavaKlasa {

  /**
   * 
   * ICAO kod aerodroma.
   */
  private String icao;

  /**
   * 
   * Naziv države.
   */
  private String drzava;

  /**
   * 
   * Udaljenost između aerodroma i države u kilometrima.
   */
  private float km;

  /**
   * 
   * Konstruktor koji inicijalizira objekt UdaljenostAerodromDrzavaKlasa s zadanim vrijednostima.
   * 
   * @param icao ICAO kod aerodroma
   * @param drzava naziv države
   * @param km udaljenost između aerodroma i države u kilometrima
   */
  public UdaljenostAerodromDrzavaKlasa(String icao, String drzava, float km) {
    super();
    this.icao = icao;
    this.drzava = drzava;
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
   * Vraća naziv države.
   * 
   * @return naziv države
   */
  public String getDrzava() {
    return drzava;
  }

  /**
   * 
   * Postavlja naziv države.
   * 
   * @param drzava naziv države
   */
  public void setDrzava(String drzava) {
    this.drzava = drzava;
  }

  /**
   * 
   * Vraća udaljenost između aerodroma i države u kilometrima.
   * 
   * @return udaljenost između aerodroma i države u kilometrima
   */
  public float getKm() {
    return km;
  }

  /**
   * 
   * Postavlja udaljenost između aerodroma i države u kilometrima.
   * 
   * @param km udaljenost između aerodroma i države u kilometrima
   */
  public void setKm(float km) {
    this.km = km;
  }
}
