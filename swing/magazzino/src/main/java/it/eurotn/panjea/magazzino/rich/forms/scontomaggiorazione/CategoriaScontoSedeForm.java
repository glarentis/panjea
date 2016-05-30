/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms.scontomaggiorazione;

import it.eurotn.panjea.magazzino.domain.CategoriaScontoSede;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * Form che gestisce una <code>CategoriaScontoSede</code>.
 * 
 * @author fattazzo
 * 
 */
public class CategoriaScontoSedeForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "categoriaScontoForm";

	public CategoriaScontoSedeForm() {
		super(PanjeaFormModelHelper.createFormModel(new CategoriaScontoSede(), false, FORM_ID), FORM_ID);
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
		((JTextField) builder.add("codice", "colSpan=1 align=left")[1]).setColumns(5);
		builder.row();

		return builder.getForm();
	}

}
