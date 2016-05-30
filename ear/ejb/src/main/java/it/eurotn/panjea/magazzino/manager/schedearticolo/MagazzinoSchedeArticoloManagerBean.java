/**
 *
 */
package it.eurotn.panjea.magazzino.manager.schedearticolo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import javax.persistence.Query;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.hibernate.ejb.QueryImpl;
import org.hibernate.transform.Transformers;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;
import org.joda.time.DateTime;

import it.eurotn.dao.exception.DAOException;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.anagrafica.service.exception.PreferenceNotFoundException;
import it.eurotn.panjea.anagrafica.service.interfaces.PreferenceService;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.SchedaArticolo;
import it.eurotn.panjea.magazzino.domain.SchedaArticolo.StatoScheda;
import it.eurotn.panjea.magazzino.manager.articolo.CustomArticoliFilter;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoMovimentazioneManager;
import it.eurotn.panjea.magazzino.manager.schedearticolo.interfaces.MagazzinoSchedeArticoloManager;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.MovimentazioneArticolo;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazioneArticolo;
import it.eurotn.panjea.magazzino.util.RigaMovimentazione;
import it.eurotn.panjea.magazzino.util.SchedaArticoloDTO;
import it.eurotn.panjea.magazzino.util.SituazioneSchedaArticoloDTO;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCreazioneSchedeArticoli;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.security.JecPrincipal;

/**
 * @author fattazzo
 *
 */
@Stateless(name = "Panjea.MagazzinoSchedeArticoloManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.MagazzinoSchedeArticoloManager")
public class MagazzinoSchedeArticoloManagerBean implements MagazzinoSchedeArticoloManager, CustomArticoliFilter {

    private static Logger logger = Logger.getLogger(MagazzinoSchedeArticoloManagerBean.class);

    @Resource
    private SessionContext context;

    @EJB
    private PanjeaDAO panjeaDAO;

    @Resource(mappedName = "queue/schedeArticoloMagazzino")
    private Queue queue;

    @Resource(mappedName = "ConnectionFactory")
    private ConnectionFactory jmsConnectionFactory;

    @EJB
    private MagazzinoMovimentazioneManager magazzinoMovimentazioneManager;

    @EJB
    private PreferenceService preferenceService;

    @EJB
    private DepositiManager depositiManager;

    /**
     * Aggiunge tutte le schede articolo dei mesi mancanti nell'anno di riferimento.
     *
     * @param schedePresenti
     *            schede articolo presenti per l'anno di riferimento
     * @param annoRiferimento
     *            anno di riferimento
     * @return situazione schede articolo dell'anno
     */
    private List<SituazioneSchedaArticoloDTO> aggiungiSchedaArticoloMancanti(
            List<SituazioneSchedaArticoloDTO> schedePresenti, Integer annoRiferimento) {

        Map<Integer, SituazioneSchedaArticoloDTO> mapSchede = new HashMap<Integer, SituazioneSchedaArticoloDTO>();

        // inserisco nella mappa tutte le schede presenti
        for (SituazioneSchedaArticoloDTO situazioneSchedeDTO : schedePresenti) {
            mapSchede.put(situazioneSchedeDTO.getMese(), situazioneSchedeDTO);
        }

        List<SituazioneSchedaArticoloDTO> schedeAnno = new ArrayList<SituazioneSchedaArticoloDTO>();

        // aggiungo le schede non presenti
        for (int i = 1; i <= 12; i++) {
            SituazioneSchedaArticoloDTO scheda = mapSchede.get(i);
            if (scheda == null) {
                // per questo mese non ho la scheda quindi la creo
                scheda = new SituazioneSchedaArticoloDTO();
                scheda.setAnno(annoRiferimento);
                scheda.setMese(i);
                scheda.setArticoliStampati(0);
                scheda.setArticoliNonValidi(0);
                scheda.setArticoliRimanenti(caricaNumeroArticoliPerGestioneSchedaArticolo(annoRiferimento, i));
            }
            schedeAnno.add(scheda);
        }

        return schedeAnno;
    }

    @SuppressWarnings("unchecked")
    @Override
    @RolesAllowed("gestioneSchedeArticolo")
    public List<ArticoloRicerca> caricaArticoliNonValidi(Integer anno, Integer mese) {

        Query query = panjeaDAO.prepareNamedQuery("SchedaArticolo.caricaArticoliNonValidi");
        ((QueryImpl) query).getHibernateQuery().setResultTransformer(Transformers.aliasToBean((ArticoloRicerca.class)));
        query.setParameter("codiceAzienda", getAzienda());
        query.setParameter("anno", anno);
        query.setParameter("mese", mese);

        List<ArticoloRicerca> articoli = null;
        try {
            articoli = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento degli articoli non validi", e);
            throw new RuntimeException("errore durante il caricamento degli articoli non validi", e);
        }

        return articoli;
    }

