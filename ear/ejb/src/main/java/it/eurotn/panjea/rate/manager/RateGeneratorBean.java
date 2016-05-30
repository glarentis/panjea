package it.eurotn.panjea.rate.manager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.security.PermitAll;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.apache.log4j.Logger;
import org.jboss.annotation.ejb.LocalBinding;
import org.jboss.annotation.security.SecurityDomain;
import org.joda.time.DateTime;
import org.joda.time.Days;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.SortedList;
import it.eurotn.dao.exception.ObjectNotFoundException;
import it.eurotn.panjea.anagrafica.classedocumento.impl.ClasseIncassoPagamento;
import it.eurotn.panjea.anagrafica.documenti.domain.IAreaDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.Importo.TIPO_OPERAZIONE_VALUTA;
import it.eurotn.panjea.anagrafica.domain.lite.ClienteLite;
import it.eurotn.panjea.anagrafica.domain.lite.SedeEntitaLite;
import it.eurotn.panjea.anagrafica.manager.interfaces.AziendeManager;
import it.eurotn.panjea.anagrafica.manager.interfaces.ValutaManager;
import it.eurotn.panjea.anagrafica.service.exception.TipoDocumentoBaseException;
import it.eurotn.panjea.contabilita.domain.AreaContabile;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase;
import it.eurotn.panjea.contabilita.domain.TipoDocumentoBase.TipoOperazioneTipoDocumento;
import it.eurotn.panjea.contabilita.manager.interfaces.FormuleManager;
import it.eurotn.panjea.contabilita.manager.interfaces.IFormula;
import it.eurotn.panjea.contabilita.manager.interfaces.TipiAreaContabileManager;
import it.eurotn.panjea.contabilita.manager.interfaces.VariabiliFormulaManager;
import it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces.RitenutaAccontoContabilitaManager;
import it.eurotn.panjea.contabilita.manager.ritenutaacconto.interfaces.RitenutaAccontoTesoreriaManager;
import it.eurotn.panjea.contabilita.service.exception.FormulaException;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento;
import it.eurotn.panjea.pagamenti.domain.CodicePagamento.TipologiaPartita;
import it.eurotn.panjea.partite.domain.RigaStrutturaPartite;
import it.eurotn.panjea.partite.domain.StrutturaPartita;
import it.eurotn.panjea.partite.util.FactoryStrategiaDataScadenza;
import it.eurotn.panjea.partite.util.StrategiaDataScadenza;
import it.eurotn.panjea.rate.domain.AreaRate;
import it.eurotn.panjea.rate.domain.CalendarioRate;
import it.eurotn.panjea.rate.domain.Rata;
import it.eurotn.panjea.rate.domain.Rata.StatoRata;
import it.eurotn.panjea.rate.domain.RigaCalendarioRate;
import it.eurotn.panjea.rate.manager.interfaces.AreaRateManager;
import it.eurotn.panjea.rate.manager.interfaces.CalendariRateManager;
import it.eurotn.panjea.rate.manager.interfaces.PagamentiAccontoGenerator;
import it.eurotn.panjea.rate.manager.interfaces.RateGenerator;
import it.eurotn.panjea.rate.manager.interfaces.RateManager;
import it.eurotn.panjea.service.interfaces.PanjeaDAO;
import it.eurotn.panjea.tesoreria.domain.Pagamento;

/**
 *
 * @author giangi
 * @version 1.0, 10/nov/2010
 */
@Stateless(name = "Panjea.RateGenerator")
@SecurityDomain("PanjeaLoginModule")
@PermitAll
@LocalBinding(jndiBinding = "Panjea.RateGenerator")
public class RateGeneratorBean implements RateGenerator {

    private static final Logger LOGGER = Logger.getLogger(RateGeneratorBean.class);

    @EJB
    private AziendeManager aziendeManager;

    @EJB
    private PanjeaDAO panjeaDAO;

    @EJB
    private VariabiliFormulaManager variabiliFormulaManager;

    @EJB
    private ValutaManager valutaManager;

    @EJB
    private RateManager rateManager;

    @EJB
    private FormuleManager formuleManager;

