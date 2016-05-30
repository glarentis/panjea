package it.eurotn.panjea.contabilita.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.IgnoreDependency;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.domain.Importo.TIPO_OPERAZIONE_VALUTA;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.ControPartita;
import it.eurotn.panjea.contabilita.domain.ETipologiaConto;
import it.eurotn.panjea.contabilita.domain.RigaContabile;
import it.eurotn.panjea.contabilita.domain.StrutturaContabile;
import it.eurotn.panjea.contabilita.domain.TipoAreaContabile;
import it.eurotn.panjea.contabilita.manager.FormuleManagerBean.TipoVariabiliFormula;
import it.eurotn.panjea.contabilita.manager.interfaces.AreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.FormuleManager;
import it.eurotn.panjea.contabilita.manager.interfaces.IFormula;
import it.eurotn.panjea.contabilita.manager.interfaces.PianoContiManager;
import it.eurotn.panjea.contabilita.manager.interfaces.StrutturaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.VariabiliFormulaManager;
import it.eurotn.panjea.contabilita.manager.rigacontabilebuider.ContoBaseRigaContabileBuilder;
import it.eurotn.panjea.contabilita.manager.rigacontabilebuider.ContoRigaContabileBuilder;
import it.eurotn.panjea.contabilita.manager.rigacontabilebuider.ControPartiteRigaContabileBuilder;
import it.eurotn.panjea.contabilita.manager.rigacontabilebuider.EntitaRigaContabileBuilder;
import it.eurotn.panjea.contabilita.manager.rigacontabilebuider.IRigaContabileBuilder;
import it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces.RitenutaAccontoContabilitaManager;
import it.eurotn.panjea.contabilita.service.exception.ContabilitaException;
import it.eurotn.panjea.contabilita.service.exception.ContiEntitaAssentiException;
import it.eurotn.panjea.contabilita.service.exception.ContoEntitaAssenteException;
import it.eurotn.panjea.contabilita.service.exception.ContoRapportoBancarioAssenteException;
import it.eurotn.panjea.contabilita.service.exception.FormulaException;
import it.eurotn.panjea.contabilita.util.ParametriCalcoloControPartite;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * Manager per gestire la generazione delle righe contabili secondo la struttura contabile definita.
 *
 * @author
 */
@Stateless(name = "Panjea.StrutturaContabileManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.StrutturaContabileManager")
public class StrutturaContabileManagerBean implements StrutturaContabileManager {

    private static final Logger LOGGER = Logger.getLogger(StrutturaContabileManagerBean.class);

    @Resource
    private SessionContext context;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB(mappedName = "Panjea.AreaContabileManager")
    private AreaContabileManager areaContabileManager;

    @EJB(mappedName = "Panjea.VariabiliFormulaManager")
    private VariabiliFormulaManager variabiliFormulaManager;

    @EJB
    private PianoContiManager pianoContiManager;

    @EJB
    private FormuleManager formuleManager;

    @EJB
    private AziendeManager aziendeManager;

    @EJB
    @IgnoreDependency
    private RitenutaAccontoContabilitaManager ritenutaAccontoContabilitaManager;

    @Override
    public void cancellaControPartita(ControPartita controPartita) {
        LOGGER.debug("--> Enter cancellaControPartita");

        try {
            panjeaDAO.delete(controPartita);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante la cancellazione della contro partita. ", e);
            throw new RuntimeException("Errore durante la cancellazione della contro partita.", e);
        }

        LOGGER.debug("--> Exit cancellaControPartita");
    }

    @Override
    public void cancellaStrutturaContabile(StrutturaContabile strutturaContabile) {
        LOGGER.debug("--> Enter cancellaStrutturaContabile");

        // se la struttura contabile e di tipo contro partita cancella tutte le
        // eventuali
        // contro partite
        if (strutturaContabile.getTipologiaConto() == ETipologiaConto.CONTRO_PARTITA) {
            List<ControPartita> listCP = caricaControPartite(strutturaContabile.getTipoDocumento(),
                    strutturaContabile.getEntita());

            for (ControPartita partita : listCP) {
                cancellaControPartita(partita);
            }
        }

        try {
            panjeaDAO.delete(strutturaContabile);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante la cancellazione della struttura contabile.", e);
            throw new RuntimeException(e);
        }

        LOGGER.debug("--> Exit cancellaStrutturaContabile");
    }

