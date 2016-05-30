/**
 *
 */
package it.eurotn.panjea.magazzino.rich.forms.fatturazione;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento;
import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.magazzino.domain.SedeMagazzinoLite;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.search.SedeMagazzinoLiteSearchObject;
import it.eurotn.panjea.magazzino.util.parametriricerca.ParametriRicercaFatturazione;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.bd.ValutaAziendaCache;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.RefreshableValueHolder;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;
import org.springframework.rules.closure.Closure;

import com.jgoodies.forms.layout.FormLayout;

/**
 * @author fattazzo
 */
public class ParametriRicercaFatturazioneForm extends PanjeaAbstractForm {

	private class EntitaPropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (getFormModel().isReadOnly()) {
				return;
			}

			EntitaLite entita = (EntitaLite) evt.getNewValue();

			if (entita == null) {
				getFormModel().getFieldMetadata("categoriaEntita").setReadOnly(false);
			} else {
				getFormModel().getFieldMetadata("categoriaEntita").setReadOnly(true);
				getFormModel().getValueModel("categoriaEntita").setValue(null);
			}
		}

	}

	private class SedePerFatturazionePropertyChange implements PropertyChangeListener {

		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (getFormModel().isReadOnly()) {
				return;
			}

			SedeMagazzinoLite sedeMagazzino = (SedeMagazzinoLite) evt.getNewValue();

			boolean showFatturazioneControl = sedeMagazzino == null;

			bindingCodPag.getControl().setVisible(showFatturazioneControl);
			labelCodPag.setVisible(showFatturazioneControl);
			sedeEntitaBinding.getControl().setVisible(showFatturazioneControl);
			sedeEntitaLabel.setVisible(showFatturazioneControl);
			bindingEntita.getControl().setVisible(showFatturazioneControl);
			entitaLabel.setVisible(showFatturazioneControl);
		}

	}

	private class TipoDocumentoDestinazionePropertyChange implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			tipiDocDaFatturareValueHolder.refresh();
			getFormModel().getValueModel("tipiDocumentoDaFatturare").setValue(listTipiDoc);
		}
	}

	public static final String FORM_ID = "parametriRicercaFatturazioneForm";
	public static final String FORMMODEL_ID = "parametriRicercaFatturazioneFormModel";

	private IMagazzinoDocumentoBD magazzinoDocumentoBD;

	private RefreshableValueHolder tipiDocDaFatturareValueHolder;

	private Binding bindingCodPag;
	private JLabel labelCodPag;
	private Binding sedeEntitaBinding;
	private Binding bindingEntita;
	private JLabel sedeEntitaLabel;
	private JLabel entitaLabel;

	private List<TipoDocumento> listTipiDoc = new ArrayList<TipoDocumento>();

	private TipoDocumentoDestinazionePropertyChange tipoDocumentoDestinazionePropertyChange = null;
	private SedePerFatturazionePropertyChange sedePerFatturazionePropertyChange = null;
	private EntitaPropertyChange entitaPropertyChange = null;

	private ValutaAziendaCache valutaCache = null;

	/**
	 * Costruttore.
	 */
	public ParametriRicercaFatturazioneForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriRicercaFatturazione(), false, FORMMODEL_ID), FORM_ID);
		this.valutaCache = RcpSupport.getBean(ValutaAziendaCache.BEAN_ID);

		// aggiungo la finta proprietà tipiEntita per far si che la search text degli agenti mi selezioni solo agenti e
		// non altri tipi entità
		List<TipoEntita> tipiEntitaAgente = new ArrayList<TipoEntita>();
		tipiEntitaAgente.add(TipoEntita.AGENTE);

		ValueModel tipiEntitaAgenteValueModel = new ValueHolder(tipiEntitaAgente);
		DefaultFieldMetadata tipiEntitaAgenteData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipiEntitaAgenteValueModel), List.class, true, null);
		getFormModel().add("tipiEntitaAgente", tipiEntitaAgenteValueModel, tipiEntitaAgenteData);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout(
				"right:pref,4dlu,left:default, 10dlu, right:pref,4dlu,left:default, 10dlu, max(100dlu;pref):grow",
				"4dlu,default, 2dlu,default, 2dlu,default, 2dlu,default, 2dlu,default,2dlu,default,2dlu,default,2dlu,default,default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel()
		builder.setLabelAttributes("r,c");

		builder.nextRow();
		builder.setRow(2);

		// ROW 1: Periodo e tipi doc da fatturare
		builder.addPropertyAndLabel("periodo", 1);
		tipiDocDaFatturareValueHolder = new RefreshableValueHolder(new Closure() {

			@Override
			public Object call(Object argument) {
				TipoDocumento tipoDocumentoDestinazione = (TipoDocumento) getFormModel().getValueModel(
						"tipoDocumentoDestinazione").getValue();
				listTipiDoc = new ArrayList<TipoDocumento>();
				if (tipoDocumentoDestinazione != null) {
					listTipiDoc = magazzinoDocumentoBD.caricaTipiDocumentoPerFatturazione(tipoDocumentoDestinazione);
				}
				return listTipiDoc;
			}
		});
		tipiDocDaFatturareValueHolder.refresh();
		JLabel labelStato = getComponentFactory().createLabel("");
		getFormModel().getFieldFace("tipiDocumentoDaFatturare").configure(labelStato);
		Binding tipiDocumentoDaFatturareBinding = bf.createBoundList("tipiDocumentoDaFatturare",
				tipiDocDaFatturareValueHolder, "descrizione", ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		builder.nextRow();

		// ROW 2: tipi doc destinazione e valuta
		ValueHolder tipiDocumentoValueHolder = new ValueHolder(
				magazzinoDocumentoBD.caricaTipiDocumentoDestinazioneFatturazione());
		Binding bindingTipoDocDest = bf.createBoundComboBox("tipoDocumentoDestinazione", tipiDocumentoValueHolder,
				"descrizione");
		builder.addLabel("tipoDocumentoDestinazione", 1);
		builder.addBinding(bindingTipoDocDest, 3);

		builder.addLabel("codiceValuta", 5);
		Binding valutaBinding = bf.createBoundSearchText("codiceValuta", null, ValutaAzienda.class);
		SearchPanel valutaSearchPanel = (SearchPanel) builder.addBinding(valutaBinding, 7);
		valutaSearchPanel.getTextFields().get(null).setColumns(10);
		builder.nextRow();

		// ROW 3: codice pagamento
		bindingCodPag = bf.createBoundSearchText("codicePagamento", new String[] { "codicePagamento", "descrizione" });
		labelCodPag = builder.addLabel("codicePagamento", 1);
		SearchPanel searchPanelCodicePagamento = (SearchPanel) builder.addBinding(bindingCodPag, 3);
		searchPanelCodicePagamento.getTextFields().get("codicePagamento").setColumns(6);
		searchPanelCodicePagamento.getTextFields().get("descrizione").setColumns(18);

		builder.nextRow();
		builder.addLabel("categoriaEntita");
		builder.addBinding(bf.createBoundSearchText("categoriaEntita", new String[] { "descrizione" }), 3);

		builder.nextRow();

		bindingEntita = bf.createBoundSearchText("entitaLite", new String[] { "codice", "anagrafica.denominazione" },
				new String[] { "tipoDocumentoDestinazione.tipoEntita" },
				new String[] { EntitaByTipoSearchObject.TIPOENTITA_KEY });
		entitaLabel = builder.addLabel("entitaLite");
		SearchPanel searchPanelEntita = (SearchPanel) builder.addBinding(bindingEntita, 3);
		searchPanelEntita.getTextFields().get("codice").setColumns(6);
		searchPanelEntita.getTextFields().get("anagrafica.denominazione").setColumns(18);

		sedeEntitaBinding = bf.createBoundSearchText("sedeEntita", new String[] { "sede.descrizione" },
				new String[] { "entitaLite" }, new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID });
		sedeEntitaLabel = builder.addLabel("sedeEntita", 5);
		SearchPanel searchPanelSedeEntita = (SearchPanel) builder.addBinding(sedeEntitaBinding, 7);
		searchPanelSedeEntita.getTextFields().get("sede.descrizione").setColumns(10);
		builder.nextRow();

		Binding bindingZonaGeo = bf.createBoundSearchText("zonaGeografica", new String[] { "codice", "descrizione" });
		SearchPanel searchPanel = (SearchPanel) bindingZonaGeo.getControl();
		searchPanel.getTextFields().get("codice").setColumns(6);
		searchPanel.getTextFields().get("descrizione").setColumns(18);
		builder.addLabel("zonaGeografica");
		builder.addBinding(bindingZonaGeo, 3);
		builder.nextRow();

		PluginManager pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
		if (pluginManager.isPresente(PluginManager.PLUGIN_AGENTI)) {
			builder.addLabel("agente");
			SearchPanel searchAgente = (SearchPanel) builder.addBinding(bf.createBoundSearchText("agente",
					new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipiEntitaAgente" },
					new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class), 3);
			searchAgente.getTextFields().get("codice").setColumns(6);
			searchAgente.getTextFields().get("anagrafica.denominazione").setColumns(18);
			builder.nextRow();
		}

		Binding sedeFattBinding = bf.createBoundSearchText("sedePerRifatturazione", new String[] {
				"sedeEntita.entita.anagrafica.denominazione", "sedeEntita.sede.descrizione" },
				new String[] { "sedePerRifatturazione" },
				new String[] { SedeMagazzinoLiteSearchObject.PARAM_SEDI_RIFATTURAZIONE });
		builder.addLabel("sedePerRifatturazione", 1);
		SearchPanel searchPanelSedeRifatturaz = (SearchPanel) builder.addBinding(sedeFattBinding, 3);
		searchPanelSedeRifatturaz.getTextFields().get("sedeEntita.entita.anagrafica.denominazione").setColumns(12);
		searchPanelSedeRifatturaz.getTextFields().get("sedeEntita.sede.descrizione").setColumns(12);
		builder.nextRow();

		tipiDocumentoDaFatturareBinding.getControl().setOpaque(false);
		JScrollPane scrollPane = new JScrollPane(tipiDocumentoDaFatturareBinding.getControl());
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);

		builder.addComponent(getComponentFactory().createTitledBorderFor(labelStato.getText(), scrollPane), 9, 1, 1, 15);

		installListeners();

		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		ParametriRicercaFatturazione parametri = new ParametriRicercaFatturazione();
		// se creo i nuovi parametri devo aggiungere il codice valuta dell'azienda
		if (parametri.getCodiceValuta() == null
				|| (parametri.getCodiceValuta() != null && parametri.getCodiceValuta().isEmpty())) {
			parametri.setCodiceValuta(valutaCache.caricaValutaAziendaCorrente().getCodiceValuta());
		}
		return parametri;
	}

	@Override
	public void dispose() {
		getFormModel().getValueModel("tipoDocumentoDestinazione").removeValueChangeListener(
				getTipoDocumentoDestinazionePropertyChange());

		getFormModel().getValueModel("sedePerRifatturazione").removeValueChangeListener(
				getSedePerFatturazionePropertyChange());

		getFormModel().getValueModel("entitaLite").removeValueChangeListener(getEntitaPropertyChange());
		super.dispose();
	}

	/**
	 * @return Returns the entitaPropertyChange.
	 */
	private EntitaPropertyChange getEntitaPropertyChange() {
		if (entitaPropertyChange == null) {
			entitaPropertyChange = new EntitaPropertyChange();
		}

		return entitaPropertyChange;
	}

	/**
	 * @return the SedePerFatturazionePropertyChange to get
	 */
	private SedePerFatturazionePropertyChange getSedePerFatturazionePropertyChange() {
		if (sedePerFatturazionePropertyChange == null) {
			sedePerFatturazionePropertyChange = new SedePerFatturazionePropertyChange();
		}
		return sedePerFatturazionePropertyChange;
	}

	/**
	 * @return the TipoDocumentoDestinazionePropertyChange to get
	 */
	private TipoDocumentoDestinazionePropertyChange getTipoDocumentoDestinazionePropertyChange() {
		if (tipoDocumentoDestinazionePropertyChange == null) {
			tipoDocumentoDestinazionePropertyChange = new TipoDocumentoDestinazionePropertyChange();
		}
		return tipoDocumentoDestinazionePropertyChange;
	}

	/**
	 * Installa tutti i listener del form.
	 */
	private void installListeners() {

		getFormModel().getValueModel("tipoDocumentoDestinazione").addValueChangeListener(
				getTipoDocumentoDestinazionePropertyChange());

		getFormModel().getValueModel("sedePerRifatturazione").addValueChangeListener(
				getSedePerFatturazionePropertyChange());

		getFormModel().getValueModel("entitaLite").addValueChangeListener(getEntitaPropertyChange());
	}

	@Override
	public void setFormObject(Object formObject) {
		// se non ho importato una valuta setto quella dell'azienda
		ParametriRicercaFatturazione parametri = (ParametriRicercaFatturazione) formObject;
		if (parametri.getCodiceValuta() == null
				|| (parametri.getCodiceValuta() != null && parametri.getCodiceValuta().isEmpty())) {
			parametri.setCodiceValuta(valutaCache.caricaValutaAziendaCorrente().getCodiceValuta());
		}
		super.setFormObject(formObject);
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            setter of magazzinoDocumentoBD
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

}
