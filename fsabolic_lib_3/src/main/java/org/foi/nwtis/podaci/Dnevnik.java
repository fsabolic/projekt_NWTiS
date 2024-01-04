package org.foi.nwtis.podaci;

import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Ovaj razred predstavlja entitet "Dnevnik" koji se koristi za pohranjivanje informacija o zahtjevima u dnevnik.
 */
@Entity
@Table(name = "DNEVNIK")
@NamedQuery(name = "Dnevnik.findAll", query = "SELECT d FROM Dnevnik d")
public class Dnevnik implements Serializable {
    private static final long serialVersionUID = -6323000114020325181L;

    /**
     * Jedinstveni identifikator zapisa u dnevniku.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ZAPISA")
    private Integer idZapisa;

    /**
     * Tekstualni opis zahtjeva koji je zabilježen u dnevniku.
     */
    @Column(name = "ZAHTJEV")
    private String zahtjev;

    /**
     * Tip zahtjeva koji je zabilježen u dnevniku.
     */
    @Column(name = "TIP")
    private String tip;

    /**
     * Vremenska oznaka kada je zabilježen zahtjev u dnevniku.
     */
    @Column(name = "VREMENSKA_OZNAKA")
    private Timestamp vremenskaOznaka;

    /**
     * Konstruktor razreda Dnevnik.
     */
    public Dnevnik() {
    }

    /**
     * Vraća jedinstveni identifikator zapisa u dnevniku.
     *
     * @return jedinstveni identifikator zapisa
     */
    public Integer getIdZapisa() {
        return idZapisa;
    }

    /**
     * Postavlja jedinstveni identifikator zapisa u dnevniku.
     *
     * @param idZapisa jedinstveni identifikator zapisa
     */
    public void setIdZapisa(Integer idZapisa) {
        this.idZapisa = idZapisa;
    }

    /**
     * Vraća tekstualni opis zahtjeva koji je zabilježen u dnevniku.
     *
     * @return tekstualni opis zahtjeva
     */
    public String getZahtjev() {
        return zahtjev;
    }

    /**
     * Postavlja tekstualni opis zahtjeva koji je zabilježen u dnevniku.
     *
     * @param zahtjev tekstualni opis zahtjeva
     */
    public void setZahtjev(String zahtjev) {
        this.zahtjev = zahtjev;
    }

    /**
     * Vraća tip zahtjeva koji je zabilježen u dnevniku.
     *
     * @return tip zahtjeva
     */
    public String getTip() {
        return tip;
    }

    /**
     * Postavlja tip zahtjeva koji je zabilježen u dnevniku.
     *
     * @param tip tip zahtjeva
     */
    public void setTip(String tip) {
        this.tip = tip;
    }

    /**
     * Vraća vremensku oznaku kada je zabilježen zahtjev u dnevniku.
     *
     * @return vremenska oznaka zahtjeva
     */
    public Timestamp getVremenskaOznaka() {
        return vremenskaOznaka;
    }

    /**
     * Postavlja vremensku oznaku kada je zabilježen zahtjev u dnevniku.
     *
     * @param vremenskaOznaka vremenska oznaka zahtjeva
     */
    public void setVremenskaOznaka(Timestamp vremenskaOznaka) {
        this.vremenskaOznaka = vremenskaOznaka;
    }
}
