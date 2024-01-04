package org.foi.nwtis.fsabolic.aplikacija_2.rest;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.fsabolic.aplikacija_2.jpa.Airports;
import org.foi.nwtis.fsabolic.aplikacija_2.jpa.AirportsDistanceMatrix;
import org.foi.nwtis.fsabolic.aplikacija_2.zrna.AirportFacade;
import org.foi.nwtis.fsabolic.aplikacija_2.zrna.AirportsDistanceMatrixFacade;
import org.foi.nwtis.fsabolic.aplikacija_2.zrna.DnevnikFacade;
import org.foi.nwtis.klase.JsonOdgovor;
import org.foi.nwtis.klase.MrezniPomocnik;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Airport;
import org.foi.nwtis.podaci.Lokacija;
import org.foi.nwtis.podaci.Odgovor;
import org.foi.nwtis.podaci.UdaljenostAerodrom;
import org.foi.nwtis.podaci.UdaljenostAerodromDrzava;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.servlet.ServletContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/***
 * Klasa za API aerodroma
 * 
 * @author Fran Sabolić
 *
 */
@Path("aerodromi")
@RequestScoped
public class RestAerodromi {
  /***
   * Injektiran pristup bazi podataka
   */
  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;


  /***
   * Injektirana klasa za rad s aerodromima
   */
  @Inject
  AirportFacade airportFacade;


  /***
   * Injektirana klasa za rad s udaljenosti aerodromia
   */
  @Inject
  AirportsDistanceMatrixFacade admFacade;


  /***
   * Injektirana klasa za rad s dnevnikom
   */
  @Inject
  DnevnikFacade dnevnikFacade;

  /***
   * Injektiran kontekst aplikacije
   */
  @Context
  ServletContext kontekst;

  /***
   * API za dohvaćanje svih aerodroma iz baze podataka sa straničenje i filtriranje po državi i
   * nazivu.
   * 
   * @param odBroja parametar upita koji određuje od kojeg podatka u bazi se vraćaju podaci. Osnovna
   *        vrijednost je 1.
   * @param broj parametar upita koji određuje koliko redova tablice će se vratiti iz baze podataka.
   *        Osnovna vrijednost je 20.
   * @param traziNaziv naziv za filtriranje
   * @param traziDrzavu država za filtriranje
   * @return lista aerodroma u JSON formatu ili, ako dođe do greške, odgovor sa statusom 500 ili 400
   */
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajSveAerodrome(@QueryParam("odBroja") String odBroja,
      @QueryParam("broj") String broj, @QueryParam("traziNaziv") String traziNaziv,
      @QueryParam("traziDrzavu") String traziDrzavu) {
    int odBrojaInt, brojInt;
    try {
      odBrojaInt = parsirajCijelobrojnuVrijednost(odBroja, 1);
      brojInt = parsirajCijelobrojnuVrijednost(broj, 20);
    } catch (NumberFormatException e) {
      Logger.getGlobal().log(Level.SEVERE, "Krivi format parametara");
      return JsonOdgovor.vratiVlastitiJsonOdgovor(400, "Krivi format parametara!");
    }
    List<Airports> airports =
        airportFacade.findAll(odBrojaInt - 1, brojInt, traziNaziv, traziDrzavu);
    if (airports == null)
      return JsonOdgovor.vratiJsonOdgovor(null);
    List<Aerodrom> aerodromi = Aerodrom.vratiAerodrome(airports);
    if (aerodromi == null) {
      return JsonOdgovor.vratiVlastitiJsonOdgovor(500, "Iznimka u radu s bazom podataka");
    }
    return JsonOdgovor.vratiJsonOdgovor(aerodromi);
  }

  /***
   * API za vraćanje pojedinog aerodroma prema njegovoj ICAO oznaci
   * 
   * @param icao oznaka aerodroma
   * @return objekt klase aerodrom u JSON obliku ili, ako dođe do greške, odgovor sa statusom 500
   */
  @GET
  @Path("{icao}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajAerodrom(@PathParam("icao") String icao) {
    Airports aerodrom = airportFacade.find(icao);

    if (aerodrom == null)
      return JsonOdgovor.vratiJsonOdgovor(null);

    Aerodrom aerodromKonv = new Aerodrom(aerodrom);
    return JsonOdgovor.vratiJsonOdgovor(aerodromKonv);
  }


