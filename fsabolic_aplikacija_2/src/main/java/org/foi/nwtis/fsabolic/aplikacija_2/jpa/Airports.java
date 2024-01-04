package org.foi.nwtis.fsabolic.aplikacija_2.jpa;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Ovaj razred predstavlja zračnu luku.
 */
@Entity
@Table(name = "AIRPORTS")
@NamedQuery(name = "Airports.findAll", query = "SELECT a FROM Airports a")
public class Airports implements Serializable {
  /***
   * Serijski broj za serijalizaciju
   */
  private static final long serialVersionUID = 1L;

  /**
   * Jedinstveni ICAO kod zračne luke.
   */
  @Id
  @Column(name = "ICAO", unique = true, nullable = false, length = 10)
  private String icao;

  /**
   * Kontinent na kojem se nalazi zračna luka.
   */
  @Column(name = "CONTINENT", length = 30)
  private String continent;

  /**
   * Koordinate zračne luke.
   */
  @Column(name = "COORDINATES", nullable = false, length = 30)
  private String coordinates;

  /**
   * Nadmorska visina zračne luke u stopama.
   */
  @Column(name = "ELEVATION_FT", length = 10)
  private String elevationFt;

  /**
   * GPS kod zračne luke.
   */
  @Column(name = "GPS_CODE", nullable = false, length = 10)
  private String gpsCode;

  /**
   * IATA kod zračne luke.
   */
  @Column(name = "IATA_CODE", nullable = false, length = 10)
  private String iataCode;

  /**
   * ISO kod zemlje u kojoj se nalazi zračna luka.
   */
  @Column(name = "ISO_COUNTRY", length = 30)
  private String isoCountry;

  /**
   * ISO kod regije u kojoj se nalazi zračna luka.
   */
  @Column(name = "ISO_REGION", length = 10)
  private String isoRegion;

  /**
   * Lokalni kod zračne luke.
   */
  @Column(name = "LOCAL_CODE", nullable = false, length = 10)
  private String localCode;

  /**
   * Općina u kojoj se nalazi zračna luka.
   */
  @Column(name = "MUNICIPALITY", length = 30)
  private String municipality;

  /**
   * Naziv zračne luke.
   */
  @Column(name = "NAME", nullable = false, length = 255)
  private String name;

  /**
   * Vrsta zračne luke.
   */
  @Column(name = "TYPE", nullable = false, length = 30)
  private String type;

  /**
   * Veza prema entitetu AirportsDistanceMatrix. Lista udaljenosti od ove zračne luke do drugih
   * zračnih luka.
   */
  @OneToMany(mappedBy = "airport1")
  private List<AirportsDistanceMatrix> airportsDistanceMatrixs1;

  /**
   * Veza prema entitetu AirportsDistanceMatrix. Lista udaljenosti od drugih zračnih luka do ove
   * zračne luke.
   */
  @OneToMany(mappedBy = "airport2")
  private List<AirportsDistanceMatrix> airportsDistanceMatrixs2;

  /**
   * Veza prema entitetu LetoviPolasci. Lista letova koji polaze iz ove zračne luke.
   */
  @OneToMany(mappedBy = "airport")
  private List<LetoviPolasci> letoviPolascis;

  /**
   * Konstruktor razreda Airports.
   */
  public Airports() {}

  /**
   * Vraća ICAO kod zračne luke.
   * 
   * @return ICAO kod zračne luke
   */
  public String getIcao() {
    return this.icao;
  }

  /**
   * Postavlja ICAO kod zračne luke.
   * 
   * @param icao ICAO kod zračne luke
   */
  public void setIcao(String icao) {
    this.icao = icao;
  }

  /**
   * Vraća kontinent na kojem se nalazi zračna luka.
   * 
   * @return kontinent
   */
  public String getContinent() {
    return this.continent;
  }

  /**
   * Postavlja kontinent na kojem se nalazi zračna luka.
   * 
   * @param continent kontinent
   */
  public void setContinent(String continent) {
    this.continent = continent;
  }

  /**
   * Vraća koordinate zračne luke.
   * 
   * @return koordinate
   */
  public String getCoordinates() {
    return this.coordinates;
  }

  /**
   * Postavlja koordinate zračne luke.
   * 
   * @param coordinates koordinate
   */
  public void setCoordinates(String coordinates) {
    this.coordinates = coordinates;
  }

  /**
   * Vraća nadmorsku visinu zračne luke u stopama.
   * 
   * @return nadmorska visina u stopama
   */
  public String getElevationFt() {
    return this.elevationFt;
  }

  /**
   * Postavlja nadmorsku visinu zračne luke u stopama.
   * 
   * @param elevationFt nadmorska visina u stopama
   */
  public void setElevationFt(String elevationFt) {
    this.elevationFt = elevationFt;
  }

