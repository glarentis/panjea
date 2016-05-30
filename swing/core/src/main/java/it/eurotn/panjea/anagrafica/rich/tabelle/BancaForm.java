/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * @author Leonardo
 * 
 */
public class BancaForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "bancaForm";

	/**
	 * Costruttore.
	 * 
	 * @param banca
	 *            banca
	 */
	public BancaForm(final Banca banca) {
		super(PanjeaFormModelHelper.createFormModel(banca, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("codice", "align=left")[1]).setColumns(6);
		builder.row();
		((JTextField) builder.add("descrizione", "align=left")[1]).setColumns(25);
		builder.row();
		((JTextField) builder.add("cin", "align=left")[1]).setColumns(2);
		builder.row();

		return builder.getForm();
	}

}
