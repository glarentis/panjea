package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import it.eurotn.panjea.magazzino.domain.RigaTestata;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.TableFormBuilder;

public class RigaTestataForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "rigaTestataForm";

	/**
	 * Costruttore.
	 * 
	 */
	public RigaTestataForm() {
		super(PanjeaFormModelHelper.createFormModel(new RigaTestata(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.row();
		builder.add("descrizione", "align=left");
		builder.row();
		builder.add("noteRiga");
		builder.row();

		return builder.getForm();
	}

}
