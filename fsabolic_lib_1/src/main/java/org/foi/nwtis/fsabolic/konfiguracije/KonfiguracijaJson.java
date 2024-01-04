package org.foi.nwtis.fsabolic.konfiguracije;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import com.google.gson.Gson;

/**
 * Klasa konfiguracija za rad s postavkama konfiguracije u json formatu
 * 
 * @author Fran Sabolić
 *
 */
public class KonfiguracijaJson extends KonfiguracijaApstraktna {
  /**
   * konstanta
   */
  public static final String TIP = "json";

  /**
   * Konstruktor za inicijalizaciju KonfiguracijeTXT
   * 
   * @param nazivDatoteke - Naziv datoteke
   */
  public KonfiguracijaJson(String nazivDatoteke) {
    super(nazivDatoteke);
    // TODO Auto-generated constructor stub
  }

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
      Gson gson = new Gson();
      FileWriter pisacDatoteke = new FileWriter(datoteka);
      BufferedWriter buffPisac = new BufferedWriter(pisacDatoteke);
      String jsonString = gson.toJson(this.postavke);
      buffPisac.write(jsonString);
      buffPisac.close();
      pisacDatoteke.close();
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
      Gson gson = new Gson();
      FileReader citacDatoteke = new FileReader(datoteka);
      BufferedReader buffCitac = new BufferedReader(citacDatoteke);
      this.postavke = gson.fromJson(buffCitac, Properties.class);
      buffCitac.close();
      citacDatoteke.close();
    } catch (IOException e) {
      throw new NeispravnaKonfiguracija(
          "Datoteku '" + datoteka + "' nije moguće čitati: " + e.getMessage());
    }

  }

}
