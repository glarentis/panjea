package it.eurotn.rich.binding.codice;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.commons.lang3.StringUtils;
import org.springframework.binding.form.FormModel;
import org.springframework.binding.validation.support.RulesValidator;
import org.springframework.richclient.form.builder.TableFormBuilder;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.Rules;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.constraint.property.PropertyValueConstraint;
import org.springframework.rules.constraint.property.RequiredIfTrue;
import org.springframework.rules.factory.Constraints;
import org.springframework.rules.support.DefaultRulesSource;

import it.eurotn.codice.generator.interfaces.VariabiliCodiceManager;
import it.eurotn.entity.EntityBase;
import it.eurotn.panjea.rich.bd.CodiceBD;
import it.eurotn.panjea.rich.bd.ICodiceBD;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.rich.rules.PropertyResolvableConstraint;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

/**
 * Form di CodicePM.
 *
 * @author adriano
 * @version 1.0, 14/nov/07
 */
public class CodiceDocumentoPMForm extends PanjeaAbstractForm {

    private class ValoreProtocolloPropertyChange implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {

            Integer newValue = (Integer) evt.getNewValue();

            if (newValue == null) {
                return;
            }

            String pattern = (String) entityFormModel.getValueModel(pattenCodicePath).getValue();

            // carico la lista delle variabili avvalorate per la generazione del codice e sovrascrivo quella del valore
            // protocollo inserendo il valore scelto nel form.
            // ricavo la proprietà su cui è mappato il codice: es: areaContabile.documento.codice diventa
            // areaContabile.documento
            String entityProperty = StringUtils.substringBeforeLast(entityFormCodicePropertyPath, ".");
            Map<String, String> variabili = codiceBD
                    .creaVariabiliCodice((EntityBase) entityFormModel.getValueModel(entityProperty).getValue());
            variabili.put(VariabiliCodiceManager.VALPROT, newValue.toString());

            String newCodice = codiceBD.generaCodice(pattern, variabili);
            getFormModel().getValueModel("codice").setValue(newCodice);
        }

    }

    private static final String FORM_ID = "CodicePMForm";

    private FormModel entityFormModel;

    private String entityFormCodicePropertyPath;
    private String pattenCodicePath;

    private ICodiceBD codiceBD;

    private ValoreProtocolloPropertyChange protocolloPropertyChange = new ValoreProtocolloPropertyChange();

    /**
     * Costruttore.
     * 
     * @param codiceDocumento
     *            codice documento
     * @param entityFormModel
     *            form model dell'entità
     * @param entityFormCodicePropertyPath
     *            nome della proprità codice dell'entita
     * @param pattenCodicePath
     *            nome della proprietà del pattern
     */
    public CodiceDocumentoPMForm(final CodiceDocumentoPM codiceDocumento, final FormModel entityFormModel,
            final String entityFormCodicePropertyPath, final String pattenCodicePath) {
        super(PanjeaFormModelHelper.createFormModel(codiceDocumento, false, "codiceDocumentoForm"), FORM_ID);
        this.entityFormModel = entityFormModel;
        this.entityFormCodicePropertyPath = entityFormCodicePropertyPath;
        this.pattenCodicePath = pattenCodicePath;
        this.codiceBD = RcpSupport.getBean(CodiceBD.BEAN_ID);
    }

    private void applyValidationRules() {

        Rules rules = new Rules(CodiceDocumentoPM.class);

        Constraint constraintForRequired = new PropertyValueConstraint("protocolloPresente",
                Constraints.instance().eq(Boolean.TRUE));
        PropertyResolvableConstraint valoreProtocolloConstraint = new PropertyResolvableConstraint(
                new RequiredIfTrue("valoreProtocollo", constraintForRequired));
        valoreProtocolloConstraint.setType("required");

        PropertyResolvableConstraint codiceConstraint = new PropertyResolvableConstraint(
                new RequiredIfTrue("codice", constraintForRequired));
        codiceConstraint.setType("required");

        rules.add(valoreProtocolloConstraint);
        rules.add(codiceConstraint);
        DefaultRulesSource rulesSource = RcpSupport.getBean("rulesSource");
        rulesSource.addRules(rules);

        getFormModel().setValidator(new RulesValidator(getFormModel(), rulesSource));
        getFormModel().validate();
    }

    @Override
    protected JComponent createFormControl() {
        final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);
        builder.setLabelAttributes("colGrId=label colSpec=right:pref");

        builder.row();
        builder.row();

        if (((CodiceDocumentoPM) getFormModel().getFormObject()).isProtocolloPresente()) {
            JLabel label = getComponentFactory().createLabel("Valore protocollo ");
            JTextField textField = (JTextField) bf.createBinding("valoreProtocollo").getControl();
            textField.setColumns(15);
            builder.getLayoutBuilder().cell(label, "align=right");
            builder.getLayoutBuilder().cell(textField, "align=left");
            builder.row();
        }

        JLabel label = getComponentFactory().createLabel(getMessage(entityFormCodicePropertyPath) + " ");
        JTextField textField = (JTextField) bf.createBinding("codice").getControl();
        textField.setColumns(15);
        builder.getLayoutBuilder().cell(label, "align=right");
        builder.getLayoutBuilder().cell(textField, "align=left");

        builder.row();

        getFormModel().getValueModel("valoreProtocollo").addValueChangeListener(protocolloPropertyChange);

        applyValidationRules();
        return builder.getForm();
    }

    @Override
    public void setFormObject(Object formObject) {
        // rimuovo il property change perchè quando viene settato il codice documento devo tenere i valori originali e
        // non ricalcolare il codice
        getFormModel().getValueModel("valoreProtocollo").removeValueChangeListener(protocolloPropertyChange);
        super.setFormObject(formObject);
        // aggiungo il property change per il ricalcolo del codice al variare del valore protocollo
        getFormModel().getValueModel("valoreProtocollo").addValueChangeListener(protocolloPropertyChange);
    }

}