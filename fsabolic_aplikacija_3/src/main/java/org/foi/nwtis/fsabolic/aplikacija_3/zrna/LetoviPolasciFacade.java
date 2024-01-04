package org.foi.nwtis.fsabolic.aplikacija_3.zrna;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.fsabolic.aplikacija_3.jpa.LetoviPolasci;
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

    TypedQuery<LetoviPolasci> tq = em.createQuery(q);
    tq.setMaxResults(1);
    LetoviPolasci lp = null;
    try {
      lp = tq.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
    return lp;

  }

}
