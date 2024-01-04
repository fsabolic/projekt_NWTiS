package org.foi.nwtis.fsabolic.aplikacija_1;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.Konfiguracija;

/**
 * Klasa GlavniPoslužitelj zadužena za otvaranje mreže na određenim mrežnim vratima/portu i
 * pokretanje dretvi za obradu podataka.
 * 
 * @author Fran Sabolić
 *
 */
public class GlavniPosluzitelj {
  /***
   * Konfiguracija poslužitelja
   */
  protected Konfiguracija konf;

  /***
   * Određuje ispis zaprimljenih naredbi
   */
  protected boolean info = false;
  /***
   * Određuje mrežna vrata poslužitelja
   */
  protected int mreznaVrata = 8000;
  /***
   * Određuje broj dretvi koje mogu čekati na obradu u redu
   */
  protected int brojCekaca = 10;
  /***
   * Određuje gašenje, odnosno izvođenje poslužitelja
   */
  protected volatile boolean kraj = false;
  /***
   * Broj dretvi koje se mogu paralelno izvoditi
   */
  protected int brojRadnika = 10;
  /***
   * Mrežna utičnica glavnog poslužitelja
   */
  protected ServerSocket posluzitelj = null;

  /***
   * Status glavnog poslužitelja (0-pasivan, 1-aktivan)
   */
  protected int status = 0;

  /***
   * Broj zaprimljenih zahtejva za računanje udaljenosti
   */
  protected int brojZahtjeva = 0;;

  /***
   * Konstruktor klase koji pohranjuje danu konfiguraciju
   * 
   * @param konf konfiguracija glavnog poslužitelja
   * @throws Exception iznimka u slučaju da se ne odvije spremanje postavki u memoriju glavnog
   *         poslužitelja
   */
  public GlavniPosluzitelj(Konfiguracija konf) throws Exception {
    this.konf = konf;
    spremiPostavke();
  }

  /***
   * Dopušta prihvaćanje zahtjeva
   */
  public void pokreniPosluzitelja() {
    otvoriMreznaVrata();

  }

  /***
   * Pohranjuje postavke iz konfiguracije u memoriju glavnog poslužitelja
   */
  public void spremiPostavke() {
    this.mreznaVrata = Integer.parseInt(konf.dajPostavku("mreznaVrata"));
    this.brojCekaca = Integer.parseInt(konf.dajPostavku("brojCekaca"));
    this.brojRadnika = Integer.parseInt(konf.dajPostavku("brojRadnika"));
  }

  /***
   * Omogućava prihvaćanje zahtjeva, rukovanje više zahtjeva koristeći dretve i mehanizme za
   * konkurentnost. ThreadPoolExecutor izvor:https://shorturl.at/fmtHN
   */
  public void otvoriMreznaVrata() {
    Logger.getGlobal().log(Level.INFO, "Poslužitelj je spreman za prihvaćanje zahtjeva");
    BlockingQueue<Runnable> redCekanja = new ArrayBlockingQueue<>(this.brojCekaca);
    RejectedExecutionHandler rukovateljOdbijanjemDretve = new RejectedExecutionHandler() {
      @Override
      public void rejectedExecution(Runnable radnik, ThreadPoolExecutor upravitelj) {
        Logger.getGlobal().log(Level.INFO,
            "Trenutno se izvodi maksimalan broj dretvi i red pauziranih dretvi je popunjen. Trenutna se odbacuje.");
        ((MrezniRadnik) radnik).interrupt();
        return;
      }
    };

    ThreadPoolExecutor upraviteljDretvi =
        kreirajUpraviteljaDretvi(redCekanja, rukovateljOdbijanjemDretve);
    try {
      posluzitelj = new ServerSocket(this.mreznaVrata, this.brojCekaca);
      while (!this.kraj) {
        var uticnica = posluzitelj.accept();
        var radnik = new MrezniRadnik(uticnica, konf);
        radnik.postaviGlavniPosluzitelj(this);
        upraviteljDretvi.execute(radnik);
      }
    } catch (IOException e) {
      Logger.getGlobal().log(Level.INFO, "Utičnica na serveru je zatvorena");
    } finally {
      upraviteljDretvi.shutdown();
    }

  }

  /***
   * Kreira upravitelja dretvi koji imenuje i pokreće dretve te na kraju čeka na njihovo izvršavanje
   * ThreadFactory izvor:https://shorturl.at/gEFT2
   * 
   * @param redCekanja proslijeđeni blokirajući red čekanja u koji se pohranjuju dretve koje čekaju
   *        izvršavanje
   * @param rukovateljOdbijanjemDretve rukovatelj za odbijanje dretve
   * @return vraća upravitelja dretvi (ThreadPoolExecutor) koji pazi na konkurentnost
   */
  public ThreadPoolExecutor kreirajUpraviteljaDretvi(BlockingQueue<Runnable> redCekanja,
      RejectedExecutionHandler rukovateljOdbijanjemDretve) {

    class ProizvodacDretvi implements ThreadFactory {
      private static long brojDretve = 0;

      @Override
      public Thread newThread(Runnable r) {
        Thread noviRadnik = new Thread(r, "fsabolic_" + brojDretve++);
        return noviRadnik;
      }
    }

    return new ThreadPoolExecutor(this.brojRadnika, this.brojRadnika, 0L, TimeUnit.MILLISECONDS,
        redCekanja, new ProizvodacDretvi(), rukovateljOdbijanjemDretve);
  }

  /***
   * Zaustavlja prihvaćanje novih zahtjeva
   */
  public void zaustaviNoveZahtjeve() {
    this.kraj = true;
  }

  /***
   * Gasi mrežnu utičnicu poslužitelja
   */
  public void ugasiServerSocket() {
    try {
      this.posluzitelj.close();
    } catch (IOException e) {
      Logger.getGlobal().log(Level.SEVERE, "Neuspješno gašenje poslužitelja!");
    }
  }

  /***
   * Inicijalizira posližitelja, postavlja broj zaprimljenih zahtjeva na 0 i postavlja status
   * poslužitelja na 1
   */
  public synchronized void inicijalizirajPosluzitelj() {
    this.brojZahtjeva = 0;
    this.status = 1;
  }

  /***
   * Pauzira poslužitelj tako da postavlja status na 0 i vraća broj zaprimljenih zahtjeva
   * 
   * @return broj zahtjeva
   */
  public synchronized int pauzirajPosluzitelj() {
    this.status = 0;
    return this.brojZahtjeva;
  }

  /***
   * Vraća status poslužitelja
   * 
   * @return status
   */
  public synchronized int dohvatiStatus() {
    return this.status;
  }

  /***
   * Inkrementira broj zahtjeva
   */
  public synchronized void inkrementirajBrojZahtjeva() {
    this.brojZahtjeva++;
  }

  /***
   * Dozvoljava ispis zaprimljenih naredbi
   * 
   * @param info status na koji se ispis želi postaviti
   */
  public synchronized void postaviInfo(boolean info) {
    this.info = info;
  }

  /***
   * Vraća status ispisa
   * 
   * @return status ispisa
   */
  public synchronized boolean vratiInfo() {
    return this.info;
  }



}
