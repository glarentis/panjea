/**
 * 
 */
package it.eurotn.panjea.magazzino.rich.forms;

import it.eurotn.panjea.magazzino.domain.Sconto;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import javax.swing.JComponent;
import javax.swing.JTextField;

import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 * Form che gestisce uno <code>Sconto</code>.<br>
 * Il form si occupa anche si abilitare/disabilitare gli sconti per far si di
 * inserire uno sconto valido.
 * 
 * @author fattazzo
 * 
 */
public class ScontoForm extends PanjeaAbstractForm implements PropertyChangeListener {

	public static final String FORM_ID = "scontoForm";

	/**
	 * Costruttore.
	 */
	public ScontoForm() {
		super(PanjeaFormModelHelper.createFormModel(new Sconto(), false, FORM_ID), FORM_ID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.richclient.form.AbstractForm#createFormControl()
	 */
	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		TableFormBuilder builder = new TableFormBuilder(bf);
		builder.setLabelAttributes("colGrId=label colSpec=right:pref");

		builder.row();
		((JTextField) builder.add("codice", "colSpan=1 align=left")[1]).setColumns(10);
		builder.row();
		((JTextField) builder.add("descrizione", "colSpan=1 align=left")[1]).setColumns(30);
		builder.row();
		((JTextField) builder.add("sconto1", "colSpan=1 align=left")[1]).setColumns(5);
		getFormModel().getValueModel("sconto1").addValueChangeListener(this);
		builder.row();
		((JTextField) builder.add("sconto2", "colSpan=1 align=left")[1]).setColumns(5);
		getFormModel().getValueModel("sconto2").addValueChangeListener(this);
		builder.row();
		((JTextField) builder.add("sconto3", "colSpan=1 align=left")[1]).setColumns(5);
		getFormModel().getValueModel("sconto3").addValueChangeListener(this);
		builder.row();
		((JTextField) builder.add("sconto4", "colSpan=1 align=left")[1]).setColumns(5);
		getFormModel().getValueModel("sconto4").addValueChangeListener(this);

		return builder.getForm();
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		getFormModel().getFieldMetadata("sconto1").setEnabled(true);

		BigDecimal sconto1 = (BigDecimal) getFormModel().getValueModel("sconto1").getValue();
		getFormModel().getFieldMetadata("sconto2").setEnabled(
				sconto1 != null && sconto1.compareTo(BigDecimal.ZERO) != 0);

		BigDecimal sconto2 = (BigDecimal) getFormModel().getValueModel("sconto2").getValue();
		getFormModel().getFieldMetadata("sconto3").setEnabled(
				sconto2 != null && sconto2.compareTo(BigDecimal.ZERO) != 0);

		BigDecimal sconto3 = (BigDecimal) getFormModel().getValueModel("sconto3").getValue();
		getFormModel().getFieldMetadata("sconto4").setEnabled(
				sconto3 != null && sconto3.compareTo(BigDecimal.ZERO) != 0);
	}

}
