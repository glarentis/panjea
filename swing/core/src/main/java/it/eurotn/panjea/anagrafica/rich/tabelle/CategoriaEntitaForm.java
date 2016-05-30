/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.CategoriaEntita;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * 
 * @author angelo
 * 
 */
public class CategoriaEntitaForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "categoriaEntitaForm";

	/**
	 * costrutore.
	 * 
	 * @param categoriaEntita
	 *            .
	 */
	public CategoriaEntitaForm(final CategoriaEntita categoriaEntita) {
		super(PanjeaFormModelHelper.createFormModel(categoriaEntita, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("sezione", "align=left")[1]).setColumns(3);
		builder.row();
		((JTextField) builder.add("descrizione", "align=left")[1]).setColumns(25);
		builder.row();

		return builder.getForm();
	}
}
