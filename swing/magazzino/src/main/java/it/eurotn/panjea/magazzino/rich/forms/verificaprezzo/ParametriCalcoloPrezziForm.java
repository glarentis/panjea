package it.eurotn.panjea.magazzino.rich.forms.verificaprezzo;

import it.eurotn.panjea.anagrafica.documenti.domain.TipoDocumento.TipoEntita;
import it.eurotn.panjea.anagrafica.domain.ValutaAzienda;
import it.eurotn.panjea.anagrafica.domain.lite.EntitaLite;
import it.eurotn.panjea.anagrafica.rich.search.EntitaByTipoSearchObject;
import it.eurotn.panjea.anagrafica.rich.search.SedeEntitaSearchObject;
import it.eurotn.panjea.magazzino.domain.ArticoloLite;
import it.eurotn.panjea.magazzino.domain.ProvenienzaPrezzoArticolo;
import it.eurotn.panjea.magazzino.domain.TipoMezzoTrasporto;
import it.eurotn.panjea.magazzino.rich.editors.verificaprezzo.ParametriCalcoloPrezziPM;
import it.eurotn.panjea.magazzino.rich.search.ArticoloSearchObject;
import it.eurotn.panjea.plugin.PluginManager;
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

import org.springframework.binding.form.support.DefaultFieldMetadata;
import org.springframework.binding.form.support.FormModelMediatingValueModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.builder.FormLayoutFormBuilder;
import org.springframework.richclient.util.RcpSupport;

import com.jgoodies.forms.layout.FormLayout;

public class ParametriCalcoloPrezziForm extends PanjeaAbstractForm {

	private class ArticoloPropertyChange implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			ArticoloLite articoloLite = (ArticoloLite) evt.getNewValue();

			boolean visibleTipoMezzo = false;
			if (articoloLite != null && articoloLite.getProvenienzaPrezzoArticolo() != null
					&& articoloLite.getProvenienzaPrezzoArticolo() == ProvenienzaPrezzoArticolo.TIPOMEZZOZONAGEOGRAFICA) {
				visibleTipoMezzo = true;
			} else {
				getFormModel().getValueModel("tipoMezzoTrasporto").setValue(new TipoMezzoTrasporto());
			}

