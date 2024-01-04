package org.foi.nwtis.fsabolic.aplikacija_3.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 * Klasa predstavlja entitet AerodromiLetovi.
 */
@Entity
@Table(name = "AERODROMI_LETOVI")
public class AerodromiLetovi implements Serializable {
  /***
   * Serijski broj za serijalizaciju
   */
  private static final long serialVersionUID = 1L;

  /**
   * ID aerodroma.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "AERODROMI_LETOVI_ID")
  private Integer aerodromiLetoviId;

  /**
   * ICAO kod aerodroma.
   */
  @Column(name = "ICAO")
  private String icao;

  /**
   * Označava je li aerodrom aktivan.
   */
  @Column(name = "AKTIVAN")
  private boolean aktivan;

  /**
   * Konstruktor bez parametara za klasu AerodromiLetovi.
   */
  public AerodromiLetovi() {}

  /**
   * Vraća ID letova aerodroma.
   *
   * @return ID letova aerodroma
   */
  public Integer getAerodromiLetoviId() {
    return aerodromiLetoviId;
  }

  /**
   * Postavlja ID letova aerodroma.
   *
   * @param aerodromiLetoviId ID letova aerodroma
   */
  public void setAerodromiLetoviId(Integer aerodromiLetoviId) {
    this.aerodromiLetoviId = aerodromiLetoviId;
  }

  /**
   * Vraća ICAO kod aerodroma.
   *
   * @return ICAO kod aerodroma
   */
  public String getIcao() {
    return icao;
  }

  /**
   * Postavlja ICAO kod aerodroma.
   *
   * @param icao ICAO kod aerodroma
   */
  public void setIcao(String icao) {
    this.icao = icao;
  }

  /**
   * Provjerava je li aerodrom aktivan.
   *
   * @return true ako je aerodrom aktivan, false inače
   */
  public boolean isAktivan() {
    return aktivan;
  }

  /**
   * Postavlja status aktivnosti aerodroma.
   *
   * @param aktivan true ako je aerodrom aktivan, false inače
   */
  public void setAktivan(boolean aktivan) {
    this.aktivan = aktivan;
  }
}
