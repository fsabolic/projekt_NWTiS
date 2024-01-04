package org.foi.nwtis.fsabolic.aplikacija_5.web;


import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.fsabolic.aplikacija_4.ws.WsAerodromi.endpoint.Aerodromi;
import org.foi.nwtis.fsabolic.aplikacija_4.ws.WsAerodromi.endpoint.PogresnaAutentikacija_Exception;
import org.foi.nwtis.fsabolic.aplikacija_4.ws.WsMeteo.endpoint.Meteo;
import org.foi.nwtis.fsabolic.aplikacija_4.ws.WsMeteo.endpoint.MeteoPodaci;
import org.foi.nwtis.fsabolic.aplikacija_5.PomocnikKontroler;
import org.foi.nwtis.fsabolic.aplikacija_5.rest.RestKlijentAerodroma;
import org.foi.nwtis.klase.RestKlijentDnevnik;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Dnevnik;
import org.foi.nwtis.podaci.UdaljenostAerodromDrzava;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.mvc.Controller;
import jakarta.mvc.Models;
import jakarta.mvc.View;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.xml.ws.WebServiceRef;

/**
 * KontrolerAerodroma klasa predstavlja kontroler za upravljanje aerodromima.
 */
@Controller
@Path("aerodromi")
@RequestScoped
public class KontrolerAerodroma {

  /**
   * Referenca na web servis za aerodrome.
   */
  @WebServiceRef(wsdlLocation = "http://localhost:8080/fsabolic_aplikacija_4/aerodromi?wsdl")
  private Aerodromi service;

  /**
   * Referenca na web servis za aerodrome.
   */
  @WebServiceRef(wsdlLocation = "http://localhost:8080/fsabolic_aplikacija_4/meteo?wsdl")
  private Meteo serviceMeteo;


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
   * Glavni menu za prikazivanje stranica vezane uz sve dijelove aplikacije
   */
  @GET
  @Path("pocetak")
  @View("index.jsp")
  public void pocetak() {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
  }

  /**
   * Glavni menu za prikazivanje stranica vezane uz aerodrome
   */
  @GET
  @Path("")
  @View("aerodromi.jsp")
  public void aerodromiPocetna() {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
  }

