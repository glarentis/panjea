package it.eurotn.panjea.magazzino.manager.articolo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;

import commonj.work.Work;
import commonj.work.WorkException;
import commonj.work.WorkItem;
import commonj.work.WorkManager;
import it.eurotn.panjea.anagrafica.domain.Deposito;
import it.eurotn.panjea.anagrafica.manager.depositi.interfaces.DepositiManager;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.manager.interfaces.MagazzinoValorizzazioneManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.DistintaBaseManager;
import it.eurotn.panjea.magazzino.manager.interfaces.articolo.ValorizzazioneDistintaBaseManager;
import it.eurotn.panjea.magazzino.util.ParametriRicercaValorizzazione;
import it.eurotn.panjea.magazzino.util.ValorizzazioneArticolo;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriValorizzazioneDistinte;
import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.manager.interfaces.MrpBomExplosionManager;
import it.eurotn.panjea.mrp.util.ArticoloConfigurazioneKey;

@Stateless(name = "Panjea.ValorizzazioneDistintBaseManager")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.ValorizzazioneDistintBaseManager")
public class ValorizzazioneDistintBaseManagerBean implements ValorizzazioneDistintaBaseManager {

    private class CaricaAnagraficaArticoloWork implements Work {

        private Map<Integer, ArticoloLite> articoli;

        /**
         * @return Returns the articoli.
         */
        public Map<Integer, ArticoloLite> getArticoli() {
            return articoli;
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
            articoli = disintaBaseManager.caricaArticoliComponenti();
        }
    }

    private class CaricaArticoliWork implements Work {

        private Map<Integer, ArticoloValorizzazioneBOM> articoli;

        /**
         *
         * @return articoli per la valorizzazione.
         */
        public Map<Integer, ArticoloValorizzazioneBOM> getArticoli() {
            return articoli;
        }

        @Override
        public boolean isDaemon() {
            return false;
        }

        @Override
        public void release() {
            articoli = null;
        }

        @Override
        public void run() {
            logger.debug("--> Enter run calcolo CaricaArticoliWork " + Thread.currentThread().getName());
            articoli = new HashMap<Integer, ArticoloValorizzazioneBOM>();
            try {
                List<ArticoloValorizzazioneBOM> articoliList = disintaBaseManager.caricaArticoliValorizzazioneBOM();
                for (ArticoloValorizzazioneBOM articoloValorizzazioneBOM : articoliList) {
                    articoli.put(articoloValorizzazioneBOM.getIdArticolo(), articoloValorizzazioneBOM);
                }
            } catch (Exception e) {
                logger.error("-->errore ", e);
            }
            logger.debug("--> Exit run calcolo CaricaArticoliWork " + Thread.currentThread().getName());
        }

    }

    private class EsplodiBOMWork implements Work {
        private Map<ArticoloConfigurazioneKey, Bom> boms;
        private ArrayList<ArticoloLite> foglie;

        public Map<ArticoloConfigurazioneKey, Bom> getBoms() {
            return boms;
        }

        /**
         * @return Returns the foglie.
         */
        public ArrayList<ArticoloLite> getFoglie() {
            return foglie;
        }

        @Override
        public boolean isDaemon() {
            return false;
        }

        @Override
        public void release() {
            boms = null;
            foglie = null;
        }

        @Override
        public void run() {
            logger.debug("--> Enter run esplosioneBom " + Thread.currentThread().getName());
            boms = bomExplosionManager.esplodiBoms();
            // trovo le foglie
            foglie = new ArrayList<ArticoloLite>();
            for (Bom bom : boms.values()) {
                if (bom.getFigli().isEmpty()) {
                    ArticoloLite articolo = new ArticoloLite();
                    articolo.setId(bom.getIdDistinta());
                    foglie.add(articolo);
                }
            }
            logger.debug("--> Exit run esplosioneBom " + Thread.currentThread().getName());
        }

    }

