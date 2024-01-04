package org.foi.nwtis.podaci;

import java.io.Serializable;

/**
 * Klasa koja predstavlja informacijski podatak. Sadrži trenutno vrijeme poslužitelja, broj
 * korisnika i broj aerodroma.
 */
public class InfoPodatak implements Serializable {
  private String trenutnoVrijemePosluzitelja = null;
  private String brojKorisnika = null;
  private String brojAerodroma = null;

  /**
   * Vraća trenutno vrijeme poslužitelja.
   * 
   * @return trenutno vrijeme poslužitelja
   */
  public String getTrenutnoVrijemePosluzitelja() {
    return trenutnoVrijemePosluzitelja;
  }

  /**
   * Postavlja trenutno vrijeme poslužitelja.
   * 
   * @param trenutnoVrijemePosluzitelja trenutno vrijeme poslužitelja
   */
  public void setTrenutnoVrijemePosluzitelja(String trenutnoVrijemePosluzitelja) {
    this.trenutnoVrijemePosluzitelja = trenutnoVrijemePosluzitelja;
  }

  /**
   * Vraća broj korisnika.
   * 
   * @return broj korisnika
   */
  public String getBrojKorisnika() {
    return brojKorisnika;
  }

  /**
   * Postavlja broj korisnika.
   * 
   * @param brojKorisnika broj korisnika
   */
  public void setBrojKorisnika(String brojKorisnika) {
    this.brojKorisnika = brojKorisnika;
  }

  /**
   * Vraća broj aerodroma.
   * 
   * @return broj aerodroma
   */
  public String getBrojAerodroma() {
    return brojAerodroma;
  }

  /**
   * Postavlja broj aerodroma.
   * 
   * @param brojAerodroma broj aerodroma
   */
  public void setBrojAerodroma(String brojAerodroma) {
    this.brojAerodroma = brojAerodroma;
  }
}