  /***
   * Metoda koja se poziva GET metodom, dohvaća sve aerodrome i postavlja ih u model kako bi se
   * prikazale u pregledSvihAerodroma.jsp
   * 
   * @param stranica trenutna stranica kojas e prikazuje
   * @param traziNaziv naziv aerodroma za filtriranje
   * @param traziDrzavu naziv države za filtriranje
   */
  @GET
  @Path("pregledSvihAerodroma")
  @View("pregledSvihAerodroma.jsp")
  public void dajAerodrome(@QueryParam("stranica") String stranica,
      @QueryParam("traziNaziv") String traziNaziv, @QueryParam("traziDrzavu") String traziDrzavu) {
    KonfiguracijaApstraktna postavke = (KonfiguracijaApstraktna) kontekst.getAttribute("konfig");
    int brojRedova = Integer.parseInt(postavke.dajPostavku("stranica.brojRedova"));
    int stranicaInt = PomocnikKontroler.odrediStranicenje(model, stranica, brojRedova);
    List<Aerodrom> aerodromi = null;
    try {
      RestKlijentAerodroma rca = postaviRestKlijentAerodroma();
      aerodromi = rca.dohvatiSveAerodrome(stranicaInt, brojRedova, traziNaziv, traziDrzavu);
      if (aerodromi.isEmpty() || aerodromi == null) {
        int prijasnjaStranica = (stranicaInt / brojRedova);
        model.put("stranica", prijasnjaStranica);
        stranicaInt = prijasnjaStranica - 1 < 1 ? 1 : ((prijasnjaStranica - 1) * brojRedova) + 1;
        aerodromi = rca.dohvatiSveAerodrome(stranicaInt, brojRedova, traziNaziv, traziDrzavu);
      }
      try {
        var port = service.getWsAerodromiPort();
        List<org.foi.nwtis.fsabolic.aplikacija_4.ws.WsAerodromi.endpoint.Aerodrom> aerodromiZaSpremanje =
            port.dajAerodromeZaLetove(PomocnikKontroler.dajKorisnickoIme(httpZahtjev),
                PomocnikKontroler.dajLozinku(httpZahtjev));
        int brojAerodromaZaSpremanje = aerodromiZaSpremanje.size();
        model.put("brojAerodroma", brojAerodromaZaSpremanje);
      } catch (PogresnaAutentikacija_Exception e) {
        model.put("autentikacija", e.getMessage());
      }
      PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
      model.put("aerodromi", aerodromi);
      model.put("traziNaziv", traziNaziv);
      model.put("traziDrzavu", traziDrzavu);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /***
   * Metoda koja se poziva POST metodom, dohvaća sve aerodrome i postavlja ih u model kako bi se
   * prikazale u pregledSvihAerodroma.jsp
   * 
   * @param stranica trenutna stranica kojas e prikazuje
   * @param traziNaziv naziv aerodroma za filtriranje
   * @param traziDrzavu naziv države za filtriranje
   * @param icao icao aerodroma koji se sprema
   */
  @POST
  @Path("pregledSvihAerodroma")
  @View("pregledSvihAerodroma.jsp")
  public void dajAerodromePost(@FormParam("stranica") String stranica,
      @FormParam("traziNaziv") String traziNaziv, @FormParam("traziDrzavu") String traziDrzavu,
      @FormParam("icao") String icao) {
    PomocnikKontroler.dajKorisnickoIme(httpZahtjev);
    KonfiguracijaApstraktna postavke = (KonfiguracijaApstraktna) kontekst.getAttribute("konfig");
    int brojRedova = Integer.parseInt(postavke.dajPostavku("stranica.brojRedova"));
    int stranicaInt = PomocnikKontroler.odrediStranicenje(model, stranica, brojRedova);
    List<Aerodrom> aerodromi = null;
    try {
      RestKlijentAerodroma rca = postaviRestKlijentAerodroma();
      aerodromi = rca.dohvatiSveAerodrome(stranicaInt, brojRedova, traziNaziv, traziDrzavu);
      if (aerodromi.isEmpty() || aerodromi == null) {
        int prijasnjaStranica = (stranicaInt / brojRedova);
        model.put("stranica", prijasnjaStranica);
        stranicaInt = prijasnjaStranica - 1 < 1 ? 1 : ((prijasnjaStranica - 1) * brojRedova) + 1;
        aerodromi = rca.dohvatiSveAerodrome(stranicaInt, brojRedova, traziNaziv, traziDrzavu);
      }
      try {
        var port = service.getWsAerodromiPort();
        if (icao != null) {
          port.dodajAerodromZaLetove(PomocnikKontroler.dajKorisnickoIme(httpZahtjev),
              PomocnikKontroler.dajLozinku(httpZahtjev), icao);
        }
        List<org.foi.nwtis.fsabolic.aplikacija_4.ws.WsAerodromi.endpoint.Aerodrom> aerodromiZaSpremanje =
            port.dajAerodromeZaLetove(PomocnikKontroler.dajKorisnickoIme(httpZahtjev),
                PomocnikKontroler.dajLozinku(httpZahtjev));
        int brojAerodromaZaSpremanje = aerodromiZaSpremanje.size();
        model.put("brojAerodroma", brojAerodromaZaSpremanje);
      } catch (PogresnaAutentikacija_Exception e) {
        model.put("autentikacija", e.getMessage());
      }
      PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
      model.put("aerodromi", aerodromi);
      model.put("traziNaziv", traziNaziv);
      model.put("traziDrzavu", traziDrzavu);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /***
   * Metoda koja se poziva GET metodom, dohvaća aerodrom i meteo podatke za njega i postavlja ih u
   * model kako bi se prikazale u pregledJednogAerodroma.jsp
   * 
   * @param icao icao aerodroma
   */
  @GET
  @Path("pregledJednogAerodroma/{icao}")
  @View("pregledJednogAerodroma.jsp")
  public void dajAerodrom(@PathParam("icao") String icao) {

    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
    RestKlijentAerodroma rka = postaviRestKlijentAerodroma();
    Aerodrom aerodrom = rka.dohvatiAerodrom(icao);
    model.put("aerodrom", aerodrom);

    var port = serviceMeteo.getWsMeteoPort();
    MeteoPodaci meteoPodaci = null;
    try {
      meteoPodaci = port.dajMeteo(icao);
    } catch (Exception e) {
      model.put("meteoPodaci", null);
    }
    model.put("meteoPodaci", meteoPodaci);


  }


  /***
   * Metoda koja se poziva GET metodom, dohvaća aerodrome za koje se preuzimaju letovi
   * 
   */
  @GET
  @Path("pregledAerodromaLetova")
  @View("pregledAerodromaLetova.jsp")
  public void dajAerodromeLetova(@QueryParam("icao") String icao,
      @QueryParam("status") String status) {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
    var port = service.getWsAerodromiPort();
    List<org.foi.nwtis.fsabolic.aplikacija_4.ws.WsAerodromi.endpoint.Aerodrom> aerodromiLetova =
        null;
    try {
      if (icao != null) {
        if (status.equals("aktiviraj"))
          port.aktivirajAerodromZaLetove(PomocnikKontroler.dajKorisnickoIme(httpZahtjev),
              PomocnikKontroler.dajLozinku(httpZahtjev), icao);
        else {
          port.pauzirajAerodromZaLetove(PomocnikKontroler.dajKorisnickoIme(httpZahtjev),
              PomocnikKontroler.dajLozinku(httpZahtjev), icao);
        }
      }
      aerodromiLetova = port.dajAerodromeZaLetove(PomocnikKontroler.dajKorisnickoIme(httpZahtjev),
          PomocnikKontroler.dajLozinku(httpZahtjev));
    } catch (PogresnaAutentikacija_Exception e) {
      model.put("autentikacija", e.getMessage());
    }
    model.put("aerodromiLetova", aerodromiLetova);
    if (aerodromiLetova != null)
      model.put("brojAerodroma", aerodromiLetova.size());

  }

  /***
   * Metoda koja se poziva POST metodom, dohvaća udaljenosti između dva aerodroma unutar država
   * preko kojih se leti te ukupna udaljenost
   * 
   */
  @POST
  @Path("pregledUdaljenosti1")
  @View("pregledUdaljenosti1.jsp")
  public void dajUdaljenosti1Post(@FormParam("icaoOd") String icaoOd,
      @FormParam("icaoDo") String icaoDo) {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
    RestKlijentAerodroma rka = postaviRestKlijentAerodroma();
    List<UdaljenostAerodromDrzava> udaljenosti = rka.dohvatiUdaljenosti1(icaoOd, icaoDo);
    model.put("udaljenosti", udaljenosti);
    float ukupnaUdaljenost = 0;
    for (UdaljenostAerodromDrzava uad : udaljenosti)
      ukupnaUdaljenost += uad.km();
    model.put("ukupnaUdaljenost", ukupnaUdaljenost);

  }

  /***
   * Metoda koja se poziva GET metodom, dohvaća udaljenosti između dva aerodroma unutar država preko
   * kojih se leti te ukupna udaljenost
   * 
   */
  @GET
  @Path("pregledUdaljenosti1")
  @View("pregledUdaljenosti1.jsp")
  public void dajUdaljenosti1() {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
  }


  /***
   * Metoda koja se poziva GET metodom, dohvaća udaljenosti između dva aerodroma
   * 
   */
  @GET
  @Path("pregledUdaljenosti2")
  @View("pregledUdaljenosti2.jsp")
  public void dajUdaljenosti2() {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
  }


  /***
   * Metoda koja se poziva POST metodom, dohvaća udaljenosti između dva aerodroma
   * 
   */
  @POST
  @Path("pregledUdaljenosti2")
  @View("pregledUdaljenosti2.jsp")
  public void dajUdaljenosti2Post(@FormParam("icaoOd") String icaoOd,
      @FormParam("icaoDo") String icaoDo) {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
    RestKlijentAerodroma rka = postaviRestKlijentAerodroma();
    float udaljenost = rka.dohvatiUdaljenosti2(icaoOd, icaoDo);
    model.put("udaljenost", udaljenost);
    model.put("icaoOd", icaoOd);
    model.put("icaoDo", icaoDo);


  }


  /***
   * Metoda koja se poziva GET metodom, dohvaća aerodrom i udaljenosti do polaznog aerodroma unutar
   * zadane države koje su manje od zadane udaljenosti.
   * 
   */
  @GET
  @Path("pregledUdaljenosti3")
  @View("pregledUdaljenosti3.jsp")
  public void dajUdaljenosti3() {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
  }


  /***
   * Metoda koja se poziva POST metodom, dohvaća aerodrom i udaljenosti do polaznog aerodroma unutar
   * zadane države koje su manje od zadane udaljenosti.
   * 
   */
  @POST
  @Path("pregledUdaljenosti3")
  @View("pregledUdaljenosti3.jsp")
  public void dajUdaljenosti3Post(@FormParam("icaoOd") String icaoOd,
      @FormParam("icaoDo") String icaoDo) {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
    RestKlijentAerodroma rka = postaviRestKlijentAerodroma();
    List<UdaljenostAerodromDrzava> udaljenosti = rka.dohvatiUdaljenosti3(icaoOd, icaoDo);
    model.put("udaljenosti", udaljenosti);
    model.put("icaoOd", icaoOd);
    model.put("icaoDo", icaoDo);


  }

  /***
   * Metoda koja se poziva GET metodom, dohvaća aerodrom i udaljenosti do polaznog aerodroma unutar
   * zadane države koje su manje od zadane udaljenosti
   * 
   * 
   * @return udaljenosti popis svih udaljenosti koje zadovoljavaju uvjete
   */
  @GET
  @Path("pregledUdaljenosti4")
  @View("pregledUdaljenosti4.jsp")
  public void dajUdaljenosti4() {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
  }


  /***
   * Metoda koja se poziva POST, dohvaća aerodrom i udaljenosti do polaznog aerodroma unutar zadane
   * države koje su manje od zadane udaljenosti
   * 
   * @param icaoOd icao polaznog aerodroma
   * @param drzava država unutar koje se promatraju udaljenosti
   * @param km broj kilometara ispod kojih se promatraju udaljenosti
   * @return udaljenosti popis svih udaljenosti koje zadovoljavaju uvjete
   */
  @POST
  @Path("pregledUdaljenosti4")
  @View("pregledUdaljenosti4.jsp")
  public void dajUdaljenosti4Post(@FormParam("icaoOd") String icaoOd,
      @FormParam("drzava") String drzava, @FormParam("km") String km) {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
    RestKlijentAerodroma rka = postaviRestKlijentAerodroma();
    float floatKm;
    try {
      floatKm = Float.parseFloat(km);
      if (floatKm < 0)
        model.put("greska", "Pogrešan format parametara");
    } catch (Exception e) {
      model.put("greska", "Pogrešan format parametara");
      return;
    }
    List<UdaljenostAerodromDrzava> udaljenosti = rka.dohvatiUdaljenosti4(icaoOd, drzava, floatKm);
    if (udaljenosti.isEmpty()) {
      model.put("greska", "Ne postoji aerodrom s tim ICAO-om ili u toj državi");
    }
    model.put("udaljenosti", udaljenosti);
    model.put("icaoOd", icaoOd);
    model.put("drzava", drzava);
    model.put("km", km);


  }

  /***
   * Metoda koja se poziva POST, dohvaća sve udaljenosti nekog aerodroma
   * 
   * @param icao icao polaznog aerodroma
   * @param odBroja red od kojeg se dohvaćaju podaci iz baze
   * @param broj količina dohvaćenih podataka
   * @return udaljenosti popis svih udaljenosti aerodroma
   */
  @GET
  @Path("udaljenostiAerodroma/{icao}")
  @View("udaljenostiAerodroma.jsp")
  public void dajUdaljenostiAerodroma(@PathParam("icao") String icao,
      @QueryParam("stranica") String stranica) {
    PomocnikKontroler.postaviOsnovniModel(kontekst, model, httpZahtjev);
    KonfiguracijaApstraktna postavke = (KonfiguracijaApstraktna) kontekst.getAttribute("konfig");
    int brojRedova = Integer.parseInt(postavke.dajPostavku("stranica.brojRedova"));
    int stranicaInt = PomocnikKontroler.odrediStranicenje(model, stranica, brojRedova);
    List<UdaljenostAerodromDrzava> udaljenosti = null;
    try {
      RestKlijentAerodroma rkd = postaviRestKlijentAerodroma();
      udaljenosti = rkd.dohvatiUdaljenostiAerodroma(icao, stranicaInt, brojRedova);
      if (udaljenosti.isEmpty() || udaljenosti == null) {
        int prijasnjaStranica = (stranicaInt / brojRedova);
        model.put("stranica", prijasnjaStranica);
        stranicaInt = prijasnjaStranica - 1 < 1 ? 1 : ((prijasnjaStranica - 1) * brojRedova) + 1;
        udaljenosti = rkd.dohvatiUdaljenostiAerodroma(icao, stranicaInt, brojRedova);
      }

      model.put("udaljenosti", udaljenosti);
      model.put("icao", icao);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Postavlja klasu za upravljanje rest servisom za aerodrome
   */

  public RestKlijentAerodroma postaviRestKlijentAerodroma() {
    Konfiguracija konfig = (Konfiguracija) kontekst.getAttribute("konfig");
    String adresaAplikacije2 = konfig.dajPostavku("adresaAplikacije2");
    RestKlijentAerodroma rka = new RestKlijentAerodroma(adresaAplikacije2);
    return rka;
  }

}