    private static Logger logger = Logger.getLogger(ValorizzazioneDistintBaseManagerBean.class);

    @EJB
    private MrpBomExplosionManager bomExplosionManager;

    @EJB
    private MagazzinoValorizzazioneManager valorizzazioneManager;

    @EJB
    private DistintaBaseManager disintaBaseManager;

    @EJB
    private DepositiManager depositiManager;

    @Resource(mappedName = "java:worker/mrpWorker")
    private WorkManager workManager;

    private List<Work> creaPadriDaElaborare(Bom bomElaborata, Map<ArticoloConfigurazioneKey, Bom> result,
            Map<ArticoloConfigurazioneKey, Bom> boms,
            Map<Integer, ArticoloValorizzazioneBOM> articoliValorizzazioneBom) {
        List<Work> padriDaElaborare = new ArrayList<Work>();
        for (Bom padre : bomElaborata.getPadri()) {
            ArticoloConfigurazioneKey key = new ArticoloConfigurazioneKey(padre.getIdDistinta(), null);
            Bom bomPadre = boms.get(key);
            boolean calcolaPadre = true;
            for (Bom figlio : bomPadre.getFigli()) {
                ArticoloConfigurazioneKey keyFiglio = new ArticoloConfigurazioneKey(figlio.getIdArticolo(), null);
                if (!result.containsKey(keyFiglio)) {
                    calcolaPadre = false;
                    break;
                }
            }
            if (calcolaPadre) {
                padriDaElaborare.add(new ValorizzazioneBOMWork(bomPadre, result,
                        articoliValorizzazioneBom.get(bomPadre.getIdDistinta())));
            }
        }
        return padriDaElaborare;
    }

