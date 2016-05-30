/**
 * 
 */
package it.eurotn.panjea.contabilita.rich.forms;

import it.eurotn.panjea.contabilita.domain.RegistroIva;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * @author Leonardo
 * 
 */
public class RegistroIvaForm extends PanjeaAbstractForm {

	private static final String FORM_ID = "registroIvaForm";

	/**
	 * Costruttore.
	 */
	public RegistroIvaForm() {
		super(PanjeaFormModelHelper.createFormModel(new RegistroIva(), false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("descrizione", "colSpan=1 align=left")[1]).setColumns(30);
		builder.row();
		((JTextField) builder.add("numero", "colSpan=1 align=left")[1]).setColumns(5);
		builder.row();
		builder.add("tipoRegistro", "colSpan=1 align=left");
		builder.row();

		return builder.getForm();
	}

}
