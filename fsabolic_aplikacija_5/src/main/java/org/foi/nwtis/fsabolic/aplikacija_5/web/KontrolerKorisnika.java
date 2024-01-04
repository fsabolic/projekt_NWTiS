package org.foi.nwtis.fsabolic.aplikacija_5.web;

import java.util.List;
import org.foi.nwtis.fsabolic.aplikacija_4.ws.WsKorisnici.endpoint.Korisnici;
import org.foi.nwtis.fsabolic.aplikacija_4.ws.WsKorisnici.endpoint.Korisnik;
import org.foi.nwtis.fsabolic.aplikacija_4.ws.WsKorisnici.endpoint.PogresnaAutentikacija_Exception;
import org.foi.nwtis.fsabolic.aplikacija_5.PomocnikKontroler;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.xml.ws.WebServiceRef;

/**
 * KontrolerKorisnika klasa predstavlja kontroler za upravljanje korisnicima.
 */
@Controller
@Path("korisnici")
@RequestScoped
public class KontrolerKorisnika {

  /**
   * Referenca na web servis za korisnike.
   */
  @WebServiceRef(wsdlLocation = "http://localhost:8080/fsabolic_aplikacija_4/korisnici?wsdl")
  private Korisnici service;

  /**
   * Model objekt koji se koristi za prikazivanje podataka na korisničkom sučelju.
   */
  @Inject
  private Models model;

  /**
   * Referenca na ServletContext objekt koji omogućuje pristup kontekstu aplikacije.
   */
  @Inject
  private ServletContext kontekst;
  /**
   * Referenca na Http zahtjev objekt koji omogućuje pristup sesiji zahtjeva.
   */
  @Inject
  private HttpServletRequest httpZahtjev;


  /**
   * Prikazuje početnu stranicu korisnika.
   */
  @GET
  @Path("")
  @View("korisnici.jsp")
  public void pocetak() {
    PomocnikKontroler.postaviOsnovniModel(this.kontekst, this.model, this.httpZahtjev);
  }

  /**
   * Prikazuje stranicu za registraciju korisnika.
   */
  @GET
  @Path("registracija")
  @View("registracija.jsp")
  public void registracijaKorisnika() {
    PomocnikKontroler.postaviOsnovniModel(this.kontekst, this.model, this.httpZahtjev);
  }

  /**
   * Dodaje novog korisnika na temelju podataka iz obrasca registracije.
   *
   * @param korisnickoIme korisničko ime novog korisnika
   * @param lozinka lozinka novog korisnika
   * @param ime ime novog korisnika
   * @param prezime prezime novog korisnika
   * @param email email adresa novog korisnika
   */
  @POST
  @Path("dodajKorisnika")
  @View("registracija.jsp")
  public void dodajKorisnika(@FormParam("korisnickoIme") String korisnickoIme,
      @FormParam("lozinka") String lozinka, @FormParam("ime") String ime,
      @FormParam("prezime") String prezime, @FormParam("email") String email) {
    PomocnikKontroler.postaviOsnovniModel(this.kontekst, this.model, this.httpZahtjev);
    if (korisnickoIme.isBlank() | lozinka.isBlank() | ime.isBlank() | prezime.isBlank()
        | email.isBlank()) {
      model.put("poruka", "Niste unijeli sve vrijednosti!");
      return;
    }
    Korisnik korisnik = new Korisnik();
    korisnik.setKorIme(korisnickoIme);
    korisnik.setLozinka(lozinka);
    korisnik.setIme(ime);
    korisnik.setPrezime(prezime);
    korisnik.setEmail(email);
    var port = service.getWsKorisniciPort();
    if (!port.dodajKorisnika(korisnik)) {
      model.put("poruka", "Korisnik već postoji!");
    } else {
      model.put("poruka", "Novi korisnik je uspješno dodan");
    }
  }

  /**
   * Prijava korisnika na temelju podataka iz obrasca prijave.
   */
  @GET
  @Path("prijava")
  @View("prijava.jsp")
  public void prijavaKorisnika() {
    PomocnikKontroler.postaviOsnovniModel(this.kontekst, this.model, this.httpZahtjev);
  }

