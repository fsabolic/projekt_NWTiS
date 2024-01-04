package org.foi.nwtis.fsabolic.aplikacija_5.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.foi.nwtis.podaci.Aerodrom;
import org.foi.nwtis.podaci.Odgovor;
import org.foi.nwtis.podaci.UdaljenostAerodromDrzava;
import com.google.gson.Gson;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/***
 * Klasa kojom se pristupa vanjskim API-jevima koji služe za dohvaćanje određenih podataka o
 * aerodromima
 * 
 * @author Fran Sabolić
 *
 */
public class RestKlijentAerodroma {
  /***
   * String za spremanje URI-ja kojima se pristupa API-ju
   */
  private String URI;

  /***
   * Konstruktor klase
   */
  public RestKlijentAerodroma() {}

  /***
   * Konstruktor klase koji pridružuje vrijednost URI
   * 
   * @param URI
   */
  public RestKlijentAerodroma(String URI) {
    this.URI = URI;
  }

  /***
   * Dohvaća sve aerodrome uz straničenje i filtriranje
   * 
   * @param odBroja red od kojeg se dohvaćaju podaci iz baze
   * @param broj količina dohvaćenih podataka
   * @param traziNaziv naziv za filtriranje
   * @param traziDrzavu država za filtriranje
   * @return aerodromi lista aerodroma
   */
  public List<Aerodrom> dohvatiSveAerodrome(int odBroja, int broj, String traziNaziv,
      String traziDrzavu) {
    RestKKlijent rk = new RestKKlijent(URI);
    Aerodrom[] json_Aerodromi = rk.dohvatiAerodrome(odBroja, broj, traziNaziv, traziDrzavu);
    List<Aerodrom> aerodromi;
    if (json_Aerodromi == null) {
      aerodromi = new ArrayList<>();
    } else {
      aerodromi = Arrays.asList(json_Aerodromi);
    }
    rk.close();
    return aerodromi;
  }

  /***
   * Dohvaća jedan aerodrom
   * 
   * @param icao icao aerodroma
   * @return aerodromi lista aerodroma
   */
  public Aerodrom dohvatiAerodrom(String icao) {
    RestKKlijent rk = new RestKKlijent(URI);
    Aerodrom aerodrom = rk.dohvatiAerodrom(icao);

    rk.close();
    return aerodrom;
  }

  /***
   * Dohvaća udaljenosti između dva aerodroma unutar država preko kojih se leti te ukupna udaljenost
   * 
   * @param icaoOd icao polaznog aerodroma
   * @param icaoDo icao dolaznog aerodroma
   * @return udaljenosti lista udaljenosti
   */
  public List<UdaljenostAerodromDrzava> dohvatiUdaljenosti1(String icaoOd, String icaoDo) {
    RestKKlijent rk = new RestKKlijent(URI);
    UdaljenostAerodromDrzava[] jsonUdaljenosti = rk.dohvatiUdaljenosti1(icaoOd, icaoDo);
    List<UdaljenostAerodromDrzava> udaljenosti;
    if (jsonUdaljenosti == null) {
      udaljenosti = new ArrayList<>();
    } else {
      udaljenosti = Arrays.asList(jsonUdaljenosti);
    }
    rk.close();
    return udaljenosti;
  }

  /***
   * Dohvaća udaljenosti između dva aerodroma
   * 
   * @param icaoOd icao polaznog aerodroma
   * @param icaoDo icao dolaznog aerodroma
   * @return udaljenost udaljenost između 2 aerodroma
   */
  public float dohvatiUdaljenosti2(String icaoOd, String icaoDo) {
    RestKKlijent rk = new RestKKlijent(URI);
    float udaljenost = rk.dohvatiUdaljenosti2(icaoOd, icaoDo);
    rk.close();
    return udaljenost;
  }

  /***
   * Pristupa API-ju za dohvaćanje svih aerodroma i udaljenosti do polaznog aerodroma unutar države
   * odredišnog aerodroma koji su manje udaljeni od udaljenosti između polaznog i odredišnog
   * aerodroma
   * 
   * @param icaoOd icao polaznog aerodroma
   * @param icaoDo icao dolaznog aerodroma
   * @return udaljenosti udaljenosti koje zadovoljavaju uvjete
   */
  public List<UdaljenostAerodromDrzava> dohvatiUdaljenosti3(String icaoOd, String icaoDo) {
    RestKKlijent rk = new RestKKlijent(URI);
    UdaljenostAerodromDrzava[] jsonUdaljenosti = rk.dohvatiUdaljenosti3(icaoOd, icaoDo);
    List<UdaljenostAerodromDrzava> udaljenosti;
    if (jsonUdaljenosti == null) {
      udaljenosti = new ArrayList<>();
    } else {
      udaljenosti = Arrays.asList(jsonUdaljenosti);
    }
    rk.close();
    return udaljenosti;
  }

