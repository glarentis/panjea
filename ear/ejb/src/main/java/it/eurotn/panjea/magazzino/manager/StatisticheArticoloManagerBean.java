package it.eurotn.panjea.magazzino.manager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import commonj.work.Work;
import commonj.work.WorkItem;
import de.myfoo.commonj.util.ThreadPool;
import de.myfoo.commonj.work.FooWorkManager;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.domain.lite.DepositoLite;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.magazzino.domain.Articolo;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.manager.interfaces.GiacenzaManager;
import it.eurotn.panjea.magazzino.manager.interfaces.IndiciRotazioneGiacenzaManager;
import it.eurotn.panjea.magazzino.manager.interfaces.StatisticaArticoloManager;
import it.eurotn.panjea.magazzino.manager.interfaces.StatisticheArticoloManager;
import it.eurotn.panjea.magazzino.util.ArticoloRicerca;
import it.eurotn.panjea.magazzino.util.DisponibilitaArticolo;
import it.eurotn.panjea.magazzino.util.IndiceGiacenzaArticolo;
import it.eurotn.panjea.magazzino.util.StatisticaArticolo;
import it.eurotn.panjea.magazzino.util.StatisticheArticolo;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriCalcoloIndiciRotazioneGiacenza;
import it.eurotn.panjea.parametriricerca.domain.Periodo;
import it.eurotn.panjea.parametriricerca.domain.Periodo.TipoPeriodo;
import it.eurotn.util.PanjeaEJBUtil;

