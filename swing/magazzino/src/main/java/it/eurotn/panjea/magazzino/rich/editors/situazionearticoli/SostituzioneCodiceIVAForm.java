/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.editors.situazionearticoli;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author fattazzo
 * 
 */
public class SostituzioneCodiceIVAForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "SostituzioneCodiceIVAForm";

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 * @param formId
	 */
	public SostituzioneCodiceIVAForm() {
		super(PanjeaFormModelHelper.createFormModel(new SostituzioneCodiceIvaPM(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("right:pref,4dlu,fill:80dlu", "4dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout);
		builder.setLabelAttributes("r, c");
		builder.setRow(2);

		builder.addComponent(new JLabel("Codice da sostituire"), 1);
		builder.addBinding(bf.createBoundSearchText("codiceIvaDaSostituire", new String[] { "codice" }), 3);
		builder.nextRow();

		builder.addComponent(new JLabel("Nuovo codice iva"), 1);
		builder.addBinding(bf.createBoundSearchText("nuovoCodiceIva", new String[] { "codice" }), 3);

		return builder.getPanel();
	}

}
