package it.eurotn.panjea.magazzino.domain;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.log4j.Logger;

import it.eurotn.panjea.anagrafica.domain.ConversioneUnitaMisura;
import it.eurotn.panjea.magazzino.domain.TipoAttributo.ETipoDatoTipoAttributo;
import it.eurotn.panjea.magazzino.exception.FormuleTipoAttributoException;
import it.eurotn.panjea.mrp.domain.Moltiplicatore;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;

public class FormuleRigaArticoloCalculator {

    public interface FormulaCalculatorClosure {

        /**
         * Closure per modificare la rigaArticoloDocumento calcolata (riga e componenti in caso sia una rigaDistinta).
         * <br>
         * Nota che questo metodo è generico, al momento lo utilizzo nell'ordine, quindi non mi preoccupo della
         * qtaMagazzino che può avere nel magazzino una formula di conversione unità di misura associata. Se questo
         * metodo servisse nel magazzino, sarebbe meglio separare applyQta e applyQtaMagazzino.
         *
         * @param rigaArticoloDocumento
         *            rigaArticoloDocumento
         * @return rigaArticoloDocumento
         */
        IRigaArticoloDocumento apply(IRigaArticoloDocumento rigaArticoloDocumento);
    }

    private static final Logger LOGGER = Logger.getLogger(FormuleRigaArticoloCalculator.class);

    private ScriptEngineManager manager;
    private ScriptEngine engine;
    private Moltiplicatore moltiplicatore;
    private FormulaCalculatorClosure formulaCalculatorClosure;

    private Map<String, Object> variabili;
    private FormuleTipoAttributoException formuleTipoAttributoException = null;

    /**
     * Costruttore.
     */
    public FormuleRigaArticoloCalculator() {
        // Costruisco nel costruttore per evitare di ricorstruirlo ogni volta
        manager = new ScriptEngineManager();
        engine = manager.getEngineByName("js");
        moltiplicatore = new Moltiplicatore();
    }

    /**
     * @param attributoRiga
     *            attributo da considerare
     * @return true se l'attributo deve essere calcolato
     */
    protected boolean attributoCalcolato(AttributoRigaArticolo attributoRiga) {
        return attributoRiga.getFormula() != null;
    }

    /**
     * Calcola il valore di tutti gli attributi e delle qta in base alle formule di trasformazione assegnate.
     *
     * @param rigaArticoloDocumento
     *            rigaArticoloDocumento
     * @return rigaArticoloDocumento con qta,qtaMagazzino e attributi calcolati in base alle formule impostate.
     * @throws FormuleTipoAttributoException
     *             errore nel calcolo della formula
     */
    public IRigaArticoloDocumento calcola(IRigaArticoloDocumento rigaArticoloDocumento)
            throws FormuleTipoAttributoException {

        return calcola(rigaArticoloDocumento, rigaArticoloDocumento);
    }