/**
 *
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.StatisticheArticoloManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.StatisticheArticoloManager")
public class StatisticheArticoloManagerBean implements StatisticheArticoloManager {

    private class DisponibilitaWork implements Work {
        private GiacenzaManager giacenzaManager;
        private Articolo articolo;
        private Map<String, Map<Integer, List<DisponibilitaArticolo>>> disponibilita;

        /**
         *
         * @param giacenzaManager
         *            manager giacenza
         * @param articolo
         *            articolo
         */
        public DisponibilitaWork(final GiacenzaManager giacenzaManager, final Articolo articolo) {
            super();
            this.giacenzaManager = giacenzaManager;
            this.articolo = articolo;
        }

        /**
         * @return Returns the disponibilita.
         */
        public Map<String, Map<Integer, List<DisponibilitaArticolo>>> getDisponibilita() {
            return disponibilita;
        }

        @Override
        public boolean isDaemon() {
            return false;
        }

        @Override
        public void release() {
            disponibilita = null;
        }

        @Override
        public void run() {
            // Recupero dispobilità e fabbisogni
            List<Articolo> articoli = new ArrayList<Articolo>();
            articoli.add(articolo);
            disponibilita = giacenzaManager.calcolaDisponibilita(articoli, null, null);
        }

    }

    private class IndiciGiacenzaWork implements Work {

        private int anno;
        private ArticoloLite articolo;
        private List<Deposito> depositi;
        private IndiciRotazioneGiacenzaManager indiciManager;
        private Map<Integer, IndiceGiacenzaArticolo> result;

        public IndiciGiacenzaWork(final int anno, final ArticoloLite articolo,
                final IndiciRotazioneGiacenzaManager indiciManager, final List<Deposito> depositi) {
            super();
            this.anno = anno;
            this.articolo = articolo;
            this.indiciManager = indiciManager;
            this.depositi = depositi;
        }

        /**
         * @return Returns the result.
         */
        public Map<Integer, IndiceGiacenzaArticolo> getResult() {
            return result;
        }

        @Override
        public boolean isDaemon() {
            return false;
        }

        @Override
        public void release() {

        }

        @Override
        public void run() {
            ParametriCalcoloIndiciRotazioneGiacenza parametri = new ParametriCalcoloIndiciRotazioneGiacenza();
            parametri.setDepositi(depositi);

            ArticoloRicerca articoloRicerca = new ArticoloRicerca();
            PanjeaEJBUtil.copyProperties(articoloRicerca, articolo);
            List<ArticoloRicerca> articoli = new ArrayList<ArticoloRicerca>();
            articoli.add(articoloRicerca);
            parametri.setArticoli(articoli);

            Periodo periodo = new Periodo();
            periodo.setTipoPeriodo(TipoPeriodo.DATE);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date startDate = format.parse(anno + "-01-01");
                periodo.setDataIniziale(startDate);

                Date endDate = format.parse(anno + "-12-31");
                periodo.setDataFinale(endDate);
                parametri.setPeriodo(periodo);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            List<IndiceGiacenzaArticolo> resultList = indiciManager.calcolaIndiciRotazione(parametri);
            result = new HashMap<Integer, IndiceGiacenzaArticolo>();
            for (IndiceGiacenzaArticolo indiceGiacenzaArticolo : resultList) {
                result.put(indiceGiacenzaArticolo.getDeposito().getId(), indiceGiacenzaArticolo);
            }
        }
    }

    private class StatisticheArticoloWork implements Work {

        private StatisticaArticoloManager statisticaArticoloManager;

        private StatisticaArticolo statisticaArticolo;

        private DepositoLite deposito;
        private Integer anno;
        private Integer idArticolo;

        /**
         * Costruttore.
         *
         * @param statisticaArticoloManager
         *            manager delle statistiche
         * @param idArticolo
         *            id articolo
         * @param anno
         *            anno
         * @param deposito
         *            deposito
         */
        public StatisticheArticoloWork(final StatisticaArticoloManager statisticaArticoloManager,
                final Integer idArticolo, final Integer anno, final DepositoLite deposito) {
            super();
            this.statisticaArticoloManager = statisticaArticoloManager;
            this.idArticolo = idArticolo;
            this.anno = anno;
            this.deposito = deposito;
        }

        /**
         * @return the deposito
         */
        public DepositoLite getDeposito() {
            return deposito;
        }

        /**
         * @return the statisticaArticolo
         */
        public StatisticaArticolo getStatisticaArticolo() {
            return statisticaArticolo;
        }

        @Override
        public boolean isDaemon() {
            return false;
        }

        @Override
        public void release() {
            statisticaArticolo = null;
        }

        @Override
        public void run() {
            statisticaArticolo = statisticaArticoloManager.caricaStatisticaArticolo(idArticolo, anno, deposito.getId());
        }
    }

    private static Logger logger = Logger.getLogger(StatisticheArticoloManagerBean.class);

    @EJB
    private GiacenzaManager giacenzaManager;

    @EJB
    private IndiciRotazioneGiacenzaManager indiciRotazioneGiacenzaManager;

    @EJB
    private StatisticaArticoloManager statisticaArticoloManager;

    @EJB
    private DepositiManager depositiManager;

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    @Override
    public StatisticheArticolo caricaStatisticheArticolo(Articolo articolo, Integer anno) {

        List<Deposito> depositi = depositiManager.caricaDepositi();
        List<StatisticaArticolo> result = new ArrayList<StatisticaArticolo>();

        // create the thread pool for this work manager
        ThreadPool pool = new ThreadPool(10, 20, 500);

        // create the work manager
        FooWorkManager workManager = new FooWorkManager(pool, 0);

        List<WorkItem> statisticheWorkItem = new ArrayList<>();

        for (Deposito deposito : depositi) {
            try {
                statisticheWorkItem.add(workManager.schedule(new StatisticheArticoloWork(statisticaArticoloManager,
                        articolo.getId(), anno, deposito.getDepositoLite())));
            } catch (Exception e) {
                logger.error("--> errore nel lanciare il task per la statistica dell'articolo " + articolo.getId(), e);
                throw new RuntimeException(
                        "errore nel lanciare il task per la statistica dell'articolo " + articolo.getId(), e);
            }
        }

        DisponibilitaWork dispWork = new DisponibilitaWork(giacenzaManager, articolo);
        IndiciGiacenzaWork rotazioneWork = new IndiciGiacenzaWork(anno, articolo.getArticoloLite(),
                indiciRotazioneGiacenzaManager, depositi);
        try {
            statisticheWorkItem.add(workManager.schedule(dispWork));
            statisticheWorkItem.add(workManager.schedule(rotazioneWork));
        } catch (Exception e) {
            logger.error("--> errore nel lanciare il task per la disponibilità dell'articolo " + articolo.getId(), e);
            throw new RuntimeException(
                    "errore nel lanciare il task per la disponibilità dell'articolo " + articolo.getId(), e);
        }

        try {
            workManager.waitForAll(statisticheWorkItem, 120000);
            for (WorkItem workItem : statisticheWorkItem) {
                if (workItem.getResult() instanceof StatisticheArticoloWork) {
                    StatisticheArticoloWork statWork = (StatisticheArticoloWork) workItem.getResult();
                    StatisticaArticolo statistica = statWork.getStatisticaArticolo();

                    // se la statistica non ha valori setto il deposito e l'articolo e lascio i
                    // valori a 0
                    if (statistica.isNew()) {
                        statistica.setIdArticolo(articolo.getId());
                        statistica.setDepositoLite(statWork.getDeposito());
                    }

                    result.add(statWork.getStatisticaArticolo());
                }
            }
        } catch (Exception e) {
            logger.error("-->errore nell'aspettare i processi per la valorizzazione dei depositi", e);
            throw new RuntimeException("-->errore nell'aspettare i processi per la valorizzazione dei depositi ", e);
        } finally {
            workManager.shutdown();
        }

        return new StatisticheArticolo(articolo.getId(), result, dispWork.getDisponibilita(),
                rotazioneWork.getResult());
    }
}