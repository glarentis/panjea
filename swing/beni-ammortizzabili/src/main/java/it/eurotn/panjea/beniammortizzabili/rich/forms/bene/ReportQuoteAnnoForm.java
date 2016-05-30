package it.eurotn.panjea.beniammortizzabili.rich.forms.bene;

import it.eurotn.panjea.beniammortizzabili2.util.parametriricerca.ParametriRicercaBeni;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.TableFormBuilder;

public class ReportQuoteAnnoForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "reportQuoteAnnoForm";

	/**
	 * Costruttore di default.
	 * 
	 * @param parametriRicercaBeni
	 *            Parametri di ricerca
	 */
	public ReportQuoteAnnoForm(final ParametriRicercaBeni parametriRicercaBeni) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicercaBeni, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("anno", "align=left colspan=2")[1]).setColumns(4);
		builder.row();

		return builder.getForm();
	}

}
