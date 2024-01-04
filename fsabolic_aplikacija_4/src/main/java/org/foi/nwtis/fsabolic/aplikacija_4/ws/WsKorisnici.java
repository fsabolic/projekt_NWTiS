package org.foi.nwtis.fsabolic.aplikacija_4.ws;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.fsabolic.aplikacija_4.greska.PogresnaAutentikacija;
import org.foi.nwtis.fsabolic.aplikacija_4.jpa.Airports;
import org.foi.nwtis.fsabolic.aplikacija_4.jpa.Korisnici;
import org.foi.nwtis.fsabolic.aplikacija_4.socket.InfoWebSocket;
import org.foi.nwtis.fsabolic.aplikacija_4.zrna.AirportFacade;
import org.foi.nwtis.fsabolic.aplikacija_4.zrna.KorisniciFacade;
import org.foi.nwtis.podaci.InfoPodatak;
import org.foi.nwtis.podaci.Korisnik;
import org.foi.nwtis.rest.klijenti.LIQKlijent;
import org.foi.nwtis.rest.klijenti.NwtisRestIznimka;
import org.foi.nwtis.rest.klijenti.OWMKlijent;
import org.foi.nwtis.rest.podaci.MeteoPodaci;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.ServletContext;
import jakarta.websocket.Session;

/**
 * Web servis za dohvaćanje meteoroloških podataka.
 */
@WebService(serviceName = "korisnici")
public class WsKorisnici {

  /**
   * Objekt fasade za rad s korisnicima
   */
  @Inject
  KorisniciFacade korisniciFacade;

  /**
   * Postavlja referencu na objekt fasade za rad s korisnicima.
   *
   * @param korisniciFacade referenca na objekt fasade za korisnike
   */
  @WebMethod(exclude=true)
  public void setKorisniciFacade(KorisniciFacade korisniciFacade) {
    this.korisniciFacade = korisniciFacade;
  }


  /**
   * Dohvaća listu korisnika koji zadovoljavaju određene kriterije pretrage.
   *
   * @param korisnik korisničko ime za autentikaciju
   * @param lozinka lozinka za autentikaciju
   * @param traziImeKorisnika ime korisnika prema kojem se vrši pretraga
   * @param traziPrezimeKorisnika prezime korisnika prema kojem se vrši pretraga
   * @return lista korisnika koji zadovoljavaju kriterije pretrage
   * @throws PogresnaAutentikacija ako autentikacija nije uspješna
   */
  @WebMethod
  public List<Korisnik> dajKorisnike(@WebParam(name = "korisnik") String korisnik,
      @WebParam(name = "lozinka") String lozinka,
      @WebParam(name = "traziImeKorisnika") String traziImeKorisnika,
      @WebParam(name = "traziPrezimeKorisnika") String traziPrezimeKorisnika)
      throws PogresnaAutentikacija {

    dajKorisnika(korisnik, lozinka, korisnik);
    List<Korisnici> korisnici =
        korisniciFacade.findKorisniciLikeImePrezime(traziImeKorisnika, traziPrezimeKorisnika);
    List<Korisnik> korisniciKonv = new ArrayList<>();

    for (Korisnici k : korisnici) {
      korisniciKonv.add(pretvoriKorisnici(k));
    }
    return korisniciKonv;
  }


  /**
   * Dohvaća korisnika prema korisničkom imenu.
   *
   * @param korisnik korisničko ime za autentikaciju
   * @param lozinka lozinka za autentikaciju
   * @param traziKorisnika korisničko ime korisnika koje se traži
   * @return pronađeni korisnik ili null ako korisnik nije pronađen
   * @throws PogresnaAutentikacija ako autentikacija nije uspješna
   */
  @WebMethod
  public Korisnik dajKorisnika(@WebParam(name = "korisnik") String korisnik,
      @WebParam(name = "lozinka") String lozinka,
      @WebParam(name = "traziKorisnika") String traziKorisnika) throws PogresnaAutentikacija {
    Korisnici autentificiraniKorisnik = korisniciFacade.findKorisniciById(korisnik);
    if (autentificiraniKorisnik == null) {
      throw new PogresnaAutentikacija("Ne postoji korisnik s danim korisničkim imenom");
    }
    if (!autentificiraniKorisnik.getLozinka().equals(lozinka)) {
      throw new PogresnaAutentikacija("Pogrešna lozinka!");
    }

    Korisnici pronadeniKorisnik = korisniciFacade.findKorisniciByUsername(traziKorisnika);
    if (pronadeniKorisnik == null)
      return null;

    return pretvoriKorisnici(pronadeniKorisnik);
  }

  /**
   * Dodaje novog korisnika u sustav.
   *
   * @param korisnik korisnik koji se dodaje
   * @return true ako je dodavanje uspješno, false inače
   */
  @WebMethod
  public boolean dodajKorisnika(@WebParam(name = "korisnik") Korisnik korisnik) {
    Korisnici noviKorisnik = new Korisnici();
    noviKorisnik.setKorisnickoime(korisnik.getKorIme());
    noviKorisnik.setIme(korisnik.getIme());
    noviKorisnik.setPrezime(korisnik.getPrezime());
    noviKorisnik.setEmail(korisnik.getEmail());
    noviKorisnik.setLozinka(korisnik.getLozinka());
    try {
      korisniciFacade.create(noviKorisnik);
    } catch (Exception e) {
      return false;
    }
    String poruka = "" + korisniciFacade.count();
    InfoPodatak info = new InfoPodatak();
    info.setBrojKorisnika(poruka);
    InfoWebSocket.dajInfo(info);
    return true;
  }

  /**
   * Pretvara objekt klase Korisnici u objekt klase Korisnik.
   *
   * @param korisnik objekt klase Korisnici
   * @return objekt klase Korisnik
   */
  private Korisnik pretvoriKorisnici(Korisnici korisnik) {
    Korisnik pretvoreniKorisnik = new Korisnik();
    pretvoreniKorisnik.setKorIme(korisnik.getKorisnickoime());
    pretvoreniKorisnik.setIme(korisnik.getIme());
    pretvoreniKorisnik.setPrezime(korisnik.getPrezime());
    pretvoreniKorisnik.setLozinka(korisnik.getLozinka());
    pretvoreniKorisnik.setEmail(korisnik.getEmail());
    return pretvoreniKorisnik;
  }

}
