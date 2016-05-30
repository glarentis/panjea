package it.eurotn.panjea.magazzino.rich.forms.areamagazzino;

import it.eurotn.panjea.magazzino.domain.SedeMagazzino.ETipologiaCodiceIvaAlternativo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.value.ValueModel;

public class AreaDocumentoCodiceIvaAlternativoPropertyChange implements PropertyChangeListener {

	private final FormModel formModel;

	private final ValueModel tipologiaCodiceIvaAlternativoValueModel;
	private final JComponent[] codiceIvaAlternativoComponents;

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            form model
	 * @param tipologiaCodiceIvaAlternativoValueModel
	 *            value model della tipologiaCodiceIvaAlternativo
	 * @param codiceIvaAlternativoComponents
	 *            componenti del codice iva alternativo
	 */
	public AreaDocumentoCodiceIvaAlternativoPropertyChange(final FormModel formModel,
			final ValueModel tipologiaCodiceIvaAlternativoValueModel, final JComponent[] codiceIvaAlternativoComponents) {
		super();
		this.formModel = formModel;
		this.tipologiaCodiceIvaAlternativoValueModel = tipologiaCodiceIvaAlternativoValueModel;
		this.codiceIvaAlternativoComponents = codiceIvaAlternativoComponents;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (!formModel.isEnabled()) {
			return;
		}

		ETipologiaCodiceIvaAlternativo tipologiaCodiceIvaAlternativo = (ETipologiaCodiceIvaAlternativo) tipologiaCodiceIvaAlternativoValueModel
				.getValue();
		if (tipologiaCodiceIvaAlternativo == ETipologiaCodiceIvaAlternativo.NESSUNO) {
			setCodiceIvaAlternativoVisible(false);
			JLabel label = (JLabel) codiceIvaAlternativoComponents[0];
			label.setText("Cod. iva per esenzione");
		} else {
			setCodiceIvaAlternativoVisible(true);
		}
	}

	/**
	 * @param visible
	 *            <code>true</code> rende visibili i controlli
	 */
	private void setCodiceIvaAlternativoVisible(boolean visible) {
		for (JComponent component : codiceIvaAlternativoComponents) {
			component.setVisible(visible);
		}
	}

}