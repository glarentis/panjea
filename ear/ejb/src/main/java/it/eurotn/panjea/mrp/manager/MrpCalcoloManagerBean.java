/**
 *
 */
package it.eurotn.panjea.mrp.manager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;
import org.joda.time.DateTime;
import org.joda.time.Days;

import commonj.work.WorkException;
import commonj.work.WorkItem;
import de.myfoo.commonj.util.ThreadPool;
import de.myfoo.commonj.work.FooWorkManager;
import it.eurotn.dao.exception.DAOException;
import it.eurotn.panjea.magazzino.domain.Giacenza;
import it.eurotn.panjea.magazzino.exception.FormulaMrpCalcoloArticoloException;
import it.eurotn.panjea.mrp.domain.ArticoloAnagrafica;
import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.domain.OrdiniFornitoreCalcolo;
import it.eurotn.panjea.mrp.domain.RigheCalcolo;
import it.eurotn.panjea.mrp.domain.RigheOrdinatoCliente;
import it.eurotn.panjea.mrp.domain.RisultatoMRPArticoloBucket;
import it.eurotn.panjea.mrp.manager.interfaces.MrpBomExplosionManager;
import it.eurotn.panjea.mrp.manager.interfaces.MrpCalcoloManager;
import it.eurotn.panjea.mrp.manager.interfaces.MrpManager;
import it.eurotn.panjea.mrp.manager.work.ArticoloAnagraficaWork;
import it.eurotn.panjea.mrp.manager.work.CalendarioWork;
import it.eurotn.panjea.mrp.manager.work.GiacenzaWork;
import it.eurotn.panjea.mrp.manager.work.MrpProcessor;
import it.eurotn.panjea.mrp.manager.work.OrdinatoFornitoreCalcoloWork;
import it.eurotn.panjea.mrp.manager.work.RigaOrdinatoClienteWork;
import it.eurotn.panjea.mrp.manager.work.RigaOrdinatoSottoscortaWork;
import it.eurotn.panjea.mrp.util.ArticoloConfigurazioneKey;
import it.eurotn.panjea.mrp.util.ArticoloDepositoConfigurazioneKey;
import it.eurotn.panjea.mrp.util.ArticoloDepositoKey;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;

/**
 * @author leonardo
 */
@Stateless(name = "Panjea.MrpCalcoloManager")
@SecurityDomain("PanjeaLoginModule")
@LocalBinding(jndiBinding = "Panjea.MrpCalcoloManager")
@PermitAll
public class MrpCalcoloManagerBean implements MrpCalcoloManager {

    private static Logger logger = Logger.getLogger(MrpCalcoloManagerBean.class);

    @EJB
    private MrpManager mrpManager;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private MrpBomExplosionManager bomExplosionManager;

