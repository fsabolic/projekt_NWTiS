package org.foi.nwtis.fsabolic.aplikacija_4.jpa;

import java.io.Serializable;
import java.sql.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * 
 * Klasa za letove polaska
 * 
 */
@Entity
@Table(name = "LETOVI_POLASCI")
@NamedQuery(name = "LetoviPolasci.findAll", query = "SELECT l FROM LetoviPolasci l")
public class LetoviPolasci implements Serializable {
  /**
   * Jedinstveni identifikator za serijalizaciju objekta.
   */
  private static final long serialVersionUID = 1L;

  /**
   * 
   * Jedinstveni identifikator leta.
   */
  @Id
  @Column(unique = true, nullable = false)
  private int id;

  /**
   * 
   * Broj potencijalnih dolaznih aerodroma.
   */
  @Column(nullable = false)
  private int arrivalAirportCandidatesCount;

  /**
   * 
   * Pozivni znak leta.
   */
  @Column(length = 20)
  private String callsign;

  /**
   * 
   * Broj potencijalnih odlaznih aerodroma.
   */
  @Column(nullable = false)
  private int departureAirportCandidatesCount;

  /**
   * 
   * ICAO oznaka dolaznog aerodroma.
   */
  @Column(nullable = false, length = 10)
  private String estArrivalAirport;

  /**
   * 
   * Horizontalna udaljenost do dolaznog aerodroma.
   */
  @Column(nullable = false)
  private int estArrivalAirportHorizDistance;

  /**
   * 
   * Vertikalna udaljenost do dolaznog aerodroma.
   */
  @Column(nullable = false)
  private int estArrivalAirportVertDistance;

  /**
   * 
   * Horizontalna udaljenost do odlaznog aerodroma.
   */
  @Column(nullable = false)
  private int estDepartureAirportHorizDistance;

  /**
   * 
   * Vertikalna udaljenost do odlaznog aerodroma.
   */
  @Column(nullable = false)
  private int estDepartureAirportVertDistance;

  /**
   * 
   * Vrijeme prvog očitanja leta.
   */
  @Column(nullable = false)
  private int firstSeen;

  /**
   * 
   * ICAO oznaka zrakoplova.
   */
  @Column(nullable = false, length = 30)
  private String icao24;

  /**
   * 
   * Vrijeme posljednjeg očitanja leta.
   */
  @Column(nullable = false)
  private int lastSeen;

  /**
   * 
   * Vrijeme spremanja podataka o letu.
   */
  @Column(nullable = false)
  private Timestamp stored;

  // bi-directional many-to-one association to Airport
  /**
   * 
   * Aerodrom s kojeg let polazi.
   */
  @ManyToOne
  @JoinColumn(name = "estDepartureAirport", nullable = false)
  private Airports airport;

  /**
   * 
   * Konstruktor bez parametara za klasu LetoviPolasci.
   */
  public LetoviPolasci() {}

  /**
   * 
   * Getter za jedinstveni identifikator leta.
   * 
   * @return Jedinstveni identifikator leta.
   */
  public int getId() {
    return this.id;
  }

  /**
   * 
   * Setter za jedinstveni identifikator leta.
   * 
   * @param id Jedinstveni identifikator leta.
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * 
   * Getter za broj potencijalnih dolaznih aerodroma.
   * 
   * @return Broj potencijalnih dolaznih aerodroma.
   */
  public int getArrivalAirportCandidatesCount() {
    return this.arrivalAirportCandidatesCount;
  }

  /**
   * 
   * Setter za broj potencijalnih dolaznih aerodroma.
   * 
   * @param arrivalAirportCandidatesCount Broj potencijalnih dolaznih aerodroma.
   */
  public void setArrivalAirportCandidatesCount(int arrivalAirportCandidatesCount) {
    this.arrivalAirportCandidatesCount = arrivalAirportCandidatesCount;
  }

  /**
   * 
   * Getter za pozivni znak leta.
   * 
   * @return Pozivni znak leta.
   */
  public String getCallsign() {
    return this.callsign;
  }

  /**
   * 
   * Setter za pozivni znak leta.
   * 
   * @param callsign Pozivni znak leta.
   */
  public void setCallsign(String callsign) {
    this.callsign = callsign;
  }

  /**
   * 
   * Getter za broj potencijalnih odlaznih aerodroma.
   * 
   * @return Broj potencijalnih odlaznih aerodroma.
   */
  public int getDepartureAirportCandidatesCount() {
    return this.departureAirportCandidatesCount;
  }

  /**
   * 
   * Setter za broj potencijalnih odlaznih aerodroma.
   * 
   * @param departureAirportCandidatesCount Broj potencijalnih odlaznih aerodroma.
   */
  public void setDepartureAirportCandidatesCount(int departureAirportCandidatesCount) {
    this.departureAirportCandidatesCount = departureAirportCandidatesCount;
  }

  /**
   * 
   * Getter za ICAO oznaku dolaznog aerodroma.
   * 
   * @return ICAO oznaka dolaznog aerodroma.
   */
  public String getEstArrivalAirport() {
    return this.estArrivalAirport;
  }