    /**
     * Calcola ricorsivamente tutte le formule quantità e attibuti presenti sulla riga ecomponenti.
     *
     * @param rigaArticoloDocumento
     *            riga di riferimento
     * @param rigaComponente
     *            riga componente
     * @return riga
     * @throws FormuleTipoAttributoException
     *             errore nel calcolo della formula
     */
    private IRigaArticoloDocumento calcola(IRigaArticoloDocumento rigaArticoloDocumento,
            IRigaArticoloDocumento rigaComponente) throws FormuleTipoAttributoException {
        formuleTipoAttributoException = new FormuleTipoAttributoException();
        List<AttributoRigaArticolo> attributi = new ArrayList<AttributoRigaArticolo>(
                rigaArticoloDocumento.getAttributi());

        if (formulaCalculatorClosure != null) {
            rigaComponente = formulaCalculatorClosure.apply(rigaComponente);
        }

        AttributoRiga attributoQta = creaAttributoQta(rigaComponente);
        attributi.add(attributoQta);

        AttributoRiga attributoQtaMagazzino = creaAttributoQtaMagazzino(rigaComponente);
        attributi.add(attributoQtaMagazzino);

        List<AttributoRiga> attributiQtaComponenti = creaAttributiQtaComponenti(rigaComponente);
        attributi.addAll(attributiQtaComponenti);

        List<AttributoRiga> attributiTrasmessiComponenti = creaAttributiTrasmessiComponenti(rigaComponente);
        attributi.addAll(attributiTrasmessiComponenti);

        variabili = new HashMap<String, Object>();
        List<AttributoRigaArticolo> attributiDaCalcolare = new ArrayList<AttributoRigaArticolo>();

        // aggiungo tutti gli attributi avvalorati e assegno quelli da calcolare
        for (AttributoRigaArticolo attributoRiga : attributi) {
            if (attributoCalcolato(attributoRiga)) {
                attributiDaCalcolare.add(attributoRiga);
            } else {
                variabili.put(attributoRiga.getTipoAttributo().getCodiceFormula(), attributoRiga.getValoreTipizzato());
            }
        }

        Map<AttributoRigaArticolo, BigDecimal> formuleCalcolate;
        List<AttributoRigaArticolo> attributiNonCalcolati;
        boolean esci = false;
        do {
            attributiNonCalcolati = new ArrayList<AttributoRigaArticolo>();
            formuleCalcolate = calcolaFormule(rigaArticoloDocumento, attributiDaCalcolare, attributiNonCalcolati);
            esci = formuleCalcolate.isEmpty() || formuleCalcolate.size() == attributiDaCalcolare.size();
            attributiDaCalcolare = attributiNonCalcolati;
        } while (!esci);

        // Gestisco le situazioni di errore lanciando l'eccezione con le
        // informazioni riguardanti gli errori sulle
        // formule relative ai tipi attributi
        if (!attributiNonCalcolati.isEmpty() && !formuleTipoAttributoException.isEmpty()) {
            throw formuleTipoAttributoException;
        }

        // Assegno i valori agli attributi.
        for (AttributoRigaArticolo attributoRigaArticolo : attributi) {
            if (attributoRigaArticolo.getFormula() != null) {
                Object result = variabili.get(attributoRigaArticolo.getTipoAttributo().getCodiceFormula());
                String resultString;
                try {
                    resultString = new DefaultNumberFormatterFactory("#,##0",
                            attributoRigaArticolo.getTipoAttributo().getNumeroDecimali(), BigDecimal.class)
                                    .getDefaultFormatter().valueToString(result);
                } catch (ParseException e) {
                    LOGGER.error("-->errore nel fare il parse del valore " + result, e);
                    resultString = "0";
                }
                attributoRigaArticolo.setValore(resultString);
            }
        }

        // NB!!! non riassegno gli attributi perchè se ho collegato la sessione di hibernate la vede come una lista
        // nuova e cerca di cancellare la precedente generando un errore di delete-orphan.
        if (rigaArticoloDocumento.equals(rigaComponente)) {
            // Se la qta aveva una formula la setto
            if (attributoQta.getFormula() != null) {
                Number value = (Number) variabili.get(attributoQta.getTipoAttributo().getCodiceFormula());
                rigaArticoloDocumento.setQta(value == null ? 0.0 : value.doubleValue());
            }
            // Se la qtaMagazzino aveva una formula la setto
            if (attributoQtaMagazzino.getFormula() != null) {
                Number value = (Number) variabili.get(attributoQtaMagazzino.getTipoAttributo().getCodiceFormula());
                rigaArticoloDocumento.setQtaMagazzino(value == null ? null : value.doubleValue());
            } else {
                rigaArticoloDocumento.setQtaMagazzino(getQtaMagazzinoDaConversioneUnitaMisura(rigaArticoloDocumento));
            }
        }

        // attributiComponenti è stato calcolato per riferimento. Cambiare appena possibile per comporensioen e bug che
        // ne possono derivare.
        if (!attributiQtaComponenti.isEmpty()) {
            int position = 0;
            for (IRigaArticoloDocumento riga : rigaComponente.getComponenti()) {
                // so che l'ordine dei componenti è lo stasso quando ho creato
                // gli attributi per il calcolo della
                // qtaComponente e la formula è obbligatoria quindi ci sarà un
                // attributo per componente;
                // restituisco l'attributo all'indice della collection dei
                // componenti
                AttributoRiga attributoComponente = attributiQtaComponenti.get(position);
                Number value = BigDecimal.ZERO;
                if (((IRigaComponente) riga).getFormulaComponente() != null) {
                    value = (Number) variabili.get(attributoComponente.getTipoAttributo().getCodiceFormula());
                } else {
                    value = BigDecimal.valueOf(riga.getQta());
                }
                riga.setQta(value == null ? null : value.doubleValue());
                riga.setQtaMagazzino(value == null ? null : value.doubleValue());

                position++;
            }
        }

        if (rigaComponente.getComponenti() != null) {
            for (IRigaArticoloDocumento componente : rigaComponente.getComponenti()) {
                calcola(rigaArticoloDocumento, componente);
            }
        }

        return rigaArticoloDocumento;
    }