    @Override
    public void calcola(int numBucket, Date startDate, Integer idAreaOrdine) {
        logger.trace("Calcolo MRP dalla data "
                + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(startDate) + " per "
                + numBucket + " giorni");
        long startTime = System.currentTimeMillis();
        int numTime = numBucket + BUCKET_ZERO;

        Date today = new Date();
        DateTime startDateTime = new DateTime(startDate.getTime());
        DateTime todayDateTime = new DateTime(today.getTime());
        int posizioneAdOggi = Days.daysBetween(startDateTime, todayDateTime).getDays();
        posizioneAdOggi = posizioneAdOggi + MrpCalcoloManager.BUCKET_ZERO;

        if (numTime < posizioneAdOggi) {
            throw new ArrayIndexOutOfBoundsException(posizioneAdOggi);
        }

        // la giacenza istanzia più work per ogni deposito, calcolo quindi la
        // giacenza e recupero i risultati per
        // evitare di avere risultati diversi nel'array di workItem da calcolare

        // create the thread pool for this work manager
        ThreadPool pool = new ThreadPool(15, 20, 100);

        // create the work manager
        FooWorkManager workManager = new FooWorkManager(pool, 0);

        GiacenzaWork giacenzaWork = new GiacenzaWork(Calendar.getInstance().getTime(), mrpManager,
                workManager);
        WorkItem giacenzaWorkItem = null;
        try {
            giacenzaWorkItem = workManager.schedule(giacenzaWork);
        } catch (IllegalArgumentException | WorkException e) {
            logger.error("-->errore nel calcolare le giacenze nel calcolo dell'mrp", e);
            throw new RuntimeException(e);
        }

        List<WorkItem> waitItems = new ArrayList<WorkItem>();

        waitItems.add(giacenzaWorkItem);

        ArticoloAnagraficaWork articoliAnagraficaWork = new ArticoloAnagraficaWork(mrpManager);
        WorkItem articoliAnagraficaWorkItem = null;
        try {
            articoliAnagraficaWorkItem = workManager.schedule(articoliAnagraficaWork);
        } catch (IllegalArgumentException | WorkException e) {
            logger.error("-->errore nel caricare le anagrafiche articolo", e);
            throw new RuntimeException(e);
        }
        waitItems.add(articoliAnagraficaWorkItem);

        RigaOrdinatoClienteWork rigaOrdinatoClienteWork = new RigaOrdinatoClienteWork(startDate,
                numTime, mrpManager);
        WorkItem rigaOrdinatoClienteWorkItem = null;
        try {
            rigaOrdinatoClienteWorkItem = workManager.schedule(rigaOrdinatoClienteWork);
        } catch (IllegalArgumentException | WorkException e) {
            e.printStackTrace();
        }
        waitItems.add(rigaOrdinatoClienteWorkItem);

        // Ordini fornitori con i link ai vari ordini clienti/produzione per
        // eliminare le qta gia impegnate
        OrdinatoFornitoreCalcoloWork ordiniFornitoreCalcoloWork = new OrdinatoFornitoreCalcoloWork(
                startDate, numTime, mrpManager);
        WorkItem ordiniFornitoreWorkItem = null;
        try {
            ordiniFornitoreWorkItem = workManager.schedule(ordiniFornitoreCalcoloWork);
        } catch (IllegalArgumentException | WorkException e) {
            logger.error("-->errore nel caricare l'ordinato cliente", e);
            throw new RuntimeException(e);
        }
        waitItems.add(ordiniFornitoreWorkItem);

        CalendarioWork calendarioWork = new CalendarioWork(startDate, numTime);
        WorkItem calendarioWorkItem = null;
        try {
            calendarioWorkItem = workManager.schedule(calendarioWork);
        } catch (IllegalArgumentException | WorkException e) {
            logger.error("-->errore nel caricare i calendari", e);
            throw new RuntimeException(e);
        }

        waitItems.add(calendarioWorkItem);
        // recupera tutti i risultati

        try {
            workManager.waitForAll(waitItems, 40000);
        } catch (IllegalArgumentException | InterruptedException e) {
            logger.error("-->errore nell'aspettare la concl. dei vari processi", e);
            throw new RuntimeException(e);
        }

        Map<ArticoloDepositoKey, Giacenza> giacenze = giacenzaWork.getGiacenze();
        Map<Integer, ArticoloAnagrafica> articoliAnagrafica = articoliAnagraficaWork
                .getArticoliAnagrafica();

        Map<ArticoloDepositoKey, List<OrdiniFornitoreCalcolo>> ordiniFornitoreCalcolo = ordiniFornitoreCalcoloWork
                .getOrdinatiFornitoreProduzione();
        boolean[] calendarWork = calendarioWork.getGiorniLavorativi();

        waitItems.clear();

        RigaOrdinatoSottoscortaWork rigaOrdinatoSottoscortaWork = new RigaOrdinatoSottoscortaWork(
                giacenze, startDate, numTime, articoliAnagrafica);
        WorkItem rigaOrdinatoSottoscortaWorkItem = null;
        try {
            rigaOrdinatoSottoscortaWorkItem = workManager.schedule(rigaOrdinatoSottoscortaWork);
        } catch (IllegalArgumentException | WorkException e) {
            logger.error("-->errore nel caricare il sottoscorta", e);
            throw new RuntimeException(e);
        }

        waitItems.add(rigaOrdinatoSottoscortaWorkItem);

        try {
            workManager.waitForAll(waitItems, 20000);
        } catch (IllegalArgumentException | InterruptedException e) {
            logger.error("-->errore nell'aspettare il terminte del caricamento dei sottoscorta", e);
            throw new RuntimeException(e);
        }

        RigheOrdinatoCliente righeSottoScorta = rigaOrdinatoSottoscortaWork.getRigheSottoScorta();

        RigheOrdinatoCliente righeOrdinatoCliente = rigaOrdinatoClienteWork
                .getRigheOrdinatoCliente();
        Set<Integer> confUtilizzate = new HashSet<Integer>(
                righeOrdinatoCliente.getConfigurazioniDistintaUtilizzate());
        confUtilizzate.remove(null);

        // Collection<Set<Integer>> confUtilizzatePerConfigurazione =
        // righeOrdinatoCliente
        // .getConfigurazioniDistintaUtilizzate().values();
        // for (Set<Integer> confPerConf : confUtilizzatePerConfigurazione) {
        // confUtilizzate.addAll(confPerConf);
        // }
        Map<ArticoloConfigurazioneKey, Bom> boms = bomExplosionManager.esplodiBoms(confUtilizzate);

        if (logger.isDebugEnabled()) {
            logger.debug("****DATI PER IL CALCOLO CARICATI*****");
            logger.debug("Tempo impiegato {" + (System.currentTimeMillis() - startTime) + "}");
            logger.debug("Bom esplose {" + boms.size() + "}");
            logger.debug("Prodotti valorizzati {" + giacenze.size() + "}");
            logger.debug("Righe Prodotti ordinati cliente {"
                    + righeOrdinatoCliente.getRigheOrdinateArticoloDeposito().size() + "}");
            logger.debug("Righe scorta minima {"
                    + righeSottoScorta.getRigheOrdinateArticoloDeposito().size() + "}");
            logger.debug("Anagrafica {" + articoliAnagrafica.size() + "}");
            logger.debug("Ordinato fornitore calcolo {" + ordiniFornitoreCalcolo.size() + "}");
            logger.debug("****INIZIO CALCOLO MATERIALE RICHIESTO*****");
        }

        startTime = System.currentTimeMillis();

        Map<ArticoloDepositoConfigurazioneKey, RigheCalcolo[]> risultati = new HashMap<>();
        try {
            MrpProcessor processor = new MrpProcessor(workManager, righeOrdinatoCliente, boms,
                    articoliAnagrafica, giacenze, calendarWork, startDate, numTime,
                    ordiniFornitoreCalcolo, idAreaOrdine);
            risultati = processor.calcola();
            // risultatiProduzione =
            // mrpRigheOrdinatoProduzioneProcessor.calcola(risultatiProduzione,
            // ordinati);
            // risultatiSottoScorta =
            // mrpRigheOrdinatoSottoScortaProcessor.calcola(risultatiSottoScorta,
            // sottoScorta);
        } catch (Exception e) {
            if (e.getCause().getCause() instanceof FormulaMrpCalcoloArticoloException) {
                Integer idDistinta = ((FormulaMrpCalcoloArticoloException) e.getCause().getCause())
                        .getIdDistinta();
                Query query = panjeaDAO
                        .prepareQuery("select codice from Articolo where id=:idArticolo");
                query.setParameter("idArticolo", idDistinta);
                String codiceDistinta = "";
                try {
                    codiceDistinta = (String) panjeaDAO.getSingleResult(query);
                } catch (DAOException e1) {
                    logger.error(
                            "-->errore nel recuperare il codice distinta che ha generato errore nel calcolo della formula",
                            e);
                }

                Integer idComponente = ((FormulaMrpCalcoloArticoloException) e.getCause()
                        .getCause()).getIdComponente();
                query.setParameter("idArticolo", idComponente);
                String codiceComponente = "";
                try {
                    codiceComponente = (String) panjeaDAO.getSingleResult(query);
                } catch (DAOException e1) {
                    logger.error(
                            "-->errore nel recuperare il codice componente che ha generato errore nel calcolo della formula",
                            e);
                }
                ((FormulaMrpCalcoloArticoloException) e.getCause().getCause())
                        .setCodComponente(codiceComponente);
                ((FormulaMrpCalcoloArticoloException) e.getCause().getCause())
                        .setCodDistinta(codiceDistinta);
                throw e;
            }
        }

        logger.trace(
                "Tempo calcolo articoli {" + (System.currentTimeMillis() - startTime) + "} msec");
        logger.trace("Numero risultati articoli {" + risultati.size() + "}");

        salvaRisultati(risultati, numTime, idAreaOrdine);
    }

