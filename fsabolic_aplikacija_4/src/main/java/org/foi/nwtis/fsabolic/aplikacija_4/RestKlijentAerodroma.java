package org.foi.nwtis.fsabolic.aplikacija_4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.podaci.Aerodrom;
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
   * Metoda kojom se dohvaćaju podaci o odabranom aerodromu
   * 
   * @param icao oznaka aerodroma
   * @return aerodrom
   * 
   */
  public Aerodrom getAerodrom(String icao) {
    RestKKlijent rc = new RestKKlijent(URI);
    Aerodrom k = rc.getAerodrom(icao);
    rc.close();
    return k;
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
     * Pristupa API-ju za dohvaćanje pojedinog aerodroma i poziva ga
     * 
     * @param icao oznaka aerodroma
     * @return aerodrom
     * @throws ClientErrorException
     */
    public Aerodrom getAerodrom(String icao) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {icao}));
      Invocation.Builder zahtjev = resource.request(MediaType.APPLICATION_JSON);
      if (zahtjev.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Aerodrom aerodrom = gson.fromJson(zahtjev.get(String.class), Aerodrom.class);
      return aerodrom;
    }

    /***
     * Zatvara klijenta
     */
    public void close() {
      client.close();
    }
  }



}
