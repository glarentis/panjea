/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.beniammortizzabili2.util.parametriricerca.ParametriRicercaBeni;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * 
 * @author adriano
 * @version 1.0, 12/dic/06
 */
public class ReportVenditaBeniForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "reportVenditaBeniForm";

	/**
	 * Costruttore.
	 * 
	 */
	public ReportVenditaBeniForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaBeni(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("anno", "align=left colspan=2")[1]).setColumns(4);
		builder.row();
		builder.add(bf.createBoundSearchText("specieIniziale", new String[] { "codice" }));
		builder.add(bf.createBoundSearchText("specieFinale", new String[] { "codice" }));
		builder.row();
		builder.add("sottoSpecieIniziale");
		builder.add("sottoSpecieFinale");
		builder.row();
		return builder.getForm();
	}

}