  /***
   * API koji vraća sve udaljenosti između 2 aerodroma unutar država preko kojih se leti
   * 
   * @param icaoFrom icao oznaka aerodroma s kojeg se kreće
   * @param icaoTo icao oznaka aerodroma na koji se stiže
   * @return lista svih udaljenosti u JSON obliku ili odgovor sa statusom pogreške
   */
  @Path("{icaoOd}/{icaoDo}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostAerodroma(@PathParam("icaoOd") String icaoFrom,
      @PathParam("icaoDo") String icaoTo) {
    List<AirportsDistanceMatrix> udaljenosti =
        admFacade.getDistancesBetweenAirports(icaoFrom, icaoTo);
    if (udaljenosti == null)
      return JsonOdgovor.vratiJsonOdgovor(null);
    List<UdaljenostAerodromDrzava> udaljenostiKonv =
        UdaljenostAerodromDrzava.vratiUdaljenostiAerodroma(udaljenosti);

    return JsonOdgovor.vratiJsonOdgovor(udaljenostiKonv);
  }


  /***
   * API za dohvaćanje svih udaljenosti između odabranog aerodroma i svih ostalih aerodroma
   * 
   * @param icao oznaka odabranog aerodroma
   * @param odBroja parametar upita koji određuje od kojeg podatka u bazi se vraćaju podaci. Osnovna
   *        vrijednost je 1.
   * @param broj parametar upita koji određuje koliko redova tablice će se vratiti iz baze podataka.
   *        Osnovna vrijednost je 20.
   * @return lista udaljenosti između odabranog aerodroma i svih ostalih aerodroma ili poruka s
   *         pogreškom
   */
  @Path("{icao}/udaljenosti")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajUdaljenostiOdAerodroma(@PathParam("icao") String icao,
      @QueryParam("odBroja") String odBroja, @QueryParam("broj") String broj) {
    int odBrojaInt, brojInt;
    try {
      odBrojaInt = parsirajCijelobrojnuVrijednost(odBroja, 1);
      brojInt = parsirajCijelobrojnuVrijednost(broj, 20);
    } catch (NumberFormatException e) {
      Logger.getGlobal().log(Level.SEVERE, "Krivi format parametara");
      return JsonOdgovor.vratiVlastitiJsonOdgovor(400, "Krivi format parametara!");
    }
    List<AirportsDistanceMatrix> udaljenosti =
        admFacade.getDistancesForAirport(icao, odBrojaInt - 1, brojInt);
    if (udaljenosti == null)
      return JsonOdgovor.vratiVlastitiJsonOdgovor(500, "Iznimka u radu s bazom");
    List<UdaljenostAerodromDrzava> udaljenostiKonv =
        UdaljenostAerodromDrzava.vratiUdaljenostiAerodroma(udaljenosti);

    return JsonOdgovor.vratiJsonOdgovor(udaljenostiKonv);
  }


  /***
   * Vraća podatke za udaljenost između odabranih aerodroma
   * 
   * @param icaoOd oznaka odabranog aerodroma
   * @param icaoDo oznaka odredišnog aerodroma
   * 
   * @return lista udaljenosti između odabranog aerodroma i svih ostalih aerodroma ili poruka s
   *         pogreškom
   */
  @Path("{icaoOd}/izracunaj/{icaoDo}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajIzracunatuUdaljenost(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo) {

    Airports airportOd = airportFacade.find(icaoOd);
    Airports airportDo = airportFacade.find(icaoDo);
    if (airportOd == null || airportDo == null)
      return JsonOdgovor.vratiJsonOdgovor(null);

    Aerodrom aerodromOd = new Aerodrom(airportOd);
    Aerodrom aerodromDo = new Aerodrom(airportDo);

    String zahtjev = kreirajZahtjevUdaljenost(aerodromOd, aerodromDo);

    String odgovor = posaljiZahtjevAplikaciji1(zahtjev.toString());

    double dobivenaUdaljenost = Double.parseDouble(odgovor.split(" ")[1]);

    return JsonOdgovor.vratiJsonOdgovor(dobivenaUdaljenost);

  }