  /**
   * Vraća GPS kod zračne luke.
   * 
   * @return GPS kod
   */
  public String getGpsCode() {
    return this.gpsCode;
  }

  /**
   * Postavlja GPS kod zračne luke.
   * 
   * @param gpsCode GPS kod
   */
  public void setGpsCode(String gpsCode) {
    this.gpsCode = gpsCode;
  }

  /**
   * Vraća IATA kod zračne luke.
   * 
   * @return IATA kod
   */
  public String getIataCode() {
    return this.iataCode;
  }

  /**
   * Postavlja IATA kod zračne luke.
   * 
   * @param iataCode IATA kod
   */
  public void setIataCode(String iataCode) {
    this.iataCode = iataCode;
  }

  /**
   * Vraća ISO kod zemlje u kojoj se nalazi zračna luka.
   * 
   * @return ISO kod zemlje
   */
  public String getIsoCountry() {
    return this.isoCountry;
  }

  /**
   * Postavlja ISO kod zemlje u kojoj se nalazi zračna luka.
   * 
   * @param isoCountry ISO kod zemlje
   */
  public void setIsoCountry(String isoCountry) {
    this.isoCountry = isoCountry;
  }

  /**
   * Vraća ISO kod regije u kojoj se nalazi zračna luka.
   * 
   * @return ISO kod regije
   */
  public String getIsoRegion() {
    return this.isoRegion;
  }

  /**
   * Postavlja ISO kod regije u kojoj se nalazi zračna luka.
   * 
   * @param isoRegion ISO kod regije
   */
  public void setIsoRegion(String isoRegion) {
    this.isoRegion = isoRegion;
  }

  /**
   * Vraća lokalni kod zračne luke.
   * 
   * @return lokalni kod
   */
  public String getLocalCode() {
    return this.localCode;
  }

  /**
   * Postavlja lokalni kod zračne luke.
   * 
   * @param localCode lokalni kod
   */
  public void setLocalCode(String localCode) {
    this.localCode = localCode;
  }

  /**
   * Vraća općinu u kojoj se nalazi zračna luka.
   * 
   * @return općina
   */
  public String getMunicipality() {
    return this.municipality;
  }

  /**
   * Postavlja općinu u kojoj se nalazi zračna luka.
   * 
   * @param municipality općina
   */
  public void setMunicipality(String municipality) {
    this.municipality = municipality;
  }

  /**
   * Vraća naziv zračne luke.
   * 
   * @return naziv zračne luke
   */
  public String getName() {
    return this.name;
  }

  /**
   * Postavlja naziv zračne luke.
   * 
   * @param name naziv zračne luke
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Vraća vrstu zračne luke.
   * 
   * @return vrsta zračne luke
   */
  public String getType() {
    return this.type;
  }

  /**
   * Postavlja vrstu zračne luke.
   * 
   * @param type vrsta zračne luke
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Vraća listu udaljenosti od ove zračne luke do drugih zračnih luka.
   * 
   * @return lista udaljenosti
   */
  public List<AirportsDistanceMatrix> getAirportsDistanceMatrixs1() {
    return this.airportsDistanceMatrixs1;
  }

  /**
   * Postavlja listu udaljenosti od ove zračne luke do drugih zračnih luka.
   * 
   * @param airportsDistanceMatrixs1 lista udaljenosti
   */
  public void setAirportsDistanceMatrixs1(List<AirportsDistanceMatrix> airportsDistanceMatrixs1) {
    this.airportsDistanceMatrixs1 = airportsDistanceMatrixs1;
  }

  /**
   * Vraća listu udaljenosti od drugih zračnih luka do ove zračne luke.
   * 
   * @return lista udaljenosti
   */
  public List<AirportsDistanceMatrix> getAirportsDistanceMatrixs2() {
    return this.airportsDistanceMatrixs2;
  }

  /**
   * Postavlja listu udaljenosti od drugih zračnih luka do ove zračne luke.
   * 
   * @param airportsDistanceMatrixs2 lista udaljenosti
   */
  public void setAirportsDistanceMatrixs2(List<AirportsDistanceMatrix> airportsDistanceMatrixs2) {
    this.airportsDistanceMatrixs2 = airportsDistanceMatrixs2;
  }

  /**
   * Vraća listu letova koji polaze iz ove zračne luke.
   * 
   * @return lista letova
   */
  public List<LetoviPolasci> getLetoviPolascis() {
    return this.letoviPolascis;
  }

  /**
   * Postavlja listu letova koji polaze iz ove zračne luke.
   * 
   * @param letoviPolascis lista letova
   */
  public void setLetoviPolascis(List<LetoviPolasci> letoviPolascis) {
    this.letoviPolascis = letoviPolascis;
  }
}
