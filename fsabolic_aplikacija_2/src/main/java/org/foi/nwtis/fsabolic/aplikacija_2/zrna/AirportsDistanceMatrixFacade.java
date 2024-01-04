package org.foi.nwtis.fsabolic.aplikacija_2.zrna;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;
import org.foi.nwtis.fsabolic.aplikacija_2.jpa.AirportsDistanceMatrix;


/**
 * 
 * Fasada za upravljanje entitetom AirportsDistanceMatrixFacade. Omogućuje stvaranje, uređivanje,
 * brisanje i dohvaćanje udaljenosti aerodroma. Također pruža razne metode za dohvaćanje podataka o
 * udaljenostima.
 */
@Stateless
public class AirportsDistanceMatrixFacade {
  /***
   * Inicijalizacija upravitelja entitetima
   */
  @PersistenceContext(unitName = "nwtis_projekt_pu")
  private EntityManager em;

  /***
   * Inicijalizacija upravitelja upitima
   */
  private CriteriaBuilder cb;

  /**
   * Metoda koja se izvršava nakon konstrukcije objekta. Inicijalizira CriteriaBuilder objekt.
   */
  @PostConstruct
  private void init() {
    System.out.println("AirportDistanceMatrixFacade- init");
    cb = em.getCriteriaBuilder();
  }

  /**
   * Stvara novi zapis udaljenosti između aerodroma.
   *
   * @param distanceMatrix objekt koji predstavlja udaljenost između aerodroma
   */
  public void create(AirportsDistanceMatrix distanceMatrix) {
    em.persist(distanceMatrix);
  }

  /**
   * Ažurira zapis udaljenosti između aerodroma.
   *
   * @param distanceMatrix objekt koji predstavlja udaljenost između aerodroma
   */
  public void edit(AirportsDistanceMatrix distanceMatrix) {
    em.merge(distanceMatrix);
  }

  /**
   * Briše zapis udaljenosti između aerodroma.
   *
   * @param distanceMatrix objekt koji predstavlja udaljenost između aerodroma
   */
  public void remove(AirportsDistanceMatrix distanceMatrix) {
    em.remove(em.merge(distanceMatrix));
  }

  /**
   * Pronalazi zapis udaljenosti između aerodroma na temelju ID-a.
   *
   * @param id ID zapisa udaljenosti između aerodroma
   * @return objekt koji predstavlja udaljenost između aerodroma
   */
  public AirportsDistanceMatrix find(Object id) {
    return em.find(AirportsDistanceMatrix.class, id);
  }

  /**
   * Pronalazi sve zapise udaljenosti između aerodroma.
   *
   * @return lista objekata koji predstavljaju udaljenosti između aerodroma
   */
  public List<AirportsDistanceMatrix> findAll() {
    CriteriaQuery<AirportsDistanceMatrix> cq = cb.createQuery(AirportsDistanceMatrix.class);
    cq.select(cq.from(AirportsDistanceMatrix.class));
    return em.createQuery(cq).getResultList();
  }

  /**
   * Pronalazi sve zapise udaljenosti između aerodroma u određenom rasponu.
   *
   * @param fromIndex početni indeks
   * @param toIndex završni indeks
   * @return lista objekata koji predstavljaju udaljenosti između aerodroma
   */
  public List<AirportsDistanceMatrix> findAll(int fromIndex, int toIndex) {
    CriteriaQuery<AirportsDistanceMatrix> cq = cb.createQuery(AirportsDistanceMatrix.class);
    Root<AirportsDistanceMatrix> root = cq.from(AirportsDistanceMatrix.class);
    cq.select(root);
    TypedQuery<AirportsDistanceMatrix> q = em.createQuery(cq);
    q.setFirstResult(fromIndex);
    q.setMaxResults(toIndex);
    return q.getResultList();
  }

  /**
   * Vraća broj zapisa udaljenosti između aerodroma.
   *
   * @return broj zapisa udaljenosti između aerodroma
   */
  public int count() {
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<AirportsDistanceMatrix> root = cq.from(AirportsDistanceMatrix.class);
    cq.select(cb.count(root));
    TypedQuery<Long> q = em.createQuery(cq);
    return q.getSingleResult().intValue();
  }

  /**
   * Pronalazi zapise udaljenosti između određenih aerodroma.
   *
   * @param icaoFrom ICAO kod polazišnog aerodroma
   * @param icaoTo ICAO kod odredišnog aerodroma
   * @return lista objekata koji predstavljaju udaljenosti između aerodroma
   */
  public List<AirportsDistanceMatrix> getDistancesBetweenAirports(String icaoFrom, String icaoTo) {
    CriteriaQuery<AirportsDistanceMatrix> cq = cb.createQuery(AirportsDistanceMatrix.class);
    Root<AirportsDistanceMatrix> root = cq.from(AirportsDistanceMatrix.class);

    cq.select(root).where(cb.and(cb.equal(root.get("id").get("icaoFrom"), icaoFrom),
        cb.equal(root.get("id").get("icaoTo"), icaoTo)));

    TypedQuery<AirportsDistanceMatrix> q = em.createQuery(cq);
    return q.getResultList();
  }

  /**
   * Pronalazi zapise udaljenosti između aerodroma za određeni aerodrom.
   *
   * @param icao ICAO kod aerodroma
   * @param odBroja početni indeks
   * @param broj broj zapisa za dohvatiti
   * @return lista objekata koji predstavljaju udaljenosti između aerodroma
   */
  public List<AirportsDistanceMatrix> getDistancesForAirport(String icao, int odBroja, int broj) {
    CriteriaQuery<AirportsDistanceMatrix> cq = cb.createQuery(AirportsDistanceMatrix.class);
    Root<AirportsDistanceMatrix> root = cq.from(AirportsDistanceMatrix.class);
    cq.select(root).where(cb.equal(root.get("id").get("icaoFrom"), icao));
    TypedQuery<AirportsDistanceMatrix> q = em.createQuery(cq);
    q.setFirstResult(odBroja);
    q.setMaxResults(broj);
    return q.getResultList();
  }
}
