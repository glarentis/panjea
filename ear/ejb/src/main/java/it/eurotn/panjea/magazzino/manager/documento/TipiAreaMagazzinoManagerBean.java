package it.eurotn.panjea.magazzino.manager.documento;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.magazzino.domain.TipoDocumentoBaseMagazzino;
import it.eurotn.panjea.magazzino.domain.TipoDocumentoBaseMagazzino.TipoOperazioneTipoDocumento;
import it.eurotn.panjea.magazzino.domain.documento.DatoAccompagnatorioMagazzinoMetaData;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.TipoMovimento;
import it.eurotn.panjea.magazzino.manager.documento.interfaces.TipiAreaMagazzinoManager;
import it.eurotn.panjea.magazzino.service.exception.MagazzinoException;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.stampe.manager.interfaces.LayoutStampeManager;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.TipiAreaMagazzinoManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.TipiAreaMagazzinoManager")
public class TipiAreaMagazzinoManagerBean implements TipiAreaMagazzinoManager {

    private static Logger logger = Logger.getLogger(TipiAreaMagazzinoManagerBean.class);

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource
    private SessionContext context;

    @EJB
    private LayoutStampeManager layoutStampeManager;

    @Override
    public void cancellaTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino) {
        logger.debug("--> Enter cancellaTipoAreaMagazzino con id " + tipoAreaMagazzino.getId());
        try {
            layoutStampeManager.cancellaLayoutStampa(tipoAreaMagazzino);
            panjeaDAO.delete(tipoAreaMagazzino);
        } catch (Exception e) {
            logger.error("--> errore nella cancellazione ", e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit cancellaTipoAreaMagazzino");
    }

    @Override
    public void cancellaTipoDocumentoBase(TipoDocumentoBaseMagazzino tipoDocumentoBase) {
        logger.debug("--> Enter cancellaTipoDocumentoBase");

        try {
            panjeaDAO.delete(tipoDocumentoBase);
        } catch (Exception e) {
            logger.error("--> errore nella cancellazione del tipo documento base", e);
            throw new RuntimeException(e);
        }

        logger.debug("--> Exit cancellaTipoDocumentoBase");
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TipoAreaMagazzino> caricaTipiAreaMagazzino(String fieldSearch, String valueSearch,
            boolean loadTipiDocumentoDisabilitati) {
        logger.debug("--> Enter caricaTipiAreaMagazzino");
        StringBuilder sb = new StringBuilder();
        sb.append("select tam.id as id , ");
        sb.append("tam.version as version, ");
        sb.append("tam.tipoDocumento.id as tipoDocumento$id, ");
        sb.append("tam.tipoDocumento.version as tipoDocumento$version, ");
        sb.append("tam.tipoDocumento.codice as tipoDocumento$codice, ");
        sb.append("tam.tipoDocumento.descrizione as tipoDocumento$descrizione, ");
        sb.append("tam.tipoDocumento.abilitato as tipoDocumento$abilitato, ");
        sb.append("tam.tipoDocumento.classeTipoDocumento as tipoDocumento$classeTipoDocumento, ");
        sb.append("tam.tipoDocumento.tipoEntita as tipoDocumento$tipoEntita , ");
        sb.append("tam.tipoMovimento as tipoMovimento, ");
        sb.append("tam.gestioneVending  as gestioneVending ");
        sb.append("from TipoAreaMagazzino tam ");
        sb.append("where tam.tipoDocumento.codiceAzienda = :paramCodiceAzienda ");
        if (valueSearch != null) {
            sb.append(" and tam.").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
        }
        if (!loadTipiDocumentoDisabilitati) {
            sb.append("and tam.tipoDocumento.abilitato = true ");
        }

        sb.append("order by tam.").append(fieldSearch);
        Query query = panjeaDAO.prepareQuery(sb.toString(), TipoAreaMagazzino.class, null);
        query.setParameter("paramCodiceAzienda", getAzienda());
        List<TipoAreaMagazzino> tipiAreaMagazzino = new ArrayList<TipoAreaMagazzino>();
        try {
            tipiAreaMagazzino = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> errore impossibile recuperare la list di TipoAreaMagazzino ", e);
            throw new MagazzinoException(e);
        }
        logger.debug("--> Exit caricaTipiAreaMagazzino");
        return tipiAreaMagazzino;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TipoDocumento> caricaTipiDocumentiMagazzino() {
        logger.debug("--> Enter caricaTipiDocumentiMagazzino");
        Query query = panjeaDAO.prepareNamedQuery("TipoAreaMagazzino.caricaTipiDocumentiMagazzino");
        query.setParameter("paramCodiceAzienda", getAzienda());
        List<TipoDocumento> tipiDocumento = null;
        try {
            tipiDocumento = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> errore in ricerca tipi documenti contabili", e);
            throw new MagazzinoException(e);
        }
        logger.debug("--> Exit caricaTipiDocumentiMagazzino con n° documenti" + tipiDocumento.size());
        return tipiDocumento;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TipoDocumento> caricaTipiDocumentoAnagraficaPerFatturazione() {
        logger.debug("--> Enter caricaTipiDocumentoAnagraficaPerFatturazione");

        Query query = panjeaDAO.prepareNamedQuery("TipoAreaMagazzino.caricaByAnagraficaFatturazione");
        query.setParameter("paramCodiceAzienda", getAzienda());
        query.setParameter("paramTipoMovimento", TipoMovimento.NESSUNO);
        List<TipoDocumento> tipiDocumento = null;
        try {
            tipiDocumento = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> errore in ricerca tipi documenti per la fatturazione", e);
            throw new MagazzinoException(e);
        }

        logger.debug("--> Exit caricaTipiDocumentoAnagraficaPerFatturazione");
        return tipiDocumento;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<TipoDocumentoBaseMagazzino> caricaTipiDocumentoBase() {
        logger.debug("--> Enter caricaTipiDocumentoBase");
        Query query = panjeaDAO.prepareNamedQuery("TipoDocumentoBaseMagazzino.caricaByAzienda");
        query.setParameter("paramCodiceAzienda", getAzienda());
        List<TipoDocumentoBaseMagazzino> tipi;
        try {
            tipi = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit caricaTipiDocumentoBase");
        return tipi;
    }

    @Override
    public TipoAreaMagazzino caricaTipoAreaMagazzino(Integer id) {
        logger.debug("--> Enter caricaTipoAreaMagazzino con id " + id);
        try {
            TipoAreaMagazzino tipoAreaMagazzino = panjeaDAO.load(TipoAreaMagazzino.class, id);
            logger.debug("--> Exit caricaTipoAreaContabile");
            return tipoAreaMagazzino;
        } catch (ObjectNotFoundException e) {
            logger.error("--> errore caricaTipoAreaMagazzino", e);
            throw new MagazzinoException(e);
        }
    }

    @Override
    public TipoAreaMagazzino caricaTipoAreaMagazzinoInventario() {
        Query query = panjeaDAO.prepareQuery("select tam from TipoAreaMagazzino tam where tam.tipoMovimento = 4");
        query.setMaxResults(1);
        TipoAreaMagazzino tipoAreaInventario = null;
        try {
            tipoAreaInventario = (TipoAreaMagazzino) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            // ritorno null
            logger.info("Carico tipoareainventario a null");
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento del tipo documento per l'inventario.", e);
            throw new RuntimeException("errore durante il caricamento del tipo documento per l'inventario.", e);
        }

        return tipoAreaInventario;
    }

    @Override
    public TipoAreaMagazzino caricaTipoAreaMagazzinoPerTipoDocumento(Integer idTipoDocumento) {
        Query query = panjeaDAO.prepareNamedQuery("TipoAreaMagazzino.caricaByTipoDocumento");
        query.setParameter("paramId", idTipoDocumento);
        TipoAreaMagazzino tipoAreaMagazzinoResult = null;
        try {
            tipoAreaMagazzinoResult = (TipoAreaMagazzino) panjeaDAO.getSingleResult(query);
            tipoAreaMagazzinoResult.getDatiAccompagnatoriMetaData().size();
        } catch (ObjectNotFoundException e) {
            logger.debug("--> TipoAreaMagazzino non trovato");
            tipoAreaMagazzinoResult = new TipoAreaMagazzino();
            tipoAreaMagazzinoResult.setDatiAccompagnatoriMetaData(new TreeSet<DatoAccompagnatorioMagazzinoMetaData>());
            logger.debug("--> Restituisco tipoAreaMagazzino " + tipoAreaMagazzinoResult);
        } catch (DAOException e) {
            logger.error("--> errore DAOException ", e);
            throw new MagazzinoException("Impossibile restituire TipoAreaMagazzino identificata da " + idTipoDocumento,
                    e);
        }
        return tipoAreaMagazzinoResult;
    }

    @Override
    public TipoDocumentoBaseMagazzino caricaTipoDocumentoBase(TipoOperazioneTipoDocumento tipoOperazione)
            throws ObjectNotFoundException {
        logger.debug("--> Enter caricaTipoDocumentoBase");
        Query query = panjeaDAO.prepareNamedQuery("TipoDocumentoBaseMagazzino.caricaByTipoOperazione");
        query.setParameter("paramCodiceAzienda", getAzienda());
        query.setParameter("paramTipoOperazione", tipoOperazione);
        TipoDocumentoBaseMagazzino tipoDocumentoBase;
        try {
            tipoDocumentoBase = (TipoDocumentoBaseMagazzino) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e1) {
            throw e1;
        } catch (Exception e) {
            logger.error("--> errore nel caricamento dei TipoDocumentoBaseMagazzino per il tipoOperazione "
                    + tipoOperazione.name(), e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit caricaTipoDocumentoBase");
        return tipoDocumentoBase;
    }

    /**
     * @return codice azienda loggata
     */
    private String getAzienda() {
        JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
        return jecPrincipal.getCodiceAzienda();
    }

    @Override
    public TipoAreaMagazzino salvaTipoAreaMagazzino(TipoAreaMagazzino tipoAreaMagazzino) {
        logger.debug("--> Enter salvaTipoAreaMagazzino");
        TipoAreaMagazzino tipoAreaMagazzinoResult = null;
        try {
            tipoAreaMagazzinoResult = panjeaDAO.save(tipoAreaMagazzino);
        } catch (Exception e) {
            logger.error("--> errore nel salvare il tipoAreaMagazzino", e);
            throw new MagazzinoException("errore nel salvare il tipoAreaMagazzino", e);
        }
        logger.debug("--> Exit salvaTipoAreaMagazzino");
        return tipoAreaMagazzinoResult;
    }

    @Override
    public TipoDocumentoBaseMagazzino salvaTipoDocumentoBase(TipoDocumentoBaseMagazzino tipoDocumentoBase) {
        logger.debug("--> Enter salvaTipoDocumentoBase");
        TipoDocumentoBaseMagazzino tipoDocumentoBaseSalvato;
        /* Inizializza il codice azienda in caso di nuovo TipoDocumentoBase */
        if (tipoDocumentoBase.isNew()) {
            tipoDocumentoBase.setCodiceAzienda(getAzienda());
        }
        try {
            tipoDocumentoBaseSalvato = panjeaDAO.save(tipoDocumentoBase);
        } catch (Exception e) {
            logger.error("--> errore nel salvataggio del tipo documento base", e);
            throw new RuntimeException(e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("--> Exit salvaTipoDocumentoBase " + tipoDocumentoBaseSalvato.getId());
        }
        return tipoDocumentoBaseSalvato;
    }

}
