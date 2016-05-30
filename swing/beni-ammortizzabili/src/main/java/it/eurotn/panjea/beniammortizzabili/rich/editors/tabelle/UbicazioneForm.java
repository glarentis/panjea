/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.tabelle;

import it.eurotn.panjea.beniammortizzabili2.domain.Ubicazione;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * Form per gestire l'ubicazione.
 * 
 * @author Aracno
 * @version 1.0, 22/set/06
 * 
 */
public class UbicazioneForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "ubicazioneForm";

	/**
	 * Costruttore.
	 * 
	 * @param ubicazione
	 *            ubicazione
	 */
	public UbicazioneForm(final Ubicazione ubicazione) {
		super(PanjeaFormModelHelper.createFormModel(ubicazione, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("codice", "align=left")[1]).setColumns(3);
		builder.row();
		((JTextField) builder.add("descrizione", "align=left")[1]).setColumns(25);
		builder.row();

		return builder.getForm();
	}

}
