package org.foi.nwtis.podaci;

/**
 * Predstavlja odgovor koji se šalje s poslužitelja.
 */
public class Odgovor {
    private int status;
    private String opis;

    /**
     * Konstruktor koji inicijalizira objekt `Odgovor` s zadanim statusom i opisom.
     * 
     * @param status status odgovora
     * @param opis   opis odgovora
     */
    public Odgovor(int status, String opis) {
        this.status = status;
        this.opis = opis;
    }

    /**
     * Vraća status odgovora.
     * 
     * @return status odgovora
     */
    public int getStatus() {
        return status;
    }

    /**
     * Postavlja status odgovora.
     * 
     * @param status status odgovora
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Vraća opis odgovora.
     * 
     * @return opis odgovora
     */
    public String getOpis() {
        return opis;
    }

    /**
     * Postavlja opis odgovora.
     * 
     * @param opis opis odgovora
     */
    public void setOpis(String opis) {
        this.opis = opis;
    }

    /**
     * Kreira novi objekt `Odgovor` na temelju dobivenog odgovora.
     * 
     * @param odgovor dobiveni odgovor
     * @return novi objekt `Odgovor` s odgovarajućim statusom i tekstom
     */
    public static Odgovor kreirajNoviOdgovor(String odgovor) {
        if (odgovor == null)
            return new Odgovor(400, "Nije dobiven odgovor od poslužitelja");
        
        String[] razdvojeniOdgovor = odgovor.split(" ");
        String statusOdgovora = razdvojeniOdgovor[0];
        int status = 200;
        String tekst = odgovor;
        if (!statusOdgovora.equals("OK")) {
            status = 400;
        }
        Odgovor kreiraniOdgovor = new Odgovor(status, tekst);
        return kreiraniOdgovor;
    }
}
