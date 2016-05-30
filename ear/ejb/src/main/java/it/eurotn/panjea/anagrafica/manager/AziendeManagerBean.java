package it.eurotn.panjea.anagrafica.manager;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.DuplicateKeyObjectException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.dao.exception.StaleObjectStateException;
import it.eurotn.panjea.anagrafica.domain.Azienda;
import it.eurotn.panjea.anagrafica.domain.RapportoBancarioAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeAzienda;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.anagrafica.domain.TipoSedeEntita;
import it.eurotn.panjea.anagrafica.domain.lite.AziendaLite;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.domain.lite.FornitoreLite;
import it.eurotn.panjea.anagrafica.domain.lite.SedeEntitaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AnagraficheTipologieManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediAziendaManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.anagrafica.service.exception.AnagraficaServiceException;
import it.eurotn.panjea.anagrafica.service.exception.SedeEntitaPrincipaleAlreadyExistException;
import it.eurotn.panjea.anagrafica.util.AziendaAnagraficaDTO;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * SessionBean Manager di {@link Azienda} .
 *
 * @author adriano
 * @version 1.0, 17/dic/07
 */
@Stateless(name = "Panjea.AziendeManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.AziendeManager")
public class AziendeManagerBean implements AziendeManager {

    private static Logger logger = Logger.getLogger(AziendeManagerBean.class);
    @Resource
    private SessionContext sessionContext;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private SediAziendaManager sediAziendaManager;

    @EJB
    private AnagraficheTipologieManager anagraficheTipologieManager;

    @EJB
    private SediEntitaManager sediEntitaManager;

    @Override
    public void cambiaSedePrincipaleAzienda(SedeAzienda nuovaSedeAziendaPrincipaleAzienda,
            TipoSedeEntita tipoSedeSostitutivaEntita) throws AnagraficaServiceException {
        logger.debug("--> Enter cambiaSedePrincipaleAzienda");
        // recupera azienda dalla nuova sede principale
        Azienda azienda = nuovaSedeAziendaPrincipaleAzienda.getAzienda();
        // carica la sede principale corrente
        SedeAzienda sedeAziendaPrincipale = null;
        try {
            sedeAziendaPrincipale = sediAziendaManager.caricaSedePrincipaleAzienda(azienda);
        } catch (AnagraficaServiceException e1) {
            logger.error("--> errore, impossibile caricare la sede principale corrente  ", e1);
            throw e1;
        }
        // sostituisci il tipo sede della sede principale da variare
        sedeAziendaPrincipale.setTipoSede(tipoSedeSostitutivaEntita);
        try {
            sedeAziendaPrincipale = sediAziendaManager.salvaSedeAzienda(sedeAziendaPrincipale);
        } catch (SedeEntitaPrincipaleAlreadyExistException e) {
            logger.error("--> errore, impossibile salvare il tipo sede alla sede azienda principale precedente ", e);
            throw new RuntimeException(e);
        } catch (AnagraficaServiceException e) {
            logger.error("--> errore, impossibile salvare il tipo sede alla sede azienda principale precedente ", e);
            throw new RuntimeException(e);
        }
        TipoSedeEntita tipoSedeEntitaPrincipale;
        try {
            tipoSedeEntitaPrincipale = anagraficheTipologieManager.caricaTipoSedeEntitaPrincipale();
        } catch (AnagraficaServiceException e) {
            logger.error("--> errore, impossibile caricare la tipo sede principale ", e);
            throw new RuntimeException(e);
        }
        // salvo la sede principale di azienda
        nuovaSedeAziendaPrincipaleAzienda.setTipoSede(tipoSedeEntitaPrincipale);
        try {
            sediAziendaManager.salvaSedeAzienda(nuovaSedeAziendaPrincipaleAzienda);
        } catch (AnagraficaServiceException e) {
            logger.error("--> errore, impossibile modificare il tipo sede alla nuova sede azienda principale ", e);
            throw new RuntimeException(e);
        } catch (SedeEntitaPrincipaleAlreadyExistException e) {
            logger.error("--> errore, impossibile modificare il tipo sede alla nuova sede azienda princiapale  ", e);
            throw new RuntimeException(e);
        }

        logger.debug("--> Exit cambiaSedePrincipaleAzienda");
    }

