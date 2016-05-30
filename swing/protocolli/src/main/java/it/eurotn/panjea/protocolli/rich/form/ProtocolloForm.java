/**
 * 
 */
package it.eurotn.panjea.protocolli.rich.form;

import it.eurotn.panjea.protocolli.domain.Protocollo;
import it.eurotn.panjea.protocolli.domain.ProtocolloValore;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * 
 * @author adriano
 * @version 1.0, 01/giu/07
 */
public class ProtocolloForm extends PanjeaAbstractForm {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ProtocolloForm.class);
	private static final String FORM_ID = "protocolloForm";

	/**
	 * costruttore protocollo.
	 * 
	 * @param protocollo
	 *            .
	 */
	public ProtocolloForm(final Protocollo protocollo) {
		super(PanjeaFormModelHelper.createFormModel(protocollo, false, FORM_ID), FORM_ID);
	}

	/**
	 * costruttore per protocollovalore.
	 * 
	 * @param protocolloValore
	 *            .
	 */
	public ProtocolloForm(final ProtocolloValore protocolloValore) {
		super(PanjeaFormModelHelper.createFormModel(protocolloValore, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("codice", "align=left")[1]).setColumns(5);
		((JTextField) builder.add("descrizione", "align=left")[1]).setColumns(30);
		builder.row();
		if (getFormObject() instanceof ProtocolloValore) {
			((JTextField) builder.add("valore", "align=left")[1]).setColumns(4);
			builder.row();
		}
		return builder.getForm();
	}
}
