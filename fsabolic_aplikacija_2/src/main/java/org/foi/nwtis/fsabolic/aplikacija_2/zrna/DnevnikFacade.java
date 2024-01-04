package org.foi.nwtis.fsabolic.aplikacija_2.zrna;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.foi.nwtis.fsabolic.aplikacija_2.jpa.AirportsDistanceMatrix;
import org.foi.nwtis.fsabolic.aplikacija_2.jpa.Dnevnik;

/***
 * Fasada za upravljanje zapisima dnevnika
 * 
 * @author Fran Sabolić
 *
 */
@Stateless
public class DnevnikFacade {
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
    System.out.println("Dnevnik - init");
    cb = em.getCriteriaBuilder();
  }

  /**
   * Stvara novi zapis dnevnika.
   *
   * @param dnevnik objekt koji predstavlja zapis dnevnika
   */
  public void create(Dnevnik dnevnik) {
    em.persist(dnevnik);
  }

  /**
   * Ažurira zapis dnevnika.
   *
   * @param dnevnik objekt koji predstavlja zapis dnevnika
   */
  public void edit(Dnevnik dnevnik) {
    em.merge(dnevnik);
  }

  /**
   * Briše zapis dnevnika.
   *
   * @param dnevnik objekt koji predstavlja zapis dnevnika
   */
  public void remove(Dnevnik dnevnik) {
    em.remove(em.merge(dnevnik));
  }

  /**
   * Pronalazi zapis dnevnika na temelju ID-a.
   *
   * @param id ID zapisa dnevnika
   * @return objekt koji predstavlja zapis dnevnika
   */
  public Dnevnik find(Object id) {
    return em.find(Dnevnik.class, id);
  }

  /**
   * Pronalazi sve zapise dnevnika.
   *
   * @return lista objekata koji predstavljaju zapise dnevnika
   */
  public List<Dnevnik> findAll() {
    CriteriaQuery<Dnevnik> cq = cb.createQuery(Dnevnik.class);
    cq.select(cq.from(Dnevnik.class));
    TypedQuery<Dnevnik> q = em.createQuery(cq);
    return q.getResultList();
  }

  /**
   * Vraća broj zapisa dnevnika.
   *
   * @return broj zapisa dnevnika
   */
  public int count() {
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<Dnevnik> root = cq.from(Dnevnik.class);
    cq.select(cb.count(root));
    TypedQuery<Long> q = em.createQuery(cq);
    return q.getSingleResult().intValue();
  }

  /**
   * Pronalazi zapise dnevnika koji odgovaraju određenoj vrsti.
   *
   * @param vrsta vrsta zapisa dnevnika
   * @param odBroja početni indeks
   * @param broj broj zapisa za dohvatiti
   * @return lista objekata koji predstavljaju zapise dnevnika
   */
  public List<Dnevnik> getRequestsByType(String vrsta, int odBroja, int broj) {
    CriteriaQuery<Dnevnik> cq = cb.createQuery(Dnevnik.class);
    Root<Dnevnik> root = cq.from(Dnevnik.class);
    cq.select(root).where(cb.equal(root.get("tip"), vrsta));
    TypedQuery<Dnevnik> q = em.createQuery(cq);
    q.setFirstResult(odBroja);
    q.setMaxResults(broj);
    return q.getResultList();
  }
}
