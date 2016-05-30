/**
 *
 */
package it.eurotn.panjea.magazzino.binding;

import it.eurotn.panjea.anagrafica.rich.pm.AziendaCorrente;
import it.eurotn.panjea.magazzino.domain.AttributoArticolo;
import it.eurotn.panjea.magazzino.domain.TipoAttributo;
import it.eurotn.panjea.magazzino.domain.TipoAttributo.ETipoDatoTipoAttributo;
import it.eurotn.panjea.magazzino.domain.TipoVariante;
import it.eurotn.panjea.magazzino.domain.ValoreVariante;
import it.eurotn.panjea.magazzino.domain.ValoreVarianteNumerica;
import it.eurotn.panjea.magazzino.domain.ValoreVarianteStringa;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.springframework.binding.form.FormModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.swing.AsYouTypeTextComponentAdapter;
import org.springframework.richclient.form.binding.support.AbstractBinding;
import org.springframework.richclient.layout.TableLayoutBuilder;
import org.springframework.richclient.list.ComboBoxListModel;
import org.springframework.richclient.list.TextValueListRenderer;

/**
 * Component Binding per {@link AttributoArticolo}.
 * 
 * @author adriano
 * @version 1.0, 05/mag/08
 * 
 */
public class AttributoArticoloBinding extends AbstractBinding {

	private class TipoVarianteCheckBox implements PropertyChangeListener, ItemListener {

		/**
		 * Costruttore.
		 */
		public TipoVarianteCheckBox() {
			super();
			getValueModel().addValueChangeListener(this);
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			if (checkBox.isSelected()) {
				getValueModel().setValueSilently(Boolean.TRUE.toString(), this);
			} else {
				getValueModel().setValueSilently(Boolean.FALSE.toString(), this);
			}
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (ValueModel.VALUE_PROPERTY.equals(evt.getPropertyName())) {
				checkBox.setSelected(Boolean.TRUE.equals(getValueModel().getValue()));
			}
		}

	}

	private class TipoVarianteComboBoxListModel extends ComboBoxListModel implements PropertyChangeListener {

		private static final long serialVersionUID = 1L;

		/**
		 * 
		 * @param items
		 *            elementi de inserire nella checkbox
		 */
		public TipoVarianteComboBoxListModel(final List items) {
			super(items);
			getValueModel().addValueChangeListener(this);
			initSelectedItem();
		}

		/**
		 * metodo incaricato di individuare l'item dalla List corrispondente al ValueModel di {@link AttributoArticolo}
		 * la ricerca avviene attraverso la corrispondenza della proprietà valore di {@link AttributoArticolo} e del
		 * valore di {@link ValoreVariante}.
		 */
		@SuppressWarnings("unchecked")
		protected void initSelectedItem() {
			logger.debug("--> Enter TipoVarianteComboBoxListModel.initSelectedItem ");
			if (getValueModel().getValue() == null) {
				logger.debug("--> TipoVarianteComboBoxListModel.initSelectedItem null ");
				// nessun elemento selezionato
				setSelectedItemOnNullValue();
				return;
			}
			logger.debug("--> ricerca valoreVariante di " + getValue());
			for (Iterator iterator = getItems().iterator(); iterator.hasNext();) {
				ValoreVariante valoreVariante = (ValoreVariante) iterator.next();
				logger.debug("--> confronto valoreVariante con " + valoreVariante.getValore());
				if (getValue().equals(valoreVariante.getValore().toString())) {
					// valore di AttributoArticolo corrispondente all'elemento corrente della List, chiamo la
					// setSelectedItem
					setSelectedItem(valoreVariante);
					return;
				}
			}
			logger.debug("--> Exit TipoVarianteComboBoxListModel.initSelectedItem");
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			logger.debug("--> Enter TipoVarianteComboBoxListModel.propertyChange " + evt.getPropertyName() + " "
					+ evt.getNewValue());
			// if (ValueModel.VALUE_PROPERTY.equals(evt.getPropertyName())) {
			// setSelectedItem(evt.getNewValue());
			// }
			logger.debug("--> Exit TipoVarianteComboBoxListModel.propertyChange");
		}