    /**
     * Calcola le formule per tutti gli attributi. AttributoRigaArticolo.
     *
     * @param rigaArticoloDocumento
     *            rigaArticoloDocumento
     * @param attributiDaCalcolare
     *            attributi con le formule da calcolare
     * @param attributiNonCalcolati
     *            valori che non posso calcolare (perchè dipendenti da altri)
     * @return variabili calcolate
     */
    private Map<AttributoRigaArticolo, BigDecimal> calcolaFormule(IRigaArticoloDocumento rigaArticoloDocumento,
            List<AttributoRigaArticolo> attributiDaCalcolare, List<AttributoRigaArticolo> attributiNonCalcolati) {
        Map<AttributoRigaArticolo, BigDecimal> attributiCalcolati = new HashMap<AttributoRigaArticolo, BigDecimal>();
        for (AttributoRigaArticolo attributoRigaArticolo : attributiDaCalcolare) {
            try {
                BigDecimal result = (BigDecimal) attributoRigaArticolo.getFormula().calcola(moltiplicatore, variabili,
                        attributoRigaArticolo.getTipoAttributo().getNumeroDecimali(), rigaArticoloDocumento.getQta());
                variabili.put(attributoRigaArticolo.getTipoAttributo().getCodiceFormula(), result);
                attributiCalcolati.put(attributoRigaArticolo, result);
            } catch (Exception e) {
                attributiNonCalcolati.add(attributoRigaArticolo);
                formuleTipoAttributoException.addErroreAttributo(attributoRigaArticolo.getTipoAttributo().getCodice(),
                        e);
            }
        }
        return attributiCalcolati;
    }