  /***
   * Pristupa API-ju za dohvaćanje aerodroma i udaljenosti do polaznog aerodroma unutar zadane
   * države koje su manje od zadane udaljenosti
   * 
   * @param icaoOd icao polaznog aerodroma
   * @param drzava država unutar koje se promatraju udaljenosti
   * @param km broj kilometara ispod kojih se promatraju udaljenosti
   * @return udaljenosti popis svih udaljenosti koje zadovoljavaju uvjete
   */
  public List<UdaljenostAerodromDrzava> dohvatiUdaljenosti4(String icaoOd, String drzava,
      float km) {
    RestKKlijent rk = new RestKKlijent(URI);
    UdaljenostAerodromDrzava[] jsonUdaljenosti = rk.dohvatiUdaljenosti4(icaoOd, drzava, km);
    List<UdaljenostAerodromDrzava> udaljenosti;
    if (jsonUdaljenosti == null) {
      udaljenosti = new ArrayList<>();
    } else {
      udaljenosti = Arrays.asList(jsonUdaljenosti);
    }
    rk.close();
    return udaljenosti;
  }

  /***
   * Pristupa API-ju za dohvaćanje svih udaljenosti nekog aerodroma
   * 
   * @param icao icao polaznog aerodroma
   * @param odBroja red od kojeg se dohvaćaju podaci iz baze
   * @param broj količina dohvaćenih podataka
   * @return udaljenosti popis svih udaljenosti aerodroma
   */
  public List<UdaljenostAerodromDrzava> dohvatiUdaljenostiAerodroma(String icao, int odBroja,
      int broj) {
    RestKKlijent rk = new RestKKlijent(URI);
    UdaljenostAerodromDrzava[] jsonUdaljenosti =
        rk.dohvatiUdaljenostiAerodroma(icao, odBroja, broj);
    List<UdaljenostAerodromDrzava> udaljenosti;
    if (jsonUdaljenosti == null) {
      udaljenosti = new ArrayList<>();
    } else {
      udaljenosti = Arrays.asList(jsonUdaljenosti);
    }
    rk.close();
    return udaljenosti;
  }

  /***
   * Statička klasa za pristupanje vanjskim API-jevima kako bi se dohvatili traženi podaci
   * 
   * @author Fran Sabolić
   *
   */
  static class RestKKlijent {
    /***
     * Predstavlja "metu" na kojoj se nalaze traženi API-jevi
     */
    private final WebTarget webTarget;
    /***
     * Predstavlja klijent za proslijeđivanje zahtjeva
     */
    private final Client client;
    /***
     * Predstavlja osnovni URI na kojemu se nalaze traženi API-jevi
     */
    private static String BASE_URI;

    /***
     * Konstruktor klase
     */
    public RestKKlijent() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("aerodromi");
    }

    /***
     * Konstruktor klase
     */
    public RestKKlijent(String URI) {
      client = ClientBuilder.newClient();
      webTarget = client.target(URI).path("aerodromi");
    }


    /***
     * Pristupa API-ju za dohvaćanje svih aerodroma
     * 
     * @param odBroja red od kojeg se dohvaćaju podaci iz baze
     * @param broj količina dohvaćenih podataka
     * @param traziNaziv naziv za filtriranje
     * @param traziDrzavu država za filtriranje
     * @return aerodromi popis svih aerodroma
     */
    public Aerodrom[] dohvatiAerodrome(int odBroja, int broj, String traziNaziv,
        String traziDrzavu) {
      WebTarget resource = webTarget;
      resource = resource.queryParam("odBroja", odBroja);
      resource = resource.queryParam("broj", broj);
      resource = resource.queryParam("traziNaziv", traziNaziv);
      resource = resource.queryParam("traziDrzavu", traziDrzavu);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Aerodrom[] odgovor = gson.fromJson(request.get(String.class), Aerodrom[].class);

      return odgovor;
    }

    /***
     * Pristupa API-ju za dohvaćanje jednog aerodroma
     * 
     * @param icao icao aerodroma
     * @return aerodromi popis svih aerodroma
     */
    public Aerodrom dohvatiAerodrom(String icao) {
      WebTarget resource = webTarget;

      resource = resource.path(java.text.MessageFormat.format(icao, new Object[] {}));

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      Gson gson = new Gson();
      Aerodrom odgovor = gson.fromJson(request.get(String.class), Aerodrom.class);

      return odgovor;
    }

