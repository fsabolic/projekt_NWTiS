package org.foi.nwtis.fsabolic.konfiguracije;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;

/***
 * Klasa konfiguracija za rad s postavkama konfiguracije u bin formatu
 * 
 * @author Fran Sabolić
 *
 */
public class KonfiguracijaBin extends KonfiguracijaApstraktna {
  /***
   * konstanta
   */
  public static final String TIP = "bin";

  /***
   * Konstruktor za konfiguraciju
   * 
   * @param nazivDatoteke naziv datoteke iz koje se čita konfiguracija
   */
  public KonfiguracijaBin(String nazivDatoteke) {
    super(nazivDatoteke);
    // TODO Auto-generated constructor stub
  }

  /***
   * konstruktor
   * 
   * @param datoteka naziv datoteke
   */
  @Override
  public void spremiKonfiguraciju(String datoteka) throws NeispravnaKonfiguracija {
    var putanja = Path.of(datoteka);

    var tip = Konfiguracija.dajTipKonfiguracije(datoteka);
    if (tip == null || tip.compareTo(TIP) != 0) {
      throw new NeispravnaKonfiguracija("Datoteka '" + datoteka + "' nije tip " + TIP);
    } else if (Files.exists(putanja)
        && (Files.isDirectory(putanja) || !Files.isWritable(putanja))) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' je direktorij ili nije moguće pisati.");
    }

    try {
      FileOutputStream fileOutputStream = new FileOutputStream(datoteka);
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
      objectOutputStream.writeObject(this.postavke);
      objectOutputStream.close();
    } catch (IOException e) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' nije moguće upisivati." + e.getMessage());
    }
  }

  @Override
  public void ucitajKonfiguraciju() throws NeispravnaKonfiguracija {
    var datoteka = this.nazivDatoteke;
    var putanja = Path.of(datoteka);
    var tip = Konfiguracija.dajTipKonfiguracije(datoteka);

    if (tip == null || tip.compareTo(TIP) != 0) {
      throw new NeispravnaKonfiguracija("Datoteka '" + datoteka + "' nije tip " + TIP);
    } else if (Files.exists(putanja)
        && (Files.isDirectory(putanja) || !Files.isReadable(putanja))) {
      throw new NeispravnaKonfiguracija(
          "Datoteka '" + datoteka + "' je direktorij ili nije moguće čitat.");
    }

    try {
      FileInputStream fileInputStream = new FileInputStream(datoteka);
      ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
      this.postavke = (Properties) objectInputStream.readObject();
      objectInputStream.close();
    } catch (IOException e) {
      throw new NeispravnaKonfiguracija(
          "Datoteku '" + datoteka + "' nije moguće čitati: " + e.getMessage());
    } catch (ClassNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

}
