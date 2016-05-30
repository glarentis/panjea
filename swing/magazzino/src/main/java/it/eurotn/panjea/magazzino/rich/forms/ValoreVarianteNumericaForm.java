/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms;

import it.eurotn.panjea.magazzino.domain.ValoreVarianteNumerica;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * @author fattazzo
 * 
 */
public class ValoreVarianteNumericaForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "valoreVarianteNumericaForm";

	public ValoreVarianteNumericaForm() {
		super(PanjeaFormModelHelper.createFormModel(new ValoreVarianteNumerica(), false, FORM_ID), FORM_ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.richclient.form.AbstractForm#createFormControl()
	 */
	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("valoreDouble", "colSpan=1 align=left")[1]).setColumns(10);
		builder.row();
		return builder.getForm();
	}

}
