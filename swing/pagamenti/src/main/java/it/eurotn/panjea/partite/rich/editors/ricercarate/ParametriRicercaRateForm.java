/**
 *
 */
package it.eurotn.panjea.partite.rich.editors.ricercarate;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.Banca;
import it.eurotn.panjea.anagrafica.domain.TipoPagamento;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.partite.domain.TipoAreaPartita.TipoPartita;
import it.eurotn.panjea.partite.util.ParametriRicercaRate;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rate.domain.Rata.StatoRata;
import it.eurotn.panjea.rich.bd.ValutaAziendaCache;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.panjea.util.DefaultNumberFormatterFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.binding.searchtext.SearchTextBinding;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;

import org.apache.log4j.Logger;
import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.components.Focussable;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Leonardo
 */
public class ParametriRicercaRateForm extends PanjeaAbstractForm implements Focussable {

	/**
	 * Sul cambio del codice valuta aggiorno l'importo del filtro con il codice valuta trovato.
	 * 
	 * @author giangi
	 * @version 1.0, 25/ago/2011
	 * 
	 */
	private class CodiceValutaPropertyChange implements PropertyChangeListener {

		private ValutaAziendaCache valutaCache;

		/**
		 * Costruttore.
		 */
		public CodiceValutaPropertyChange() {
			valutaCache = RcpSupport.getBean(ValutaAziendaCache.BEAN_ID);
		}

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			int numeroDecimali = 6;
			if (evt.getNewValue() != null && !((String) evt.getNewValue()).isEmpty()) {
				numeroDecimali = valutaCache.caricaValutaAzienda(evt.getNewValue().toString()).getNumeroDecimali();
			}
			DefaultNumberFormatterFactory formatter = new ImportoNumberFormatterFactory(
					ValutaAzienda.DEFAULT_FORMATTER, numeroDecimali);
			importoFinaleControl.setFormatterFactory(formatter);
			importoInizialeControl.setFormatterFactory(formatter);
		}

	}

	private class CompensazionePropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			Boolean compensazione = (Boolean) evt.getNewValue();

			getFormModel().getFieldMetadata("tipoPartita").setReadOnly(compensazione);

			if (compensazione) {
				getValueModel("sedeEntita").setValue(null);
				getValueModel("entita").setValue(null);
			} else {
				getValueModel("anagrafica").setValue(null);
			}

			bindingEntita.getControl().setVisible(!compensazione);
			entitaLabel.setVisible(!compensazione);

			sedeEntitaBinding.getControl().setVisible(!compensazione);
			sedeEntitaLabel.setVisible(!compensazione);

			bindingAnagrafica.getControl().setVisible(compensazione);
			anagraficaLabel.setVisible(compensazione);
		}

	}

	private class ImportoNumberFormatterFactory extends DefaultNumberFormatterFactory {

		private static final long serialVersionUID = 6047414836727898471L;

		/**
		 * Costruttore.
		 * 
		 * @param pattern
		 *            pattern per la formattazione
		 * @param numeroDecimaliQta
		 *            numero dei decimali da visualizzare
		 */
		public ImportoNumberFormatterFactory(final String pattern, final Integer numeroDecimaliQta) {
			super(pattern, numeroDecimaliQta, BigDecimal.class);
		}

		@Override
		public void configureNumberFormatter(NumberFormatter numberFormatter) {
			super.configureNumberFormatter(numberFormatter);
			numberFormatter.setAllowsInvalid(true);
		}

		@Override
		public NumberFormatter getNumberFormatter() {
			return new NumberFormatter() {
				private static final long serialVersionUID = 5019974328902866896L;

				@Override
				public Object stringToValue(String text) throws java.text.ParseException {
					if (text == null || text.length() == 0) {
						return null;
					}
					return super.stringToValue(text);
				}
			};
		}
	}

	private class TipoPartitaPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			getFormModel().getValueModel("entita").setValue(null);

			// risetto il TipoEntita perchè mi viene cambiato sul setTipoPartita
			// ma il value model non viene aggiornato.
			TipoEntita tipoEntita = (TipoEntita) getFormModel().getValueModel("tipoEntita").getValue();
			getFormModel().getValueModel("tipoEntita").setValue(null);
			getFormModel().getValueModel("tipoEntita").setValue(tipoEntita);
		}

	}

	private static Logger logger = Logger.getLogger(ParametriRicercaRateForm.class);
	public static final String FORM_ID = "parametriRicercaRateForm";
	public static final String FORM_MODEL_ID = "parametriRicercaRateForm";

	private JFormattedTextField importoInizialeControl;
	private JFormattedTextField importoFinaleControl;
	private JComponent focusComponent;

	private Binding bindingEntita;
	private JLabel entitaLabel;

	private Binding sedeEntitaBinding;
	private JLabel sedeEntitaLabel;

	private Binding bindingAnagrafica;
	private JLabel anagraficaLabel;

	private CodiceValutaPropertyChange codiceValutaPropertyChange = null;

	private TipoPartitaPropertyChange tipoPartitaPropertyChange = null;
	private CompensazionePropertyChange compensazionePropertyChange = null;

	/**
	 * Costruttore.
	 * 
	 * @param parametriRicercaRate
	 *            parametri di ricerca
	 */
	public ParametriRicercaRateForm(final ParametriRicercaRate parametriRicercaRate) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicercaRate, false, FORM_MODEL_ID), FORM_ID);

		// Aggiungo il value model che mi servirà solamente nella search text
		// degli agenti per cercare solo le entità di tipo agente
		ValueModel entitaAgenteValueModel = new ValueHolder(TipoEntita.AGENTE);
		DefaultFieldMetadata entitaAgenteMetaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(entitaAgenteValueModel), TipoEntita.class, true, null);
		getFormModel().add("tipoEntitaAgente", entitaAgenteValueModel, entitaAgenteMetaData);
	}

	/**
	 * Aggiunge i property change al form.
	 */
	private void addPropertyChange() {
		addFormValueChangeListener("codiceValuta", getCodiceValutaPropertyChange());
		addFormValueChangeListener("tipoPartita", getTipoPartitaPropertyChange());
		addFormValueChangeListener("compensazione", getCompensazionePropertyChange());
	}

	@Override
	protected JComponent createFormControl() {
		logger.debug("--> Enter createFormControl");
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"left:default,4dlu,left:default,4dlu,right:pref,4dlu,fill:170dlu,4dlu,right:pref,4dlu,left:50dlu,4dlu,right:pref,4dlu,left:50dlu,left:10dlu,right:pref,4dlu,fill:50dlu",
				"2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,10 dlu");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanelNumbered()
		builder.setLabelAttributes("r, c");

		// stati rata
		Binding bindingStato = bf.createBoundEnumCheckBoxList("statiRata", StatoRata.class,
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, JList.VERTICAL_WRAP);
		JLabel labelStatiRata = getComponentFactory().createLabel("");
		getFormModel().getFieldFace("statiRata").configure(labelStatiRata);
		JComponent compStatiRata = getComponentFactory().createTitledBorderFor(labelStatiRata.getText(),
				bindingStato.getControl());
		builder.addComponent(compStatiRata, 1, 2, 1, 12);

		// tipi pagamento
		Binding bindingTipiPagamento = bf.createBoundEnumCheckBoxList("tipiPagamento", TipoPagamento.class,
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, JList.VERTICAL_WRAP);
		JLabel labelTipiPagamento = getComponentFactory().createLabel("");
		getFormModel().getFieldFace("tipiPagamento").configure(labelTipiPagamento);
		JComponent compTipiPag = getComponentFactory().createTitledBorderFor(labelTipiPagamento.getText(),
				bindingTipiPagamento.getControl());
		builder.addComponent(compTipiPag, 3, 2, 1, 11);

		// tipo partita
		Binding bindingTipoPartita = (bf.createEnumRadioButtonBinding(TipoPartita.class, "tipoPartita",
				JList.HORIZONTAL_WRAP));
		builder.addLabel("tipoPartita", 5);

		JPanel panel = getComponentFactory().createPanel(new BorderLayout());
		panel.add(bindingTipoPartita.getControl(), BorderLayout.CENTER);

		Binding bindingCompensazione = bf.createBoundCheckBox("compensazione");
		JCheckBox checkBoxCompensazione = (JCheckBox) bindingCompensazione.getControl();
		checkBoxCompensazione.setText("Compensazione");
		checkBoxCompensazione.setHorizontalTextPosition(SwingConstants.LEFT);
		panel.add(checkBoxCompensazione, BorderLayout.EAST);

		builder.addComponent(panel, 7, 2);

		// anagrafica
		anagraficaLabel = builder.addLabel("anagrafica", 5, 4);
		bindingAnagrafica = bf.createBoundSearchText("anagrafica", new String[] { "codice", "denominazione" });
		builder.addBinding(bindingAnagrafica, 7, 4, 5, 1);
		anagraficaLabel.setVisible(false);
		SearchPanel anagraficaSearchPanel = (SearchPanel) bindingAnagrafica.getControl();
		anagraficaSearchPanel.setVisible(false);
		anagraficaSearchPanel.getTextFields().get("codice").setColumns(6);
		anagraficaSearchPanel.getTextFields().get("denominazione").setColumns(18);

		// entita
		bindingEntita = bf.createBoundSearchText("entita", new String[] { "codice", "anagrafica.denominazione" },
				new String[] { "tipoEntita" }, new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY });
		SearchPanel searchPanelEntita = (SearchPanel) bindingEntita.getControl();
		searchPanelEntita.getTextFields().get("codice").setColumns(5);
		searchPanelEntita.getTextFields().get("anagrafica.denominazione").setColumns(18);
		entitaLabel = builder.addLabel("entita", 5, 4);
		focusComponent = builder.addBinding(bindingEntita, 7, 4);

		sedeEntitaLabel = builder.addLabel("sedeEntita", 9, 4);
		sedeEntitaBinding = bf.createBoundSearchText("sedeEntita", null, new String[] { "entita" },
				new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID });
		SearchPanel searchPanelSede = (SearchPanel) builder.addBinding(sedeEntitaBinding, 11, 4, 7, 1);
		searchPanelSede.getTextFields().get(null).setColumns(30);

		// periodo
		builder.addPropertyAndLabel("dataScadenza", 9, 2, 9, 1);

		// codice pagamento
		builder.addLabel("codicePagamento", 5, 6);
		Binding bindingCodicePagamento = bf.createBoundSearchText("codicePagamento", new String[] { "codicePagamento",
				"descrizione" }, new String[] {}, new String[] {});
		SearchPanel searchCodicePagamento = (SearchPanel) bindingCodicePagamento.getControl();
		searchCodicePagamento.getTextFields().get("codicePagamento").setColumns(5);
		searchCodicePagamento.getTextFields().get("descrizione").setColumns(18);
		builder.addBinding(bindingCodicePagamento, 7, 6);

		// da importo
		builder.addLabel("daImporto", 9, 6);
		importoInizialeControl = (JFormattedTextField) builder.addBinding(
				bf.createBoundFormattedTextField("importoIniziale", new ImportoNumberFormatterFactory("#,##0", 6)), 11,
				6);
		importoInizialeControl.setColumns(8);
		importoInizialeControl.setHorizontalAlignment(JTextField.RIGHT);

		// a importo
		builder.addLabel("AImporto", 13, 6);
		importoFinaleControl = (JFormattedTextField) builder
				.addBinding(bf.createBoundFormattedTextField("importoFinale", new ImportoNumberFormatterFactory(
						"#,##0", 6)), 15, 6);
		importoFinaleControl.setColumns(8);
		importoFinaleControl.setHorizontalAlignment(JTextField.RIGHT);

		// valuta
		builder.addLabel("codiceValuta", 17, 6);
		SearchTextBinding bindingCodiceValuta = (SearchTextBinding) bf.createBoundSearchText("codiceValuta", null,
				ValutaAzienda.class);
		builder.addBinding(bindingCodiceValuta, 19, 6);
		((SearchPanel) bindingCodiceValuta.getControl()).getTextFields().get(null).setColumns(5);

		// rapporto bancario
		Binding bindingBanca = bf.createBoundSearchText("rapportoBancarioAzienda", new String[] { "numero",
				"descrizione" }, new String[] {}, new String[] {});
		SearchPanel searchPanelRapportoBancario = (SearchPanel) bindingBanca.getControl();
		searchPanelRapportoBancario.getTextFields().get("numero").setColumns(5);
		searchPanelRapportoBancario.getTextFields().get("descrizione").setColumns(18);
		builder.addLabel("rapportoBancarioAzienda", 5, 8);
		builder.addBinding(bindingBanca, 7, 8);

		// banca entità
		builder.addLabel("bancaEntita", 9, 8);
		SearchPanel searchPanelBanca = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("bancaEntita", new String[] { Banca.PROP_CODICE, Banca.PROP_DESCRIZIONE }),
				11, 8, 9, 1);
		searchPanelBanca.getTextFields().get(Banca.PROP_CODICE).setColumns(5);
		searchPanelBanca.getTextFields().get(Banca.PROP_DESCRIZIONE).setColumns(18);

		// categoria
		builder.addLabel("categoriaEntita", 5, 10);
		builder.addBinding(bf.createBoundSearchText("categoriaEntita", new String[] { "descrizione" }), 7, 10);

		// zona geografica
		builder.addLabel("zonaGeografica", 9, 10);
		SearchPanel searchPanelZona = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("zonaGeografica", new String[] { "codice", "descrizione" }), 11, 10, 9, 1);
		searchPanelZona.getTextFields().get("codice").setColumns(5);
		searchPanelZona.getTextFields().get("descrizione").setColumns(18);

		// agenti
		PluginManager pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
		if (pluginManager.isPresente(PluginManager.PLUGIN_AGENTI)) {
			builder.setComponentAttributes("f,t");
			builder.setLabelAttributes("r,t");
			Binding bindingAgente = bf.createBoundSearchText("agente", new String[] { "codice",
					"anagrafica.denominazione" }, new String[] { "tipoEntitaAgente" },
					new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY }, EntitaLite.class);
			SearchPanel searchPanelAgente = (SearchPanel) bindingAgente.getControl();
			searchPanelAgente.getTextFields().get("codice").setColumns(5);
			searchPanelAgente.getTextFields().get("anagrafica.denominazione").setColumns(18);
			builder.addLabel("agente", 5, 12);
			builder.addBinding(bindingAgente, 7, 12);
		}

		addPropertyChange();
		logger.debug("--> Exit createFormControl");
		return builder.getPanel();
	}

	@Override
	public void dispose() {
		removeFormValueChangeListener("codiceValuta", getCodiceValutaPropertyChange());
		removeFormValueChangeListener("tipoPartita", getTipoPartitaPropertyChange());
		removeFormValueChangeListener("compensazione", getCompensazionePropertyChange());

		super.dispose();
	}

	/**
	 * @return codiceValutaPropertyChange
	 */
	public CodiceValutaPropertyChange getCodiceValutaPropertyChange() {
		if (codiceValutaPropertyChange == null) {
			codiceValutaPropertyChange = new CodiceValutaPropertyChange();
		}
		return codiceValutaPropertyChange;
	}

	/**
	 * @return compensazionePropertyChange
	 */
	public CompensazionePropertyChange getCompensazionePropertyChange() {
		if (compensazionePropertyChange == null) {
			compensazionePropertyChange = new CompensazionePropertyChange();
		}
		return compensazionePropertyChange;
	}

	/**
	 * @return tipoPartitaPropertyChange
	 */
	public TipoPartitaPropertyChange getTipoPartitaPropertyChange() {
		if (tipoPartitaPropertyChange == null) {
			tipoPartitaPropertyChange = new TipoPartitaPropertyChange();
		}
		return tipoPartitaPropertyChange;
	}

	@Override
	public void grabFocus() {
		((SearchPanel) focusComponent).requestFocusInWindow();
	}

	/**
	 * richiede il focus alla finestra.
	 */
	public void requestFocusInWindow() {
		((SearchPanel) focusComponent).requestFocusInWindow();
	}
}