    /**
     * Crea la lista di attributi fittizi utili per il calcolo della qta del componente.
     *
     * @param rigaArticoloDocumento
     *            la riga documento
     * @return la lista di attributi contenente n elementi quanti sono i componenti della riga o una lista vuota nel
     *         caso non ci fossero componenti associati
     */
    private List<AttributoRiga> creaAttributiQtaComponenti(IRigaArticoloDocumento rigaArticoloDocumento) {
        List<AttributoRiga> attributiComponenti = new ArrayList<AttributoRiga>();
        int position = 0;
        if (rigaArticoloDocumento.getComponenti() != null) {
            for (IRigaArticoloDocumento rigaDocumento : rigaArticoloDocumento.getComponenti()) {
                position++;

                AttributoRiga attributoQtaComponente = new AttributoRiga();
                attributoQtaComponente.setId(position);
                TipoAttributo tipoAttributoQtaMagazzino = new TipoAttributo();
                tipoAttributoQtaMagazzino.setCodice("qtaComponente" + position);
                tipoAttributoQtaMagazzino.setTipoDato(ETipoDatoTipoAttributo.NUMERICO);
                IRigaComponente rigaComponente = (IRigaComponente) rigaDocumento;
                tipoAttributoQtaMagazzino.setNumeroDecimali(rigaComponente.getNumeroDecimaliQta());
                attributoQtaComponente.setTipoAttributo(tipoAttributoQtaMagazzino);
                attributoQtaComponente.setRicalcolaInEvasione(true);

                String formulaComponente = rigaComponente.getFormulaComponente();
                if (formulaComponente == null || formulaComponente.isEmpty()) {
                    formulaComponente = rigaComponente.getArticolo().getCategoria() != null
                            ? rigaComponente.getArticolo().getCategoria().getFormulaPredefinitaComponente() : null;
                }

                if (formulaComponente != null) {
                    FormulaTrasformazione formulaQtaComponente = new FormulaTrasformazione();
                    formulaQtaComponente.setCodice("qtaComponente" + position);
                    formulaQtaComponente.setFormula(formulaComponente);

                    attributoQtaComponente.setFormula(formulaQtaComponente);
                    attributoQtaComponente.setValore(null);
                } else if (rigaComponente.getQta() != null) {
                    attributoQtaComponente.setValore(rigaComponente.getQta().toString().replace(".", ","));
                }

                attributiComponenti.add(attributoQtaComponente);
            }
        }
        return attributiComponenti;
    }

    /**
     * Crea la lista di attributi fittizi utili per il calcolo della qta del componente.
     *
     * @param rigaArticoloDocumento
     *            la riga documento
     * @return la lista di attributi contenente n elementi quanti sono i componenti della riga o una lista vuota nel
     *         caso non ci fossero componenti associati
     */
    private List<AttributoRiga> creaAttributiTrasmessiComponenti(IRigaArticoloDocumento rigaArticoloDocumento) {
        List<AttributoRiga> attributiComponenti = new ArrayList<AttributoRiga>();
        if (rigaArticoloDocumento.getComponenti() != null) {
            for (IRigaArticoloDocumento rigaDocumento : rigaArticoloDocumento.getComponenti()) {
                IRigaComponente rigaComponente = (IRigaComponente) rigaDocumento;

                if (rigaComponente.getArticolo().getAttributiArticolo() != null
                        && rigaComponente.getArticolo().getAttributo(Articolo.ATTRIBUTO_TRASMETTI_ATTRIBUTI) != null) {
                    List<AttributoArticolo> attributiArticolo = rigaComponente.getArticolo().getAttributiArticolo();
                    for (AttributoArticolo attributoArticolo : attributiArticolo) {
                        if (attributoArticolo.getTipoAttributo().getCodice()
                                .equals(Articolo.ATTRIBUTO_TRASMETTI_ATTRIBUTI)) {
                            continue;
                        }

                        AttributoRiga attributoComponente = new AttributoRiga();
                        attributoComponente.setTipoAttributo(attributoArticolo.getTipoAttributo());
                        attributoComponente.setValore(attributoArticolo.getValore());

                        attributiComponenti.add(attributoComponente);
                    }
                }
            }
        }
        return attributiComponenti;
    }

    /**
     * @param rigaArticoloDocumento
     *            riga dalla quale recuperare l'att qta
     * @return attributoRiga con il valore e la formula per la qta
     */
    protected AttributoRiga creaAttributoQta(IRigaArticoloDocumento rigaArticoloDocumento) {
        TipoAttributo tipoAttributo = new TipoAttributo();
        tipoAttributo.setCodice("qta");
        tipoAttributo.setTipoDato(ETipoDatoTipoAttributo.NUMERICO);
        tipoAttributo.setNumeroDecimali(rigaArticoloDocumento.getNumeroDecimaliQta());

        AttributoRiga attributoQta = new AttributoRiga();
        attributoQta.setId(100);
        attributoQta.setTipoAttributo(tipoAttributo);
        attributoQta.setFormula(rigaArticoloDocumento.getFormulaTrasformazione());
        if (attributoQta.getFormula() != null) {
            attributoQta.setValore(null);
        } else if (rigaArticoloDocumento.getQta() != null) {
            attributoQta.setValore(rigaArticoloDocumento.getQta().toString().replace(".", ","));
        }
        return attributoQta;
    }

