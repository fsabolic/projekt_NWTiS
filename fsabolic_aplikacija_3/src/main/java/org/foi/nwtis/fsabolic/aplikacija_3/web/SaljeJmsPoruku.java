package org.foi.nwtis.fsabolic.aplikacija_3.web;

import java.io.IOException;
import org.foi.nwtis.fsabolic.aplikacija_3.zrna.JmsPosiljatelj;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/**
 * Servlet koji šalje JMS poruku.
 */
@WebServlet(name = "SaljeJmsPoruku", urlPatterns = {"/SaljeJmsPoruku"})
public class SaljeJmsPoruku extends HttpServlet {
  /***
   * Serijski broj za serijalizaciju
   */
  private static final long serialVersionUID = 6677591326517241529L;
  /**
   * Referenca na objekt koji omogućuje slanje JMS poruka.
   */
  @EJB
  JmsPosiljatelj jmsPosiljatelj;

  /**
   * Metoda koja se izvršava prilikom HTTP GET zahtjeva. Preuzima poruku iz parametra zahtjeva i
   * šalje je putem JMS poruke.
   *
   * @param req HTTP zahtjev
   * @param resp HTTP odgovor
   * @throws ServletException ako se dogodi servlet iznimka
   * @throws IOException ako se dogodi I/O iznimka prilikom komunikacije s klijentom
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    var poruka = req.getParameter("poruka");
    if (poruka != null && !poruka.isEmpty()) {
      if (jmsPosiljatelj.saljiPoruku(poruka)) {
        System.out.println("Poruka je poslana!");
        return;
      }
      System.out.println("Greška kod slanja");
      return;
    }
    System.out.println("Poruka nema tekst!");
  }

}
