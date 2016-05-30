package it.eurotn.panjea.tesoreria.solleciti.editors;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.tesoreria.rich.bd.ITesoreriaBD;
import it.eurotn.panjea.tesoreria.rich.bd.TesoreriaBD;
import it.eurotn.panjea.tesoreria.solleciti.Sollecito;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class SollecitiForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "sollecitiForm";

	private final ITesoreriaBD tesoreriaBD;

	/**
	 * Costruttore.
	 * 
	 */
	public SollecitiForm() {
		super(PanjeaFormModelHelper.createFormModel(new Sollecito(), false, FORM_ID), FORM_ID);
		this.tesoreriaBD = RcpSupport.getBean(TesoreriaBD.BEAN_ID);
		// Aggiungo il value model che mi servirà solamente per sapere se
		// salvare o no
		ValueModel saveValueModel = new ValueHolder(Boolean.TRUE);
		DefaultFieldMetadata saveData = new DefaultFieldMetadata(getFormModel(), new FormModelMediatingValueModel(
				saveValueModel), Boolean.class, true, null);
		getFormModel().add("save", saveValueModel, saveData);

	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:default,4dlu,left:pref", "2dlu,default,2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("r, c");

		builder.addLabel("template", 1, 2);
		ValueHolder templateSollecitiValueHolder = new ValueHolder(tesoreriaBD.caricaTemplateSolleciti());
		Binding bindingTemplateSolleciti = bf.createBoundComboBox("template", templateSollecitiValueHolder,
				"descrizione");
		builder.addBinding(bindingTemplateSolleciti, 3, 2);

		builder.nextRow();
		((JTextField) builder.addPropertyAndLabel("nota", 1, 4)[1]).setColumns(40);

		builder.nextRow();
		JPanel panelroot = builder.getPanel();
		panelroot.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 0));
		return panelroot;

	}
}
