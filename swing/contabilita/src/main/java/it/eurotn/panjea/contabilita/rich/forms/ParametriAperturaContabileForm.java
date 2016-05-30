/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.forms;

import it.eurotn.panjea.contabilita.util.ParametriAperturaContabile;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * 
 * @author adriano
 * @version 1.0, 05/set/07
 * 
 */
public class ParametriAperturaContabileForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "parametriAperturaContabileForm";

	/**
	 * Costruttore.
	 * 
	 */
	public ParametriAperturaContabileForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriAperturaContabile(), false, FORM_ID), FORM_ID);

	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.row();
		((JTextField) builder.add("anno", "align=left")[1]).setColumns(5);

		builder.add("dataMovimento", "align=left");
		builder.row();
		return builder.getForm();
	}

}