    @SuppressWarnings("unchecked")
    @Override
    public void cancellaStrutturaContabile(TipoAreaContabile tipoAreaContabile) {
        String hql = "from StrutturaContabile s where s.codiceAzienda = :paramCodiceAzienda and s.tipoDocumento.id = :paramTipoDocumentoId order by ordine";
        Query query = panjeaDAO.prepareQuery(hql);
        query.setParameter("paramTipoDocumentoId", tipoAreaContabile.getTipoDocumento().getId());
        query.setParameter("paramCodiceAzienda", getAzienda());

        List<StrutturaContabile> list = new ArrayList<StrutturaContabile>();
        try {
            list = panjeaDAO.getResultList(query);
            for (StrutturaContabile strutturaContabile : list) {
                cancellaStrutturaContabile(strutturaContabile);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ControPartita caricaControPartita(ControPartita controPartita) throws ContabilitaException {
        LOGGER.debug("--> Enter caricaControPartita");

        ControPartita controPartitaCaricata = null;
        try {
            controPartitaCaricata = panjeaDAO.load(ControPartita.class, controPartita.getId());
        } catch (ObjectNotFoundException e) {
            LOGGER.error("-->Oggetto non trovato. Id cercato " + controPartita.getId(), e);
            throw new ContabilitaException("Oggetto non trovato. Id cercato " + controPartita.getId(), e);
        }

        LOGGER.debug("--> Exit caricaControPartita");
        return controPartitaCaricata;
    }

    @Override
    public List<ControPartita> caricaControPartite(AreaContabile areaContabile) throws ContabilitaException {
        LOGGER.debug("--> Enter caricaControPartite");

        List<StrutturaContabile> strutturaContabile = caricaStrutturaContabile(areaContabile);

        List<ControPartita> list = new ArrayList<ControPartita>();
        for (StrutturaContabile scontabile : strutturaContabile) {
            if (scontabile.getTipologiaConto() == ETipologiaConto.CONTRO_PARTITA) {
                list = caricaControPartite(scontabile.getTipoDocumento(), scontabile.getEntita());
                break;
            }
        }

        LOGGER.debug("--> Exit caricaControPartite");
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ControPartita> caricaControPartite(TipoDocumento tipoDocumento) {
        LOGGER.debug("--> Enter caricaControPartite");

        List<ControPartita> listControPartite = new ArrayList<ControPartita>();

        String queryString = "from ControPartita c where c.tipoDocumento.id = :paramTipoDocumentoId";
        Query query = panjeaDAO.prepareQuery(queryString);
        query.setParameter("paramTipoDocumentoId", tipoDocumento.getId());

        try {
            listControPartite = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> Errore durante il caricamento delle contro partite.", e);
            throw new RuntimeException("Errore durante il caricamento delle contro partite.", e);
        }

        LOGGER.debug("--> Exit caricaControPartite");
        return listControPartite;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ControPartita> caricaControPartite(TipoDocumento tipoDocumento, EntitaLite entita) {
        LOGGER.debug("--> Enter caricaControPartite");

        List<ControPartita> listControPartite = new ArrayList<ControPartita>();

        Query query = null;
        if (entita == null) {
            query = panjeaDAO.prepareNamedQuery("ControPartita.caricaByTipoDoc");
        } else {
            query = panjeaDAO.prepareNamedQuery("ControPartita.caricaByTipoDocEEntita");
            query.setParameter("paramEntitaId", entita.getId());
        }
        query.setParameter("paramTipoDocumentoId", tipoDocumento.getId());

        try {
            listControPartite = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> Errore durante il caricamento delle contro partite.", e);
            throw new RuntimeException("Errore durante il caricamento delle contro partite.", e);
        }

        LOGGER.debug("--> Exit caricaControPartite");
        return listControPartite;
    }

    @Override
    public List<ControPartita> caricaControPartiteConImporto(AreaContabile areaContabile)
            throws ContabilitaException, FormulaException {
        LOGGER.debug("--> Enter caricaControPartiteConImporto");
        List<ControPartita> list = caricaControPartite(areaContabile);

        Map<String, BigDecimal> map = null;

        ParametriCalcoloControPartite parametriCalcoloControPartite = variabiliFormulaManager
                .getMapVariabiliFromControPartite(areaContabile, list);

        for (ControPartita controPartita : list) {

            if (controPartita.getFormula() != null && controPartita.getFormula().length() > 0) {

                // restituisco la mappa delle variabili in base al codice iva
                // specificato
                // sulla contro partita
                if (controPartita.getCodiceIva() == null) {
                    map = parametriCalcoloControPartite.get(ParametriCalcoloControPartite.SENZA_CODICE_IVA);
                } else {
                    // se la lista di parametri contiene il codice iva della
                    // contro partita restituisco
                    // la mappa relativa al codiceiva
                    if (parametriCalcoloControPartite.containsKey(controPartita.getCodiceIva().getCodice())) {
                        map = parametriCalcoloControPartite.get(controPartita.getCodiceIva().getCodice());
                    } else {
                        // altrimenti restituisco la mappa senza codice iva dove
                        // tutte le variabili di riga
                        // hanno valore = 0
                        map = parametriCalcoloControPartite.get(ParametriCalcoloControPartite.SENZA_CODICE_IVA);
                    }
                }
                controPartita.setImporto(formuleManager.calcola(controPartita.getFormula(), map, 2));
            }
        }
        LOGGER.debug("--> Exit caricaControPartiteConImporto");
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<EntitaLite> caricaEntitaConStrutturaContabile(TipoDocumento tipoDocumento) {
        LOGGER.debug("--> Enter caricaEntitaConStrutturaContabile");

        StringBuilder sb = new StringBuilder();
        sb.append("select distinct st.entita ");
        sb.append("from StrutturaContabile st ");
        sb.append("where st.codiceAzienda = :codiceAzienda ");
        sb.append("and st.tipoDocumento.id = :idTipoDocumento ");
        sb.append("and st.entita is not null ");

        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("codiceAzienda", getAzienda());
        query.setParameter("idTipoDocumento", tipoDocumento.getId());

        List<EntitaLite> entita = new ArrayList<>();
        try {
            entita = panjeaDAO.getResultList(query);
        } catch (DAOException e) {
            LOGGER.error("--> errore durante il caricamento delle entità con struttura contabile.", e);
            throw new RuntimeException("errore durante il caricamento delle entità con struttura contabile.", e);
        }

        return entita;
    }

    @Override
    public List<StrutturaContabile> caricaStrutturaContabile(AreaContabile areaContabile) throws ContabilitaException {
        LOGGER.debug("--> Enter caricaStrutturaContabile");

        TipoDocumento tipoDocumento = areaContabile.getTipoAreaContabile().getTipoDocumento();

        List<StrutturaContabile> strutturaContabile = caricaStrutturaContabile(tipoDocumento,
                areaContabile.getDocumento().getEntita(), true);

        LOGGER.debug("--> Exit caricaStrutturaContabile");
        return strutturaContabile;

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<StrutturaContabile> caricaStrutturaContabile(TipoDocumento tipoDocumento) {
        LOGGER.debug("--> Enter caricaStrutturaContabile");

        List<StrutturaContabile> list = new ArrayList<StrutturaContabile>();

        String queryString = "from StrutturaContabile s where s.codiceAzienda = :paramCodiceAzienda and s.tipoDocumento.id = :paramTipoDocumentoId";
        Query query = panjeaDAO.prepareQuery(queryString);
        query.setParameter("paramTipoDocumentoId", tipoDocumento.getId());
        query.setParameter("paramCodiceAzienda", getAzienda());

        try {
            list = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante il caricamento delle strutture contabili.", e);
            throw new RuntimeException("errore durante il caricamento delle strutture contabili.", e);
        }

        LOGGER.debug("--> Exit caricaStrutturaContabile");
        return list;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<StrutturaContabile> caricaStrutturaContabile(TipoDocumento tipoDocumento, EntitaLite entita,
            boolean loadDefault) throws ContabilitaException {
        LOGGER.debug("--> Enter caricaStrutturaContabile");

        List<StrutturaContabile> list = new ArrayList<StrutturaContabile>();

        Query query = null;
        if (entita == null) {
            query = panjeaDAO.prepareNamedQuery("StrutturaContabile.caricaByTipoDoc");
        } else {
            query = panjeaDAO.prepareNamedQuery("StrutturaContabile.caricaByTipoDocEEntita");
            query.setParameter("paramEntitaId", entita.getId());
        }
        query.setParameter("paramTipoDocumentoId", tipoDocumento.getId());
        query.setParameter("paramCodiceAzienda", getAzienda());

        try {
            list = panjeaDAO.getResultList(query);
            // se il parametro loadDefault � uguale a true e la ricerca della
            // struttura contabile in base all'entit�
            // non ha dato nessun risultato, carica la struttura contabile di
            // default del tipo documento
            if ((loadDefault) && (list.size() == 0) && (entita != null)) {
                query = panjeaDAO.prepareNamedQuery("StrutturaContabile.caricaByTipoDoc");
                query.setParameter("paramTipoDocumentoId", tipoDocumento.getId());
                query.setParameter("paramCodiceAzienda", getAzienda());
                list = panjeaDAO.getResultList(query);
            }
        } catch (DAOException e) {
            LOGGER.error("--> Errore durante il caricamento della struttura contabile. ", e);
            throw new ContabilitaException("Errore durante il caricamento della struttura contabile.", e);
        }

        LOGGER.debug("--> Exit caricaStrutturaContabile # " + list.size());
        return list;
    }

    @Override
    public List<String> caricaVariabiliFormulaControPartite() {
        List<String> list = new ArrayList<String>();
        list.addAll(variabiliFormulaManager.getMapVariabiliControPartite().keySet());
        return list;
    }

    @Override
    public List<String> caricaVariabiliFormulaStrutturaContabile() {
        List<String> list = new ArrayList<String>();
        list.addAll(variabiliFormulaManager.getMapVariabiliStrutturaContabile().keySet());
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return list;
    }

    @Override
    public List<RigaContabile> creaRigheContabili(AreaContabile areaContabile, List<ControPartita> controPartite)
            throws ContabilitaException, FormulaException, ContoRapportoBancarioAssenteException,
            ContiEntitaAssentiException {
        LOGGER.debug("--> Enter creaRigheContabili");

        List<StrutturaContabile> listStrutturaContabile = caricaStrutturaContabile(areaContabile);
        List<IFormula> formule = new ArrayList<IFormula>(listStrutturaContabile);
        Map<String, BigDecimal> mapSC = variabiliFormulaManager.creaMapVariabili(areaContabile, formule,
                TIPO_OPERAZIONE_VALUTA.AZIENDA);

        ContiEntitaAssentiException contiEntitaAssentiException = new ContiEntitaAssentiException();

        // Costruisco la mappa dei builder per le righe contabili
        Map<ETipologiaConto, IRigaContabileBuilder> mapRigaContabileBuilder = new HashMap<ETipologiaConto, IRigaContabileBuilder>();
        mapRigaContabileBuilder.put(ETipologiaConto.CONTO,
                new ContoRigaContabileBuilder(pianoContiManager, formuleManager, areaContabileManager, aziendeManager));
        mapRigaContabileBuilder.put(ETipologiaConto.CONTO_BASE, new ContoBaseRigaContabileBuilder(pianoContiManager,
                formuleManager, areaContabileManager, aziendeManager));
        mapRigaContabileBuilder.put(ETipologiaConto.ENTITA, new EntitaRigaContabileBuilder(pianoContiManager,
                formuleManager, areaContabileManager, aziendeManager));
        mapRigaContabileBuilder.put(ETipologiaConto.CONTRO_PARTITA,
                new ControPartiteRigaContabileBuilder(areaContabileManager));

        // setto manualmente l'ordinamento altrimenti generando le righe
        // contabili nel ciclo assumerebbero tutte lo
        // stesso valore. Incremento di 1 ogni volta che vado a generare una
        // riga
        long ordinamentoRiga = Calendar.getInstance().getTimeInMillis();

        for (StrutturaContabile strutturaContabile : listStrutturaContabile) {

            IRigaContabileBuilder rigaContabileBuilder = mapRigaContabileBuilder
                    .get(strutturaContabile.getTipologiaConto());

            if (rigaContabileBuilder != null) {
                List<RigaContabile> righeCreate = null;
                try {
                    righeCreate = rigaContabileBuilder.creaRigheContabili(strutturaContabile, areaContabile, mapSC,
                            ordinamentoRiga, controPartite, false);
                } catch (ContoEntitaAssenteException e) {
                    LOGGER.warn("--> Attenzione, non esiste un sottocondo per l'entità. Non genero la riga contabile.");
                    contiEntitaAssentiException.add(e);
                }

                ordinamentoRiga = ordinamentoRiga + (righeCreate != null ? righeCreate.size() : 0);
            }
        }

        creaRigheContabiliAutomatiche(areaContabile, ordinamentoRiga);

        if (!contiEntitaAssentiException.getContiEntitaExceptions().isEmpty()) {
            // lancio la runtime per fare la rollback sulle operazioni eseguite
            // fin'ora
            throw new RuntimeException(contiEntitaAssentiException);
        }

        LOGGER.debug("--> Exit creaRigheContabili");
        return areaContabileManager.caricaRigheContabili(areaContabile.getId());
    }

    @Override
    public void creaRigheContabiliAutomatiche(AreaContabile areaContabile, long ordinamento) {

        // cancello prima tutte le righe contabili presenti
        StringBuilder sb = new StringBuilder();
        sb.append("delete from RigaContabile r where r.areaContabile = :areaContabile and r.automatica = true ");
        Query query = panjeaDAO.prepareQuery(sb.toString());
        query.setParameter("areaContabile", areaContabile);
        try {
            panjeaDAO.executeQuery(query);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la cancellazione delle righe automatiche.", e);
            throw new RuntimeException("errore durante la cancellazione delle righe automatiche.", e);
        }

        // creo tutte le righe contabili automatiche
        try {
            ritenutaAccontoContabilitaManager.creaRigheContabiliRitenutaAcconto(areaContabile, ordinamento);
        } catch (Exception e) {
            LOGGER.error("--> errore durante la generazione delle righe automatiche.", e);
            throw new RuntimeException("errore durante la generazione delle righe automatiche.", e);
        }
    }

    /**
     * @return Restituisce il codice azienda.
     */
    private String getAzienda() {
        JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
        return jecPrincipal.getCodiceAzienda();
    }

    @Override
    public ControPartita salvaControPartita(ControPartita controPartita) throws FormulaException {
        LOGGER.debug("--> Enter salvaControPartita");

        // verifico che la formula sia corretta
        validaFormula(controPartita.getFormula(), TipoVariabiliFormula.CONTRO_PARTITA);

        ControPartita controPartitaSalvata = null;
        try {
            controPartitaSalvata = panjeaDAO.save(controPartita);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il salvataggio della contro partita.", e);
            throw new RuntimeException("Errore durante il salvataggio della contro partita.", e);
        }

        LOGGER.debug("--> Exit salvaControPartita");
        return controPartitaSalvata;
    }

    @Override
    public StrutturaContabile salvaStrutturaContabile(StrutturaContabile strutturaContabile) throws FormulaException {
        LOGGER.debug("--> Enter salvaStrutturaContabile");

        // verifico che la formula sia corretta
        if (strutturaContabile.getTipologiaConto() != ETipologiaConto.CONTRO_PARTITA) {
            validaFormula(strutturaContabile.getFormula(), TipoVariabiliFormula.STRUTTURA_CONTABILE);
        }

        // se non e' presente il codice azienda setto quello del principal
        if (strutturaContabile.getCodiceAzienda() == null) {
            strutturaContabile.setCodiceAzienda(getAzienda());
        }

        StrutturaContabile strutturaContabileSalvata = null;
        try {
            strutturaContabileSalvata = panjeaDAO.save(strutturaContabile);
        } catch (Exception e) {
            LOGGER.error("--> Errore durante il salvataggio della struttura contabile. ", e);
            throw new RuntimeException("Errore durante il salvataggio della struttura contabile.", e);
        }

        LOGGER.debug("--> Exit salvaStrutturaContabile");
        return strutturaContabileSalvata;
    }

    /**
     * Valida la formula.
     * 
     * @param formula
     *            formula
     * @param tipoVariabiliFormula
     *            tipoVariabiliFormula
     * @throws FormulaException
     *             formula errata
     */
    private void validaFormula(String formula, TipoVariabiliFormula tipoVariabiliFormula) throws FormulaException {

        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();

        switch (tipoVariabiliFormula) {
        case CONTRO_PARTITA:
            map.putAll(variabiliFormulaManager.getMapVariabiliControPartite());
            break;
        case STRUTTURA_CONTABILE:
            map.putAll(variabiliFormulaManager.getMapVariabiliStrutturaContabile());
            break;
        default:
            break;
        }

        Set<String> chiavi = map.keySet();

        // setto tutti i valori delle variabili a 1
        for (String chiave : chiavi) {
            map.put(chiave, BigDecimal.ONE);
        }

        formuleManager.calcola(formula, map, 2);
    }
}
