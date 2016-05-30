package it.eurotn.panjea.mrp.manager.work;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import it.eurotn.panjea.magazzino.domain.Giacenza;
import it.eurotn.panjea.magazzino.exception.FormulaException;
import it.eurotn.panjea.magazzino.exception.FormulaMrpCalcoloArticoloException;
import it.eurotn.panjea.mrp.domain.ArticoloAnagrafica;
import it.eurotn.panjea.mrp.domain.Bom;
import it.eurotn.panjea.mrp.domain.Moltiplicatore;
import it.eurotn.panjea.mrp.domain.OrdiniFornitoreCalcolo;
import it.eurotn.panjea.mrp.domain.RigheCalcolo;
import it.eurotn.panjea.mrp.domain.RisultatoMRPArticoloBucket;
import it.eurotn.panjea.mrp.domain.RisultatoSottoScortaMRPArticoloBucket;
import it.eurotn.panjea.mrp.manager.interfaces.MrpCalcoloManager;
import it.eurotn.panjea.mrp.util.ArticoloConfigurazioneKey;
import it.eurotn.panjea.mrp.util.ArticoloDepositoConfigurazioneKey;
import it.eurotn.panjea.mrp.util.ArticoloDepositoKey;
import it.eurotn.panjea.mrp.util.RisultatoPadre;

/**
 * @author leonardo Calcola le qta considerando gli ordini fornitori legati ai loro ordini clienti.
 *         La materia prima impegnata per quei ordini non può più essere impegnata per altro.
 */
public class ArticoloCalcoloOrdineWork extends ArticoloCalcoloWork {

    private static final Logger LOGGER = Logger.getLogger(ArticoloCalcoloOrdineWork.class);

    private Integer idAreaOrdineFiltrata;

    /**
     * Costruttore.
     *
     * @param idDeposito
     *            idDeposito
     * @param idArticolo
     *            idArticolo
     * @param giacenza
     *            giacenza iniziale
     * @param ordinatoCliente
     *            righe iniziali di R.
     * @param numBucket
     *            orizzonte calcolo
     * @param articoloAnagrafica
     *            articoloAnagrafica
     * @param calendarWork
     *            calendarWork
     * @param righeCalcolatePadre
     *            righeCalcolatePadre
     * @param boms
     *            bom esplose
     * @param ordiniFornitoreCalcolo
     *            ordini fornitore
     * @param idAreaOrdine
     *            area ordine filtrata
     *
     */
    public ArticoloCalcoloOrdineWork(final int idDeposito, final int idArticolo, final Giacenza giacenza,
            final RigheCalcolo[] ordinatoCliente, final int numBucket, final ArticoloAnagrafica articoloAnagrafica,
            final boolean[] calendarWork, final List<RisultatoPadre> righeCalcolatePadre,
            final Map<ArticoloConfigurazioneKey, Bom> boms, final List<OrdiniFornitoreCalcolo> ordiniFornitoreCalcolo,
            final Integer idAreaOrdine) {
        super(idDeposito, idArticolo, giacenza, ordinatoCliente, numBucket, articoloAnagrafica, calendarWork,
                righeCalcolatePadre, boms, ordiniFornitoreCalcolo);
        this.idAreaOrdineFiltrata = idAreaOrdine;
    }

