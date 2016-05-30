/**
 *
 */
package it.eurotn.rich.binding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.ColorUIResource;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.validation.support.RulesValidator;
import org.springframework.binding.value.ValueModel;
import org.springframework.richclient.form.binding.support.CustomBinding;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.Rules;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.TypeResolvableConstraint;
import org.springframework.rules.support.DefaultRulesSource;

import foxtrot.AsyncTask;
import foxtrot.AsyncWorker;
import it.eurotn.panjea.anagrafica.domain.CambioValuta;
import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.service.exception.CambioNonPresenteException;
import it.eurotn.panjea.rich.bd.IValutaBD;
import it.eurotn.panjea.rich.bd.ValutaAziendaCache;
import it.eurotn.panjea.rich.bd.ValutaBD;
import it.eurotn.rich.components.ImportoTextField;

/**
 * Binding per l'oggetto {@link Importo}. <br/>
 * numeroDecimali settato a 2 di default. Impostare il property path nel context per indicare quale proprietà utilizzare
 * per il numero di decimali e arrotondare l'importo al variare della proprietà
 *
 * @author adriano
 * @version 1.0, 30/mag/08
 */
public class ImportoBinding extends CustomBinding {

    private class DataValutaPropertyChange implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getNewValue() != null && !getFormModel().isReadOnly()) {
                aggiornaTasso((Date) evt.getNewValue());
                setToolTip();
            }
        }

    }

    private class DocumentTextFieldListener implements DocumentListener {
        /**
         * Aggiorna il valueModel dell'importo ricalcolando l'importo in valuta azienda.
         */
        private void aggiornaImporto() {
            if (importoTextField == null) {
                return;
            }
            if (importoTextField.isFormattingText()) {
                return;
            }

            if (formModel == null) {
                return;
            }

            Importo importo = (Importo) getValue();

            if (importo == null) {
                return;
            }
            if (importo.getImportoInValuta() == null) {
                return;
            }

            if (importoTextField.getValue() != null && !formModel.isReadOnly()
                    && importoTextField.getTextField().isFocusOwner()
                    && importo.getImportoInValuta().compareTo(importoTextField.getValue()) != 0) {
                Importo importoClone = importo.clone();
                importoClone.setImportoInValuta(importoTextField.getValue());
                importoClone.calcolaImportoValutaAzienda(getNtOfDecimalValutaAzienda());
                controlValueChanged(importoClone);
                setToolTip();
            }
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            aggiornaImporto();
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            aggiornaImporto();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            aggiornaImporto();
        }

    }

    private class ImportoRirefimentoPropertyChange implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (formModel.isReadOnly()) {
                return;
            }
            // Se cambia il codice valuta del valueModel di riferimento cambio anche il mio.
            Importo importoDiRiferimento = (Importo) evt.getNewValue();
            BigDecimal tasso = BigDecimal.ONE;
            if (importoDiRiferimento != null) {
                tasso = importoDiRiferimento.getTassoDiCambio();
                aggiornaValuta(importoDiRiferimento.getCodiceValuta());
            }
            Importo importo = (Importo) getValue();
            importo.setCodiceValuta(valuta.getCodiceValuta());
            if (tasso.compareTo(importo.getTassoDiCambio()) != 0) {
                importo.setTassoDiCambio(tasso);
                importo.calcolaImportoValutaAzienda(getNtOfDecimalValutaAzienda());
            }
            setToolTip();
            controlValueChanged(importo.clone());
        }

    }

    public class NumeroDecimaliPropertyChange implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Integer numeroDecimali = (Integer) evt.getNewValue();
            if (numeroDecimali != null) {
                setNrOfDecimalsInternal(numeroDecimali);
            }
        }

    }

    private final class TassoRuleConstraint extends TypeResolvableConstraint implements Constraint {
        /**
         * Costruttore.
         */
        public TassoRuleConstraint() {
            super("tassoNonValidoRule");
        }

        @Override
        public boolean test(Object obj) {
            return ((Importo) obj).getTassoDiCambio().compareTo(BigDecimal.ZERO) > 0;
        }
    }

    private static Logger logger = Logger.getLogger(ImportoBinding.class);
    /**
     * Indica che il numero decimali è legato alla valuta.<br/>
     * . Se il numero decimali sono setttati tramite {@link ImportoBinding#setNrOfDecimals(Integer)} questa proprietà
     * viene settata a false
     */
    private boolean numeroDecimaliFromValuta = true;

    protected Integer nrOfDecimals = 2;
    private Integer nrOfNonDecimals = 10;
    private ValutaAzienda valuta = null;
    private final ImportoTextField importoTextField;
    private ImportoTextField tassoTextField;

    private ValueModel dataCambioValueModel = null;

    private ValutaAziendaCache valutaCache;

    private ValueModel numeroDecimaliValueModel;
    private final IValutaBD valutaBD;

    /**
     * Constructor di {@link ImportoBinding}.
     * 
     * @param formModel
     *            .
     * @param formPropertyPath
     *            .
     * @param requiredSourceClass
     *            .
     * @param component
     *            .
     */
    public ImportoBinding(final FormModel formModel, final String formPropertyPath, final Class<?> requiredSourceClass,
            final ImportoTextField component) {
        super(formModel, formPropertyPath, null);
        this.importoTextField = component;
        valutaBD = RcpSupport.getBean(ValutaBD.BEAN_ID);
    }

    /**
     * Installa automaticamente la rules per il tasso di cambio negativo (non valido).
     */
    private void addTassoRules() {

        Constraint tassoRequiredConstraint = new TassoRuleConstraint();

        DefaultRulesSource rulesSource = RcpSupport.getBean("rulesSource");

        Rules rules = rulesSource.getRules(formModel.getFormObject().getClass());
        // se non ho impostato nessula regola la rules risulta essere null
        if (rules != null) {
            rules.add(getProperty(), tassoRequiredConstraint);
            ((ValidatingFormModel) formModel).setValidator(new RulesValidator(formModel, rulesSource));
            ((ValidatingFormModel) formModel).validate();
        }
    }

    /**
     * Aggiorna il tasso alla data indicata.
     * 
     * @param data
     *            data alla quale aggiornare il tasso
     */
    private void aggiornaTasso(Date data) {
        Importo importo = (Importo) getValue();
        boolean tassoCambiato = false;

        if (valuta.getCodiceValuta().equals(getValutaBD().caricaValutaAziendaCorrente().getCodiceValuta())) {

            // E' utilizzata la valuta aziendale
            // Dato che il tasso è sempre 1 non serve ricalcolare nulla tranne nel caso in cui
            // abbia cambiato entità passando da una con valuta diversa a quella aziendale ad una
            // con valuta aziendale.
            // In questo caso il tasso di cambio in importo sarebbe quello dell'altra valuta
            // e la devo reimpostare ad 1.

            tassoCambiato = !importo.getTassoDiCambio().equals(BigDecimal.ONE);
            importo.setTassoDiCambio(BigDecimal.ONE);
        } else {
            try {
                CambioValuta cambio = valutaBD.caricaCambioValuta(valuta.getCodiceValuta(), data);
                tassoCambiato = importo.getTassoDiCambio().equals(cambio.getTasso());
                importo.setTassoDiCambio(cambio.getTasso());
            } catch (CambioNonPresenteException e) {
                importo.setTassoDiCambio(new BigDecimal(-1));
            }
        }

        importo.calcolaImportoValutaAzienda(getNtOfDecimalValutaAzienda());
        if (tassoTextField != null) {
            tassoTextField.setValue(importo.getTassoDiCambio());
        }

        if (tassoCambiato) {
            controlValueChanged(importo.clone());
        }
    }

    /**
     * Aggiorna variabile membro valuta in base al codice valuta.
     * 
     * @param codiceValuta
     *            stringa della valuta da caricare
     */
    private void aggiornaValuta(String codiceValuta) {
        if (codiceValuta == null) {
            return;
        }

        if (valuta == null || (valuta != null && !valuta.getCodiceValuta().equals(codiceValuta))) {
            // Per performance controllo se la valuta è quella aziendale. In caso prendo quello
            // senza dover fare
            // richieste al server.
            if (codiceValuta.equals(getValutaBD().caricaValutaAziendaCorrente().getCodiceValuta())) {
                valuta = getValutaBD().caricaValutaAziendaCorrente();
            } else {
                valuta = getValutaBD().caricaValutaAzienda(codiceValuta);
            }
            importoTextField.setLabelText(valuta.getSimbolo());
            setToolTip();
            aggiornaVisibilitaTassoField();
            // se devo controllare una data per aggiornare il tasso al cambio
            // della valuta devo aggiornarlo, altrimenti non faccio nulla
            if (dataCambioValueModel != null && !getFormModel().isReadOnly()) {
                aggiornaTasso((Date) dataCambioValueModel.getValue());
            }
            // Se i numeri decimali non sono variabili dipendentemente da un campo
            // devo aggiornarli con i decimali della valuta.
            if (numeroDecimaliFromValuta) {
                int numeroDecimali = valuta.getNumeroDecimali() == null ? 2 : valuta.getNumeroDecimali();
                setNrOfDecimalsInternal(numeroDecimali);
            }
        }
    }

    /**
     * Se la valuta è quella aziendale nasconde il campo tasso.
     */
    private void aggiornaVisibilitaTassoField() {
        if (tassoTextField != null) {
            tassoTextField.setVisible(
                    !valuta.getCodiceValuta().equals(getValutaBD().caricaValutaAziendaCorrente().getCodiceValuta()));
        }
    }

    /**
     * Applica il context di ImportoBinding : ValueModel per il numero di decimali e ValueModel per il controllo del
     * codiceValuta.
     * 
     * @param context
     *            contesto per il binding
     */
    public void applyContext(Map<String, Object> context) {
        if (context.containsKey(ImportoBinder.IMPORTO_RIFERIMENTO_PATH_KEY)) {
            ValueModel importoValueModel = getFormModel()
                    .getValueModel(context.get(ImportoBinder.IMPORTO_RIFERIMENTO_PATH_KEY).toString());
            importoValueModel.addValueChangeListener(new ImportoRirefimentoPropertyChange());
        }

        if (context.containsKey(ImportoBinder.DATA_CAMBIO_PATH)) {
            dataCambioValueModel = getFormModel().getValueModel(context.get(ImportoBinder.DATA_CAMBIO_PATH).toString());
            dataCambioValueModel.addValueChangeListener(new DataValutaPropertyChange());
        }

        setNrOfNonDecimals(10);
        importoTextField.setNrOfDecimals(6);
        if (context.containsKey(ImportoBinder.NUMERO_DECIMALI_PATH_KEY)) {
            numeroDecimaliFromValuta = false;
            numeroDecimaliValueModel = getFormModel()
                    .getValueModel(context.get(ImportoBinder.NUMERO_DECIMALI_PATH_KEY).toString());
            nrOfDecimals = (Integer) numeroDecimaliValueModel.getValue();
            importoTextField.setNrOfDecimals(nrOfDecimals);
            numeroDecimaliValueModel.addValueChangeListener(new NumeroDecimaliPropertyChange());
        }
        // inizialmente imposto la valuta a quella aziendale, poi chiamo l'aggiorna valuta solametne
        // per aggiornare i
        // controlli (le textfield di importo e cambio)
        aggiornaValuta(getValutaBD().caricaValutaAziendaCorrente().getCodiceValuta());
        importoTextField.getTextField().setName(getFormModel().getId() + "." + getProperty());
        importoTextField.getLabel().setName(getFormModel().getId() + "." + getProperty() + ".datiValuta");
        importoTextField.setNrOfNonDecimals(nrOfNonDecimals);
        addTassoRules();
    }

    @Override
    protected JComponent createControl() {
        JComponent control = doBindControl();
        readOnlyChanged();
        return control;
    }

    @Override
    protected JComponent doBindControl() {
        logger.debug("--> Enter doBindControl");
        // Inizialmente carico la valuta aziendale.
        importoTextField.getTextField().getDocument().addDocumentListener(new DocumentTextFieldListener());

        if (dataCambioValueModel != null) {
            // se ho una data da verificare è il controllo che gestisce il tasso.
            // Visualizzo quindi anche il tasso.
            tassoTextField = new ImportoTextField();
            tassoTextField.setLabelText(RcpSupport.getMessage("tasso"));
            tassoTextField.setNrOfDecimals(6);
        }
        logger.debug("--> Exit doBindControl");
        return importoTextField;
    }

    @Override
    protected void enabledChanged() {
        logger.debug("--> Enter enabledChanged " + getProperty() + " " + isEnabled());
        setReadOnly(!isEnabled());
        logger.debug("--> Exit enabledChanged");
    }

    /**
     * @return Returns the importoTextField.
     */
    public ImportoTextField getImportoTextField() {
        return importoTextField;
    }

    /**
     * Numero decimali da utilizzare per il calcolo della valuta azienda.
     * 
     * @return numero decimali
     */
    private Integer getNtOfDecimalValutaAzienda() {
        if (numeroDecimaliFromValuta) {
            return getValutaBD().caricaValutaAziendaCorrente().getNumeroDecimali();
        }

        return nrOfDecimals;
    }

    /**
     * @return Returns the tassoTextField.
     */
    public ImportoTextField getTassoTextField() {
        return tassoTextField;
    }

    /**
     * 
     * @return bd per la gestione della valuta
     */
    private ValutaAziendaCache getValutaBD() {
        if (valutaCache == null) {
            valutaCache = RcpSupport.getBean(ValutaAziendaCache.BEAN_ID);
        }
        return valutaCache;
    }

    @Override
    protected void readOnlyChanged() {
        logger.debug("--> Enter readOnlyChanged " + getProperty() + " " + isReadOnly());
        importoTextField.getTextField().setEditable(!isReadOnly());

        ColorUIResource color;
        if (!UIManager.getLookAndFeel().getName().equals("Nimbus")) {
            if (!isReadOnly()) {
                color = ((ColorUIResource) UIManager.get("TextField.background"));
            } else {
                color = ((ColorUIResource) UIManager.get("TextField.inactiveBackground"));
            }
            importoTextField.setBackground(color);
            if (tassoTextField != null) {
                tassoTextField.setBackground(color);
            }
        }
        logger.debug("--> Exit readOnlyChanged");
    }

    /**
     * Cambia il numero di decimali inseribili..<br/>
     * Impostando questa proprietà i numeri decimali non vengono presi dalla valuta.
     * 
     * @param nrOfDecimals
     *            The nrOfDecimals to set.
     */
    public void setNrOfDecimals(Integer nrOfDecimals) {
        numeroDecimaliFromValuta = false;
        setNrOfDecimalsInternal(nrOfDecimals);
    }

    /**
     * Cambia il numero di decimali inseribili.
     * 
     * @param nrOfDecimalsParam
     *            numeri di decimali da settare.
     */
    private void setNrOfDecimalsInternal(Integer nrOfDecimalsParam) {
        this.nrOfDecimals = nrOfDecimalsParam;
        importoTextField.setNrOfDecimals(nrOfDecimals);
        // devo aggiornare il testo riformattandolo
        Importo importoCorrente = (Importo) getValue();
        importoTextField.setValue(importoCorrente.getImportoInValuta());
        importoCorrente.calcolaImportoValutaAzienda(getNtOfDecimalValutaAzienda());
        if (!getFormModel().isReadOnly()) {
            controlValueChanged(importoCorrente.clone());
        }

        setToolTip();
    }

    /**
     * @param nrOfNonDecimals
     *            The nrOfNonDecimals to set.
     */
    public void setNrOfNonDecimals(Integer nrOfNonDecimals) {
        this.nrOfNonDecimals = nrOfNonDecimals;
    }

    /**
     * Aggiorna il tooltips della casella di testo.
     */
    private void setToolTip() {
        AsyncWorker.post(new AsyncTask() {

            @Override
            public void failure(Throwable ex) {
                logger.error("-->errore nel settare il tooltip", ex);
            }

            @Override
            public Object run() throws Exception {
                Importo importo = (Importo) getValue();
                StringBuilder sb = new StringBuilder("<HTML><B>");
                if (importo != null) {
                    sb.append(getValutaBD().caricaValutaAziendaCorrente().getSimbolo());
                    sb.append(" </B>");
                    sb.append(importo.getImportoInValutaAzienda());
                    sb.append("<br><b>Cambio: </>");
                    sb.append(importo.getTassoDiCambio());
                }
                sb.append("</HTML>");
                return sb.toString();
            }

            @Override
            public void success(Object tooltip) {
                importoTextField.getLabel().setToolTipText(tooltip.toString());
            }
        });
    }

    @Override
    protected void valueModelChanged(Object value) {
        logger.debug("--> Enter valueModelChanged " + getProperty() + " " + value);
        Importo nuovoImporto = (Importo) value;
        importoTextField.setValue(nuovoImporto.getImportoInValuta());
        // se la valuta cambia viene aggiornata
        aggiornaValuta(nuovoImporto.getCodiceValuta());
        setToolTip();
    }
}
