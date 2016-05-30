package it.eurotn.panjea.tesoreria.rich.forms.acconto;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.tesoreria.util.parametriricerca.ParametriRicercaAcconti;
import it.eurotn.rich.binding.CustomEnumComboBoxEditor;
import it.eurotn.rich.binding.CustomEnumListRenderer;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.richclient.form.binding.swing.ComboBoxBinding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class ParametriRicercaAreaAccontoForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "parametriRicercaAreaAccontoForm";

	/**
	 * Costruttore.
	 */
	public ParametriRicercaAreaAccontoForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaAcconti(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:default",
				"2dlu,default,2dlu");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel()
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		builder.addPropertyAndLabel("daDataDocumento", 1);

		builder.addPropertyAndLabel("aDataDocumento", 5);

		builder.addPropertyAndLabel("statoAcconto", 9);

		List<TipoEntita> tipiEntita = new ArrayList<TipoEntita>();
		tipiEntita.add(TipoEntita.CLIENTE);
		tipiEntita.add(TipoEntita.FORNITORE);
		tipiEntita.add(TipoEntita.AZIENDA);

		ComboBoxBinding comboBoxBinding = (ComboBoxBinding) bf.createBoundComboBox("tipoEntita", tipiEntita);
		comboBoxBinding.setRenderer(new CustomEnumListRenderer(getMessages()));
		comboBoxBinding.setEditor(new CustomEnumComboBoxEditor(getMessages(), comboBoxBinding.getEditor()));
		builder.addLabel("tipoEntita", 13);
		builder.addBinding(comboBoxBinding, 15);

		builder.nextRow();

		return builder.getPanel();
	}

}
