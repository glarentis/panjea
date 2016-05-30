/**
 *
 */
package it.eurotn.panjea.mrp.manager.work;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;

import commonj.work.WorkItem;
import commonj.work.WorkManager;
import it.eurotn.panjea.magazzino.domain.Giacenza;
import it.eurotn.panjea.mrp.domain.ArticoloAnagrafica;
import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.domain.OrdiniFornitoreCalcolo;
import it.eurotn.panjea.mrp.domain.RigheCalcolo;
import it.eurotn.panjea.mrp.domain.RigheOrdinatoCliente;
import it.eurotn.panjea.mrp.util.ArticoloConfigurazioneKey;
import it.eurotn.panjea.mrp.util.ArticoloDepositoConfigurazioneKey;
import it.eurotn.panjea.mrp.util.ArticoloDepositoKey;
import it.eurotn.panjea.mrp.util.RisultatoPadre;

/**
 * @author leonardo
 */
public class MrpProcessor {

    protected static Logger logger = Logger.getLogger(MrpProcessor.class);

    protected WorkManager workManager = null;

    protected RigheOrdinatoCliente righeOrdinatoCliente = null;
    protected Map<ArticoloConfigurazioneKey, Bom> boms = null;
    protected Map<Integer, ArticoloAnagrafica> articoliAnagrafica = null;
    protected Map<ArticoloDepositoKey, Giacenza> giacenze = null;
    protected boolean[] calendarWork = null;
    protected Date startDate = null;
    protected int numTime = 0;

    private Map<ArticoloDepositoKey, List<OrdiniFornitoreCalcolo>> ordiniFornitoreCalcolo;

    private Integer idAreaOrdineFiltrata;

    /**
     * Costruttore.
     *
     * @param workManager
     *            workManager
     * @param righeOrdinatoCliente
     *            righeOrdinatoCliente
     * @param boms
     *            boms
     * @param articoliAnagrafica
     *            articoliAnagrafica
     * @param giacenze
     *            giacenze
     * @param calendarWork
     *            calendarWork
     * @param startDate
     *            startDate
     * @param numTime
     *            numTime
     * @param ordiniFornitoreCalcolo
     *            ordiniFornitoreCalcolo
     * @param idAreaOrdine
     *            area ordine da calcolare. Null per calcolare tutti gli ordini
     */
    public MrpProcessor(final WorkManager workManager, final RigheOrdinatoCliente righeOrdinatoCliente,
            final Map<ArticoloConfigurazioneKey, Bom> boms, final Map<Integer, ArticoloAnagrafica> articoliAnagrafica,
            final Map<ArticoloDepositoKey, Giacenza> giacenze, final boolean[] calendarWork, final Date startDate,
            final int numTime, final Map<ArticoloDepositoKey, List<OrdiniFornitoreCalcolo>> ordiniFornitoreCalcolo,
            final Integer idAreaOrdine) {
        super();
        this.workManager = workManager;
        this.righeOrdinatoCliente = righeOrdinatoCliente;
        this.boms = boms;
        this.articoliAnagrafica = articoliAnagrafica;
        this.giacenze = giacenze;
        this.calendarWork = calendarWork;
        this.startDate = startDate;
        this.numTime = numTime;
        this.ordiniFornitoreCalcolo = ordiniFornitoreCalcolo;
        this.idAreaOrdineFiltrata = idAreaOrdine;
    }

