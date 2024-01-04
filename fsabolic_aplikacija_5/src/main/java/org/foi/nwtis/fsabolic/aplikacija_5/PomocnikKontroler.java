package org.foi.nwtis.fsabolic.aplikacija_5;

import org.foi.nwtis.KonfiguracijaApstraktna;
import jakarta.mvc.Models;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/***
 * Klasa za izvođenje postavljanja osnovnog modela i provjere prijavljenog korisnika
 * 
 * @author Fran Sabolić
 *
 */
public class PomocnikKontroler {
  /**
   * Metoda koja postavlja osnovni model sa podacima o autoru i aplikaciji.
   * 
   * @param kontekst kontekst servleta koji se izvršava
   * @param model model u koji se pohranjuju podaci
   */
  public static void postaviOsnovniModel(ServletContext kontekst, Models model,
      HttpServletRequest httpZahtjev) {
    KonfiguracijaApstraktna postavke = (KonfiguracijaApstraktna) kontekst.getAttribute("konfig");
    String ime = postavke.dajPostavku("autor.ime");
    String prezime = postavke.dajPostavku("autor.prezime");
    String predmet = postavke.dajPostavku("autor.predmet");
    String godina = postavke.dajPostavku("aplikacija.godina");
    String verzija = postavke.dajPostavku("aplikacija.verzija");

    HttpSession session = httpZahtjev.getSession();
    String korisnickoIme = (String) session.getAttribute("korisnickoIme");
    String lozinka = (String) session.getAttribute("lozinka");



    model.put("korisnickoIme", korisnickoIme);
    model.put("lozinka", lozinka);
    model.put("ime", ime);
    model.put("prezime", prezime);
    model.put("predmet", predmet);
    model.put("godina", godina);
    model.put("verzija", verzija);
  }

  /**
   * Provjerava je li korisnik prijavljen na temelju korisničkog imena i lozinke.
   *
   * @param httpZahtjev HTTP zahtjev koji sadrži podatke o sesiji
   * @return true ako je korisnik prijavljen, inače false
   */
  public static boolean postojiPrijavljenKorisnik(HttpServletRequest httpZahtjev) {
    HttpSession session = httpZahtjev.getSession();
    String korisnickoIme = (String) session.getAttribute("korisnickoIme");
    String lozinka = (String) session.getAttribute("lozinka");
    return korisnickoIme != null && lozinka != null;
  }

  public static String dajKorisnickoIme(HttpServletRequest httpZahtjev) {
    HttpSession session = httpZahtjev.getSession();
    String korisnickoIme = (String) session.getAttribute("korisnickoIme");
    if (korisnickoIme == null)
      return "";
    return korisnickoIme;
  }

  public static String dajLozinku(HttpServletRequest httpZahtjev) {
    HttpSession session = httpZahtjev.getSession();
    String lozinka = (String) session.getAttribute("lozinka");
    if (lozinka == null)
      return "";
    return lozinka;
  }

  /**
   * Metoda koja određuje straničenje rezultata na temelju zadane stranice.
   *
   * @param model model u koji se sprema trenutna stranica
   * @param stranica broj stranice koju je potrebno prikazati
   * @param brojRedova broj redova po stranici
   * @return početni redak za dohvaćanje rezultata
   */
  public static int odrediStranicenje(Models model, String stranica, int brojRedova) {
    int stranicaInt = 1;
    if (stranica == null) {
      model.put("stranica", 1);
      return 1;
    }
    try {
      stranicaInt = Integer.parseInt(stranica);
    } catch (NumberFormatException e) {
      model.put("stranica", 1);
      return 1;
    }
    if (stranicaInt < 1) {
      model.put("stranica", 1);
      return 1;
    }
    model.put("stranica", ++stranicaInt);
    return (stranicaInt - 1) * brojRedova + 1;
  }
}
