package org.foi.nwtis.fsabolic.aplikacija_4.zrna;

import java.util.List;
import org.foi.nwtis.fsabolic.aplikacija_4.jpa.Airports;
import org.foi.nwtis.podaci.Airport;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * 
 * Fasada za upravljanje entitetom Airports. Omogućuje stvaranje, uređivanje, brisanje i dohvaćanje
 * aerodroma. Također pruža razne metode za dohvaćanje podataka o aerodromima.
 */
@Stateless
public class AirportFacade {
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
   * 
   * Metoda koja se izvršava nakon konstrukcije objekta. Inicijalizira CriteriaBuilder objekt.
   */
  @PostConstruct
  private void init() {
    System.out.println("AirportFacade- init");
    cb = em.getCriteriaBuilder();
  }

  /**
   * 
   * Metoda za stvaranje novog aerodroma.
   * 
   * @param airport Aerodrom koji se stvara.
   */
  public void create(Airports airport) {
    em.persist(airport);
  }

  /**
   * 
   * Metoda za uređivanje postojećeg aerodroma.
   * 
   * @param airport Aerodrom koji se uređuje.
   */
  public void edit(Airports airport) {
    em.merge(airport);
  }

  /**
   * 
   * Metoda za brisanje postojećeg aerodroma.
   * 
   * @param Airport Aerodrom koji se briše.
   */
  public void remove(Airport Airport) {
    em.remove(em.merge(Airport));
  }

  /**
   * 
   * Metoda za pronalaženje aerodroma na temelju ID-a.
   * 
   * @param id ID aerodroma.
   * @return Pronađeni aerodrom.
   */
  public Airports find(Object id) {
    return em.find(Airports.class, id);
  }

  /**
   * 
   * Metoda za dohvaćanje svih aerodroma.
   * 
   * @return Lista svih aerodroma.
   */
  public List<Airports> findAll() {
    CriteriaQuery<Airports> cq = cb.createQuery(Airports.class);
    cq.select(cq.from(Airports.class));
    return em.createQuery(cq).getResultList();
  }

  /**
   * 
   * Metoda za dohvaćanje određenog broja aerodroma.
   * 
   * @param odBroja Početni indeks aerodroma.
   * @param broj Broj aerodroma koji se dohvaća.
   * @param traziNaziv naziv za filtriranje
   * @param traziDrzavu država za filtriranje
   * @return Lista dohvaćenih aerodroma.
   */
  public List<Airports> findAll(int odBroja, int broj, String traziNaziv, String traziDrzavu) {
    CriteriaQuery<Airports> cq = cb.createQuery(Airports.class);
    Root<Airports> root = cq.from(Airports.class);
    traziNaziv = traziNaziv == null ? "" : traziNaziv;
    Predicate namePredikat = cb.like(root.get("name"), "%" + traziNaziv + "%");
    Predicate countryPredikat;

    if (traziDrzavu != null && !traziDrzavu.isEmpty()) {
      countryPredikat = cb.equal(root.get("isoCountry"), traziDrzavu);
    } else {
      countryPredikat = cb.conjunction();
    }

    cq.select(root).where(cb.and(namePredikat, countryPredikat));

    TypedQuery<Airports> q = em.createQuery(cq);
    q.setMaxResults(broj);
    q.setFirstResult(odBroja);
    return q.getResultList();
  }

  /**
   * 
   * Metoda za brojanje svih aerodroma.
   * 
   * @return Broj aerodroma.
   */
  public int count() {
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<Airports> rt = cq.from(Airports.class);
    cq.select(cb.count(rt));
    Query q = em.createQuery(cq);
    return ((Long) q.getSingleResult()).intValue();
  }

  /**
   * 
   * Metoda za dohvaćanje svih aerodroma unutar određene države.
   * @param odBroja Početni indeks aerodroma.
   * @param broj Broj aerodroma koji se dohvaća.
   * @param drzava država za filtriranje
   * @return aerodromi u državi
   */
  public List<Airports> getAirportsInCountry(String drzava, int odBroja, int broj) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Airports> cq = cb.createQuery(Airports.class);
    Root<Airports> root = cq.from(Airports.class);

    cq.select(root).where(cb.equal(root.get("isoCountry"), drzava));

    TypedQuery<Airports> q = em.createQuery(cq);
    q.setMaxResults(broj);
    q.setFirstResult(odBroja);
    return q.getResultList();

  }

}
