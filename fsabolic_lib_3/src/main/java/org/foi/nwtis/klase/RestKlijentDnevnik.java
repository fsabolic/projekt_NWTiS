package org.foi.nwtis.klase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.foi.nwtis.podaci.Dnevnik;
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
public class RestKlijentDnevnik {
  /***
   * String za spremanje URI-ja kojima se pristupa API-ju
   */
  private String URI;

  /***
   * Konstruktor klase
   */
  public RestKlijentDnevnik() {
    // TODO Auto-generated constructor stub
  }

  /***
   * Konstruktor klase koji pridružuje vrijednost URI
   * 
   * @param URI
   */
  public RestKlijentDnevnik(String URI) {
    this.URI = URI;
  }


  /***
   * Metoda kojom se dohvaćaju aerodromi (sa straničenjem)
   * 
   * @param odBroja broj retka od kojeg se dohvaćaju podaci
   * @param broj broj redaka koji se dohvaćaju
   * @return lista aerodroma
   */
  public List<Dnevnik> getZapisi(String vrsta, int odBroja, int broj) {
    RestKKlijent rc = new RestKKlijent(URI);
    Dnevnik[] json_zapisi = rc.getZapisi(vrsta, odBroja, broj);
    List<Dnevnik> zapisi;
    if (json_zapisi == null) {
      zapisi = new ArrayList<>();
    } else {
      zapisi = Arrays.asList(json_zapisi);
    }
    rc.close();
    return zapisi;
  }

  public Response setZapis(String zahtjev, String tip) {
    RestKKlijent rc = new RestKKlijent(URI);
    return rc.setZapis(zahtjev, tip);
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
      webTarget = client.target(BASE_URI).path("dnevnik");
    }

    /***
     * Konstruktor klase
     */
    public RestKKlijent(String URI) {
      client = ClientBuilder.newClient();
      webTarget = client.target(URI).path("dnevnik");
    }

    /***
     * Pristupa API-ju za dohvaćanje svih aerodroma i poziva ga
     * 
     * @param odBroja broj retka od kojeg se dohvaćaju podaci
     * @param broj broj redaka koji se dohvaćaju
     * @return lista aerodroma
     * @throws ClientErrorException
     */
    public Dnevnik[] getZapisi(String vrsta, int odBroja, int broj) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.queryParam("odBroja", odBroja);
      resource = resource.queryParam("vrsta", vrsta);
      resource = resource.queryParam("broj", broj);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      if (request.get(String.class).isEmpty()) {
        return null;
      }
      Gson gson = new Gson();
      Dnevnik[] zapisi = gson.fromJson(request.get(String.class), Dnevnik[].class);

      return zapisi;
    }

    public Response setZapis(String zahtjev, String tip) {

      WebTarget resource = webTarget;
      resource = resource.queryParam("zahtjev", zahtjev);
      resource = resource.queryParam("tip", tip);

      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);

      Response response = request.post(null);

      return response;
    }

    /***
     * Zatvara klijenta
     */
    public void close() {
      client.close();
    }
  }



}