  /**
   * Prijavi korisnika na temelju unesenih korisničkog imena i lozinke.
   *
   * @param korisnickoIme korisničko ime korisnika
   * @param lozinka lozinka korisnika
   */
  @POST
  @Path("prijaviKorisnika")
  @View("prijava.jsp")
  public void prijaviKorisnika(@FormParam("korisnickoIme") String korisnickoIme,
      @FormParam("lozinka") String lozinka) {
    PomocnikKontroler.postaviOsnovniModel(this.kontekst, this.model, this.httpZahtjev);
    if (korisnickoIme.isBlank() | lozinka.isBlank()) {
      model.put("poruka", "Niste unijeli sve vrijednosti!");
      return;
    }

    var port = service.getWsKorisniciPort();
    try {
      Korisnik korisnik = port.dajKorisnika(korisnickoIme, lozinka, korisnickoIme);
    } catch (PogresnaAutentikacija_Exception e) {
      model.put("poruka", e.getLocalizedMessage());
      return;
    }

    HttpSession session = httpZahtjev.getSession();
    session.setAttribute("korisnickoIme", korisnickoIme);
    session.setAttribute("lozinka", lozinka);
    model.put("poruka", "Uspješna prijava");
  }

  /**
   * Dohvaća sve korisnike za prikaz na stranici pregleda korisnika.
   */
  @GET
  @Path("pregledKorisnika")
  @View("pregledKorisnika.jsp")
  public void dohvatiSveKorisnike() {
    PomocnikKontroler.postaviOsnovniModel(this.kontekst, this.model, this.httpZahtjev);
    if (!PomocnikKontroler.postojiPrijavljenKorisnik(httpZahtjev))
      return;

    var port = service.getWsKorisniciPort();
    List<Korisnik> korisnici = null;
    int brojKorisnika = 0;
    try {
      korisnici = port.dajKorisnike(PomocnikKontroler.dajKorisnickoIme(httpZahtjev),
          PomocnikKontroler.dajLozinku(httpZahtjev), "", "");
      brojKorisnika = port.dajKorisnike(PomocnikKontroler.dajKorisnickoIme(httpZahtjev),
          PomocnikKontroler.dajLozinku(httpZahtjev), "", "").size();
    } catch (PogresnaAutentikacija_Exception e) {
      model.put("poruka", e.getLocalizedMessage());
      return;
    }
    model.put("brojKorisnika", brojKorisnika);
    model.put("korisnici", korisnici);

  }

  /**
   * Dohvaća sve korisnike za prikaz na stranici pregleda korisnika na temelju zadanih parametara
   * pretraživanja.
   *
   * @param traziImeKorisnika Ime korisnika za pretraživanje
   * @param traziPrezimeKorisnika Prezime korisnika za pretraživanje
   */
  @POST
  @Path("pregledKorisnika")
  @View("pregledKorisnika.jsp")
  public void dohvatiSveKorisnikePost(@FormParam("traziImeKorisnika") String traziImeKorisnika,
      @FormParam("traziPrezimeKorisnika") String traziPrezimeKorisnika) {
    PomocnikKontroler.postaviOsnovniModel(this.kontekst, this.model, this.httpZahtjev);
    if (!PomocnikKontroler.postojiPrijavljenKorisnik(httpZahtjev))
      return;

    var port = service.getWsKorisniciPort();
    List<Korisnik> korisnici = null;
    int brojKorisnika = 0;
    try {
      korisnici = port.dajKorisnike(PomocnikKontroler.dajKorisnickoIme(httpZahtjev),
          PomocnikKontroler.dajLozinku(httpZahtjev), traziImeKorisnika, traziPrezimeKorisnika);
      brojKorisnika = port.dajKorisnike(PomocnikKontroler.dajKorisnickoIme(httpZahtjev),
          PomocnikKontroler.dajLozinku(httpZahtjev), "", "").size();
    } catch (PogresnaAutentikacija_Exception e) {
      model.put("poruka", e.getLocalizedMessage());
      return;
    }

    model.put("brojKorisnika", brojKorisnika);
    model.put("korisnici", korisnici);

  }

}
