package it.eurotn.rich.binding.codice;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang3.StringUtils;
import org.springframework.binding.PropertyAccessStrategy;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.validation.support.RulesValidator;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.swing.AsYouTypeTextComponentAdapter;
import org.springframework.context.NoSuchMessageException;
import org.springframework.richclient.application.ApplicationServicesLocator;
import org.springframework.richclient.dialog.CloseAction;
import org.springframework.richclient.form.binding.support.AbstractBinding;
import org.springframework.richclient.image.IconSource;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.Rules;
import org.springframework.rules.closure.Closure;
import org.springframework.rules.constraint.And;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.Or;
import org.springframework.rules.constraint.Required;
import org.springframework.rules.constraint.property.PropertyConstraint;
import org.springframework.rules.constraint.property.PropertyValueConstraint;
import org.springframework.rules.constraint.property.RequiredIfTrue;
import org.springframework.rules.factory.Constraints;
import org.springframework.rules.support.DefaultRulesSource;
import org.springframework.util.Assert;

import it.eurotn.panjea.anagrafica.documenti.domain.CodiceDocumento;
import it.eurotn.panjea.rich.rules.PropertyResolvableConstraint;
import it.eurotn.rich.binding.codice.CodiceBinder.CodicePanel;
import it.eurotn.util.PanjeaEJBUtil;

/**
 * @author Aracno
 * @version 1.0, 06/ott/06
 */
public class CodiceBinding extends AbstractBinding {

    private class CodiceDocumentoAsYouTypeTextComponentAdapter extends AsYouTypeTextComponentAdapter {

        /**
         * Costruttore.
         *
         * @param control
         *            control
         * @param valueModel
         *            valueModel
         */
        public CodiceDocumentoAsYouTypeTextComponentAdapter(final JTextComponent control, final ValueModel valueModel) {
            super(control, valueModel);
        }

        @Override
        protected void adaptedValueChanged(Object newValue) {
            CodiceDocumento codiceDocumento = new CodiceDocumento();
            codiceDocumento.setCodice((String) newValue);
            super.adaptedValueChanged(codiceDocumento);
        }

        @Override
        protected void valueModelValueChanged(Object value) {
            if (value != null) {
                super.valueModelValueChanged(((CodiceDocumento) value).getCodice());
            }
        }

    }