    /***
     * Pristupa API-ju za dohvaćanje udaljenosti između dva aerodroma unutar država preko kojih se
     * leti te ukupna udaljenost
     * 
     * @param icaoOd icao polaznog aerodroma
     * @param icaoDo icao dolaznog aerodroma
     * @return udaljenosti popis svih udaljenosti
     */
    public UdaljenostAerodromDrzava[] dohvatiUdaljenosti1(String icaoOd, String icaoDo) {
      WebTarget resource = webTarget;

      resource =
          resource.path(java.text.MessageFormat.format(icaoOd + "/" + icaoDo, new Object[] {}));

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      Gson gson = new Gson();
      UdaljenostAerodromDrzava[] udaljenosti =
          gson.fromJson(request.get(String.class), UdaljenostAerodromDrzava[].class);

      return udaljenosti;
    }


    /***
     * Pristupa API-ju za dohvaćanje daljenosti između dva aerodroma
     * 
     * @param icaoOd icao polaznog aerodroma
     * @param icaoDo icao dolaznog aerodroma
     * @return udaljenosti popis svih udaljenosti
     */
    public float dohvatiUdaljenosti2(String icaoOd, String icaoDo) {
      WebTarget resource = webTarget;

      resource = resource
          .path(java.text.MessageFormat.format(icaoOd + "/izracunaj/" + icaoDo, new Object[] {}));

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      float udaljenost;
      Gson gson = new Gson();
      try {
        udaljenost = gson.fromJson(request.get(String.class), float.class);
      } catch (NullPointerException e) {
        udaljenost = -1;
      }
      return udaljenost;
    }

    /***
     * Pristupa API-ju za dohvaćanje aerodroma i udaljenosti do polaznog aerodroma unutar države
     * odredišnog aerodroma koji su manje udaljeni od udaljenosti između polaznog i odredišnog
     * aerodroma
     * 
     * @param icaoOd icao polaznog aerodroma
     * @param icaoDo icao dolaznog aerodroma
     * @return udaljenosti popis svih udaljenosti koje zadovoljavaju uvjete
     */
    public UdaljenostAerodromDrzava[] dohvatiUdaljenosti3(String icaoOd, String icaoDo) {
      WebTarget resource = webTarget;

      resource = resource
          .path(java.text.MessageFormat.format(icaoOd + "/udaljenost1/" + icaoDo, new Object[] {}));

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      Gson gson = new Gson();
      UdaljenostAerodromDrzava[] udaljenosti =
          gson.fromJson(request.get(String.class), UdaljenostAerodromDrzava[].class);

      return udaljenosti;
    }

    /***
     * Pristupa API-ju za dohvaćanje aerodroma i udaljenosti do polaznog aerodroma unutar zadane
     * države koje su manje od zadane udaljenosti
     * 
     * @param icaoOd icao polaznog aerodroma
     * @param drzava država unutar koje se promatraju udaljenosti
     * @param km broj kilometara ispod kojih se promatraju udaljenosti
     * @return udaljenosti popis svih udaljenosti koje zadovoljavaju uvjete
     */
    public UdaljenostAerodromDrzava[] dohvatiUdaljenosti4(String icaoOd, String drzava, float km) {
      WebTarget resource = webTarget;

      resource =
          resource.path(java.text.MessageFormat.format(icaoOd + "/udaljenost2", new Object[] {}));
      resource = resource.queryParam("drzava", drzava);
      resource = resource.queryParam("km", km);

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      Response response = request.get();
      if (response.getStatus() == 400) {
        return null;
      }

      Gson gson = new Gson();
      UdaljenostAerodromDrzava[] udaljenosti =
          gson.fromJson(request.get(String.class), UdaljenostAerodromDrzava[].class);

      return udaljenosti;
    }

    /***
     * Pristupa API-ju za dohvaćanje svih udaljenosti nekog aerodroma
     * 
     * @param icao icao polaznog aerodroma
     * @param odBroja red od kojeg se dohvaćaju podaci iz baze
     * @param broj količina dohvaćenih podataka
     * @return udaljenosti popis svih udaljenosti aerodroma
     */
    public UdaljenostAerodromDrzava[] dohvatiUdaljenostiAerodroma(String icao, int odBroja,
        int broj) {
      WebTarget resource = webTarget;

      resource =
          resource.path(java.text.MessageFormat.format(icao + "/udaljenosti", new Object[] {}));
      resource = resource.queryParam("odBroja", odBroja);
      resource = resource.queryParam("broj", broj);

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      Gson gson = new Gson();
      UdaljenostAerodromDrzava[] udaljenosti =
          gson.fromJson(request.get(String.class), UdaljenostAerodromDrzava[].class);

      return udaljenosti;
    }


    /***
     * Zatvara klijenta
     */
    public void close() {
      client.close();
    }


  }



}
