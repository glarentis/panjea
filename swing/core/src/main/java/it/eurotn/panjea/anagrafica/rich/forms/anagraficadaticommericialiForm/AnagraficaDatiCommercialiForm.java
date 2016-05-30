package it.eurotn.panjea.anagrafica.rich.forms.anagraficadaticommericialiForm;

import it.eurotn.panjea.anagrafica.domain.SedeEntita;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class AnagraficaDatiCommercialiForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "anagraficaDatiCommericialiForm";

	public static final int COLUMN_SIZE = 90;

	/**
	 * Form per i dati commerciali.
	 */
	public AnagraficaDatiCommercialiForm() {
		super(PanjeaFormModelHelper.createFormModel(new SedeEntita(), false, FORM_ID + "Model"), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:"
						+ AnagraficaDatiCommercialiForm.COLUMN_SIZE
						+ "dlu,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, fill:default:grow",
				"2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");

		return builder.getPanel();
	}
}
