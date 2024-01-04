package org.foi.nwtis.fsabolic.aplikacija_1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * Klasa za rad s regularnim izrazima
 * 
 * @author Fran Sabolić
 *
 */
public class RegexValidator {
  /***
   * Regex uzorak koji će se provjeravati
   */
  private Pattern uzorak;
  /***
   * Objekt za provjeru ispravnosti uzorka i nekog izraza
   */
  private Matcher provjera;

  /***
   * Konstruktor za RegexValidator
   * 
   * @param stringUzorak regularni izraz
   */
  public RegexValidator(String stringUzorak) {
    this.uzorak = Pattern.compile(stringUzorak);
  }

  /***
   * Provjerava podudaraju li se dani regularni izraz i dani izraz
   * 
   * @param izraz izraz za provjeru
   * @return true ako se podudaraju, inače false
   */
  public boolean provjeriRegex(String izraz) {
    this.provjera = this.uzorak.matcher(izraz);
    return this.provjera.matches();
  }

  /***
   * Provjerava postoji li neki podatak pod danom grupom
   * 
   * @param grupa grupa
   * @return true ako postoji, inače false
   */
  public boolean provjeriGrupu(String grupa) {
    if (!this.provjera.matches())
      return false;

    return this.provjera.group(grupa) != null;
  }

  /***
   * Vraća vrijednost dane grupe u regularnom izrazu
   * 
   * @param grupa grupa koja se traži
   * @return vrijednost pod grupom u regularnom izrazu ili null ako ta vrijednost ne postoji
   */
  public String vratiVrijednostGrupe(String grupa) {
    if (!this.provjeriGrupu(grupa))
      return null;

    return this.provjera.group(grupa);
  }

}
