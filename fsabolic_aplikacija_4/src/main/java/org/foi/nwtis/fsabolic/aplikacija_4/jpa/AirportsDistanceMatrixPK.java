package org.foi.nwtis.fsabolic.aplikacija_4.jpa;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * 
 * Klasa primarnog ključa za Airports Distance Matrix
 * 
 */
@Embeddable
public class AirportsDistanceMatrixPK implements Serializable {
  /**
   * Jedinstveni identifikator za serijalizaciju objekta.
   */
  private static final long serialVersionUID = 1L;

  /**
   * 
   * ICAO oznaka polazišnog aerodroma.
   */
  @Column(name = "ICAO_FROM", insertable = false, updatable = false, unique = true,
      nullable = false, length = 10)
  private String icaoFrom;

  /**
   * 
   * ICAO oznaka odredišnog aerodroma.
   */
  @Column(name = "ICAO_TO", insertable = false, updatable = false, unique = true, nullable = false,
      length = 10)
  private String icaoTo;

  /**
   * 
   * Država u kojoj se nalazi aerodrom.
   */
  @Column(name = "COUNTRY", unique = true, nullable = false, length = 30)
  private String country;

  /**
   * 
   * Konstruktor bez parametara za klasu AirportsDistanceMatrixPK.
   */
  public AirportsDistanceMatrixPK() {}

  /**
   * 
   * Getter za ICAO oznaku polazišnog aerodroma.
   * 
   * @return ICAO oznaka polazišnog aerodroma.
   */
  public String getIcaoFrom() {
    return this.icaoFrom;
  }

  /**
   * 
   * Setter za ICAO oznaku polazišnog aerodroma.
   * 
   * @param icaoFrom ICAO oznaka polazišnog aerodroma.
   */
  public void setIcaoFrom(String icaoFrom) {
    this.icaoFrom = icaoFrom;
  }

  /**
   * 
   * Getter za ICAO oznaku odredišnog aerodroma.
   * 
   * @return ICAO oznaka odredišnog aerodroma.
   */
  public String getIcaoTo() {
    return this.icaoTo;
  }

  /**
   * 
   * Setter za ICAO oznaku odredišnog aerodroma.
   * 
   * @param icaoTo ICAO oznaka odredišnog aerodroma.
   */
  public void setIcaoTo(String icaoTo) {
    this.icaoTo = icaoTo;
  }

  /**
   * 
   * Getter za državu u kojoj se nalazi aerodrom.
   * 
   * @return Država u kojoj se nalazi aerodrom.
   */
  public String getCountry() {
    return this.country;
  }

  /**
   * 
   * Setter za državu u kojoj se nalazi aerodrom.
   * 
   * @param country Država u kojoj se nalazi aerodrom.
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * 
   * Metoda koja provjerava jednakost objekta AirportsDistanceMatrixPK s drugim objektom.
   * 
   * @param other Drugi objekt s kojim se uspoređuje.
   * @return true ako su objekti jednaki, false inače.
   */
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof AirportsDistanceMatrixPK)) {
      return false;
    }
    AirportsDistanceMatrixPK castOther = (AirportsDistanceMatrixPK) other;
    return this.icaoFrom.equals(castOther.icaoFrom) && this.icaoTo.equals(castOther.icaoTo)
        && this.country.equals(castOther.country);
  }

  /**
   * 
   * Metoda koja vraća hash vrijednost objekta.
   * 
   * @return Hash vrijednost objekta.
   */
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.icaoFrom.hashCode();
    hash = hash * prime + this.icaoTo.hashCode();
    hash = hash * prime + this.country.hashCode();

    return hash;

  }
}
