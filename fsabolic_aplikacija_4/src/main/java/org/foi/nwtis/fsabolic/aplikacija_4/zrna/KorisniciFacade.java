package org.foi.nwtis.fsabolic.aplikacija_4.zrna;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.foi.nwtis.fsabolic.aplikacija_4.jpa.Korisnici;
import org.foi.nwtis.podaci.Korisnik;

@Stateless
public class KorisniciFacade {
  /**
   * Entity manager za upravljanje entitetima u kontekstu jedinice persistencije "nwtis_projekt_pu".
   */
  @PersistenceContext(unitName = "nwtis_projekt_pu")
  private EntityManager em;
  /**
   * Graditelj kriterija koji se koristi za izgradnju upita.
   */
  private CriteriaBuilder cb;

  /**
   * Inicijalizira graditelja kriterija na temelju trenutnog EntityManager-a.
   */
  @PostConstruct
  private void init() {
    System.out.println("KorisniciFacade- init");
    cb = em.getCriteriaBuilder();
  }

  /**
   * Stvara novog korisnika u bazi podataka.
   *
   * @param korisnik Korisnik koji se stvara.
   */
  public void create(Korisnici korisnik) {
    em.persist(korisnik);
  }

  /**
   * Ažurira podatke o korisniku u bazi podataka.
   *
   * @param korisnik Korisnik čiji se podaci ažuriraju.
   */
  public void edit(Korisnici korisnik) {
    em.merge(korisnik);
  }

  /**
   * Briše korisnika iz baze podataka.
   *
   * @param korisnik Korisnik koji se briše.
   */
  public void remove(Korisnici korisnik) {
    em.remove(em.merge(korisnik));
  }

  /**
   * 
   * Metoda za brojanje svih aerodroma.
   * 
   * @return Broj aerodroma.
   */
  public int count() {
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    Root<Korisnici> rt = cq.from(Korisnici.class);
    cq.select(cb.count(rt));
    Query q = em.createQuery(cq);
    return ((Long) q.getSingleResult()).intValue();
  }

  /**
   * Pronalazi korisnika u bazi podataka prema korisničkom imenu.
   *
   * @param korisnickoime Korisničko ime korisnika.
   * @return Pronađeni korisnik ili null ako korisnik ne postoji.
   */
  public Korisnici findKorisniciByUsername(String korisnickoime) {
    CriteriaQuery<Korisnici> q = cb.createQuery(Korisnici.class);
    Root<Korisnici> root = q.from(Korisnici.class);
    Predicate korisnickoImePredikat = cb.equal(root.get("korisnickoime"), korisnickoime);
    q.select(root).where(korisnickoImePredikat);
    try {
      return em.createQuery(q).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  /**
   * Pronalazi korisnika u bazi podataka prema korisničkom identifikatoru.
   *
   * @param korime Korisnički identifikator korisnika.
   * @return Pronađeni korisnik ili null ako korisnik ne postoji.
   */
  public Korisnici findKorisniciById(String korime) {
    return em.find(Korisnici.class, korime);
  }

  /**
   * Pronalazi sve korisnike u bazi podataka.
   *
   * @return Lista pronađenih korisnika.
   */
  public List<Korisnici> findAllKorisnici() {
    CriteriaQuery<Korisnici> q = cb.createQuery(Korisnici.class);
    Root<Korisnici> root = q.from(Korisnici.class);
    q.select(root);
    return em.createQuery(q).getResultList();
  }

  /**
   * Pronalazi korisnike koji odgovaraju zadanim kriterijima imena i prezimena.
   *
   * @param ime Ime korisnika.
   * @param prezime Prezime korisnika.
   * @return Lista pronađenih korisnika koji odgovaraju kriterijima.
   */
  public List<Korisnici> findKorisniciLikeImePrezime(String ime, String prezime) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<Korisnici> q = cb.createQuery(Korisnici.class);
    Root<Korisnici> root = q.from(Korisnici.class);

    Predicate namePredikat = cb.isTrue(cb.literal(true));

    if (ime != null && prezime != null) {
      namePredikat = cb.and(cb.like(root.get("ime"), "%" + ime + "%"),
          cb.like(root.get("prezime"), "%" + prezime + "%"));
    } else if (ime != null) {
      namePredikat = cb.like(root.get("ime"), "%" + ime + "%");
    } else if (prezime != null) {
      namePredikat = cb.like(root.get("prezime"), "%" + prezime + "%");
    }

    q.select(root).where(namePredikat);

    return em.createQuery(q).getResultList();
  }


}
