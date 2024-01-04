package org.foi.nwtis.fsabolic.aplikacija_5.zrna;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import jakarta.ejb.Singleton;

/**
 * 
 * Singleton razred koji služi za sakupljanje JMS poruka. Ovaj razred sadrži listu poruka te nudi
 * metode za dodavanje poruka i dohvaćanje određenog raspona poruka.
 */
@Singleton
public class SakupljacJmsPoruka {

  /**
   * 
   * Lista JMS poruka.
   */
  private static List<String> poruke = new CopyOnWriteArrayList<String>();

  /**
   * 
   * Dodaje poruku u listu poruka.
   * 
   * @param poruka poruka koja se dodaje
   */
  public static void addPoruka(String poruka) {
    poruke.add(poruka);
  }

  /**
   * 
   * Dohvaća listu poruka u zadanim granicama.
   * 
   * @param odBroja početni indeks poruka
   * @param broj broj poruka za dohvatiti
   * @return lista poruka u zadanim granicama
   */
  public static List<String> getPoruke(int odBroja, int broj) {
    odBroja--;
    broj = odBroja + broj;
    if (broj > poruke.size()) {
      broj = poruke.size();
    }
    if (odBroja > broj) {
      return new ArrayList<String>();
    }
    return poruke.subList(odBroja, broj);
  }


  public static void obrisiPoruke() {
    poruke.clear();
  }
}
