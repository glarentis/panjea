/**
 * 
 */
package it.eurotn.panjea.beniammortizzabili.rich.editors.beni;

import it.eurotn.panjea.beniammortizzabili2.domain.Simulazione;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * 
 * @author Aracno
 * @version 1.0, 30/ott/06
 * 
 */
public class SimulazioneForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "simulazioneForm";
	private JTextField fieldDescrizione;

	/**
	 * Costruttore di default.
	 */
	public SimulazioneForm() {
		super(PanjeaFormModelHelper.createFormModel(new Simulazione(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.row();

		fieldDescrizione = (JTextField) builder.add(Simulazione.PROP_DESCRIZIONE, "colSpan=1 align=left")[1];
		builder.row();
		builder.add(Simulazione.PROP_DATA, "colSpan=1 align=left");
		builder.row();
		getFormModel().getFieldMetadata(Simulazione.PROP_DATA).setEnabled(false);

		getFormModel().addPropertyChangeListener(FormModel.READONLY_PROPERTY, new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				getFormModel().getFieldMetadata(Simulazione.PROP_DATA).setEnabled(false);
			}
		});
		return builder.getForm();
	}

	/**
	 * Imposta il focus sul textfield legato alla descrizione.
	 */
	public void requestFocusForDescrizione() {
		fieldDescrizione.requestFocusInWindow();
	}

}