    @Override
    public Map<ArticoloConfigurazioneKey, Bom> valorizzaDistinte(
            ParametriValorizzazioneDistinte parametriValorizzazioneDistinte) {
        logger.debug("--> Enter valorizzaDistinte");

        CaricaArticoliWork caricaArticoloWork = new CaricaArticoliWork();
        EsplodiBOMWork esplodiBOMWork = new EsplodiBOMWork();
        CaricaAnagraficaArticoloWork caricaAnagraficaArticoloWork = new CaricaAnagraficaArticoloWork();

        ArrayList<ArticoloLite> articoliToFilter = null;
        Map<ArticoloConfigurazioneKey, Bom> boms = null;
        Map<Integer, ArticoloValorizzazioneBOM> articoliValorizzazioneBom = null;
        Map<Integer, ArticoloLite> articoliAnagrafica;

        try {

            WorkItem caricaArticoloWorkItem = workManager.schedule(caricaArticoloWork);
            WorkItem esplodiBOMWorkItem = workManager.schedule(esplodiBOMWork);
            WorkItem caricaAnagraficaArticoloWorkItem = workManager.schedule(caricaAnagraficaArticoloWork);

            List<WorkItem> waitItems = new ArrayList<WorkItem>();
            waitItems.add(esplodiBOMWorkItem);
            waitItems.add(caricaArticoloWorkItem);
            waitItems.add(caricaAnagraficaArticoloWorkItem);

            workManager.waitForAll(waitItems, 1200000);
            articoliToFilter = esplodiBOMWork.getFoglie();
            boms = esplodiBOMWork.getBoms();
            articoliValorizzazioneBom = caricaArticoloWork.getArticoli();
            articoliAnagrafica = caricaAnagraficaArticoloWork.getArticoli();
        } catch (Exception e) {
            logger.error("-->errore nell'esplodere le disinte o caricare gli articoli", e);
            throw new RuntimeException(e);
        }

        ParametriRicercaValorizzazione parametriRicercaValorizzazione = parametriValorizzazioneDistinte
                .creaParametriValorizzazione();
        parametriRicercaValorizzazione.setArticoliLite(articoliToFilter);
        List<Deposito> depositi = new ArrayList<Deposito>();
        depositi.add(depositiManager.caricaDepositoPrincipale());
        parametriRicercaValorizzazione.setDepositi(depositi);
        parametriRicercaValorizzazione.setConsideraGiacenzaZero(true);
        parametriRicercaValorizzazione.setConsideraMovimentatiZero(true);

        List<ValorizzazioneArticolo> valorizzazioneResult = valorizzazioneManager
                .caricaValorizzazione(parametriRicercaValorizzazione);

        Map<ArticoloConfigurazioneKey, Bom> result = Collections
                .synchronizedMap(new HashMap<ArticoloConfigurazioneKey, Bom>());
        List<WorkItem> workers = new ArrayList<WorkItem>();

        for (ValorizzazioneArticolo valorizzazioneArticolo : valorizzazioneResult) {
            ArticoloConfigurazioneKey key = new ArticoloConfigurazioneKey(valorizzazioneArticolo.getArticolo().getId(),
                    null);
            Bom bomDaElaborare = boms.get(key);
            bomDaElaborare.setCosto(valorizzazioneArticolo.getCosto());
            result.put(key, bomDaElaborare);

            List<Work> padriDaElaborare = creaPadriDaElaborare(bomDaElaborare, result, boms, articoliValorizzazioneBom);
            for (Work work : padriDaElaborare) {
                try {
                    WorkItem workItem = workManager.schedule(work);
                    workers.add(workItem);
                } catch (IllegalArgumentException | WorkException e) {
                    e.printStackTrace();
                }
            }
        }

        boolean workersDone = false;
        while (!workersDone) {
            try {
                @SuppressWarnings("unchecked")
                Collection<WorkItem> workerFinished = workManager.waitForAny(workers, 200);
                workers.removeAll(workerFinished);
                for (WorkItem workItem : workerFinished) {
                    Bom bomElaborata = ((ValorizzazioneBOMWork) workItem.getResult()).getBom();
                    result.put(new ArticoloConfigurazioneKey(bomElaborata.getIdDistinta(), null), bomElaborata);

                    List<Work> padriDaElaborare = creaPadriDaElaborare(bomElaborata, result, boms,
                            articoliValorizzazioneBom);
                    for (Work work : padriDaElaborare) {
                        try {
                            workItem = workManager.schedule(work);
                        } catch (IllegalArgumentException | WorkException e) {
                            e.printStackTrace();
                        }
                        workers.add(workItem);
                    }
                }
                workersDone = workers.isEmpty();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Rimuovo gli articoli che non ho richiesto

        // Creo l'albero
        Map<ArticoloConfigurazioneKey, Bom> mappaDTO = new HashMap<>();
        for (Bom bom : result.values()) {
            ArticoloLite articolo = articoliAnagrafica.get(bom.getIdDistinta());
            // if (articolo.isDistinta()) {
            bom.setArticolo(articolo);
            ArticoloConfigurazioneKey keyPadre = new ArticoloConfigurazioneKey(bom.getIdDistinta(), null);
            mappaDTO.put(keyPadre, bom);

            for (Bom figlio : bom.getFigli()) {
                Bom bomFiglio = boms.get(new ArticoloConfigurazioneKey(figlio.getIdArticolo(), null));
                articolo = articoliAnagrafica.get(figlio.getIdArticolo());
                bomFiglio.setArticolo(articolo);
                figlio.setArticolo(articolo);
                // figlio.getFigli().clear();
                // figlio.addFigli(bomFiglio.getFigli());
                // figlio.setCosto(bom.getCosto());
                // figlio.setMoltiplicatore(bom.getMoltiplicatore());
                // PanjeaEJBUtil.copyProperties(figlio, bomFiglio);

                // Bom figlioDto = mappaDTO.get(new ArticoloConfigurazioneKey(bomFiglio.getIdDistinta(), null));
                // bomFiglio.addFiglio(figlioDto);
                // bomPadre.addFiglio(bomFiglio);
            }
        }
        // }
        return mappaDTO;
    }
}