    @SuppressWarnings("unchecked")
    @Override
    @RolesAllowed("gestioneSchedeArticolo")
    public List<ArticoloRicerca> caricaArticoliRimanenti(Integer anno, Integer mese) {
        Query query = panjeaDAO.prepareNamedQuery("SchedaArticolo.caricaArticoliDaStampare");
        ((QueryImpl) query).getHibernateQuery().setResultTransformer(Transformers.aliasToBean((ArticoloRicerca.class)));
        query.setParameter("codiceAzienda", getAzienda());
        query.setParameter("anno", anno);
        query.setParameter("mese", mese);

        List<ArticoloRicerca> articoli = null;
        try {
            articoli = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento degli articoli non Rimanenti", e);
            throw new RuntimeException("errore durante il caricamento degli articoli Rimanenti", e);
        }

        return articoli;
    }

    @SuppressWarnings("unchecked")
    @Override
    @RolesAllowed("gestioneSchedeArticolo")
    public List<ArticoloRicerca> caricaArticoliStampati(Integer anno, Integer mese) {
        Query query = panjeaDAO.prepareNamedQuery("SchedaArticolo.caricaArticoliStampati");
        ((QueryImpl) query).getHibernateQuery().setResultTransformer(Transformers.aliasToBean((ArticoloRicerca.class)));
        query.setParameter("codiceAzienda", getAzienda());
        query.setParameter("anno", anno);
        query.setParameter("mese", mese);

        List<ArticoloRicerca> articoli = null;
        try {
            articoli = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento degli articoli stampati", e);
            throw new RuntimeException("errore durante il caricamento degli articoli stampati", e);
        }

        return articoli;
    }

