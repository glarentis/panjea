/**
 *
 */
package it.eurotn.panjea.magazzino.rich.forms.statistiche.movimentazione;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino;
import it.eurotn.panjea.magazzino.domain.documento.TipoAreaMagazzino.ESezioneTipoMovimento;
import it.eurotn.panjea.magazzino.rich.bd.IMagazzinoDocumentoBD;
import it.eurotn.panjea.magazzino.rich.search.ArticoloSearchObject;
import it.eurotn.panjea.magazzino.util.ParametriRicercaMovimentazione;
import it.eurotn.panjea.plugin.PluginManager;
import it.eurotn.panjea.rich.factory.PanjeaSwingBindingFactory;
import it.eurotn.rich.binding.searchtext.SearchPanel;
import it.eurotn.rich.form.PanjeaAbstractForm;
import it.eurotn.rich.form.PanjeaFormModelHelper;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

/**
 * Form per l'input dei parametri di ricerca della movimentazione.
 * 
 * @author Leonardo
 */
public class ParametriRicercaMovimentazioneMagazzinoForm extends PanjeaAbstractForm {

	public static final String FORM_ID = "parametriRicercaMovimentazioneMagazzinoForm";
	private IMagazzinoDocumentoBD magazzinoDocumentoBD = null;
	private List<TipoAreaMagazzino> tipiAreeMagazzino = null;

	/**
	 * Costruttore di default.
	 * 
	 * @param parametriRicercaMovimentazione
	 *            parametri di ricerca iniziale
	 */
	public ParametriRicercaMovimentazioneMagazzinoForm(
			final ParametriRicercaMovimentazione parametriRicercaMovimentazione) {
		super(PanjeaFormModelHelper.createFormModel(parametriRicercaMovimentazione, false, FORM_ID), FORM_ID);

		// aggiungo la finta proprietà tipiEntita per far si che la search text dell'entità mi selezioni solo clienti e
		// fornitori
		List<TipoEntita> tipiEntita = new ArrayList<TipoEntita>();
		tipiEntita.add(TipoEntita.CLIENTE);
		tipiEntita.add(TipoEntita.FORNITORE);

		ValueModel tipiEntitaValueModel = new ValueHolder(tipiEntita);
		DefaultFieldMetadata tipiEntitaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(tipiEntitaValueModel), List.class, true, null);
		getFormModel().add("tipiEntita", tipiEntitaValueModel, tipiEntitaData);

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
				"right:pref,4dlu,fill:pref,10dlu,fill:default:grow",
				"2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,default,2dlu,fill:default:grow");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r,c");

		builder.nextRow();
		builder.setRow(2);
		builder.addPropertyAndLabel("periodo");
		builder.nextRow();
		builder.addLabel("articoloLite");
		SearchPanel searchPanel = (SearchPanel) builder.addBinding(bf.createBoundSearchText("articoloLite",
				new String[] { "codice", "descrizione" }, new String[] { "entitaLite" },
				new String[] { ArticoloSearchObject.ENTITA_KEY }), 3);
		searchPanel.getTextFields().get("codice").setColumns(10);
		searchPanel.getTextFields().get("descrizione").setColumns(26);
		builder.nextRow();

		builder.addLabel("depositoLite");
		SearchPanel searchPanelDeposito = (SearchPanel) builder.addBinding(
				bf.createBoundSearchText("depositoLite", new String[] { "codice", "descrizione" }), 3);
		searchPanelDeposito.getTextFields().get("codice").setColumns(10);
		searchPanelDeposito.getTextFields().get("descrizione").setColumns(26);
		builder.nextRow();

		builder.addLabel("entitaLite");
		SearchPanel searchPanelEntita = (SearchPanel) builder.addBinding(bf.createBoundSearchText("entitaLite",
				new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipiEntita" },
				new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }), 3);
		searchPanelEntita.getTextFields().get("codice").setColumns(10);
		searchPanelEntita.getTextFields().get("anagrafica.denominazione").setColumns(26);
		builder.nextRow();

		builder.addLabel("sedeEntita");
		Binding sedeEntitaBinding = bf.createBoundSearchText("sedeEntita", new String[] { "sede.descrizione" },
				new String[] { "entitaLite" }, new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID });
		SearchPanel searchPanelSede = (SearchPanel) builder.addBinding(sedeEntitaBinding, 3);
		searchPanelSede.getTextFields().get("sede.descrizione").setColumns(30);
		builder.nextRow();

		PluginManager pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);

		if (pluginManager.isPresente(PluginManager.PLUGIN_AGENTI)) {
			builder.addLabel("agenteLite");
			SearchPanel agenteComponents = (SearchPanel) builder.addBinding(bf.createBoundSearchText("agenteLite",
					new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipiEntitaAgente" },
					new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class), 3);

			agenteComponents.getTextFields().get("codice").setColumns(10);
			agenteComponents.getTextFields().get("anagrafica.denominazione").setColumns(26);

			builder.nextRow();
		}

		((JTextField) builder.addPropertyAndLabel("descrizioneRiga")[1]).setColumns(41);
		builder.nextRow();

		((JTextField) builder.addPropertyAndLabel("noteRiga")[1]).setColumns(41);
		builder.nextRow();

		builder.addPropertyAndLabel("righeOmaggio");

		builder.nextRow();

		builder.setLabelAttributes("r,t");
		builder.addLabel("sezioniTipoMovimento");
		builder.addBinding(bf.createBoundEnumCheckBoxList("sezioniTipoMovimento", ESezioneTipoMovimento.class,
				ListSelectionModel.MULTIPLE_INTERVAL_SELECTION, JList.HORIZONTAL_WRAP), 3);

		List<TipoAreaMagazzino> tipiAreeInDataWarehouse = getTipiAreaMagazzino();
		builder.addBinding(
				bf.createBoundCheckBoxTree("tipiAreaMagazzino", new String[] { "tipoDocumento.abilitato",
						"tipoDocumento.classeTipoDocumento" }, new ValueHolder(tipiAreeInDataWarehouse)), 5, 2, 1, 15);

		logger.debug("--> Exit createFormControl");
		return builder.getPanel();
	}

	@Override
	protected Object createNewObject() {
		ParametriRicercaMovimentazione parametriRicercaMovimentazione = new ParametriRicercaMovimentazione();
		return parametriRicercaMovimentazione;
	}

	/**
	 * Restituisco tutti i TipiAreaMagazzino; i movimenti di inventario non ci sarebberro, ma vengono aggiunti nella
	 * ricerca della movimentazione.
	 * 
	 * @return List<TipoAreaMagazzino>
	 */
	private List<TipoAreaMagazzino> getTipiAreaMagazzino() {
		if (tipiAreeMagazzino == null) {
			tipiAreeMagazzino = magazzinoDocumentoBD.caricaTipiAreaMagazzino("tipoDocumento.codice", null, true);
		}
		return tipiAreeMagazzino;
	}

	/**
	 * @param magazzinoDocumentoBD
	 *            The magazzinoDocumentoBD to set.
	 */
	public void setMagazzinoDocumentoBD(IMagazzinoDocumentoBD magazzinoDocumentoBD) {
		this.magazzinoDocumentoBD = magazzinoDocumentoBD;
	}

}
