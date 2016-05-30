/**
 * 
 */
package it.eurotn.panjea.anagrafica.rich.tabelle;

import it.eurotn.panjea.anagrafica.domain.Mansione;
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
public class MansioneForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "mansioneForm";

	/**
	 * Costruttore.
	 * 
	 * @param mansione
	 *            {@link Mansione}
	 */
	public MansioneForm(final Mansione mansione) {
		super(PanjeaFormModelHelper.createFormModel(mansione, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("descrizione", "align=left")[1]).setColumns(25);
		builder.row();

		return builder.getForm();
	}

}