    /**
     * Se ho fitlrato per un ordine rimuovo le righe che non sono collegate all'ordine
     *
     * @param righeCalcolo
     * @return
     */
    private Collection<? extends RisultatoMRPArticoloBucket> creaRisultatiDaSalvare(
            RigheCalcolo righeCalcolo, Integer idAreaOrdineFiltrata) {
        List<RisultatoMRPArticoloBucket> risultatiMRPArticoloBucket = new ArrayList<>();
        for (RisultatoMRPArticoloBucket risultatoMRPArticoloBucket : righeCalcolo
                .getRisultatiMRPArticoloBucket()) {
            boolean aggiungi = idAreaOrdineFiltrata == null
                    || idAreaOrdineFiltrata == risultatoMRPArticoloBucket.getIdOrdineCliente();
            if (aggiungi) {
                risultatiMRPArticoloBucket.add(risultatoMRPArticoloBucket);
            }
        }
        return risultatiMRPArticoloBucket;
    }

    /**
     * @param risultati
     *            risultatiOrdiniCliente
     * @param risultatiOrdiniProduzione
     *            risultatiOrdiniProduzione
     * @param risultatiSottoScorta
     *            risultatiSottoScorta
     * @param numTime
     *            orizzonte temporale
     */
    private void salvaRisultati(Map<ArticoloDepositoConfigurazioneKey, RigheCalcolo[]> risultati,
            int numTime, Integer idAreaOrdineFiltrata) {
        Collection<RigheCalcolo[]> articoliCalcolati = risultati.values();

        List<RisultatoMRPArticoloBucket> risultatiToSave = new ArrayList<>();
        for (RigheCalcolo[] righeCalcolate : articoliCalcolati) {
            for (int i = 0; i < numTime; i++) {
                if (righeCalcolate[i] != null) {
                    risultatiToSave.addAll(
                            creaRisultatiDaSalvare(righeCalcolate[i], idAreaOrdineFiltrata));
                }
            }
        }
        // for (ArticoloCalcoloWork articoloCalcolo : articoliSottocorta) {
        // RigheCalcolo[] righeCalcolate = articoloCalcolo.getRisultato();
        // for (int i = 0; i < numTime; i++) {
        // if (righeCalcolate[i] != null) {
        // risultatiToSave.addAll(righeCalcolate[i].getRisultatiMRPArticoloBucket());
        // }
        // }
        // }

        Collections.sort(risultatiToSave, new Comparator<RisultatoMRPArticoloBucket>() {
            @Override
            public int compare(RisultatoMRPArticoloBucket o1, RisultatoMRPArticoloBucket o2) {
                return new Integer(o1.getOrdinamento()).compareTo(o2.getOrdinamento());
            }
        });
        try {
            mrpManager.salvaRisultatoMRP(risultatiToSave);
        } catch (Exception e) {
            logger.error("Errore nel salvare i risultati", e);
            throw new RuntimeException("Errore nel salvare i risultati", e);
        }
    }

}