			tipoMezzoLabel.setVisible(visibleTipoMezzo);
			tipoMezzoSearch.setVisible(visibleTipoMezzo);
			listinoLabel.setVisible(!visibleTipoMezzo);
			listinoSearch.setVisible(!visibleTipoMezzo);
			listinoAlternativoLabel.setVisible(!visibleTipoMezzo);
			listinoAlternativoSearch.setVisible(!visibleTipoMezzo);
		}

	}

	private static final String FORMMODEL_ID = "parametriCalcoloPrezziForm";
	private static final String FORM_ID = "ParametriCalcoloPrezzi";
	private SearchPanel searchPanelArticolo;

	private JLabel tipoMezzoLabel;
	private JComponent tipoMezzoSearch;

	private JLabel listinoLabel;
	private JComponent listinoSearch;
	private JLabel listinoAlternativoLabel;
	private JComponent listinoAlternativoSearch;

	private PluginManager pluginManager;

	/**
	 * Costruttore.
	 */
	public ParametriCalcoloPrezziForm() {
		super(PanjeaFormModelHelper.createFormModel(new ParametriCalcoloPrezziPM(), false, FORMMODEL_ID), FORM_ID);
		ValueModel sediDisabilitateInRicercaValueModel = new ValueHolder(Boolean.TRUE);
		DefaultFieldMetadata sediDisabilitatemetaData = new DefaultFieldMetadata(getFormModel(),
				new FormModelMediatingValueModel(sediDisabilitateInRicercaValueModel), Boolean.class, true, null);
		getFormModel().add("sediDisabilitate", sediDisabilitateInRicercaValueModel, sediDisabilitatemetaData);

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

		this.pluginManager = RcpSupport.getBean(PluginManager.BEAN_ID);
	}

	@Override
	protected JComponent createFormControl() {
		final PanjeaSwingBindingFactory bf = (PanjeaSwingBindingFactory) getBindingFactory();
		FormLayout layout = new FormLayout("left:default, 4dlu, left:170dlu, 10dlu,right:pref, 4dlu,left:100dlu",
				"default");
		FormLayoutFormBuilder builder = new FormLayoutFormBuilder(bf, layout); // , new FormDebugPanel());
		builder.setLabelAttributes("r,c");
		builder.nextRow();
		builder.addPropertyAndLabel("data");

		builder.addLabel("codiceValuta", 5);
		Binding valutaBinding = bf.createBoundSearchText("codiceValuta", null, ValutaAzienda.class);
		SearchPanel valutaSearchPanel = (SearchPanel) builder.addBinding(valutaBinding, 7);
		valutaSearchPanel.getTextFields().get(null).setColumns(7);

		builder.nextRow();

		// ARTICOLO
		builder.addLabel("articolo", 1);
		Binding bindingArticolo = bf.createBoundSearchText("articolo", new String[] { "codice", "descrizione" },
				new String[] { "entita" }, new String[] { ArticoloSearchObject.ENTITA_KEY });
		searchPanelArticolo = (SearchPanel) builder.addBinding(bindingArticolo, 3);
		searchPanelArticolo.getTextFields().get("codice").setColumns(12);
		searchPanelArticolo.getTextFields().get("descrizione").setColumns(25);

		builder.nextRow();

		Binding bindingEntita = bf.createBoundSearchText("entita",
				new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipiEntita" },
				new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY });
		SearchPanel searchPanelEntita = (SearchPanel) bindingEntita.getControl();
		searchPanelEntita.getTextFields().get("codice").setColumns(12);
		searchPanelEntita.getTextFields().get("anagrafica.denominazione").setColumns(25);
		builder.addLabel("entita", 1);
		builder.addBinding(bindingEntita, 3);

		Binding sedeEntitaBinding = bf.createBoundSearchText("sedeEntita", new String[] { "sede.descrizione" },
				new String[] { "entita", "sediDisabilitate" },
				new String[] { SedeEntitaSearchObject.PARAMETER_ENTITA_ID,
						SedeEntitaSearchObject.PARAMETER_SEDE_DISABILITATE });
		builder.addLabel("sedeEntita", 5);
		SearchPanel searchPanelSedeEntita = (SearchPanel) builder.addBinding(sedeEntitaBinding, 7);
		searchPanelSedeEntita.getTextFields().get("sede.descrizione").setColumns(15);

		getValueModel("idZonaGeografica");

		builder.nextRow();

		builder.addLabel("codicePagamento", 1);
		Binding codPagBinding = bf.createBoundSearchText("codicePagamento", new String[] { "codicePagamento",
				"descrizione" });
		SearchPanel codPagSearchPanel = (SearchPanel) builder.addBinding(codPagBinding, 3);
		codPagSearchPanel.getTextFields().get("codicePagamento").setColumns(8);
		codPagSearchPanel.getTextFields().get("descrizione").setColumns(35);

		builder.nextRow();
		// LISTINO

		tipoMezzoLabel = builder.addLabel("tipoMezzoTrasporto", 1);
		Binding bindingTipoMezzo = bf.createBoundSearchText("tipoMezzoTrasporto", new String[] { "codice",
				"descrizione" });
		((SearchPanel) bindingTipoMezzo.getControl()).getTextFields().get("codice").setColumns(7);
		((SearchPanel) bindingTipoMezzo.getControl()).getTextFields().get("descrizione").setColumns(15);
		tipoMezzoSearch = builder.addBinding(bindingTipoMezzo, 3);

		tipoMezzoLabel.setVisible(false);
		tipoMezzoSearch.setVisible(false);

		listinoLabel = builder.addLabel("listino", 1);
		Binding bindingListino = bf.createBoundSearchText("listino", new String[] { "codice" });
		((SearchPanel) bindingListino.getControl()).getTextFields().get("codice").setColumns(15);
		listinoSearch = builder.addBinding(bindingListino, 3);

		// LISTINO ALTERNATIVO
		listinoAlternativoLabel = builder.addLabel("listinoAlternativo", 5);
		Binding bindingListinoAlternativo = bf.createBoundSearchText("listinoAlternativo", new String[] { "codice" });
		((SearchPanel) bindingListinoAlternativo.getControl()).getTextFields().get("codice").setColumns(15);
		listinoAlternativoSearch = builder.addBinding(bindingListinoAlternativo, 7);

		if (pluginManager.isPresente(PluginManager.PLUGIN_AGENTI)) {
			builder.nextRow();
			builder.addLabel("agente");
			SearchPanel searchAgente = (SearchPanel) builder.addBinding(bf.createBoundSearchText("agente",
					new String[] { "codice", "anagrafica.denominazione" }, new String[] { "tipiEntitaAgente" },
					new String[] { EntitaByTipoSearchObject.TIPI_ENTITA_LIST_KEY }, EntitaLite.class), 3);
			searchAgente.getTextFields().get("codice").setColumns(6);
		}

		getFormModel().getValueModel("articolo").addValueChangeListener(new ArticoloPropertyChange());
		return builder.getPanel();
	}

	/**
	 * focus personalizzato per il form.
	 */
	public void requestFocus() {
		searchPanelArticolo.getTextFields().get("codice").getTextField().requestFocusInWindow();
	}
}