  /**
   * 
   * Setter za ICAO oznaku dolaznog aerodroma.
   * 
   * @param estArrivalAirport ICAO oznaka dolaznog aerodroma.
   */
  public void setEstArrivalAirport(String estArrivalAirport) {
    this.estArrivalAirport = estArrivalAirport;
  }

  /**
   * 
   * Getter za horizontalnu udaljenost do dolaznog aerodroma.
   * 
   * @return Horizontalna udaljenost do dolaznog aerodroma.
   */
  public int getEstArrivalAirportHorizDistance() {
    return this.estArrivalAirportHorizDistance;
  }

  /**
   * 
   * Setter za horizontalnu udaljenost do dolaznog aerodroma.
   * 
   * @param estArrivalAirportHorizDistance Horizontalna udaljenost do dolaznog aerodroma.
   */
  public void setEstArrivalAirportHorizDistance(int estArrivalAirportHorizDistance) {
    this.estArrivalAirportHorizDistance = estArrivalAirportHorizDistance;
  }

  /**
   * 
   * Getter za vertikalnu udaljenost do dolaznog aerodroma.
   * 
   * @return Vertikalna udaljenost do dolaznog aerodroma.
   */
  public int getEstArrivalAirportVertDistance() {
    return this.estArrivalAirportVertDistance;
  }

  /**
   * 
   * Setter za vertikalnu udaljenost do dolaznog aerodroma.
   * 
   * @param estArrivalAirportVertDistance Vertikalna udaljenost do dolaznog aerodroma.
   */
  public void setEstArrivalAirportVertDistance(int estArrivalAirportVertDistance) {
    this.estArrivalAirportVertDistance = estArrivalAirportVertDistance;
  }

  /**
   * 
   * Getter za horizontalnu udaljenost do odlaznog aerodroma.
   * 
   * @return Horizontalna udaljenost do odlaznog aerodroma.
   */
  public int getEstDepartureAirportHorizDistance() {
    return this.estDepartureAirportHorizDistance;
  }

  /**
   * 
   * Setter za horizontalnu udaljenost do odlaznog aerodroma.
   * 
   * @param estDepartureAirportHorizDistance Horizontalna udaljenost do odlaznog aerodroma.
   */
  public void setEstDepartureAirportHorizDistance(int estDepartureAirportHorizDistance) {
    this.estDepartureAirportHorizDistance = estDepartureAirportHorizDistance;
  }

  /**
   * 
   * Getter za vertikalnu udaljenost do odlaznog aerodroma.
   * 
   * @return Vertikalna udaljenost do odlaznog aerodroma.
   */
  public int getEstDepartureAirportVertDistance() {
    return this.estDepartureAirportVertDistance;
  }

  /**
   * 
   * Setter za vertikalnu udaljenost do odlaznog aerodroma.
   * 
   * @param estDepartureAirportVertDistance Vertikalna udaljenost do odlaznog aerodroma.
   */
  public void setEstDepartureAirportVertDistance(int estDepartureAirportVertDistance) {
    this.estDepartureAirportVertDistance = estDepartureAirportVertDistance;
  }

  /**
   * 
   * Getter za vrijeme prvog očitanja leta.
   * 
   * @return Vrijeme prvog očitanja leta.
   */
  public int getFirstSeen() {
    return this.firstSeen;
  }

  /**
   * 
   * Setter za vrijeme prvog očitanja leta.
   * 
   * @param firstSeen Vrijeme prvog očitanja leta.
   */
  public void setFirstSeen(int firstSeen) {
    this.firstSeen = firstSeen;
  }

  /**
   * 
   * Getter za ICAO oznaku zrakoplova.
   * 
   * @return ICAO oznaka zrakoplova.
   */
  public String getIcao24() {
    return this.icao24;
  }

  /**
   * 
   * Setter za ICAO oznaku zrakoplova.
   * 
   * @param icao24 ICAO oznaka zrakoplova.
   */
  public void setIcao24(String icao24) {
    this.icao24 = icao24;
  }

  /**
   * 
   * Getter za vrijeme posljednjeg očitanja leta.
   * 
   * @return Vrijeme posljednjeg očitanja leta.
   */
  public int getLastSeen() {
    return this.lastSeen;
  }

  /**
   * 
   * Setter za vrijeme posljednjeg očitanja leta.
   * 
   * @param lastSeen Vrijeme posljednjeg očitanja leta.
   */
  public void setLastSeen(int lastSeen) {
    this.lastSeen = lastSeen;
  }

  /**
   * 
   * Getter za vrijeme spremanja podataka o letu.
   * 
   * @return Vrijeme spremanja podataka o letu.
   */
  public Timestamp getStored() {
    return this.stored;
  }

  /**
   * 
   * Setter za vrijeme spremanja podataka o letu.
   * 
   * @param stored Vrijeme spremanja podataka o letu.
   */
  public void setStored(Timestamp stored) {
    this.stored = stored;
  }

  /**
   * 
   * Getter za aerodrom s kojeg let polazi.
   * 
   * @return Aerodrom s kojeg let polazi.
   */
  public Airports getAirport() {
    return this.airport;
  }

  /**
   * 
   * Setter za aerodrom s kojeg let polazi.
   * 
   * @param airport Aerodrom s kojeg let polazi.
   */
  public void setAirport(Airports airport) {
    this.airport = airport;
  }

}
