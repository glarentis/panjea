/**
 * 
 */
package it.eurotn.panjea.lotti.rich.editors.riepilogolotto;

import it.eurotn.panjea.lotti.util.ParametriRicercaLotti;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author fattazzo
 * 
 */
public class ParametriRiepilogoLottoForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "parametriRiepilogoLottoForm";

	/**
	 * Costruttore.
	 */
	public ParametriRiepilogoLottoForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaLotti(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:150dlu", "4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r, c");
		builder.setRow(2);

		builder.addLabel("lotto", 1);
		builder.addBinding(bf.createBoundSearchText("lotto", new String[] { "codice", "dataScadenza" }), 3);

		return builder.getPanel();
	}

}
