/**
 * 
 */
package it.eurotn.panjea.protocolli.rich.form;

import it.eurotn.panjea.protocolli.domain.ProtocolloAnno;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * @author adriano
 * @version 1.0, 24/mag/07
 */
public class ProtocolloAnnoForm extends PanjeaAbstractForm {

	public class ObjectChangeListener implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			ProtocolloAnnoForm.this.getFormModel().getFieldMetadata("protocollo.codice")
					.setReadOnly(((ProtocolloAnno) evt.getNewValue()).getId() != null);
		}

	}

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(ProtocolloAnnoForm.class);

	private static final String FORM_ID = "protocolloAnnoForm";

	/**
	 * form dei protocolli.
	 * 
	 * @param protocolloAnno
	 *            .
	 */
	public ProtocolloAnnoForm(final ProtocolloAnno protocolloAnno) {
		super(PanjeaFormModelHelper.createFormModel(protocolloAnno, false, FORM_ID), FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("protocollo.codice", "align=left")[1]).setColumns(5);
		((JTextField) builder.add("protocollo.descrizione", "align=left")[1]).setColumns(30);
		builder.row();
		((JTextField) builder.add("anno", "align=left")[1]).setColumns(4);
		((JTextField) builder.add("valore", "align=left")[1]).setColumns(4);
		builder.row();
		addFormObjectChangeListener(new ObjectChangeListener());
		return builder.getForm();
	}
}
