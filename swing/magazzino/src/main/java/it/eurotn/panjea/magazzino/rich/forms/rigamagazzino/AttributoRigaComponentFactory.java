package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import it.eurotn.panjea.magazzino.domain.AttributoRigaArticolo;
import it.eurotn.panjea.rich.factory.PanjeaComponentFactory;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;

import java.math.BigDecimal;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.log4j.Logger;
import org.springframework.richclient.application.Application;
import org.springframework.richclient.factory.ComponentFactory;
import org.springframework.richclient.text.SelectAllFocusListener;

public class AttributoRigaComponentFactory {

	private static Logger logger = Logger.getLogger(AttributoRigaComponentFactory.class);

	/**
	 * Restituisce il componente da utilizzare per l'attributo richiesto.
	 * 
	 * @param attributoRiga
	 *            attributo riga
	 * @return componente
	 */
	public static JComponent getComponent(AttributoRigaArticolo attributoRiga) {

		PanjeaComponentFactory componentFactory = (PanjeaComponentFactory) Application.services().getService(
				ComponentFactory.class);

		JComponent component = null;

		switch (attributoRiga.getTipoAttributo().getTipoDato()) {
		case BOOLEANO:
			JCheckBox checkBoxField = componentFactory.createCheckBox("");
			// checkBoxField.setEnabled(attributoRiga.isUpdatable() && !isReadOnly());
			checkBoxField.setSelected((Boolean) attributoRiga.getValoreTipizzato());
			component = checkBoxField;
			break;
		case NUMERICO:
			JFormattedTextField valoreNumericoField = componentFactory.createFormattedTextField();
			valoreNumericoField.setColumns(5);
			valoreNumericoField.setHorizontalAlignment(SwingConstants.RIGHT);
			valoreNumericoField.getDocument().putProperty("parent", valoreNumericoField);
			valoreNumericoField.setVisible(true);
			// valoreNumericoField.setEnabled(true);
			valoreNumericoField.setFormatterFactory(new DefaultNumberFormatterFactory("#,##0", attributoRiga
					.getTipoAttributo().getNumeroDecimali(), BigDecimal.class, true));
			// valoreNumericoField.setEditable(!isReadOnly() && attributoRiga.isUpdatable());
			// BigDecimal valore = BigDecimal.ZERO;
			try {
				// valore = (BigDecimal) ((DefaultNumberFormatterFactory) valoreNumericoField.getFormatterFactory())
				// .getDefaultFormatter().stringToValue(attributoRigaArticolo.getValore());
				valoreNumericoField.setText(attributoRiga.getValore());
			} catch (Exception e) {
				// eccezione non gestita
				logger.warn("--> Attenzione, errore durante la verifica del valore dell'attributo.", e);
			}
			component = valoreNumericoField;
			break;
		default:
			JTextField field = componentFactory.createTextField();
			field.setColumns(5);
			field.setHorizontalAlignment(SwingConstants.RIGHT);
			field.addFocusListener(new SelectAllFocusListener(field));
			field.getDocument().putProperty("parent", field);
			// field.setEditable(attributoRiga.isUpdatable());
			// field.setEnabled(enabled);
			field.setText(attributoRiga.getValore());
			component = field;
			break;
		}

		return component;
	}
}
