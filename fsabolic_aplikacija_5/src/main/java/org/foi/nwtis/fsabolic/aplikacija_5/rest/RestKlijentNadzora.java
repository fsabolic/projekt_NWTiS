package org.foi.nwtis.fsabolic.aplikacija_5.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.foi.nwtis.podaci.Odgovor;
import com.google.gson.Gson;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/***
 * Klasa kojom se pristupa vanjskim API-jevima koji služe za dohvaćanje određenih podataka o
 * poslužitelju s aplikacije 1
 * 
 * @author Fran Sabolić
 *
 */
public class RestKlijentNadzora {
  /***
   * String za spremanje URI-ja kojima se pristupa API-ju
   */
  private String URI;

  /***
   * Konstruktor klase
   */
  public RestKlijentNadzora() {}

  /***
   * Konstruktor klase koji pridružuje vrijednost URI
   * 
   * @param URI
   */
  public RestKlijentNadzora(String URI) {
    this.URI = URI;
  }


  /***
   * Dohvaća status poslužitelja
   * 
   * @return odgovor status i tekst s poslužitelja
   */
  public Odgovor getStatus() {
    RestKKlijent rc = new RestKKlijent(URI);
    Odgovor odgovor = rc.getStatus();
    rc.close();
    return odgovor;
  }

  /***
   * Gasi poslužitelj
   * 
   * @return odgovor status i tekst s poslužitelja
   */
  public Odgovor setKraj() {
    RestKKlijent rc = new RestKKlijent(URI);
    Odgovor odgovor = rc.setKraj();
    rc.close();
    return odgovor;
  }

  /***
   * Inicijalizira poslužitelj
   * 
   * @return odgovor status i tekst s poslužitelja
   */
  public Odgovor setInit() {
    RestKKlijent rc = new RestKKlijent(URI);
    Odgovor odgovor = rc.setInit();
    rc.close();
    return odgovor;
  }

  /***
   * Pauzira poslužitelj
   * 
   * @return odgovor status i tekst s poslužitelja
   */
  public Odgovor setPauza() {
    RestKKlijent rc = new RestKKlijent(URI);
    Odgovor odgovor = rc.setPauza();
    rc.close();
    return odgovor;
  }

  /***
   * Postavlja ispis na standardni izlaz poslužitelja
   * 
   * @return odgovor status i tekst s poslužitelja
   */
  public Odgovor setInfoDa() {
    RestKKlijent rc = new RestKKlijent(URI);
    Odgovor odgovor = rc.setInfoDa();
    rc.close();
    return odgovor;
  }

  /***
   * Gasi ispis na standardni izlaz poslužitelja
   * 
   * @return odgovor status i tekst s poslužitelja
   */
  public Odgovor setInfoNe() {
    RestKKlijent rc = new RestKKlijent(URI);
    Odgovor odgovor = rc.setInfoNe();
    rc.close();
    return odgovor;
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
      webTarget = client.target(BASE_URI).path("nadzor");
    }

    /***
     * Konstruktor klase
     */
    public RestKKlijent(String URI) {
      client = ClientBuilder.newClient();
      webTarget = client.target(URI).path("nadzor");
    }


    /***
     * Pristupa API-ju za dohvaćanje statusa poslužitelja
     * 
     * @return odgovor odgovor poslužitelja
     */
    public Odgovor getStatus() {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("", new Object[] {}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      Gson gson = new Gson();
      Odgovor odgovor = gson.fromJson(request.get(String.class), Odgovor.class);

      return odgovor;
    }

    /***
     * Pristupa API-ju za gašenje poslužitelja
     * 
     * @return odgovor odgovor poslužitelja
     */
    public Odgovor setKraj() {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("KRAJ", new Object[] {}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      Gson gson = new Gson();
      Odgovor odgovor = gson.fromJson(request.get(String.class), Odgovor.class);

      return odgovor;
    }

    /***
     * Pristupa API-ju za inicijalizaciju poslužitelja
     * 
     * @return odgovor odgovor poslužitelja
     */
    public Odgovor setInit() {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("INIT", new Object[] {}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      Gson gson = new Gson();
      Odgovor odgovor = gson.fromJson(request.get(String.class), Odgovor.class);

      return odgovor;
    }

    /***
     * Pristupa API-ju za pauziranje poslužitelja
     * 
     * @return odgovor odgovor poslužitelja
     */
    public Odgovor setPauza() {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("PAUZA", new Object[] {}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      Gson gson = new Gson();
      Odgovor odgovor = gson.fromJson(request.get(String.class), Odgovor.class);

      return odgovor;
    }

    /***
     * Pristupa API-ju za postavljanje ispisa poslužitelja
     * 
     * @return odgovor odgovor poslužitelja
     */
    public Odgovor setInfoDa() {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("INFO/DA", new Object[] {}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      Gson gson = new Gson();
      Odgovor odgovor = gson.fromJson(request.get(String.class), Odgovor.class);

      return odgovor;
    }

    /***
     * Pristupa API-ju za gašenje ispisa poslužitelja
     * 
     * @return odgovor odgovor poslužitelja
     */
    public Odgovor setInfoNe() {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("INFO/NE", new Object[] {}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      Gson gson = new Gson();
      Odgovor odgovor = gson.fromJson(request.get(String.class), Odgovor.class);

      return odgovor;
    }


    /***
     * Zatvara klijenta
     */
    public void close() {
      client.close();
    }


  }



}
