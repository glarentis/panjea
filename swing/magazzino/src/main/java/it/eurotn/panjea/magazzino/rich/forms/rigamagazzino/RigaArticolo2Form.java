package it.eurotn.panjea.magazzino.rich.forms.rigamagazzino;

import it.eurotn.panjea.anagrafica.domain.Importo;
import it.eurotn.panjea.magazzino.domain.RigaArticolo;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultFormatterFactory;

import org.apache.log4j.Logger;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;

import com.jgoodies.forms.layout.FormLayout;

public class RigaArticolo2Form extends PanjeaAbstractForm {

	private class RigaArticoloCodiceIvaChangeListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			if (getFormModel().isReadOnly()) {
				logger.debug("--> Exit RigaArticoloAggiornaPrezzoPropertyChange form model is read only");
				return;
			}

			if (getFormObject() instanceof RigaArticolo) {
				Importo importo = ((RigaArticolo) getFormObject()).getPrezzoUnitarioReale();
				getFormModel().getValueModel("prezzoUnitarioReale").setValue(importo.clone());
			}
		}
	}

	/**
	 * PropertyChange per aggiornare il formatted text field di qtaMagazzino.<br/>
	 * Reimposto il value al field perchè quando setto il factory il formatted field ha ancora il valore vecchio e non
	 * lo cambia più. Comportamento che non accade nel form di RigaArticoloForm. Why ???
	 * 
	 * @author leonardo
	 */
	private class RigaArticoloNumeroDecimaliQtaMagazzinoPropertyChange extends
			RigaArticoloNumeroDecimaliQtaPropertyChange {

		/**
		 * Costruttore.
		 * 
		 * @param formModel
		 *            formModel
		 * @param qtaTextComponentField
		 *            qtaTextComponentField
		 */
		public RigaArticoloNumeroDecimaliQtaMagazzinoPropertyChange(final FormModel formModel,
				final JFormattedTextField qtaTextComponentField) {
			super(formModel, qtaTextComponentField);
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			Double d = (Double) getValue("qtaMagazzino");
			super.propertyChange(evt);
			qtaMagazzinoFormattedField.setValue(d);
		}

	}

	public static final String FORM_ID = "rigaArticolo2Form";

	protected static Logger logger = Logger.getLogger(RigaArticolo2Form.class);

	private boolean isContabilitaPluginEnabled;
	private RigaArticoloNumeroDecimaliQtaMagazzinoPropertyChange rigaArticoloNumeroDecimaliQtaPropertyChange = null;
	private JFormattedTextField qtaMagazzinoFormattedField;

	/**
	 * Costruttore.
	 * 
	 * @param formModel
	 *            formmodel da utilizzare
	 */
	public RigaArticolo2Form(final FormModel formModel) {
		super(formModel, FORM_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:pref,4dlu,fill:80dlu,left:50dlu,6dlu", "2dlu,default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r, c");

		builder.nextRow();
		builder.setRow(2);

		builder.addLabel("qtaMagazzino", 1);
		qtaMagazzinoFormattedField = (JFormattedTextField) builder.addBinding(
				bf.createBoundFormattedTextField("qtaMagazzino", getFactory()), 3);
		qtaMagazzinoFormattedField.setHorizontalAlignment(SwingConstants.RIGHT);
		qtaMagazzinoFormattedField.setColumns(5);

		JTextField umComponent = (JTextField) builder.addProperty("unitaMisuraQtaMagazzino", 4);
		umComponent.setColumns(5);
		builder.nextRow();

		builder.addLabel("codiceIva", 1);
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("codiceIva", new String[] { "codice" }), 3);
		searchPanel.getTextFields().get("codice").setColumns(6);

		if (isContabilitaPluginEnabled()) {
			builder.nextRow();

			builder.addLabel("categoriaContabileArticolo", 1);
			builder.addBinding(bf.createBoundSearchText("categoriaContabileArticolo", new String[] { "codice" }), 3);
		}

		builder.nextRow();

		getFormModel().getValueModel("numeroDecimaliQta").getValue();
		installNumerDecimaliQtaPropertyChange();
		getFormModel().getValueModel("codiceIva").addValueChangeListener(new RigaArticoloCodiceIvaChangeListener());

		return builder.getPanel();
	}

	@Override
	public void dispose() {
		getFormModel().getValueModel("numeroDecimaliQta").removeValueChangeListener(
				getRigaArticoloNumeroDecimaliQtaPropertyChange());

		super.dispose();
	}

	/**
	 * @return factory per la formattazione qtaDecimanli
	 */
	private DefaultFormatterFactory getFactory() {
		Integer numeroDecimaliQt = (Integer) getFormModel().getValueModel("numeroDecimaliQta").getValue();
		DefaultNumberFormatterFactory f = new DefaultNumberFormatterFactory("#,##0", numeroDecimaliQt, Double.class);
		return f;
	}

	/**
	 * @return RigaArticoloNumeroDecimaliQtaPropertyChange
	 */
	private RigaArticoloNumeroDecimaliQtaPropertyChange getRigaArticoloNumeroDecimaliQtaPropertyChange() {
		if (rigaArticoloNumeroDecimaliQtaPropertyChange == null) {
			rigaArticoloNumeroDecimaliQtaPropertyChange = new RigaArticoloNumeroDecimaliQtaMagazzinoPropertyChange(
					getFormModel(), qtaMagazzinoFormattedField);
		}
		return rigaArticoloNumeroDecimaliQtaPropertyChange;
	}

	/**
	 * Installa il listener sulla proprietà "rigaArticolo.numeroDecimaliQta".
	 */
	private void installNumerDecimaliQtaPropertyChange() {
		getFormModel().getValueModel("numeroDecimaliQta").addValueChangeListener(
				getRigaArticoloNumeroDecimaliQtaPropertyChange());
	}

	/**
	 * 
	 * @return true se il plugin della contabiltà è abilitato
	 */
	public boolean isContabilitaPluginEnabled() {
		return isContabilitaPluginEnabled;
	}

	/**
	 * setter for contabilitaPluginEnabled.
	 * 
	 * @param contabilitaPluginEnabled
	 *            true se plugin abilitato
	 */
	public void setContabilitaPluginEnabled(boolean contabilitaPluginEnabled) {
		this.isContabilitaPluginEnabled = contabilitaPluginEnabled;
	}

}
