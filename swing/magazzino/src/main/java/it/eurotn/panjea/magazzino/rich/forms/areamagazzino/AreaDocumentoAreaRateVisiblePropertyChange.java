package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;

public class AreaDocumentoAreaRateVisiblePropertyChange implements PropertyChangeListener {

	private static Logger logger = Logger.getLogger(AreaDocumentoAreaRateVisiblePropertyChange.class);

	private final FormModel formModel;

	private final JComponent[] codicePagamentoComponents;
	private final JComponent[] componentSpeseIncasso;

	/**
	 * Visualizza o nasconde i controlli del codice di pagamento e delle spese di incasso in base alla presenza
	 * dell'area rate.
	 * 
	 * @param formModel
	 *            form model
	 * @param codicePagamentoComponents
	 *            componenti del codice di pagamento
	 * @param componentSpeseIncasso
	 *            componenti delle spese di incasso
	 */
	public AreaDocumentoAreaRateVisiblePropertyChange(final FormModel formModel,
			final JComponent[] codicePagamentoComponents, final JComponent[] componentSpeseIncasso) {
		super();
		this.formModel = formModel;
		this.codicePagamentoComponents = codicePagamentoComponents;
		this.componentSpeseIncasso = componentSpeseIncasso;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		logger.debug("--> Enter propertyChange");
		if (!formModel.isEnabled()) {
			return;
		}
		if ("enabled".equals(evt.getPropertyName())) {
			boolean visible = ((Boolean) evt.getNewValue()).booleanValue();

			if (codicePagamentoComponents != null) {
				for (JComponent component : codicePagamentoComponents) {
					component.setVisible(visible);
				}
			}

			if (componentSpeseIncasso != null) {
				for (JComponent component : componentSpeseIncasso) {
					component.setVisible(visible);
				}
			}
		}
		logger.debug("--> Exit propertyChange");
	}
}