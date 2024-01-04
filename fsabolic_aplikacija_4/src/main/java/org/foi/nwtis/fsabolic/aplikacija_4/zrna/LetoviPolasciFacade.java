package org.foi.nwtis.fsabolic.aplikacija_4.zrna;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.fsabolic.aplikacija_4.jpa.LetoviPolasci;
import org.foi.nwtis.rest.podaci.LetAviona;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * 
 * Stateless ejb koji se koristi za rad s letovima polascima.
 */
@Stateless
public class LetoviPolasciFacade {
  /**
   * Upravitelj entitetima
   */
  @PersistenceContext(unitName = "nwtis_projekt_pu")
  private EntityManager em;
  /**
   * Builder upita
   */
  private CriteriaBuilder cb;

  /**
   * Resurs za bazu podataka
   */
  @Resource(lookup = "java:app/jdbc/nwtis_bp")
  javax.sql.DataSource ds;

  /**
   * Inicijalizacija buildera
   */
  @PostConstruct
  private void init() {
    System.out.println("LetoviPolasciFacade- init");
    cb = em.getCriteriaBuilder();
  }

  /**
   * 
   * Metoda za stvaranje leta polaska.
   * 
   * @param let Let polaska koji se sprema.
   */
  public void create(LetoviPolasci let) {
    em.persist(let);
  }

  /**
   * 
   * Metoda za ažuriranje leta polaska.
   * 
   * @param let Let polaska koji se ažurira.
   */
  public void edit(LetoviPolasci let) {
    em.merge(let);
  }

  /**
   * 
   * Metoda za brisanje leta polaska.
   * 
   * @param let Let polaska koji se briše.
   */
  public void remove(LetoviPolasci let) {
    em.remove(em.merge(let));
  }

  /**
   * 
   * Metoda za pronalaženje posljednjeg leta polaska.
   * 
   * @return Posljednji let polaska.
   */
  public LetoviPolasci findLast() {
    CriteriaQuery<LetoviPolasci> q = cb.createQuery(LetoviPolasci.class);
    Root<LetoviPolasci> root = q.from(LetoviPolasci.class);
    q.select(root);
    q.orderBy(cb.desc(root.get("firstSeen")));

    TypedQuery<LetoviPolasci> tQ = em.createQuery(q);
    tQ.setMaxResults(1);
    LetoviPolasci lp = null;
    try {
      lp = tQ.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
    return lp;

  }

  /**
   * 
   * Metoda za pronalaženje letova u određenom intervalu sa straničenjem.
   * 
   * @return Posljednji let polaska.
   */
  public List<LetoviPolasci> findLetoviInterval(int odBroja, int broj, long epohaOd, long epohaDo,
      String icao) {
    CriteriaQuery<LetoviPolasci> cQ = cb.createQuery(LetoviPolasci.class);
    Root<LetoviPolasci> root = cQ.from(LetoviPolasci.class);

    Predicate firstSeenPredikat = cb.between(root.get("firstSeen"), epohaOd, epohaDo);
    Predicate icaoPredikat = cb.equal(root.get("airport").get("icao"), icao);

    Predicate cijeliPredikat = cb.and(firstSeenPredikat, icaoPredikat);

    cQ.select(root).where(cijeliPredikat);

    List<LetoviPolasci> rezultat =
        em.createQuery(cQ).setFirstResult(odBroja).setMaxResults(broj).getResultList();

    return rezultat;

  }

}
