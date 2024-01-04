package org.foi.nwtis.fsabolic.aplikacija_3.zrna;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.foi.nwtis.fsabolic.aplikacija_3.jpa.AerodromiLetovi;

/**
 * Fasada za upravljanje entitetom AerodromiLetovi.
 */
@Stateless
public class AerodromiLetoviFacade {
  /***
   * Varijabla za upravitelja entitetima
   */
  @PersistenceContext(unitName = "nwtis_projekt_pu")
  private EntityManager em;

  /**
   * Stvara novi entitet AerodromiLetovi.
   *
   * @param aerodromiLetovi entitet AerodromiLetovi koji se stvara
   */
  public void create(AerodromiLetovi aerodromiLetovi) {
    em.persist(aerodromiLetovi);
  }

  /**
   * Ažurira postojeći entitet AerodromiLetovi.
   *
   * @param aerodromiLetovi entitet AerodromiLetovi koji se ažurira
   */
  public void edit(AerodromiLetovi aerodromiLetovi) {
    em.merge(aerodromiLetovi);
  }

  /**
   * Uklanja postojeći entitet AerodromiLetovi.
   *
   * @param aerodromiLetovi entitet AerodromiLetovi koji se uklanja
   */
  public void remove(AerodromiLetovi aerodromiLetovi) {
    em.remove(em.merge(aerodromiLetovi));
  }

  /**
   * Pronalazi entitet AerodromiLetovi na temelju ID-a.
   *
   * @param id ID entiteta AerodromiLetovi koji se pronalazi
   * @return entitet AerodromiLetovi ili null ako ne postoji entitet s traženim ID-om
   */
  public AerodromiLetovi find(Object id) {
    return em.find(AerodromiLetovi.class, id);
  }

  /**
   * Vraća listu svih entiteta AerodromiLetovi.
   *
   * @return lista svih entiteta AerodromiLetovi
   */
  public List<AerodromiLetovi> findAll() {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<AerodromiLetovi> cq = cb.createQuery(AerodromiLetovi.class);
    Root<AerodromiLetovi> root = cq.from(AerodromiLetovi.class);
    cq.select(root);
    return em.createQuery(cq).getResultList();
  }

  /**
   * Vraća broj entiteta AerodromiLetovi.
   *
   * @return broj entiteta AerodromiLetovi
   */
  public int count() {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<AerodromiLetovi> root = cq.from(AerodromiLetovi.class);
    cq.select(cb.count(root));
    return em.createQuery(cq).getSingleResult().intValue();
  }

  /**
   * Vraća listu entiteta AerodromiLetovi filtriranu po aktivnosti.
   *
   * @param aktivan status aktivnosti za filtriranje
   * @return lista entiteta AerodromiLetovi koja zadovoljava uvjet aktivnosti
   */
  public List<AerodromiLetovi> getAerodromiLetoviByAktivan(boolean aktivan) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<AerodromiLetovi> cq = cb.createQuery(AerodromiLetovi.class);
    Root<AerodromiLetovi> root = cq.from(AerodromiLetovi.class);
    cq.select(root).where(cb.equal(root.get("aktivan"), aktivan));
    return em.createQuery(cq).getResultList();
  }
}
