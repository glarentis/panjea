/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.editors.riepilogoblacklist;

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
public class RiepilogoDocumentiBlacklistForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "riepilogoDocumentiBlacklistForm";

	/**
	 * Costruttore.
	 */
	public RiepilogoDocumentiBlacklistForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaRiepilogoBlacklist(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,left:pref", "4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("r, c");
		builder.setRow(2);
		builder.addPropertyAndLabel("dataIniziale");
		builder.nextRow();
		builder.addPropertyAndLabel("dataFinale");

		return builder.getPanel();
	}

}
