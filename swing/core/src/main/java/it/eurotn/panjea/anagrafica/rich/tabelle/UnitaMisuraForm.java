/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.UnitaMisura;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * @author fattazzo
 * 
 */
public class UnitaMisuraForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "unitaMisuraForm";

	/**
	 * Costruttore.
	 * 
	 * @param unitaMisura
	 *            unità di misura
	 */
	public UnitaMisuraForm(final UnitaMisura unitaMisura) {
		super(PanjeaFormModelHelper.createFormModel(unitaMisura, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("codice", "align=left")[1]).setColumns(3);
		builder.row();
		((JTextField) builder.add("descrizione", "align=left")[1]).setColumns(25);
		builder.row();
		builder.add("intra", "align=left");
		return builder.getForm();
	}
}
