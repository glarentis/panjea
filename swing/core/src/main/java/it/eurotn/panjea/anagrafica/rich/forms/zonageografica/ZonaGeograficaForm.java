package it.eurotn.panjea.anagrafica.rich.forms.zonageografica;

import it.eurotn.panjea.anagrafica.domain.ZonaGeografica;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;

public class ZonaGeograficaForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "zonaGeograficaForm";

	/**
	 * Costruttore di default, inizializza un nuovo tipo mezzo di trasporto.
	 */
	public ZonaGeograficaForm() {
		super(PanjeaFormModelHelper.createFormModel(new ZonaGeografica(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("codice", "align=left")[1]).setColumns(10);
		builder.row();
		((JTextField) builder.add("descrizione", "align=left")[1]).setColumns(30);
		builder.row();

		return builder.getForm();
	}
}
