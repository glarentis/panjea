package it.eurotn.panjea.magazzino.rich.forms.aspettoesteriore;

import it.eurotn.panjea.magazzino.domain.AspettoEsteriore;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.TableFormBuilder;

public class AspettoEsterioreForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "aspettoEsterioreForm";

	/**
	 * Costruttore.
	 * 
	 * @param aspettoEsteriore
	 *            {@link AspettoEsteriore}
	 */
	public AspettoEsterioreForm(final AspettoEsteriore aspettoEsteriore) {
		super(PanjeaFormModelHelper.createFormModel(aspettoEsteriore, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("descrizione", "align=left")[1]).setColumns(25);
		builder.row();

		return builder.getForm();
	}

}
