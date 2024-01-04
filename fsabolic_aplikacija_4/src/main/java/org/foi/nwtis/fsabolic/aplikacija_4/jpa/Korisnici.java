package org.foi.nwtis.fsabolic.aplikacija_4.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

/**
 * Klasa Korisnici predstavlja entitet koji opisuje podatke o korisnicima.
 */
@Entity
@Table(name = "KORISNICI")
public class Korisnici implements Serializable {
  /**
   * Jedinstveni identifikator za serijalizaciju objekta.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Korisničko ime korisnika.
   */
  @Id
  @Column(name = "korisnickoime")
  private String korisnickoime;

  /**
   * Ime korisnika.
   */
  @Column(name = "ime")
  private String ime;

  /**
   * Prezime korisnika.
   */
  @Column(name = "prezime")
  private String prezime;

  /**
   * Lozinka korisnika.
   */
  @Column(name = "lozinka")
  private String lozinka;
  
  /**
   * Email korisnika.
   */
  @Column(name = "email")
  private String email;

  /**
   * Konstruktor za stvaranje objekta Korisnici.
   */
  public Korisnici() {}
  /**
   * Vraća korisničko ime.
   *
   * @return korisničko ime
   */
  public String getKorisnickoime() {
    return korisnickoime;
  }

  /**
   * Postavlja korisničko ime.
   *
   * @param korisnickoime korisničko ime
   */
  public void setKorisnickoime(String korisnickoime) {
    this.korisnickoime = korisnickoime;
  }

  /**
   * Vraća ime korisnika.
   *
   * @return ime korisnika
   */
  public String getIme() {
    return ime;
  }

  /**
   * Postavlja ime korisnika.
   *
   * @param ime ime korisnika
   */
  public void setIme(String ime) {
    this.ime = ime;
  }

  /**
   * Vraća prezime korisnika.
   *
   * @return prezime korisnika
   */
  public String getPrezime() {
    return prezime;
  }

  /**
   * Postavlja prezime korisnika.
   *
   * @param prezime prezime korisnika
   */
  public void setPrezime(String prezime) {
    this.prezime = prezime;
  }

  /**
   * Vraća lozinku korisnika.
   *
   * @return lozinka korisnika
   */
  public String getLozinka() {
    return lozinka;
  }

  /**
   * Postavlja lozinku korisnika.
   *
   * @param lozinka lozinka korisnika
   */
  public void setLozinka(String lozinka) {
    this.lozinka = lozinka;
  }
  
  /**
   * Vraća email korisnika.
   *
   * @return email korisnika
   */
  public String getEmail() {
    return email;
  }

  /**
   * Postavlja email korisnika.
   *
   * @param email email korisnika
   */
  public void setEmail(String email) {
    this.email = email;
  }
}