    /**
     * Inner Class Action per la gestione dell'input del codice e del prefisso.
     *
     * @author adriano
     * @version 1.0, 12/nov/07
     */
    private class InputCodiceAction extends AbstractAction {

        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent event) {
            CodiceDocumentoPM codiceDocumento = new CodiceDocumentoPM();
            PanjeaEJBUtil.copyProperties(codiceDocumento, getFormModel().getValueModel(getProperty()).getValue());

            codiceDocumento.setProtocolloPresente(protocolloPropertyPath != null);
            if (protocolloPropertyPath != null) {
                codiceDocumento.setValoreProtocollo(
                        (Integer) getFormModel().getValueModel(valoreProtocolloPropertyPath).getValue());
            }

            InputCodiceDialog dialog = new InputCodiceDialog(codiceDocumento, getFormModel(), getProperty(),
                    patternCodicePropertyPath, applyCodiceClosure);
            dialog.setCloseAction(CloseAction.DISPOSE);
            dialog.showDialog();
            dialog = null;
        }
    }

    /**
     * {@link PropertyChangeListener} applicato a codice protocollo, applica i cambiamenti ai componenti di codice e
     * prefisso al suo variare.
     *
     * @author adriano
     * @version 1.0, 14/nov/07
     */
    private class ProtocolloPropertyChangeListener implements PropertyChangeListener {

        /**
         * Costruttore.
         */
        public ProtocolloPropertyChangeListener() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ((formModel.getValueModel(protocolloPropertyPath).getValue() == null) || (StringUtils.EMPTY
                    .equals(formModel.getValueModel(CodiceBinding.this.protocolloPropertyPath).getValue()))) {
                codiceReadOnly = false;
            } else {
                codiceReadOnly = true;
            }
            readOnlyChanged();
            enabledChanged();
            button.setVisible(codiceReadOnly);
        }

    }

    private JTextComponent textComponentCodice = null;

    private JButton button = null;
    private CodicePanel panel = null;

    /**
     * attributo valorizzato attraverso il context il quale ha la responsabilit� di identificare il property path per la
     * definizione del registro protocollo per controllarne la variazione da parte dell'utente.
     */
    private String protocolloPropertyPath = null;

    /**
     * Path dell proprietà che gestisce il valore del protocollo utilizzato per la generazione del codice.
     */
    private String valoreProtocolloPropertyPath = null;

    private String patternCodicePropertyPath = null;

    /**
     * Closure che verrà chiamata alla chiusura del dialogo del codice.
     */
    private Closure applyCodiceClosure = null;

    /**
     * Identifica se la casella di testo per il codice è in ReadOnly. Questo dipende dall'EditorMode del
     * GeneratoreCodiceBehaviour.
     */
    private boolean codiceReadOnly;

    private boolean codiceRequired;

    private Constraint codiceRequiredConstraint;

    /**
     * Constraint aggiuntive per definire ulteriormente le condizioni per cui il campo codice � richiesto.<br>
     * Ad esempio se il codice non deve essere obbligatorio perche' deve esserlo solo nel caso di un valore specifico di
     * una proprieta'.
     */
    private List<Constraint> additionalConstraints;

    /**
     * Costruttore.
     *
     * @param panel
     *            panel
     * @param formModel
     *            formModel
     * @param formPropertyPath
     *            formPropertyPath
     * @param context
     *            context
     */
    public CodiceBinding(final JPanel panel, final FormModel formModel, final String formPropertyPath,
            final Map<String, Object> context) {
        super(formModel, formPropertyPath, CodiceDocumento.class);

        applyContext(context);
        extractPanelComponent((CodicePanel) panel);

        this.applyCodiceClosure = new ApplyCodiceClosure(formModel, getProperty(), valoreProtocolloPropertyPath);
    }

    /**
     * Aggiunge le rules a seconda dei componenti visualizzati nelle diverse configurazioni.
     */
    private void addCodiceRules() {
        boolean installRequiredRules = false;

        Constraints constraints = Constraints.instance();

        installRequiredRules = false;
        if (protocolloPropertyPath != null) {
            installRequiredRules = true;

            // constraint principale sull'esistenza del protocollo su tipo
            // documento
            Constraint constraintForRequired = new PropertyValueConstraint(protocolloPropertyPath,
                    new Or(constraints.eq(null), constraints.eq(StringUtils.EMPTY)));

            // constraints addizionali provenienti dal form per ulteriori
            // condizioni di codice richiesto
            if (additionalConstraints != null) {
                constraintForRequired = constraints.conjunction().add(constraintForRequired);
                for (Constraint constraint : additionalConstraints) {
                    ((And) constraintForRequired).add(constraint);
                }
            }

            PropertyConstraint requiredIfTrue = new RequiredIfTrue(formPropertyPath, constraintForRequired) {

                @Override
                protected boolean test(PropertyAccessStrategy domainObjectAccessStrategy) {
                    // se il componente non è visibile la validazione è
                    // irrilevante, restituisco true
                    if (!CodiceBinding.this.textComponentCodice.isVisible()) {
                        return true;
                    }

                    domainObjectAccessStrategy.getPropertyValue(formPropertyPath + ".codice");
                    if (getConstraint().test(domainObjectAccessStrategy)) {
                        return Required.instance()
                                .test(domainObjectAccessStrategy.getPropertyValue(formPropertyPath + ".codice"));
                    }

                    return true;
                }

            };

            codiceRequiredConstraint = new PropertyResolvableConstraint(requiredIfTrue);
            ((PropertyResolvableConstraint) codiceRequiredConstraint).setType("codicePropertyResolvableConstraint");
        } else if (codiceRequired) {
            installRequiredRules = true;
            codiceRequiredConstraint = constraints.required();
        }

        if (installRequiredRules) {
            DefaultRulesSource rulesSource = RcpSupport.getBean("rulesSource");
            Rules rules = rulesSource.getRules(formModel.getFormObject().getClass());
            if (codiceRequiredConstraint instanceof PropertyConstraint) {
                rules.add((PropertyConstraint) codiceRequiredConstraint);
            } else {
                rules.add(formPropertyPath, codiceRequiredConstraint);
            }

            ((ValidatingFormModel) formModel).setValidator(new RulesValidator(formModel, rulesSource));
            ((ValidatingFormModel) formModel).validate();
        }
    }

    /**
     * @param context
     *            context to apply
     */
    @SuppressWarnings("unchecked")
    protected void applyContext(Map<String, Object> context) {
        Object object = context.get(CodiceBinder.PROTOCOLLOFORMPROPERTY_KEY);
        if (object != null) {
            this.protocolloPropertyPath = (String) object;
        }

        object = context.get(CodiceBinder.VALOREPROTOCOLLO_FORMPROPERTY_KEY);
        if (object != null) {
            this.valoreProtocolloPropertyPath = (String) object;
        }

        object = context.get(CodiceBinder.PATTERN_FORMPROPERTY_KEY);
        if (object != null) {
            this.patternCodicePropertyPath = (String) object;
        }

        object = context.get(CodiceBinder.ADDITIONAL_CONSTRAINT);
        if (object != null) {
            this.additionalConstraints = (List<Constraint>) object;
        }

        codiceReadOnly = Boolean.TRUE;
        object = context.get(CodiceBinder.EDIT_ENABLE);
        if (object != null) {
            this.codiceReadOnly = !(Boolean) object;
        }

        codiceRequired = Boolean.FALSE;
        object = context.get(CodiceBinder.REQUIRED);
        if (object != null) {
            this.codiceRequired = (Boolean) object;
        }
    }

    @Override
    protected JComponent doBindControl() {
        logger.debug("--> Enter doBindControl");
        final ValueModel valueModel = getValueModel();
        try {
            String codice = StringUtils.defaultString(((CodiceDocumento) valueModel.getValue()).getCodice());
            textComponentCodice.setText(codice);
        } catch (ClassCastException e) {
            IllegalArgumentException ex = new IllegalArgumentException("Class cast exception converting '"
                    + getProperty() + "' property value to string - did you install a type converter?");
            ex.initCause(e);
            throw ex;
        }

        new CodiceDocumentoAsYouTypeTextComponentAdapter(textComponentCodice, valueModel);
        textComponentCodice.setName(getFormModel().getId() + "." + formPropertyPath);

        if (protocolloPropertyPath != null) {

            if ((formModel.getValueModel(protocolloPropertyPath).getValue() == null) || (StringUtils.EMPTY
                    .equals(formModel.getValueModel(CodiceBinding.this.protocolloPropertyPath).getValue()))) {
                codiceReadOnly = false;
            } else {
                codiceReadOnly = true;
            }

            formModel.getValueModel(protocolloPropertyPath)
                    .addValueChangeListener(new ProtocolloPropertyChangeListener());
        }

        button.setEnabled(false);
        button.setVisible(codiceReadOnly);

        addCodiceRules();
        logger.debug("--> Exit doBindControl");
        return panel;
    }

    @Override
    protected void enabledChanged() {
        logger.debug("--> Enter enabledChanged " + formPropertyPath + " enabled " + isEnabled() + " readOnly "
                + isReadOnly());
        boolean enabled = isEnabled();
        logger.debug("--> codiceReadOny " + codiceReadOnly);
        if (codiceReadOnly) {
            textComponentCodice.setEnabled(false);
        } else {
            textComponentCodice.setEnabled(enabled);
        }
        button.setEnabled(codiceReadOnly && isEnabled() && !isReadOnly());
        logger.debug("--> Exit enabledChanged");
    }

    /**
     * @param codicePanel
     *            codicePanel
     */
    private void extractPanelComponent(CodicePanel codicePanel) {
        this.panel = codicePanel;

        this.textComponentCodice = codicePanel.getTextFieldCodice();
        this.button = codicePanel.getButton();

        Assert.notNull(this.textComponentCodice, "Attenzione, componente textComponent per il binding codice e' nullo");
        Assert.notNull(this.button, "Attenzione, componente button per il binding codice e' nullo");

        // setto l'azione del pulsante di calcolo del codice fiscale
        button.setAction(new InputCodiceAction());

        // setto il testo e l'icona per il pulsante del calcolo del codice
        // fiscale
        Icon buttonIcon = null;
        try {
            buttonIcon = ((IconSource) ApplicationServicesLocator.services().getService(IconSource.class))
                    .getIcon("cambiaCodice.button.icon");
        } catch (NoSuchMessageException e) {
            logger.warn("--> Attenzione, testo o icona del pulsante per il calcolo del codice fiscale non definito.");
        }

        if (buttonIcon != null) {
            button.setIcon(buttonIcon);
        }
    }

    @Override
    protected void readOnlyChanged() {
        logger.debug("--> Enter readOnlyChanged " + formPropertyPath + " readOnly  " + isReadOnly() + " enabled "
                + isEnabled() + " codiceReadOnly " + codiceReadOnly);

        boolean readOnly = isReadOnly();
        if (codiceReadOnly) {
            textComponentCodice.setEditable(false);
        } else {
            textComponentCodice.setEditable(!readOnly);
        }
        // abilito il commmand per la modifica del codice se l'attributo codice
        // è di sola lettura e se il ValueModel
        // non è readOnly
        button.setEnabled(codiceReadOnly && !readOnly);
        logger.debug("--> Exit readOnlyChanged");
    }
}