    @Override
    public void cancellaRapportoBancario(RapportoBancarioAzienda rapportoBancario) throws AnagraficaServiceException {
        logger.debug("--> Enter cancellaRapportoBancario");
        if (rapportoBancario.isNew()) {
            logger.error("--> Impossibile cancellare il rapporto bancario, id nullo.");
            throw new AnagraficaServiceException("Impossibile cancellare il rapporto bancario, id nullo.");
        }
        try {
            for (SedeEntita sede : rapportoBancario.getSediEntita()) {
                SedeEntita sedeEntita = panjeaDAO.loadLazy(SedeEntita.class, sede.getId());
                sedeEntita.setRapportoBancarioAzienda(null);
                panjeaDAO.save(sedeEntita);
            }
            panjeaDAO.delete(rapportoBancario);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit cancellaRapportoBancario");
    }

    @Override
    public AziendaLite caricaAzienda() {
        return caricaAzienda(false);
    }

    @Override
    public AziendaLite caricaAzienda(boolean loadSede) {
        try {
            return caricaAzienda(getPrincipal().getCodiceAzienda(), loadSede);
        } catch (AnagraficaServiceException e) {
            logger.error("--> errore nel caricare l'azienda loggata " + getPrincipal().getCodiceAzienda(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public AziendaLite caricaAzienda(String codiceAzienda) throws AnagraficaServiceException {
        return caricaAzienda(codiceAzienda, false);
    }

    @Override
    public AziendaLite caricaAzienda(String codiceAzienda, boolean loadSede) throws AnagraficaServiceException {
        logger.debug("--> Enter caricaAziendaLite");
        AziendaLite aziendaLite = null;
        String namedQuery = "AziendaLite.caricaByCodice";
        Query query = panjeaDAO.prepareNamedQuery(namedQuery);
        query.setParameter("paramCodiceAzienda", codiceAzienda);
        try {
            aziendaLite = (AziendaLite) panjeaDAO.getSingleResult(query);
            if (logger.isDebugEnabled()) {
                logger.debug("--> azienda lite caricata " + aziendaLite);
            }
            if (loadSede) {
                Azienda azienda = new Azienda();
                azienda.setId(aziendaLite.getId());
                azienda.setVersion(azienda.getVersion());
                SedeAzienda sedeAzienda = sediAziendaManager.caricaSedePrincipaleAzienda(azienda);

                aziendaLite.setNazione(sedeAzienda.getSede().getDatiGeografici().getNazione());
                aziendaLite.setLocalita(sedeAzienda.getSede().getDatiGeografici().getLocalita());
                aziendaLite.setCap(sedeAzienda.getSede().getDatiGeografici().getCap());
                aziendaLite.setLivelloAmministrativo1(
                        sedeAzienda.getSede().getDatiGeografici().getLivelloAmministrativo1());
                aziendaLite.setLivelloAmministrativo2(
                        sedeAzienda.getSede().getDatiGeografici().getLivelloAmministrativo2());
                aziendaLite.setLivelloAmministrativo3(
                        sedeAzienda.getSede().getDatiGeografici().getLivelloAmministrativo3());
                aziendaLite.setLivelloAmministrativo4(
                        sedeAzienda.getSede().getDatiGeografici().getLivelloAmministrativo4());
                aziendaLite.setIndirizzo(sedeAzienda.getSede().getIndirizzo());
            }
        } catch (DAOException e) {
            throw new AnagraficaServiceException(e.getMessage());
        }
        logger.debug("--> Exit caricaAziendaLite");
        return aziendaLite;
    }

    @Override
    public AziendaAnagraficaDTO caricaAziendaAnagrafica(String codice) throws AnagraficaServiceException {
        logger.debug("--> Enter caricaAziendaAnagrafica");
        AziendaAnagraficaDTO aziendaAnagraficaDTO = new AziendaAnagraficaDTO();

        Azienda azienda = caricaAziendaByCodice(codice);
        if (azienda == null) {
            return null;
        }

        aziendaAnagraficaDTO.setAzienda(azienda);
        SedeAzienda sedeAzienda = sediAziendaManager.caricaSedePrincipaleAzienda(azienda);
        aziendaAnagraficaDTO.setSedeAzienda(sedeAzienda);
        logger.debug("--> Exit caricaAziendaAnagrafica");
        return aziendaAnagraficaDTO;
    }

    @Override
    public Azienda caricaAziendaByCodice(String codice) throws AnagraficaServiceException {
        logger.debug("--> Enter caricaAziendaByCodice");
        if ("".equals(codice)) {
            throw new AnagraficaServiceException("CODICE AZIENDA VUOTO");
        }
        Query query = panjeaDAO.prepareNamedQuery("Azienda.caricaByCodice");
        query.setParameter("paramCodice", codice);
        Azienda azienda;
        try {
            azienda = (Azienda) panjeaDAO.getSingleResult(query);
        } catch (DAOException e) {
            throw new AnagraficaServiceException(e);
        }
        logger.debug("--> Exit caricaAziendaByCodice");
        return azienda;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<AziendaLite> caricaAziende() throws AnagraficaServiceException {
        logger.debug("--> Enter caricaAziende");
        List<AziendaLite> aziende = null;
        String namedQuery = "AziendaLite.caricaAll";
        Query query = panjeaDAO.prepareNamedQuery(namedQuery);
        try {
            aziende = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> errore in getresultList caricaAziende", e);
            throw new AnagraficaServiceException(e.getMessage());
        }
        logger.debug("--> Exit caricaAziende");
        return aziende;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<RapportoBancarioAzienda> caricaRapportiBancariAzienda(String fieldSearch, String valueSearch)
            throws AnagraficaServiceException {
        logger.debug("--> Enter caricaRapportiBancariAzienda");
        StringBuilder sb = new StringBuilder(
                "from RapportoBancarioAzienda rb where rb.azienda.codice = :paramCodiceAzienda and rb.abilitato = true ");
        if (valueSearch != null) {
            sb.append(" and rb.").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
        }
        String fieldOrder = fieldSearch;
        if (fieldOrder == null) {
            fieldOrder = "numero";
        }
        sb.append(" order by rb.").append(fieldOrder);
        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("paramCodiceAzienda", getPrincipal().getCodiceAzienda());
        List<RapportoBancarioAzienda> rapporti = null;
        try {
            rapporti = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> errore, impossibile caricare Rapporti Bancari Azienda  ", e);
            throw new AnagraficaServiceException(e);
        }
        return rapporti;
    }

    @Override
    public RapportoBancarioAzienda caricaRapportoBancario(Integer idRapportoBancario, boolean initializeLazy) {
        logger.debug("--> Enter caricaRapportoBancario");

        RapportoBancarioAzienda rapportoBancarioAzienda = null;
        try {
            rapportoBancarioAzienda = panjeaDAO.load(RapportoBancarioAzienda.class, idRapportoBancario);
            if (initializeLazy) {
                rapportoBancarioAzienda.getSediEntita().size();
                for (SedeEntita sedeEntita : rapportoBancarioAzienda.getSediEntita()) {
                    Hibernate.initialize(sedeEntita.getEntita().getAnagrafica());
                }
                if (rapportoBancarioAzienda.getRapportoBancarioRegolamentazione() != null) {
                    rapportoBancarioAzienda.getRapportoBancarioRegolamentazione().getId();
                }
                if (rapportoBancarioAzienda.getSottoConto() != null) {
                    rapportoBancarioAzienda.getSottoConto().getId();
                }
                if (rapportoBancarioAzienda.getSottoContoEffettiAttivi() != null) {
                    rapportoBancarioAzienda.getSottoContoEffettiAttivi().getId();
                }
                if (rapportoBancarioAzienda.getSottoContoAnticipoFatture() != null) {
                    rapportoBancarioAzienda.getSottoContoAnticipoFatture().getId();
                }
            }
        } catch (ObjectNotFoundException e) {
            logger.error("--> Errore durante il caricamento del rapporto bancario", e);
            throw new RuntimeException("Errore durante il caricamento del rapporto bancario", e);
        }

        logger.debug("--> Exit caricaRapportoBancario");
        return rapportoBancarioAzienda;
    }

    @Override
    public RapportoBancarioAzienda caricaRapportoBancarioAzienda(EntitaLite entita, SedeEntitaLite sedeEntita,
            TipoPagamento tipoPagamento) {
        logger.debug("--> Enter caricaRapportoBancarioAzienda");

        RapportoBancarioAzienda result = null;

        // cerco prima il rapporto legato all'entità se esiste e se l'entità è un fornitore
        if (entita != null && entita.getClass().equals(FornitoreLite.class)) {
            // prendo subito il rapporto bancario legato alla sede he ho sul documento.
            SedeEntitaLite sedeEntitaLite = panjeaDAO.loadLazy(SedeEntitaLite.class, sedeEntita.getId());
            result = sedeEntitaLite.getRapportoBancarioAzienda();

            // se non lo trovo prendo quello sulla sede principale del fornitore. NB: un rapporto
            // bancario settato sulla
            // sede principale del fornitore è valido anche per tutte le sue altre sedi se non ne
            // viene specificato uno
            if (result == null) {
                try {
                    SedeEntita sedeEntitaPrinc = sediEntitaManager
                            .caricaSedePredefinitaEntita(entita.creaProxyEntita());
                    result = sedeEntitaPrinc.getRapportoBancarioAzienda();
                } catch (AnagraficaServiceException e) {
                    result = null;
                }
            }
        }

        // se non ho trovato un rapporto bancario provo con il tipo pagamento
        if (result == null) {
            List<RapportoBancarioAzienda> rapporti;
            try {
                rapporti = caricaRapportiBancariAzienda("numero", null);
            } catch (AnagraficaServiceException e) {
                throw new RuntimeException(e);
            }
            for (RapportoBancarioAzienda rapportoBancarioAzienda : rapporti) {
                if (rapportoBancarioAzienda.getDefaultPagamenti().contains(tipoPagamento)) {
                    result = rapportoBancarioAzienda;
                    break;
                }
            }
            if (result == null && rapporti.size() > 0) {
                logger.debug("--> Exit caricaRapportoBancarioPerTipoPagamento con il primo rapporto");
                return rapporti.get(0);
            }
        }

        logger.debug("--> Exit caricaRapportoBancarioAzienda");
        return result;
    }

    /**
     * @return principal
     */
    private JecPrincipal getPrincipal() {
        return (JecPrincipal) sessionContext.getCallerPrincipal();
    }

    @Override
    public Azienda salvaAzienda(Azienda azienda) throws AnagraficaServiceException {
        logger.debug("--> Enter salvaAzienda");
        Azienda aziendaSave = null;
        try {
            aziendaSave = panjeaDAO.save(azienda);
        } catch (DAOException e) {
            throw new RuntimeException();
        }
        logger.debug("--> Exit salvaAzienda");
        return aziendaSave;
    }

    @Override
    public AziendaAnagraficaDTO salvaAziendaAnagrafica(AziendaAnagraficaDTO aziendaAnagraficaDTO)
            throws AnagraficaServiceException, DuplicateKeyObjectException, StaleObjectStateException,
            SedeEntitaPrincipaleAlreadyExistException {
        logger.debug("--> Enter salvaAziendaAnagrafica");

        // recupero azienda e sede principale
        Azienda azienda = aziendaAnagraficaDTO.getAzienda();
        SedeAzienda sedeAzienda = aziendaAnagraficaDTO.getSedeAzienda();

        // salvo azienda e sede azienda
        Azienda aziendaSave = salvaAzienda(azienda);
        SedeAzienda sedeAziendaSave = sediAziendaManager.salvaSedeAzienda(sedeAzienda);

        // ritorno l' aziendaAnagrafica salvata
        aziendaAnagraficaDTO.setAzienda(aziendaSave);
        aziendaAnagraficaDTO.setSedeAzienda(sedeAziendaSave);
        logger.debug("--> Exit salvaAziendaAnagrafica");
        return aziendaAnagraficaDTO;
    }

    @Override
    public RapportoBancarioAzienda salvaRapportoBancarioAzienda(RapportoBancarioAzienda rapportoBancarioAzienda)
            throws AnagraficaServiceException {
        logger.debug("--> Enter salvaRapportoBancario");
        try {
            // Se non ho l'azienda errore
            if (rapportoBancarioAzienda.getAzienda() == null) {
                logger.error("--> Impossibile salvare il rapporto , Azienda nullo.");
                throw new AnagraficaServiceException("--> Impossibile salvare il rapporto , Azienda nullo.");
            }

            if (rapportoBancarioAzienda.getId() != null) {
                RapportoBancarioAzienda rapportoBancarioAziendaPrec = panjeaDAO.load(RapportoBancarioAzienda.class,
                        rapportoBancarioAzienda.getId());
                rapportoBancarioAziendaPrec.getSediEntita().size();
                Set<SedeEntita> sediCancellate = rapportoBancarioAziendaPrec.getSediEntita();
                sediCancellate.removeAll(rapportoBancarioAzienda.getSediEntita());
                ((Session) panjeaDAO.getEntityManager().getDelegate()).evict(rapportoBancarioAziendaPrec);

                for (SedeEntita sedeEntita : sediCancellate) {
                    SedeEntita sede = panjeaDAO.loadLazy(SedeEntita.class, sedeEntita.getId());
                    sede.setRapportoBancarioAzienda(null);
                    panjeaDAO.save(sede);
                }
            }

            for (SedeEntita sedeEntita : rapportoBancarioAzienda.getSediEntita()) {
                ((Session) panjeaDAO.getEntityManager().getDelegate()).refresh(sedeEntita);
                sedeEntita.setRapportoBancarioAzienda(rapportoBancarioAzienda);
            }

            RapportoBancarioAzienda rapportoBancarioAziendaSave = panjeaDAO.save(rapportoBancarioAzienda);
            // inizializzazione degli attributi lazy
            if (rapportoBancarioAziendaSave.getRapportoBancarioRegolamentazione() != null) {
                rapportoBancarioAziendaSave.getRapportoBancarioRegolamentazione().getId();
            }
            logger.debug("--> Exit salvaRapportoBancario");
            return rapportoBancarioAziendaSave;
        } catch (DAOException e) {
            logger.error("--> Errore nel salvare il rapporto bancario per l'azienda corrente ", e);
            throw new RuntimeException(e);
        }
    }
}
