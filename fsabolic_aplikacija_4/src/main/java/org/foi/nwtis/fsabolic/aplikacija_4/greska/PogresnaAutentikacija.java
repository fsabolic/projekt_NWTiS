package org.foi.nwtis.fsabolic.aplikacija_4.greska;

/**
 * Klasa PogresnaAutentikacija predstavlja iznimku koja se baca u slučaju pogrešne autentikacije.
 */
public class PogresnaAutentikacija extends Exception {

  /**
   * Konstruktor za stvaranje objekta PogresnaAutentikacija s određenom porukom pogreške.
   *
   * @param tekst poruka pogreške
   */
  public PogresnaAutentikacija(String tekst) {
    super(tekst);
  }
}