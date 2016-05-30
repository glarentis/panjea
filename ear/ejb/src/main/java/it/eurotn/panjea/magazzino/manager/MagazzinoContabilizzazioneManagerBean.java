package it.eurotn.panjea.magazzino.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.TransactionTimeout;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.Documento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.SediEntitaManager;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.AreaContabile.StatoAreaContabile;
import it.eurotn.panjea.contabilita.domain.ContabilitaSettings;
import it.eurotn.panjea.contabilita.domain.ContiBase;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.ETipoContoBase;
import it.eurotn.panjea.contabilita.domain.ETipologiaContoControPartita;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.SottoConto;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileCancellaManager;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.ContabilitaSettingsManager;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.manager.interfaces.StrutturaContabileManager;
import it.eurotn.panjea.contabilita.service.exception.AreaContabileDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.AreeContabiliDuplicateException;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiBaseException;
import it.eurotn.panjea.contabilita.service.exception.ContiEntitaAssentiException;
import it.eurotn.panjea.contabilita.util.ContabilizzazioneStateDescriptor;
import it.eurotn.panjea.iva.domain.AreaIva;
import it.eurotn.panjea.iva.manager.interfaces.AreaIvaManager;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.CategoriaContabileArticolo;
import it.eurotn.panjea.magazzino.domain.CategoriaContabileDeposito;
import it.eurotn.panjea.magazzino.domain.CategoriaContabileSedeMagazzino;
import it.eurotn.panjea.magazzino.domain.DatiGenerazione.TipoGenerazione;
import it.eurotn.panjea.magazzino.domain.SedeMagazzino;
import it.eurotn.panjea.magazzino.domain.SottoContiContabilizzazione;
import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione;
import it.eurotn.panjea.magazzino.domain.SottoContoContabilizzazione.ETipoEconomico;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoContabilizzazioneManager;
import it.eurotn.panjea.magazzino.manager.interfaces.SediMagazzinoManager;
import it.eurotn.panjea.magazzino.service.exception.ContabilizzazioneException;
import it.eurotn.panjea.magazzino.service.exception.SottoContiContabiliAssentiException;
import it.eurotn.panjea.magazzino.service.exception.SottoContoContabileAssenteException;
import it.eurotn.panjea.magazzino.util.AreaMagazzinoRicerca;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.service.interfaces.PanjeaMessage;
import it.eurotn.security.JecPrincipal;
import it.eurotn.util.PanjeaEJBUtil;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(mappedName = "Panjea.MagazzinoContabilizzazioneManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
public class MagazzinoContabilizzazioneManagerBean implements MagazzinoContabilizzazioneManager {

    private static Logger logger = Logger.getLogger(MagazzinoContabilizzazioneManagerBean.class);

    public static final String BEAN_ID = "";

    public static final String CONTABILIZZAZIONE_MESSAGE_SELECTOR = "contabilizzazioneMessageSelector";

    @Resource
    private SessionContext sessionContext;

    /**
     * @uml.property name="panjeaDAO"
     * @uml.associationEnd
     */
    @EJB
    private PanjeaDAO panjeaDAO;

    /**
     * @uml.property name="areaContabileManager"
     * @uml.associationEnd
     */
    @EJB
    private AreaContabileManager areaContabileManager;

    @EJB
    private AreaContabileCancellaManager areaContabileCancellaManager;

    /**
     * @uml.property name="strutturaContabileManager"
     * @uml.associationEnd
     */
    @EJB
    private StrutturaContabileManager strutturaContabileManager;

    /**
     * @uml.property name="areaRateManager"
     * @uml.associationEnd
     */
    @EJB
    private AreaRateManager areaRateManager;

    /**
     * @uml.property name="sediMagazzinoManager"
     * @uml.associationEnd
     */
    @EJB
    private SediMagazzinoManager sediMagazzinoManager;

    /**
     * @uml.property name="pianoContiManager"
     * @uml.associationEnd
     */
    @EJB
    private PianoContiManager pianoContiManager;

    /**
     * @uml.property name="contabilitaSettingsManager"
     * @uml.associationEnd
     */
    @EJB
    private ContabilitaSettingsManager contabilitaSettingsManager;

    /**
     * @uml.property name="areaIvaManager"
     * @uml.associationEnd
     */
    @EJB
    private AreaIvaManager areaIvaManager;

    @EJB
    private SediEntitaManager sediEntitaManager;

    @EJB
    private PanjeaMessage panjeaMessage;

    @EJB
    private DepositiManager depositiManager;

    @Override
    public void cancellaCategoriaContabileArticolo(CategoriaContabileArticolo categoriaContabileArticolo) {
        logger.debug("--> Enter cancellaCategoriaContabileArticolo");
        try {
            panjeaDAO.delete(categoriaContabileArticolo);
        } catch (Exception e) {
            logger.error("--> errore nel cancellare la categoriacontabileArticolo " + categoriaContabileArticolo, e);
            throw new RuntimeException(
                    "Errore nel cancellare la categoriaContabileArticolo " + categoriaContabileArticolo, e);
        }
        logger.debug("--> Exit cancellaCategoriaContabileArticolo");
    }

    @Override
    public void cancellaCategoriaContabileDeposito(CategoriaContabileDeposito categoriaContabileDeposito) {
        logger.debug("--> Enter cancellaCategoriaContabileDeposito");
        try {
            panjeaDAO.delete(categoriaContabileDeposito);
        } catch (Exception e) {
            logger.error("--> errore nel cancellare la categoria contabile deposito " + categoriaContabileDeposito, e);
            throw new RuntimeException(
                    "Errore nel cancellare la categoriaContabileDeposito" + categoriaContabileDeposito, e);
        }
        logger.debug("--> Exit cancellaCategoriaContabileDeposito");
    }

    @Override
    public void cancellaCategoriaContabileSedeMagazzino(
            CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino) {
        logger.debug("--> Enter cancellaCategoriaContabileSedeMagazzino");
        try {
            panjeaDAO.delete(categoriaContabileSedeMagazzino);
        } catch (Exception e) {
            logger.error(
                    "--> errore nel cancellare la CategoriaContabileSedeMagazzino " + categoriaContabileSedeMagazzino,
                    e);
            throw new RuntimeException(
                    "Errore nel cancellare la CategoriaContabileSedeMagazzino" + categoriaContabileSedeMagazzino, e);
        }
        logger.debug("--> Enter cancellaCategoriaContabileSedeMagazzino");
    }

    @Override
    public void cancellaSottoContoContabilizzazione(Integer id) {
        logger.debug("--> Enter cancellaSottoContoContabilizzazione");

        SottoContoContabilizzazione entity = new SottoContoContabilizzazione();
        try {
            entity.setId(id);
            panjeaDAO.delete(entity);
        } catch (Exception e) {
            logger.error("--> Errore durante la cancellazione di  SottoContoContabilizzazione" + entity.getId(), e);
            throw new RuntimeException(
                    "Errore durante la cancellazione di  SottoContoContabilizzazione" + entity.getId(), e);
        }
        logger.debug("--> Exit cancellaSottoContoContabilizzazione");
    }

    /**
     * Carica solo i campi che servono di tutte le aree magazzino da contabilizzare.
     *
     * @param uuid
     *            UUID di contabilizzaqzione
     *
     * @return Aree magazzino caricate.
     */
    @SuppressWarnings("unchecked")
    private List<AreaMagazzinoContabilizzazione> caricaAreeMagazzinoContabilizzazione(String uuid) {

        List<AreaMagazzinoContabilizzazione> listAree = new ArrayList<AreaMagazzinoContabilizzazione>();

        StringBuilder sb = new StringBuilder();
        sb.append("select new it.eurotn.panjea.magazzino.manager.AreaMagazzinoContabilizzazione(");
        sb.append("am.id,");
        sb.append("tac.id,");
        sb.append("am.documento.id,");
        sb.append("am.documento.dataDocumento,");
        sb.append("am.documento.tipoDocumento.tipoEntita,");
        sb.append("am.depositoOrigine.id,");
        sb.append("am.documento.sedeEntita.id,");
        sb.append("am.documento.tipoDocumento.notaCreditoEnable, ");
        sb.append("am.tipoAreaMagazzino.generaAreaContabile ");
        sb.append(") ");
        sb.append("from AreaMagazzino am, TipoAreaContabile tac where am.uUIDContabilizzazione = :UUID ");
        sb.append("and  am.documento.tipoDocumento = tac.tipoDocumento ");
        sb.append("and am.statoAreaMagazzino not in (1,3)");

        Query query = panjeaDAO.getEntityManager().createQuery(sb.toString());
        query.setParameter("UUID", uuid);

        try {
            listAree = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("-->errore durante il caricamento della aree magazzino da contabilizzare.", e);
            throw new RuntimeException("errore durante il caricamento della aree magazzino da contabilizzare.", e);
        }

        return listAree;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<AreaMagazzinoRicerca> caricaAreeMAgazzinoDaContabilizzare(TipoGenerazione tipoGenerazione, int anno) {
        logger.debug("--> Enter caricaAreeMAgazzinoDaContabilizzare");

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        sb.append("		a.documento.codiceAzienda as azienda, ");
        sb.append("		a.id as idAreaMagazzino, ");
        sb.append("		a.statoAreaMagazzino as stato, ");
        sb.append("		a.tipoAreaMagazzino.tipoMovimento as tipoMovimento, ");
        sb.append("     a.tipoAreaMagazzino.id as idTipoAreaMagazzino, ");
        sb.append("		a.documento.id as idDocumento, ");
        sb.append("		a.documento.tipoDocumento.id as idTipoDocumento, ");
        sb.append("		a.documento.codice as codiceDocumento, ");
        sb.append("		a.documento.totale.importoInValutaAzienda as totaleDocumentoInValutaAzienda, ");
        sb.append("		a.documento.totale.importoInValuta as totaleDocumentoInValuta, ");
        sb.append("		a.documento.totale.tassoDiCambio as totaleDocumentoTassoDiCambio, ");
        sb.append("		a.documento.tipoDocumento.codice as codiceTipoDocumento, ");
        sb.append("		a.documento.dataDocumento as dataDocumento, ");
        sb.append("		a.dataRegistrazione as dataRegistrazione, ");
        sb.append("		a.depositoOrigine.id as idDepositoOrigine, ");
        sb.append("		a.depositoOrigine.codice as codiceDepositoOrigine, ");
        sb.append("		a.depositoOrigine.descrizione as descrizioneDepositoOrigine, ");
        sb.append("		a.depositoDestinazione.id as idDepositoDestinazione, ");
        sb.append("		a.depositoDestinazione.codice as codiceDepositoDestinazione, ");
        sb.append("		a.depositoDestinazione.descrizione as descrizioneDepositoDestinazione, ");
        sb.append("		a.documento.tipoDocumento.descrizione as descrizioneTipoDocumento, ");
        sb.append("		a.documento.entita.id as idEntita, ");
        sb.append("		a.documento.entita.codice as codiceEntita, ");
        sb.append("		a.documento.entita.anagrafica.denominazione as denominazioneEntita, ");
        sb.append("		a.documento.tipoDocumento.tipoEntita as tipoEntita, ");
        sb.append("		a.areaMagazzinoNote.noteTestata as note, ");
        sb.append("		a.datiGenerazione as datiGenerazione, ");
        sb.append("		a.tipoAreaMagazzino.valoriFatturato as valoriFatturato ");
        sb.append(
                "		 from AreaMagazzino a, TipoAreaContabile tac left join a.depositoDestinazione left join a.documento.entita left join a.documento.entita.anagrafica ");
        sb.append(
                "where a.documento.codiceAzienda = :codiceAzienda and a.tipoAreaMagazzino.tipoDocumento = tac.tipoDocumento and ");
        sb.append(" a.statoAreaMagazzino in (0,2) and ");
        sb.append("a.annoMovimento = :anno and ");
        if (tipoGenerazione != TipoGenerazione.TUTTI) {
            sb.append("a.datiGenerazione.tipoGenerazione = :tipoGenerazione and ");
        }
        sb.append(
                "a not in (select amCont from AreaMagazzino amCont, AreaContabile cont where amCont.documento = cont.documento and amCont.annoMovimento = :anno and amCont.documento.codiceAzienda = :codiceAzienda and amCont.statoAreaMagazzino in (0,2) ");
        if (tipoGenerazione != TipoGenerazione.TUTTI) {
            sb.append("and amCont.datiGenerazione.tipoGenerazione = :tipoGenerazione)");
        }
        sb.append(")");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        ((QueryImpl) query).getHibernateQuery()
                .setResultTransformer(Transformers.aliasToBean((AreaMagazzinoRicerca.class)));

        if (tipoGenerazione != TipoGenerazione.TUTTI) {
            query.setParameter("tipoGenerazione", tipoGenerazione);
        }
        query.setParameter("anno", anno);
        query.setParameter("codiceAzienda", getAzienda());

        List<AreaMagazzinoRicerca> aree = new ArrayList<AreaMagazzinoRicerca>();
        try {
            aree = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("-->errore durante il caricamento delle aree magazzino non contabilizzate.", e);
            throw new RuntimeException("errore durante il caricamento delle aree magazzino non contabilizzate.", e);
        }

        logger.debug("--> Exit caricaAreeMAgazzinoDaContabilizzare");
        return aree;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CategoriaContabileArticolo> caricaCategorieContabileArticolo(String fieldSearch, String valueSearch) {
        logger.debug("--> Enter caricaCategorieContabileArticolo");
        List<CategoriaContabileArticolo> list = new ArrayList<CategoriaContabileArticolo>();

        StringBuilder sb = new StringBuilder(
                "select cca from CategoriaContabileArticolo cca where cca.codiceAzienda=:paramCodiceAzienda ");
        if (valueSearch != null) {
            sb.append(" and cca.").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
        }
        sb.append(" order by ");
        sb.append(fieldSearch);

        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("paramCodiceAzienda", getAzienda());
        try {
            list = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> Errore durante il caricamento delle categorieContabileArticolo", e);
            throw new RuntimeException("Errore durante il caricamento delle categorieContabileArticolo", e);
        }
        logger.debug("--> Exit caricaCategorieContabileArticolo");
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CategoriaContabileDeposito> caricaCategorieContabileDeposito(String fieldSearch, String valueSearch) {
        logger.debug("--> Enter caricaCategorieContabileDeposito");
        List<CategoriaContabileDeposito> list = new ArrayList<CategoriaContabileDeposito>();

        StringBuilder sb = new StringBuilder(
                "select ccd from CategoriaContabileDeposito ccd where ccd.codiceAzienda=:paramCodiceAzienda ");
        if (valueSearch != null) {
            sb.append(" and ccd.").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
        }
        sb.append(" order by ");
        sb.append(fieldSearch);

        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("paramCodiceAzienda", getAzienda());
        try {
            list = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> Errore durante il caricamento delle categoriaContabileDeposito", e);
            throw new RuntimeException("Errore durante il caricamento delle CategoriaContabileDeposito", e);
        }
        logger.debug("--> Exit caricaCategorieContabileDeposito");
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CategoriaContabileSedeMagazzino> caricaCategorieContabileSedeMagazzino(String fieldSearch,
            String valueSearch) {
        logger.debug("--> Enter caricaCategorieContabileSedeMagazzino");
        List<CategoriaContabileSedeMagazzino> list = new ArrayList<CategoriaContabileSedeMagazzino>();

        StringBuilder sb = new StringBuilder(
                "select ccsm from CategoriaContabileSedeMagazzino ccsm where ccsm.codiceAzienda=:paramCodiceAzienda ");
        if (valueSearch != null) {
            sb.append(" and ccsm.").append(fieldSearch).append(" like ").append(PanjeaEJBUtil.addQuote(valueSearch));
        }
        sb.append(" order by ");
        sb.append(fieldSearch);

        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("paramCodiceAzienda", getAzienda());
        try {
            list = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> Errore durante il caricamento delle CategoriaContabileSedeMagazzino", e);
            throw new RuntimeException("Errore durante il caricamento delle CategoriaContabileSedeMagazzino", e);
        }
        logger.debug("--> Exit caricaCategorieContabileSedeMagazzino");
        return list;
    }

    /**
     * Carica una mappa che contiene come chiave l'id del deposito azienda e come valore la categoria contabile
     * associata al deposito.
     *
     * @return mappa dei valori
     */
    @SuppressWarnings("unchecked")
    private Map<Integer, CategoriaContabileDeposito> caricaCategorieContabiliDeiDepositi() {
        logger.debug("--> Enter caricaCategorieContabiliDeiDepositi");

        List<Map<String, Object>> categorie;

        StringBuilder sb = new StringBuilder();
        sb.append("select new map(dm.deposito.id as idDep,dm.categoriaContabileDeposito as cat) ");
        sb.append("from DepositoMagazzino dm inner join dm.deposito d ");
        sb.append("where d.codiceAzienda = :paramCodiceAzienda");

        Query query = panjeaDAO.getEntityManager().createQuery(sb.toString());
        query.setParameter("paramCodiceAzienda", getAzienda());

        try {
            categorie = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("-->errore durante il caricamento delle categorie contabili dei depositi", e);
            throw new RuntimeException("errore durante il caricamento delle categorie contabili dei depositi", e);
        }

        Map<Integer, CategoriaContabileDeposito> map = new HashMap<Integer, CategoriaContabileDeposito>();

        for (Map<String, Object> mapCat : categorie) {
            map.put((Integer) mapCat.get("idDep"), (CategoriaContabileDeposito) mapCat.get("cat"));
        }

        logger.debug("--> Exit caricaCategorieContabiliDeiDepositi");
        return map;
    }

    /**
     * Carica tutti gli id delle aree magazzino che hanno già un'area contabile associata al documento.
     *
     * @param uuidContabilizzazione
     *            UUID di riferimento
     * @return lista di ID
     */
    @SuppressWarnings("unchecked")
    private List<Integer> caricaIdAreeMagazzinoConAreaContabile(String uuidContabilizzazione) {

        List<Integer> idAree = new ArrayList<Integer>();

        Query query = panjeaDAO.getEntityManager().createQuery(
                "select new list(am.id as idArea) from AreaMagazzino am, AreaContabile ac where am.uUIDContabilizzazione = :UUID and ac.documento = am.documento");
        query.setParameter("UUID", uuidContabilizzazione);

        try {
            idAree = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("-->errore nel caricare le aree magazzino che hanno già una area contabile.", e);
            throw new RuntimeException("errore nel caricare le aree magazzino che hanno già una area contabile.", e);
        }

        return idAree;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SottoContoContabilizzazione> caricaSottoContiContabilizzazione(ETipoEconomico tipoEconomico) {
        logger.debug("--> Enter caricaSottoContiContabilizzazione");
        List<SottoContoContabilizzazione> list = new ArrayList<SottoContoContabilizzazione>();
        Query query = panjeaDAO.prepareNamedQuery("SottoContoContabilizzazione.caricaByTipoEconomico");
        query.setParameter("paramCodiceAzienda", getAzienda());
        query.setParameter("paramTipoEconomico", tipoEconomico);
        try {
            list = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            logger.error("--> Errore durante il caricamento di SottoContoContabilizzazione", e);
            throw new RuntimeException("Errore durante il caricamento di SottoContoContabilizzazione", e);
        }
        logger.debug("--> Exit caricaSottoContiContabilizzazione");
        return list;
    }

    @Override
    public SottoContoContabilizzazione caricaSottoContoContabilizzazione(Integer id) {
        logger.debug("--> Enter caricaSottoContoContabilizzazione");
        SottoContoContabilizzazione entityResult = null;
        try {
            entityResult = panjeaDAO.load(SottoContoContabilizzazione.class, id);
        } catch (ObjectNotFoundException e) {
            logger.error("--> SottoContoContabilizzazione non trovato. Id: " + id, e);
            throw new RuntimeException(e);
        }
        logger.debug("--> Exit caricaSottoContoContabilizzazione");
        return entityResult;
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @TransactionTimeout(value = 7200)
    @Override
    public void contabilizzaAreeMagazzino(List<Integer> idAreeMagazzino, boolean forzaGenerazioneAreaContabile)
            throws ContabilizzazioneException {

        ContabilitaSettings contabilitaSettings = contabilitaSettingsManager.caricaContabilitaSettings();

        contabilizzaAreeMagazzino(idAreeMagazzino, forzaGenerazioneAreaContabile,
                contabilitaSettings.getAnnoCompetenza());

    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @TransactionTimeout(value = 7200)
    @Override
    public void contabilizzaAreeMagazzino(List<Integer> idAreeMagazzino, boolean forzaGenerazioneAreaContabile,
            int annoContabile) throws ContabilizzazioneException {
        logger.debug("--> Enter contabilizzaAreeMagazzino");

        // se non ho aree da contabilizzare esco
        if (idAreeMagazzino.isEmpty()) {
            return;
        }

        // genero l'UUID per la contabilizzazione
        Random random = new Random();
        long r1 = random.nextLong();
        long r2 = random.nextLong();
        String uuid = Long.toHexString(r1) + Long.toHexString(r2);

        Query query = panjeaDAO.prepareQuery(
                "update AreaMagazzino am set am.uUIDContabilizzazione = :uuid where am.id in (:idAreeMagazzino)");
        query.setParameter("uuid", uuid);
        query.setParameter("idAreeMagazzino", idAreeMagazzino);

        try {
            panjeaDAO.executeQuery(query);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        contabilizzaAreeMagazzino(uuid, CONTABILIZZAZIONE_MESSAGE_SELECTOR, forzaGenerazioneAreaContabile,
                annoContabile);

        logger.debug("--> Exit contabilizzaAreeMagazzino");
    }

    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @TransactionTimeout(value = 7200)
    @Override
    public void contabilizzaAreeMagazzino(String uuid, String messageSelector, boolean forzaGenerazioneAreaContabile)
            throws ContabilizzazioneException {

        // carico le settings della contabilità
        ContabilitaSettings contabilitaSettings = contabilitaSettingsManager.caricaContabilitaSettings();

        contabilizzaAreeMagazzino(uuid, messageSelector, forzaGenerazioneAreaContabile,
                contabilitaSettings.getAnnoCompetenza());
    }

    @Override
    public void contabilizzaAreeMagazzino(String uuid, String messageSelector, boolean forzaGenerazioneAreaContabile,
            int annoContabile) throws ContabilizzazioneException {
        logger.debug("--> Enter contabilizzaAreeMagazzino");

        Calendar calendar = Calendar.getInstance();
        long timeStart = calendar.getTimeInMillis();

        logger.debug("INIZIO contabilizzazione." + timeStart);

        // carico le aree da contabilizzare
        List<AreaMagazzinoContabilizzazione> areeDaContabilizzare = caricaAreeMagazzinoContabilizzazione(uuid);

        // se non ho aree da contabilizzare esco
        if (areeDaContabilizzare.isEmpty()) {
            return;
        }

        SottoConto sottoContoSpeseIncasso = null;
        try {
            // carico i conti base
            ContiBase contiBase = pianoContiManager.caricaTipiContoBase();
            if (!contiBase.containsKey(ETipoContoBase.SPESE_INCASSO)) {
                throw new RuntimeException(new ContiBaseException(ETipoContoBase.SPESE_INCASSO));
            }
            sottoContoSpeseIncasso = contiBase.get(ETipoContoBase.SPESE_INCASSO);
        } catch (ContabilitaException e1) {
            logger.error("-->errore durante il caricamento dei tipi conto base", e1);
            throw new RuntimeException("errore durante il caricamento dei tipi conto base", e1);
        }

        // carico i sottoconticontabilizzazione e li unisco in una mappa
        // distinti per tipo entità
        List<SottoContoContabilizzazione> sottoContiRicavi = caricaSottoContiContabilizzazione(ETipoEconomico.RICAVO);
        List<SottoContoContabilizzazione> sottoContiCosti = caricaSottoContiContabilizzazione(ETipoEconomico.COSTO);
        Map<TipoEntita, List<SottoContoContabilizzazione>> mapSottoConti = new HashMap<TipoDocumento.TipoEntita, List<SottoContoContabilizzazione>>();
        mapSottoConti.put(TipoEntita.FORNITORE, sottoContiCosti);
        mapSottoConti.put(TipoEntita.CLIENTE, sottoContiRicavi);

        // carico le categorie contabili dei depositi
        Map<Integer, CategoriaContabileDeposito> categorieContabiliDepositi = caricaCategorieContabiliDeiDepositi();

        // carico tutte le eventuali aree contabili già presenti per le aree da
        // contabilizzare
        List<Integer> idAreeConAreaContabile = caricaIdAreeMagazzinoConAreaContabile(uuid);

        // Creo l'oggetto per recuperare i sottoContiContabilizzazione. Prendo
        // la prima area perchè non potrò mai ( per
        // il momento ) contabilizzare sia documenti attivi che passivi quindi
        // il tipo entità non cambia.
        SottoContiContabilizzazione sottoContiContabilizzazione = new SottoContiContabilizzazione(
                mapSottoConti.get(areeDaContabilizzare.get(0).getTipoEntita()));

        if (logger.isDebugEnabled()) {
            logger.debug("--> FINE CARICAMENTO. " + ((Calendar.getInstance().getTimeInMillis() - timeStart) / 1000)
                    + " secondi. ---------------------------------------------------");
        }

        ContabilizzazioneException contabilizzazioneException = new ContabilizzazioneException();

        int totaleDocumenti = areeDaContabilizzare.size();
        int numDoc = 1;
        // lo step di pubblicazione sarà pari al 5% dei documenti da
        // contabilizzare in modo tale da evitare la pubblicazione sulla coda di
        // ogni singolo documento in caso di un numero elevato.
        int stepCoda = totaleDocumenti / 100 * 5;
        if (stepCoda < 1) {
            stepCoda = 1;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("--> Totale documenti: " + totaleDocumenti + "   ----  stepCoda: " + stepCoda);
        }

        for (AreaMagazzinoContabilizzazione areaMagazzinoContabilizzazione : areeDaContabilizzare) {

            long docTimeStart = Calendar.getInstance().getTimeInMillis();

            ContabilizzazioneException contabilizzazioneDocumentoException = new ContabilizzazioneException();
            SottoContiContabiliAssentiException sottoContiContabiliAssentiException = null;
            AreeContabiliDuplicateException areeContabiliDuplicateException = null;
            ContiEntitaAssentiException contiEntitaAssentiException = null;

            boolean generaAreaContabile = forzaGenerazioneAreaContabile
                    || areaMagazzinoContabilizzazione.isGeneraAreaContabile();
            // Controllo se esiste già un'area contabile e se la devo creare. In
            // tal caso salto la
            // contabilizzazione dell'area
            if (!generaAreaContabile
                    || idAreeConAreaContabile.contains(areaMagazzinoContabilizzazione.getIdAreaMagazzino())) {

                if (messageSelector != null && ((numDoc % stepCoda) == 0)) {
                    ContabilizzazioneStateDescriptor descriptor = new ContabilizzazioneStateDescriptor(totaleDocumenti,
                            numDoc);
                    panjeaMessage.send(descriptor, messageSelector);
                }

                numDoc++;

                continue;
            }

            TipoAreaContabile tipoAreaContabile = null;
            Documento documento = null;
            try {
                tipoAreaContabile = panjeaDAO.load(TipoAreaContabile.class,
                        areaMagazzinoContabilizzazione.getIdTipoAreaContabile());

            } catch (Exception e) {
                // non rilancio eccezioni unchecked altrimenti la tarnsazxione va in rollback e non riuscirei a
                // contabilizzare gli altri documenti
                logger.error("-->errore nel caricare il tipo area contabile con id  "
                        + areaMagazzinoContabilizzazione.getIdTipoAreaContabile(), e);
            }
            try {
                documento = panjeaDAO.load(Documento.class, areaMagazzinoContabilizzazione.getIdDocumento());
            } catch (Exception e2) {
                logger.error(
                        "-->errore nel caricare il documento id  " + areaMagazzinoContabilizzazione.getIdDocumento(),
                        e2);
            }

            AreaContabile areaContabile = new AreaContabile();
            areaContabile.setTipoAreaContabile(tipoAreaContabile);

            areaContabile.setDocumento(documento);

            areaContabile.setAnnoIva(annoContabile);
            areaContabile.setAnnoMovimento(annoContabile);
            areaContabile.setDataRegistrazione(areaMagazzinoContabilizzazione.getDataDocumento());
            try {
                areaContabile = areaContabileManager.salvaAreaContabile(areaContabile, false);
            } catch (ContabilitaException e) {
                logger.error("--> errore nel salvare l' areaContabile ", e);
                throw new RuntimeException(e);
            } catch (AreaContabileDuplicateException e) {
                logger.error("--> errore nel salvare l' areaContabile ", e);
                areeContabiliDuplicateException = new AreeContabiliDuplicateException();
                areeContabiliDuplicateException.add(e);

                contabilizzazioneDocumentoException.setAreeContabiliDuplicateException(areeContabiliDuplicateException);
                if (!contabilizzazioneDocumentoException.isEmpty()) {
                    contabilizzazioneException.add(contabilizzazioneDocumentoException);
                }

                try {
                    areaContabileCancellaManager.cancellaAreaContabile(
                            areaContabileManager.caricaAreaContabileByDocumento(documento.getId()), false);
                } catch (Exception e1) {
                    logger.error("-->errore durante la cancellazione dell'area contabile.", e1);
                }

                // salto i passaggi successivi perchè avendo un'area duplicata
                // non devo fare più niente
                continue;
            } catch (Exception e) {
                logger.error("--> errore nel salvare l' areaContabile ", e);
                throw new RuntimeException(e);
            }

            // Carico le righe magazzino raggruppate per sottoConti e importo
            // per creare la lista di contropartite
            Query query = panjeaDAO.prepareNamedQuery("RigaArticolo.caricaImportiByCategoriaContabileArticolo");
            query.setParameter("paramIdAreaMagazzino", areaMagazzinoContabilizzazione.getIdAreaMagazzino());
            @SuppressWarnings("unchecked")
            List<Object[]> importiCategorieContabiliArticolo = query.getResultList();

            // Creo la lista di contropartite la tupla contiene in [0] la categoria Contabile ed in [1] l'importo
            List<ControPartita> controPartite = new ArrayList<ControPartita>();

            // Carico la categoria contabile del deposito dell'area magazzino
            CategoriaContabileDeposito categoriaContabileDeposito = null;
            // Se il deposito origine non è assegnato la categoria contabile la lascio a null
            if (areaMagazzinoContabilizzazione.getIdDepositoOrigine() != null) {

                categoriaContabileDeposito = categorieContabiliDepositi
                        .get(areaMagazzinoContabilizzazione.getIdDepositoOrigine());
            }

            // Carico la categoria contabile della sede dell'area magazzino
            SedeMagazzino sedeMagazzino = null;
            try {
                SedeEntita sedeEntita = new SedeEntita();
                sedeEntita.setId(areaMagazzinoContabilizzazione.getIdSedeEntita());
                sedeMagazzino = sediMagazzinoManager.caricaSedeMagazzinoBySedeEntita(sedeEntita, false);
            } catch (Exception ex) {
                logger.error("--> errore ", ex);
            }
            CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino = sedeMagazzino
                    .getCategoriaContabileSedeMagazzino();

            // Creo le righe di controPartita
            Integer ordine = 1;
            for (Object[] importoCategoriaContabileArticolo : importiCategorieContabiliArticolo) {
                ControPartita controPartita = null;
                try {
                    controPartita = creaControPartita(areaMagazzinoContabilizzazione, importoCategoriaContabileArticolo,
                            sottoContiContabilizzazione, categoriaContabileDeposito, categoriaContabileSedeMagazzino);
                    controPartita.setOrdine(ordine++);
                    controPartite.add(controPartita);
                } catch (SottoContoContabileAssenteException e) {
                    if (sottoContiContabiliAssentiException == null) {
                        sottoContiContabiliAssentiException = new SottoContiContabiliAssentiException();
                    }
                    sottoContiContabiliAssentiException.add(e);
                }
            }

            // Creo la riga di contropartita per le spese di incasso
            AreaRate areaRate = areaRateManager.caricaAreaRate(areaContabile.getDocumento());
            if (areaRate.getId() != null && areaRate.getSpeseIncasso() != null
                    && BigDecimal.ZERO.compareTo(areaRate.getSpeseIncasso()) != 0) {
                // aggiungo le spese incasso come riga di contropartita

                ControPartita controPartitaSpeseIncasso = new ControPartita();

                controPartitaSpeseIncasso.setTipologiaContoControPartita(ETipologiaContoControPartita.SOTTOCONTO);
                controPartitaSpeseIncasso.setImporto(areaRate.getSpeseIncasso());
                controPartitaSpeseIncasso.setOrdine(ordine++);
                if (areaMagazzinoContabilizzazione.getTipoEntita() == TipoEntita.CLIENTE) {
                    // Da rivedere!!!
                    if (documento.getTipoDocumento().isNotaCreditoEnable()) {
                        controPartitaSpeseIncasso.setDare(sottoContoSpeseIncasso);
                    } else {
                        controPartitaSpeseIncasso.setAvere(sottoContoSpeseIncasso);
                    }
                } else {
                    // Da rivedere!!!
                    if (documento.getTipoDocumento().isNotaCreditoEnable()) {
                        controPartitaSpeseIncasso.setAvere(sottoContoSpeseIncasso);
                    } else {
                        controPartitaSpeseIncasso.setDare(sottoContoSpeseIncasso);
                    }
                }
                controPartite.add(controPartitaSpeseIncasso);
            }

            // -------------------------------------------------------------------------------------------------------
            // creo le righe contabili
            try {
                List<RigaContabile> righeContabili = strutturaContabileManager.creaRigheContabili(areaContabile,
                        controPartite);
                if (areaContabile.getTipoAreaContabile()
                        .getStatoAreaContabileGenerata() != StatoAreaContabile.PROVVISORIO) {
                    boolean validaAreaContabile = true;
                    for (RigaContabile rigaContabile : righeContabili) {
                        if (!rigaContabile.isValid()) {
                            validaAreaContabile = false;
                            break;
                        }
                    }
                    if (validaAreaContabile) {
                        areaContabile.setStatoAreaContabile(
                                areaContabile.getTipoAreaContabile().getStatoAreaContabileGenerata());
                        areaContabile.setValidRigheContabili(true);
                        areaContabile.setValidDataRigheContabili(Calendar.getInstance().getTime());
                        areaContabile.setValidUtenteRigheContabili(getPrincipal().getUserName());
                        areaContabile = areaContabileManager.salvaAreaContabile(areaContabile, false);
                        areaRate.getDatiValidazione().valida(getPrincipal().getUserName());
                        areaRate = areaRateManager.salvaAreaRate(areaRate);
                    }
                }
            } catch (ContiEntitaAssentiException e) {
                contiEntitaAssentiException = e;
            } catch (Exception e) {
                logger.error("--> errore generico nel contabilizzare il documento ", e);
                throw new RuntimeException("errore generico nel contabilizzare il documento ", e);
            }

            contabilizzazioneDocumentoException.setContiEntitaAssentiException(contiEntitaAssentiException);
            contabilizzazioneDocumentoException
                    .setSottoContiContabiliAssentiException(sottoContiContabiliAssentiException);

            if (!contabilizzazioneDocumentoException.isEmpty()) {
                contabilizzazioneException.add(contabilizzazioneDocumentoException);
            }

            // l'areaIva creata dal magazzino non ha associati i campi registro
            // iva e registro iva collegato
            AreaIva areaIva = areaIvaManager.caricaAreaIva(areaContabile);
            areaIvaManager.updateRegistroAreaIva(areaIva, areaContabile);

            if (logger.isDebugEnabled()) {
                logger.debug("--> CONTABILIZZAZIONE DOCUMENTO in "
                        + ((Calendar.getInstance().getTimeInMillis() - docTimeStart) / 1000) + " secondi. Documento "
                        + numDoc + " di " + totaleDocumenti);
            }

            if (messageSelector != null && ((numDoc % stepCoda) == 0)) {
                ContabilizzazioneStateDescriptor descriptor = new ContabilizzazioneStateDescriptor(totaleDocumenti,
                        numDoc);
                panjeaMessage.send(descriptor, messageSelector);
            }

            numDoc++;

            // se ho avuto degli errori provvedo a cancellare l'area
            // contabile
            // creata
            if (!contabilizzazioneDocumentoException.isEmpty()) {
                try {
                    areaContabileCancellaManager.cancellaAreaContabile(
                            areaContabileManager.caricaAreaContabileByDocumento(documento.getId()), false);
                } catch (Exception e1) {
                    logger.error("-->errore durante la cancellazione dell'area contabile.", e1);
                }
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("--> CONTABILIZZAZIONE TOTALE in "
                    + ((Calendar.getInstance().getTimeInMillis() - timeStart) / 1000) + " secondi.");
        }

        if (messageSelector != null) {
            ContabilizzazioneStateDescriptor descriptor = new ContabilizzazioneStateDescriptor(totaleDocumenti,
                    totaleDocumenti);
            panjeaMessage.send(descriptor, messageSelector);
        }

        if (!contabilizzazioneException.isEmpty()) {
            sessionContext.setRollbackOnly();
            throw contabilizzazioneException;
        } else {
            // non ho avuto errori in contabilizzazione quindi tolgo tutti gli
            // uuid dalle aree contabilizzate
            Query query = panjeaDAO.getEntityManager().createQuery(
                    "update AreaMagazzino am set am.uUIDContabilizzazione = null where am.uUIDContabilizzazione = :uuid ");
            query.setParameter("uuid", uuid);
            try {
                panjeaDAO.executeQuery(query);
            } catch (DAOException e) {
                throw new RuntimeException(e);
            }
        }

        logger.debug("--> Exit contabilizzaAreeMagazzino");
    }

    /**
     *
     * @param areaMagazzinoContabilizzazione
     *            area magazzino di riferimento
     * @param importoCategoriaContabileArticolo
     *            importo della categoria contabile dell'articolo
     * @param sottoContiContabilizzazione
     *            sotto conto contabilizzazione
     * @param categoriaContabileDeposito
     *            categoria contabile del deposito
     * @param categoriaContabileSedeMagazzino
     *            categoria contabile della sede
     * @return Contro partita
     * @throws SottoContoContabileAssenteException
     *             eccezione sollevata se il sotto conto è assente
     */
    private ControPartita creaControPartita(AreaMagazzinoContabilizzazione areaMagazzinoContabilizzazione,
            Object[] importoCategoriaContabileArticolo, SottoContiContabilizzazione sottoContiContabilizzazione,
            CategoriaContabileDeposito categoriaContabileDeposito,
            CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino)
                    throws SottoContoContabileAssenteException {

        ControPartita controPartita = new ControPartita();

        BigDecimal importo = (BigDecimal) importoCategoriaContabileArticolo[1];
        CategoriaContabileArticolo categoriaContabileArticolo = (CategoriaContabileArticolo) importoCategoriaContabileArticolo[0];

        SottoConto sottoConto = sottoContiContabilizzazione.getSottoConto(categoriaContabileArticolo,
                categoriaContabileDeposito, categoriaContabileSedeMagazzino);
        if (sottoConto == null) {

            // Carico gli articoli della categoria articolo
            StringBuilder sb = new StringBuilder();
            sb.append(
                    "select riga.articolo from RigaArticolo riga where riga.areaMagazzino.id = :paramIdAreaMagazzino ");
            if (categoriaContabileArticolo == null) {
                sb.append(" and riga.categoriaContabileArticolo is null");
            } else {
                sb.append(" and riga.categoriaContabileArticolo.id = " + categoriaContabileArticolo.getId());
            }
            Query query = panjeaDAO.prepareQuery(sb.toString());
            query.setParameter("paramIdAreaMagazzino", areaMagazzinoContabilizzazione.getIdAreaMagazzino());
            @SuppressWarnings("unchecked")
            List<ArticoloLite> articoliCategoria = query.getResultList();

            SedeEntita sedeEntita;
            try {
                sedeEntita = sediEntitaManager.caricaSedeEntita(areaMagazzinoContabilizzazione.getIdSedeEntita());
            } catch (Exception e) {
                logger.error("-->errore durante il caricamento della sede entità", e);
                throw new RuntimeException("errore durante il caricamento della sede entità", e);
            }

            Deposito deposito = depositiManager.caricaDeposito(areaMagazzinoContabilizzazione.getIdDepositoOrigine());

            throw new SottoContoContabileAssenteException(categoriaContabileArticolo, categoriaContabileDeposito,
                    categoriaContabileSedeMagazzino, articoliCategoria, deposito.getDepositoLite(), sedeEntita);
        }

        controPartita.setImporto(importo);
        controPartita.setTipologiaContoControPartita(ETipologiaContoControPartita.SOTTOCONTO);

        if (areaMagazzinoContabilizzazione.getTipoEntita() == TipoEntita.CLIENTE) {
            if (!areaMagazzinoContabilizzazione.isNotaCreditoEnable()) {
                // if (importo.signum() > -1) {
                controPartita.setAvere(sottoConto);
                // } else {
                // controPartita.setDare(sottoConto);
                // }
            } else {
                // if (importo.signum() > -1) {
                controPartita.setDare(sottoConto);
                // } else {
                // controPartita.setAvere(sottoConto);
                // }
            }
        } else {
            if (!areaMagazzinoContabilizzazione.isNotaCreditoEnable()) {
                // if (importo.signum() > -1) {
                controPartita.setDare(sottoConto);
                // } else {
                // controPartita.setAvere(sottoConto);
                // }
            } else {
                // if (importo.signum() > -1) {
                controPartita.setAvere(sottoConto);
                // } else {
                // controPartita.setDare(sottoConto);
                // }
            }
        }
        return controPartita;
    }

    /**
     * @return codice azienda corrente
     */
    private String getAzienda() {
        return ((JecPrincipal) sessionContext.getCallerPrincipal()).getCodiceAzienda();
    }

    /**
     * Restituisce il nome dell'utente.
     *
     * @return nome
     */
    private JecPrincipal getPrincipal() {
        return (JecPrincipal) sessionContext.getCallerPrincipal();
    }

    @Override
    public CategoriaContabileArticolo salvaCategoriaContabileArticolo(
            CategoriaContabileArticolo categoriaContabileArticolo) {
        logger.debug("--> Enter salvaCategoriaContabileArticolo");
        try {
            if (categoriaContabileArticolo.getCodiceAzienda() == null) {
                categoriaContabileArticolo.setCodiceAzienda(getAzienda());
            }
            categoriaContabileArticolo = panjeaDAO.save(categoriaContabileArticolo);
        } catch (Exception e) {
            logger.error("--> Errore durante il salvataggio delle categoriaContabileArticolo", e);
            throw new RuntimeException("Errore durante il salvataggio delle categoriaContabileArticolo", e);
        }
        logger.debug("--> Exit salvaCategoriaContabileArticolo");
        return categoriaContabileArticolo;
    }

    @Override
    public CategoriaContabileDeposito salvaCategoriaContabileDeposito(
            CategoriaContabileDeposito categoriaContabileDeposito) {
        logger.debug("--> Enter salvaCategoriaContabileDeposito");
        try {
            if (categoriaContabileDeposito.getCodiceAzienda() == null) {
                categoriaContabileDeposito.setCodiceAzienda(getAzienda());
            }
            categoriaContabileDeposito = panjeaDAO.save(categoriaContabileDeposito);
        } catch (Exception e) {
            logger.error("--> Errore durante il salvataggio delle categoriaContabileDeposito", e);
            throw new RuntimeException("Errore durante il salvataggio delle categoriaContabileDeposito", e);
        }
        logger.debug("--> Exit salvaCategoriaContabileDeposito");
        return categoriaContabileDeposito;
    }

    @Override
    public CategoriaContabileSedeMagazzino salvaCategoriaContabileSedeMagazzino(
            CategoriaContabileSedeMagazzino categoriaContabileSedeMagazzino) {
        logger.debug("--> Enter salvaCategoriaContabileSedeMagazzino");
        try {
            if (categoriaContabileSedeMagazzino.getCodiceAzienda() == null) {
                categoriaContabileSedeMagazzino.setCodiceAzienda(getAzienda());
            }
            categoriaContabileSedeMagazzino = panjeaDAO.save(categoriaContabileSedeMagazzino);
        } catch (Exception e) {
            logger.error("--> Errore durante il salvataggio delle salvaCategoriaContabileSedeMagazzino", e);
            throw new RuntimeException("Errore durante il salvataggio delle salvaCategoriaContabileSedeMagazzino", e);
        }
        logger.debug("--> Exit salvaCategoriaContabileSedeMagazzino");
        return categoriaContabileSedeMagazzino;
    }

    @Override
    public SottoContoContabilizzazione salvaSottoContoContabilizzazione(
            SottoContoContabilizzazione sottoContoContabilizzazione) {
        logger.debug("--> Enter salvaSottoContoContabilizzazione");

        if (sottoContoContabilizzazione.isNew()) {
            sottoContoContabilizzazione.setCodiceAzienda(getAzienda());
        }

        SottoContoContabilizzazione sottoContoContabilizzazioneSalvato = null;
        try {
            sottoContoContabilizzazioneSalvato = panjeaDAO.save(sottoContoContabilizzazione);
        } catch (Exception e) {
            logger.error("--> Errore durante il salvataggio di SottoContoContabilizzazione.", e);
            throw new RuntimeException("Errore durante il salvataggio di SottoContoContabilizzazione", e);
        }

        logger.debug("--> Exit salvaSottoContoContabilizzazione");
        return sottoContoContabilizzazioneSalvato;
    }
}