		@Override
		public void setSelectedItem(Object selectedItem) {
			logger.debug("--> Enter TipoVarianteComboBoxListModel.setSelectedItem " + selectedItem);
			super.setSelectedItem(selectedItem);
			if (!(selectedItem instanceof ValoreVariante)) {
				logger.debug("--> Exit TipoVarianteComboBoxListModel.setSelectedItem : selectedItem  non è istanza di ValoreVariante");
				return;
			}
			ValoreVariante valoreVariante = (ValoreVariante) selectedItem;
			getValueModel().setValueSilently(valoreVariante.getValore().toString(), this);
			logger.debug("--> Exit setSelectedItem " + valoreVariante);
		}

	}

	/**
	 * renderer personalizzato di di ValoreVariante per la ricerca del valore nella lingua di codiceLingua.
	 * 
	 * @author adriano
	 * @version 1.0, 14/mag/08
	 * 
	 */
	private class ValoreVarianteValueListRenderer extends TextValueListRenderer {

		private static final long serialVersionUID = 1L;

		private final String codiceLingua;

		/**
		 * @param codiceLingua
		 *            codice della lingua da visualizzare
		 */
		public ValoreVarianteValueListRenderer(final String codiceLingua) {
			super();
			this.codiceLingua = codiceLingua;
		}

		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			logger.debug("--> ValoreVarianteValueListRenderer.getListCellRendererComponent index: " + index
					+ "; value: " + value + " isSelected: " + isSelected);
			if ((index == -1) && (getValueModel().getValue() == null)) {
				// se viene effettuato il renderer dell'object con index == 1 e il value model is null setto il text del
				// renderer ad empty string
				setText("");
				return this;
			}
			return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		}

		@Override
		protected String getTextValue(Object value) {
			logger.debug("--> Enter ValoreVarianteValueListRenderer.getTextValue " + value);
			if (value == null) {
				logger.debug("--> Exit ValoreVarianteValueListRenderer.getTextValue: value == null ");
				return "";
			}
			if (value instanceof ValoreVariante) {
				logger.debug("--> Exit ValoreVarianteValueListRenderer.getTextValue: " + value);
				return getValoreVariante((ValoreVariante) value);
			}
			logger.debug("--> Exit ValoreVarianteValueListRenderer.getTextValue !(value instanceof ValoreVariante) ");
			return "";
		}

