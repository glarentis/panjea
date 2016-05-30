/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.ConversioneUnitaMisura;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Leonardo
 */
public class ConversioneUnitaMisuraForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "conversioneUnitaMisuraForm";

	/**
	 * Costruttore.
	 * 
	 * @param conversioneUnitaMisura
	 *            conversione unità di misura
	 */
	public ConversioneUnitaMisuraForm(final ConversioneUnitaMisura conversioneUnitaMisura) {
		super(PanjeaFormModelHelper.createFormModel(conversioneUnitaMisura, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:default, left:pref:grow",
				"4dlu,default, 2dlu,default, 2dlu,default, fill:pref:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // ,
																				// new
																				// FormDebugPanel()
		builder.setLabelAttributes("r, c");

		builder.setRow(2);
		builder.addLabel("unitaMisuraOrigine", 1);
		builder.addBinding(bf.createBoundSearchText("unitaMisuraOrigine", new String[] { "codice" }), 3);
		builder.nextRow();

		builder.addLabel("unitaMisuraDestinazione", 1);
		builder.addBinding(bf.createBoundSearchText("unitaMisuraDestinazione", new String[] { "codice" }), 3);
		builder.nextRow();

		List<String> variabili = new ArrayList<String>();
		variabili.add(ConversioneUnitaMisura.ORIGINAL_VALUE_VARIABLE);
		ValueHolder variabiliValueHolder = new ValueHolder(variabili);

		builder.setLabelAttributes("r, t");
		builder.addLabel("formula", 1);
		builder.addBinding(bf.createBoundCodeEditor("formula", variabiliValueHolder), 3, 6, 2, 2);

		builder.nextRow();

		return builder.getPanel();
	}
}
