package it.eurotn.panjea.anagrafica.manager.datigeografici;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Cap;
import it.eurotn.panjea.anagrafica.domain.datigeografici.DatiGeografici;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo1;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo2;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo3;
import it.eurotn.panjea.anagrafica.domain.datigeografici.LivelloAmministrativo4;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Localita;
import it.eurotn.panjea.anagrafica.domain.datigeografici.Nazione;
import it.eurotn.panjea.anagrafica.domain.datigeografici.SuddivisioneAmministrativa;
import it.eurotn.panjea.anagrafica.domain.datigeografici.SuddivisioneAmministrativa.NumeroLivelloAmministrativo;
import it.eurotn.panjea.anagrafica.manager.datigeografici.interfaces.DatiGeograficiManager;
import it.eurotn.panjea.exception.GenericException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.util.PanjeaEJBUtil;

@Stateless(name = "Panjea.DatiGeograficiManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.DatiGeograficiManager")
public class DatiGeograficiManagerBean implements DatiGeograficiManager {

    private static final Logger LOGGER = Logger.getLogger(DatiGeograficiManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Override
    public void cancellaCap(Cap cap) {
        LOGGER.debug("--> Ent cancellaCap");
        try {
            cap = panjeaDAO.load(Cap.class, cap.getId());
            for (Localita localita : cap.getLocalita()) {
                Localita localitaLoad = panjeaDAO.load(Localita.class, localita.getId());
                localitaLoad.getCap().size();
                localitaLoad.getCap().remove(cap);
                salvaLocalita(localitaLoad);
            }
            panjeaDAO.delete(cap);
        } catch (Exception e) {
            LOGGER.error("--> Errore cancellaCap", e);
            throw new GenericException("Errore cancellaCap", e);
        }
        LOGGER.debug("--> Exit cancellaCap");
    }

    @Override
    public void cancellaLocalita(Localita localita) {
        LOGGER.debug("--> Enter cancellaLocalita");
        try {
            panjeaDAO.delete(localita);
        } catch (Exception e) {
            LOGGER.error("--> Errore cancellaLocalita", e);
            throw new GenericException("Errore cancellaLocalita", e);
        }
        LOGGER.debug("--> Exit cancellaLocalita");
    }

    @Override
    public void cancellaNazione(Nazione nazione) {
        LOGGER.debug("--> Enter cancellaNazione");
        try {
            panjeaDAO.delete(nazione);
        } catch (Exception e) {
            LOGGER.error("--> Errore cancellaNazione", e);
            throw new GenericException("Errore cancellaNazione", e);
        }
        LOGGER.debug("--> Exit cancellaNazione");
    }

    @Override
    public void cancellaSuddivisioneAmministrativa(SuddivisioneAmministrativa suddivisioneAmministrativa) {
        LOGGER.debug("--> Enter cancellaSuddivisioneAmministrativa");
        try {
            panjeaDAO.delete(suddivisioneAmministrativa);
        } catch (Exception e) {
            LOGGER.error("--> Errore cancellaSuddivisioneAmministrativa", e);
            throw new GenericException("Errore cancellaSuddivisioneAmministrativa", e);
        }
        LOGGER.debug("--> Exit cancellaSuddivisioneAmministrativa");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Cap> caricaCap(DatiGeografici datiGeografici) {
        LOGGER.debug("--> Enter caricaCap");
        List<Cap> cap = null;
        SuddivisioneAmministrativa suddivisioneAmministrativa = datiGeografici.getUltimaSuddivisioneAmministrativa();
        Localita localita = datiGeografici.getLocalita();
        Nazione nazione = datiGeografici.getNazione();
        String whereAdd = "where ";

        StringBuffer hql = new StringBuffer();
        hql.append(
                "select c from Cap c left join fetch c.livelloAmministrativo1 left join fetch c.livelloAmministrativo2 left join fetch c.livelloAmministrativo3 left join fetch c.livelloAmministrativo4 ");
        if (localita != null && localita.getId() != null) {
            hql.append(" inner join c.localita ll");
            hql.append(" where ll.id=:paramIdLocalita");
            whereAdd = " and ";
        } else if (suddivisioneAmministrativa != null && suddivisioneAmministrativa.getId() != null) {
            switch (suddivisioneAmministrativa.getNumeroLivelloAmministrativo()) {
            case LVL1:
                hql.append(" where c.livelloAmministrativo1.id=:paramIdSuddAmm");
                whereAdd = " and ";
                break;
            case LVL2:
                hql.append(" where c.livelloAmministrativo2.id=:paramIdSuddAmm");
                whereAdd = " and ";
                break;
            case LVL3:
                hql.append(" where c.livelloAmministrativo3.id=:paramIdSuddAmm");
                whereAdd = " and ";
                break;
            case LVL4:
                hql.append(" where c.livelloAmministrativo4.id=:paramIdSuddAmm");
                whereAdd = " and ";
                break;
            default:
                break;
            }
        } else if (nazione != null && nazione.getId() != null) {
            hql.append(" where c.nazione.id=:paramIdNazione");
            whereAdd = " and ";
        }
        Cap capFiltro = datiGeografici.getCap();
        if (capFiltro != null && capFiltro.getDescrizione() != null && !capFiltro.getDescrizione().trim().isEmpty()) {
            hql.append(whereAdd);
            hql.append(" c.descrizione like (:paramDescrizione)");
        }
        hql.append(" order by c.descrizione");

        Query query = panjeaDAO.prepareQuery(hql.toString());
        if (localita != null && localita.getId() != null) {
            query.setParameter("paramIdLocalita", localita.getId());
        } else if (suddivisioneAmministrativa != null) {
            query.setParameter("paramIdSuddAmm", suddivisioneAmministrativa.getId());
        } else if (nazione != null && nazione.getId() != null) {
            query.setParameter("paramIdNazione", nazione.getId());
        }
        if (capFiltro != null && capFiltro.getDescrizione() != null && !capFiltro.getDescrizione().trim().isEmpty()) {
            query.setParameter("paramDescrizione", capFiltro.getDescrizione().trim() + "%");
        }

        try {
            cap = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> Errore caricaCap", e);
            throw new GenericException("Errore caricaCap", e);
        }
        LOGGER.debug("--> Exit caricaCap");
        return cap;
    }

    @Override
    public Cap caricaCap(Integer idCap) {
        return caricaCap(idCap, true);
    }

    /**
     * Carica il cap ed eventualmente le sue localita collegate.
     *
     * @param idCap
     *            l'id del cap da caricare
     * @param initializeCollections
     *            definisce se inizializzare le collection
     * @return Cap
     */
    private Cap caricaCap(Integer idCap, boolean initializeCollections) {
        LOGGER.debug("--> Enter caricaCap");

        Cap cap = null;
        try {
            cap = panjeaDAO.load(Cap.class, idCap);
            // inizializzo caps se necessario
            if (initializeCollections) {
                cap.getLocalita().size();
            }
        } catch (ObjectNotFoundException e) {
            LOGGER.error("--> Errore caricaCap", e);
            throw new GenericException("Errore caricaCap", e);
        }

        LOGGER.debug("--> Exit caricaCap");
        return cap;
    }

    @Override
    public DatiGeografici caricaeCreaDatiGeografici(DatiGeografici datiGeografici) {
        LOGGER.debug("--> Enter caricaeCreaDatiGeografici");
        DatiGeografici datiGeograficiResult = new DatiGeografici();
        // Carico la nazione
        List<Nazione> nazioni = caricaNazioni(datiGeografici.getNazione().getCodice());
        if (nazioni.isEmpty()) {
            Nazione nazione = new Nazione();
            nazione.setCodice(datiGeografici.getNazione().getCodice());
            datiGeograficiResult.setNazione(salvaNazione(nazione));
        } else {
            datiGeograficiResult.setNazione(nazioni.get(0));
        }
        // Carico la regione..lva1
        if (datiGeografici.getLivelloAmministrativo1() != null) {
            List<LivelloAmministrativo1> livelli1 = caricaLivelloAmministrativo1(datiGeograficiResult,
                    datiGeografici.getLivelloAmministrativo1().getNome());
            if (livelli1.isEmpty()) {
                LivelloAmministrativo1 livello1 = new LivelloAmministrativo1();
                livello1.setNome(datiGeografici.getLivelloAmministrativo1().getNome());
                livello1.setNazione(datiGeograficiResult.getNazione());
                datiGeograficiResult
                        .setLivelloAmministrativo1((LivelloAmministrativo1) salvaSuddivisioneAmministrativa(livello1));
            } else {
                datiGeograficiResult.setLivelloAmministrativo1(livelli1.get(0));
            }
        }

        // livello2
        if (datiGeografici.getLivelloAmministrativo2() != null) {
            List<LivelloAmministrativo2> livelli2 = caricaLivelloAmministrativo2(datiGeograficiResult,
                    datiGeografici.getLivelloAmministrativo2().getSigla());
            if (livelli2.isEmpty()) {
                LivelloAmministrativo2 livello2 = new LivelloAmministrativo2();
                livello2.setNazione(datiGeograficiResult.getNazione());
                livello2.setSuddivisioneAmministrativaPrecedente(datiGeograficiResult.getLivelloAmministrativo1());
                livello2.setSigla(datiGeografici.getLivelloAmministrativo2().getSigla());
                datiGeograficiResult
                        .setLivelloAmministrativo2((LivelloAmministrativo2) salvaSuddivisioneAmministrativa(livello2));
            } else {
                datiGeograficiResult.setLivelloAmministrativo2(livelli2.get(0));
            }
        }

        // livello3
        if (datiGeografici.getLivelloAmministrativo3() != null) {
            List<LivelloAmministrativo3> livelli3 = caricaLivelloAmministrativo3(datiGeograficiResult,
                    datiGeografici.getLivelloAmministrativo3().getNome());
            if (livelli3.isEmpty()) {
                LivelloAmministrativo3 livello3 = new LivelloAmministrativo3();
                livello3.setNazione(datiGeograficiResult.getNazione());
                livello3.setSuddivisioneAmministrativaPrecedente(datiGeograficiResult.getLivelloAmministrativo2());
                livello3.setNome(datiGeografici.getLivelloAmministrativo3().getNome());
                datiGeograficiResult
                        .setLivelloAmministrativo3((LivelloAmministrativo3) salvaSuddivisioneAmministrativa(livello3));
            } else {
                datiGeograficiResult.setLivelloAmministrativo3(livelli3.get(0));
            }
        }

        // livello4
        if (datiGeografici.getLivelloAmministrativo4() != null) {
            List<LivelloAmministrativo4> livelli4 = caricaLivelloAmministrativo4(datiGeograficiResult,
                    datiGeografici.getLivelloAmministrativo4().getNome());
            if (livelli4.isEmpty()) {
                LivelloAmministrativo4 livello4 = new LivelloAmministrativo4();
                livello4.setNazione(datiGeograficiResult.getNazione());
                livello4.setSuddivisioneAmministrativaPrecedente(datiGeograficiResult.getLivelloAmministrativo3());
                livello4.setNome(datiGeografici.getLivelloAmministrativo4().getNome());
                datiGeograficiResult
                        .setLivelloAmministrativo4((LivelloAmministrativo4) salvaSuddivisioneAmministrativa(livello4));
            } else {
                datiGeograficiResult.setLivelloAmministrativo4(livelli4.get(0));
            }
        }

        // localita
        if (datiGeografici.getLocalita() != null) {
            datiGeograficiResult.setLocalita(datiGeografici.getLocalita());
            List<Localita> locs = caricaLocalita(datiGeograficiResult);
            if (locs.isEmpty()) {
                Localita loc = new Localita();
                loc.setDescrizione(datiGeograficiResult.getLocalita().getDescrizione());
                loc.setLivelloAmministrativo1(datiGeograficiResult.getLivelloAmministrativo1());
                loc.setLivelloAmministrativo2(datiGeograficiResult.getLivelloAmministrativo2());
                loc.setLivelloAmministrativo3(datiGeograficiResult.getLivelloAmministrativo3());
                datiGeograficiResult.setLocalita(salvaLocalita(loc));
            } else {
                datiGeograficiResult.setLocalita(locs.get(0));
            }
        }

        // cap
        if (datiGeografici.getCap() != null) {
            List<Cap> caps = caricaCap(datiGeograficiResult);
            if (caps.isEmpty()) {
                Cap cap = new Cap();
                cap.setLivelloAmministrativo1(datiGeograficiResult.getLivelloAmministrativo1());
                cap.setLivelloAmministrativo2(datiGeograficiResult.getLivelloAmministrativo2());
                cap.setLivelloAmministrativo3(datiGeograficiResult.getLivelloAmministrativo3());
                cap.setNazione(datiGeograficiResult.getNazione());
                datiGeograficiResult.setCap(salvaCap(cap));
            } else {
                datiGeograficiResult.setCap(null);
            }
        }

        LOGGER.debug("--> Exit caricaeCreaDatiGeografici");
        return datiGeograficiResult;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LivelloAmministrativo1> caricaLivelloAmministrativo1(DatiGeografici datiGeografici, String filtro) {
        LOGGER.debug("--> Enter caricaLivelloAmministrativo1");
        List<LivelloAmministrativo1> livelloAmministrativo1 = null;
        Nazione nazione = datiGeografici.getNazione();

        StringBuffer hql = new StringBuffer();
        hql.append("select l from LivelloAmministrativo1 l where 1=1 ");
        if (nazione != null && nazione.getId() != null) {
            hql.append(" and l.nazione=:paramNazione");
        }
        if (filtro != null) {
            hql.append(" and l.nome like ").append(PanjeaEJBUtil.addQuote(filtro));
        }
        hql.append(" order by l.nome");

        Query query = panjeaDAO.prepareQuery(hql.toString());
        if (nazione != null && nazione.getId() != null) {
            query.setParameter("paramNazione", nazione);
        }

        try {
            livelloAmministrativo1 = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> Errore caricaLivelloAmministrativo1", e);
            throw new GenericException("Errore caricaLivelloAmministrativo1", e);
        }
        LOGGER.debug("--> Exit caricaLivelloAmministrativo1");
        return livelloAmministrativo1;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LivelloAmministrativo2> caricaLivelloAmministrativo2(DatiGeografici datiGeografici, String filtro) {
        LOGGER.debug("--> Enter caricaLivelloAmministrativo2");
        List<LivelloAmministrativo2> livelloAmministrativo2 = null;
        LivelloAmministrativo1 livelloAmministrativo1 = datiGeografici.getLivelloAmministrativo1();
        Nazione nazione = datiGeografici.getNazione();

        StringBuffer hql = new StringBuffer();
        hql.append("select l from LivelloAmministrativo2 l where 1=1 ");
        if (datiGeografici.hasLivelloAmministrativo1()) {
            hql.append(" and l.suddivisioneAmministrativaPrecedente=:paramlvl1");
        } else if (nazione != null && nazione.getId() != null) {
            hql.append(" and l.nazione=:paramNazione");
        }
        if (filtro != null) {
            hql.append(" and l.nome like ").append(PanjeaEJBUtil.addQuote(filtro));
        }
        hql.append(" order by l.nome");

        Query query = panjeaDAO.prepareQuery(hql.toString());
        if (datiGeografici.hasLivelloAmministrativo1()) {
            query.setParameter("paramlvl1", livelloAmministrativo1);
        } else if (nazione != null && nazione.getId() != null) {
            query.setParameter("paramNazione", nazione);
        }

        try {
            livelloAmministrativo2 = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> Errore caricaLivelloAmministrativo2", e);
            throw new GenericException("Errore caricaLivelloAmministrativo2", e);
        }
        LOGGER.debug("--> Exit caricaLivelloAmministrativo2");
        return livelloAmministrativo2;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LivelloAmministrativo3> caricaLivelloAmministrativo3(DatiGeografici datiGeografici, String filtro) {
        LOGGER.debug("--> Enter caricaLivelloAmministrativo3");
        List<LivelloAmministrativo3> livelloAmministrativo3 = null;
        LivelloAmministrativo2 livelloAmministrativo2 = datiGeografici.getLivelloAmministrativo2();
        LivelloAmministrativo1 livelloAmministrativo1 = datiGeografici.getLivelloAmministrativo1();
        Nazione nazione = datiGeografici.getNazione();

        StringBuffer hql = new StringBuffer();
        hql.append("select l from LivelloAmministrativo3 l where 1=1 ");
        if (datiGeografici.hasLivelloAmministrativo2()) {
            hql.append(" and l.suddivisioneAmministrativaPrecedente=:paramlvl2");
        } else if (datiGeografici.hasLivelloAmministrativo1()) {
            hql.append(" and l.suddivisioneAmministrativaPrecedente.suddivisioneAmministrativaPrecedente=:paramlvl1");
        } else if (nazione != null && nazione.getId() != null) {
            hql.append(" and l.nazione=:paramNazione");
        }
        if (filtro != null) {
            hql.append(" and l.nome like ").append(PanjeaEJBUtil.addQuote(filtro));
        }
        hql.append(" order by l.nome");

        Query query = panjeaDAO.prepareQuery(hql.toString());
        if (datiGeografici.hasLivelloAmministrativo2()) {
            query.setParameter("paramlvl2", livelloAmministrativo2);
        } else if (datiGeografici.hasLivelloAmministrativo1()) {
            query.setParameter("paramlvl1", livelloAmministrativo1);
        } else if (nazione != null && nazione.getId() != null) {
            query.setParameter("paramNazione", nazione);
        }

        try {
            livelloAmministrativo3 = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> Errore caricaLivelloAmministrativo3", e);
            throw new GenericException("Errore caricaLivelloAmministrativo3", e);
        }
        LOGGER.debug("--> Exit caricaLivelloAmministrativo3");
        return livelloAmministrativo3;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<LivelloAmministrativo4> caricaLivelloAmministrativo4(DatiGeografici datiGeografici, String filtro) {
        LOGGER.debug("--> Enter caricaLivelloAmministrativo3");
        List<LivelloAmministrativo4> livelloAmministrativo4 = null;
        LivelloAmministrativo3 livelloAmministrativo3 = datiGeografici.getLivelloAmministrativo3();
        LivelloAmministrativo2 livelloAmministrativo2 = datiGeografici.getLivelloAmministrativo2();
        LivelloAmministrativo1 livelloAmministrativo1 = datiGeografici.getLivelloAmministrativo1();
        Nazione nazione = datiGeografici.getNazione();

        StringBuffer hql = new StringBuffer();
        hql.append("select l from LivelloAmministrativo4 l where 1=1 ");
        if (datiGeografici.hasLivelloAmministrativo3()) {
            hql.append(" and l.suddivisioneAmministrativaPrecedente=:paramlvl3");
        } else if (datiGeografici.hasLivelloAmministrativo2()) {
            hql.append(" and l.suddivisioneAmministrativaPrecedente.suddivisioneAmministrativaPrecedente=:paramlvl2");
        } else if (datiGeografici.hasLivelloAmministrativo1()) {
            hql.append(
                    " and l.suddivisioneAmministrativaPrecedente.suddivisioneAmministrativaPrecedente.suddivisioneAmministrativaPrecedente=:paramlvl1");
        } else if (nazione != null && nazione.getId() != null) {
            hql.append(" and l.nazione=:paramNazione");
        }
        if (filtro != null) {
            hql.append(" and l.nome like ").append(PanjeaEJBUtil.addQuote(filtro));
        }
        hql.append(" order by l.nome");

        Query query = panjeaDAO.prepareQuery(hql.toString());
        if (datiGeografici.hasLivelloAmministrativo3()) {
            query.setParameter("paramlvl3", livelloAmministrativo3);
        } else if (datiGeografici.hasLivelloAmministrativo2()) {
            query.setParameter("paramlvl2", livelloAmministrativo2);
        } else if (datiGeografici.hasLivelloAmministrativo1()) {
            query.setParameter("paramlvl1", livelloAmministrativo1);
        } else if (nazione != null && nazione.getId() != null) {
            query.setParameter("paramNazione", nazione);
        }

        try {
            livelloAmministrativo4 = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> Errore caricaLivelloAmministrativo3", e);
            throw new GenericException("Errore caricaLivelloAmministrativo3", e);
        }
        LOGGER.debug("--> Exit caricaLivelloAmministrativo3");
        return livelloAmministrativo4;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Localita> caricaLocalita(DatiGeografici datiGeografici) {
        LOGGER.debug("--> Enter caricaLocalita");
        List<Localita> localita = null;
        SuddivisioneAmministrativa suddivisioneAmministrativa = datiGeografici.getUltimaSuddivisioneAmministrativa();
        Cap cap = datiGeografici.getCap();
        Nazione nazione = datiGeografici.getNazione();
        String whereAdd = "where ";

        StringBuffer hql = new StringBuffer();
        hql.append(
                "select l from Localita l left join fetch l.livelloAmministrativo1 left join fetch l.livelloAmministrativo2 left join fetch l.livelloAmministrativo3 left join fetch l.livelloAmministrativo4 ");
        if (cap != null && cap.getId() != null) {
            hql.append(" inner join l.cap cc");
            hql.append(" where cc.id=:paramIdCap");
            whereAdd = " and ";
        } else if (suddivisioneAmministrativa != null && suddivisioneAmministrativa.getId() != null) {
            switch (suddivisioneAmministrativa.getNumeroLivelloAmministrativo()) {
            case LVL1:
                hql.append(" where l.livelloAmministrativo1.id=:paramIdSuddAmm");
                whereAdd = " and ";
                break;
            case LVL2:
                hql.append(" where l.livelloAmministrativo2.id=:paramIdSuddAmm");
                whereAdd = " and ";
                break;
            case LVL3:
                hql.append(" where l.livelloAmministrativo3.id=:paramIdSuddAmm");
                whereAdd = " and ";
                break;
            case LVL4:
                hql.append(" where l.livelloAmministrativo4.id=:paramIdSuddAmm");
                whereAdd = " and ";
                break;
            default:
                break;
            }
        } else if (nazione != null && nazione.getId() != null) {
            hql.append(" where l.nazione.id=:paramIdNazione");
            whereAdd = " and ";
        }
        Localita locFiltro = datiGeografici.getLocalita();
        if (locFiltro != null && locFiltro.getDescrizione() != null && !locFiltro.getDescrizione().trim().isEmpty()) {
            hql.append(whereAdd);
            hql.append(" l.descrizione like (:paramDescrizione)");
        }
        hql.append(" order by l.descrizione");

        Query query = panjeaDAO.prepareQuery(hql.toString());
        if (cap != null && cap.getId() != null) {
            query.setParameter("paramIdCap", cap.getId());
        } else if (suddivisioneAmministrativa != null) {
            query.setParameter("paramIdSuddAmm", suddivisioneAmministrativa.getId());
        } else if (nazione != null && nazione.getId() != null) {
            query.setParameter("paramIdNazione", nazione.getId());
        }
        if (locFiltro != null && locFiltro.getDescrizione() != null && !locFiltro.getDescrizione().trim().isEmpty()) {
            query.setParameter("paramDescrizione", locFiltro.getDescrizione().trim() + "%");
        }

        try {
            localita = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> Errore caricaLocalita", e);
            throw new GenericException("Errore caricaLocalita", e);
        }
        LOGGER.debug("--> Exit caricaLocalita");
        return localita;
    }

    @Override
    public Localita caricaLocalita(Integer idLocalita) {
        return caricaLocalita(idLocalita, true);
    }

    /**
     * Carica la località ed eventualmente i suoi cap collegati.
     *
     * @param idLocalita
     *            l'id della località da caricare
     * @param initializeCollections
     *            definisce se inizializzare i cap e suddivisioni amministrative
     * @return Localita
     */
    private Localita caricaLocalita(Integer idLocalita, boolean initializeCollections) {
        LOGGER.debug("--> Enter caricaLocalita");

        Localita localita = null;
        try {
            localita = panjeaDAO.load(Localita.class, idLocalita);
            // inizializzo caps se necessario
            if (initializeCollections) {
                localita.getCap().size();
            }
        } catch (ObjectNotFoundException e) {
            LOGGER.error("--> Errore caricaLocalita", e);
            throw new GenericException("Errore caricaLocalita", e);
        }

        LOGGER.debug("--> Exit caricaLocalita");
        return localita;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Nazione> caricaNazioni(String codice) {
        LOGGER.debug("--> Enter caricaNazioni");

        if (codice == null) {
            codice = "%";
        }
        List<Nazione> list = new ArrayList<Nazione>();
        Query query = panjeaDAO.prepareNamedQuery("Nazione.caricaAll");
        query.setParameter("codice", codice);
        try {
            list = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> Errore caricaNazioni", e);
            throw new GenericException("Errore caricaNazioni", e);
        }

        LOGGER.debug("--> Exit caricaNazioni");
        return list;
    }

    @Override
    public List<SuddivisioneAmministrativa> caricaSuddivisioniAmministrative(DatiGeografici datiGeografici,
            SuddivisioneAmministrativa.NumeroLivelloAmministrativo lvl, String filtro) {
        List<SuddivisioneAmministrativa> l = new ArrayList<SuddivisioneAmministrativa>();
        switch (lvl) {
        case LVL1:
            l.addAll(caricaLivelloAmministrativo1(datiGeografici, filtro));
            break;
        case LVL2:
            l.addAll(caricaLivelloAmministrativo2(datiGeografici, filtro));
            break;
        case LVL3:
            l.addAll(caricaLivelloAmministrativo3(datiGeografici, filtro));
            break;
        case LVL4:
            l.addAll(caricaLivelloAmministrativo4(datiGeografici, filtro));
            break;
        default:
            break;
        }
        return l;
    }

    /**
     * Esegue la query per sincronizzare i dati geografici legati alla località o cap in seguito ad una modifica di un
     * link tra una suddivisione amministrativa ed un' altra.
     *
     * @param identificativoLocalitaClass
     *            la class Cap o Località su cui aggiornare i livelli amministrativi
     * @param suddivisioneAmministrativa
     *            la suddivisione amministrativa modificata
     * @param suddivisioneAmministrativaOld
     *            la suddivisione amministrativa esistente prima di essere modificata
     */
    @SuppressWarnings("rawtypes")
    private void executeUpdateIdentificativoLocalitaSql(Class identificativoLocalitaClass,
            SuddivisioneAmministrativa suddivisioneAmministrativa,
            SuddivisioneAmministrativa suddivisioneAmministrativaOld) {

        SuddivisioneAmministrativa suddAmmCollegata = suddivisioneAmministrativa
                .getSuddivisioneAmministrativaPrecedente();
        SuddivisioneAmministrativa suddAmmOld = suddivisioneAmministrativaOld.getSuddivisioneAmministrativaPrecedente();
        Integer idSuddAmmCollegata = null;
        if (suddAmmCollegata != null) {
            idSuddAmmCollegata = suddAmmCollegata.getId();
        }
        Integer idSuddAmmOld = null;
        if (suddAmmOld != null) {
            idSuddAmmOld = suddAmmOld.getId();
        }

        StringBuilder sqlUpdate = new StringBuilder();
        String tableIdentificativoLocalita = null;
        if (identificativoLocalitaClass.equals(Localita.class)) {
            tableIdentificativoLocalita = "geog_localita";
        } else if (identificativoLocalitaClass.equals(Cap.class)) {
            tableIdentificativoLocalita = "geog_cap";
        }

        NumeroLivelloAmministrativo lvlA = suddivisioneAmministrativa.getNumeroLivelloAmministrativo();
        switch (lvlA) {
        case LVL2:
            sqlUpdate = sqlUpdate.append(" update " + tableIdentificativoLocalita + " l ")
                    .append(" set livello1_id=" + idSuddAmmCollegata)
                    .append(" where livello2_id=" + suddivisioneAmministrativa.getId());
            break;
        case LVL3:
            sqlUpdate = sqlUpdate.append("update " + tableIdentificativoLocalita + " l ")
                    .append(" left join geog_livello_2 l2 on l2.id=" + idSuddAmmOld)
                    .append(" left join geog_livello_2 lvl2 on lvl2.id=" + idSuddAmmCollegata)
                    .append(" left join geog_livello_1 lvl1 on lvl2.livello1_id=lvl1.id ")
                    .append(" set l.livello2_id=" + idSuddAmmCollegata + ",").append("l.livello1_id=lvl1.id ")
                    .append(" where l.livello3_id=" + suddivisioneAmministrativa.getId());
            break;
        case LVL4:
            sqlUpdate = sqlUpdate.append("update " + tableIdentificativoLocalita + " l ")
                    .append(" left join geog_livello_3 l3 on l3.id=" + idSuddAmmOld)
                    .append(" left join geog_livello_3 lvl3 on lvl3.id=" + idSuddAmmCollegata)
                    .append(" left join geog_livello_2 lvl2 on lvl2.id=lvl3.livello2_id ")
                    .append("set l.livello3_id=" + idSuddAmmCollegata + ",").append("l.livello2_id=lvl2.id,")
                    .append("l.livello1_id=lvl2.livello1_id ")
                    .append("where l.livello4_id=" + suddivisioneAmministrativa.getId());
            break;
        default:
            break;
        }

        if (!sqlUpdate.toString().equals("")) {
            javax.persistence.Query query = panjeaDAO.getEntityManager().createNativeQuery(sqlUpdate.toString());
            try {
                panjeaDAO.executeQuery(query);
            } catch (Exception e) {
                LOGGER.error("Errore nell query sql eseguita " + query, e);
                throw new GenericException(e);
            }
        }
    }

    @Override
    public List<Cap> ricercaCap(String descrizioneCap) {
        LOGGER.debug("--> Enter ricercaCap");
        Cap capRicerca = new Cap();
        capRicerca.setDescrizione(descrizioneCap);
        DatiGeografici dgRicerca = new DatiGeografici();
        dgRicerca.setCap(capRicerca);
        LOGGER.debug("--> Exit ricercaCap");
        return caricaCap(dgRicerca);
    }

    @Override
    public Cap salvaCap(Cap cap) {
        LOGGER.debug("--> Enter salvaCap");
        Cap capSalvato = null;
        try {
            capSalvato = panjeaDAO.save(cap);
        } catch (Exception e) {
            LOGGER.debug("--> Errore salvaCap", e);
            throw new GenericException("Errore salvaCap", e);
        }
        LOGGER.debug("--> Exit salvaCap");
        return capSalvato;
    }

    @Override
    public Cap salvaCap(Cap cap, List<Localita> listLocalita) {
        LOGGER.debug("--> Enter salvaCap");

        Cap capSalvato = null;

        capSalvato = salvaCap(cap);
        Localita localitaLoad;
        Set<Localita> localitaDaRimuovere = capSalvato.getLocalita();
        if (localitaDaRimuovere != null) {
            for (Localita localitaDaCancellare : localitaDaRimuovere) {
                localitaLoad = caricaLocalita(localitaDaCancellare.getId());
                localitaLoad.getCap().size();
                localitaLoad.getCap().remove(capSalvato);
                salvaLocalita(localitaLoad);
            }
            capSalvato.getLocalita().clear();
        }

        for (Localita localita : listLocalita) {
            localitaLoad = caricaLocalita(localita.getId());
            localitaLoad.getCap().add(capSalvato);
            salvaLocalita(localitaLoad);
        }

        // inizializzazione della Collection di località
        if (capSalvato.getLocalita() != null) {
            capSalvato.getLocalita().size();
        }
        LOGGER.debug("--> Exit salvaCap");
        return capSalvato;
    }

    @Override
    public Cap salvaCap(Cap cap, List<Localita> localitaAggiunte, List<Localita> localitaRimosse) {
        LOGGER.debug("--> Enter salvaCap");
        // mappedby impostata su Localita pertanto e' necessario
        // aggiungere/rimuovere la relazione attraverso l'attributo
        // cap di Localita
        Cap capSave = salvaCap(cap);
        Localita localitaLoad;
        for (Localita localitaAggiunta : localitaAggiunte) {
            localitaLoad = caricaLocalita(localitaAggiunta.getId());
            localitaLoad.getCap().size();
            localitaLoad.getCap().add(capSave);
            salvaLocalita(localitaLoad);
        }
        for (Localita localitaRimossa : localitaRimosse) {
            localitaLoad = caricaLocalita(localitaRimossa.getId());
            localitaLoad.getCap().size();
            localitaLoad.getCap().remove(capSave);
            salvaLocalita(localitaLoad);
        }
        LOGGER.debug("--> Exit salvaCap");
        return capSave;
    }

    @Override
    public Localita salvaLocalita(Localita localita) {
        LOGGER.debug("--> Enter salvaLocalita");

        Localita localitaSalvata = null;
        try {
            localitaSalvata = panjeaDAO.save(localita);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il salvataggio della localita", e);
            throw new GenericException("Errore durante il salvataggio della localita", e);
        }

        LOGGER.debug("--> Exit salvaLocalita");
        return localitaSalvata;
    }

    @Override
    public Localita salvaLocalita(Localita localita, List<Cap> caps) {
        LOGGER.debug("--> Enter salvaLocalita");
        Localita localitaSalvata = null;
        Set<Cap> capSet = new HashSet<Cap>();
        capSet.addAll(caps);
        localita.setCap(capSet);
        localitaSalvata = salvaLocalita(localita);
        LOGGER.debug("--> Exit salvaLocalita");
        return localitaSalvata;
    }

    @Override
    public Localita salvaLocalita(Localita localita, List<Cap> capsAggiunti, List<Cap> capsRimossi) {
        LOGGER.debug("--> Enter salvaLocalita");
        // esegue prima il salvataggio di Localita'

        Localita localitaSalvata = salvaLocalita(localita);
        // poi aggiunge e rimuove caps e riesegue il salvataggio
        localitaSalvata.getCap().size();
        localitaSalvata.getCap().removeAll(capsRimossi);
        localitaSalvata.getCap().addAll(capsAggiunti);
        localitaSalvata = salvaLocalita(localitaSalvata);

        LOGGER.debug("--> Exit salvaLocalita");
        return localitaSalvata;
    }

    @Override
    public Nazione salvaNazione(Nazione nazione) {
        LOGGER.debug("--> Enter salvaNazione");
        Nazione nazioneSalvata;
        try {
            nazioneSalvata = panjeaDAO.save(nazione);
        } catch (DAOException e) {
            LOGGER.error("--> Errore salvaNazione", e);
            throw new GenericException("Errore salvaNazione", e);
        }
        LOGGER.debug("--> Exit salvaNazione");
        return nazioneSalvata;
    }

    @Override
    public SuddivisioneAmministrativa salvaSuddivisioneAmministrativa(
            SuddivisioneAmministrativa suddivisioneAmministrativa) {
        LOGGER.debug("--> Enter salvaSuddivisioneAmministrativa");
        SuddivisioneAmministrativa suddivisioneAmministrativaSalvata;
        try {
            if (suddivisioneAmministrativa.getId() != null) {
                SuddivisioneAmministrativa suddivisioneAmministrativaOld = panjeaDAO
                        .load(suddivisioneAmministrativa.getClass(), suddivisioneAmministrativa.getId());

                executeUpdateIdentificativoLocalitaSql(Localita.class, suddivisioneAmministrativa,
                        suddivisioneAmministrativaOld);
                executeUpdateIdentificativoLocalitaSql(Cap.class, suddivisioneAmministrativa,
                        suddivisioneAmministrativaOld);
            }

            suddivisioneAmministrativaSalvata = panjeaDAO.save(suddivisioneAmministrativa);
        } catch (DAOException e) {
            LOGGER.error("--> Errore salvaSuddivisioneAmministrativa", e);
            throw new GenericException("Errore salvaSuddivisioneAmministrativa", e);
        }
        LOGGER.debug("--> Exit salvaSuddivisioneAmministrativa");
        return suddivisioneAmministrativaSalvata;
    }

}