		/**
		 * Getter dell'attributo valore di ValoreVariante. Se codiceLingua è diversa dal codice lingua dell'azienda
		 * viene restituito il suo valore in lingua
		 * 
		 * @param valoreVariante
		 *            variante per la quale restituire il valore
		 * @return valore della variante in linuga.
		 */
		private String getValoreVariante(ValoreVariante valoreVariante) {
			logger.debug("--> Enter ValoreVarianteValueListRenderer.getValoreVariante " + valoreVariante);
			if (valoreVariante instanceof ValoreVarianteNumerica) {
				logger.debug("--> Exit ValoreVarianteValueListRenderer.getValoreVariante valoreVariante instanceof ValoreVarianteNumerica : "
						+ valoreVariante.getValore());
				return String.valueOf(valoreVariante.getValore());
			}
			if (codiceLingua.equals(AttributoArticoloBinding.this.aziendaCorrente.getLingua())) {
				logger.debug("--> Exit ValoreVarianteValueListRenderer.getValoreVariante codiceLingua equals aziendaCorrente.getLingua : "
						+ valoreVariante.getValore());
				return String.valueOf(valoreVariante.getValore());
			}
			if (valoreVariante instanceof ValoreVarianteStringa) {
				logger.debug("-->  ValoreVarianteValueListRenderer cerca il valore in lingua di " + codiceLingua);
				ValoreVarianteStringa valoreVarianteStringa = (ValoreVarianteStringa) valoreVariante;
				if (valoreVarianteStringa.getValoriLingua().containsKey(codiceLingua)) {
					logger.debug("--> Exit ValoreVarianteValueListRenderer.getValoreVariante "
							+ valoreVarianteStringa.getValoriLingua().get(codiceLingua).getDescrizione());
					return valoreVarianteStringa.getValoriLingua().get(codiceLingua).getDescrizione();
				}
			}
			logger.debug("--> Exit ValoreVarianteValueListRenderer.getValoreVariante non trovato ");
			return "";
		}

	}

	public static final String CODICE_LINGUA_KEY = "codiceLingua";
	public static final String AZIENDA_CORRENTE_KEY = "aziendaCorrente";

	private JPanel panel;
	private JLabel label;
	private JComboBox comboBox = null;

	private JTextField textComponent = null;
	private JCheckBox checkBox = null;

	private String codiceLingua;

	private AziendaCorrente aziendaCorrente;

	/**
	 * 
	 * Costruttore.
	 * 
	 * @param formModel
	 *            .
	 * @param formPropertyPath
	 *            .
	 * @param requiredSourceClass
	 *            .
	 * @param context
	 *            .
	 */
	public AttributoArticoloBinding(final FormModel formModel, final String formPropertyPath,
			final Class<?> requiredSourceClass, final Map<String, Object> context) {
		super(formModel, formPropertyPath, requiredSourceClass);
		applyContext(context);
	}

	/**
	 * Applica il context al binding di AttributoArticolo.
	 * 
	 * @param context
	 *            contesto del binding
	 */
	protected void applyContext(Map<String, Object> context) {
		logger.debug("--> Enter applyContext");
		codiceLingua = Locale.getDefault().getLanguage();
		if (context.containsKey(CODICE_LINGUA_KEY)) {
			codiceLingua = (String) context.get(CODICE_LINGUA_KEY);
		}
		aziendaCorrente = null;
		if (context.containsKey(AZIENDA_CORRENTE_KEY)) {
			aziendaCorrente = (AziendaCorrente) context.get(AZIENDA_CORRENTE_KEY);
		}

		logger.debug("--> Exit applyContext");
	}

	/**
	 * crea e restituisce il componente adatto al tipo dato di TipoAttributo.
	 * 
	 * @return componente del binding
	 */
	@SuppressWarnings("unchecked")
	private JComponent createComponent() {
		logger.debug("--> Enter createComponent");
		AttributoArticolo attributoArticolo = (AttributoArticolo) getFormModel().getFormObject();
		if (attributoArticolo.getTipoAttributo() instanceof TipoVariante) {
			TipoVariante tipoVariante = (TipoVariante) attributoArticolo.getTipoAttributo();
			comboBox = getComponentFactory().createComboBox();
			comboBox.setRenderer(new ValoreVarianteValueListRenderer(codiceLingua));
			comboBox.setModel(new TipoVarianteComboBoxListModel(tipoVariante.getValoriVarianti()));
			comboBox.setEditable(false);

			// se ho dei valori varianti inserisco il primo valore di default sull'attributo.
			// if (tipoVariante.getValoriVarianti().size() > 0) {
			// getFormModel().getValueModel("valore").setValue(
			// tipoVariante.getValoriVarianti().get(0).getValore().toString());
			// getFormModel().commit();
			// }

			logger.debug("--> Exit createComponent : comboBox ");
			return comboBox;
		} else if (ETipoDatoTipoAttributo.BOOLEANO.equals(attributoArticolo.getTipoAttributo().getTipoDato())) {
			checkBox = getComponentFactory().createCheckBox("");
			// HACK la definizione del valore in AttributoArticolo è momentaneamente definita come vero/falso
			checkBox.setSelected(Boolean.TRUE.toString().equals(attributoArticolo.getValore()));
			checkBox.getModel().addItemListener(new TipoVarianteCheckBox());
			logger.debug("--> Exit createComponent : checkBox ");
			return checkBox;
		}

		textComponent = getComponentFactory().createTextField();
		if (ETipoDatoTipoAttributo.NUMERICO.equals(attributoArticolo.getTipoAttributo().getTipoDato())) {
			textComponent.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		textComponent.setColumns(30);
		textComponent.setText(attributoArticolo.getValore());
		new AsYouTypeTextComponentAdapter(textComponent, getValueModel());
		logger.debug("--> Exit createComponent : textComponent");
		return textComponent;
	}

	@Override
	protected JComponent doBindControl() {
		logger.debug("--> Enter doBindControl");
		if (panel != null) {
			return panel;
		}
		TableLayoutBuilder tableLayoutBuilder = new TableLayoutBuilder(getComponentFactory().createPanel());
		label = getComponentFactory().createLabel("");
		label.setText(getNomeAttributo());

		tableLayoutBuilder.cell(label);
		tableLayoutBuilder.cell("colSpec=5");
		String unitaMisura = getUnitaDiMisuraAttributo();
		if (unitaMisura != null) {
			tableLayoutBuilder.cell(new JLabel(unitaMisura), "colSpec=right:max(10dlu;p)");
			tableLayoutBuilder.cell("colSpec=5");
		}
		tableLayoutBuilder.cell(createComponent(), "colSpec=150");
		panel = tableLayoutBuilder.getPanel();

		logger.debug("--> Exit doBindControl");
		return panel;
	}

	@Override
	protected void enabledChanged() {
		logger.debug("--> Enter enabledChanged " + isEnabled());
		if (comboBox != null) {
			logger.debug("--> enabled comboBox " + isEnabled());
			// comboBox.setEditable(false);
			// comboBox.setEnabled(false);
		}
		if (textComponent != null) {
			logger.debug("--> enabled textComponent " + isEnabled());
			textComponent.setEnabled(isEnabled());
		}
		if (checkBox != null) {
			logger.debug("--> enabled checkBox " + isEnabled());
			// checkBox.setEnabled(isEnabled());
		}
		logger.debug("--> Exit enabledChanged");
	}

	/**
	 * restituisce il nome di AttributoArticolo corrispondente alla Lingua di codiceLingua.
	 * 
	 * @return nome dell'attributo
	 */
	private String getNomeAttributo() {
		logger.debug("--> Enter getNomeAttributo");
		TipoAttributo tipoAttributo = (TipoAttributo) getFormModel().getValueModel("tipoAttributo").getValue();
		if (codiceLingua.equals(aziendaCorrente.getLingua())) {
			// codiceLingua corrisponde alla Lingua di AziendaCorrente: viene restituita la descrizione aziendale
			logger.debug("--> Exit getNomeAttributo aziendale ");
			return tipoAttributo.getNome();
		}
		// ricerca del nome attributo per lingua
		if (tipoAttributo.getNomiLingua().containsKey(codiceLingua)) {
			logger.debug("--> Exit getNomeAttributo in lingua");
			return tipoAttributo.getNomiLingua().get(codiceLingua).getDescrizione();
		}
		logger.warn("--> Exit getNomeAttributo non trovato");
		return "";
	}

	/**
	 * Restituisce l'unità di misura associata al tipo attributo.<br>
	 * Se il tipo attributo non ha una unità di misura collegata viene restituita una stringa vuota.
	 * 
	 * @return unità di misure dell'attributo.
	 */
	private String getUnitaDiMisuraAttributo() {
		logger.debug("--> Enter getUnitaDiMisuraAttributo");
		TipoAttributo tipoAttributo = (TipoAttributo) getFormModel().getValueModel("tipoAttributo").getValue();

		if (tipoAttributo.getUnitaMisura() == null) {
			return null;
		} else {
			return "[" + tipoAttributo.getUnitaMisura().getCodice() + "]";
		}
	}

	@Override
	protected void readOnlyChanged() {
		logger.debug("--> Enter readOnlyChanged " + isReadOnly());
		if (comboBox != null) {
			logger.debug("--> readOnlyChanged comboBox " + isReadOnly());
			comboBox.setEditable(false);
			comboBox.setEnabled(!isReadOnly());
		}
		if (textComponent != null) {
			logger.debug("--> readOnlyChanged textComponent " + isReadOnly());
			textComponent.setEnabled(!isReadOnly());
			textComponent.setEditable(!isReadOnly());
		}
		if (checkBox != null) {
			logger.debug("--> readOnlyChanged checkBox " + isReadOnly());
			checkBox.setEnabled(!isReadOnly());
		}
		logger.debug("--> Exit readOnlyChanged");
	}

}
