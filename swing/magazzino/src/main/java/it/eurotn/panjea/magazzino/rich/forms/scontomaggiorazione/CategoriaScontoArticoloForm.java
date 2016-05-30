/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms.scontomaggiorazione;

import it.eurotn.panjea.magazzino.domain.CategoriaScontoArticolo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * Form che gestisce una <code>CategoriaScontoArticolo</code>.
 * 
 * @author fattazzo
 * 
 */
public class CategoriaScontoArticoloForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "categoriaScontoArticoloForm";

	public CategoriaScontoArticoloForm() {
		super(PanjeaFormModelHelper.createFormModel(new CategoriaScontoArticolo(), false, FORM_ID), FORM_ID);
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
