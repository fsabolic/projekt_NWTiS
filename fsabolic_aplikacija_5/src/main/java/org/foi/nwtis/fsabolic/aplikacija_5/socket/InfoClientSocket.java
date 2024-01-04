package org.foi.nwtis.fsabolic.aplikacija_5.socket;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;

/**
 * Ova klasa implementira WebSocket klijentski endpoint (@ClientEndpoint) i definira metodu za
 * obradu dolaznih poruka (@OnMessage).
 * 
 * @author Fran Sabolić
 *
 */
@ClientEndpoint
public class InfoClientSocket {
  /**
   * Metoda koja se poziva prilikom primanja nove poruke s poslužitelja.
   * 
   * @param poruka primljena poruka sa poslužitelja
   * @param sesija WebSocket sesija
   */
  @OnMessage
  public void onMessage(String poruka, Session sesija) {
    System.out.println("Poruka poslužitelja: " + poruka);
  }
}