  /***
   * Vraća podatke za udaljenosti između odabranog aerodroma i svakog aerodroma iz države iz koje je
   * icaoDo čija je udaljenosti manja od udaljenosti između icaoOd i icaoDo
   * 
   * @param icaoOd oznaka odabranog aerodroma
   * @param icaoDo oznaka odabranog aerodroma
   * @param odBroja parametar upita koji određuje od kojeg podatka u bazi se vraćaju podaci. Osnovna
   *        vrijednost je 1.
   * @param broj parametar upita koji određuje koliko redova tablice će se vratiti iz baze podataka.
   *        Osnovna vrijednost je 20.
   * @return lista udaljenosti između odabranog aerodroma i svih ostalih aerodroma ili poruka s
   *         pogreškom
   */
  @Path("{icaoOd}/udaljenost1/{icaoDo}")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajAerodromePremaUdaljenosti(@PathParam("icaoOd") String icaoOd,
      @PathParam("icaoDo") String icaoDo, @QueryParam("odBroja") String odBroja,
      @QueryParam("broj") String broj) {

    int odBrojaInt;
    int brojInt;
    try {
      odBrojaInt = parsirajCijelobrojnuVrijednost(odBroja, 1);
      brojInt = parsirajCijelobrojnuVrijednost(broj, 20);

    } catch (Exception e) {
      return JsonOdgovor.vratiVlastitiJsonOdgovor(400, "Krivi format parametara");
    }

    Airports airportOd = airportFacade.find(icaoOd);
    Airports airportDo = airportFacade.find(icaoDo);
    if (airportOd == null || airportDo == null)
      return JsonOdgovor.vratiJsonOdgovor(null);


    Aerodrom aerodromOd = new Aerodrom(airportOd);
    Aerodrom aerodromDo = new Aerodrom(airportDo);

    String zahtjev = kreirajZahtjevUdaljenost(aerodromOd, aerodromDo);

    String odgovor = posaljiZahtjevAplikaciji1(zahtjev.toString());

    float dobivenaUdaljenost = Float.parseFloat(odgovor.split(" ")[1]);

    List<Aerodrom> listaAerodroma = Aerodrom.vratiAerodrome(
        airportFacade.getAirportsInCountry(aerodromDo.getDrzava(), odBrojaInt - 1, brojInt));

    List<UdaljenostAerodromDrzava> listaUdaljenostiBlizihAerodroma = new ArrayList<>();
    for (Aerodrom a : listaAerodroma) {
      zahtjev = kreirajZahtjevUdaljenost(aerodromOd, a);
      odgovor = posaljiZahtjevAplikaciji1(zahtjev.toString());
      float novaDobivenaUdaljenost = Float.parseFloat(odgovor.split(" ")[1]);
      if (dobivenaUdaljenost > novaDobivenaUdaljenost)
        listaUdaljenostiBlizihAerodroma
            .add(new UdaljenostAerodromDrzava(a.getIcao(), a.getDrzava(), novaDobivenaUdaljenost));

    }

    return JsonOdgovor.vratiJsonOdgovor(listaUdaljenostiBlizihAerodroma);

  }

  /***
   * Vraća podatke za udaljenosti između odabranog aerodroma i svakog aerodroma iz određene države
   * čija je udaljenosti manja od zadane
   * 
   * @param icaoOd oznaka odabranog aerodroma
   * @param drzava odabrana država
   * @param km odabrana maksimalna udaljenost
   * @param odBroja parametar upita koji određuje od kojeg podatka u bazi se vraćaju podaci. Osnovna
   *        vrijednost je 1.
   * @param broj parametar upita koji određuje koliko redova tablice će se vratiti iz baze podataka.
   *        Osnovna vrijednost je 20.
   * @return lista udaljenosti između odabranog aerodroma i svih ostalih aerodroma ili poruka s
   *         pogreškom
   */
  @Path("{icaoOd}/udaljenost2")
  @GET
  @Produces(MediaType.APPLICATION_JSON)
  public Response dajAerodromePremaUdaljenostiIcao(@PathParam("icaoOd") String icaoOd,
      @QueryParam("drzava") String drzava, @QueryParam("km") String km,
      @QueryParam("odBroja") String odBroja, @QueryParam("broj") String broj) {
    if (drzava == null || km == null)
      return JsonOdgovor.vratiJsonOdgovor(null);

    int odBrojaInt;
    int brojInt;
    float udaljenost;
    try {
      odBrojaInt = parsirajCijelobrojnuVrijednost(odBroja, 1);
      brojInt = parsirajCijelobrojnuVrijednost(broj, 20);
      udaljenost = parsirajDecimalnuVrijednost(km);
    } catch (Exception e) {
      return JsonOdgovor.vratiVlastitiJsonOdgovor(400, "Krivi format parametara");
    }

    Airports airportOd = airportFacade.find(icaoOd);
    if (airportOd == null)
      return JsonOdgovor.vratiVlastitiJsonOdgovor(400, "Aerodrom za dani Icao nije pronađen!");

    Aerodrom aerodromOd = new Aerodrom(airportOd);
    List<Airports> airports = airportFacade.getAirportsInCountry(drzava, odBrojaInt - 1, brojInt);
    if (airports == null)
      return JsonOdgovor.vratiJsonOdgovor(null);
    List<Aerodrom> aerodromi = Aerodrom.vratiAerodrome(airports);
    List<UdaljenostAerodromDrzava> udaljenosti = new ArrayList<>();

    for (Aerodrom a : aerodromi) {
      String zahtjev = kreirajZahtjevUdaljenost(aerodromOd, a);
      String odgovor = posaljiZahtjevAplikaciji1(zahtjev.toString());
      float izracunataUdaljenost = Float.parseFloat(odgovor.split(" ")[1]);
      if (udaljenost > izracunataUdaljenost) {
        UdaljenostAerodromDrzava uad =
            new UdaljenostAerodromDrzava(a.getIcao(), a.getDrzava(), izracunataUdaljenost);
        udaljenosti.add(uad);
      }
    }

    return JsonOdgovor.vratiJsonOdgovor(udaljenosti);
  }

