/**
 *
 */
package it.eurotn.panjea.tesoreria.rich.forms;

import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.tesoreria.rich.pm.ParametriCreazioneAreaEffetti;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * Form per l'inserimento dei dati per costruire un'area effetti di tipo distinta o accredito.
 *
 * @author Leonardo
 */
public class ParametriCreazioneAreaEffettiForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "distintaBancariaForm";

	/**
	 * costruttore di default.
	 */
	public ParametriCreazioneAreaEffettiForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriCreazioneAreaEffetti(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colSpec=right:pref:");
		builder.row();

		builder.add("dataDocumento", "align=left");
		JTextField numDoc = (JTextField) builder.add("numeroDocumento", "align=left")[1];
		numDoc.setColumns(8);
		builder.add("raggruppaBanche");

		builder.row();
		((JTextField) builder.add("spese", "align=left")[1]).setColumns(8);
		((JTextField) builder.add("speseDistinta", "align=left")[1]).setColumns(8);
		builder.row();
		return builder.getForm();
	}

}
