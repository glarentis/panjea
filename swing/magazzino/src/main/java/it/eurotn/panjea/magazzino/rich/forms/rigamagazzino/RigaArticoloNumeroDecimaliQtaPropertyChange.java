package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import it.eurotn.panjea.util.DefaultNumberFormatterFactory;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatterFactory;

import org.springframework.binding.form.FormModel;

public class RigaArticoloNumeroDecimaliQtaPropertyChange implements PropertyChangeListener {

	private final FormModel formModel;
	private final JFormattedTextField qtaTextComponentField;

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            formmodel
	 * @param qtaTextComponentField
	 *            componente della qta
	 */
	public RigaArticoloNumeroDecimaliQtaPropertyChange(final FormModel formModel,
			final JFormattedTextField qtaTextComponentField) {
		super();
		this.formModel = formModel;
		this.qtaTextComponentField = qtaTextComponentField;
	}

	/**
	 * 
	 * @return factory per la formattazione qtaDecimanli
	 */
	private DefaultFormatterFactory getFactory() {
		Integer numeroDecimaliQt = (Integer) formModel.getValueModel("numeroDecimaliQta").getValue();
		DefaultNumberFormatterFactory f = new DefaultNumberFormatterFactory("#,##0", numeroDecimaliQt, Double.class);
		return f;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		DefaultFormatterFactory factory = getFactory();
		qtaTextComponentField.setFormatterFactory(factory);
	}

}
