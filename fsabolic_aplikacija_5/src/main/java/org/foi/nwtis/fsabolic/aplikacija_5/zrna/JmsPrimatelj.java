package org.foi.nwtis.fsabolic.aplikacija_5.zrna;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;

/**
 * 
 * Message-driven bean koji prima JMS poruke. 
 */
@MessageDriven(mappedName = "jms/NWTiS_fsabolic",
    activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode",
            propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType",
            propertyValue = "jakarta.jms.Queue")})
public class JmsPrimatelj implements MessageListener {

  /**
   * 
   * Metoda koja se poziva prilikom primanja JMS poruke.
   * 
   * @param message primljena JMS poruka
   */
  public void onMessage(Message message) {
    if (message instanceof TextMessage) {
      try {
        var msg = (TextMessage) message;
        System.out.println("Stigla poruka:" + msg.getText());
        SakupljacJmsPoruka.addPoruka(msg.getText());
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

}
