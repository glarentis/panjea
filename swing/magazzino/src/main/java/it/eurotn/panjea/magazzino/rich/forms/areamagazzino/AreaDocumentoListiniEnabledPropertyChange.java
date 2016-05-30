package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;

public class AreaDocumentoListiniEnabledPropertyChange implements PropertyChangeListener {

	private static Logger logger = Logger.getLogger(AreaDocumentoListiniEnabledPropertyChange.class);

	private final FormModel formModel;
	private final JComponent[] componentListino;
	private final JComponent[] componentListinoAlternativo;

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            form model
	 * @param componentListino
	 *            componenti del listino
	 * @param componentListinoAlternativo
	 *            componenti del listino alternativo
	 */
	public AreaDocumentoListiniEnabledPropertyChange(final FormModel formModel, final JComponent[] componentListino,
			final JComponent[] componentListinoAlternativo) {
		super();
		this.formModel = formModel;
		this.componentListino = componentListino;
		this.componentListinoAlternativo = componentListinoAlternativo;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		logger.debug("--> Enter propertyChange");
		if (!formModel.isEnabled()) {
			return;
		}
		if ("enabled".equals(evt.getPropertyName())) {
			boolean visible = ((Boolean) evt.getNewValue()).booleanValue();
			for (JComponent component : componentListino) {
				component.setVisible(visible);
			}
			for (JComponent component : componentListinoAlternativo) {
				component.setVisible(visible);
			}
		}
		logger.debug("--> Exit propertyChange");
	}

}