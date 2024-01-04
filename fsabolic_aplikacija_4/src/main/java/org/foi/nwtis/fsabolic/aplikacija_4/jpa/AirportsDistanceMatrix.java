package org.foi.nwtis.fsabolic.aplikacija_4.jpa;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * 
 * Klasa za airports distance matrix entitet
 * 
 */
@Entity
@Table(name = "AIRPORTS_DISTANCE_MATRIX")
@NamedQuery(name = "AirportsDistanceMatrix.findAll",
    query = "SELECT a FROM AirportsDistanceMatrix a")
public class AirportsDistanceMatrix implements Serializable {
  /**
   * Jedinstveni identifikator za serijalizaciju objekta.
   */
  private static final long serialVersionUID = 1L;

  /**
   * 
   * Primarni ključ entiteta AirportsDistanceMatrix koji se sastoji od ICAO oznaka aerodroma od i
   * do.
   */
  @EmbeddedId
  private AirportsDistanceMatrixPK id;

  /**
   * 
   * Udaljenost između aerodroma u istoj državi.
   */
  @Column(name = "DIST_CTRY")
  private float distCtry;

  /**
   * 
   * Ukupna udaljenost između aerodroma.
   */
  @Column(name = "DIST_TOT")
  private float distTot;

  /**
   * 
   * Napomena.
   */
  @Column(name = "NOTE", length = 30)
  private String note;

  /**
   * 
   * Aerodrom od kojeg se računa udaljenost.
   */
  // bi-directional many-to-one association to Airport
  @ManyToOne
  @JoinColumn(name = "ICAO_FROM", nullable = false, insertable = false, updatable = true)
  private Airports airport1;

  /**
   * 
   * Aerodrom do kojeg se računa udaljenost.
   */
  // bi-directional many-to-one association to Airport
  @ManyToOne
  @JoinColumn(name = "ICAO_TO", nullable = false, insertable = false, updatable = true)
  private Airports airport2;

  /**
   * 
   * Konstruktor bez parametara za klasu AirportsDistanceMatrix.
   */
  public AirportsDistanceMatrix() {}

  /**
   * 
   * Getter za primarni ključ entiteta AirportsDistanceMatrix.
   * 
   * @return Primarni ključ entiteta AirportsDistanceMatrix.
   */
  public AirportsDistanceMatrixPK getId() {
    return this.id;
  }

  /**
   * 
   * Setter za primarni ključ entiteta AirportsDistanceMatrix.
   * 
   * @param id Primarni ključ entiteta AirportsDistanceMatrix.
   */
  public void setId(AirportsDistanceMatrixPK id) {
    this.id = id;
  }

  /**
   * 
   * Getter za udaljenost između aerodroma u istoj državi.
   * 
   * @return Udaljenost između aerodroma u istoj državi.
   */
  public float getDistCtry() {
    return this.distCtry;
  }

  /**
   * 
   * Setter za udaljenost između aerodroma u istoj državi.
   * 
   * @param distCtry Udaljenost između aerodroma u istoj državi.
   */
  public void setDistCtry(float distCtry) {
    this.distCtry = distCtry;
  }

  /**
   * 
   * Getter za ukupnu udaljenost između aerodroma.
   * 
   * @return Ukupna udaljenost između aerodroma.
   */
  public float getDistTot() {
    return this.distTot;
  }

  /**
   * 
   * Setter za ukupnu udaljenost između aerodroma.
   * 
   * @param distTot Ukupna udaljenost između aerodroma.
   */
  public void setDistTot(float distTot) {
    this.distTot = distTot;
  }

  /**
   * 
   * Getter za napomenu.
   * 
   * @return Napomena.
   */
  public String getNote() {
    return this.note;
  }

  /**
   * 
   * Setter za napomenu.
   * 
   * @param note Napomena.
   */
  public void setNote(String note) {
    this.note = note;
  }

  /**
   * 
   * Getter za aerodrom od kojeg se računa udaljenost.
   * 
   * @return Aerodrom od kojeg se računa udaljenost.
   */
  public Airports getAirport1() {
    return this.airport1;
  }

  /**
   * 
   * Setter za aerodrom od kojeg se računa udaljenost.
   * 
   * @param airport1 Aerodrom od kojeg se računa udaljenost.
   */
  public void setAirport1(Airports airport1) {
    this.airport1 = airport1;
  }

  /**
   * 
   * Getter za aerodrom do kojeg se računa udaljenost.
   * 
   * @return Aerodrom do kojeg se računa udaljenost.
   */
  public Airports getAirport2() {
    return this.airport2;
  }

  /**
   * 
   * Setter za aerodrom do kojeg se računa udaljenost.
   * 
   * @param airport2 Aerodrom do kojeg se računa udaljenost.
   */
  public void setAirport2(Airports airport2) {
    this.airport2 = airport2;
  }
}
