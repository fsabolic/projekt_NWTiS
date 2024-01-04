package org.foi.nwtis.podaci;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Klasa koja predstavlja lokaciju na temelju geografske širine i dužine.
 * 
 * @author Dragutin Kermek
 * @author Matija Novak
 * @version 2.3.0
 */
@AllArgsConstructor()
public class Lokacija {

    /**
     * Geografska širina lokacije.
     */
    @Getter
    @Setter
    private String latitude;

    /**
     * Geografska dužina lokacije.
     */
    @Getter
    @Setter
    private String longitude;

    /**
     * Konstruktor bez parametara za klasu Lokacija.
     */
    public Lokacija() {
    }

    /**
     * Postavlja geografsku širinu i dužinu lokacije.
     * 
     * @param latitude  geografska širina
     * @param longitude geografska dužina
     */
    public void postavi(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
