package org.foi.nwtis.fsabolic.aplikacija_4.socket;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.foi.nwtis.podaci.InfoPodatak;
import com.google.gson.Gson;
import jakarta.ejb.Stateless;
import jakarta.websocket.EncodeException;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

/**
 * Klasa InfoWebSocket predstavlja WebSocket endpoint koji omogućuje komunikaciju putem WebSocket
 * protokola.
 */
@Stateless
@ServerEndpoint("/info")
public class InfoWebSocket {
  private static List<Session> aktivneSesije = new CopyOnWriteArrayList<>();

  /**
   * Poziva se kada se uspostavi nova WebSocket sesija.
   *
   * @param sesija uspostavljena sesija
   */
  @OnOpen
  public void onOpen(Session sesija) {
    aktivneSesije.add(sesija);
  }

  /**
   * Poziva se kada se primi poruka od klijenta putem WebSocketa.
   *
   * @param poruka primljena poruka
   * @param sesija sesija na koju je poruka primljena
   */
  @OnMessage
  public void onMessage(String poruka, Session sesija) {}

  /**
   * Poziva se kada se zatvori WebSocket sesija.
   *
   * @param sesija zatvorena sesija
   */
  @OnClose
  public void onClose(Session sesija) {
    aktivneSesije.remove(sesija);
  }

  /**
   * Služi za slanje informacija o podacima putem WebSocketa svim aktivnim sesijama.
   *
   * @param info informacije o podacima koje se šalju
   */
  public static void dajInfo(InfoPodatak info) {
    for (Session s : aktivneSesije) {
      try {
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Date datum = new Date();
        info.setTrenutnoVrijemePosluzitelja(formater.format(datum));
        var gson = new Gson();
        var jsonOdgovor = gson.toJson(info);
        s.getBasicRemote().sendObject(jsonOdgovor);
      } catch (IOException e) {
        e.printStackTrace();
      } catch (EncodeException e) {
        e.printStackTrace();
      }
    }
  }

}