    /**
     * Calcolo.
     *
     * @return Map<ArticoloDepositoConfigurazioneKey, ArticoloCalcoloWork>
     */
    @SuppressWarnings("unchecked")
    public Map<ArticoloDepositoConfigurazioneKey, RigheCalcolo[]> calcola() {
        Map<ArticoloDepositoConfigurazioneKey, RigheCalcolo[]> risultati = Collections
                .synchronizedMap(new HashMap<ArticoloDepositoConfigurazioneKey, RigheCalcolo[]>());
        try {
            List<WorkItem> processati = new ArrayList<>();
            try {
                // Creo gli articoli da calcolare. La classe che si prende
                // carico del calcolo è ArticoloCalcolo che
                // "raccoglie" tutte le informazioni utili per articolo/deposito
                for (Entry<ArticoloDepositoKey, RigheCalcolo[]> righeDaCalcolare : righeOrdinatoCliente
                        .getRigheOrdinateArticoloDeposito().entrySet()) {
                    ArticoloDepositoKey chiave = righeDaCalcolare.getKey();
                    ArticoloAnagrafica articoloAnagrafica = articoliAnagrafica.get(chiave.getIdArticolo());

                    if (articoloAnagrafica != null) {
                        if (elaboraDistintaCheck(chiave.getIdArticolo(), chiave.getIdDeposito(), risultati)) {
                            Giacenza giacenza = ObjectUtils.defaultIfNull(giacenze.get(chiave), new Giacenza(0.0, 0.0));
                            if (logger.isDebugEnabled()) {
                                logger.debug("--> Giacenza articolo " + chiave + ":" + giacenza);
                            }

                            ArticoloCalcoloWork articoloCalcoloWork = createArticoloCalcoloWork(chiave.getIdDeposito(),
                                    chiave.getIdArticolo(), giacenza, righeDaCalcolare.getValue(), numTime,
                                    articoloAnagrafica, calendarWork, null);
                            WorkItem articoloWorkItem = workManager.schedule(articoloCalcoloWork);
                            processati.add(articoloWorkItem);
                        }
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                logger.error("Intervallo di calcolo insufficiente", e);
                throw e;
            } catch (Exception e) {
                logger.error("Errore nel calcolo mrp", e);
                throw new RuntimeException("Errore nelcalcolo mrp", e);
            }

            boolean workersDone = false;
            while (!workersDone) {
                Collection<WorkItem> workerFinished = workManager.waitForAny(processati, 10000);
                processati.removeAll(workerFinished);
                for (WorkItem workItem : workerFinished) {
                    ArticoloCalcoloWork articoloCalcolo = (ArticoloCalcoloWork) workItem.getResult();

                    logger.trace("Calcolata richiesta id articolo " + articoloCalcolo.getKey());
                    Map<ArticoloDepositoConfigurazioneKey, RigheCalcolo[]> risultatoArticolo = articoloCalcolo
                            .getRisultato();

                    for (Entry<ArticoloDepositoConfigurazioneKey, RigheCalcolo[]> risultatoConfArticolo : risultatoArticolo
                            .entrySet()) {
                        risultati.put(risultatoConfArticolo.getKey(), risultatoConfArticolo.getValue());
                        Bom bomRisultato = boms
                                .get(new ArticoloConfigurazioneKey(risultatoConfArticolo.getKey().getIdArticolo(),
                                        risultatoConfArticolo.getKey().getIdConfigurazione()));
                        if (bomRisultato != null) {
                            for (Bom bomArticoloFiglio : bomRisultato.getFigli()) {
                                ArticoloAnagrafica articoloAnagraficaFiglio = articoliAnagrafica
                                        .get(bomArticoloFiglio.getIdArticolo());
                                if (logger.isTraceEnabled()) {
                                    logger.trace("Verifico se posso elaborare per il padre  {"
                                            + risultatoConfArticolo.getKey() + "}" + " il figlio {"
                                            + bomArticoloFiglio.getIdArticolo() + ":"
                                            + bomArticoloFiglio.getIdConfigurazione());
                                }
                                boolean elaboraFiglio = elaboraDistintaCheck(bomArticoloFiglio.getIdArticolo(),
                                        articoloCalcolo.getKey().getIdDeposito(), risultati);
                                if (articoloAnagraficaFiglio != null && elaboraFiglio) {
                                    logger.trace(
                                            "Posso elaborare il figlio {" + bomArticoloFiglio.getIdArticolo() + "}");

                                    List<RisultatoPadre> padri = new ArrayList<RisultatoPadre>();
                                    for (Integer idConfigurazione : righeOrdinatoCliente
                                            .getConfigurazioniDistintaUtilizzate()) {
                                        ArticoloConfigurazioneKey keyFiglio = new ArticoloConfigurazioneKey(
                                                bomArticoloFiglio.getIdArticolo(), idConfigurazione);
                                        Bom bomConfigurazione = boms.get(keyFiglio);

                                        if (bomConfigurazione != null) {
                                            for (Bom bomPadre : bomConfigurazione.getPadri()) {
                                                logger.trace("Creo i risultati  del padre {" + bomPadre.getIdDistinta()
                                                        + "} da passare al figlio {" + bomArticoloFiglio.getIdArticolo()
                                                        + "}");
                                                ArticoloDepositoConfigurazioneKey chiaveRisultatoPadre = new ArticoloDepositoConfigurazioneKey(
                                                        bomPadre.getIdDistinta(),
                                                        articoloCalcolo.getKey().getIdDeposito(),
                                                        bomPadre.getIdConfigurazione());
                                                RigheCalcolo[] articoloCalcoloPadre = risultati
                                                        .get(chiaveRisultatoPadre);
                                                if (articoloCalcoloPadre != null) {
                                                    logger.trace("Padre {" + bomPadre.getIdDistinta()
                                                            + "} calcolato lo aggiungo");
                                                    RisultatoPadre risultatoPadre = new RisultatoPadre(bomPadre,
                                                            articoloCalcoloPadre);
                                                    padri.add(risultatoPadre);
                                                }
                                            }
                                        }
                                    }
                                    Integer idFiglio = bomArticoloFiglio.getIdArticolo();
                                    ArticoloDepositoKey articoloDepositoKey = new ArticoloDepositoKey(idFiglio,
                                            articoloCalcolo.getIdDeposito());
                                    ArticoloCalcoloWork articoloCalcoloWork = createArticoloCalcoloWork(
                                            articoloCalcolo.getIdDeposito(), idFiglio,
                                            giacenze.get(articoloDepositoKey),
                                            righeOrdinatoCliente.getRigheOrdinateArticoloDeposito()
                                                    .get(articoloDepositoKey),
                                            numTime, articoliAnagrafica.get(idFiglio), calendarWork, padri);

                                    WorkItem articoloWorkItem = workManager.schedule(articoloCalcoloWork);
                                    processati.add(articoloWorkItem);
                                }
                            }
                        }
                    }
                }
                workersDone = processati.isEmpty();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            logger.error("Intervallo di calcolo insufficiente", e);
            throw e;
        } catch (Exception e) {
            logger.error("Errore nelcalcolo mrp", e);
            throw new RuntimeException("Errore nelcalcolo mrp", e);
        }
        return risultati;
    }

    /**
     * Crea l'istanza di ArticoloCalcoloWork.
     *
     * @param idDeposito
     *            idDeposito
     * @param idArticolo
     *            idArticolo
     * @param idConfigurazione
     *            idConfigurazione
     * @param giacenza
     *            giacenza
     * @param righeCalcolo
     *            righeCalcolo
     * @param ordinatoAttivo
     *            s
     * @param numBucket
     *            numBucket
     * @param articoloAnagrafica
     *            articoloAnagrafica
     * @param calendarWorkParam
     *            calendarWork
     * @param ordinamento
     *            ordinamento
     * @param risultatiPadre
     *            righeCalcolatePadre
     * @param startDateParam
     *            startDate
     * @return ArticoloCalcoloWork
     */
    protected ArticoloCalcoloWork createArticoloCalcoloWork(int idDeposito, int idArticolo, Giacenza giacenza,
            RigheCalcolo[] righeCalcolo, int numBucket, ArticoloAnagrafica articoloAnagrafica,
            boolean[] calendarWorkParam, List<RisultatoPadre> risultatiPadre) {
        ArticoloDepositoKey key = new ArticoloDepositoKey(idArticolo, idDeposito);
        ArticoloCalcoloWork articoloCalcoloWork = new ArticoloCalcoloOrdineWork(idDeposito, idArticolo, giacenza,
                righeCalcolo, numBucket, articoloAnagrafica, calendarWorkParam, risultatiPadre, boms,
                ordiniFornitoreCalcolo.get(key), idAreaOrdineFiltrata);
        if (logger.isTraceEnabled()) {
            logger.trace("Lancio il calcolo per l'articolo {" + idArticolo + "}");
        }
        return articoloCalcoloWork;
    }

    /**
     * Elebora ricorsivamente i figli.
     *
     * @param idDistinta
     *            idFiglio
     * @param idConfigurazione
     *            idConfigurazione
     * @param idDeposito
     *            idDeposito
     * @param bomsEsplose
     *            boms
     * @param risultatiParam
     *            risultati
     * @param righeOrdinatoClienteParam
     *            righeOrdinatoCliente
     * @return true o false
     */
    protected boolean elaboraDistintaCheck(Integer idDistinta, Integer idDeposito,
            Map<ArticoloDepositoConfigurazioneKey, RigheCalcolo[]> risultati) {
        boolean calcola = true;

        if (logger.isTraceEnabled()) {
            logger.trace("verifico se posso elaborare  l'articolo {" + idDistinta + "}");
        }

        for (Integer idConfigurazione : righeOrdinatoCliente.getConfigurazioniDistintaUtilizzate()) {
            if (!elaboraDistintaConfigurazioneCheck(new ArticoloConfigurazioneKey(idDistinta, idConfigurazione),
                    idDeposito, risultati)) {
                calcola = false;
                break;
            }
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Elaboro l'articolo {" + idDistinta + "}" + ":" + calcola);
        }
        return calcola;
    }

    /**
     * Elebora ricorsivamente i figli.
     *
     * @param idDistinta
     *            idFiglio
     * @param idConfigurazione
     *            idConfigurazione
     * @param idDeposito
     *            idDeposito
     * @param bomsParam
     *            boms
     * @param risultatiParam
     *            risultati
     * @param righeOrdinatoClienteParam
     *            righeOrdinatoCliente
     * @return true o false
     */
    protected boolean elaboraDistintaConfigurazioneCheck(ArticoloConfigurazioneKey configurazioneArticoloKey,
            Integer idDeposito, Map<ArticoloDepositoConfigurazioneKey, RigheCalcolo[]> risultati) {
        boolean calcola = true;
        Bom bom = boms.get(configurazioneArticoloKey);
        if (bom == null) {
            return true;
        }

        for (Bom padre : bom.getPadri()) {
            ArticoloDepositoConfigurazioneKey keyPadre = new ArticoloDepositoConfigurazioneKey(padre.getIdDistinta(),
                    idDeposito, padre.getIdConfigurazione());
            boolean padreCalcolato = risultati.containsKey(keyPadre);
            if (!padreCalcolato) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Padre non calcolato  {" + keyPadre + "}....verifico se devo calcolarlo");
                }
                padreCalcolato = !padreDaElaborare(padre.getIdDistinta(), idDeposito, padre.getIdConfigurazione(),
                        risultati);
                if (logger.isTraceEnabled()) {
                    logger.trace("Padre da calcolare {" + keyPadre + "} : " + !padreCalcolato);
                }
            }
            calcola = calcola && padreCalcolato;
            if (logger.isTraceEnabled()) {
                logger.trace("Posso calcolare articolo  dopo x cicli {" + configurazioneArticoloKey + "} : " + calcola);
            }
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Elaboro per configurazioneArticolo {" + configurazioneArticoloKey + ":" + calcola);
        }
        return calcola;
    }

    private boolean padreDaElaborare(int idDistinta, int idDeposito, Integer idConfigurazione,
            Map<ArticoloDepositoConfigurazioneKey, RigheCalcolo[]> risultati) {
        boolean padreDaElaborareResult = false;

        ArticoloDepositoConfigurazioneKey keyPadre = new ArticoloDepositoConfigurazioneKey(idDistinta, idDeposito,
                idConfigurazione);

        if (logger.isTraceEnabled()) {
            logger.trace("Verifico se devo elaborare il padre {" + keyPadre + "}");
        }

        boolean padreOrdinato = righeOrdinatoCliente.getRigheOrdinateArticoloDepositoConfigurazione()
                .containsKey(keyPadre);

        if (padreOrdinato) {
            padreDaElaborareResult = true;
        } else {
            ArticoloConfigurazioneKey keyPadreArtConf = new ArticoloConfigurazioneKey(idDistinta, idConfigurazione);
            Bom bomPadre = boms.get(keyPadreArtConf);
            if (bomPadre == null || bomPadre.getPadri().isEmpty()) {
                padreDaElaborareResult = false;
            } else {
                if (bomPadre != null) {
                    for (Bom padre : bomPadre.getPadri()) {
                        padreDaElaborareResult = padreDaElaborareResult
                                || padreDaElaborare(padre.getIdDistinta(), idDeposito, idConfigurazione, risultati);
                    }
                }
            }
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Padre da Elaborare  {" + keyPadre + "} : " + padreDaElaborareResult);
        }

        return padreDaElaborareResult;
    }
}