  /***
   * Klasa za provjeru ispravnosti cijelobrojne vrijednosti za potrebe straničenja
   * 
   * @param vrijednost dobiveni parametar
   * @param zadanaVrijednost vrijednost koju varijbala poprima ukoliko nije dana vrijednost
   * @return dobro parsiran broj
   * @throws NumberFormatException ukoliko nije dobar format danog broja
   */
  private int parsirajCijelobrojnuVrijednost(String vrijednost, int zadanaVrijednost)
      throws NumberFormatException {
    int vrijednostInt = (vrijednost != null) ? Integer.parseInt(vrijednost) : zadanaVrijednost;
    if (vrijednostInt < 1)
      throw new NumberFormatException("Vrijednost nije cijelobrojna");
    return vrijednostInt;
  }

  /***
   * Klasa za provjeru ispravnosti decimalne vrijednosti za potrebe straničenja
   * 
   * @param vrijednost dobiveni parametar
   * @return dobro parsiran broj
   * @throws NumberFormatException ukoliko nije dobar format danog broja
   */
  private float parsirajDecimalnuVrijednost(String vrijednost)
      throws NumberFormatException, NullPointerException {
    if (vrijednost == null)
      throw new NullPointerException("Nije zadana udaljenost!");
    float vrijednostFloat = Float.parseFloat(vrijednost);
    if (vrijednostFloat < 1)
      throw new NumberFormatException("Vrijednost nije točna");
    return vrijednostFloat;
  }

  /***
   * Metoda za slanje zahtjeva na aplikaciju 1
   * 
   * @param zahtjev komanda koja se šalje na aplikaciju
   * @return vraća odgovor od aplikacije 1
   */
  private String posaljiZahtjevAplikaciji1(String zahtjev) {
    KonfiguracijaApstraktna konfig = (KonfiguracijaApstraktna) kontekst.getAttribute("konfig");
    String adresaApp1 = konfig.dajPostavku("adresaAplikacije1");
    int portApp1 = Integer.parseInt(konfig.dajPostavku("mreznaVrataAplikacije1"));
    return MrezniPomocnik.slanjeZahtjevaNa(adresaApp1, portApp1, 0, zahtjev, 0);
  }

  /***
   * Metoda za formatiranje dvaju objekta Aerodrom u zahtjev koji se šalje na aplikaciju 1
   * 
   * @param aerodromOd polazišni aerodrom
   * @param aerodromDo odredišni aerodrom
   * @return zahtjev
   */
  private String kreirajZahtjevUdaljenost(Aerodrom aerodromOd, Aerodrom aerodromDo) {
    StringBuilder zahtjev = new StringBuilder();
    zahtjev.append("UDALJENOST ");

    String gpsSirina1 = aerodromOd.getLokacija().getLatitude();
    String gosDuzina1 = aerodromOd.getLokacija().getLongitude();
    String gpsSirina2 = aerodromDo.getLokacija().getLatitude();
    String gosDuzina2 = aerodromDo.getLokacija().getLongitude();

    zahtjev.append(gpsSirina1).append(" ");
    zahtjev.append(gosDuzina1).append(" ");
    zahtjev.append(gpsSirina2).append(" ");
    zahtjev.append(gosDuzina2);
    return zahtjev.toString();
  }
}
