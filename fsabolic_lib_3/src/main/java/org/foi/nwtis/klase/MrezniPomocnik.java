package org.foi.nwtis.klase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/***
 * Klasa za abstrakciju slanja i primanja poruka
 * 
 * @author Fran Sabolić
 *
 */
public class MrezniPomocnik {
  /***
   * Šalje zahtjev na određenu adresu i port te čita povratnu poruku
   * 
   * @param adresa adresa primatelja
   * @param mreznaVrata mrežna vrata primatelja
   * @param cekanje najveće vrijeme čekanja na povezivanje
   * @param zahtjev zahtjev
   * @param ispis definira ispis određenih operacija
   * @return povratna poruka primatelja
   */
  public static String slanjeZahtjevaNa(String adresa, int mreznaVrata, int cekanje, String zahtjev,
      int ispis) {
    String poruka = null;
    try {
      var mreznaUticnica = new Socket();
      try {

        mreznaUticnica.connect(new InetSocketAddress(adresa, mreznaVrata), cekanje);
      } catch (Exception e) {
        Logger.getGlobal().log(Level.INFO, "Ne mogu se spojiti na server! Veza je odbijena!");
        mreznaUticnica.close();
        return null;
      }
      try {
        poruka = MrezniPomocnik.razmjeniPoruke(mreznaUticnica, zahtjev, ispis);
      } catch (IOException e) {
        Logger.getGlobal().log(Level.SEVERE,
            "Došlo je do pogreške u razmjeni podataka s poslužiteljem!");
      } finally {
        mreznaUticnica.close();
      }

    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, "Došlo je do pogreške u razmjeni podataka!");

    } catch (Exception e) {
      Logger.getGlobal().log(Level.SEVERE, "Došlo je do neočekivanje greške nakon spajanja!");
    }

    return poruka;
  }

  /***
   * Razmjenjuje poruke između dva čvora
   * 
   * @param mreznaUticnica mrežna utičnica primatljea
   * @param komanda komanda za poslati
   * @param ispis definira ispis određenih operacija
   * @return povratnu poruku
   * @throws IOException ukoliko dođe do pogreške pri čitanju za utičnice
   */
  private static String razmjeniPoruke(Socket mreznaUticnica, String komanda, int ispis)
      throws IOException {
    var citac = new BufferedReader(
        new InputStreamReader(mreznaUticnica.getInputStream(), Charset.forName("UTF-8")));
    var pisac = new BufferedWriter(
        new OutputStreamWriter(mreznaUticnica.getOutputStream(), Charset.forName("UTF-8")));

    pisac.write(komanda);
    pisac.flush();
    if (ispis == 1)
      Logger.getGlobal().log(Level.INFO, "Poruka: " + komanda);
    mreznaUticnica.shutdownOutput();

    var poruka = new StringBuilder();
    while (true) {
      var red = citac.readLine();
      if (red == null)
        break;
      if (ispis == 1)
        Logger.getGlobal().log(Level.INFO, "Odgovor: " + red);
      poruka.append(red);
    }
    mreznaUticnica.shutdownInput();

    return poruka.toString();
  }
}