    /**
     * Aggiunge la qta degli attrezzaggi
     *
     * @param attrezzaggiAggiunti
     *            attrezzaggi aggiunti nel processo
     */
    private void aggiungiAttrezzaggi(List<String> attrezzaggiAggiunti) {
        // Aggiungo le qta di attrezzaggio alle distinte
        for (int i = 0; i < numBucket; i++) {
            RigheCalcolo risultatoGiornoAtt = ordinatoCliente[i];
            if (risultatoGiornoAtt != null) {
                List<RisultatoMRPArticoloBucket> risultatiAtt = risultatoGiornoAtt.getRisultatiMRPArticoloBucket();
                for (RisultatoMRPArticoloBucket risultatoMRPArticoloBucket : risultatiAtt) {
                    if (!(risultatoMRPArticoloBucket instanceof RisultatoSottoScortaMRPArticoloBucket)) {
                        ArticoloConfigurazioneKey keyBom = new ArticoloConfigurazioneKey(getIdArticolo(),
                                risultatoMRPArticoloBucket.getIdConfigurazioneDistinta());
                        Bom bom = boms.get(keyBom);
                        if (bom != null) {
                            if (risultatoMRPArticoloBucket.getQtaR() > 0.0
                                    && risultatoMRPArticoloBucket.getTipoRiga() == 0 && !attrezzaggiAggiunti
                                            .contains(getChiaveAttrezzaggio(risultatoMRPArticoloBucket))) {
                                risultatoMRPArticoloBucket.aggiungiQtaAttrezzaggio(bom.getQtaAttrezzaggioDistinta());
                                attrezzaggiAggiunti.add(getChiaveAttrezzaggio(risultatoMRPArticoloBucket));
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * calcolo por.
     */
    @Override
    protected void calcola() {

        ordinatoCliente = ObjectUtils.defaultIfNull(ordinatoCliente, new RigheCalcolo[numBucket]);
        this.ordiniFornitoreCalcolo = ObjectUtils.defaultIfNull(ordiniFornitoreCalcolo,
                new ArrayList<OrdiniFornitoreCalcolo>());

        giacenza = ObjectUtils.defaultIfNull(giacenza, new Giacenza(0.0, 0.0));

        List<String> attrezzaggiAggiunti = new ArrayList<String>();
        ordinatoCliente = generaRighePadre(attrezzaggiAggiunti);
        aggiungiAttrezzaggi(attrezzaggiAggiunti);

        Double giacenzaIniziale = giacenza.getGiacenza();
        Double disponibilita = giacenza.getGiacenza();
        risultatiConfigurazione = new HashMap<ArticoloDepositoConfigurazioneKey, RigheCalcolo[]>();

        // Scarico gli ordini fornitore o la giacenza degli ordini già elaborati (quindi con ordini
        // fornitore o produzione legati)
        for (OrdiniFornitoreCalcolo ordineFornitore : ordiniFornitoreCalcolo) {
            int[] idRigheCollegate = ordineFornitore.getRigheOrdiniCollegate();
            for (int idRigaOrdine : idRigheCollegate) {
                consumaQtaPerOrdineCliente(ordineFornitore, idRigaOrdine);
            }
        }

        List<List<OrdiniFornitoreCalcolo>> ordiniFornitoreBucket = new ArrayList<List<OrdiniFornitoreCalcolo>>(
                numBucket);
        for (int i = 0; i < numBucket; i++) {
            ordiniFornitoreBucket.add(new ArrayList<OrdiniFornitoreCalcolo>());
        }

        // Inserisco il ordineFornitoreRimanenti gli ordini che hanno una qta ancora utilizzabile
        if (ordiniFornitoreCalcolo != null) {
            for (OrdiniFornitoreCalcolo ordineFornitore : ordiniFornitoreCalcolo) {
                int pos = ordineFornitore.getDiffData() + MrpCalcoloManager.BUCKET_ZERO;
                if (ordineFornitore.getQtaRimanente() > 0) {
                    ordiniFornitoreBucket.get(pos).add(ordineFornitore);
                }
            }
        }

        double[] ordiniInArrivo = calcolaS();
        List<OrdiniFornitoreCalcolo> ordinifornitoreRimanenti = new ArrayList<OrdiniFornitoreCalcolo>();
        for (int i = 0; i < numBucket; i++) {
            ordinifornitoreRimanenti.addAll(ordiniFornitoreBucket.get(i));
            disponibilita += ordiniInArrivo[i];
            RigheCalcolo risultatoGiorno = ordinatoCliente[i];
            if (risultatoGiorno != null) {
                // Se ho righe in risultatoGiorno calcolo i tempi con leadTime e
                // calendari.
                int leadTimeProgressivo = articoloAnagrafica.getLeadTime();
                Date dataConsegna = risultatoGiorno.getRisultatiMRPArticoloBucket().get(0).getDataConsegna();
                Date dataProduzione = risultatoGiorno.getRisultatiMRPArticoloBucket().get(0).getDataProduzione();

                int bucketDataDocumento = 0;
                while (leadTimeProgressivo > 0 && (i - bucketDataDocumento) > 0) {
                    bucketDataDocumento++;
                    try {
                        if (calendarWork[i - bucketDataDocumento]) {
                            leadTimeProgressivo--;
                        }
                    } catch (Exception e) {
                        LOGGER.error("-->errore ", e);
                    }
                }

                // Per ogni riga setto la dataDocumento appena calcolata e setto
                // i parametri dell'anagrafica
                // articolo

                Date dataDocumento = new DateTime(dataConsegna).minusDays(bucketDataDocumento).toDate();
                Double minOrdinabile = articoloAnagrafica.getMinOrdinabile();
                // se ho impostata una data di produzione sull'ordine cliente,
                // allora uso quello invece di considerare
                // la data calcolata
                if (dataProduzione != null && articoloAnagrafica.isDistinta()) {
                    dataDocumento = dataProduzione;
                }

                List<RisultatoMRPArticoloBucket> risultati = risultatoGiorno.getRisultatiMRPArticoloBucket();
                for (RisultatoMRPArticoloBucket risultatoMRPArticoloBucket : risultati) {

                    // Se nel bucket ho righe create da l sottoscorta non le
                    // considero
                    if (!(risultatoMRPArticoloBucket instanceof RisultatoSottoScortaMRPArticoloBucket)) {

                        if (risultatoMRPArticoloBucket.getIdRigaOrdine() == 4805) {
                            System.out.println("RIGA " + risultatoMRPArticoloBucket.getQtaR() + " : "
                                    + risultatoMRPArticoloBucket.getQtarRimanente());
                        }
                        if (risultatoMRPArticoloBucket.getTipoRiga() != 2) {
                            risultatoMRPArticoloBucket.setDisponibilita(disponibilita);
                            disponibilita -= consumaDisponibilita(risultatoMRPArticoloBucket, ordinifornitoreRimanenti);
                            risultatoMRPArticoloBucket.setDataDocumento(dataDocumento);
                            risultatoMRPArticoloBucket.setQtaInArrivo(ordiniInArrivo[i]);
                            risultatoMRPArticoloBucket
                                    .setDisponibilitaDopoUtilizzo(disponibilita < 0 ? 0 : disponibilita);
                        } else {
                            risultatoMRPArticoloBucket.setOrdinamento(0);
                            // in una riga produzione la disponibilità è quella
                            // attuale meno la qta prodotta da "se stesso"
                            risultatoMRPArticoloBucket
                                    .setDisponibilita(disponibilita - risultatoMRPArticoloBucket.getQtaR());
                            risultatoMRPArticoloBucket
                                    .setDisponibilitaDopoUtilizzo(disponibilita < 0 ? 0 : disponibilita);
                        }

                        risultatoMRPArticoloBucket.setGiacenza(giacenzaIniziale);

                        risultatoMRPArticoloBucket.setScorta(giacenza.getScorta());
                        risultatoMRPArticoloBucket.setArticoloAnagrafica(articoloAnagrafica);
                        if (risultatoMRPArticoloBucket.getOrdinamento() == -1) {
                            risultatoMRPArticoloBucket.setOrdinamento(ordinamento++);
                        }

                        ArticoloDepositoConfigurazioneKey keyConfigurazione = new ArticoloDepositoConfigurazioneKey(
                                articoloAnagrafica.getIdArticolo(), key.getIdDeposito(),
                                risultatoMRPArticoloBucket.getIdConfigurazioneDistinta());
                        RigheCalcolo[] risultatoConfigurazione = risultatiConfigurazione.get(keyConfigurazione);
                        risultatoConfigurazione = ObjectUtils.defaultIfNull(risultatoConfigurazione,
                                new RigheCalcolo[numBucket]);
                        RigheCalcolo risultatoGiornoCalcolato = ObjectUtils
                                .defaultIfNull(risultatoConfigurazione[i - bucketDataDocumento], new RigheCalcolo());
                        risultatoGiornoCalcolato.addRisultatiMRPArticoloBucket(
                                risultatoMRPArticoloBucket.getIdConfigurazioneDistinta(),
                                risultatoMRPArticoloBucket.getIdArticolo(), risultatoMRPArticoloBucket);
                        risultatoConfigurazione[i - bucketDataDocumento] = risultatoGiornoCalcolato;
                        risultatiConfigurazione.put(keyConfigurazione, risultatoConfigurazione);
                    }
                }
                // Controllo le scorte. Se sono sotto scorta creo una riga per
                // la scorta.
                if (disponibilita < giacenza.getScorta()) {
                    double qtaDaOrdinare = giacenza.getScorta() - disponibilita;
                    if (minOrdinabile > 0 && qtaDaOrdinare < minOrdinabile) {
                        qtaDaOrdinare = minOrdinabile;
                    }
                    disponibilita = qtaDaOrdinare + disponibilita;
                    RisultatoMRPArticoloBucket rigaScorta = new RisultatoMRPArticoloBucket();
                    rigaScorta.setDataDocumento(dataDocumento);
                    rigaScorta.setDataConsegna(dataConsegna);
                    rigaScorta.setIdDeposito(key.getIdDeposito());
                    rigaScorta.setGiacenza(giacenzaIniziale);
                    rigaScorta.setDisponibilita(disponibilita);
                    rigaScorta.setDisponibilitaDopoUtilizzo(disponibilita);
                    rigaScorta.setScorta(giacenza.getScorta());
                    rigaScorta.setQtaR(qtaDaOrdinare);
                    rigaScorta.setQtaPor(qtaDaOrdinare);
                    rigaScorta.setArticoloAnagrafica(articoloAnagrafica);
                    rigaScorta.setOrdinamento(ordinamento++);

                    ArticoloDepositoConfigurazioneKey keyConfigurazione = new ArticoloDepositoConfigurazioneKey(
                            articoloAnagrafica.getIdArticolo(), key.getIdDeposito(), null);
                    RigheCalcolo[] risultatoConfigurazione = ObjectUtils
                            .defaultIfNull(risultatiConfigurazione.get(keyConfigurazione), new RigheCalcolo[numBucket]);
                    RigheCalcolo risultatoGiornoCalcolato = ObjectUtils
                            .defaultIfNull(risultatoConfigurazione[i - bucketDataDocumento], new RigheCalcolo());
                    risultatoGiornoCalcolato.addRisultatiMRPArticoloBucket(rigaScorta.getIdConfigurazioneDistinta(),
                            rigaScorta.getIdArticolo(), rigaScorta);
                    risultatoConfigurazione[i - bucketDataDocumento] = risultatoGiornoCalcolato;
                    risultatiConfigurazione.put(keyConfigurazione, risultatoConfigurazione);

                }
            }
        }
    }

    /**
     *
     * @param risultato
     *            con la qta da consumare
     * @return disponibilità rimanente
     */
    private double consumaDisponibilita(RisultatoMRPArticoloBucket risultato,
            List<OrdiniFornitoreCalcolo> ordinifornitoreRimanenti) {

        double qtaPor = risultato.getQtarRimanente();
        if (qtaPor <= 0) {
            return risultato.getQtaR();
        }

        if (idAreaOrdineFiltrata != null && risultato.getIdOrdineCliente() != idAreaOrdineFiltrata) {
            return 0;
        }
        for (OrdiniFornitoreCalcolo ordiniFornitoreCalcolo : ordinifornitoreRimanenti) {
            double qtaOrdine = ordiniFornitoreCalcolo.getQtaRimanente();
            if (qtaOrdine <= 0) {
                continue;
            }
            risultato.collegaRigaOrdineFornitore(ordiniFornitoreCalcolo.getIdRigaOrdineFornitore());
            double qtaDaConsumare = qtaPor;
            qtaPor = qtaPor - qtaOrdine;
            if (qtaPor >= 0) {
                ordiniFornitoreCalcolo.addQtaConsumata(ordiniFornitoreCalcolo.getQtaRimanente());
                risultato.setQtaInArrivo(risultato.getQtaInArrivo() + ordiniFornitoreCalcolo.getQtaRimanente());
            } else {
                risultato.setQtaInArrivo(risultato.getQtaInArrivo() + Math.abs(qtaPor));
                ordiniFornitoreCalcolo.addQtaConsumata(Math.abs(qtaDaConsumare));
                qtaPor = 0;
                break;
            }
        }
        if (qtaPor > 0) {
            qtaPor = qtaPor - giacenza.getGiacenza();
            if (qtaPor > 0) {
                giacenza.setGiacenza(0.0);
            } else {
                giacenza.setGiacenza(Math.abs(qtaPor));
                qtaPor = 0;
            }
        }
        risultato.setQtaPor(qtaPor);
        return risultato.getQtarRimanente() - qtaPor;
    }

    private void consumaQtaPerOrdineCliente(OrdiniFornitoreCalcolo ordineFornitore, int idRigaOrdine) {
        for (RigheCalcolo rigaCalcoloGiorno : ordinatoCliente) {
            if (rigaCalcoloGiorno == null) {
                continue;
            }
            for (RisultatoMRPArticoloBucket risultato : rigaCalcoloGiorno.getRisultatiMRPArticoloBucket()) {
                if (idRigaOrdine == risultato.getIdRigaOrdine() && risultato.getTipoRiga() != 2) {
                    risultato.setQtaInArrivo(risultato.getQtaInArrivo() + ordineFornitore.getQta());
                    if (risultato.getOrdinamento() == -1) {
                        risultato.setOrdinamento(ordinamento++);
                    }
                    double qtaR = risultato.getQtarRimanente() - ordineFornitore.getQtaRimanente();
                    if (qtaR <= 0) {
                        ordineFornitore.addQtaConsumata(risultato.getQtarRimanente());
                        risultato.setQtarRimanente(0);
                    } else {
                        // Consumo la qta ordinata mancante
                        ordineFornitore.addQtaConsumata(ordineFornitore.getQtaRimanente());
                        // Consumo la giacenza
                        double qtarConGiacenza = qtaR - this.giacenza.getGiacenza();
                        if (qtarConGiacenza <= 0) {
                            giacenza.setGiacenza(giacenza.getGiacenza() - qtaR);
                            risultato.setQtarRimanente(0);
                        } else {
                            risultato.setQtarRimanente(qtarConGiacenza);
                            giacenza.setGiacenza(0.0);
                        }
                    }
                }
            }
        }
    }

    /**
     * Genera le righe dei componenti utilizzate per costruire il padre
     *
     * @param attrezzaggiAggiunti
     *            attrezaggi aggiunti sulla generazione componenti
     * @return ordinatoCliente con le righe generate dai padri
     */
    private RigheCalcolo[] generaRighePadre(List<String> attrezzaggiAggiunti) {
        righeCalcolatePadre = ObjectUtils.defaultIfNull(righeCalcolatePadre, new ArrayList<RisultatoPadre>());
        Moltiplicatore moltiplicatoreCalculator = new Moltiplicatore();
        for (RisultatoPadre padre : righeCalcolatePadre) {
            RigheCalcolo[] risultatoPadre = padre.getRigheCalcolo();
            for (int i = 0; i < risultatoPadre.length; i++) {
                RigheCalcolo righeCalcoloPadre = risultatoPadre[i];
                if (righeCalcoloPadre != null) {
                    Integer idArticoloPadre = padre.getBom().getIdDistinta();
                    Integer idConfigurazionePadre = padre.getBom().getKeyArticoloConfigurazione().getIdConfigurazione();

                    ArticoloDepositoConfigurazioneKey keyArticoloDepositoConfigurazionePadre = new ArticoloDepositoConfigurazioneKey(
                            idArticoloPadre, key.getIdDeposito(), idConfigurazionePadre);
                    // Ciclo sui padri e calcolo le quantità che mi servono
                    // moltiplicando il molt.*qta del padre
                    for (RisultatoMRPArticoloBucket risultatoArticoloBucketPadre : righeCalcoloPadre
                            .getRisultatiMRPArticoloBucket(keyArticoloDepositoConfigurazionePadre)) {
                        if (risultatoArticoloBucketPadre.getTipoRiga() > 0) {
                            continue;
                        }
                        String formula = padre.getBom().getFormulaMolt();
                        String codiciAttributo = risultatoArticoloBucketPadre.getCodiciAttributo() != null
                                ? risultatoArticoloBucketPadre.getCodiciAttributo() : "";
                        String valoriAttributo = risultatoArticoloBucketPadre.getValoriAttributo() != null
                                ? risultatoArticoloBucketPadre.getValoriAttributo() : "";

                        String separator = "";
                        if (padre.getBom().isTrasmettiAttributi()) {
                            String codiciAttributoPadre = padre.getBom().getCodiciAttributi() != null
                                    ? padre.getBom().getCodiciAttributi() : "";
                            String valoriAttributoPadre = padre.getBom().getValoriAttributi() != null
                                    ? padre.getBom().getValoriAttributi() : "";
                            if (!codiciAttributoPadre.isEmpty() && !codiciAttributo.isEmpty()) {
                                separator = ",";
                            }

                            codiciAttributo = codiciAttributo.concat(separator).concat(codiciAttributoPadre);
                            valoriAttributo = valoriAttributo.concat(separator).concat(valoriAttributoPadre);
                        }
                        BigDecimal qtaPor = BigDecimal.valueOf(risultatoArticoloBucketPadre.getQtaPor());
                        Double qtaAttrezzaggio = padre.getBom().getQtaAttrezzaggioArticolo();

                        Double qtaComponente = 0.0;
                        try {
                            qtaComponente = moltiplicatoreCalculator.calcola(formula, qtaPor,
                                    articoloAnagrafica.getNumDecimali(), codiciAttributo, valoriAttributo)
                                    .doubleValue();
                        } catch (FormulaException ex) {
                            FormulaMrpCalcoloArticoloException exf = new FormulaMrpCalcoloArticoloException(formula,
                                    ex.getMessage(), padre.getBom().getIdDistinta(),
                                    articoloAnagrafica.getIdArticolo());
                            LOGGER.error(
                                    "-->errore nella formula per la distinta  " + articoloAnagrafica.getIdArticolo()
                                            + " ed il padre con chiave \n " + padre.getBom(),
                                    ex);
                            throw exf;
                        }

                        if (qtaComponente > 0.0
                                && !attrezzaggiAggiunti.contains(getChiaveAttrezzaggio(risultatoArticoloBucketPadre))) {
                            attrezzaggiAggiunti.add(getChiaveAttrezzaggio(risultatoArticoloBucketPadre));
                            qtaComponente += qtaAttrezzaggio;
                        }

                        RisultatoMRPArticoloBucket rigaComponente = new RisultatoMRPArticoloBucket();
                        rigaComponente
                                .setDataConsegna(DateUtils.addDays(risultatoArticoloBucketPadre.getDataDocumento(),
                                        ObjectUtils.defaultIfNull(articoloAnagrafica.getGgSicurezza() * -1, 0)));
                        rigaComponente.setIdOrdineCliente(risultatoArticoloBucketPadre.getIdOrdineCliente());
                        rigaComponente.setArticoloAnagrafica(articoloAnagrafica);
                        rigaComponente.setIdDeposito(key.getIdDeposito());
                        rigaComponente.setIdConfigurazioneDistinta(
                                risultatoArticoloBucketPadre.getIdConfigurazioneDistinta());
                        rigaComponente.setIdComponente(padre.getBom().getIdComponente());
                        rigaComponente.setFormula(padre.getBom().getFormulaMolt());
                        rigaComponente.setQtaR(qtaComponente);
                        rigaComponente.setIdRigaOrdine(risultatoArticoloBucketPadre.getIdRigaOrdine());
                        rigaComponente.setValoriAttributo(risultatoArticoloBucketPadre.getValoriAttributo());
                        rigaComponente.setCodiciAttributo(risultatoArticoloBucketPadre.getCodiciAttributo());
                        rigaComponente.setRisultatoArticoloDistinta(risultatoArticoloBucketPadre);
                        rigaComponente.setIdOrdine(risultatoArticoloBucketPadre.getIdOrdine());

                        RigheCalcolo righeCalcoloRisultatoArticolo = ObjectUtils.defaultIfNull(ordinatoCliente[i],
                                new RigheCalcolo());
                        righeCalcoloRisultatoArticolo
                                .addRisultatoMRPArticoloBucket(keyArticoloDepositoConfigurazionePadre, rigaComponente);
                        ordinatoCliente[i] = righeCalcoloRisultatoArticolo;
                    }
                }
            }
        }
        return ordinatoCliente;
    }

    /**
     * @return Returns the calendarWork.
     */
    @Override
    public boolean[] getCalendarWork() {
        return calendarWork;
    }

    private String getChiaveAttrezzaggio(RisultatoMRPArticoloBucket risultato) {
        return String.valueOf(risultato.getIdOrdine()) + String.valueOf(risultato.getIdArticolo());
    }

    /**
     * @return Returns the idArticolo.
     */
    @Override
    public int getIdArticolo() {
        return key.getIdArticolo();
    }

    /**
     *
     * @return id deposito
     */
    @Override
    public int getIdDeposito() {
        return key.getIdDeposito();
    }

    /**
     *
     * @return chiave con articoloedeposito.
     */
    @Override
    public ArticoloDepositoKey getKey() {
        return key;
    }

    /**
     * @return the ordinamento
     */
    @Override
    public int getOrdinamento() {
        return ordinamento;
    }

    /**
     * @return Returns the risultato.
     */
    @Override
    public Map<ArticoloDepositoConfigurazioneKey, RigheCalcolo[]> getRisultato() {
        return risultatiConfigurazione;
    }

    @Override
    public boolean isDaemon() {
        return false;
    }

    /**
     * @return per calcolare le righe ordine cliente o produzione.
     */
    @Override
    protected boolean isRigaOrdineCliente() {
        return true;
    }

    @Override
    public void release() {
        LOGGER.debug("--> release");
    }

    @Override
    public void run() {
        calcola();
    }

    /**
     * @param calendarWork
     *            The calendarWork to set.
     */
    @Override
    public void setCalendarWork(boolean[] calendarWork) {
        this.calendarWork = calendarWork;
    }

}
