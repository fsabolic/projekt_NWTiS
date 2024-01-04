package org.foi.nwtis.podaci;

/**
 * 
 * Predstavlja klasu koja sadrži informacije o udaljenosti u odnosu na državu. Klasa sadrži naziv
 * države i udaljenost u kilometrima.
 * 
 */
public class UdaljenostKlasa {

  /**
   * 
   * Naziv države.
   */
  private String drzava;

  /**
   * 
   * Udaljenost u kilometrima.
   */
  private float km;

  /**
   * 
   * Konstruktor koji inicijalizira objekt UdaljenostKlasa s zadanim vrijednostima.
   * 
   * @param drzava naziv države
   * @param km udaljenost u kilometrima
   */
  public UdaljenostKlasa(String drzava, float km) {
    this.drzava = drzava;
    this.km = km;
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
   * Vraća udaljenost u kilometrima.
   * 
   * @return udaljenost u kilometrima
   */
  public float getKm() {
    return km;
  }

  /**
   * 
   * Postavlja udaljenost u kilometrima.
   * 
   * @param km udaljenost u kilometrima
   */
  public void setKm(float km) {
    this.km = km;
  }
}