    @EJB
    private PagamentiAccontoGenerator pagamentiAccontoGenerator;

    @EJB
    private CalendariRateManager calendariRateManager;

    @EJB
    private RitenutaAccontoTesoreriaManager ritenutaAccontoTesoreriaManager;

    @EJB
    private RitenutaAccontoContabilitaManager ritenutaAccontoContabilitaManager;

    @EJB
    private TipiAreaContabileManager tipiAreaContabileManager;

    @EJB
    private AreaRateManager areaRateManager;

    /**
     * Verifica se le collections di rate sono uguali considerando le dimensioni della lista e
     * valutando rata per rata i valori delle principali proprietà.
     *
     * @param ratePresenti
     *            rate esistenti
     * @param rateGenerate
     *            rate generate da comparare
     * @return true o false
     */
    private boolean areEqual(Set<Rata> ratePresenti, Set<Rata> rateGenerate) {
        if (ratePresenti.size() != rateGenerate.size()) {
            return false;
        }

        for (int i = 0; i < ratePresenti.size(); i++) {
            Rata rataPresente = ratePresenti.iterator().next();
            Rata rataGenerata = rateGenerate.iterator().next();

            if (!rataPresente.checkEqualForDomainValues(rataGenerata)) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param dataPartenza
     *            Date
     * @param strutturaPartita
     *            StrutturaPartita
     * @param tipo
     *            TipologiaPartita
     * @param calendarioRate
     *            calendario rate
     * @return List di tipo Rata
     */
    private List<Rata> calcolaRate(Date dataPartenza, StrutturaPartita strutturaPartita,
            TipologiaPartita tipo, CalendarioRate calendarioRate) {
        StrategiaDataScadenza strategiaDataScadenza = FactoryStrategiaDataScadenza
                .getStrategia(strutturaPartita.getTipoStrategiaDataScadenza().ordinal());

        // uso la data di partenza senza ore minuti e secondi
        Calendar dataPartenzaCal = Calendar.getInstance();
        dataPartenzaCal.setTime(dataPartenza);
        dataPartenzaCal.set(Calendar.HOUR_OF_DAY, 0);
        dataPartenzaCal.set(Calendar.MINUTE, 0);
        dataPartenzaCal.set(Calendar.SECOND, 0);
        dataPartenzaCal.set(Calendar.MILLISECOND, 0);

        List<Rata> rate = strategiaDataScadenza.getScadenze(dataPartenzaCal.getTime(),
                strutturaPartita, tipo);

        List<RigaCalendarioRate> righeCalendario = null;
        if (calendarioRate != null) {
            righeCalendario = calendariRateManager.caricaRigheCalendarioRate(calendarioRate);
        }

        for (Rata rata : rate) {
            // sincronizzo il flag di stampa richiesta versamento della rata con
            // quello della struttura partita
            rata.setStampaRV(strutturaPartita.isStampaRV());

            // verifico se devo spostare le date delle rate in base al
            // calendario
            if (calendarioRate != null) {
                for (RigaCalendarioRate rigaCalendarioRate : righeCalendario) {
                    if (rigaCalendarioRate.isInRange(rata.getDataScadenza())) {

                        // calcolo la differenza in giorni tra le date
                        Date dataAlternativa = rigaCalendarioRate
                                .calcolaDataAlternativa(rata.getDataScadenza());

                        Calendar calendarDataAlternativa = Calendar.getInstance();
                        calendarDataAlternativa.setTime(dataAlternativa);
                        calendarDataAlternativa.set(Calendar.HOUR_OF_DAY, 0);
                        calendarDataAlternativa.set(Calendar.MINUTE, 0);
                        calendarDataAlternativa.set(Calendar.SECOND, 0);
                        calendarDataAlternativa.set(Calendar.MILLISECOND, 0);

                        Calendar calendarDataScadenza = Calendar.getInstance();
                        calendarDataScadenza.setTime(rata.getDataScadenza());
                        calendarDataScadenza.set(Calendar.HOUR_OF_DAY, 0);
                        calendarDataScadenza.set(Calendar.MINUTE, 0);
                        calendarDataScadenza.set(Calendar.SECOND, 0);
                        calendarDataScadenza.set(Calendar.MILLISECOND, 0);

                        Days giorni = Days.daysBetween(new DateTime(calendarDataScadenza.getTime()),
                                new DateTime(calendarDataAlternativa.getTime()));

                        Integer giorniDifferenza = giorni.getDays();

                        // se non c'è la data alternativa la nuova data diventa
                        // il giorno successivo della data finale
                        if (rigaCalendarioRate.getDataAlternativa() == null) {
                            giorniDifferenza++;
                        }

                        calendarDataScadenza.add(Calendar.DAY_OF_MONTH, giorniDifferenza);
                        rata.setDataScadenza(calendarDataScadenza.getTime());
                        break;
                    }
                }
            }
        }
        return rate;
    }

    /**
     * Carica il tipo documento base della liquidazione iva.
     *
     * @return TipoDocumento o null nel caso in cui non sia impostato
     */
    private TipoDocumento caricaTipoDocumentoLiquidazione() {
        TipoDocumento tipoDocumentoLiquidazione = null;
        List<TipoDocumentoBase> caricaTipiDocumentoBase = tipiAreaContabileManager
                .caricaTipiDocumentoBase();
        for (TipoDocumentoBase tipoDocumentoBase : caricaTipiDocumentoBase) {
            if (TipoOperazioneTipoDocumento.LIQUIDAZIONE_IVA
                    .equals(tipoDocumentoBase.getTipoOperazione())) {
                tipoDocumentoLiquidazione = tipoDocumentoBase.getTipoAreaContabile()
                        .getTipoDocumento();
            }
        }
        return tipoDocumentoLiquidazione;
    }

    /**
     * Compatta (Somma in unica rata eventuali rate aventi stessa data scadenza e setto tipo
     * pagam.),<br>
     * Ordina per data le rate<br>
     * Numera le rate ordinate.
     *
     * @param rate
     *            Collection di rate
     * @return Collection di rate ordinate per data e numerate in base all'ordinamento
     */
    private Collection<Rata> compattaEOrdinaPerData(Collection<Rata> rate) {
        BasicEventList<Rata> rateTemp = new BasicEventList<Rata>();
        rateTemp.addAll(rate);

        SortedList<Rata> rateOrdinate = new SortedList<Rata>(rateTemp, new Comparator<Rata>() {
            @Override
            public int compare(Rata o1, Rata o2) {
                return o1.getDataScadenza().compareTo(o2.getDataScadenza());
            }
        });
        int numeroRata = 0;
        for (Rata rata : rateOrdinate) {
            rata.setNumeroRata(++numeroRata);
        }
        return rateOrdinate;
    }

    @Override
    public List<Rata> generaRate(CodicePagamento codicePagamento, Date dataDocumento,
            BigDecimal imponibile, BigDecimal iva, CalendarioRate calendarioRate) {

        try {
            codicePagamento = panjeaDAO.load(CodicePagamento.class, codicePagamento.getId());
        } catch (ObjectNotFoundException e) {
            LOGGER.error("-->errore nel caricare il codice di pagamento " + codicePagamento, e);
            throw new RuntimeException(e);
        }

        // costruisco la lista delle stutture castate con IFormula
        List<IFormula> formule = new ArrayList<IFormula>();
        for (IFormula formula : codicePagamento.getStrutturePartita()) {
            formule.add(formula);
        }
        Map<String, BigDecimal> variabiliFormule = new HashMap<String, BigDecimal>();
        variabiliFormule.put(VariabiliFormulaManager.TOT, imponibile.add(iva));
        variabiliFormule.put(VariabiliFormulaManager.IMP, imponibile);
        variabiliFormule.put(VariabiliFormulaManager.IVA, iva);

        Collection<Rata> rate = new HashSet<Rata>();

        // Creo le rate per ogni struttura
        if (codicePagamento.getStrutturePartita() != null) {
            for (StrutturaPartita strutturaPartita : codicePagamento.getStrutturePartita()) {
                // verifico che il calendario sia associato alla categoria rata
                // della struttura
                CalendarioRate calendarioUtile = null;
                if (calendarioRate != null) {
                    calendarioRate = calendariRateManager.caricaCalendarioRate(calendarioRate,
                            true);

                    if (calendarioRate.getCategorieRate() == null
                            || calendarioRate.getCategorieRate().isEmpty()
                            || calendarioRate.getCategorieRate()
                                    .contains(strutturaPartita.getCategoriaRata())) {
                        calendarioUtile = calendarioRate;
                    }
                }
                List<Rata> rateStruttura = calcolaRate(dataDocumento, strutturaPartita,
                        TipologiaPartita.NORMALE, calendarioUtile);
                // devo valorizzare l'importo
                Importo importo = new Importo("EUR", BigDecimal.ONE);
                valorizzaImportoRateConFormula(rateStruttura, importo, false, strutturaPartita,
                        variabiliFormule);
                rate.addAll(rateStruttura);
            }
        }

        // Ordino le rate (ogni struttura genera le rate con la sua numerazione
        Collection<Rata> rateOrdinate = compattaEOrdinaPerData(rate);
        return new ArrayList<Rata>(rateOrdinate);
    }

    @Override
    public AreaRate generaRate(IAreaDocumento areaDocumento) {
        LOGGER.debug("--> Enter generaRate");
        AreaRate areaRate = areaRateManager.caricaAreaRate(areaDocumento.getDocumento());
        LOGGER.debug("--> Exit generaRate");
        return generaRate(areaDocumento, areaRate);
    }

    @Override
    public AreaRate generaRate(IAreaDocumento areaDocumento, AreaRate areaRate) {
        LOGGER.debug("--> Enter generaRate");
        // non esegue il calcolo delle rate partite se la classe del documento è
        // ClasseIncassoPagamento
        TipoDocumento tipoDocumento = areaDocumento.getDocumento().getTipoDocumento();
        if (tipoDocumento.getClasseTipoDocumentoInstance() instanceof ClasseIncassoPagamento) {
            LOGGER.debug("--> Exit calcolaRatePartita: classe tipo documento non valida ");
            return areaRate;
        }

        // Per rifare le rate devo avere tutte le rate aperte.
        for (Rata rata : areaRate.getRate()) {
            if (rata.getStatoRata() != StatoRata.APERTA) {
                return areaRate;
            }
        }

        TipoDocumento tipoDocumentoLiquidazione = caricaTipoDocumentoLiquidazione();
        boolean isTipoDocumentoLiquidazione = tipoDocumentoLiquidazione != null
                && tipoDocumento.equals(tipoDocumentoLiquidazione);

        // se il totale documento è 0 cancello le rate esistenti, ma se il documento ha il tipo
        // documento per la
        // liquidazione (tipodoc base Liquidazione Iva) allora devo comunque generare le rate
        if (BigDecimal.ZERO.compareTo(
                areaDocumento.getDocumento().getTotale().getImportoInValutaAzienda()) == 0
                && !isTipoDocumentoLiquidazione) {
            for (Rata rataDaEliminare : areaRate.getRate()) {
                rateManager.cancellaRataNoCheck(rataDaEliminare);
            }
            areaRate.setRate(new TreeSet<Rata>());
            return areaRate;
        }

        Set<Rata> ratePresenti = areaRate.getRate();
        Set<Rata> rateGenerate = generaRateDocumento(areaDocumento, areaRate);

        boolean areEqual = areEqual(ratePresenti, rateGenerate);

        if (areEqual) {
            return areaRate;
        }

        // Cancello le rate dal database
        for (Rata rataDaEliminare : ratePresenti) {
            rateManager.cancellaRataNoCheck(rataDaEliminare);
        }
        // Salvo la rate generate
        Set<Rata> rateSalvate = new TreeSet<Rata>(new Rata.Ratacomparator());
        for (Rata rataGenerata : rateGenerate) {
            rataGenerata = rateManager.salvaRataNoCheck(rataGenerata);
            rateSalvate.add(rataGenerata);
        }

        // converto le rate in rate con importo per acconto
        List<Pagamento> pagamenti = new ArrayList<Pagamento>();
        for (Rata rata : rateSalvate) {
            if (!rata.isRitenutaAcconto()) {
                Pagamento pagamento = new Pagamento();
                pagamento.setRata(rata);
                pagamento.getImporto().setImportoInValuta(rata.getImporto().getImportoInValuta());
                pagamenti.add(pagamento);
            }
        }
        areaRate.setRate(rateSalvate);

        // se necessario vado a pagare in automatico tutti i pagamenti con gli acconti
        try {
            pagamentiAccontoGenerator.creaPagamentiConAcconto(pagamenti, null);
        } catch (TipoDocumentoBaseException e) {
            LOGGER.warn("tipoDocumento base tesoreria per acconto non definito", e);
            // se non è impostato il tipodocumentobase non faccio nulla, non creerà i pagamenti con
            // acconto
            // evito di rilanciare l'exception dato che la genera rate viene chiamata anche nella
            // fatturazione,
            // evasione e in altre importazioni dati
        }

        areaRate.setRate(rateSalvate);

        LOGGER.debug("--> Exit generaRate");
        return areaRate;
    }

    /**
     * Genera le rate per l'area documento e l'area rate, ma non le salva.
     *
     * @param areaDocumento
     *            l'area documento per cui creare le rate
     * @param areaRate
     *            l'area rate a cui collegare le rate create
     * @return set ordinato di rate
     */
    private TreeSet<Rata> generaRateDocumento(IAreaDocumento areaDocumento, AreaRate areaRate) {
        CodicePagamento codicePagamento = areaRate.getCodicePagamento();
        try {
            codicePagamento = panjeaDAO.load(CodicePagamento.class, codicePagamento.getId());
        } catch (ObjectNotFoundException e1) {
            throw new RuntimeException(e1);
        }

        // costruisco la lista delle stutture castate con IFormula
        List<IFormula> formule = new ArrayList<IFormula>();
        for (IFormula formula : codicePagamento.getStrutturePartita()) {
            formule.add(formula);
        }

        BigDecimal importoRitenuta = ritenutaAccontoContabilitaManager
                .getImportoRitenutaAcconto(areaRate.getDocumento());
        Map<String, BigDecimal> variabiliFormule = new HashMap<String, BigDecimal>();
        try {
            variabiliFormule = variabiliFormulaManager.creaMapVariabili(areaDocumento, formule,
                    TIPO_OPERAZIONE_VALUTA.VALUTA);
            // per le rate il totale documento su cui generarle deve essere quello senza l'importo
            // della ritenuta
            // d'acconto visto per per quello verranno generate delle rate per gestirlo
            variabiliFormule.put(VariabiliFormulaManager.TOT,
                    variabiliFormule.get(VariabiliFormulaManager.TOT).subtract(importoRitenuta));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Collection<Rata> rate = new HashSet<Rata>();

        // Creo le rate per ogni struttura
        if (codicePagamento.getStrutturePartita() != null) {
            for (StrutturaPartita strutturaPartita : codicePagamento.getStrutturePartita()) {

                // Verifico la presenza di un calendario rate
                CalendarioRate calendarioRate = null;
                TipoEntita tipoEntita = areaDocumento.getDocumento().getTipoDocumento()
                        .getTipoEntita();
                switch (tipoEntita) {
                case CLIENTE:
                    calendarioRate = calendariRateManager.caricaCalendarioRateCliente(
                            (ClienteLite) areaDocumento.getDocumento().getEntita(),
                            strutturaPartita.getCategoriaRata());
                    break;
                case FORNITORE:
                case AZIENDA:
                    calendarioRate = calendariRateManager
                            .caricaCalendarioRateAziendale(strutturaPartita.getCategoriaRata());
                    break;
                default:
                    break;
                }

                List<Rata> rateStruttura = calcolaRate(
                        areaDocumento.getDocumento().getDataDocumento(), strutturaPartita,
                        codicePagamento.getTipologiaPartita(), calendarioRate);
                // devo valorizzare l'importo
                // L'importo da considerare è il totale documento meno l'eventuale importo della
                // ritenuta d'acconto
                // quindi
                Importo importo = areaRate.getDocumento().getTotale().clone();
                importo.setImportoInValutaAzienda(
                        importo.getImportoInValutaAzienda().subtract(importoRitenuta));
                importo.setImportoInValuta(importo.getImportoInValuta().subtract(importoRitenuta));
                valorizzaImportoRateConFormula(rateStruttura, importo,
                        areaRate.getDocumento().getTipoDocumento().isNotaCreditoEnable(),
                        strutturaPartita, variabiliFormule);
                rate.addAll(rateStruttura);
            }
        }

        // Ordino le rate (ogni struttura genera le rate con la sua numerazione
        Collection<Rata> rateOrdinate = compattaEOrdinaPerData(rate);
        // rateOrdinate.addAll(tutteLeRate);

        // numerazione finale
        TreeSet<Rata> rateDaSalvare = new TreeSet<Rata>(new Rata.Ratacomparator());
        for (Rata rata : rateOrdinate) {
            rata.setAreaRate(areaRate);
            // Valorizzo i rapporti bancari delle rate
            if (rata.getTipoPagamento().isRapportoBancarioRequired()) {
                SedeEntitaLite sedeEntita = areaRate.getDocumento().getSedeEntita() != null
                        ? areaRate.getDocumento().getSedeEntita().getSedeEntitaLite() : null;
                rata.setRapportoBancarioAzienda(aziendeManager.caricaRapportoBancarioAzienda(
                        areaRate.getDocumento().getEntita(), sedeEntita, rata.getTipoPagamento()));
                if (areaRate.getTipoAreaPartita().getTipoDocumento()
                        .getTipoEntita() == TipoEntita.CLIENTE
                        || areaRate.getTipoAreaPartita().getTipoDocumento()
                                .getTipoEntita() == TipoEntita.FORNITORE) {
                    // Rapporto bancario entita
                    rateManager.associaRapportoBancario(rata, rata.getAreaRate(),
                            rata.getTipoPagamento(), false);
                }
            }
            rateDaSalvare.add(rata);
        }

        if (areaDocumento instanceof AreaContabile) {
            Integer numeroRata = rateDaSalvare.size() + 1;
            Rata rataRA = ritenutaAccontoTesoreriaManager
                    .creaRataRitenutaAcconto((AreaContabile) areaDocumento, areaRate, numeroRata);
            if (rataRA != null) {
                rateDaSalvare.add(rataRA);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("--> Comparator utilizzato per l'ordinamento delle rata "
                    + rateDaSalvare.comparator());
        }
        return rateDaSalvare;
    }

    /**
     *
     * @param rate
     *            List<Rata>
     * @param importoBase
     *            importo con i campi codiceValuta a tassoDiCambio avvalorati.
     * @param strutturaPartita
     *            StrutturaPartita
     * @param isNotaCreditoEnabled
     *            flag nota di credito
     * @param variabiliFormule
     *            Map<String, BigDecimal>
     */
    private void valorizzaImportoRateConFormula(List<Rata> rate, Importo importoBase,
            boolean isNotaCreditoEnabled, StrutturaPartita strutturaPartita,
            Map<String, BigDecimal> variabiliFormule) {
        LOGGER.debug("--> Enter valorizzaImportoRateConFormula");
        // Attenzione : se ho due percentuali sulla rigaStruttura devo
        // valorizzare una sola rata!!!
        // devo sapere quali formule mettere in esecuzione, inltre salvo
        // l'indice dell'ultima percentuale
        boolean prima = false;
        boolean seconda = false;
        int ultimoPrimo = 0;
        int ultimoSecondo = 0;
        int numeroDecimaliValuta = valutaManager.caricaValutaAzienda(importoBase.getCodiceValuta())
                .getNumeroDecimali();
        int i = 0;
        for (RigaStrutturaPartite rigaStrutturaPartite : strutturaPartita
                .getRigheStrutturaPartita()) {
            if (BigDecimal.ZERO.compareTo(rigaStrutturaPartite.getPrimaPercentuale()) != 0) {
                prima = true;
                ultimoPrimo = i;
            }
            if (BigDecimal.ZERO.compareTo(rigaStrutturaPartite.getSecondaPercentuale()) != 0) {
                seconda = true;
                ultimoSecondo = i;
            }
            i++;
        }
        BigDecimal importoPrimo = BigDecimal.ZERO;
        BigDecimal importoSecondo = BigDecimal.ZERO;
        if (prima) {
            try {
                importoPrimo = formuleManager.calcola(strutturaPartita.getPrimaFormula(),
                        variabiliFormule, numeroDecimaliValuta);
                // tolgo eventuali segni negativi
                // importoPrimo = imp.abs();
            } catch (FormulaException e) {
                throw new RuntimeException(e);
            }
        }
        if (seconda) {
            try {
                importoSecondo = formuleManager.calcola(strutturaPartita.getSecondaFormula(),
                        variabiliFormule, numeroDecimaliValuta);
                // tolgo eventuali segni negativi
                // importoSecondo = imp.abs();
            } catch (FormulaException e) {
                throw new RuntimeException(e);
            }
        }
        i = 0;
        List<BigDecimal> ratePrima = new ArrayList<BigDecimal>();
        List<BigDecimal> rateSeconda = new ArrayList<BigDecimal>();
        BigDecimal progressivoPrimo = BigDecimal.ZERO;
        BigDecimal progressivoSecondo = BigDecimal.ZERO;
        // ciclo di calcolo sulle percentuali
        i = 0;
        for (RigaStrutturaPartite rigaStrutturaPartite : strutturaPartita
                .getRigheStrutturaPartita()) {
            if (i == ultimoPrimo) {
                // trovo importo per differenza
                BigDecimal result = importoPrimo.subtract(progressivoPrimo)
                        .setScale(numeroDecimaliValuta, RoundingMode.HALF_UP);
                ratePrima.add(result);
            }
            if (i == ultimoSecondo) {
                // trovo importo per differenza
                BigDecimal result = importoSecondo.subtract(progressivoSecondo)
                        .setScale(numeroDecimaliValuta, RoundingMode.HALF_UP);
                rateSeconda.add(result);
            }
            // importi normali
            if (i < ultimoPrimo) {
                BigDecimal resultPrimo = importoPrimo
                        .multiply((rigaStrutturaPartite.getPrimaPercentuale()
                                .divide(Importo.HUNDRED)))
                        .setScale(numeroDecimaliValuta, RoundingMode.HALF_UP);
                ratePrima.add(resultPrimo);
                progressivoPrimo = progressivoPrimo.add(resultPrimo).setScale(numeroDecimaliValuta,
                        RoundingMode.HALF_UP);
            } else if (i > ultimoPrimo) {
                ratePrima.add(BigDecimal.ZERO);
            }
            if (i < ultimoSecondo) {
                BigDecimal resultSecondo = importoSecondo
                        .multiply((rigaStrutturaPartite.getSecondaPercentuale()
                                .divide(Importo.HUNDRED)))
                        .setScale(numeroDecimaliValuta, RoundingMode.HALF_UP);
                rateSeconda.add(resultSecondo);
                progressivoSecondo = progressivoSecondo.add(resultSecondo)
                        .setScale(numeroDecimaliValuta, RoundingMode.HALF_UP);
            } else if (i > ultimoSecondo) {
                rateSeconda.add(BigDecimal.ZERO);
            }
            i++;
        }
        // ciclo di somma sulla rata
        i = 0;
        for (Rata rata : rate) {
            Importo importoSomma = importoBase;
            BigDecimal somma = ratePrima.get(i).add(rateSeconda.get(i))
                    .setScale(numeroDecimaliValuta, RoundingMode.HALF_UP);

            importoSomma.setImportoInValuta(somma);
            int numeroDecimaliValutaAzienda = valutaManager.caricaValutaAziendaCorrente()
                    .getNumeroDecimali();
            importoSomma.calcolaImportoValutaAzienda(numeroDecimaliValutaAzienda);
            rata.setImporto(importoSomma.clone());
            i++;
        }
        LOGGER.debug("--> Exit valorizzaImportoRateConFormula");
    }

}
