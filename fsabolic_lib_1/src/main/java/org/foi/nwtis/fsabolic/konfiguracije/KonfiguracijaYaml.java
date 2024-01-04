package org.foi.nwtis.fsabolic.konfiguracije;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import org.foi.nwtis.Konfiguracija;
import org.foi.nwtis.KonfiguracijaApstraktna;
import org.foi.nwtis.NeispravnaKonfiguracija;
import org.snakeyaml.engine.v2.api.Dump;
import org.snakeyaml.engine.v2.api.DumpSettings;
import org.snakeyaml.engine.v2.api.Load;
import org.snakeyaml.engine.v2.api.LoadSettings;

/**
 * Klasa konfiguracija za rad s postavkama konfiguracije u yaml formatu
 * 
 * @author Fran Sabolić
 *
 */
public class KonfiguracijaYaml extends KonfiguracijaApstraktna {
  /**
   * konstanta
   */
  public static final String TIP = "yaml";

  /**
   * Konstruktor za inicijalizaciju KonfiguracijeTXT
   * 
   * @param nazivDatoteke - Naziv datoteke
   */
  public KonfiguracijaYaml(String nazivDatoteke) {
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
      DumpSettings dumpPostavke = DumpSettings.builder().build();
      Dump dump = new Dump(dumpPostavke);
      FileWriter pisacDatoteke = new FileWriter(datoteka);
      BufferedWriter buffPisac = new BufferedWriter(pisacDatoteke);
      String yamlString = dump.dumpToString(this.postavke);
      buffPisac.write(yamlString);
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
      LoadSettings loadPostavke = LoadSettings.builder().build();
      Load load = new Load(loadPostavke);
      FileReader citacDatoteke = new FileReader(datoteka);
      BufferedReader buffCitac = new BufferedReader(citacDatoteke);
      LinkedHashMap<String, Object> map =
          (LinkedHashMap<String, Object>) load.loadFromReader(buffCitac);

      Properties properties = new Properties();
      for (Map.Entry<String, Object> atribut : map.entrySet()) {
        properties.setProperty(atribut.getKey(), String.valueOf(atribut.getValue()));
      }

      this.postavke = properties;
      buffCitac.close();
      citacDatoteke.close();
    } catch (IOException e) {
      throw new NeispravnaKonfiguracija(
          "Datoteku '" + datoteka + "' nije moguće čitati: " + e.getMessage());
    }

  }

}
