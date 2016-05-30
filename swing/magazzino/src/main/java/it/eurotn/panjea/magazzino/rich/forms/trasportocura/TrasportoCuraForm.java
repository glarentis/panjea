package it.eurotn.panjea.magazzino.rich.forms.trasportocura;

import it.eurotn.panjea.magazzino.domain.TrasportoCura;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.TableFormBuilder;

public class TrasportoCuraForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "trasportoCuraForm";

	/**
	 * Costruttore.
	 * 
	 * @param trasportoCura
	 *            {@link TrasportoCura}
	 */
	public TrasportoCuraForm(final TrasportoCura trasportoCura) {
		super(PanjeaFormModelHelper.createFormModel(trasportoCura, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");
		builder.row();
		((JTextField) builder.add("descrizione", "align=left")[1]).setColumns(30);
		builder.row();
		builder.add("mittente", "align=left");
		builder.row();

		return builder.getForm();
	}

}