    @Override
    public byte[] caricaFileSchedaArticolo(Integer anno, Integer mese, Integer idArticolo)
            throws FileNotFoundException {

        // carico la directory dove è stato salvato il file della scheda articolo
        String filePath = StampaSchedaArticoloBuilder.getStampeSchedaArticoloExportPath(getDirectoryExportPath(), anno,
                mese);

        // ottengo il nome del file in base all'articolo
        ArticoloLite articolo;
        try {
            articolo = panjeaDAO.load(ArticoloLite.class, idArticolo);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento dell'articolo.", e);
            throw new RuntimeException("errore durante il caricamento dell'articolo.", e);
        }
        String fileName = StampaSchedaArticoloBuilder.getStampaSchedaArticoloFileName(articolo.createArticoloRicerca());

        // Carico il file
        String fileSchedaString = new StringBuilder(filePath).append(File.separator).append(fileName).toString();
        File fileScheda = new File(fileSchedaString);

        byte[] result = null;
        if (fileScheda.exists()) {
            try {
                result = FileUtils.readFileToByteArray(fileScheda);
            } catch (IOException e) {
                logger.error("-->errore nel leggere il file della scheda articolo", e);
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * Carica il numero di articoli che devono essere gestiti con le schede per il mese e anno indicato.
     *
     * @param anno
     *            anno
     * @param mese
     *            mese
     * @return numero di articoli
     */
    private Integer caricaNumeroArticoliPerGestioneSchedaArticolo(Integer anno, Integer mese) {

        Query query = panjeaDAO.prepareQuery(
                "select count(a.id) from Articolo a where a.codiceAzienda = :codiceAzienda and a.gestioneSchedaArticolo=true and (a.gestioneSchedaArticoloAnno < :anno or ( a.gestioneSchedaArticoloAnno = :anno and  a.gestioneSchedaArticoloMese <= :mese))");
        query.setParameter("codiceAzienda", getAzienda());
        query.setParameter("anno", anno);
        query.setParameter("mese", mese);

        Integer numeroArticoli = ((Long) query.getSingleResult()).intValue();

        return numeroArticoli;
    }

    @Override
    public SchedaArticolo caricaSchedaArticolo(Integer anno, Integer mese, Integer idArticolo) {
        Query query = panjeaDAO.prepareNamedQuery("SchedaArticolo.caricaSchedaArticolo");
        query.setParameter("codiceAzienda", getAzienda());
        query.setParameter("anno", anno);
        query.setParameter("mese", mese);
        query.setParameter("idArticolo", idArticolo);

        SchedaArticolo scheda = null;
        try {
            scheda = (SchedaArticolo) panjeaDAO.getSingleResult(query);
        } catch (ObjectNotFoundException e) {
            // la scheda ancora non è stata creata, ne creo uno nuova
            scheda = new SchedaArticolo();
            scheda.setAnno(anno);
            scheda.setMese(mese);
            scheda.setArticolo(panjeaDAO.loadLazy(Articolo.class, idArticolo));
            scheda.setStato(StatoScheda.IN_ELABORAZIONE);
        } catch (DAOException e) {
            logger.error("--> errore durante il caricamento della scheda articolo.", e);
            throw new RuntimeException("errore durante il caricamento della scheda articolo.", e);
        }

        return scheda;
    }

    @Override
    public List<MovimentazioneArticolo> caricaSchedaArticolo(Map<Object, Object> params) {

        List<MovimentazioneArticolo> movimentazioni = new ArrayList<MovimentazioneArticolo>();

        Integer anno = (Integer) params.get("anno");
        Integer mese = (Integer) params.get("mese");
        Integer idArticolo = (Integer) params.get("idArticolo");

        ParametriRicercaMovimentazioneArticolo parametri = new ParametriRicercaMovimentazioneArticolo();

        parametri.setGiacenzaProgressivaInizialeAZero(true);

        // periodo
        Periodo periodo = new Periodo();
        periodo.setTipoPeriodo(TipoPeriodo.DATE);
        DateTime dataIniziale = new DateTime(anno, mese, 1, 0, 0, 0, 0);
        periodo.setDataIniziale(dataIniziale.toDate());
        periodo.setDataFinale(dataIniziale.dayOfMonth().withMaximumValue().toDate());
        parametri.setPeriodo(periodo);

        // articolo
        ArticoloLite articolo = new ArticoloLite();
        articolo.setId(idArticolo);
        parametri.setArticoloLite(articolo);

        // carico la movimentazione di tutti i depositi dell'articolo
        List<DepositoLite> depositi = depositiManager.caricaDepositiAzienda();
        for (DepositoLite deposito : depositi) {

            parametri.setDepositoLite(deposito);

            MovimentazioneArticolo movimentazione = magazzinoMovimentazioneManager
                    .caricaMovimentazioneArticolo(parametri);
            if (!isMovimentazioneEmpty(movimentazione)) {
                movimentazioni.add(movimentazione);
            }
        }

        return movimentazioni;
    }

    @SuppressWarnings("unchecked")
    @Override
    @RolesAllowed("gestioneSchedeArticolo")
    public List<SituazioneSchedaArticoloDTO> caricaSituazioneSchedeArticolo(Integer anno) {
        StringBuilder sb = new StringBuilder();
        sb.append("select sa.anno as anno,sa.mese as mese, ");
        sb.append("cast(coalesce(sum(CASE WHEN sa.stato=1 THEN 1 ELSE 0 END),0), int) as articoliStampati, ");
        sb.append("cast(coalesce(sum(CASE WHEN sa.stato=2 THEN 1 ELSE 0 END),0), int) as articoliNonValidi, ");
        sb.append("(select cast(count(a.id),int) from Articolo a ");
        sb.append(
                "where a.gestioneSchedaArticolo=true and (a.codiceAzienda = :codiceAzienda and a.gestioneSchedaArticoloAnno < sa.anno or ( a.gestioneSchedaArticoloAnno = sa.anno and  a.gestioneSchedaArticoloMese <= sa.mese)) ");
        sb.append(
                "and a.id not in (select sa2.articolo.id from SchedaArticolo sa2  where sa2.anno=sa.anno and sa2.mese=sa.mese) ) as articoliRimanenti ");
        sb.append("from SchedaArticolo sa ");
        sb.append("where sa.codiceAzienda = :codiceAzienda and ");
        sb.append("               sa.anno = :anno ");
        sb.append("group by sa.anno,sa.mese ");

        Query query = panjeaDAO.prepareQuery(sb.toString(), SituazioneSchedaArticoloDTO.class, null);
        query.setParameter("codiceAzienda", getAzienda());
        query.setParameter("anno", anno);

        List<SituazioneSchedaArticoloDTO> situazione = new ArrayList<SituazioneSchedaArticoloDTO>();
        try {
            situazione = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento della situazione schede articolo. Anno: " + anno, e);
        }

        situazione = aggiungiSchedaArticoloMancanti(situazione, anno);

        return situazione;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.NEVER)
    @RolesAllowed("gestioneSchedeArticolo")
    public void creaSchedeArticolo(ParametriCreazioneSchedeArticoli parametriCreazioneSchedeArticoli) {

        for (ArticoloRicerca articolo : parametriCreazioneSchedeArticoli.getArticoli()) {
            Connection connection = null;
            Session session = null;
            MessageProducer producer = null;
            try {
                connection = jmsConnectionFactory.createConnection();
                connection.start();
                session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
                logger.debug("-->Session JMS creata");
                producer = session.createProducer(queue);
                ObjectMessage message = session.createObjectMessage();
                SchedaArticoloDTO schedaArticoloDTO = new SchedaArticoloDTO(parametriCreazioneSchedeArticoli.getAnno(),
                        parametriCreazioneSchedeArticoli.getMese(), articolo,
                        parametriCreazioneSchedeArticoli.getNote(), getAzienda());
                message.setObject(schedaArticoloDTO);
                message.setJMSRedelivered(false);
                logger.debug("--> Spedizione messaggio");
                producer.send(message);
            } catch (JMSException e) {
                logger.error("--> errore, impossibile inviare il messaggio ", e);
                throw new RuntimeException(e);
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (Exception e) {
                    throw new RuntimeException("errore durante la chiusura della connessione", e);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<ArticoloRicerca> filtra(List<ArticoloRicerca> articoli, Map<String, Object> params) {

        Integer meseScheda = (Integer) params.get("meseScheda");
        Integer annoScheda = (Integer) params.get("annoScheda");

        // per performance invece di usare la in con gli id degli articoli passati li carico tutti tanto costruisco gli
        // articoli ricerca solo con l'id per poter poi fare la retainAll sulla collection
        StringBuilder sb = new StringBuilder();
        sb.append("select distinct a.id as id ");
        sb.append("from Articolo a ");
        sb.append(
                "where  a.gestioneSchedaArticolo = true and (a.codiceAzienda = :codiceAzienda and a.gestioneSchedaArticoloAnno < :anno or ( a.gestioneSchedaArticoloAnno = :anno and  a.gestioneSchedaArticoloMese <= :mese)) ");
        sb.append(
                "and a.id not in ( select sa.articolo.id from SchedaArticolo sa  where sa.mese = :mese and sa.anno = :anno)  ");

        Query query = panjeaDAO.prepareQuery(sb.toString(), ArticoloRicerca.class, null);
        query.setParameter("codiceAzienda", getAzienda());
        query.setParameter("mese", meseScheda);
        query.setParameter("anno", annoScheda);

        List<ArticoloRicerca> articoliValidi = new ArrayList<ArticoloRicerca>();
        try {
            articoliValidi = panjeaDAO.getResultList(query);
        } catch (Exception e) {
            logger.error("--> errore durante il caricamento degli articoli validi per le schede articolo", e);
            throw new RuntimeException("errore durante il caricamento degli articoli validi per le schede articolo", e);
        }

        // lascio solo gli articoli che sono validi
        articoli.retainAll(articoliValidi);

        return articoli;
    }

    /**
     *
     * @return codice Azienda loggata
     */
    private String getAzienda() {
        JecPrincipal jecPrincipal = (JecPrincipal) context.getCallerPrincipal();
        return jecPrincipal.getCodiceAzienda();
    }

    /**
     * Restituisce la directory di salvataggio delle stampe delle schede articolo.
     *
     * @return directory
     */
    private String getDirectoryExportPath() {
        String baseDir = "";

        try {
            baseDir = preferenceService.caricaPreference(StampaSchedaArticoloBuilder.OUTPUT_BASE_PATH_KEY).getValore();
        } catch (PreferenceNotFoundException e) {
            logger.error("--> errore durante il caricamento della chiave "
                    + StampaSchedaArticoloBuilder.OUTPUT_BASE_PATH_KEY, e);
        }

        return baseDir;
    }

    /**
     * Verifica se la movimentazione è vuota per la stampa della scheda articolo. La movimentazione è vuota se non ha
     * righe o se tutte le sue righe si riferiscono a inventari con quantità zero.
     *
     * @param movimentazione
     *            movimentazione
     * @return <code>true</code> se vuota
     */
    private boolean isMovimentazioneEmpty(MovimentazioneArticolo movimentazione) {
        if (movimentazione.getRigheMovimentazione().isEmpty()) {
            return true;
        }

        int nrInventarioZero = 0;
        for (RigaMovimentazione riga : movimentazione.getRigheMovimentazione()) {
            if (riga.getTipoOperazione() == 0
                    && (riga.getQtaMagazzinoScaricoTotale() + riga.getQtaMagazzinoCaricoTotale()) == 0.0) {
                nrInventarioZero++;
            }
        }

        return nrInventarioZero == movimentazione.getRigheMovimentazione().size();
    }

    @Override
    @RolesAllowed("gestioneSchedeArticolo")
    public SchedaArticolo salvaSchedaArticolo(SchedaArticolo schedaArticolo) {
        logger.debug("--> Enter salvaSchedaArticolo");

        SchedaArticolo schedaSalvata = null;

        if (schedaArticolo.getId() == null) {
            schedaArticolo.setCodiceAzienda(getAzienda());
        }

        try {
            schedaSalvata = panjeaDAO.save(schedaArticolo);
        } catch (Exception e) {
            logger.error("--> errore durante il salvataggio della scheda articolo", e);
            throw new RuntimeException("errore durante il salvataggio della scheda articolo", e);
        }

        logger.debug("--> Exit salvaSchedaArticolo");
        return schedaSalvata;
    }

}
