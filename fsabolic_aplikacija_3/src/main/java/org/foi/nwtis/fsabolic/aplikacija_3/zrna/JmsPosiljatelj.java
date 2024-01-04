package org.foi.nwtis.fsabolic.aplikacija_3.zrna;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;
import jakarta.jms.MessageProducer;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;

/**
 * 
 * Stateless ejb koji se koristi za slanje poruka putem JMS-a.
 */
@Stateless
public class JmsPosiljatelj {

  /**
   * Factory veza
   */
  @Resource(mappedName = "jms/nwtis_qf_projekt")
  private ConnectionFactory connectionFactory;
  /**
   * Resurs za red poruka
   */
  @Resource(mappedName = "jms/NWTiS_fsabolic")
  private Queue queue;

  /**
   * 
   * Metoda za slanje poruke putem JMS-a.
   * 
   * @param tekstPoruke Tekst poruke koja se šalje.
   * @return True ako je slanje uspješno, inače false.
   */
  public boolean saljiPoruku(String tekstPoruke) {
    boolean status = true;

    try {
      Connection connection = connectionFactory.createConnection();
      Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      MessageProducer messageProducer = session.createProducer(queue);
      TextMessage message = session.createTextMessage();

      String poruka = tekstPoruke;

      message.setText(poruka);
      messageProducer.send(message);
      messageProducer.close();
      connection.close();
    } catch (JMSException ex) {
      ex.printStackTrace();
      status = false;
    }
    return status;

  }

}
