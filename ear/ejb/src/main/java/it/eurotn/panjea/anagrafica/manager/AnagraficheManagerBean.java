package it.eurotn.panjea.anagrafica.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.dao.exception.VincoloException;
import it.eurotn.panjea.anagrafica.domain.Anagrafica;
import it.eurotn.panjea.anagrafica.domain.CategoriaEntita;
import it.eurotn.panjea.anagrafica.domain.SedeAnagrafica;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.AnagraficaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficheManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.util.RubricaDTO;
import it.eurotn.panjea.anagrafica.util.RubricaDTO.ChildrenType;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * SessionBean Manager per {@link Anagrafica} e {@link SedeAnagrafica}.
 *
 * @author adriano
 * @version 1.0, 17/dic/07
 */
@Stateless(name = "Panjea.AnagraficheManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AnagraficheManager")
public class AnagraficheManagerBean implements AnagraficheManager {
    private static Logger logger = Logger.getLogger(AnagraficheManagerBean.class);
    @Resource
    private SessionContext sessionContext;
    /**
     * @uml.property name="panjeaDAO"
     * @uml.associationEnd
     */
    @EJB
    private PanjeaDAO panjeaDAO;

    @Override
    public void cancellaCategoriaEntita(CategoriaEntita categoriaEntita) {
        logger.debug("--> Enter cancellaCategoriaEntita");
        try {
            panjeaDAO.delete(categoriaEntita);
        } catch (VincoloException e) {
            logger.error(
                    "--> Errore durante la cancellazione della categoria entita con id = " + categoriaEntita.getId(),
                    e);
            throw new RuntimeException(e);
        } catch (DAOException e) {
            logger.error(
                    "--> Errore durante la cancellazione della categoria entita con id = " + categoriaEntita.getId(),
                    e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit cancellaCategoriaEntita");
    }

    @Override
    public void cancellaSedeAnagrafica(SedeAnagrafica sedeAnagrafica) {
        logger.debug("--> Enter cancellaSedeAnagrafica");
        try {
            panjeaDAO.delete(sedeAnagrafica);
        } catch (VincoloException e) {
            logger.error("--> Errore durante la cancellazione della sede anagrafica con id = " + sedeAnagrafica.getId(),
                    e);
            throw new RuntimeException(e);
        } catch (DAOException e) {
            logger.error("--> Errore durante la cancellazione della sede anagrafica con id = " + sedeAnagrafica.getId(),
                    e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit cancellaSedeAnagrafica");
    }

    /*
     * @seeit.eurotn.panjea.anagrafica.manager.interfaces.AnagraficheManager#
     * caricaAnagrafica(java.lang.Integer)
     */
    @Override
    public Anagrafica caricaAnagrafica(Integer idAnagrafica) throws AnagraficaServiceException {
        logger.debug("--> Enter caricaAnagrafica");
        try {
            Anagrafica anagrafica = panjeaDAO.load(Anagrafica.class, idAnagrafica);
            logger.debug("--> Exit caricaAnagrafica");
            return anagrafica;
        } catch (ObjectNotFoundException e) {
            logger.error("--> errore, impossibile recuperare Anagrafica identificata da id " + idAnagrafica, e);
            throw new AnagraficaServiceException(e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Anagrafica> caricaAnagrafiche(String partitaIva, String codiceFiscale)
            throws AnagraficaServiceException {
        try {
            logger.debug("--> Enter caricaAnagrafiche");
            Map<String, Object> parametri = new HashMap<String, Object>();
            StringBuffer where = new StringBuffer(" where a.codiceAzienda = :paramCodiceAzienda ");
            parametri.put("paramCodiceAzienda", getJecPrincipal().getCodiceAzienda());
            if ((partitaIva != null) && (!"".equals(partitaIva))) {
                where.append(" and a.partiteIVA = :paramPartiteIVA ");
                parametri.put("paramPartiteIVA", partitaIva);
            }
            if ((codiceFiscale != null) && (!"".equals(codiceFiscale))) {
                where.append(" and a.codiceFiscale = :paramCodiceFiscale ");
                parametri.put("paramCodiceFiscale", codiceFiscale);
            }

            StringBuffer select = new StringBuffer(" from Anagrafica a ");
            Query query = panjeaDAO.prepareQuery(select.append(where.toString()).toString());
            for (String key : parametri.keySet()) {
                query.setParameter(key, parametri.get(key));
            }
            List<Anagrafica> anagrafiche = panjeaDAO.getResultList(query);
            logger.debug("--> Exit caricaAnagrafiche");
            return anagrafiche;
        } catch (DAOException ex) {
            logger.error("--> errore, impossibile caricare le anagrafiche", ex);
            throw new AnagraficaServiceException(ex);
        }

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RubricaDTO> caricaAnagraficheFull() {
        logger.debug("--> Enter caricaAnagraficheFull");

        // Carico entita e anagrafiche e creo la rubrica
        org.hibernate.Query query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createQuery(
                "select e.id as id,e.version as version, e.id as idEntita,concat(e.codice) as codice,e.class as tipo,a.denominazione as denominazione,a.codiceFiscale as codiceFiscale,a.partiteIVA as piva from EntitaLite e inner join e.anagrafica a where e.abilitato = true");
        query.setResultTransformer(Transformers.aliasToBean(RubricaDTO.class));
        List<RubricaDTO> rubricaEntita = query.list();

        Map<Integer, RubricaDTO> mapEntita = new HashMap<Integer, RubricaDTO>();
        for (RubricaDTO rubricaDTO : rubricaEntita) {
            mapEntita.put(rubricaDTO.getId(), rubricaDTO);
        }

        // Carico le sedi entita e aggiorno la rubrica esistente
        query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createQuery(
                "select se.id as id ,se.entita.id as idRif, se.entita.id as idEntita,sa.indirizzo as indirizzo,sa.telefono as telefono,sa.fax as fax,sa.indirizzoMail as email,sa.indirizzoPEC as indirizzoPEC,sa.indirizzoMailSpedizione as indirizzoMailSpedizione,loc.descrizione as localita,cap.descrizione as cap,liv1.nome as livelloAmministrativo1,liv2.nome as livelloAmministrativo2,liv3.nome as livelloAmministrativo3,liv4.nome as livelloAmministrativo4,naz.descrizione as nazione,se.tipoSede.sedePrincipale as sedePrincipale,concat(se.codice) as codice,sa.descrizione as denominazione from SedeEntita se inner join se.sede sa left join sa.datiGeografici.livelloAmministrativo1 liv1 left join sa.datiGeografici.livelloAmministrativo2 liv2 left join sa.datiGeografici.livelloAmministrativo3 liv3 left join sa.datiGeografici.livelloAmministrativo4 liv4 left join sa.datiGeografici.localita loc left join sa.datiGeografici.nazione naz left join sa.datiGeografici.cap cap where se.abilitato=true ");
        query.setResultTransformer(Transformers.aliasToBean(RubricaDTO.class));
        List<RubricaDTO> rubricaSedi = query.list();

        Map<Integer, RubricaDTO> mapSedi = new HashMap<Integer, RubricaDTO>();
        for (RubricaDTO rubricaDTO : rubricaSedi) {
            // controllo che la sede abbia una entita nella mappa perchè potrei non trovarla nel
            // caso in cui la sede non
            // sia disabilitata e l'entità si
            if (mapEntita.get(rubricaDTO.getIdRif()) != null) {
                RubricaDTO rubricaSede = mapEntita.get(rubricaDTO.getIdRif()).updateData(rubricaDTO, ChildrenType.SEDE);
                mapSedi.put(rubricaDTO.getId(), rubricaSede);
            }
        }

        // Carico i contatti delle sedi e aggiorno la rubrica esistente
        query = ((Session) panjeaDAO.getEntityManager().getDelegate()).createQuery(
                "select cs.id as id ,cs.sedeEntita.id as idRif,cs.sedeEntita.entita.id as idEntita,c.interno as telefono,c.email as email,c.nome as codice,c.cognome as denominazione,c.cellulare as cell,c.fax as fax from ContattoSedeEntita cs inner join cs.contatto c where cs.sedeEntita.abilitato = true");
        query.setResultTransformer(Transformers.aliasToBean(RubricaDTO.class));
        List<RubricaDTO> rubricaContatti = query.list();

        for (RubricaDTO rubricaDTO : rubricaContatti) {
            if (mapSedi.get(rubricaDTO.getIdRif()) != null) {
                mapSedi.get(rubricaDTO.getIdRif()).updateData(rubricaDTO, ChildrenType.CONTATTO);
            }
        }

        //
        List<RubricaDTO> rubrica = new ArrayList<RubricaDTO>();
        rubrica.addAll(mapEntita.values());

        logger.debug("--> Exit caricaAnagraficheFull");
        return rubrica;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AnagraficaLite> caricaAnagraficheSearchObject(String codice, String denominazione) {
        logger.debug("--> Enter caricaAnagraficheSearchObject");

        StringBuilder sb = new StringBuilder();
        sb.append(
                "select anag.id as id,anag.denominazione as denominazione,anag.partiteIVA as partiteIVA,anag.codiceFiscale as codiceFiscale ");
        sb.append("from AnagraficaLite anag ");
        sb.append("where anag.codiceAzienda = :codiceAzienda ");
        if (codice != null) {
            sb.append(" and (anag.partiteIVA like :codice or anag.codiceFiscale like :codice) ");
        }
        if (denominazione != null) {
            sb.append(" and anag.denominazione like :denominazione ");
        }

        Query query = panjeaDAO.prepareQuery(sb.toString(), AnagraficaLite.class, null);
        query.setParameter("codiceAzienda", getJecPrincipal().getCodiceAzienda());
        if (codice != null) {
            query.setParameter("codice", codice.replace("*", "%") + "%");
        }
        if (denominazione != null) {
            query.setParameter("denominazione", denominazione.replace("*", "%") + "%");
        }

        List<AnagraficaLite> anagrafiche = new ArrayList<AnagraficaLite>();
        try {
            anagrafiche = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento delle anagrafiche.", e);
            throw new RuntimeException("errore durante il caricamento delle anagrafiche.", e);
        }

        logger.debug("--> Exit caricaAnagraficheSearchObject");
        return anagrafiche;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CategoriaEntita> caricaCategoriaEntitaByEntita(Integer idEntita) throws AnagraficaServiceException {
        logger.debug("--> Enter caricaCategoriaEntitaByEntita");
        Query query = panjeaDAO.prepareNamedQuery("Entita.caricaCategoriaEntitaByEntita");
        query.setParameter("paramIdEntita", idEntita);
        List<CategoriaEntita> categorie;
        try {
            categorie = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> errore, impossibile recuperare le categorie della entita ", e);
            throw new AnagraficaServiceException(e);
        }
        logger.debug("--> Exit caricaCategoriaEntitaByEntita");
        return categorie;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CategoriaEntita> caricaCategorieEntita(String fieldSearch, String valueSearch)
            throws AnagraficaServiceException {
        logger.debug("--> Enter caricaCategoriaEntita");
        StringBuilder sb = new StringBuilder("from CategoriaEntita ce ");
        if (valueSearch != null) {
            sb.append(" where ").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
        }
        sb.append(" order by ");
        sb.append(fieldSearch);

        Query query = panjeaDAO.prepareQuery(sb.toString());
        List<CategoriaEntita> categoria;
        try {
            categoria = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> errore, impossibile recuperare le categoria Entita ", e);
            throw new AnagraficaServiceException(e);
        }
        logger.debug("--> Exit caricaCategoriaEntita");
        return categoria;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SedeEntita> caricaEntitaByCategorie(List<CategoriaEntita> categorie) {
        logger.debug("--> Enter caricaEntitaByCategorie");
        List<SedeEntita> result = new ArrayList<>();
        Query query = panjeaDAO.prepareQuery(
                "select s from SedeEntita  s  join fetch  s.categoriaEntita cat join fetch s.entita e join fetch e.anagrafica  where cat in (:paramCategoria)");
        for (CategoriaEntita categoriaEntita : categorie) {
            query.setParameter("paramCategoria", categoriaEntita);
            List<SedeEntita> resultCategoria = null;
            try {
                resultCategoria = panjeaDAO.getResultList(query);
            } catch (DAOException e) {
                logger.error("-->errore nel caricare le sedi per la categoria", e);
                throw new RuntimeException(e);
            }
            if (result.isEmpty()) {
                result = resultCategoria;
            } else {
                result.retainAll(resultCategoria);
            }
        }
        logger.debug("--> Exit caricaEntitaByCategorie");
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<it.eurotn.panjea.anagrafica.domain.SedeAnagrafica> caricaSediAnagrafica(Anagrafica anagrafica)
            throws AnagraficaServiceException {
        logger.debug("--> Enter caricaSediAnagrafica");
        Query query = panjeaDAO.prepareNamedQuery("SedeAnagrafica.caricaSediAnagrafica");
        query.setParameter("paramIdAnagrafica", anagrafica.getId());
        query.setParameter("paramAbilitato", true);
        List<SedeAnagrafica> sedi;
        try {
            sedi = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> errore, impossibile recuperare le sedi anagrafica ", e);
            throw new AnagraficaServiceException(e);
        }
        logger.debug("--> Exit caricaSediAnagrafica");
        return sedi;
    }

    /*
     * @seeit.eurotn.panjea.anagrafica.manager.interfaces.AnagraficheManager#
     * eliminaSediAnagraficheOrfane()
     */
    @Override
    public void eliminaSediAnagraficheOrfane() {
        logger.debug("--> Enter eliminaSediAnagraficheOrfane");
        Query query = panjeaDAO.prepareNamedQuery("SedeAnagrafica.eliminaAnagraficheOrfane");
        try {
            panjeaDAO.executeQuery(query);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit eliminaSediAnagraficheOrfane");
    }

    /**
     * @return Principal corrente
     */
    private JecPrincipal getJecPrincipal() {
        return (JecPrincipal) sessionContext.getCallerPrincipal();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isSedeOrphan(it.eurotn.panjea.anagrafica.domain.SedeAnagrafica sedeAnagrafica)
            throws AnagraficaServiceException {
        logger.debug("--> Enter isSedeOrphan");
        Query query = panjeaDAO.prepareNamedQuery("Anagrafica.caricaBySedeAnagrafica");
        query.setParameter("paramIdSedeAnagrafica", sedeAnagrafica.getId());
        List<Anagrafica> anagrafiche = new ArrayList<Anagrafica>();
        try {
            anagrafiche = panjeaDAO.getResultList(query);

        } catch (DAOException e) {
            logger.error("--> errore, impossibile ottenere la lista di anagrafiche relazionate con sedeAnagrafica "
                    + sedeAnagrafica, e);
            throw new AnagraficaServiceException(e);
        }
        query = panjeaDAO.prepareNamedQuery("SedeEntita.caricaBySedeAnagrafica");
        query.setParameter("paramIdSedeAnagrafica", sedeAnagrafica.getId());
        List<SedeEntita> sedi = new ArrayList<SedeEntita>();
        try {
            sedi = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> errore, impossibile ottenere la lista di sedi relazionate con sedeAnagrafica "
                    + sedeAnagrafica, e);
            throw new AnagraficaServiceException(e);
        }
        logger.debug("--> Exit isSedeOrphan result  " + (anagrafiche.size() == 0 && sedi.size() <= 1));
        return (anagrafiche.size() == 0 && sedi.size() <= 1);
    }

    @Override
    public CategoriaEntita salvaCategoriaEntita(CategoriaEntita categoriaEntita) {
        logger.debug("--> Enter salvaContatto");
        CategoriaEntita categoriaEntitaSave;
        try {
            categoriaEntitaSave = panjeaDAO.save(categoriaEntita);
            logger.debug("--> Exit salvaContatto");
            return categoriaEntitaSave;
        } catch (DAOException e) {
            logger.error("--> salva categoria entita errore DAO ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<?> test() {
        return null;
    }
}