    /**
     *
     * @param rigaArticoloDocumento
     *            riga dalla quale recuperare l'att qtaMagazzino
     * @return attributoRiga con il valore e la formula per la qtaMagazzino
     */
    protected AttributoRiga creaAttributoQtaMagazzino(IRigaArticoloDocumento rigaArticoloDocumento) {
        TipoAttributo tipoAttributoQtaMagazzino = new TipoAttributo();
        tipoAttributoQtaMagazzino.setCodice("qtaMagazzino");
        tipoAttributoQtaMagazzino.setTipoDato(ETipoDatoTipoAttributo.NUMERICO);
        tipoAttributoQtaMagazzino.setNumeroDecimali(rigaArticoloDocumento.getNumeroDecimaliQta());

        AttributoRiga attributoQtaMagazzino = new AttributoRiga();
        attributoQtaMagazzino.setId(101);
        attributoQtaMagazzino.setTipoAttributo(tipoAttributoQtaMagazzino);
        FormulaTrasformazione formulaTrasformazioneQtaMagazzino = rigaArticoloDocumento
                .getFormulaTrasformazioneQtaMagazzino();
        attributoQtaMagazzino.setFormula(formulaTrasformazioneQtaMagazzino);
        attributoQtaMagazzino.setValore(null);
        attributoQtaMagazzino.setRicalcolaInEvasione(true);
        return attributoQtaMagazzino;
    }

    /**
     * Calcola la qta magazzino nel caso in cui la qta magazzino abbia una unità di misura diversa dalla qta e ci sia
     * configurata una formula di trasformazione tra le due unità di misura; in caso non ci sia una formula di
     * conversione unità di misura viene restituita la qta della riga articolo.
     *
     * @param rigaArticoloDocumento
     *            rigaArticoloDocumento
     * @return la qta calcolata tramite la formula conversione unità di misura o la qta della riga
     */
    private Double getQtaMagazzinoDaConversioneUnitaMisura(IRigaArticoloDocumento rigaArticoloDocumento) {
        BigDecimal qtaMagaConvertita = null;
        String um = rigaArticoloDocumento.getUnitaMisura();
        String umMaga = rigaArticoloDocumento.getUnitaMisuraQtaMagazzino();
        // solo se le unità di misura sono diverse
        if (um != null && umMaga != null && !um.equals(umMaga) && rigaArticoloDocumento.getQta() != null) {
            String formulaConversioneUm = rigaArticoloDocumento.getFormulaConversioneUnitaMisura();

            // e solo se c'è una conversione unità di misura impostata tra le
            // unità di misura
            if (formulaConversioneUm != null) {
                ConversioneUnitaMisura conversioneUm = new ConversioneUnitaMisura();
                conversioneUm.setFormula(formulaConversioneUm);
                qtaMagaConvertita = (BigDecimal) conversioneUm.converti(
                        BigDecimal.valueOf(rigaArticoloDocumento.getQta()),
                        rigaArticoloDocumento.getNumeroDecimaliQta(), engine);
            }
        }
        return qtaMagaConvertita != null ? qtaMagaConvertita.doubleValue() : rigaArticoloDocumento.getQta();
    }

    /**
     * @param formulaCalculatorClosure
     *            formulaCalculatorClosure
     */
    public void setFormulaCalculatorClosure(FormulaCalculatorClosure formulaCalculatorClosure) {
        this.formulaCalculatorClosure = formulaCalculatorClosure;
    }

}
